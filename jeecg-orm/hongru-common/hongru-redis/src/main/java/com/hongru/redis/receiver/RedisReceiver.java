package com.hongru.redis.receiver;


import cn.hutool.core.util.ObjectUtil;
import com.hongru.redis.base.BaseMap;
import com.hongru.redis.constant.GlobalConstants;
import com.hongru.redis.listener.JeecgRedisListerer;
import com.hongru.redis.util.SpringContextHolder;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author zyf
 */
@Component
@Data
public class RedisReceiver {


    /**
     * 接受消息并调用业务逻辑处理器
     *
     * @param params
     */
    public void onMessage(BaseMap params) {
        Object handlerName = params.get(GlobalConstants.HANDLER_NAME);
        JeecgRedisListerer messageListener = SpringContextHolder.getHandler(handlerName.toString(), JeecgRedisListerer.class);
        if (ObjectUtil.isNotEmpty(messageListener)) {
            messageListener.onMessage(params);
        }
    }

}
