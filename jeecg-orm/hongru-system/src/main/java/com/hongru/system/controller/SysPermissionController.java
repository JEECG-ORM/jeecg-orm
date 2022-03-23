package com.hongru.system.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hongru.constant.CommonConstant;
import com.hongru.ebean.EbeanUtil;
import com.hongru.ebean.HongRuEntity;
import com.hongru.exception.HongRuException;
import com.hongru.redis.constant.CacheConstant;
import com.hongru.system.vo.SysPermissionTree;
import com.hongru.system.vo.TreeModel;
import com.hongru.util.BeanUtil;
import com.hongru.util.MD5Util;
import com.hongru.util.StringUtil;
import com.hongru.vo.LoginUser;
import com.hongru.system.entity.SysPermission;
import com.hongru.system.entity.SysRole;
import com.hongru.system.entity.SysUser;
import com.hongru.system.enums.RoleIndexConfigEnum;
import com.hongru.system.util.PermissionDataUtil;
import com.hongru.vo.Result;
import io.ebean.DB;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/sys/permission")
public class SysPermissionController {

    @GetMapping(value = "/list")
    public Result<List<SysPermissionTree>> list() {
        long start = System.currentTimeMillis();
        Result<List<SysPermissionTree>> result = new Result<>();
        try {
            List<SysPermission> list = DB.find(SysPermission.class).where().eq("deleted", false).orderBy("sortNo").findList();
            List<SysPermissionTree> treeList = new ArrayList<>();
            getTreeList(treeList, list, null);
            result.setResult(treeList);
            result.setSuccess(true);
            log.info("======获取全部菜单数据=====耗时:" + (System.currentTimeMillis() - start) + "毫秒");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        EbeanUtil.delete(id, SysPermission.class);
        return Result.OK("删除成功");
    }

    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        EbeanUtil.deleteBatch(ids, SysPermission.class);
        return Result.OK("批量删除成功");
    }

