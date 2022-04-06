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
@ApiModel("身体检测对象")
public class UmsPe extends HongRuEntity {


    @ApiModelProperty(value = "会员ID",example = "",readOnly = true)
    private String memberId;

    @ApiModelProperty(value = "分类ID",example = "F05")
    @Dict(dicCode = "code",dictTable ="sys_category",dicText = "name")
    private String categoryId;

    @ApiModelProperty(value = "报告名称",example = " 2022年10月份身体成分检测")
    private String name;

    @ApiModelProperty(value = "报告文件",example = "")
    private String file;

    @ApiModelProperty(value = "状态(0:关闭 1:开启 )",example = "0",readOnly = true)
    @Dict(dicCode = "status")
    private Boolean status;

    @Override
    public void save() {
        if(null==this.memberId){
        this.memberId="";
        }
        if(null==this.status){
        this.status=0==1;
        }
        super.save();
    }


}
