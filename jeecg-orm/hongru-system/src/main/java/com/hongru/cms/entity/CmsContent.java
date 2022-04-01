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
@ApiModel("内容对象")
public class CmsContent extends HongRuEntity {


    @ApiModelProperty(value = "栏目编码",example = "C01",readOnly = true)
    private String channelCode;

    @ApiModelProperty(value = "标题",example = "首页")
    private String title;

    @ApiModelProperty(value = "副标题",example = "0")
    private String subTitle;

    @ApiModelProperty(value = "简介",example = "0")
    private String intro;

    @ApiModelProperty(value = "图片",example = "0")
    private String image;

    @ApiModelProperty(value = "文件",example = "0")
    private String file;

    @ApiModelProperty(value = "富文本",example = "0")
    private String detailCms;

    @ApiModelProperty(value = "路由ID",example = "0")
    private String routerId;

    @ApiModelProperty(value = "数据ID",example = "0")
    private String dataId;

    @ApiModelProperty(value = "状态",example = "1",readOnly = true)
    private Boolean status;

    @ApiModelProperty(value = "分类",example = "F01")
    private String categoryCode;

    @Override
    public void save() {
        if(null==this.channelCode){
        this.channelCode="C01";
        }
        if(null==this.status){
        this.status=1==1;
        }
        super.save();
    }


}
