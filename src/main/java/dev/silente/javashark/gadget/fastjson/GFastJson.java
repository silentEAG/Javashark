package dev.silente.javashark.gadget.fastjson;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class GFastJson {
    public static JSONObject toString2GetterJSONObject(Object obj){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("foo", obj);
        return jsonObject;
    }
    public static JSONArray toString2GetterJSONArray(Object obj){
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(obj);
        return jsonArray;
    }
}
