package com.hongru.ums.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "ums_comment")
@ApiModel("评论对象")
public class UmsCommentVo extends HongRuEntity {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private UmsMemberDynamicVo member;

    @ApiModelProperty(value = "评论内容", example = "")
    private String content;

    @ApiModelProperty(value = "点赞数", example = "", readOnly = true)
    private Integer likeNum;

    @ApiModelProperty(value = "评论数", example = "", readOnly = true)
    private Integer commentNum;

    @JsonIgnore
    transient Integer status;

    @Transient
    @ApiModelProperty(value = "是否已赞", example = "", readOnly = true)
    private Boolean isGiveLike;


}
