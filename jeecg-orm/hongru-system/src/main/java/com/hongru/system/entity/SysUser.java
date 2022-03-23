package com.hongru.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hongru.aspect.annotation.Dict;
import com.hongru.ebean.HongRuEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @Author scott
 * @since 2018-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
public class SysUser extends HongRuEntity {

    @ManyToMany
    @JoinTable(name = "sys_user_role",joinColumns={@JoinColumn(name="user_id")},inverseJoinColumns={@JoinColumn(name="role_id")})
    private List<SysRole> roles;

    @ManyToOne
    @JoinColumn(name = "lbs_id", insertable = false)
    private SysLbs lbs;

    private String username;
    private String realname;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String salt;

    private String avatar;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    @Dict(dicCode = "sex")
    private Integer sex;

    private String email;

    private String phone;

    /**
     * 部门code(当前选择登录部门)
     */
    private String orgCode;

    /**部门名称*/
    private transient String orgCodeTxt;

    private Integer status;

    private String workNo;

    private String post;

    private String telephone;

    private Integer activitiSync;

    private Integer userIdentity;

    private String departIds;

    private String relTenantIds;

    private String lbsId;
    @Transient
    private String token;

    @Transient
    private String selectedroles;


}
