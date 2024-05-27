package com.example.flowabledemo;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SelectDemoTests {
    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;
    @Test
    public void selectProcess() {
        //根据流程定义的key查询流程定义
        ProcessDefinition processDefinition1 = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("myProcess").singleResult();

        //查询所有流程定义
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().list();
        for (ProcessDefinition processDefinition : processDefinitions) {
            System.out.println("Process definition ID: " + processDefinition.getId());
            System.out.println("Process definition Name: " + processDefinition.getName());
            System.out.println("Process definition Key: " + processDefinition.getKey());
            System.out.println("Process definition Version: " + processDefinition.getVersion());
            System.out.println("--------------------------------------------------");
        }

    }

    @Test
    public void selectTask(){
        //查询所有任务
        List<Task> tasks = taskService.createTaskQuery().list();
        for (Task task : tasks) {
            System.out.println("Task ID: " + task.getId());
            System.out.println("Task Name: " + task.getName());
            System.out.println("Task Assignee: " + task.getAssignee());
            System.out.println("Task Create Time: " + task.getCreateTime());
            System.out.println("--------------------------------------------------");
        }
        //查询任务数量
        System.out.println("Number of tasks : " + taskService.createTaskQuery().count());
    }

}
