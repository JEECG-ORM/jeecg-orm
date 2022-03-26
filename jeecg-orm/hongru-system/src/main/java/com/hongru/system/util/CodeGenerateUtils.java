package com.hongru.system.util;

import com.hongru.system.entity.GenTable;
import com.hongru.system.entity.GenTableColumn;
import com.hongru.system.entity.SysDict;
import com.hongru.util.StringUtil;
import freemarker.template.Template;
import io.ebean.DB;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description
 * @Copyright (c) 1998-2022 北京新鸿儒世纪网络技术有限公司 All Rights Reserved.
 * @Url https://www.xinhongru.com
 * @ClassName CodeGenerateUtils
 * @Author salter <salter@vip.163.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2022/1/28 10:10
 */
@Component
@Data
public class CodeGenerateUtils {
    /**
     * 项目根目录
     */
    String projectPath = System.getProperty("user.dir");
    private GenTable table;
    private String targetPath;
    String tableName;
    String tablePrefix;
    String businessName;
    String modalName;

    public static void main(String[] args) {
        new CodeGenerateUtils().generateVueFile("4cdf92a7357e4d8095a9464516a391df");
    }


    public void init(String tableId) {
        table = DB.find(GenTable.class).where().idEq(tableId).findOne();
        if (2 == table.getTableType()) {
            List<GenTable> subTableList = DB.find(GenTable.class).where().eq("mainTable", table.getTableName()).findList();
            for (GenTable genTable : subTableList) {
                String modalName = getModalName(genTable);
                genTable.setModalName(modalName);
            }
            table.setSubTableList(subTableList);
        }
        List<GenTableColumn> tableColumnList = table.getTableColumnList();
        List<SysDict> dicts = DB.find(SysDict.class).findList();
        Map<String, SysDict> dictMap = dicts.stream().collect(Collectors.toMap(SysDict::getDictCode, e -> e));
        for (GenTableColumn genTableColumn : tableColumnList) {
            if (StringUtil.isNotEmpty(genTableColumn.getDictCode())) {
                genTableColumn.setDict(dictMap.get(genTableColumn.getDictCode()));
            }
        }
        table.setTableColumnList(tableColumnList);
        targetPath = projectPath + "/jeecg-orm/" + table.getModuleName() + "/src/main/java/" + table.getPackageName().replaceAll("\\.", "/");
        tableName = table.getTableName();
        tablePrefix = table.getTableName().split("_")[0];
        businessName = tableName.split("_")[1];
        modalName = table.getClassName().replace(captureName(tablePrefix), "");
        table.setModalName(modalName);
    }

    public String getModalName(GenTable genTable) {
        String tablePrefix = genTable.getTableName().split("_")[0];
        return genTable.getClassName().replace(captureName(tablePrefix), "");
    }

    /**
     * 根据模板创建文件
     *
     * @throws Exception
     */
    public void generateVueFile(String tableId) {
        init(tableId);
        try {
            //生成列表Vue文件
            generateListVueFile("AsyncList.vue.ftl");
            //生成表单Vue文件
            generateModalVueFile("AsyncModal.vue.ftl");
        } catch (Exception e) {

        }
    }

    public void generateJavaFile(String tableId) {
        init(tableId);
        try {
            //生成实体Entity文件
            generateEntityFile();
            //生成控制器Controller文件
            generateControllerFile();
        } catch (Exception e) {

        }
    }

    /**
     * 生成表单Vue文件
     *
     * @throws Exception
     */
    private void generateModalVueFile(String templateName) throws Exception {
        // 文件路径
        String path = projectPath + "/ant-design-vue-jo/src/views/" + tablePrefix + "/" + businessName + "/";
        // 初始化文件路径
        initFileDir(path);
        // 文件后缀
        String suffix = modalName + "Modal.vue";
        // 完整的文件路径
        String filePath = path + suffix;
        File indexVueFile = new File(filePath);
        // 删除已存在文件
        indexVueFile.delete();
        // 模板文件
        generateFileByTemplate(templateName, indexVueFile);
    }

    /**
     * 生成列表Vue文件
     *
     * @throws Exception
     */
    private void generateListVueFile(String templateName) throws Exception {
        // 文件路径
        String path = projectPath + "/ant-design-vue-jo/src/views/" + tablePrefix + "/" + businessName + "/";
        // 初始化文件路径
        initFileDir(path);
        // 文件后缀
        String suffix = modalName + "List.vue";
        // 完整的文件路径
        String filePath = path + suffix;
        File indexVueFile = new File(filePath);
        // 删除已存在文件
        indexVueFile.delete();
        // 模板文件
        generateFileByTemplate(templateName, indexVueFile);
    }

    public static String captureName(String name) {
        char[] cs = name.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }

    /**
     * 生成实体对象Entity.java文件
     *
     * @throws Exception
     */
    private void generateEntityFile() throws Exception {
        // 文件路径
        String path = targetPath + "/entity/";
        // 初始化文件路径
        initFileDir(path);
        // 文件后缀
        String suffix = ".java";
        // 完整的文件路径
        String filePath = path + table.getClassName() + suffix;
        File entityFile = new File(filePath);
        // 删除已存在文件
        entityFile.delete();
        // 模板文件
        String templateName = "Entity.ftl";
        generateFileByTemplate(templateName, entityFile);
    }

    /**
     * 生成控制器文件
     *
     * @throws Exception
     */
    private void generateControllerFile() throws Exception {
        // 文件路径
        String path = targetPath + "/controller/";
        // 初始化文件路径
        initFileDir(path);
        // 文件后缀
        String suffix = "Controller.java";
        // 完整的文件路径
        String filePath = path + table.getClassName() + suffix;
        File controllerFile = new File(filePath);
        // 删除已存在文件
        controllerFile.delete();
        // 模板文件
        String templateName = "Controller.ftl";
        generateFileByTemplate(templateName, controllerFile);
    }

    /**
     * 生成模板文件
     *
     * @param templateName 模板名称
     * @param file         生成文件
     * @throws Exception
     */
    private void generateFileByTemplate(String templateName, File file) throws Exception {
        Template template = FreeMarkerUtils.getTemplate(templateName);
        FileOutputStream fos = new FileOutputStream(file);
        Writer out = new BufferedWriter(new OutputStreamWriter(fos, "utf-8"), 10240);
        template.process(table, out);
    }

    /**
     * 根据路径创建文件夹
     *
     * @param path 路径
     */
    private void initFileDir(String path) {
        // 文件路径
        File file = new File(path);
        // 判断文件路径是否存在
        if (!file.exists()) {
            // 创建文件路径
            file.mkdirs();
        }
    }
}
