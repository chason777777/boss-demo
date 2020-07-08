package com.chason.bossdemo.common.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author zhang bo
 * @version 1.0
 * @Description
 * @date 2017-12-08
 */
public class VerifyParamsUtil {

    private static Logger logger = LoggerFactory.getLogger(VerifyParamsUtil.class);

    /**
     * 检验参数是否正确
     */
    @SuppressWarnings("Duplicates")
    public static boolean verifyParams(JSONObject params, JSONObject verify) {
        logger.debug("VerifyParamsUtil==verifyParams==params = " + verify);
        if (params != verify && params.size() > 0 && null != verify && verify.size() > 0) {
            Boolean isFlag = true;
            Set<Map.Entry<String, Object>> set = verify.entrySet();
            for (Map.Entry<String, Object> entry : set) {
                try {
                    Class<?> clazz = Class.forName(entry.getValue().toString());
                    if (Objects.nonNull(params.get(entry.getKey())) && clazz.isInstance(params.get(entry.getKey()))) {
                        if (params.get(entry.getKey()) instanceof String
                                && params.get(entry.getKey()).toString().trim().length() <= 0) {
                            isFlag = false;//参数类型校验失败
                            break;
                        }
                    } else {
                        isFlag = false;//参数类型校验失败
                        break;
                    }
                } catch (Exception e) {
                    logger.warn("VerifyParamsUtil==verifyParams==params cast type is Fail");
                }
            }
            if (isFlag) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

}