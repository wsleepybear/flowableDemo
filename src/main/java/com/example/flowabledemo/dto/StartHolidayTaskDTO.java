package com.example.flowabledemo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.flowable.bpmn.model.Process;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wangkai
 * @description:
 * @date 2024/5/13 11:01
 */
@Data
public class StartHolidayTaskDTO {


    @ApiModelProperty(value = "员工ID")
    private String employeeId;

    @ApiModelProperty(value = "请假开始日期")
    private Date startDate;

    @ApiModelProperty(value = "请假结束日期")
    private Date endDate;

    @ApiModelProperty(value = "请假原因")
    private String reason;

    @ApiModelProperty(value = "请假类型，如病假、年假等")
    private String leaveType;

    @ApiModelProperty(value = "HR审批人")
    private List<String> HRReviews;

    @ApiModelProperty(value = "部门领导审批人")
    private List<String> ManagerReviews;

    @ApiModelProperty
    private List<String> approvers;

    @ApiModelProperty(value = "流程id")
    private String processId;

}
