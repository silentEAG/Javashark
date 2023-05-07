package dev.silente.javashark.utils;


import com.caucho.hessian.io.*;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.nibblesec.tools.SerialKiller;
import org.objenesis.strategy.StdInstantiatorStrategy;
import java.io.*;

public class SerializeUtils {
    public static class NoWriteReplaceSerializerFactory extends SerializerFactory {

        /**
         * {@inheritDoc}
         *
         * @see com.caucho.hessian.io.SerializerFactory#getObjectSerializer(java.lang.Class)
         */
        @Override
        public Serializer getObjectSerializer ( Class<?> cl ) throws HessianProtocolException {
            return super.getObjectSerializer(cl);
        }


        /**
         * {@inheritDoc}
         *
         * @see com.caucho.hessian.io.SerializerFactory#getSerializer(java.lang.Class)
         */
        @Override
        public Serializer getSerializer ( Class cl ) throws HessianProtocolException {
            Serializer serializer = super.getSerializer(cl);

            if ( serializer instanceof WriteReplaceSerializer ) {
                return UnsafeSerializer.create(cl);
            }
            return serializer;
        }

    }

    public static class HessianLiteNoWriteReplaceSerializerFactory extends com.alibaba.com.caucho.hessian.io.SerializerFactory {

    }

    public static byte[] serialize(Object o) throws Exception{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(o);
        oos.close();
        bos.close();
        return bos.toByteArray();
    }

    public static void serialize(Object o,String outPath) throws Exception{
        FileOutputStream fos = new FileOutputStream(outPath);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(o);
        oos.close();
        fos.close();
        return;
    }

    public static Object deserialize(byte[] bytes) throws Exception{
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bis);
        bis.close();
        ois.close();
        return ois.readObject();
    }

    public static Object deserialize(String binFile,String killerLocation) throws Exception {
        ObjectInputStream ois = new SerialKiller(new FileInputStream(binFile), killerLocation);
        return ois.readObject();
    }

    public static Object deserialize(byte[] bytes,String killerLocation) throws Exception {
        ObjectInputStream ois = new SerialKiller(new ByteArrayInputStream(bytes), killerLocation);
        return ois.readObject();
    }

    public static byte[] hessian2Serialize(Object o) throws Exception{
//        ByteArrayOutputStream os = new ByteArrayOutputStream();
//        AbstractHessianOutput out = new Hessian2Output(os);
//        out.writeObject(o);
//        return os.toByteArray();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        AbstractHessianOutput out = new Hessian2Output(bos);
        out.writeObject(o);
        out.close();
        return bos.toByteArray();
    }

    public static Object hessian2Deserialize(byte[] bytes) throws Exception{
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        AbstractHessianInput ahi = new Hessian2Input(bis);

        return ahi.readObject();
    }

    public static byte[] kryoSerialize(Object o) throws Exception{
        Kryo kryo = new Kryo();
        kryo.setReferences(true);
        kryo.setRegistrationRequired(false);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Output output = new Output(bos);
        kryo.writeClassAndObject(output,o);
        output.flush();
        output.close();
        return bos.toByteArray();
    }

    public static Object kryoDeserialize(byte[] bytes) throws Exception{
        Kryo kryo=new Kryo();
        kryo.setReferences(true);
        kryo.setRegistrationRequired(false);
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        Input input = new Input(bis);
        return kryo.readClassAndObject(input);
    }

    public static Object kryoAltDeserialize(byte[] bytes) throws Exception{
        Kryo kryo=new Kryo();
        kryo.setReferences(true);
        kryo.setRegistrationRequired(false);
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        Input input = new Input(bis);
        return kryo.readClassAndObject(input);
    }

    public static Object serializeKillerDeserialize(byte[] bytes,String configPath) throws Exception{
        ObjectInputStream ois = new SerialKiller(new ByteArrayInputStream(bytes), configPath);
        return ois.readObject();
    }

    public static Object hessianLiteDeserialize(byte[] bytes) throws Exception{
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        com.alibaba.com.caucho.hessian.io.AbstractHessianInput ahi = new com.alibaba.com.caucho.hessian.io.Hessian2Input(bis);
        return ahi.readObject();
    }

    public static byte[] hessianLiteSerialize(Object o) throws Exception{
//        ByteArrayOutputStream os = new ByteArrayOutputStream();
//        AbstractHessianOutput out = new Hessian2Output(os);
//        out.writeObject(o);
//        return os.toByteArray();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        com.alibaba.com.caucho.hessian.io.AbstractHessianOutput out = new com.alibaba.com.caucho.hessian.io.Hessian2Output(bos);
        HessianLiteNoWriteReplaceSerializerFactory sf = new HessianLiteNoWriteReplaceSerializerFactory();
        sf.setAllowNonSerializable(true);
        out.setSerializerFactory(sf);
        out.writeObject(o);
        out.close();
        return bos.toByteArray();
    }


    public static byte[] hessian2SerializeThrowExpectToString(Object object) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Hessian2Output out = new Hessian2Output(byteArrayOutputStream);
        NoWriteReplaceSerializerFactory factory = new NoWriteReplaceSerializerFactory();
        factory.setAllowNonSerializable(true);
        out.setSerializerFactory(factory);
        out.writeString("aaa");
        out.writeObject(object);
        out.flushBuffer();
        return byteArrayOutputStream.toByteArray();
    }

}
