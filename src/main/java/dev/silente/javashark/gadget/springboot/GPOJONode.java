package dev.silente.javashark.gadget.springboot;

import com.fasterxml.jackson.databind.node.POJONode;

public class GPOJONode {
    public static POJONode toString2Getter(Object o) {
        return new POJONode(o);
    }
}
