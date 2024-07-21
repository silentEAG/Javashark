package dev.silente.javashark.utils;



import com.sun.org.apache.bcel.internal.Repository;
import com.sun.org.apache.bcel.internal.classfile.JavaClass;
import com.sun.org.apache.bcel.internal.classfile.Utility;
import javassist.ClassPool;
import javassist.CtClass;

import java.lang.reflect.Method;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MiscUtils {

    public static String getLocalTime(){
        Date d = new Date();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(d);
    }

    public static String randomString(int len){
        Random rnd = ThreadLocalRandom.current();
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < len; i++) {
            boolean isChar = (rnd.nextInt(2) % 2 == 0);
            if (isChar) {
                int choice = rnd.nextInt(2) % 2 == 0 ? 65 : 97;
                ret.append((char) (choice + rnd.nextInt(26)));
            } else {
                ret.append(rnd.nextInt(10));
            }
        }
        return ret.toString();
    }

    public static String base64Encode(byte[] bytes){
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static byte[] base64Decode(String str){
        return Base64.getDecoder().decode(str);
    }

    public static byte[] dumpClass(Class<?> clazz) throws Exception{
        ClassPool cp = ClassPool.getDefault();
        CtClass cc = cp.get(clazz.getName());
        byte[] res = cc.toBytecode();
        cc.defrost();
        cc.detach();
        return res;
    }

    public static byte[] dumpCopyClass(Class<?> clazz) throws Exception{
        ClassPool cp = ClassPool.getDefault();
        CtClass cc = cp.getAndRename(clazz.getName(), randomString(6)+"."+clazz.getSimpleName());
        byte[] res = cc.toBytecode();
        cc.defrost();
        cc.detach();
        return res;
    }

    public static String dumpBCELClass(Class<?> clazz) throws Exception {
        JavaClass javaClass = Repository.lookupClass(clazz);
        String code = Utility.encode(javaClass.getBytes(),true);
        return "$$BCEL$$"+code;
    }

    public static String dumpBCELClassReflect(Class<?> clazz) throws Exception {
        Class<?> repositoryClass = Class.forName("com.sun.org.apache.bcel.internal.Repository");
        Method lookupClassMethod = repositoryClass.getMethod("lookupClass", Class.class);
        Object javaClass = lookupClassMethod.invoke(null, clazz);
        Class<?> javaClassClass = Class.forName("com.sun.org.apache.bcel.internal.classfile.JavaClass");
        Method getBytesMethod = javaClassClass.getMethod("getBytes");
        byte[] bytes = (byte[]) getBytesMethod.invoke(javaClass);
        Class<?> utilityClass = Class.forName("com.sun.org.apache.bcel.internal.classfile.Utility");
        Method encodeMethod = utilityClass.getMethod("encode", byte[].class, boolean.class);
        String code = (String) encodeMethod.invoke(null, bytes, true);
        return "$$BCEL$$" + code;
    }

    public static String bytes2HexString(byte[] bytes) {
        StringBuffer ret = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret.append(hex.toUpperCase());
        }
        return ret.toString();
    }

    public static String executeCommand(String command) {
        StringBuilder output = new StringBuilder();

        Process process;
        try {
            process = Runtime.getRuntime().exec(command);
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return output.toString();
    }

    public static String classAsFile(final Class<?> clazz) {
        return classAsFile(clazz, true);
    }

    public static String classAsFile(final Class<?> clazz, boolean suffix) {
        String str;
        if (clazz.getEnclosingClass() == null) {
            str = clazz.getName().replace(".", "/");
        } else {
            str = classAsFile(clazz.getEnclosingClass(), false) + "$" + clazz.getSimpleName();
        }
        if (suffix) {
            str += ".class";
        }
        return str;
    }

    public static byte[] classAsBytes(final Class<?> clazz) {
        try {
            final byte[] buffer = new byte[1024];
            final String file = classAsFile(clazz);
            final InputStream in = MiscUtils.class.getClassLoader().getResourceAsStream(file);
            if (in == null) {
                throw new IOException("couldn't find '" + file + "'");
            }
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendToServer(String link, byte[] code) throws Exception {
        URL url = new URL(link);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setUseCaches(false);
        con.setRequestMethod("POST");
        OutputStream os = con.getOutputStream();

        String line;
        os.write(code);
        os.flush();

        InputStream inputStream = con.getInputStream();
        StringBuilder resultBuffer = new StringBuilder();
        BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        while ((line = buffer.readLine()) != null) {
            resultBuffer.append(line);
        }
        System.out.println("Response:" + resultBuffer);
    }

}
