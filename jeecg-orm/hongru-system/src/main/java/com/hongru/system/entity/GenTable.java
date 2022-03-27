package com.hongru.system.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hongru.ebean.HongRuEntity;
import com.hongru.util.StringUtil;
import io.ebean.annotation.Where;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
public class GenTable extends HongRuEntity {
    /**
     * 表名称
     */
    private String tableName;

    private Integer tableType;

    /**
     * 表描述
     */
    private String tableComment;

    /**
     * 实体类名称
     */
    private String className;

    private String dePrefixClassName;
    /**
     * 使用的模板（crud单表操作 tree树表操作）
     */
    private String tplCategory;

    /**
     * 生成包路径
     */
    private String packageName;

    /**
     * 生成模块名
     */
    private String moduleName;

    /**
     * 生成功能名
     */
    private String functionName;

    /**
     * 生成功能作者
     */
    private String functionAuthor;

    /**
     * 其它生成选项
     */
    private String options;


    private String mainTable;

    /**
     * 备注
     */
    private String note;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "table_id")
    @Where(clause = "channelCode='0'")
    @OrderBy(value = "sortNo asc")
    @JsonManagedReference
    private List<GenTableColumn> tableColumnList;

    @Transient
    private List<GenTable> subTableList=new ArrayList<>();



    @Override
    public void save() {
        String className = StringUtil.underline2Camel(this.getTableName(), true);
        this.setClassName(className);
        this.setPackageName("com.hongru." + this.getTableName().split("_")[0]);
        this.setModuleName("jeecg-orm");
        super.save();
    }
}
