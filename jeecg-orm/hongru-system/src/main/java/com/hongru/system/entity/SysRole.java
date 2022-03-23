package com.hongru.system.entity;

import com.hongru.ebean.HongRuEntity;
import io.ebean.annotation.Where;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @Author scott
 * @since 2018-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
public class SysRole extends HongRuEntity {


    private String roleName;

    private String roleCode;

    private String description;

    @ManyToMany
    @JoinTable(name = "sys_role_permission",joinColumns={@JoinColumn(name="role_id")},inverseJoinColumns={@JoinColumn(name="permission_id")})
    @Where(clause = "deleted=0")
    @OrderBy("sortNo")
    private List<SysPermission> permissions;

}
