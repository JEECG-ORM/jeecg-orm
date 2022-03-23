package com.hongru.cms.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hongru.ebean.HongRuEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
* @Description
* @Copyright (c) 1998-2022 北京新鸿儒世纪网络技术有限公司 All Rights Reserved.
* @Url https://www.xinhongru.com
* @ClassName LbsController
* @Author salter <salter@vip.163.com>
* @Version V1.0.0
* @Since 1.0
* @Date 2022/1/26 15:12
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@ApiModel("栏目对象")
public class CmsChannel extends HongRuEntity {

    @ApiModelProperty(value = "父ID",example = "0")
    private String pid;
    @ApiModelProperty(value = "栏目名称",example = "1")
    private String name;
    @ApiModelProperty(value = "栏目编码",example = "1")
    private String code;
    @ApiModelProperty(value = "层级",example = "0")
    private Integer level;
    @ApiModelProperty(value = "是否有子级",example = "0")
    private Boolean hasChild;
    @Transient
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<CmsChannel> children = new ArrayList<>();
    @Override
    public void save() {
        if(null==this.pid){
        this.pid="0";
        }
        if(null==this.hasChild){
        this.hasChild=0==1;
        }
        super.save();
    }


}
