package com.example.flowabledemo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wangkai
 * @description:
 * @date 2024/5/29 10:33
 */

@Data
public class SequenceFlowDefinitionDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "序列流ID")
    private String id;

    @ApiModelProperty(value = "源节点ID")
    private String sourceRef;

    @ApiModelProperty(value = "目标节点ID")
    private String targetRef;

    @ApiModelProperty(value = "条件表达式")
    private String conditionExpression;
}
