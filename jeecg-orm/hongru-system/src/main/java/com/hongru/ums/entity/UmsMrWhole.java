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
@ApiModel("整体医学报告对象")
public class UmsMrWhole extends HongRuEntity {


    @ApiModelProperty(value = "会员ID",example = "",readOnly = true)
    private String memberId;

    @ApiModelProperty(value = "报告名称",example = "整体医学报告--定制版")
    private String name;

    @ApiModelProperty(value = "综合检测分析",example = "")
    private String detailCms1;

    @ApiModelProperty(value = "整体医学干预建议",example = "")
    private String detailCms2;

    @ApiModelProperty(value = "临床体检项目建议",example = "")
    private String detailCms3;

    @ApiModelProperty(value = "整体医学干预方案",example = "")
    private String detailCms4;

    @Override
    public void save() {
        if(null==this.memberId){
        this.memberId="";
        }
        super.save();
    }


}
