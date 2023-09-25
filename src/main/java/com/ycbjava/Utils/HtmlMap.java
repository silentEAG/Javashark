package com.ycbjava.Utils;

import com.ycbjava.Utils.HtmlUploadUtil;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class HtmlMap implements Map, Serializable {
    public String filename;

    public String content;

    public int size() {
        return 0;
    }

    public boolean isEmpty() {
        return false;
    }

    public boolean containsKey(Object key) {
        return false;
    }

    public boolean containsValue(Object value) {
        return false;
    }

    public Object get(Object key) {
        Object obj;
        try {
            obj = Boolean.valueOf(HtmlUploadUtil.uploadfile(this.filename, this.content));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return obj;
    }

    public Object put(Object key, Object value) {
        return null;
    }

    public Object remove(Object key) {
        return null;
    }

    public void putAll(Map m) {}

    public void clear() {}

    public Set keySet() {
        return null;
    }

    public Collection values() {
        return null;
    }

    public Set<Map.Entry> entrySet() {
        return null;
    }
}
