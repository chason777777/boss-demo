package com.chason.bossdemo.infrastructure.redis.util;

/**
 * Created by chason on 2018/3/21.16:31
 */
public class RedisKeyUtil {
    private final static String KEY_SPLIT = "-";
    private final static String KEYS_SPLITER = "#";
    private final static String LOGIN_TOKEN = "orangeota_boss:token";


    /**
     * 获取用户登录token KEY
     *
     * @param userUuid
     * @return
     */
    public static String getUserLoginKey(String userUuid) {
        return LOGIN_TOKEN + KEY_SPLIT + userUuid;
    }
}
