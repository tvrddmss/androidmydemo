package esa.mylibrary.info;

import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import esa.mylibrary.utils.MyJson;
import esa.mylibrary.utils.MySharedPreferences;
import esa.mylibrary.utils.log.MyLog;

/**
 * @ProjectName: mydemo
 * @Package: esa.mylibrary.info
 * @ClassName: CodeInfo
 * @Description: java类作用描述
 * @Author: Administrator
 * @CreateDate: 2023/03/31 11:28
 * @UpdateUser: Administrator
 * @UpdateDate: 2023/03/31 11:28
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class CodeInfo {
    public static String codeVersion;
    public static Map<String, JSONArray> codeMap;

    /**
     * @param jsonObject
     * @return void
     * @description 设置字典
     * @author Administrator
     * @time 2023/03/31 13:18
     */
    public static void setCode(JSONObject jsonObject) throws Exception {
        //保存字典
        MyLog.d(jsonObject.toString());
        JSONArray rows = jsonObject.getJSONArray("rows");
        if (rows.length() > 0) {
            codeMap = new HashMap<>();
            for (int i = 0; i < rows.length(); i++) {
                JSONObject o = rows.getJSONObject(i);
                if (!codeMap.containsKey(o.getString("nodeid"))) {
                    codeMap.put(o.getString("nodeid"), new JSONArray());
                }
                JSONArray codeJSONArray = codeMap.get(o.getString("nodeid"));
                codeJSONArray.put(o);
            }
            MyLog.d(codeMap.toString());
            //保存版本号
            codeVersion = jsonObject.getString("codeversion");

            //保存到临时存储
            save();
        }
    }

    /**
     * @return void
     * @description 初始化
     * @author Administrator
     * @time 2023/03/31 15:28
     */
    public static void init() {
        codeVersion = MySharedPreferences.base.getString("codeversion", "1");
        String mapString = MySharedPreferences.base.getString("codemap", "");
        if (!mapString.isEmpty()) {
            codeMap = new HashMap<>();
            codeMap = (Map<String, JSONArray>) MyJson.fromJson(mapString, codeMap.getClass());
        } else {
            codeMap = new HashMap<>();
        }
        MyLog.d("初始化code:" + codeVersion);
    }

    /**
     * @return void
     * @description 保存
     * @author Administrator
     * @time 2023/03/31 15:27
     */
    public static void save() {
        SharedPreferences.Editor editor = MySharedPreferences.base.edit();
        editor.putString("codeversion", codeVersion);
        editor.putString("codemap", codeMap.toString());
        editor.commit();
        MyLog.d("保存code:" + codeVersion);
    }
}
