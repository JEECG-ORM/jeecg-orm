package com.hongru.system.service.impl;

import com.hongru.ebean.EbeanUtil;
import com.hongru.system.entity.SysCategory;
import com.hongru.system.service.SysCategoryService;
import com.hongru.util.StringUtil;
import io.ebean.DB;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class SysCategoryImpl implements SysCategoryService {

    @Override
    public void deleteByIds(String id) {
        String allIds = this.queryTreeChildIds(id);
        String pids = this.queryTreePids(id);
        //1.删除时将节点下所有子节点一并删除
        EbeanUtil.deleteBatch(allIds, SysCategory.class);
        //2.将父节点中已经没有下级的节点，修改为没有子节点
        if (StringUtil.isNotEmpty(pids)) {
            String[] pidArr = pids.split(",");
            DB.update(SysCategory.class).set("hasChild", false).where().idIn(pidArr).update();
        }
    }

    /**
     * 递归 根据父id获取子节点id
     *
     * @param pidVal
     * @param sb
     * @return
     */
    private StringBuffer getTreeChildIds(String pidVal, StringBuffer sb) {
        List<SysCategory> dataList = EbeanUtil.initExpressionList(SysCategory.class).where().eq("pid", pidVal).findList();
        if (dataList.size() > 0) {
            for (SysCategory category : dataList) {
                if (!sb.toString().contains(category.getId())) {
                    sb.append(",").append(category.getId());
                }
                this.getTreeChildIds(category.getId(), sb);
            }
        }
        return sb;
    }

    /**
     * 查询节点下所有子节点
     *
     * @param ids
     * @return
     */
    private String queryTreeChildIds(String ids) {
        //获取id数组
        String[] idArr = ids.split(",");
        StringBuffer sb = new StringBuffer();
        for (String pidVal : idArr) {
            if (pidVal != null) {
                if (!sb.toString().contains(pidVal)) {
                    if (sb.toString().length() > 0) {
                        sb.append(",");
                    }
                    sb.append(pidVal);
                    this.getTreeChildIds(pidVal, sb);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 查询需修改标识的父节点ids
     *
     * @param ids
     * @return
     */
    private String queryTreePids(String ids) {
        StringBuffer sb = new StringBuffer();
        //获取id数组
        String[] idArr = ids.split(",");
        for (String id : idArr) {
            if (id != null) {
                SysCategory category = EbeanUtil.initExpressionList(SysCategory.class).idEq(id).findOne();
                //根据id查询pid值
                String metaPid = category.getPid();
                //查询此节点上一级是否还有其他子节点
                List<SysCategory> dataList = EbeanUtil.initExpressionList(SysCategory.class).eq("pid", metaPid).notIn("id", idArr).findList();
                if ((dataList.size() == 0) && !Arrays.asList(idArr).contains(metaPid)
                        && !sb.toString().contains(metaPid)) {
                    //如果当前节点原本有子节点 现在木有了，更新状态
                    sb.append(metaPid).append(",");
                }
            }
        }
        if (sb.toString().endsWith(",")) {
            sb = sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }


}

