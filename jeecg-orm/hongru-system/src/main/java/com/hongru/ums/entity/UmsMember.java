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
@ApiModel("会员对象")
public class UmsMember extends HongRuEntity {

    @ApiModelProperty(value = "open_id",example = "")
    private String openId;
    @ApiModelProperty(value = "union_id",example = "")
    private String unionId;
    @ApiModelProperty(value = "是否授权昵称",example = "0")
    private Boolean auth;
    @ApiModelProperty(value = "微信昵称",example = "salter")
    private String nickName;
    @ApiModelProperty(value = "微信头像",example = "https://portrait.gitee.com/uploads/avatars/user/2464/7394744_jeecg-salter_1648046300.png")
    private String avatarUrl;
    @ApiModelProperty(value = "姓名",example = "刘晓天")
    private String realName;
    @ApiModelProperty(value = "手机号",example = "18813066492")
    private String phone;
    @ApiModelProperty(value = "年龄",example = "18")
    private String age;
    @ApiModelProperty(value = "性别",example = "1")
    private Integer gender;
    @ApiModelProperty(value = "身高",example = "192cm")
    private String height;
    @ApiModelProperty(value = "体重",example = "85kg")
    private String weight;
    @ApiModelProperty(value = "血型",example = "A")
    private String blood;
    @ApiModelProperty(value = "生日",example = "19940408")
    private String birthday;
    @ApiModelProperty(value = "token")
    @Transient
    private String token;
    @Override
    public void save() {
        if(null==this.openId){
        this.openId="";
        }
        if(null==this.unionId){
        this.unionId="";
        }
        if(null==this.auth){
        this.auth=0==1;
        }
        if(null==this.nickName){
        this.nickName="salter";
        }
        if(null==this.avatarUrl){
        this.avatarUrl="https://portrait.gitee.com/uploads/avatars/user/2464/7394744_jeecg-salter_1648046300.png";
        }
        if(null==this.realName){
        this.realName="刘晓天";
        }
        if(null==this.phone){
        this.phone="18813066492";
        }
        if(null==this.age){
        this.age="18";
        }
        if(null==this.height){
        this.height="192cm";
        }
        if(null==this.weight){
        this.weight="85kg";
        }
        if(null==this.blood){
        this.blood="A";
        }
        if(null==this.birthday){
        this.birthday="19940408";
        }
        super.save();
    }


}
