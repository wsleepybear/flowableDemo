package com.example.flowabledemo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wangkai
 * @description:
 * @date 2024/5/8 17:43
 */

@Data
public class GatewayDefinitionDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "网关名称")
    private String gatewayName;

    @ApiModelProperty(value = "网关类型" ,notes="Exclusive、Parallel或Inclusive")
    private String gatewayType;

    @ApiModelProperty(value = "网关条件", notes = "这是一个基于流程变量的布尔表达式，用于决定流程的走向。")
    private String gatewayCondition;

    @ApiModelProperty(value = "目标节点")
    private String targetNode;
}
