package com.hongru.constant;

public interface CommonConstant {

    /** {@code 200 OK} (HTTP/1.0 - RFC 1945) */
    public static final Integer SC_OK_200 = 200;
    /** {@code 500 Server Error} (HTTP/1.0 - RFC 1945) */
    public static final Integer SC_INTERNAL_SERVER_ERROR_500 = 500;
    /**访问权限认证未通过 510*/
    public static final Integer SC_JEECG_NO_AUTHZ=510;
    /**
     * 微服务读取配置文件属性 服务地址
     */
    public final static String CLOUD_SERVER_KEY = "spring.cloud.nacos.discovery.server-addr";
    /**
     * 是否用户已被冻结 1正常(解冻) 2冻结
     */
    public static final Integer USER_UNFREEZE = 1;
    public static final Integer USER_FREEZE = 2;

    /** 登录用户Token令牌缓存KEY前缀 */
    public static final String PREFIX_USER_TOKEN  = "prefix_user_token_";
    public final static String X_ACCESS_TOKEN = "X-Access-Token";
    public final static String TOKEN_IS_INVALID_MSG = "Token失效，请重新登录!";

    /**
     *  0：一级菜单
     */
    public static final Integer MENU_TYPE_0  = 0;
    /**
     *  1：子菜单
     */
    public static final Integer MENU_TYPE_1  = 1;
    /**
     *  2：按钮权限
     */
    public static final Integer MENU_TYPE_2  = 2;

    /**
     * 状态(0无效1有效)
     */
    public static final String STATUS_0 = "0";
    public static final String STATUS_1 = "1";
    /**字典翻译文本后缀*/
    public static final String DICT_TEXT_SUFFIX = "_dictText";
    /**
     * 文件上传类型（本地：local，Minio：minio，阿里云：alioss）
     */
    public static final String UPLOAD_TYPE_LOCAL = "local";
    public static final String UPLOAD_TYPE_MINIO = "minio";
    public static final String UPLOAD_TYPE_OSS = "alioss";
    /**
     * 删除标志
     */
    public static final Integer DEL_FLAG_1 = 1;

    /**
     * 未删除
     */
    public static final Integer DEL_FLAG_0 = 0;
    /** 登录用户Shiro权限缓存KEY前缀 */
    public static String PREFIX_USER_SHIRO_CACHE  = "shiro:cache:org.jeecg.config.shiro.ShiroRealm.authorizationCache:";
}
