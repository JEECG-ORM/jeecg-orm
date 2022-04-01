package com.hongru.ums.entity.vo;

import com.hongru.ebean.HongRuEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
@Table(name = "ums_member")
@ApiModel("会员动态VO对象")
public class UmsMemberDynamicVo  {
    @Id
    @ApiModelProperty(hidden = true)
    private String id;
    @ApiModelProperty(value = "微信昵称",example = "salter")
    private String nickName;
    @ApiModelProperty(value = "微信头像",example = "https://portrait.gitee.com/uploads/avatars/user/2464/7394744_jeecg-salter_1648046300.png")
    private String avatarUrl;

}
