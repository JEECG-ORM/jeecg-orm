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
@ApiModel("动态消息对象")
public class UmsDynamic extends HongRuEntity {


    @ApiModelProperty(value = "会员ID",example = "",readOnly = true)
    private String memberId;

    @ApiModelProperty(value = "消息类型(txt:文本 img:图片 video:视频 )",example = "txt")
    @Dict(dicCode = "dynamic_type")
    private String type;

    @ApiModelProperty(value = "文件路径,图片类型逗号拼接",example = "")
    private String filePath;

    @ApiModelProperty(value = "文字内容",example = "")
    private String messageText;

    @ApiModelProperty(value = "视频审核校验任务",example = "",readOnly = true)
    private String mediaCheckTaskId;

    @ApiModelProperty(value = "审核状态(-1:审核未通过 1:审核通过 0:待审核 )",example = "-1",readOnly = true)
    @Dict(dicCode = "audit_status")
    private Integer status;

    @ApiModelProperty(value = "点赞数",example = "0",readOnly = true)
    private Integer likeNum;

    @ApiModelProperty(value = "评论数",example = "0",readOnly = true)
    private Integer commentNum;

    @Override
    public void save() {
        if(null==this.memberId){
        this.memberId="";
        }
        if(null==this.mediaCheckTaskId){
        this.mediaCheckTaskId="";
        }
        if(null==this.status){
        this.status=1;
        }
        if(null==this.likeNum){
        this.likeNum=0;
        }
        if(null==this.commentNum){
        this.commentNum=0;
        }
        super.save();
    }


}
