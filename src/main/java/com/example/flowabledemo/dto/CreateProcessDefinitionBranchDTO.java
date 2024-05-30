package com.example.flowabledemo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CreateProcessDefinitionBranchDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "流程名称")
    private String flowName;

    @ApiModelProperty(value = "流程类别")
    private String category;

    @ApiModelProperty(value = "网关定义")
    private List<GatewayDefinitionDTO> gatewayDefinitionDTOS;

    @ApiModelProperty(value = "用户任务定义")
    private List<ProcessNodeDefinitionDTO> processNodeDefinitionDTOS;

    @ApiModelProperty(value = "序列流定义")
    private List<SequenceFlowDefinitionDTO> sequenceFlowDefinitionDTOS;
}
