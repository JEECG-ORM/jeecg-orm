package ${packageName}.entity;

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
@ApiModel("${tableComment}对象")
public class ${className} extends HongRuEntity {

<#if tableColumnList?exists>
    <#list tableColumnList as model>
        <#if model.columnName!= 'id'&&model.columnName!= 'sort_no'&&model.columnName!= 'create_by'&&model.columnName!= 'create_time'&&model.columnName!= 'update_by'&&model.columnName!= 'update_time'&&model.columnName!= 'deleted'>
            <#if (model.javaType != 'Date') >

        <#if model.dict??>
    @ApiModelProperty(value = "${model.columnComment}(<#list model.dict.dictItems as dictItem>${dictItem.itemValue}:${dictItem.itemText} </#list>)",example = "${model.dict.dictItems[0].itemValue}"<#if !model.isInsert>,readOnly = true</#if>)
    @Dict(dicCode = "${model.dictCode}"<#if (model.dictTable != '') > ,dictTable ="${dictTable}",dicText = "${dictText}"</#if>)
        <#else>
    @ApiModelProperty(value = "${model.columnComment}",example = "${model.columnExample}"<#if !model.isInsert>,readOnly = true</#if>)
        </#if>
    private ${model.javaType} ${model.javaField};
            </#if>
            <#if (model.javaType = 'Date') >
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "${model.columnComment}",example = "${model.columnExample}")
    private Date ${model.columnName?replace('(^_+)|(_+$)', '', 'r')
            ?replace('\\_+(\\w)?', ' $1', 'r')
            ?replace('([A-Z])', ' $1', 'r')
            ?capitalize
            ?replace(' ' , '')
            ?uncap_first};
            </#if>
        </#if>
    </#list>
</#if>

    @Override
    public void save() {
<#if tableColumnList?exists>
    <#list tableColumnList as model>
        <#if model.columnName!= 'id'&&model.columnName!= 'sort_no'&&model.columnName!= 'create_by'&&model.columnName!= 'create_time'&&model.columnName!= 'update_by'&&model.columnName!= 'update_time'&&model.columnName!= 'deleted'>
    <#if !model.isInsert>
    <#if model.javaType="String">
        if(null==this.${model.javaField}){
        this.${model.javaField}="${model.columnExample}";
        }
    </#if>
    <#if model.javaType="Boolean">
        if(null==this.${model.javaField}){
        this.${model.javaField}=${model.columnExample}==1;
        }
    </#if>
    </#if>
        </#if>
    </#list>
</#if>
        super.save();
    }


}
