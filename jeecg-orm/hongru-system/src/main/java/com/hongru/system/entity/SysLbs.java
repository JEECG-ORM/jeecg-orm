package com.hongru.system.entity;

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
@ApiModel("位置服务对象")
public class SysLbs extends HongRuEntity {


    @ApiModelProperty(value = "项目名称",example = "北京")
    private String name;

    @ApiModelProperty(value = "省",example = "1",readOnly = true)
    private String province;

    @ApiModelProperty(value = "区",example = "1",readOnly = true)
    private String area;

    @ApiModelProperty(value = "市",example = "1",readOnly = true)
    private String city;

    @ApiModelProperty(value = "省市区",example = "1")
    private String areaCode;

    @ApiModelProperty(value = "经纬度",example = "1",readOnly = true)
    private String coordinate;

    @ApiModelProperty(value = "联系电话",example = "400")
    private String tel;

    @ApiModelProperty(value = "详细地址",example = "1")
    private String address;

    @ApiModelProperty(value = "状态",example = "1",readOnly = true)
    private Boolean status;

    @ApiModelProperty(value = "社区风采",example = "1")
    private String detailCms;

    @Override
    public void save() {
        if(null==this.province){
        this.province="1";
        }
        if(null==this.area){
        this.area="1";
        }
        if(null==this.city){
        this.city="1";
        }
        if(null==this.coordinate){
        this.coordinate="1";
        }
        if(null==this.status){
        this.status=1==1;
        }
        super.save();
    }


}
