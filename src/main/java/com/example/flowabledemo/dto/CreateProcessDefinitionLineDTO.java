package com.example.flowabledemo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CreateProcessDefinitionLineDTO implements Serializable {
    private static final long serialVersionUID = 8721595101258489619L;

    @ApiModelProperty(value = "流程定义名称")
    private String flowName;

    @ApiModelProperty(value = "流程类型",notes = "请假,报销等")
    private String category;

    @ApiModelProperty(value = "流程节点定义")
    private List<ProcessNodeDefinitionDTO> processNodeDefinitionDTOS;

}
