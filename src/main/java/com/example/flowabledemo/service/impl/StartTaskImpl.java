package com.example.flowabledemo.service.impl;

import com.example.flowabledemo.dto.StartHolidayTaskDTO;
import com.example.flowabledemo.service.IStartTask;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangkai
 * @description:
 * @date 2024/5/15 11:10
 */

public class StartTaskImpl implements IStartTask {
    @Resource
    private RuntimeService runtimeService;

    @Override
    public void startHolidayTask(StartHolidayTaskDTO startHolidayTaskDTO) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("approvers", startHolidayTaskDTO.getApprovers());
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(startHolidayTaskDTO.getProcessId(), variables);
        //保存流程实例id与请假任务到数据库，关联用户
    }
}
