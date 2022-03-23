package com.hongru.system.entity;

import com.hongru.ebean.HongRuEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @Author zhangweijian
 * @since 2018-12-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
public class SysDictItem extends HongRuEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 字典id
     */
    private String dictId;

    @ManyToOne
    private SysDict dict;

    /**
     * 字典项文本
     */
    private String itemText;

    /**
     * 字典项值
     */
    private String itemValue;

    /**
     * 描述
     */
    private String description;

    /**
     * 状态（1启用 0不启用）
     */
    private Integer status;


}
