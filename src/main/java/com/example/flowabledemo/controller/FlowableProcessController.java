package com.example.flowabledemo.controller;

import com.example.flowabledemo.dto.CreateProcessDefinitionLineDTO;
import com.example.flowabledemo.dto.StartHolidayTaskDTO;
import com.example.flowabledemo.service.IFlowableProcessBaselineService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author wangkai
 * @description:
 * @date 2024/5/8 16:37
 */
@RestController
@RequestMapping("/flowableProcess")
public class FlowableProcessController {
    @Resource
    private IFlowableProcessBaselineService flowableProcessService;
    @PostMapping("/createProcessDefinition")
    public String createProcessDefinition(@RequestBody CreateProcessDefinitionLineDTO createProcessDefinitionLineDTO) {
        flowableProcessService.createProcessLineDefinition(createProcessDefinitionLineDTO);
        return "success";
    }

    @PostMapping("/startHolidayProcess")
    public String startProcess(@RequestBody StartHolidayTaskDTO startHolidayTaskDTO) {
        flowableProcessService.startHolidayInstance(startHolidayTaskDTO);
        return "success";
    }



}
