package com.example.flowabledemo;

import com.example.flowabledemo.dto.CreateProcessDefinitionLineDTO;
import com.example.flowabledemo.dto.ProcessNodeDefinitionDTO;
import com.example.flowabledemo.dto.StartHolidayTaskDTO;
import com.example.flowabledemo.service.impl.FlowableProcessBaselineServiceImpl;
import org.flowable.engine.TaskService;
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
 * @date 2024/5/9 17:41
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class FlowableProcessServiceSimpleLineTest {
    @Resource
    private FlowableProcessBaselineServiceImpl flowableProcessService;


    @Resource
    private TaskService taskService;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createProcessDefinitionBaseline() {
        // Arrange
        CreateProcessDefinitionLineDTO createProcessDefinitionLineDTO = new CreateProcessDefinitionLineDTO();
        createProcessDefinitionLineDTO.setFlowName("HolidayApplicationProcess");
        createProcessDefinitionLineDTO.setCategory("Holiday");

        ProcessNodeDefinitionDTO hrReview = new ProcessNodeDefinitionDTO();
        hrReview.setNodeName("HRReview");
        hrReview.setNodeType("userTask");

        ProcessNodeDefinitionDTO ManagerReview = new ProcessNodeDefinitionDTO();
        ManagerReview.setNodeName("ManagerReview");
        ManagerReview.setNodeType("userTask");

        createProcessDefinitionLineDTO.setProcessNodeDefinitionDTOS(Arrays.asList(hrReview, ManagerReview));
        // Act
        flowableProcessService.createProcessLineDefinition(createProcessDefinitionLineDTO);
    }
    @Test
    public void startProcessInstanceBaseline() {
        StartHolidayTaskDTO mockStartHolidayTaskDTO = new StartHolidayTaskDTO();
        mockStartHolidayTaskDTO.setEmployeeId("EmployeeId");
        mockStartHolidayTaskDTO.setStartDate(new Date());
        mockStartHolidayTaskDTO.setEndDate(new Date());
        mockStartHolidayTaskDTO.setReason("Reason");
        mockStartHolidayTaskDTO.setLeaveType("Type");
        mockStartHolidayTaskDTO.setApprovers(Arrays.asList("kermit", "gonzo"));
        mockStartHolidayTaskDTO.setProcessId("HolidayApplicationProcess");
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
        String hrReview = taskService.createTaskQuery().taskName("HRReview").singleResult().getId();
        taskService.complete(hrReview);
        count = taskService.createTaskQuery().count();
        System.out.printf("Number of tasks: %d\n", count);
        String managerReview = taskService.createTaskQuery().taskName("ManagerReview").singleResult().getId();
        taskService.complete(managerReview);
        count = taskService.createTaskQuery().count();
        System.out.printf("Number of tasks: %d\n", count);

    }


}
