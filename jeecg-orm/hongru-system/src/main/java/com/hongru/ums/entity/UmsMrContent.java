package com.hongru.ums.entity;

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
@ApiModel("报告内容对象")
public class UmsMrContent extends HongRuEntity {


    @ApiModelProperty(value = "标题",example = "综合检测分析")
    private String title;

    @ApiModelProperty(value = "内容",example = "")
    private String detailCms;

    @ApiModelProperty(value = "报告ID",example = "",readOnly = true)
    private String mrId;

    @Override
    public void save() {
        if(null==this.mrId){
        this.mrId="";
        }
        super.save();
    }


}
