package com.cybercloud.sprbotfreedom.platform.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpMethod;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Http请求发送工具类
 * @author liuyutang
 * @date 2023/10/8
 */
@Slf4j
public class HttpUtil {

    /**
     * 发送请求
     * @param method http方法
     * @param httpUrl 请求地址
     * @param params 参数
     * @return 响应内容
     */
    private static final Lock lock = new ReentrantLock(true);
    public static String send(HttpMethod method, String httpUrl, Map<String,Object> params,boolean form){
        if(MapUtils.isNotEmpty(params) && method == HttpMethod.GET){
            httpUrl = buildParameter(httpUrl, params);
        }
        // 构建 URL
        URL requestUrl;
        // 打开连接
        HttpURLConnection connection = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader in = null;
        OutputStream outputStream = null;
        lock.lock();
        try {
            requestUrl = new URL(httpUrl);
            connection = (HttpURLConnection) requestUrl.openConnection();
            // 设置请求方式
            connection.setRequestMethod(method.name());
            // 设置连接超时时间
            connection.setConnectTimeout(50000);
            // 设置读取超时时间
            connection.setReadTimeout(50000);
            connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            if(MapUtils.isNotEmpty(params) && method == HttpMethod.POST) {
                connection.setDoOutput(true);
                String paramUrlString;
                // 预留传递参数方式
                if (form){
                    paramUrlString = buildParameter(httpUrl, params);
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                }
                else {
                    paramUrlString = JSONObject.toJSONString(params);
                    connection.setRequestProperty("Content-Type", "application/json; utf-8");
                }
                // 设置请求参数
                byte[] postData = paramUrlString.getBytes(StandardCharsets.UTF_8);
                connection.setRequestProperty("Content-Length", String.valueOf(postData.length));
                outputStream = connection.getOutputStream();
                outputStream.write(postData);
            }
            // 获取响应码
            int responseCode = connection.getResponseCode();
            // 请求成功
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 读取响应内容
                inputStreamReader = new InputStreamReader(connection.getInputStream());
                in = new BufferedReader(inputStreamReader);
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                // 返回响应内容
                return response.toString();
            }
            // 处理请求失败的情况
            else {
                throw new IOException(String.format("HTTP请求失败，响应码：%s",responseCode));
            }
        }
        // 处理请求异常的情况
        catch (IOException ex){
            log.error("HTTP请求失败，{}",ex);
        }
        // 关闭连接
        finally {
            IOUtils.closeQuietly(inputStreamReader);
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(in);
            if(connection != null){
                connection.disconnect();
            }
            lock.unlock();
        }
        return null;
    }

    /**
     * 构建请求参数
     * @param httpUrl 请求地址
     * @param params 参数
     * @return 拼接好参数的请求地址
     */
    private static String buildParameter(String httpUrl, Map<String, Object> params) {
        StringBuffer paramUrl = new StringBuffer(httpUrl);
        paramUrl.append("?");
        params.forEach((k, v)->{
            paramUrl.append(k).append("=").append(v).append("&");
        });
        httpUrl = paramUrl.deleteCharAt(paramUrl.length() - 1).toString();
        return httpUrl;
    }

    /**
     * 发送GET请求
     * @param httpUrl 请求地址
     * @param params 参数
     * @return 响应内容
     */
    public static String sendGet(String httpUrl, Map<String,Object> params){

        return send(HttpMethod.GET,httpUrl,params,false);
    }

    /**
     * 发送POST请求
     * @param httpUrl 请求地址
     * @param params 参数
     * @return 响应内容
     */
    public static String sendPost(String httpUrl, Map<String,Object> params,boolean form){

        return send(HttpMethod.POST,httpUrl,params,form);
    }

    public static String postHttps(String url, String postData, String signKey, String signVal, String certPath) {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                        @Override
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }
                        @Override
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HttpsURLConnection.setDefaultHostnameVerifier((String hostname, SSLSession session) -> true);

            HttpURLConnection connection;
            if (url.startsWith("https")) {
                URL urlObj = new URL(url);
                connection = (HttpsURLConnection) urlObj.openConnection();
            } else {
                URL urlObj = new URL(url);
                connection = (HttpURLConnection) urlObj.openConnection();
            }

            if (!signKey.isEmpty() && !signVal.isEmpty()) {
                connection.setRequestProperty(signKey, signVal);
            }

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            connection.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                byte[] data = postData.getBytes("UTF-8");
                wr.write(data, 0, data.length);
            }

            StringBuilder response;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }

            return response.toString();
        } catch (Exception e) {
            // 处理异常
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String s = sendGet("http://192.168.10.245:10516/IDataService/GetHomePageInfo", null);
        System.out.println(s);
    }
}
