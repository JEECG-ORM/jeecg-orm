package com.hongru.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.hongru.ebean.EbeanUtil;
import com.hongru.ebean.HongRuEntity;
import com.hongru.ebean.HongRuPage;
import com.hongru.system.entity.GenTable;
import com.hongru.system.entity.GenTableColumn;
import com.hongru.system.entity.SysLbs;
import com.hongru.system.util.CodeGenerateUtils;
import com.hongru.util.StringUtil;
import com.hongru.vo.Result;
import io.ebean.DB;
import io.ebean.SqlQuery;
import io.ebean.SqlUpdate;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description
 * @Copyright (c) 1998-2022 北京新鸿儒世纪网络技术有限公司 All Rights Reserved.
 * @Url https://www.xinhongru.com
 * @ClassName CommonController
 * @Author salter <salter@vip.163.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2022/1/11 13:45
 */
@Slf4j
@RestController
@RequestMapping("/gen/table")
public class GenTableController {


    @Value("${generate.packageName}")
    private String packageName = "";
    @Value("${generate.moduleName}")
    private String moduleName = "";
    private CodeGenerateUtils codeGenerateUtils;

    @Autowired
    public GenTableController(CodeGenerateUtils codeGenerateUtils) {
        this.codeGenerateUtils = codeGenerateUtils;
    }

    @PostMapping("/list")
    public Result<HongRuPage<GenTable>> queryPageList(@RequestBody JSONObject searchObj) {
        return Result.OK(EbeanUtil.pageList(searchObj, GenTable.class));
    }

    @PostMapping(value = "/add")
    public Result<GenTable> add(@RequestBody GenTable genTable) {
        List<GenTableColumn> tableColumnList = genTable.getTableColumnList();
        String tablePrefix = genTable.getTableName().split("_")[0];
        genTable.setTableColumnList(null);
        genTable.setPackageName(packageName+"."+tablePrefix );
        genTable.setModuleName(moduleName);
        genTable.setDePrefixClassName(genTable.getClassName().replace(StringUtil.captureName(tablePrefix), ""));
        genTable.save();
        for (GenTableColumn genTableColumn : tableColumnList) {
            genTableColumn.save();
        }
        //创建表
        String createTableSql = "CREATE TABLE " + genTable.getTableName() + "   (\n" +
                "\t`id` VARCHAR(32) NOT NULL COMMENT '主键id' COLLATE 'utf8_general_ci',\n" +
                "\t`sort_no` DOUBLE NULL DEFAULT NULL COMMENT '排序',\n" +
                "\t`create_by` VARCHAR(32) NULL DEFAULT NULL COMMENT '创建人' COLLATE 'utf8_general_ci',\n" +
                "\t`update_by` VARCHAR(32) NULL DEFAULT NULL COMMENT '更新人' COLLATE 'utf8_general_ci',\n" +
                "\t`update_time` DATETIME NULL DEFAULT NULL COMMENT '更新时间',\n" +
                "\t`deleted` TINYINT(1) NULL DEFAULT NULL COMMENT '删除标记',\n" +
                "\tPRIMARY KEY (`id`) USING BTREE\n" +
                ")\n" +
                "COMMENT='" + genTable.getTableComment() + "'\n" +
                "COLLATE='utf8_general_ci'\n" +
                "ENGINE=InnoDB\n" +
                "ROW_FORMAT=DYNAMIC\n" +
                ";\n";
        SqlUpdate createTableUpdate = DB.sqlUpdate(createTableSql);
        createTableUpdate.execute();
        //添加表字段
        for (GenTableColumn genTableColumn : tableColumnList) {
            alterTable(genTable.getTableName(), genTableColumn);
        }
        return Result.OK();
    }

    public void alterTable(String tableName, GenTableColumn genTableColumn) {
        String alterTableSql = "ALTER TABLE " + tableName + " ADD COLUMN " + splitJoinColumnInfo(genTableColumn);
        SqlUpdate alterTableUpdate = DB.sqlUpdate(alterTableSql);
        alterTableUpdate.execute();
    }

    public String splitJoinColumnInfo(GenTableColumn genTableColumn) {
        StringBuilder sb = new StringBuilder();
        //拼接字段名称
        sb.append("`").append(genTableColumn.getColumnName()).append("`");
        //拼接字段数据类型
        sb.append(genTableColumn.getColumnType());
        //字符串类型处理
        if ("VARCHAR".equals(genTableColumn.getColumnType())) {
            if (StringUtil.isNotBlank(genTableColumn.getColumnLength())) {
                sb.append("(").append(genTableColumn.getColumnLength()).append(")");
            }
        }
        sb.append(" COMMENT '" + genTableColumn.getColumnComment() + "'");
        return sb.toString();
    }

    @PostMapping(value = "/edit")
    public Result<GenTable> edit(@RequestBody GenTable genTable) {
        //原Table
        GenTable oTable = DB.find(GenTable.class).where().idEq(genTable.getId()).findOne();
        //原TableColumn ID数组
        List<String> oTableColumnIds = oTable.getTableColumnList().stream().map(HongRuEntity::getId).collect(Collectors.toList());
        //当前提交表单TableColumn数据
        List<GenTableColumn> cTableColumnList = genTable.getTableColumnList();
        //修改的TableColumn
        List<GenTableColumn> uTableColumnList = cTableColumnList.stream().filter(e -> oTableColumnIds.contains(e.getId())).collect(Collectors.toList());
        //新增的TableColumn
        List<GenTableColumn> sTableColumnList = cTableColumnList.stream().filter(e -> !oTableColumnIds.contains(e.getId())).collect(Collectors.toList());
        genTable.setTableColumnList(uTableColumnList);
        genTable.update();
        //修改表描述
        if (!oTable.getTableComment().equals(genTable.getTableComment())) {
            String updateTableCommentSql = " ALTER TABLE `" + genTable.getTableName() + "` COMMENT='" + genTable.getTableComment() + "';";
            DB.sqlUpdate(updateTableCommentSql).execute();
        }
        Map<String, GenTableColumn> cTableColumnMap = cTableColumnList.stream().collect(Collectors.toMap(GenTableColumn::getId, e -> e));
        //修改表字段
        for (GenTableColumn genTableColumn : uTableColumnList) {
            GenTableColumn oGenTableColumn = cTableColumnMap.get(genTableColumn.getId());
            String changeColumnSql = "ALTER TABLE `" + genTable.getTableName() + "` CHANGE COLUMN `" + oGenTableColumn.getColumnName() + "` " + splitJoinColumnInfo(genTableColumn);
            DB.sqlUpdate(changeColumnSql).execute();
        }
        for (GenTableColumn genTableColumn : sTableColumnList) {
            genTableColumn.save();
            //添加表字段
            alterTable(genTable.getTableName(), genTableColumn);
        }
        return Result.OK();
    }

    @GetMapping(value = "/generateFile/{type}")
    public Result generateFile(String id,@PathVariable String type) {
        if("java".equals(type)){
            codeGenerateUtils.generateJavaFile(id);
        }
        if("vue".equals(type)){
            codeGenerateUtils.generateVueFile(id);
            GenTable table = DB.find(GenTable.class).where().idEq(id).findOne();
            if (2 == table.getTableType()) {
                List<GenTable> subTableList = DB.find(GenTable.class).where().eq("mainTable", table.getTableName()).findList();
                for(GenTable t:subTableList){
                    codeGenerateUtils.generateVueFile(t.getId());
                }
            }
        }
        return Result.OK();
    }

}
