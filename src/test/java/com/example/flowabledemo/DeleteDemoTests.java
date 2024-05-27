package com.example.flowabledemo;

import com.example.flowabledemo.service.impl.FlowableProcessBaselineServiceImpl;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wangkai
 * @description:
 * @date 2024/5/10 16:29
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class DeleteDemoTests {
    @Resource
    private TaskService taskService;
    @Resource
    private FlowableProcessBaselineServiceImpl flowableProcessService;

    @Resource
    private RepositoryService repositoryService;
    @Test
    @DisplayName("DeleteAllProcessDefinition")
    public void DeleteAllProcessDefinition() {
        // Arrange
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().list();
        for (ProcessDefinition processDefinition : processDefinitions) {
            if (processDefinition != null) {
                repositoryService.deleteDeployment(processDefinition.getDeploymentId(), true);
            }
        }
    }

    @Test
    public void DeleteAllTask(){
        //查询所有任务
        List<Task> tasks = taskService.createTaskQuery().list();
        for (Task task : tasks) {
            taskService.complete(task.getId());
        }
    }

}
