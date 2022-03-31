package ${packageName}.controller;

import com.alibaba.fastjson.JSONObject;
import com.hongru.ebean.StatusDto;
import com.hongru.ebean.SortNoDto;
import com.hongru.ebean.EbeanUtil;
import com.hongru.ebean.HongRuPage;
import ${packageName}.entity.${className};
import com.hongru.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
import com.github.xiaoymin.knife4j.annotations.DynamicParameters;
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
@RestController
@RequestMapping("/${className?replace("([a-z])([A-Z]+)","$1/$2","r")?lower_case}")
@Slf4j
@Api(tags = "${tableComment}管理")
public class ${className}Controller {

    @PostMapping("/list")
    @ApiOperation("列表")
    @ApiOperationSupport(params = @DynamicParameters(properties = {
<#if tableColumnList?exists>
    <#list tableColumnList as model>
        <#if model.isQuery>
            <#if model.dict??>
                @DynamicParameter(name = "${model.javaField}", value = "${model.columnComment}(<#list model.dict.dictItems as dictItem>${dictItem.itemValue}:${dictItem.itemText} </#list>)",example = "${model.dict.dictItems[0].itemValue}"),
            <#else>
                @DynamicParameter(name = "${model.javaField}", value = "${model.columnComment}",example = "${model.columnExample}"),
            </#if>
        </#if>
    </#list>
</#if>
    }))
    public Result<HongRuPage<${className}>> query${className}PageList(@RequestBody JSONObject searchObj) {
        return Result.OK(EbeanUtil.pageList(searchObj, ${className}.class));
    }

    @ApiOperation("添加")
    @ApiOperationSupport(ignoreParameters = {"${className?uncap_first}.id"})
    @PostMapping(value = "/add")
    public Result<${className}> add(@RequestBody ${className} ${className?uncap_first}) {
        ${className?uncap_first}.save();
        return Result.OK("添加成功");
    }

    @PostMapping(value = "/edit")
    public Result<${className}> edit(@RequestBody ${className} ${className?uncap_first}) {
        ${className?uncap_first}.update();
        return Result.OK("修改成功");
    }

    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        EbeanUtil.delete(id, ${className}.class);
        return Result.OK("删除成功");
    }
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        EbeanUtil.deleteBatch(ids, ${className}.class);
        return Result.OK("批量删除成功");
    }

    @PostMapping(value="/field/{field}")
    public Result<?> field(@PathVariable String field, @RequestBody StatusDto statusDto) {
        EbeanUtil.field(field,statusDto,${className}.class);
        return Result.OK();
    }

    @PostMapping(value="/sortNo")
    public Result<?> sortNo(@RequestBody SortNoDto sortNoDto) {
        EbeanUtil.sortNo(sortNoDto,${className}.class);
        return Result.OK();
    }





}

