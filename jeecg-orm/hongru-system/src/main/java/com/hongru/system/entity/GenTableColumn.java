

package com.hongru.system.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hongru.ebean.HongRuEntity;
import com.hongru.util.StringUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
public class GenTableColumn extends HongRuEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(insertable = false, updatable = false)
    private String tableId;

    @ManyToOne
    @JoinColumn(name = "table_id")
    @JsonBackReference
    private GenTable genTable;

    /**
     * 栏目编码
     */
    private String channelCode;

    /**
     * 列名称
     */
    private String columnName;

    /**
     * 列描述
     */
    private String columnComment;

    /**
     * 列类型
     */
    private String columnType;

    /**
     * 列长度
     */
    private String columnLength;


    /**
     * 列示例值
     */
    private String columnExample;

    private String mainTable;

    private String mainField;

    /**
     * JAVA类型
     */
    private String javaType;

    /**
     * JAVA字段名
     */
    private String javaField;

    private String dictTable;

    private String dictText;

    private String dictCode;

    @Transient
    private SysDict dict;

    /**
     * 是否主键（1是）
     */
    private Integer isPk;

    /**
     * 是否唯一（1是 0否）
     */
    private Boolean isUnique;

    /**
     * 是否自增（1是）
     */
    private String isIncrement;

    /**
     * 是否必填（1是）
     */
    private Boolean isRequired;

    /**
     * 是否为插入字段（1是）
     */
    private Boolean isInsert;

    /**
     * 是否编辑字段（1是）
     */
    private String isEdit;

    /**
     * 是否列表字段（1是）
     */
    private Boolean isList;

    /**
     * 是否查询字段（1是）
     */
    private Boolean isQuery;

    /**
     * 查询方式（等于、不等于、大于、小于、范围）
     */
    private String queryType;

    /**
     * 显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）
     */
    private String htmlType;
    /**
     * 验证规则
     */
    private String fieldValid;
    /**
     * 控件长度
     */
    private Integer fieldLength;


    /**
     * 字典类型
     */
    private String dictType;

    private Boolean status;

    @Override
    public void save() {
        this.setJavaType(javaTypeDict());
        this.setJavaField(StringUtil.lineToHump(columnName));
        if (null == this.getChannelCode()) {
            this.setChannelCode("0");
        }
        if (null == this.status) {
            this.setStatus(false);
        }
        if(null==this.isUnique){
            this.setIsUnique(false);
        }
        super.save();
    }

    public String javaTypeDict() {
        String javaType = "String";
        if ("INT".equals(columnType)) {
            javaType = "Integer";
        }
        if ("DOUBLE".equals(columnType)) {
            javaType = "Double";
        }
        if ("TINYINT".equals(columnType)) {
            javaType = "Boolean";
        }
        if ("DATE".equals(columnType)) {
            javaType = "Date";
        }
        return javaType;
    }


}
