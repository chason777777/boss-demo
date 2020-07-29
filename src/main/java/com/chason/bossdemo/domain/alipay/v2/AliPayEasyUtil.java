package com.chason.bossdemo.domain.alipay.v2;

import com.alibaba.fastjson.JSONObject;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.payment.common.models.AlipayTradeCreateResponse;
import com.alipay.easysdk.payment.common.models.AlipayTradeQueryResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: chason
 * @Date: 2020/7/17 17:31
 * @Description:
 */
public class AliPayEasyUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(AliPayEasyUtil.class);

    {
        // 1. 设置参数（全局只需设置一次）
        Factory.setOptions(getOptions());
    }

    /**
     * 统一收单交易创建
     *
     * @param subject
     * @param out_trade_no
     * @param total_amount
     * @return
     */
    public static JSONObject create(String subject, String out_trade_no, String total_amount) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("notify_url", "");// 回调地址
            AlipayTradeCreateResponse response = Factory.Payment.Common()
//                    .optional("","")// 可选参数
                    .batchOptional(map)// 批量可选参数
                    .create(subject, out_trade_no, total_amount, null);
            return JSONObject.parseObject(response.httpBody);
        } catch (Exception e) {
            LOGGER.error("AliPayEasyUtil create", e);
        }
        return null;
    }

    /**
     * 统一收单线下交易查询
     *
     * @param out_trade_no
     * @return
     */
    public static JSONObject query(String out_trade_no) {
        if (StringUtils.isEmpty(out_trade_no)) {
            return null;
        }
        try {
            AlipayTradeQueryResponse response = Factory.Payment.Common().query(out_trade_no);
            return JSONObject.parseObject(response.httpBody);
        } catch (Exception e) {
            LOGGER.error("AliPayEasyUtil query", e);
        }
        return null;
    }

    /**
     * 异步通知验签
     *
     * @param map
     * @return
     */
    public boolean verifyNotify(Map<String, String> map) {
        if (null == map) {
            return false;
        }
        try {
            return Factory.Payment.Common().verifyNotify(map);
        } catch (Exception e) {
            LOGGER.error("AliPayEasyUtil verifyNotify", e);
        }
        return false;
    }


    /**
     * 配置参数
     * @return
     */
    private static Config getOptions() {
        Config config = new Config();
        config.protocol = "https";
        config.gatewayHost = "openapi.alipay.com";
        config.signType = "RSA2";

        config.appId = "2021001179653880";

        // 为避免私钥随源码泄露，推荐从文件中读取私钥字符串而不是写入源码中
        config.merchantPrivateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCDasiOnwoKm9cxTQde/OTJrbucNjkhNtNKoLF1GXNK2xkjzMUBOiOJM8T7IsSoW6vkuGBx1TVluKmYVT43hEvGIXuYgOl1QmLFx3evAa4/PJRQTM0YRcZyMFWnCAMtZDHSf+EQfuRAz+gUDmzgYDkyaxsd4vs8DyuKZ+Ut1vmdQ5zgcmclFzFpqjVZisuAXXtzHPGGRuG7cGJ3qDucoSzRgOebjqXzo6JFQeF++hrdFe2a6aHfzvBQE48LdJIJ7RXJie3MezIxkFWQEuebq/rj2d1a8YgQnhQDpDL8lbnIFGxNFEb2LdzwO1KbXv5G8cybZODvL0wY4WLBP9POIx+nAgMBAAECggEAMAypC8yGhV55jeMWINriZAMLp5ADz2sWUiDzAVeu2d3GEVrbZRl2yMGO8ivCL7Rk4xmu2bAU9/5OtA3m07Ghau3vHNF1ipQdPSczUV+R8DkcGUgbbRaF+lZF2u6JCt2pgceT6M4ccr4RotOrZ1aHUzHqFkFZjpoVV2OpXdtOJPt/NuQj0oKljOY8fWOBfVvWzrbv2pRPXZ9rYVLzYzdqbDGOMCqHfCAglNVWHDo4l86BQI8ylthSppP//DpntFtqA//YpKmc8AtTAqM2NCFT0Y09T/iHtdd1ujtVu6zqjKUPbGaOc1s89egOd3R/Bin5yQOA08K/Zs/FV3eDnGZ80QKBgQDE7pWX2y3A/3VcTqqJV33BrGL5V7fusgv7ujhdHdge79bmbngiogHGqbdViCdUqS+4l1XLQUjZVJNvs5Ax4SoJh86LHoAbwJ6H/7Pf9in+liK5ysEcznMkwJaXuBtqymCmBeTsEfyXTB6J1YcKXUPuHKPOTy3nl1Ze/9LbvgERqwKBgQCq1aS2mkpGQyZILVde+YgN74TjKWg7jrx8yHAC1snDYNzwmSJPtc5AUndkRMRoH3Brgp8xg8Mpmt8HhDmLvXcl0XLxpFHzD5JSvvMChoJOfmDkTbI6WFWWjHsSJMMPtMfAiTspeFWXw6O2w/luFUzc3FQfj11uD9X4E2x3Ti6l9QKBgCu/kM32a/inB4xlEW+HVzGSOT6JJRxKw2y9ls3g1mUxBL2WJuwSr3BRvZ+rqokdOLO6Mt3/d86IAmTbMBpUmgEaAWFYej8wEEfIH06Oog+jjL1XdIUQu/WbLxkvVi/oG4ZmYs+qw0FdMJ+QJvq/c1rPU/h+EIWVUiOyk0YrtJMXAoGAJOkkPY/4nFXF37JsPkkL7Hbj73Dy5GtirlNgYDyoO16iXhm4ICDX/23R/ggdp6GjWJdQbhJaCJM9vRIEnPzC0Zj/jNZEHpAuvVIkj1cgsHT92lYvPAv58h1IOhNXUf1ta1w7PCqT08ahiMuvCOnxQYcZZfhD55PkV0VbGiv6SEECgYAleL8nH9MDb1ayXfaQvWDsCjQpSi/tFl0NaJT3jXrLV7CzhFyea5xkO3Oz/U0QvxVFBzdZkAiKIbrg3Qwp4JQZIvTurL8C3mXkTqBxtxl5372FlS3PgVr6SWPm7kdZGxe+X/P7giiLT9ERBktfuv6Ouy7LgMjEgQqDKbkjufdunw==";

        //注：证书文件路径支持设置为文件系统中的路径或CLASS_PATH中的路径，优先从文件系统中加载，加载失败后会继续尝试从CLASS_PATH中加载
        config.merchantCertPath = "appCertPublicKey_2021001179653880.crt";
        config.alipayCertPath = "alipayCertPublicKey_RSA2.crt";
        config.alipayRootCertPath = "alipayRootCert.crt";

        //注：如果采用非证书模式，则无需赋值上面的三个证书路径，改为赋值如下的支付宝公钥字符串即可
        // config.alipayPublicKey = "<-- 请填写您的支付宝公钥，例如：MIIBIjANBg... -->";

        //可设置异步通知接收服务地址（可选）
        config.notifyUrl = "https://www.test.com/callback";

        //可设置AES密钥，调用AES加解密相关接口时需要（可选）
//        config.encryptKey = "<-- 请填写您的AES密钥，例如：aa4BtZ4tspm2wnXLb1ThQA== -->";

        return config;
    }

    public static void main(String[] args) {
        new AliPayEasyUtil();
        System.out.println( AliPayEasyUtil.create("subject","231424354365","0.01").toJSONString());
    }
}
