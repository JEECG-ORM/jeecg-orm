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
@ApiModel("健康课堂报名信息对象")
public class CmsCourseApply extends HongRuEntity {


    @ApiModelProperty(value = "健康课堂ID",example = "")
    private String courseId;

    @ApiModelProperty(value = "姓名",example = "")
    private String name;

    @ApiModelProperty(value = "年龄",example = "")
    private String age;

    @ApiModelProperty(value = "性别(1:男 0:未知 2:女 )",example = "1")
    @Dict(dicCode = "sex")
    private String sex;

    @ApiModelProperty(value = "手机号",example = "")
    private String phone;

    @Override
    public void save() {
        super.save();
    }


}
