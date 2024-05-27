package com.example.flowabledemo.service;

import com.example.flowabledemo.dto.CreateProcessDefinitionLineDTO;
import com.example.flowabledemo.dto.StartHolidayTaskDTO;

/**
 * @author wangkai
 * @description:
 * @date 2024/5/8
 */
public interface IFlowableProcessBaselineService {
    /**
     * 创建流程定义
     *
     * @param createProcessDefinitionLineDTO
     * @return
     */
    Integer createProcessLineDefinition(CreateProcessDefinitionLineDTO createProcessDefinitionLineDTO);

    /**
     * 开启假期实例
     * @param startHolidayTaskDTO
     */
    String startHolidayInstance(StartHolidayTaskDTO startHolidayTaskDTO);


}
