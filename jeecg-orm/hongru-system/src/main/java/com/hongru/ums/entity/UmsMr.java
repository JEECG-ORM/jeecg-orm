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
@ApiModel("医学报告对象")
public class UmsMr extends HongRuEntity {


    @ApiModelProperty(value = "报告名称",example = "整体医学报告--基础版")
    private String name;

    @ApiModelProperty(value = "报告类型",example = "F06")
    @Dict(dicCode = "code",dictTable ="sys_category",dicText = "name")
    private String categoryCode;

    @ApiModelProperty(value = "报告版本(2:定制版 1:基础版 )",example = "2")
    @Dict(dicCode = "mr_type")
    private Integer type;

    @ApiModelProperty(value = "会员ID",example = "",readOnly = true)
    private String memberId;

    @Override
    public void save() {
        if(null==this.memberId){
        this.memberId="";
        }
        super.save();
    }


}
