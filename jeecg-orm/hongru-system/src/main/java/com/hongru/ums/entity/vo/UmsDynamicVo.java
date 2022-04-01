package com.hongru.ums.entity.vo;

import com.hongru.aspect.annotation.Dict;
import com.hongru.ebean.HongRuEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;

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
@Table(name = "ums_dynamic")
@ApiModel("动态消息VO对象")
public class UmsDynamicVo extends HongRuEntity {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private UmsMemberDynamicVo member;

    @ApiModelProperty(value = "消息类型(txt:文本 img:图片 video:视频 )",example = "txt")
    @Dict(dicCode = "dynamic_type")
    private String type;

    @ApiModelProperty(value = "文件路径,图片类型逗号拼接",example = "")
    private String filePath;

    @ApiModelProperty(value = "文字内容",example = "")
    private String messageText;

    @ApiModelProperty(value = "点赞数",example = "0",readOnly = true)
    private Integer likeNum;

    @ApiModelProperty(value = "评论数",example = "0",readOnly = true)
    private Integer commentNum;





}
