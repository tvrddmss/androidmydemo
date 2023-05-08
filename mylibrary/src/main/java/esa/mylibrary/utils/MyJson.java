package esa.mylibrary.utils;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

public class MyJson {

    public static Object parse(String str) throws JSONException {
        JSONTokener jsonTokener = new JSONTokener(str);
        return jsonTokener.nextValue();

    }

    static Gson gson = new Gson();

    public static Object fromJson(String str, Class type) {
        Object o = gson.fromJson(str, type);
        return o;
    }


    /**
     * @param jsonArray
     * @param jsonArray_part
     * @return java.lang.Object
     * @description 合并
     * @author Administrator
     * @time 2023/04/21 14:44
     */

    public static JSONArray merge(JSONArray jsonArray, JSONArray jsonArray_part) throws Exception {
        for (int i = 0; i < jsonArray_part.length(); i++) {
            jsonArray.put(jsonArray_part.getJSONObject(i));
        }
        return jsonArray;
    }
}
