package com.example.flowabledemo.service.impl;

import com.example.flowabledemo.dto.CreateProcessDefinitionLineDTO;
import com.example.flowabledemo.dto.ProcessNodeDefinitionDTO;
import com.example.flowabledemo.dto.StartHolidayTaskDTO;
import com.example.flowabledemo.service.IFlowableProcessBaselineService;
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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author wangkai
 * @description:
 * @date 2024/5/8 16:40
 */
@Service
public class FlowableProcessBaselineServiceImpl implements IFlowableProcessBaselineService {
    @Resource
    private RepositoryService repositoryService;
    @Resource
    private RuntimeService runtimeService;

    @Override
    public Integer createProcessLineDefinition(CreateProcessDefinitionLineDTO createProcessDefinitionLineDTO) {

        Process process = new Process();

        process.setId(createProcessDefinitionLineDTO.getFlowName());
        process.setName(createProcessDefinitionLineDTO.getFlowName());
        // 开始节点
        StartEvent startEvent = new StartEvent();
        startEvent.setId("StartEvent"+ UUID.randomUUID().toString().substring(0, 11));
        startEvent.setName("开始");
        process.addFlowElement(startEvent);

        FlowElement previousElement = startEvent;

        ServiceTask startServiceTask = new ServiceTask();
        startServiceTask.setId("StartServiceTask"+UUID.randomUUID().toString().substring(0,11));
        startServiceTask.setName("提交");
        startServiceTask.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_CLASS);
        startServiceTask.setImplementation(StartServiceTask.class.getName());
        process.addFlowElement(startServiceTask);

        SequenceFlow startServiceSequence = new SequenceFlow();
        startServiceSequence.setId("StartServiceSequence"+UUID.randomUUID().toString().substring(0,11));
        startServiceSequence.setSourceRef(previousElement.getId());
        startServiceSequence.setTargetRef(startServiceTask.getId());
        process.addFlowElement(startServiceSequence);
        previousElement = startServiceTask;
        for (ProcessNodeDefinitionDTO form : createProcessDefinitionLineDTO.getProcessNodeDefinitionDTOS()) {
            if ("userTask".equals(form.getNodeType())){
                UserTask userTask = new UserTask();
                userTask.setId("UserTask"+ UUID.randomUUID().toString().substring(0, 11));
                userTask.setName(form.getNodeName());
                userTask.setAssignee("${" + form.getNodeName() + "Assignee}");
                process.addFlowElement(userTask);

                SequenceFlow sequenceFlow = new SequenceFlow();
                sequenceFlow.setSourceRef(previousElement.getId());
                sequenceFlow.setTargetRef(userTask.getId());
                process.addFlowElement(sequenceFlow);
                previousElement = userTask;
            }
        }

        EndEvent endEvent = new EndEvent();
        endEvent.setId("endEvent"+UUID.randomUUID().toString().substring(0,11));
        endEvent.setName("结束");
        process.addFlowElement(endEvent);

        SequenceFlow sequenceFlowToEnd = new SequenceFlow();
        sequenceFlowToEnd.setSourceRef(previousElement.getId());
        sequenceFlowToEnd.setTargetRef(endEvent.getId());
        process.addFlowElement(sequenceFlowToEnd);
        BpmnModel bpmnModel = new BpmnModel();
        bpmnModel.addProcess(process);
        if (bpmnModel == null) {
            throw new IllegalArgumentException("bpmnModel cannot be null");
        }
        new BpmnAutoLayout(bpmnModel).execute();

        Deployment deployment = repositoryService.createDeployment()
                .addBpmnModel(createProcessDefinitionLineDTO.getFlowName() + ".bpmn20.xml", bpmnModel)
                .name(createProcessDefinitionLineDTO.getFlowName())
                .category(createProcessDefinitionLineDTO.getCategory())
                .deploy();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .singleResult();

        repositoryService.setProcessDefinitionCategory(
                processDefinition.getId(), createProcessDefinitionLineDTO.getCategory());
        return 1;
    }

    @Override
    public String startHolidayInstance(StartHolidayTaskDTO startHolidayTaskDTO) {
        Map<String, Object> variables = new HashMap<>();

        variables.put("HRReviewAssignee", startHolidayTaskDTO.getApprovers().get(0));
        variables.put("ManagerReviewAssignee", startHolidayTaskDTO.getApprovers().get(1));
        variables.put("processType","HolidayApplicationProcess");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(startHolidayTaskDTO.getProcessId(), variables);
        // 保存请假相关信息以及实例到数据库
        // save();
        return processInstance.getId();
    }
}
