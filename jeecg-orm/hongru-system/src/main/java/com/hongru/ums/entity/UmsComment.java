package com.hongru.ums.entity;

import com.hongru.ebean.HongRuEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import javax.persistence.Entity;
import javax.persistence.Transient;
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
@ApiModel("评论对象")
public class UmsComment extends HongRuEntity {


    @ApiModelProperty(value = "会员ID",example = "",readOnly = true)
    private String memberId;

    @ApiModelProperty(value = "父评论ID",example = "")
    private String commentId;

    @ApiModelProperty(value = "评论数据ID",example = "")
    private String sourceId;

    @ApiModelProperty(value = "评论类型(UmsDynamic:动态 CmsContent:内容 )",example = "UmsDynamic")
    @Dict(dicCode = "comment_type")
    private String type;

    @ApiModelProperty(value = "评论内容",example = "")
    private String content;

    @ApiModelProperty(value = "点赞数",example = "",readOnly = true)
    private Integer likeNum;

    @ApiModelProperty(value = "评论数",example = "",readOnly = true)
    private Integer commentNum;

    @ApiModelProperty(value = "状态(-1:审核未通过 1:审核通过 0:待审核 )",example = "-1",readOnly = true)
    @Dict(dicCode = "audit_status")
    private Integer status;

    @Transient
    private Boolean isGiveLike;

    @Override
    public void save() {
        if(null==this.memberId){
        this.memberId="";
        }
        if(null==this.likeNum){
        this.likeNum=0;
        }
        if(null==this.commentNum){
        this.commentNum=0;
        }
        if(null==this.status){
        this.status=1;
        }
        super.save();
    }


}
