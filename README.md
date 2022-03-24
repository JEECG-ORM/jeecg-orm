![JEECG-ORM](https://images.gitee.com/uploads/images/2022/0323/232707_6740b49e_7394744.png "JeecgORM低代码开发平台")


JEECG ORM 低代码开发平台（前后端分离版本）
===============

当前最新版本： 1.0.0（发布日期：2022-03-23）



项目介绍：
-----------------------------------

JeecgORM 是一款基于代码生成器的`低代码平台`！前后端分离架构 SpringBoot2.x，SpringCloud，Ant Design&Vue，EbeanORM，Shiro，JWT，支持微服务。强大的代码生成器让前后端代码一键生成，实现低代码开发! 帮助解决Java项目70%的重复工作，让开发更多关注业务。既能快速提高效率，节省研发成本，同时又不失灵活性！

适用项目
-----------------------------------
Jeecg-Boot低代码开发平台，可以应用在任何J2EE项目的开发中，尤其适合SAAS项目、企业信息管理系统（MIS）、内部办公系统（OA）、企业资源计划系统（ERP）、客户关系管理系统（CRM）等，可以显著提高开发效率70%以上，极大降低开发成本。


技术文档
-----------------------------------

- 技术官网：  [http://www.jeecgorm.com](http://www.jeecgorm.com)

- 在线演示 ： [http://jo.jeecgorm.com](http://jo.jeecgorm.com)

- 开发文档： 暂无

- 入门视频： 暂无

- 新手指南： 暂无

- 微服务开发： 暂不支持

- 微信交流群 ：18813066492 


为什么选择JEECG-BOOT?
-----------------------------------
* 1.采用最新主流前后分离框架（Springboot+JPA(EbeanORM)+antd），容易上手; 代码生成器依赖性低,灵活的扩展能力，可快速实现二次开发;
* 2.支持微服务SpringCloud Alibaba(Nacos、Gateway、Sentinel、Skywalking)，提供切换机制支持单体和微服务自由切换
* 3.开发效率高,采用代码生成器，单表、树列表、一对多、一对一等数据模型，增删改查功能一键生成，菜单配置直接使用；
* 4.代码生成器提供强大模板机制，支持自定义模板，目前提供四套风格模板（单表两套、树模型一套、一对多三套）
* 5.代码生成器非常智能，在线业务建模、在线配置、所见即所得支持23种类控件，一键生成前后端代码，大幅度提升开发效率，不再为重复工作发愁。
* 6.封装完善的用户、角色、菜单、组织机构、数据字典、在线定时任务等基础功能，支持访问授权、按钮权限、数据权限等功能
* 7.常用共通封装，各种工具类(定时任务,短信接口,邮件发送,Excel导入导出等),基本满足80%项目需求
* 8.简易Excel导入导出，支持单表导出和一对多表模式导出，生成的代码自带导入导出功能
* 9.集成简易报表工具，图像报表和数据导出非常方便，可极其方便的生成图形报表、pdf、excel、word等报表；
* 10.采用前后分离技术，页面UI风格精美，针对常用组件做了封装：时间、行表格控件、截取显示控件、报表组件，编辑器等等
* 11.查询过滤器：查询功能自动生成，后台动态拼SQL追加查询条件；支持多种匹配方式（全匹配/模糊查询/包含查询/不匹配查询）；
* 12.数据权限（精细化数据权限控制，控制到行级，列表级，表单字段级，实现不同人看不同数据，不同人对同一个页面操作不同字段
* 13.页面校验自动生成(必须输入、数字校验、金额校验、时间空间等);
* 14.分布式文件服务，集成minio、阿里OSS等优秀的第三方，提供便捷的文件上传与管理，同时也支持本地存储。
* 15.提供单点登录CAS集成方案，项目中已经提供完善的对接代码
* 16.专业接口对接机制，统一采用restful接口方式，集成swagger-ui在线接口文档，Jwt token安全验证，方便客户端对接
* 17.接口安全机制，可细化控制接口授权，非常简便实现不同客户端只看自己数据等控制
* 18.提供各种系统监控，实时跟踪系统运行情况（监控 Redis、Tomcat、jvm、服务器信息、请求追踪、SQL监控）
* 19.消息中心（支持短信、邮件、微信推送等等）
* 20.集成Websocket消息通知机制
* 21.移动自适应效果优秀，提供APP发布方案：
* 22.支持多语言，提供国际化方案；
* 23.数据变更记录日志，可记录数据每次变更内容，通过版本对比功能查看历史变化
* 24.平台UI强大，实现了移动自适应
* 25.平台首页风格，提供多种组合模式，支持自定义风格
* 26.提供简单易用的打印插件，支持谷歌、火狐、IE11+ 等各种浏览器
* 27.示例代码丰富，提供很多学习案例参考
* 28.采用maven分模块开发方式
* 29.支持菜单动态路由
* 30.权限控制采用 RBAC（Role-Based Access Control，基于角色的访问控制）
* 31.提供新行编辑表格JVXETable，轻松满足各种复杂ERP布局，拥有更高的性能、更灵活的扩展、更强大的功能

 
 
 
技术架构：
-----------------------------------
#### 开发环境

- 语言：Java 8+ (小于17)

- IDE(JAVA)： IDEA (必须安装lombok插件 Ebean enhancer增强插件)

- IDE(前端)： IDEA 或者 WebStorm

- 依赖管理：Maven

- 缓存：Redis

- 数据库脚本：MySQL5.7+  &  Oracle 11g & Sqlserver2017（默认只提供三个库脚本，其他库需要自己转）


#### 后端

- 基础框架：Spring Boot 2.3.5.RELEASE

- 微服务框架： Spring Cloud Alibaba 2.2.3.RELEASE(暂未使用)

- 持久层框架：EbeanOrm 12.12.1 

- 报表工具： JimuReport 1.4.32(暂未使用)

- 安全框架：Apache Shiro 1.8.0，Jwt 3.11.0

- 微服务技术栈：Spring Cloud Alibaba、Nacos、Gateway、Sentinel、Skywalking(暂未使用)

- 数据库连接池：阿里巴巴Druid 1.1.22(暂未使用)

- 日志打印：logback

- 其他：autopoi, fastjson，poi，Swagger-ui，quartz, lombok（简化代码）等。


#### 前端
 
- [Vue 2.6.10](https://cn.vuejs.org/),[Vuex](https://vuex.vuejs.org/zh/),[Vue Router](https://router.vuejs.org/zh/)
- [Axios](https://github.com/axios/axios)
- [ant-design-vue](https://vuecomponent.github.io/ant-design-vue/docs/vue/introduce-cn/)
- [webpack](https://www.webpackjs.com/),[yarn](https://yarnpkg.com/zh-Hans/)
- [vue-cropper](https://github.com/xyxiao001/vue-cropper) - 头像裁剪组件
- [@antv/g2](https://antv.alipay.com/zh-cn/index.html) - Alipay AntV 数据可视化图表
- [Viser-vue](https://viserjs.github.io/docs.html#/viser/guide/installation)  - antv/g2 封装实现
- eslint，[@vue/cli 3.2.1](https://cli.vuejs.org/zh/guide)
- vue-print-nb-jeecg - 打印


#### 支持库

|  数据库   |  支持   |
| --- | --- |
|   MySQL   |  √   |
|  Oracle11g   |  x   |
|  Sqlserver2017   |  x   |
|   PostgreSQL   |  x   |
|   DB2、Informix   |  x   |
|   MariaDB   |  x   |
|  SQLite、Hsqldb、Derby、H2   |  x   |
|   达梦、人大金仓、神通   |  x   |
|   华为高斯、虚谷、瀚高数据库   |  x   |
|   阿里云PolarDB、PPAS、HerdDB   |  x   |
|  Hive、HBase、CouchBase   |  x   |


## 微服务解决方案(暂未使用)


1、服务注册和发现 Nacos x

2、统一配置中心 Nacos  x

3、路由网关 gateway(三种加载方式) x

4、分布式 http feign x

5、熔断和降级 Sentinel x

6、分布式文件 Minio、阿里OSS x

7、统一权限控制 JWT + Shiro √

8、服务监控 SpringBootAdmin x

9、链路跟踪 Skywalking  x [参考文档](https://www.kancloud.cn/zhangdaiscott/jeecgcloud/1771670)

10、消息中间件 RabbitMQ  x

11、分布式任务 xxl-job  x

12、分布式事务 Seata  x

13、分布式日志 elk + kafka x

14、支持 docker-compose、k8s、jenkins x

15、CAS 单点登录   x

16、路由限流   x


### 功能模块
```
├─系统管理
│  ├─用户管理
│  ├─角色管理
│  ├─菜单管理
│  ├─权限设置（支持按钮权限、数据权限）
│  └─字典管理
│  └─分类字典
├─消息中心(开发中)
│  ├─消息管理
│  ├─模板管理
├─代码生成器(低代码)
│  ├─代码生成器功能（一键生成前后端代码，生成后无需修改直接用，绝对是后端开发福音）
│  ├─代码生成器模板（提供4套模板，分别支持单表和一对多模型，不同风格选择）(开发中)
│  ├─代码生成器模板（生成代码，自带excel导入导出）(开发中)
│  ├─查询过滤器（查询逻辑无需编码，系统根据页面配置自动生成）
│  ├─高级查询器（弹窗自动组合查询条件）(开发中)
│  ├─Excel导入导出工具集成（支持单表，一对多 导入导出）(开发中)
│  ├─平台移动自适应支持
├─系统监控
│  ├─Gateway路由网关(开发中)
│  ├─性能扫描监控
│  │  ├─监控 Redis
│  │  ├─Tomcat
│  │  ├─jvm
│  │  ├─服务器信息
│  │  ├─请求追踪
│  │  ├─磁盘监控
│  ├─定时任务
│  ├─系统日志
│  ├─消息中心（支持短信、邮件、微信推送等等）
│  ├─数据日志（记录数据快照，可对比快照，查看数据变更情况）
│  ├─系统通知
│  ├─SQL监控
│  ├─swagger-ui(在线接口文档)
│─常用示例
│  ├─自定义组件
│  ├─对象存储(对接阿里云)
│  ├─JVXETable示例（各种复杂ERP布局示例）
│  ├─单表模型例子
│  └─一对多模型例子
│  └─打印例子
│  └─一对多TAB例子
│  └─内嵌table例子
│  └─常用选择组件
│  └─异步树table
│  └─接口模拟测试
│  └─表格合计示例
│  └─异步树列表示例
│  └─一对多JEditable
│  └─JEditable组件示例
│  └─图片拖拽排序
│  └─图片翻页
│  └─图片预览
│  └─PDF预览
│  └─分屏功能
│─封装通用组件	
│  ├─行编辑表格JEditableTable
│  └─省略显示组件
│  └─时间控件
│  └─高级查询
│  └─用户选择组件
│  └─报表组件封装
│  └─字典组件
│  └─下拉多选组件
│  └─选人组件
│  └─选部门组件
│  └─通过部门选人组件
│  └─封装曲线、柱状图、饼状图、折线图等等报表的组件（经过封装，使用简单）
│  └─在线code编辑器
│  └─上传文件组件
│  └─验证码组件
│  └─树列表组件
│  └─表单禁用组件
│  └─等等
│─更多页面模板
│  ├─各种高级表单
│  ├─各种列表效果
│  └─结果页面
│  └─异常页面
│  └─个人页面
└─其他模块 (暂不开源)
   └─更多功能开发中。。
   
```





系统效果
----

##### PC端
![输入图片说明](https://static.oschina.net/uploads/img/201904/14155402_AmlV.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201904/14160657_cHwb.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201904/14160813_KmXS.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201904/14160935_Nibs.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201904/14161004_bxQ4.png "在这里输入图片标题")


##### 在线接口文档
![输入图片说明](https://static.oschina.net/uploads/img/201908/27095258_M2Xq.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201904/14160957_hN3X.png "在这里输入图片标题")


##### 报表
![输入图片说明](https://static.oschina.net/uploads/img/201904/14160828_pkFr.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201904/14160834_Lo23.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201904/14160842_QK7B.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201904/14160849_GBm5.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201904/14160858_6RAM.png "在这里输入图片标题")


##### 手机端
![](https://oscimg.oschina.net/oscnet/da543c5d0d57baab0cecaa4670c8b68c521.jpg)
![](https://oscimg.oschina.net/oscnet/fda4bd82cab9d682de1c1fbf2060bf14fa6.jpg)

##### PAD端
![](https://oscimg.oschina.net/oscnet/e90fef970a8c33790ab03ffd6c4c7cec225.jpg)
![](https://oscimg.oschina.net/oscnet/d78218803a9e856a0aa82b45efc49849a0c.jpg)
![](https://oscimg.oschina.net/oscnet/0404054d9a12647ef6f82cf9cfb80a5ac02.jpg)
![](https://oscimg.oschina.net/oscnet/59c23b230f52384e588ee16309b44fa20de.jpg)


## 捐赠 

如果觉得还不错，请作者喝杯咖啡吧 ☺

![微信扫一扫捐赠](%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20220324102719.jpg)



