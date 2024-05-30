package com.example.flowabledemo.service;

import com.example.flowabledemo.dto.CreateProcessDefinitionBranchDTO;
import com.example.flowabledemo.dto.StartHolidayTaskDTO;

/**
 * @author wangkai
 * @description:
 * @date 2024/5/29
 */
public interface IFlowableProcessBranchService {
    void createProcessBranchDefinition(CreateProcessDefinitionBranchDTO createProcessDefinitionBranchDTO);
    String startHolidayInstance(StartHolidayTaskDTO startHolidayTaskDTO);
}
