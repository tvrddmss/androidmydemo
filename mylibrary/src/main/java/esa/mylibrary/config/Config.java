package esa.mylibrary.config;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.core.content.pm.PackageInfoCompat;

import java.util.HashMap;
import java.util.Map;

import esa.mylibrary.gps.MyGps;
import esa.mylibrary.icon.MyIcon;
import esa.mylibrary.info.AppInfo;
import esa.mylibrary.info.CodeInfo;
import esa.mylibrary.info.DeviceInfo;
import esa.mylibrary.map.Osmdroid;
import esa.mylibrary.sensor.MySensor;
import esa.mylibrary.utils.log.MyLog;

public class Config {

    public static class api {

        /**
         * api协议
         */
        public static String http = "";
        /**
         * ip
         */
        public static String ip = "";
        /**
         * port
         */
        public static String port = "";
        /**
         * 项目api地址段:esa-zjgcdy-server
         */
        public static String project = "";
        /**
         * api-functiontoken
         */
        public static Map<String, String> functiontoken = new HashMap<>();


        public static String loginToken = "";
    }

    //region 版本号


    //endregion

    /**
     * 自定义application,一般用于全局变量存放
     */
    public static Application myApplication;

    /**
     * 是否写日志文件到公共存储
     */
    public static boolean isWriteLogFile = false;

    public static void init(String logTag, Application myApplication, String pageName, String appCode, String appName, String appNameEn) {
        try {

            //获取当前application
            myApplication = myApplication;
            //如果没有初始化functiontoken则用默认
            if (api.functiontoken.isEmpty()) {
                api.functiontoken.put("functiontoken", "12345678");
            }
            //注意 使用之前 需要用户允许操作SD卡权限
            MyLog.init(logTag);//日志初始化 推荐在Application的onCreate中初始化

            //轻量级存储工具
            esa.mylibrary.utils.MySharedPreferences.init(myApplication);

            //app奔溃信息收集--在轻量级存储工具之后，因为会用到
            esa.mylibrary.crash.CrashHandler.getInstance().init(myApplication);

            //设备信息IP，mac
            DeviceInfo.init();

            //当前APPcode/name/版本号
            PackageManager manager = myApplication.getPackageManager();
            PackageInfo info = manager.getPackageInfo(myApplication.getPackageName(), 0);
            AppInfo.init(pageName, appCode, appName, appNameEn, info.versionName, PackageInfoCompat.getLongVersionCode(info));

            //code/版本号、字典
            CodeInfo.init();

            //osmdroid
            Osmdroid.init(myApplication);

            //GPS
            MyGps.getInstance().init(myApplication);

            //Sensor
            MySensor.getInstance().init(myApplication);

            //osm自带指南针监听-同样采用 方向角、俯仰角、翻滚角
            //esa.mylibrary.sensor.InternalCompassOrientationProvider.getInstance(myApplication).startOrientationProvider(null);

            //icon
            MyIcon.INSTANCE.init();


        } catch (Exception ex) {
            showErrorMessage(ex.getMessage());
        }

    }


    /**
     * @param
     * @return null
     * @description 页面展示错误信息
     * @author Administrator
     * @time 2023/03/29 14:25
     */
    public static void showErrorMessage(String message) {
        MyLog.e(message);
//        final AlertDialog a = new AlertDialog.Builder(myApplication)
//                .setTitle("提示")
//                .setMessage(message)
//                .setPositiveButton("确定", null)
//                .show();
    }


}
