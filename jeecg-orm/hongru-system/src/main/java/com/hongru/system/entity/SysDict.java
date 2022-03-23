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
 * 字典表
 * </p>
 *
 * @Author zhangweijian
 * @since 2018-12-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
public class SysDict extends HongRuEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * [预留字段，暂时无用]
     * 字典类型,0 string,1 number类型,2 boolean
     * 前端js对stirng类型和number类型 boolean 类型敏感，需要区分。在select 标签匹配的时候会用到
     * 默认为string类型
     */
    private Integer type;

    /**
     * 字典名称
     */
    private String dictName;

    /**
     * 字典编码
     */
    private String dictCode;

    /**
     * 描述
     */
    private String description;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "dict_id")
    private List<SysDictItem> dictItems;

}
