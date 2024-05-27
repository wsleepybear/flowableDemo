package com.example.flowabledemo.task;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

import java.util.Map;

/**
 * @author wangkai
 * @description:
 * @date 2024/5/9 16:00
 */

public class StartServiceTask implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        Map<String, Object> variables = execution.getVariables();
        String processType = (String) variables.get("processType");

        switch (processType) {
            case "HolidayApplicationProcess":
                // 获取请假流程的特定变量
                String startUserId = (String) variables.get("startUserId");
                // 执行请假流程的业务逻辑
                break;
            case "ApprovalProcess":
                // 获取审批流程的特定变量
                String approvalUserId = (String) variables.get("approvalUserId");
                // 执行审批流程的业务逻辑
                break;
            // 其他流程类型
            default:
                // 执行默认的业务逻辑
                break;
        }
    }
}
