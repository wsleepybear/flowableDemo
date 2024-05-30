package com.example.flowabledemo;

import com.example.flowabledemo.dto.*;
import com.example.flowabledemo.service.impl.FlowableProcessBaselineServiceImpl;
import com.example.flowabledemo.service.impl.FlowableProcessBranchServiceImpl;
import liquibase.pro.packaged.H;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author wangkai
 * @description:
 * @date 2024/5/29 16:30
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class FlowableProcessServiceBranchTest {
    @Resource
    private FlowableProcessBranchServiceImpl flowableProcessService;

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private TaskService taskService;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void createProcessDefinitionBranch() {

        // Arrange
        CreateProcessDefinitionBranchDTO createProcessDefinitionBranchDTO = new CreateProcessDefinitionBranchDTO();
        createProcessDefinitionBranchDTO.setFlowName("HolidayApplicationProcessBranch");
        createProcessDefinitionBranchDTO.setCategory("Holiday");
        createProcessDefinitionBranchDTO.setProcessNodeDefinitionDTOS(new ArrayList<>());
        createProcessDefinitionBranchDTO.setGatewayDefinitionDTOS(new ArrayList<>());
        createProcessDefinitionBranchDTO.setSequenceFlowDefinitionDTOS(new ArrayList<>());
        // HR审批
        ProcessNodeDefinitionDTO HRReview = new ProcessNodeDefinitionDTO();
        HRReview.setNodeId("HRReview"+ UUID.randomUUID().toString().substring(0,11));
        HRReview.setNodeName("HRReview");
        HRReview.setNodeType("UserTask");
        HRReview.setNodeCategory(0);
        createProcessDefinitionBranchDTO.getProcessNodeDefinitionDTOS().add(HRReview);
        // 主管审批
        ProcessNodeDefinitionDTO ManagerReview = new ProcessNodeDefinitionDTO();
        ManagerReview.setNodeId("ManagerReview"+ UUID.randomUUID().toString().substring(0,11));
        ManagerReview.setNodeName("ManagerReview");
        ManagerReview.setNodeType("UserTask");
        ManagerReview.setNodeCategory(0);
        createProcessDefinitionBranchDTO.getProcessNodeDefinitionDTOS().add(ManagerReview);

        // 判断是否需要高级经理审批的排他网关
        GatewayDefinitionDTO ManagerReviewGateway = new GatewayDefinitionDTO();
        ManagerReviewGateway.setId("ManagerReviewGateway"+UUID.randomUUID().toString().substring(0,11));
        ManagerReviewGateway.setName("ManagerReviewGateway");
        ManagerReviewGateway.setType("ExclusiveGateway");
        createProcessDefinitionBranchDTO.getGatewayDefinitionDTOS().add(ManagerReviewGateway);

        // 开始服务连接hr审批
        SequenceFlowDefinitionDTO startToHRReview = new SequenceFlowDefinitionDTO();
        startToHRReview.setId("startToHRReview"+UUID.randomUUID().toString().substring(0,11));
        startToHRReview.setSourceRef("StartEvent");
        startToHRReview.setTargetRef(HRReview.getNodeId());
        createProcessDefinitionBranchDTO.getSequenceFlowDefinitionDTOS().add(startToHRReview);

        //hr审批连接排他网关
        SequenceFlowDefinitionDTO HRReviewToManagerReviewGateway = new SequenceFlowDefinitionDTO();
        HRReviewToManagerReviewGateway.setId("HRReviewToManagerReviewGateway"+UUID.randomUUID().toString().substring(0,11));
        HRReviewToManagerReviewGateway.setSourceRef(HRReview.getNodeId());
        HRReviewToManagerReviewGateway.setTargetRef(ManagerReviewGateway.getId());
        createProcessDefinitionBranchDTO.getSequenceFlowDefinitionDTOS().add(HRReviewToManagerReviewGateway);

        //排他网关连接结束节点
        SequenceFlowDefinitionDTO ManagerReviewGatewayToEnd = new SequenceFlowDefinitionDTO();
        ManagerReviewGatewayToEnd.setId("ManagerReviewGatewayToEnd"+UUID.randomUUID().toString().substring(0,11));
        ManagerReviewGatewayToEnd.setSourceRef(ManagerReviewGateway.getId());
        ManagerReviewGatewayToEnd.setTargetRef("EndEvent");
        ManagerReviewGatewayToEnd.setConditionExpression("${leaveDays <= 10}");
        createProcessDefinitionBranchDTO.getSequenceFlowDefinitionDTOS().add(ManagerReviewGatewayToEnd);

        //排他网关连接主管审批
        SequenceFlowDefinitionDTO ManagerReviewGatewayToManagerReview = new SequenceFlowDefinitionDTO();
        ManagerReviewGatewayToManagerReview.setId("ManagerReviewGatewayToManagerReview"+UUID.randomUUID().toString().substring(0,11));
        ManagerReviewGatewayToManagerReview.setSourceRef(ManagerReviewGateway.getId());
        ManagerReviewGatewayToManagerReview.setTargetRef(ManagerReview.getNodeId());
        ManagerReviewGatewayToManagerReview.setConditionExpression("${leaveDays > 10}");
        createProcessDefinitionBranchDTO.getSequenceFlowDefinitionDTOS().add(ManagerReviewGatewayToManagerReview);

        //主管审批连接
        SequenceFlowDefinitionDTO ManagerReviewToEnd = new SequenceFlowDefinitionDTO();
        ManagerReviewToEnd.setId("ManagerReviewToEnd"+UUID.randomUUID().toString().substring(0,11));
        ManagerReviewToEnd.setSourceRef(ManagerReview.getNodeId());
        ManagerReviewToEnd.setTargetRef("EndEvent");
        createProcessDefinitionBranchDTO.getSequenceFlowDefinitionDTOS().add(ManagerReviewToEnd);
        flowableProcessService.createProcessBranchDefinition(createProcessDefinitionBranchDTO);
    }
    @Test
    public void startProcessInstanceBranch() {
        StartHolidayTaskDTO mockStartHolidayTaskDTO = new StartHolidayTaskDTO();
        mockStartHolidayTaskDTO.setEmployeeId("EmployeeId");
        mockStartHolidayTaskDTO.setStartDate(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date()); // 设置当前日期
        calendar.add(Calendar.DATE, 20); // 在当前日期基础上添加20天
        mockStartHolidayTaskDTO.setEndDate(calendar.getTime()); // 设置新的结束日期
        mockStartHolidayTaskDTO.setReason("Reason");
        mockStartHolidayTaskDTO.setLeaveType("Type");
        mockStartHolidayTaskDTO.setHRReviews(Arrays.asList("kermit", "gonzo","scooter"));
        mockStartHolidayTaskDTO.setManagerReviews(Arrays.asList("fozzie", "piggy"));

        mockStartHolidayTaskDTO.setProcessId("HolidayApplicationProcessBranch");
        String holidayInstanceId = flowableProcessService.startHolidayInstance(mockStartHolidayTaskDTO);

        List<Task> tasks = taskService.createTaskQuery().processInstanceId(holidayInstanceId).active().list();
        // 打印task
        for (Task task : tasks) {
            System.out.println(task);
        }
    }
    @Test
    public void completeTask() {
        //查询正在进行的任务数量
        long count = taskService.createTaskQuery().count();
        System.out.printf("Number of tasks: %d\n", count);
        String hrReview = "";
        List<Task> tasks = taskService.createTaskQuery().taskName("HRReview").list();
        if (!tasks.isEmpty()) {
            hrReview = tasks.get(0).getId(); // 其中一个hr完成审批即通过
        }
        taskService.complete(hrReview);
        count = taskService.createTaskQuery().count();
        System.out.printf("Number of tasks: %d\n", count);
        String managerReview = "";
        tasks = taskService.createTaskQuery().taskName("ManagerReview").list();
        if (!tasks.isEmpty()) {
            managerReview = tasks.get(0).getId();  // 其中一个主管完成审批即通过
        }
        taskService.complete(managerReview);
        count = taskService.createTaskQuery().count();
        System.out.printf("Number of tasks: %d\n", count);

    }

}
