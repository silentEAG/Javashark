package dev.silente.javashark.utils;

import com.nqzero.permit.Permit;
import sun.misc.Unsafe;
import sun.reflect.ReflectionFactory;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ReflectUtils {

    public static void setAccessible(AccessibleObject member) {
        String versionStr = System.getProperty("java.version");
        int javaVersion = Integer.parseInt(versionStr.split("\\.")[0]);
        if (javaVersion < 12) {
            // quiet runtime warnings from JDK9+
            Permit.setAccessible(member);
        } else {
            // not possible to quiet runtime warnings anymore...
            // see https://bugs.openjdk.java.net/browse/JDK-8210522
            // to understand impact on Permit (i.e. it does not work
            // anymore with Java >= 12)
            member.setAccessible(true);
        }
    }

    public static Object getStaticField(String className, String fieldName) {
        try {
            Class<?> clazz = Class.forName(className);
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(clazz);
        }
        catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static Field getField(final Class<?> clazz, final String fieldName) throws Exception {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            if ( field != null )
                field.setAccessible(true);
            else if ( clazz.getSuperclass() != null )
                field = getField(clazz.getSuperclass(), fieldName);
            return field;
        }
        catch ( NoSuchFieldException e ) {
            if ( !clazz.getSuperclass().equals(Object.class) ) {
                return getField(clazz.getSuperclass(), fieldName);
            }
            throw e;
        }
    }

    public static void setFieldValue(final Object obj, final String fieldName, final Object value) throws Exception {
        final Field field = getField(obj.getClass(), fieldName);
        field.set(obj, value);
    }

    public static Object getFieldValue(Object object, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = null;
        Class<?> clazz = object.getClass();
        try{
            field = clazz.getField(fieldName);
        }catch (NoSuchFieldException e1){
            while (clazz != Object.class) {
                try {
                    field = clazz.getDeclaredField(fieldName);
                    break;
                } catch (NoSuchFieldException e2) {
                    clazz = clazz.getSuperclass();
                }
            }
        }

        if (field == null) {
            throw new NoSuchFieldException(fieldName);
        } else {
            field.setAccessible(true);
            return field.get(object);
        }
    }

    public static Constructor<?> getFirstCtor(final String name) throws Exception {
        final Constructor<?> ctor = Class.forName(name).getDeclaredConstructors()[0];
        setAccessible(ctor);
        return ctor;
    }

    public static Constructor<?> getFirstCtor(Class<?> clz) throws Exception {
        final Constructor<?> ctor = clz.getDeclaredConstructors()[0];
        setAccessible(ctor);
        return ctor;
    }

    @SuppressWarnings ( {"unchecked"} )
    public static <T> T newInstance(String className, Class[] types, Object... args) {
        try {
            Constructor constructor = Class.forName(className).getDeclaredConstructor(types);
            constructor.setAccessible(true);
            return (T)constructor.newInstance(args);
        }
        catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings ( {"unchecked"} )
    public static <T> T newInstance(String className, Object... args) {
        try {
            Constructor constructor = Class.forName(className).getDeclaredConstructor(args2types(args));
            constructor.setAccessible(true);
            return (T)constructor.newInstance(args);
        }
        catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings ( {"unchecked"} )
    public static <T> T newInstance(Class clazz, Object... args) {
        try {
            Constructor constructor = clazz.getDeclaredConstructor(args2types(args));
            constructor.setAccessible(true);
            return (T)constructor.newInstance(args);
        }
        catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T createWithoutConstructor(Class<T> classToInstantiate)
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return createWithConstructor(classToInstantiate, Object.class, new Class[0], new Object[0]);
    }


    @SuppressWarnings ( {"unchecked"} )
    public static <T> T createWithConstructor(
            Class<T> classToInstantiate, Class<? super T> constructorClass, Class<?>[] consArgTypes, Object[] consArgs
    ) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Constructor<? super T> objCons = constructorClass.getDeclaredConstructor(consArgTypes);
        objCons.setAccessible(true);
        Constructor<?> sc = ReflectionFactory.getReflectionFactory().newConstructorForSerialization(classToInstantiate, objCons);
        sc.setAccessible(true);
        return (T) sc.newInstance(consArgs);
    }

    public static <T> T createProxy(final InvocationHandler ih, final Class<T> iface, final Class<?>... ifaces) {
        final Class<?>[] allIfaces = (Class<?>[]) Array.newInstance(Class.class, ifaces.length + 1);
        allIfaces[0] = iface;
        if (ifaces.length > 0) {
            System.arraycopy(ifaces, 0, allIfaces, 1, ifaces.length);
        }
        return iface.cast(Proxy.newProxyInstance(ReflectUtils.class.getClassLoader(), allIfaces, ih));
    }

    public static void TriggerAllGetter(Object obj) throws Exception{
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        List<PropertyDescriptor> descriptors = Arrays.stream(beanInfo.getPropertyDescriptors()).filter(p -> {
            String name = p.getName();
            return !"class".equals(name) && !"id".equals(name);
        }).collect(Collectors.toList());
        for (PropertyDescriptor descriptor : descriptors) {
            Method readMethod = descriptor.getReadMethod();
            try {
                Object o = readMethod.invoke(obj);
                System.out.println(o);
            } catch (Exception ignored) {}
        }
    }

    public static Object forceNewInstance(Class<?> clazz) throws Exception{
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        Unsafe unsafe = (Unsafe) theUnsafe.get(null);
        return unsafe.allocateInstance(clazz);
    }

    public static Class[] args2types(Object[] args) {
        Class[] types = new Class[args.length];
        for (int i = 0; i < types.length; i++) {
            if (args[i] != null) {
                types[i] = args[i].getClass();
            }
            else {
                // Class.class by default
                types[i] = Class.class;
            }
        }
        return types;
    }
}
