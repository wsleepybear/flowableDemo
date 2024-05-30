package com.example.flowabledemo.service.impl;

import com.example.flowabledemo.dto.*;
import com.example.flowabledemo.service.IFlowableProcessBranchService;
import com.example.flowabledemo.task.StartServiceTask;
import org.flowable.bpmn.BpmnAutoLayout;
import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author wangkai
 * @description:
 * @date 2024/5/29 10:20
 */
@Service
public class FlowableProcessBranchServiceImpl implements IFlowableProcessBranchService {
    @Resource
    private RepositoryService repositoryService;
    @Resource
    private RuntimeService runtimeService;
    @Override
    public void createProcessBranchDefinition(CreateProcessDefinitionBranchDTO createProcessDefinitionBranchDTO) {
        Process process = new Process();
        process.setId(createProcessDefinitionBranchDTO.getFlowName());
        process.setName(createProcessDefinitionBranchDTO.getFlowName());

        // 开始节点
        StartEvent startEvent = new StartEvent();
        startEvent.setId("StartEvent"+ UUID.randomUUID().toString().substring(0, 11));
        startEvent.setName("开始");
        process.addFlowElement(startEvent);

        // 开始服务任务
        ServiceTask startServiceTask = new ServiceTask();
        startServiceTask.setId("StartServiceTask"+UUID.randomUUID().toString().substring(0,11));
        startServiceTask.setName("提交");
        startServiceTask.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_CLASS);
        startServiceTask.setImplementation(StartServiceTask.class.getName());
        process.addFlowElement(startServiceTask);

        // 开始序列流
        SequenceFlow startServiceSequence = new SequenceFlow();
        startServiceSequence.setId("StartServiceSequence"+UUID.randomUUID().toString().substring(0,11));
        startServiceSequence.setSourceRef(startEvent.getId());
        startServiceSequence.setTargetRef(startServiceTask.getId());
        process.addFlowElement(startServiceSequence);

        Map<String, Gateway> gateways = new HashMap<>();
        for (GatewayDefinitionDTO gatewayDTO : createProcessDefinitionBranchDTO.getGatewayDefinitionDTOS()) {
            Gateway gateway;
            if ("ExclusiveGateway".equals(gatewayDTO.getType())) {
                gateway = new ExclusiveGateway();
            } else if ("ParallelGateway".equals(gatewayDTO.getType())) {
                gateway = new ParallelGateway();
            } else {
                throw new IllegalArgumentException("Unsupported gateway type: " + gatewayDTO.getType());
            }
            gateway.setId(gatewayDTO.getId()); // 使用前端提供的ID
            gateway.setName(gatewayDTO.getName());
            process.addFlowElement(gateway);

            gateways.put(gatewayDTO.getId(), gateway); // 将网关添加到映射中
        }

        // 创建所有的用户任务

        for (ProcessNodeDefinitionDTO nodeDTO : createProcessDefinitionBranchDTO.getProcessNodeDefinitionDTOS()) {
            UserTask userTask = new UserTask();
            userTask.setId(nodeDTO.getNodeId()); // 使用前端提供的ID
            userTask.setName(nodeDTO.getNodeName());
            userTask.setAssignee("${" + nodeDTO.getNodeName() + "Assignee}");

            MultiInstanceLoopCharacteristics loopCharacteristics = new MultiInstanceLoopCharacteristics();
            loopCharacteristics.setSequential(false);
            loopCharacteristics.setInputDataItem(nodeDTO.getNodeName() + "Assignees");
            loopCharacteristics.setElementVariable( nodeDTO.getNodeName() + "Assignee");

            String completionCondition = nodeDTO.getNodeCategory() == 1 ?
                    "${nrOfCompletedInstances == nrOfInstances}" :
                    "${nrOfCompletedInstances >= 1}";
            loopCharacteristics.setCompletionCondition(completionCondition);

            userTask.setLoopCharacteristics(loopCharacteristics);
            process.addFlowElement(userTask);
        }
        // 结束节点
        EndEvent endEvent = new EndEvent();
        endEvent.setId("endEvent"+UUID.randomUUID().toString().substring(0,11));
        endEvent.setName("结束");
        process.addFlowElement(endEvent);

        // 创建所有的序列流
        for (SequenceFlowDefinitionDTO sequenceFlowDTO : createProcessDefinitionBranchDTO.getSequenceFlowDefinitionDTOS()) {
            SequenceFlow sequenceFlow = new SequenceFlow();
            sequenceFlow.setId(sequenceFlowDTO.getId()); // 使用前端提供的ID

            // 判断并设置sourceRef
            if ("StartEvent".equals(sequenceFlowDTO.getSourceRef())){
                sequenceFlow.setSourceRef(startServiceTask.getId());
            } else {
                sequenceFlow.setSourceRef(sequenceFlowDTO.getSourceRef()); // 使用映射中的网关ID
            }

            // 判断并设置targetRef
            if ("EndEvent".equals(sequenceFlowDTO.getTargetRef())){
                sequenceFlow.setTargetRef(endEvent.getId());
            } else {
                sequenceFlow.setTargetRef(sequenceFlowDTO.getTargetRef()); // 使用映射中的用户任务ID
            }

            sequenceFlow.setConditionExpression(sequenceFlowDTO.getConditionExpression());
            process.addFlowElement(sequenceFlow);
        }


        // 创建BPMN模型
        BpmnModel bpmnModel = new BpmnModel();
        bpmnModel.addProcess(process);
        deploy(bpmnModel, createProcessDefinitionBranchDTO);
    }

    @Override
    public String startHolidayInstance(StartHolidayTaskDTO startHolidayTaskDTO) {
        Map<String, Object> variables = new HashMap<>();

        variables.put("HRReviewAssignees", startHolidayTaskDTO.getHRReviews());
        variables.put("ManagerReviewAssignees", startHolidayTaskDTO.getManagerReviews());
        variables.put("processType","HolidayApplicationProcess");
        long leaveDays = ChronoUnit.DAYS.between(startHolidayTaskDTO.getStartDate().toInstant(), startHolidayTaskDTO.getEndDate().toInstant());
        variables.put("leaveDays", leaveDays);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(startHolidayTaskDTO.getProcessId(), variables);
        // 保存请假相关信息以及实例到数据库
        // save();
        return processInstance.getId();
    }

    public ProcessDefinition deploy(BpmnModel bpmnModel, CreateProcessDefinitionBranchDTO createProcessDefinitionBranchDTO) {
        new BpmnAutoLayout(bpmnModel).execute();

        // 部署流程定义
        Deployment deployment = repositoryService.createDeployment()
                .addBpmnModel(createProcessDefinitionBranchDTO.getFlowName() + ".bpmn20.xml", bpmnModel)
                .name(createProcessDefinitionBranchDTO.getFlowName())
                .category(createProcessDefinitionBranchDTO.getCategory())
                .deploy();

        // 设置流程定义的类别
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .singleResult();
        repositoryService.setProcessDefinitionCategory(
                processDefinition.getId(), createProcessDefinitionBranchDTO.getCategory());

        return processDefinition;
    }
}
