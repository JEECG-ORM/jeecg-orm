package com.hongru.cms.entity;

import com.hongru.ebean.HongRuEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import javax.persistence.Entity;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.hongru.aspect.annotation.Dict;
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
@ApiModel("健康课堂对象")
public class CmsCourse extends HongRuEntity {


    @ApiModelProperty(value = "分类",example = "F02")
    @Dict(dicCode = "code",dictTable ="sys_category",dicText = "name")
    private String categoryCode;

    @ApiModelProperty(value = "课程名称",example = "专家教您如何守护孩子脊柱健康")
    private String name;

    @ApiModelProperty(value = "课程简介",example = "专家教您如何守护孩子脊柱健康")
    private String intro;

    @ApiModelProperty(value = "课程地址",example = "北京 · 双井碧朗湾康养中心")
    private String address;

    @ApiModelProperty(value = "课程封面",example = "")
    private String cover;

    @ApiModelProperty(value = "报名开始日期",example = "")
    private String bmStartDate;

    @ApiModelProperty(value = "报名截止日期",example = "")
    private String bmEndDate;

    @ApiModelProperty(value = "课程开始时间",example = "")
    private String courseStartDate;

    @ApiModelProperty(value = "课程结束时间",example = "")
    private String courseEndDate;

    @ApiModelProperty(value = "课堂人数",example = "")
    private Integer courseNum;

    @ApiModelProperty(value = "咨询电话",example = "400-84576486")
    private String phone;

    @ApiModelProperty(value = "是否热门(0:否 1:是 )",example = "0",readOnly = true)
    @Dict(dicCode = "is_open")
    private Boolean isHot;

    @ApiModelProperty(value = "课程详情",example = "")
    private String detail;

    @ApiModelProperty(value = "课程回顾",example = "")
    private String review;

    @ApiModelProperty(value = "课程总结",example = "")
    private String summary;

    @ApiModelProperty(value = "是否发布(0:否 1:是 )",example = "0",readOnly = true)
    @Dict(dicCode = "is_open")
    private Boolean status;

    @Override
    public void save() {
        if(null==this.isHot){
        this.isHot=0==1;
        }
        if(null==this.status){
        this.status=1==1;
        }
        super.save();
    }


}
