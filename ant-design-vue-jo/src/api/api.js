import { getAction, deleteAction, putAction, postAction, httpAction } from '@/api/manage'
import Vue from 'vue'
import {UI_CACHE_DB_DICT_DATA } from "@/store/mutation-types"

//健康课堂管理
const addCourse = (params)=>postAction("/cms/course/add",params);
const editCourse = (params)=>postAction("/cms/course/edit",params);

//内容管理
const addContent = (params)=>postAction("/cms/content/add",params);
const editContent = (params)=>postAction("/cms/content/edit",params);


//代码生成
const addTable = (params)=>postAction("/gen/table/add",params);
const editTable = (params)=>postAction("/gen/table/edit",params);
const generateFile = (params)=>getAction("/gen/table/generateFile",params);
const queryColumnList = (params)=>postAction("/gen/tableColumn/list",params);

//项目管理
const addLbs = (params)=>postAction("/sys/lbs/add",params);
const editLbs = (params)=>postAction("/sys/lbs/edit",params);

//角色管理
const addRole = (params)=>postAction("/sys/role/add",params);
const editRole = (params)=>postAction("/sys/role/edit",params);
const checkRoleCode = (params)=>getAction("/sys/role/checkRoleCode",params);
const roleList = (params)=>postAction("/sys/role/list",params);

//用户管理
const addUser = (params)=>postAction("/sys/user/add",params);
const editUser = (params)=>postAction("/sys/user/edit",params);
const queryUserRole = (params)=>getAction("/sys/user/queryUserRole",params);
const getUserList = (params)=>getAction("/sys/user/list",params);
const frozenBatch = (params)=>putAction("/sys/user/frozenBatch",params);
//验证用户是否存在
const checkOnlyUser = (params)=>getAction("/sys/user/checkOnlyUser",params);
//改变密码
const changePassword = (params)=>postAction("/sys/user/changePassword",params);

//权限管理
const addPermission= (params)=>postAction("/sys/permission/add",params);
const editPermission= (params)=>postAction("/sys/permission/edit",params);
const getPermissionList = (params)=>getAction("/sys/permission/list",params);
const getSystemMenuList = (params)=>getAction("/sys/permission/getSystemMenuList",params);
const getSystemSubmenu = (params)=>getAction("/sys/permission/getSystemSubmenu",params);
const getSystemSubmenuBatch = (params) => getAction('/sys/permission/getSystemSubmenuBatch', params)
const queryTreeList = (params)=>getAction("/sys/permission/queryTreeList",params);
const queryTreeListForRole = (params)=>getAction("/sys/role/queryTreeList",params);
const queryListAsync = (params)=>getAction("/sys/permission/queryListAsync",params);
const queryRolePermission = (params)=>getAction("/sys/permission/queryRolePermission",params);
const saveRolePermission = (params)=>postAction("/sys/permission/saveRolePermission",params);
const queryPermissionsByUser = ()=>getAction("/sys/permission/getUserPermission");
const loadAllRoleIds = (params)=>getAction("/sys/permission/loadAllRoleIds",params);
const getPermissionRuleList = (params)=>getAction("/sys/permission/getPermRuleListByPermId",params);
const queryPermissionRule = (params)=>getAction("/sys/permission/queryPermissionRule",params);

// 部门管理
const queryDepartTreeList = (params)=>getAction("/sys/sysDepart/queryTreeList",params);
const queryDepartTreeSync = (params)=>getAction("/sys/sysDepart/queryDepartTreeSync",params);
const queryIdTree = (params)=>getAction("/sys/sysDepart/queryIdTree",params);
const queryParentName   = (params)=>getAction("/sys/sysDepart/queryParentName",params);
const searchByKeywords   = (params)=>getAction("/sys/sysDepart/searchBy",params);
const deleteByDepartId   = (params)=>deleteAction("/sys/sysDepart/delete",params);

//二级部门管理
const queryDepartPermission = (params)=>getAction("/sys/permission/queryDepartPermission",params);
const saveDepartPermission = (params)=>postAction("/sys/permission/saveDepartPermission",params);
const queryTreeListForDeptRole = (params)=>getAction("/sys/sysDepartPermission/queryTreeListForDeptRole",params);
const queryDeptRolePermission = (params)=>getAction("/sys/sysDepartPermission/queryDeptRolePermission",params);
const saveDeptRolePermission = (params)=>postAction("/sys/sysDepartPermission/saveDeptRolePermission",params);
const queryMyDepartTreeList = (params)=>getAction("/sys/sysDepart/queryMyDeptTreeList",params);

