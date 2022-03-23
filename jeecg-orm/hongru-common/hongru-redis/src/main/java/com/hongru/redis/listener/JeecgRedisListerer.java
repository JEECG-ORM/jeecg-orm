package com.hongru.redis.listener;


import com.hongru.redis.base.BaseMap;

/**
 * 自定义消息监听
 */
public interface JeecgRedisListerer {

    void onMessage(BaseMap message);

}
