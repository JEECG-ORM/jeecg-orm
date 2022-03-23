package com.hongru.ebean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hongru.vo.LoginUser;
import io.ebean.DB;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.shiro.SecurityUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * @Description
 * @Copyright (c) 1998-2022 北京新鸿儒世纪网络技术有限公司 All Rights Reserved.
 * @Url https://www.xinhongru.com
 * @ClassName HongRuEntity
 * @Author salter <salter@vip.163.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2022/1/6 22:15
 */
@MappedSuperclass
@Data
public abstract class HongRuEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    public HongRuEntity() {
    }

    @Id
    @ApiModelProperty(hidden = true)
    private String id;
    @ApiModelProperty(hidden = true)
    private Double sortNo;
    @ApiModelProperty(hidden = true)
    private String createBy;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(hidden = true)
    private Date createTime;
    @ApiModelProperty(hidden = true)
    private String updateBy;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(hidden = true)
    private Date updateTime;
    @ApiModelProperty(hidden = true)
    private Boolean deleted;


    public void save() {
        this.setId(UUID.randomUUID().toString().replace("-", ""));
        this.setDeleted(false);
        this.setCreateTime(new Date());
        this.setUpdateTime(new Date());
        if (null == sortNo) {
            this.setSortNo(0D);
        }
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        this.setCreateBy(loginUser.getUsername());
        this.setUpdateBy(loginUser.getUsername());
        DB.save(this);
    }

    public void update() {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        this.setUpdateBy(loginUser.getUsername());
        this.setUpdateTime(new Date());
        DB.update(this);
    }


}
