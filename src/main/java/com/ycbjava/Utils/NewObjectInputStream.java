package com.ycbjava.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.HashSet;
import java.util.Set;

public class NewObjectInputStream extends ObjectInputStream {
    private static final Set<String> BLACKLISTED_CLASSES = new HashSet<>();

    static {
        BLACKLISTED_CLASSES.add("java.lang.Runtime");
        BLACKLISTED_CLASSES.add("java.lang.ProcessBuilder");
        BLACKLISTED_CLASSES.add("com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl");
        BLACKLISTED_CLASSES.add("java.security.SignedObject");
        BLACKLISTED_CLASSES.add("com.sun.jndi.ldap.LdapAttribute");
    }

    public NewObjectInputStream(InputStream inputStream) throws IOException {
        super(inputStream);
    }

    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        if (BLACKLISTED_CLASSES.contains(desc.getName()))
            throw new SecurityException("Class not allowed: " + desc.getName());
        return super.resolveClass(desc);
    }
}
