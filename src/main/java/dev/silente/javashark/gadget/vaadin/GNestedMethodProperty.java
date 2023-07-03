package dev.silente.javashark.gadget.vaadin;

import com.vaadin.data.util.NestedMethodProperty;
import com.vaadin.data.util.PropertysetItem;

public class GNestedMethodProperty {
    public static Object toString2Getter(Object o) {
        PropertysetItem propertysetItem = new PropertysetItem();
        NestedMethodProperty<Object> nestedMethodProperty = new NestedMethodProperty<Object>(o, "outputProperties");
        propertysetItem.addItemProperty("outputProperties", nestedMethodProperty);
        return propertysetItem;
    }
}
