package com.chason.bossdemo.common.util;


import org.apache.log4j.Logger;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;

/**
 * @Author: kevin
 * @Description:
 * @Date:Creat in: 2018/4/10 15:03
 * @Modifies By:
 */
@SuppressWarnings("Duplicates")
public class HttpClient {

    private static Logger log = Logger.getLogger("MyLogger");

    private final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    private static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }
        }};
        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * @param url
     * @param param 参数
     */
    public static String httpsRequest(String url, String param) {


        String result = "";
        PrintWriter out = null;
        BufferedReader in = null;
        HttpURLConnection conn;
        Long startTime=0L;
        try {
            startTime=System.currentTimeMillis();
            trustAllHosts();
            URL realUrl = new URL(url);
            //通过请求地址判断请求类型(http或者是https)
            if (realUrl.getProtocol().toLowerCase().equals("https")) {

                HttpsURLConnection https = (HttpsURLConnection) realUrl.openConnection();
                https.setHostnameVerifier(DO_NOT_VERIFY);
                conn = https;
            } else {
                conn = (HttpURLConnection) realUrl.openConnection();
            }
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();

            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {// 使用finally块来关闭输出流、输入流
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();

            }
        }
        return result;
    }
}
