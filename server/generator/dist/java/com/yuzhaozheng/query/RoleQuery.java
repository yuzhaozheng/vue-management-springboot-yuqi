package com.yuzhaozheng.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("角色查询条件")
public class RoleQuery extends PageQuery {

    @ApiModelProperty(value = "角色ID", notes = "角色ID")
    private String roleId;
    @ApiModelProperty(value = "名称", notes = "名称")
    private String name;
    @ApiModelProperty(value = "排序", notes = "排序")
    private String sort;
    @ApiModelProperty(value = "备注", notes = "备注")
    private String remark;
}
