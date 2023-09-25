package com.sad.security;


import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

public class OwnObjectInputStream extends ObjectInputStream {
    String[] evil_black = new String[] {
            "java.util.HashMap", "com.sun.jndi.rmi.registry.RegistryContext", "sun.reflect.annotation.AnnotationInvocationHandler", "java.util.PriorityQueue", "java.util.Hashtable", "sun.rmi.server.UnicastRef", "java.rmi.server.UnicastRemoteObject", "java.util.Hashtable", "org.springframework.core.SerializableTypeWrapper", "javax.management.BadAttributeValueExpException",
            "org.springframework.beans.factory.ObjectFactory", "xalan.internal.xsltc.trax.TemplatesImpl", "com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl", "java.lang.Runtime", "com.sun.rowset.JdbcRowSetImpl", "javax.sql.rowset.BaseRowSet" };

    public OwnObjectInputStream(InputStream in) throws IOException {
        super(in);
    }

    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        System.out.println(desc.getName());
        return super.resolveClass(desc);
    }
}
