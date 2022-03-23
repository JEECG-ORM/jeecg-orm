package com.hongru.system.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class SysPermissionTree implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	private String id;
	private String tableId;
	private String key;
	private String title;

	/**
	 * 父id
	 */
	private String parentId;

	/**
	 * 菜单名称
	 */
	private String name;

	/**
	 * 菜单权限编码
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
	 * 跳转网页链接
	 */
	private String url;

	/**
	 * 一级菜单跳转地址
	 */
	private String redirect;

	/**
	 * 菜单排序
	 */
	private Double sortNo;

	/**
	 * 类型（0：一级菜单；1：子菜单 ；2：按钮权限）
	 */
	private Integer menuType;

	/**
	 * 是否叶子节点: 1:是 0:不是
	 */
	private boolean isLeaf;

	/**
	 * 是否路由菜单: 0:不是  1:是（默认值1）
	 */
	private boolean route;


	/**
	 * 是否路缓存页面: 0:不是  1:是（默认值1）
	 */
	private boolean keepAlive;

	/**
	 * 描述
	 */
	private String description;

	/**alwaysShow*/
    private boolean alwaysShow;
    /**是否隐藏路由菜单: 0否,1是（默认值0）*/
    private boolean hidden;

    /**按钮权限状态(0无效1有效)*/
	private String status;

	/*update_begin author:wuxianquan date:20190908 for:model增加字段 */
	/** 外链菜单打开方式 0/内部打开 1/外部打开 */
	private boolean internalOrExternal;
	/*update_end author:wuxianquan date:20190908 for:model增加字段 */

	private List<SysPermissionTree> children;

}