//日志管理
const deleteLog = (params)=>deleteAction("/sys/log/delete",params);
const deleteLogList = (params)=>deleteAction("/sys/log/deleteBatch",params);

//数据字典
const addDict = (params)=>postAction("/sys/dict/add",params);
const editDict = (params)=>postAction("/sys/dict/edit",params);
const treeList = (params)=>getAction("/sys/dict/treeList",params);
const addDictItem = (params)=>postAction("/sys/dictItem/add",params);
const editDictItem = (params)=>putAction("/sys/dictItem/edit",params);

//字典标签专用（通过code获取字典数组）
export const ajaxGetDictItems = (code, params)=>getAction(`/sys/dict/getDictItems/${code}`,params);
//从缓存中获取字典配置
function getDictItemsFromCache(dictCode) {
  if (Vue.ls.get(UI_CACHE_DB_DICT_DATA) && Vue.ls.get(UI_CACHE_DB_DICT_DATA)[dictCode]) {
    let dictItems = Vue.ls.get(UI_CACHE_DB_DICT_DATA)[dictCode];
    //console.log("-----------getDictItemsFromCache----------dictCode="+dictCode+"---- dictItems=",dictItems)
    return dictItems;
  }
}

//系统通告
const doReleaseData = (params)=>getAction("/sys/annountCement/doReleaseData",params);
const doReovkeData = (params)=>getAction("/sys/annountCement/doReovkeData",params);
//获取系统访问量
const getLoginfo = (params)=>getAction("/sys/loginfo",params);
const getVisitInfo = (params)=>getAction("/sys/visitInfo",params);

// 根据部门主键查询用户信息
const queryUserByDepId = (params)=>getAction("/sys/user/queryUserByDepId",params);

// 重复校验
const duplicateCheck = (params)=>getAction("/sys/duplicate/check",params);
// 加载分类字典
const loadCategoryData = (params)=>getAction("/sys/category/loadAllData",params);
const checkRuleByCode = (params) => getAction('/sys/checkRule/checkByCode', params)
//加载我的通告信息
const getUserNoticeInfo= (params)=>getAction("/sys/sysAnnouncementSend/getMyAnnouncementSend",params);
const getTransitURL = url => `/sys/common/transitRESTful?url=${encodeURIComponent(url)}`
// 中转HTTP请求
export const transitRESTful = {
  get: (url, parameter) => getAction(getTransitURL(url), parameter),
  post: (url, parameter) => postAction(getTransitURL(url), parameter),
  put: (url, parameter) => putAction(getTransitURL(url), parameter),
  http: (url, parameter) => httpAction(getTransitURL(url), parameter),
}

export {
  addCourse,
  editCourse,
  addContent,
  editContent,
  addLbs,
  editLbs,
  addTable,
  editTable,
  generateFile,
  queryColumnList,
  // imgView,
  // doMian,
  addRole,
  editRole,
  checkRoleCode,
  addUser,
  editUser,
  queryUserRole,
  getUserList,
  roleList,
  frozenBatch,
  checkOnlyUser,
  changePassword,
  getPermissionList,
  addPermission,
  editPermission,
  queryTreeList,
  queryListAsync,
  queryRolePermission,
  saveRolePermission,
  queryPermissionsByUser,
  loadAllRoleIds,
  getPermissionRuleList,
  queryPermissionRule,
  queryDepartTreeList,
  queryDepartTreeSync,
  queryIdTree,
  queryParentName,
  searchByKeywords,
  deleteByDepartId,
  deleteLog,
  deleteLogList,
  addDict,
  editDict,
  treeList,
  addDictItem,
  editDictItem,
  doReleaseData,
  doReovkeData,
  getLoginfo,
  getVisitInfo,
  queryUserByDepId,
  duplicateCheck,
  queryTreeListForRole,
  getSystemMenuList,
  getSystemSubmenu,
  getSystemSubmenuBatch,
  loadCategoryData,
  checkRuleByCode,
  queryDepartPermission,
  saveDepartPermission,
  queryTreeListForDeptRole,
  queryDeptRolePermission,
  saveDeptRolePermission,
  queryMyDepartTreeList,
  getUserNoticeInfo,
  getDictItemsFromCache
}



