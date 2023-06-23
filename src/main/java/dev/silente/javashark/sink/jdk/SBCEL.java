package dev.silente.javashark.sink.jdk;

import com.sun.org.apache.bcel.internal.Repository;
import com.sun.org.apache.bcel.internal.classfile.JavaClass;
import com.sun.org.apache.bcel.internal.classfile.Utility;
import dev.silente.javashark.utils.TestUtils;

import java.io.IOException;

public class SBCEL {

    public static String getBCELCode(Class<?> clz) throws IOException {
        JavaClass evil = Repository.lookupClass(clz);
        return "$$BCEL$$" + Utility.encode(evil.getBytes(), true);
    }
}
