package esa.mylibrary.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author: Administrator
 * @date: 2023/03/28
 */
public class MySharedPreferences {

    public static SharedPreferences base;
    public static SharedPreferences user;

    /**
     * @param
     * @return
     * @description 初始化方法
     * @author Administrator
     * @time 2023/03/29 9:29
     */
    public static void init(Context context) {
        base = context.getSharedPreferences("base", MODE_PRIVATE);
        user = context.getSharedPreferences("user", MODE_PRIVATE);
    }

    /**
     * @param
     * @return
     * @description 是否第一次打开APP, 只是第一次询问为true,后面都为false
     * @author Administrator
     * @time 2023/03/29 9:28
     */
    public static Boolean getIsFirstStart() {
        boolean isFirstStart = base.getBoolean("isFirstStart", true);
        if (isFirstStart) {
            base.edit().putBoolean("isFirstStart", false).commit();
        }
        return isFirstStart;
    }

    /**
     * @param
     * @return
     * @description 获取崩溃信息并清空
     * @author Administrator
     * @time 2023/03/29 9:40
     */
    public static String getCrashMessage() {
        String CrashMessage = base.getString("CrashMessage", "");
        base.edit().putString("CrashMessage", "").commit();
        return CrashMessage;
    }

    /**
     * @param
     * @return
     * @description 保存崩溃信息
     * @author Administrator
     * @time 2023/03/29 9:40
     */
    public static void setCrashMessage(String crashMessage) {
        base.edit().putString("CrashMessage", crashMessage).commit();
    }





}
