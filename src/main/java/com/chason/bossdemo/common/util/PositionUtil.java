package com.chason.bossdemo.common.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

public class PositionUtil {
    private static final String IP_URL = "https://apis.map.qq.com/ws/location/v1/ip";
    private static final String SK = "7zIIkTOzGBGctCYkSkAepLkJ2FP0p8b3";
    private static final String KEY = "FFZBZ-QYBKG-OFKQQ-IQPH3-YY7H7-VSFVR";

    public static JSONObject getPositionFromIp(String ip) {
        if (StringUtils.isEmpty(ip)) {
            return null;
        }
        String sign = Md5Util.getMD5(("/ws/location/v1/ip?callback=function1&ip=" + ip + "&key=" + KEY + "&output=json" + SK).getBytes());
        String res = HttpUtil.sendHttpGetReqToServer(IP_URL + "?callback=function1&ip=" + ip + "&key=" + KEY + "&output=json" + "&sig=" + sign);
        if (StringUtils.isEmpty(res)) {
            return null;
        }

        JSONObject resJsong = JSONObject.parseObject(res);
        return resJsong;
    }

    public static void main(String[] args) {
        PositionUtil.getPositionFromIp("116.24.65.120");
    }
}
