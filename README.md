![JEECG-ORM](https://images.gitee.com/uploads/images/2022/0323/232707_6740b49e_7394744.png "JeecgORM低代码开发平台")


JEECG ORM 低代码开发平台（前后端分离版本）
===============

当前最新版本： 1.0.0（发布日期：2022-03-23）



项目介绍：
-----------------------------------


JEECG-ORM 是一款基于代码生成器的`低代码平台`！前后端分离架构 SpringBoot2.x，SpringCloud，Ant Design&Vue，Mybatis-plus，Shiro，JWT，支持微服务。强大的代码生成器让前后端代码一键生成，实现低代码开发!  帮助解决Java项目70%的重复工作，让开发更多关注业务。既能快速提高效率，节省研发成本，同时又不失灵活性！

适用项目
-----------------------------------
JEECG-ORM低代码开发平台，可以应用在任何J2EE项目的开发中，尤其适合CMS项目、企业信息管理系统（MIS）、内部办公系统（OA）、企业资源计划系统（ERP）、客户关系管理系统（CRM）等，其半智能手工Merge的开发方式，可以显著提高开发效率70%以上，极大降低开发成本。


技术文档
-----------------------------------

- 技术官网：  [http://www.jeecgorm.com](http://www.jeecgorm.com)

- 在线演示 ： [http://jo.jeecgorm.com](http://jo.jeecgorm.com)

- 开发文档：  [http://doc.jeecgorm.com](http://doc.jeecgorm.com)

- 入门视频： 制作中

- 新手指南： 制作中

- 微服务开发： 开发中

- 微信交流群 ：18813066492
 
 
技术架构：
-----------------------------------
#### 开发环境

- 语言：Java 8+ (小于17)

- IDE(JAVA)： IDEA (必须安装lombok插件 Ebean enhancer 12.12.1增强插件 )

- IDE(前端)： IDEA 或者 WebStorm

- 依赖管理：Maven

- 缓存：Redis

- 数据库脚本：MySQL5.7+  &  Oracle 11g & Sqlserver2017（默认只提供三个库脚本，其他库需要自己转）


#### 后端

- 基础框架：Spring Boot 2.3.5.RELEASE

- 持久层框架：Ebean 12.12.1 

- 安全框架：Apache Shiro 1.8.0，Jwt 3.11.0

- 微服务技术栈：Spring Cloud Alibaba、Nacos、Gateway、Sentinel、Skywalking

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

#### 项目下载和运行（前端项目）

#### 拉取项目代码
- git clone https://gitee.com/jeecg-salter/jeecg-orm.git
- cd  jeecg-orm/ant-design-vue-jo
- 安装node.js
- 如果您电脑未安装Node.js，请安装它。
#### 验证
- 出现相应npm版本即可
- npm -v
#### 出现相应node版本即可
- node -v
#### 全局安装yarn
npm i -g yarn
#### 验证
- yarn -v # 出现对应版本号即代表安装成功
- 切换到ant-design-vue-jo文件夹下
#### 下载依赖
- yarn install
#### 启动
- yarn run serve
#### 编译项目
- yarn run build
#### 接口地址配置
- .env.development
- NODE_ENV=development
- VUE_APP_API_BASE_URL=http://localhost:8080/jeecg-orm


#### 支持库

|  数据库   |  支持   |
| --- | --- |
|   MySQL   |  √   |
|  Oracle11g   |  X   |
|  Sqlserver2017   |  X   |
|   PostgreSQL   |  X   |
|   DB2、Informix   |  X   |
|   MariaDB   |  X   |
|  SQLite、Hsqldb、Derby、H2   |  X   |
|   达梦、人大金仓、神通   |  X   |
|   华为高斯、虚谷、瀚高数据库   |  X   |
|   阿里云PolarDB、PPAS、HerdDB   |  X   |
|  Hive、HBase、CouchBase   |  X   |


### 功能模块
```
├─系统管理
│  ├─用户管理
│  ├─角色管理
│  ├─菜单管理
│  └─字典管理
│  └─分类字典
│  └─代码生成
├─内容管理
│  ├─栏目管理
│  └─栏目内容管理
└─其他模块 (暂不开源)
   └─更多功能开发中。。
   
```

系统效果
----

## 捐赠 

如果觉得还不错，请作者喝杯咖啡吧 ☺
![输入图片说明](%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20220324102719.jpg)



