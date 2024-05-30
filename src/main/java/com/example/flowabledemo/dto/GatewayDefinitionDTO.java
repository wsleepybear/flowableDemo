package com.example.flowabledemo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author wangkai
 * @description:
 * @date 2024/5/8 17:43
 */

@Data
public class GatewayDefinitionDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "节点ID")
    private String id;

    @ApiModelProperty(value = "节点名称")
    private String name;

    @ApiModelProperty(value = "节点类型", notes = "ExclusiveGateway或ParallelGateway")
    private String type;
}
