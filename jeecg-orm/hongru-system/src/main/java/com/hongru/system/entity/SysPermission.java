package com.hongru.system.entity;

import com.hongru.ebean.HongRuEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 菜单权限表
 * </p>
 *
 * @Author scott
 * @since 2018-12-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
public class SysPermission extends HongRuEntity {

	/**
	 * 父id
	 */
	private String parentId;

	private String tableId;

	/**
	 * 菜单名称
	 */
	private String name;

	/**
	 * 菜单权限编码，例如：“sys:schedule:list,sys:schedule:info”,多个逗号隔开
	 */
	private String perms;
	/**
	 * 权限策略1显示2禁用
	 */
	private String permsType;

	/**
	 * 菜单图标
	 */
	private String icon;

	/**
	 * 组件
	 */
	private String component;

	/**
	 * 组件名字
	 */
	private String componentName;

	/**
	 * 路径
	 */
	private String url;
	/**
	 * 一级菜单跳转地址
	 */
	private String redirect;

	/**
	 * 类型（0：一级菜单；1：子菜单 ；2：按钮权限）
	 */
	private Integer menuType;

	/**
	 * 是否叶子节点: 1:是  0:不是
	 */
	private boolean isLeaf;

	/**
	 * 是否路由菜单: 0:不是  1:是（默认值1）
	 */
	private boolean isRoute;


	/**
	 * 是否缓存页面: 0:不是  1:是（默认值1）
	 */
	private boolean keepAlive;

	/**
	 * 描述
	 */
	private String description;


	/**
	 * 是否配置菜单的数据权限 1是0否 默认0
	 */
	private Integer ruleFlag;

	/**
	 * 是否隐藏路由菜单: 0否,1是（默认值0）
	 */
	private boolean hidden;

	/**
	 * 是否隐藏Tab: 0否,1是（默认值0）
	 */
	private boolean hideTab;

	/**按钮权限状态(0无效1有效)*/
	private String status;

	/**alwaysShow*/
    private boolean alwaysShow;

	/*update_begin author:wuxianquan date:20190908 for:实体增加字段 */
    /** 外链菜单打开方式 0/内部打开 1/外部打开 */
    private boolean internalOrExternal;
	/*update_end author:wuxianquan date:20190908 for:实体增加字段 */


    public SysPermission() {

    }
    public SysPermission(boolean index) {
    	if(index) {
    		this.setId("9502685863ab87f0ad1134142788a385");
        	this.name="首页";
        	this.component="dashboard/Analysis";
        	this.componentName="dashboard-analysis";
        	this.url="/dashboard/analysis";
        	this.icon="home";
        	this.menuType=0;
        	this.setSortNo(0.0);
        	this.ruleFlag=0;
        	this.alwaysShow=false;
        	this.isRoute=true;
        	this.keepAlive=true;
        	this.isLeaf=true;
        	this.hidden=false;
    	}

    }
}
