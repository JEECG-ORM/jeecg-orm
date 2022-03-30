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
@ApiModel("点赞收藏记录对象")
public class UmsLikeRecord extends HongRuEntity {


    @ApiModelProperty(value = "会员ID",example = "")
    private String memberId;

    @ApiModelProperty(value = "点赞收藏类型(UmsDynamic:动态消息 CmsContent:内容 UmsComment:评论 )",example = "UmsDynamic")
    @Dict(dicCode = "like_type")
    private String type;

    @ApiModelProperty(value = "点赞收藏数据ID",example = "")
    private String sourceId;

    @ApiModelProperty(value = "操作类型(like:点赞 collect:收藏 )",example = "like")
    @Dict(dicCode = "like_operation")
    private String operation;

    @Override
    public void save() {
        if(null==this.memberId){
        this.memberId="";
        }
        if(null==this.type){
        this.type="";
        }
        if(null==this.sourceId){
        this.sourceId="";
        }
        if(null==this.operation){
        this.operation="";
        }
        super.save();
    }


}