    /**
     * 获取全部的权限树
     *
     * @return
     */
    @GetMapping(value = "/queryTreeList")
    public Result<Map<String, Object>> queryTreeList() {
        Result<Map<String, Object>> result = new Result<>();
        // 全部权限ids
        try {
            List<SysPermission> list = DB.find(SysPermission.class).where().eq("deleted", false).orderBy("sortNo").findList();
            List<String> ids = list.stream().map(HongRuEntity::getId).collect(Collectors.toList());
            List<TreeModel> treeList = new ArrayList<>();
            getTreeModelList(treeList, list, null);
            Map<String, Object> resMap = new HashMap<String, Object>();
            resMap.put("treeList", treeList); // 全部树节点数据
            resMap.put("ids", ids);// 全部树ids
            result.setResult(resMap);
            result.setSuccess(true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 保存角色授权
     *
     * @return
     */
    @PostMapping(value = "/saveRolePermission")
    //@RequiresRoles({ "admin" })
    public Result<String> saveRolePermission(@RequestBody JSONObject json) {
        try {
            String roleId = json.getString("roleId");
            String permissionIds = json.getString("permissionIds");
            String lastPermissionIds = json.getString("lastpermissionIds");
            SysRole sysRole = new SysRole();
            sysRole.setId(roleId);

            List<SysPermission> permissions = new ArrayList();
            for (String permissionid : permissionIds.split(",")) {
                SysPermission sysPermission = new SysPermission();
                sysPermission.setId(permissionid);
                permissions.add(sysPermission);
            }
            sysRole.setPermissions(permissions);
            sysRole.update();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Result.OK();
    }

    private void getTreeModelList(List<TreeModel> treeList, List<SysPermission> metaList, TreeModel temp) {
        for (SysPermission permission : metaList) {
            String tempPid = permission.getParentId();
            TreeModel tree = new TreeModel(permission);
            if (temp == null && StringUtil.isEmpty(tempPid)) {
                treeList.add(tree);
                if (!tree.getIsLeaf()) {
                    getTreeModelList(treeList, metaList, tree);
                }
            } else if (temp != null && tempPid != null && tempPid.equals(temp.getKey())) {
                temp.getChildren().add(tree);
                if (!tree.getIsLeaf()) {
                    getTreeModelList(treeList, metaList, tree);
                }
            }

        }
    }

    //@RequiresRoles({ "admin" })
    @PostMapping(value = "/add")
    public Result<SysPermission> add(@RequestBody SysPermission permission) {
        Result<SysPermission> result = new Result<SysPermission>();
        try {
            permission = PermissionDataUtil.intelligentProcessData(permission);
            //----------------------------------------------------------------------
            //判断是否是一级菜单，是的话清空父菜单
            if (CommonConstant.MENU_TYPE_0.equals(permission.getMenuType())) {
                permission.setParentId(null);
            }
            //----------------------------------------------------------------------
            String pid = permission.getParentId();
            if (StringUtil.isNotEmpty(pid)) {
                //设置父节点不为叶子节点
                setMenuLeaf(pid, false);
            }
            permission.setLeaf(true);
            permission.save();
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }


    public void setMenuLeaf(String id, boolean leaf) {
        SysPermission parentPermission = new SysPermission();
        parentPermission.setId(id);
        parentPermission.setLeaf(leaf);
        parentPermission.update();
    }

    @PostMapping(value = "/edit")
    public Result<SysPermission> edit(@RequestBody SysPermission permission) {
        Result<SysPermission> result = new Result<SysPermission>();
        try {
            SysPermission p = DB.find(SysPermission.class).where().idEq(permission.getId()).findOne();
            //TODO 该节点判断是否还有子节点
            if (p == null) {
                throw new HongRuException("未找到菜单信息");
            } else {
                //----------------------------------------------------------------------
                //Step1.判断是否是一级菜单，是的话清空父菜单ID
                if (CommonConstant.MENU_TYPE_0.equals(permission.getMenuType())) {
                    permission.setParentId("");
                }
                //Step2.判断菜单下级是否有菜单，无则设置为叶子节点
                int count = DB.find(SysPermission.class).where().eq("parentId", permission.getId()).findCount();
                if (count == 0) {
                    permission.setLeaf(true);
                }
                permission.update();
                //----------------------------------------------------------------------

                //如果当前菜单的父菜单变了，则需要修改新父菜单和老父菜单的，叶子节点状态
                String pid = permission.getParentId();
                if ((StringUtil.isNotEmpty(pid) && !pid.equals(p.getParentId())) || StringUtil.isEmpty(pid) && StringUtil.isNotEmpty(p.getParentId())) {
                    //a.设置新的父菜单不为叶子节点
                    setMenuLeaf(pid, false);
                    //b.判断老的菜单下是否还有其他子菜单，没有的话则设置为叶子节点
                    int cc = DB.find(SysPermission.class).where().eq("parentId", p.getId()).findCount();
                    if (cc == 0) {
                        if (StringUtil.isNotEmpty(p.getParentId())) {
                            setMenuLeaf(p.getParentId(), true);
                        }
                    }
                }
            }
            result.success("修改成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    private void getTreeList(List<SysPermissionTree> treeList, List<SysPermission> metaList, SysPermissionTree temp) {
        for (SysPermission permission : metaList) {
            String tempPid = permission.getParentId();
            SysPermissionTree tree = BeanUtil.copy(permission, SysPermissionTree.class);
            tree.setTitle(permission.getName());
            tree.setKey(permission.getId());
            if (!tree.isLeaf()) {
                tree.setChildren(new ArrayList<>());
            }
            if (temp == null && StringUtil.isEmpty(tempPid)) {
                treeList.add(tree);
                if (!tree.isLeaf()) {
                    getTreeList(treeList, metaList, tree);
                }
            } else if (temp != null && tempPid != null && tempPid.equals(temp.getId())) {
                temp.getChildren().add(tree);
                if (!tree.isLeaf()) {
                    getTreeList(treeList, metaList, tree);
                }
            }

        }
    }

    @GetMapping(value = "/getUserPermission")
    public Result<?> getUserPermission() {
        try {
            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            if (null == loginUser) {
                return Result.error("请登录系统！");
            }
            SysUser sysUser = DB.find(SysUser.class).where().idEq(loginUser.getId()).findOne();
            //获取角色
            List<SysRole> roles = sysUser.getRoles();
            //存放当前用户的所有菜单
            ArrayList<SysPermission> metaList = new ArrayList<>();
            for (SysRole role : roles) {
                List<SysPermission> permissions = role.getPermissions();
                List<SysPermission> collect = new ArrayList<>(permissions);
                metaList.addAll(collect);
            }
            //添加首页路由
            //update-begin-author:taoyan date:20200211 for: TASK #3368 【路由缓存】首页的缓存设置有问题，需要根据后台的路由配置来实现是否缓存
            if (!PermissionDataUtil.hasIndexPage(metaList)) {
                SysPermission indexMenu = DB.find(SysPermission.class).where().eq("name", "首页").findList().get(0);
                metaList.add(0, indexMenu);
            }
            //update-end-author:taoyan date:20200211 for: TASK #3368 【路由缓存】首页的缓存设置有问题，需要根据后台的路由配置来实现是否缓存

            //update-begin--Author:liusq  Date:20210624  for:自定义首页地址LOWCOD-1578
            List<String> roleCodeList = roles.stream().map(SysRole::getRoleCode).collect(Collectors.toList());
            String compUrl = RoleIndexConfigEnum.getIndexByRoles(roleCodeList);
            if (StringUtils.isNotBlank(compUrl)) {
                List<SysPermission> menus = metaList.stream().filter(sysPermission -> "首页".equals(sysPermission.getName())).collect(Collectors.toList());
                menus.get(0).setComponent(compUrl);
            }
            //update-end--Author:liusq  Date:20210624  for：自定义首页地址LOWCOD-1578
            JSONObject json = new JSONObject();
            JSONArray menujsonArray = new JSONArray();
            this.getPermissionJsonArray(menujsonArray, metaList, null);
            //一级菜单下的子菜单全部是隐藏路由，则一级菜单不显示
            this.handleFirstLevelMenuHidden(menujsonArray);

            JSONArray authjsonArray = new JSONArray();
            this.getAuthJsonArray(authjsonArray, metaList);
            //查询所有的权限
            List<SysPermission> allAuthList = DB.find(SysPermission.class).where().eq("deleted", false).eq("menuType", CommonConstant.MENU_TYPE_2).findList();
            JSONArray allauthjsonArray = new JSONArray();
            this.getAllAuthJsonArray(allauthjsonArray, allAuthList);
            //路由菜单
            json.put("menu", menujsonArray);
            //按钮权限（用户拥有的权限集合）
            json.put("auth", authjsonArray);
            //全部权限配置集合（按钮权限，访问权限）
            json.put("allAuth", allauthjsonArray);
            //json.put("sysSafeMode", jeeccgBaseConfig.getSafeMode());
            return Result.OK(json);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("查询失败:" + e.getMessage());
        }

    }

    /**
     * 获取权限JSON数组
     *
     * @param jsonArray
     * @param allList
     */
    private void getAllAuthJsonArray(JSONArray jsonArray, List<SysPermission> allList) {
        JSONObject json = null;
        for (SysPermission permission : allList) {
            json = new JSONObject();
            json.put("action", permission.getPerms());
            json.put("status", permission.getStatus());
            //1显示2禁用
            json.put("type", permission.getPermsType());
            json.put("describe", permission.getName());
            jsonArray.add(json);
        }
    }

    /**
     * 获取权限JSON数组
     *
     * @param jsonArray
     * @param metaList
     */
    private void getAuthJsonArray(JSONArray jsonArray, List<SysPermission> metaList) {
        for (SysPermission permission : metaList) {
            if (permission.getMenuType() == null) {
                continue;
            }
            JSONObject json = null;
            if (permission.getMenuType().equals(CommonConstant.MENU_TYPE_2) && CommonConstant.STATUS_1.equals(permission.getStatus())) {
                json = new JSONObject();
                json.put("action", permission.getPerms());
                json.put("type", permission.getPermsType());
                json.put("describe", permission.getName());
                jsonArray.add(json);
            }
        }
    }

    /**
     * 一级菜单的子菜单全部是隐藏路由，则一级菜单不显示
     *
     * @param jsonArray
     */
    private void handleFirstLevelMenuHidden(JSONArray jsonArray) {
        jsonArray = jsonArray.stream().map(obj -> {
            JSONObject returnObj = new JSONObject();
            JSONObject jsonObj = (JSONObject) obj;
            if (jsonObj.containsKey("children")) {
                JSONArray childrens = jsonObj.getJSONArray("children");
                childrens = childrens.stream().filter(arrObj -> !"true".equals(((JSONObject) arrObj).getString("hidden"))).collect(Collectors.toCollection(JSONArray::new));
                if (childrens == null || childrens.size() == 0) {
                    jsonObj.put("hidden", true);

                    //vue3版本兼容代码
                    JSONObject meta = new JSONObject();
                    meta.put("hideMenu", true);
                    jsonObj.put("meta", meta);
                }
            }
            return returnObj;
        }).collect(Collectors.toCollection(JSONArray::new));
    }


    /**
     * 获取菜单JSON数组
     *
     * @param jsonArray
     * @param metaList
     * @param parentJson
     */
    private void getPermissionJsonArray(JSONArray jsonArray, List<SysPermission> metaList, JSONObject parentJson) {
        for (SysPermission permission : metaList) {
            if (permission.getMenuType() == null) {
                continue;
            }
            String tempPid = permission.getParentId();
            JSONObject json = getPermissionJsonObject(permission);
            if (json == null) {
                continue;
            }
            if (parentJson == null && StringUtils.isEmpty(tempPid)) {
                jsonArray.add(json);
                if (!permission.isLeaf()) {
                    getPermissionJsonArray(jsonArray, metaList, json);
                }
            } else if (parentJson != null && StringUtils.isNotEmpty(tempPid) && tempPid.equals(parentJson.getString("id"))) {
                // 类型( 0：一级菜单 1：子菜单 2：按钮 )
                if (permission.getMenuType().equals(CommonConstant.MENU_TYPE_2)) {
                    JSONObject metaJson = parentJson.getJSONObject("meta");
                    if (metaJson.containsKey("permissionList")) {
                        metaJson.getJSONArray("permissionList").add(json);
                    } else {
                        JSONArray permissionList = new JSONArray();
                        permissionList.add(json);
                        metaJson.put("permissionList", permissionList);
                    }
                    // 类型( 0：一级菜单 1：子菜单 2：按钮 )
                } else if (permission.getMenuType().equals(CommonConstant.MENU_TYPE_1) || permission.getMenuType().equals(CommonConstant.MENU_TYPE_0)) {
                    if (parentJson.containsKey("children")) {
                        parentJson.getJSONArray("children").add(json);
                    } else {
                        JSONArray children = new JSONArray();
                        children.add(json);
                        parentJson.put("children", children);
                    }

                    if (!permission.isLeaf()) {
                        getPermissionJsonArray(jsonArray, metaList, json);
                    }
                }
            }

        }
    }

    /**
     * 根据菜单配置生成路由json
     *
     * @param permission
     * @return
     */
    private JSONObject getPermissionJsonObject(SysPermission permission) {
        JSONObject json = new JSONObject();
        // 类型(0：一级菜单 1：子菜单 2：按钮)
        if (permission.getMenuType().equals(CommonConstant.MENU_TYPE_2)) {
            //json.put("action", permission.getPerms());
            //json.put("type", permission.getPermsType());
            //json.put("describe", permission.getName());
            return null;
        } else if (permission.getMenuType().equals(CommonConstant.MENU_TYPE_0) || permission.getMenuType().equals(CommonConstant.MENU_TYPE_1)) {
            json.put("id", permission.getId());
            if (permission.isRoute()) {
                json.put("route", "1");// 表示生成路由
            } else {
                json.put("route", "0");// 表示不生成路由
            }

            if (isWWWHttpUrl(permission.getUrl())) {
                json.put("path", MD5Util.MD5Encode(permission.getUrl(), "utf-8"));
            } else {
                json.put("path", permission.getUrl());
            }

            // 重要规则：路由name (通过URL生成路由name,路由name供前端开发，页面跳转使用)
            if (StringUtils.isNotEmpty(permission.getComponentName())) {
                json.put("name", permission.getComponentName());
            } else {
                json.put("name", urlToRouteName(permission.getUrl()));
            }
            JSONObject meta = new JSONObject();
            // 是否隐藏路由，默认都是显示的
            json.put("hidden", permission.isHidden());
            // 聚合路由
            json.put("alwaysShow", permission.isAlwaysShow());
            json.put("component", permission.getComponent());
            // 由用户设置是否缓存页面 用布尔值
            meta.put("keepAlive", permission.isKeepAlive());

            /*update_begin author:wuxianquan date:20190908 for:往菜单信息里添加外链菜单打开方式 */
            //外链菜单打开方式
            meta.put("internalOrExternal", permission.isInternalOrExternal());
            /* update_end author:wuxianquan date:20190908 for: 往菜单信息里添加外链菜单打开方式*/

            meta.put("title", permission.getName());

            //update-begin--Author:scott  Date:20201015 for：路由缓存问题，关闭了tab页时再打开就不刷新 #842
            String component = permission.getComponent();
            if (StringUtils.isNotEmpty(permission.getComponentName()) || StringUtils.isNotEmpty(component)) {
                meta.put("componentName", StringUtils.isEmpty(permission.getComponentName()) ? component.substring(component.lastIndexOf("/") + 1) : permission.getComponentName());
            }
            //update-end--Author:scott  Date:20201015 for：路由缓存问题，关闭了tab页时再打开就不刷新 #842

            if (StringUtils.isEmpty(permission.getParentId())) {
                // 一级菜单跳转地址
                json.put("redirect", permission.getRedirect());
                if (StringUtils.isNotEmpty(permission.getIcon())) {
                    meta.put("icon", permission.getIcon());
                }
            } else {
                if (StringUtils.isNotEmpty(permission.getIcon())) {
                    meta.put("icon", permission.getIcon());
                }
            }
            if (isWWWHttpUrl(permission.getUrl())) {
                meta.put("url", permission.getUrl());
            }
            // update-begin--Author:sunjianlei  Date:20210918 for：新增适配vue3项目的隐藏tab功能
            meta.put("hideTab", permission.isHideTab());
            // update-end--Author:sunjianlei  Date:20210918 for：新增适配vue3项目的隐藏tab功能
            json.put("meta", meta);
        }

        return json;
    }

    /**
     * 判断是否外网URL 例如： http://localhost:8080/jeecg-boot/swagger-ui.html#/ 支持特殊格式： {{
     * window._CONFIG['domianURL'] }}/druid/ {{ JS代码片段 }}，前台解析会自动执行JS代码片段
     *
     * @return
     */
    private boolean isWWWHttpUrl(String url) {
        if (url != null && (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("{{"))) {
            return true;
        }
        return false;
    }

    /**
     * 通过URL生成路由name（去掉URL前缀斜杠，替换内容中的斜杠‘/’为-） 举例： URL = /isystem/role RouteName =
     * isystem-role
     *
     * @return
     */
    private String urlToRouteName(String url) {
        if (StringUtils.isNotEmpty(url)) {
            if (url.startsWith("/")) {
                url = url.substring(1);
            }
            url = url.replace("/", "-");

            // 特殊标记
            url = url.replace(":", "@");
            return url;
        } else {
            return null;
        }
    }


}
