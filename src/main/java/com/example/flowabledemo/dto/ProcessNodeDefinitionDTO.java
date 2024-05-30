package com.example.flowabledemo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class ProcessNodeDefinitionDTO implements Serializable {
    private static final long serialVersionUID = -1916624505173789546L;

    @ApiModelProperty(value = "节点定义ID")
    private String nodeId;

    @ApiModelProperty(value = "节点定义名称")
    private String nodeName;

    @ApiModelProperty(value = "节点类型")
    private String nodeType;
    @ApiModelProperty(value = "会签或者或签",notes = "0:或签 1:会签")
    private Integer nodeCategory;


}
