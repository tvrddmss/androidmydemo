package esa.mylibrary.info;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Window;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

import esa.mylibrary.utils.DeviceUtil;
import esa.mylibrary.utils.log.MyLog;

/**
 * @ProjectName: mydemo
 * @Package: esa.mylibrary.security
 * @ClassName: DeviceUtil
 * @Description: java类作用描述
 * @Author: Administrator
 * @CreateDate: 2023/03/29 14:05
 * @UpdateUser: Administrator
 * @UpdateDate: 2023/03/29 14:05
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class DeviceInfo {

    public static String ip;
    public static String mac;
    public static String platform = "android";
    public static String deviceTokenString = "";

    public static String model = "";

    public static int statubarHeight = 0;

    public static String local = "";

    //屏幕
    public static int screenHeightPixels = 0;
    public static int screenWidthPixels = 0;

    //应用区域
    public static int applicationHeightPixels = 0;
    public static int applicationWidthPixels = 0;

    //View绘制区域
    public static int viewDrawHeightPixels = 0;
    public static int viewDrawWidthPixels = 0;

    /**
     * @param
     * @return null
     * @description 初始化
     * @author Administrator
     * @time 2023/03/29 14:07
     */
    public static void init() throws Exception {
        ip = DeviceUtil.GetLocalIp();
        mac = DeviceUtil.getLocalMacAddressFromIp();
        model = Build.MODEL;
        local = Locale.getDefault().getLanguage();


//        硬件制造商（MANUFACTURER）
//        品牌名称（BRAND）
//        主板名称（BOARD）
//        设备名 （DEVICE）
//        型号（MODEL）:即用户可见的名称
//        显示屏参数（DISPLAY）
//        产品名称（PRODUCT）：即手机厂商
//        设备唯一识别码（FINGERPRINT）
//        CPU指令集（CPU_ABI）
//        CPU指令集2（CPU_ABI2）
//        修订版本列表（ID）
//        硬件序列号（SERIAL）
//        描述build的标签（TAGS）
//        MyLog.d(Build.MANUFACTURER);
//        MyLog.d(Build.BRAND);
//        MyLog.d(Build.BOARD);
//        MyLog.d(Build.DEVICE);
//        MyLog.d(Build.MODEL);
//        MyLog.d(Build.DISPLAY);
//        MyLog.d(Build.PRODUCT);
//        MyLog.d(Build.FINGERPRINT);
//        MyLog.d(Build.CPU_ABI);
//        MyLog.d(Build.CPU_ABI2);
//        MyLog.d(Build.ID);
//        MyLog.d(Build.SERIAL);
//        MyLog.d(Build.TAGS);

    }

    public static void getScreen(Activity activity) {
        if (statubarHeight == 0) {

            Rect frame = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            DeviceInfo.statubarHeight = frame.top;
//            MyLog.d("top" + frame.top);

            //屏幕
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            screenHeightPixels = dm.heightPixels;
            screenWidthPixels = dm.widthPixels;
//            MyLog.d("dm" + dm.heightPixels);
            //应用区域
            Rect outRect1 = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect1);
            applicationHeightPixels = outRect1.height();
            applicationWidthPixels = outRect1.width();
//            MyLog.d("top" + outRect1.top);
//            MyLog.d("height" + outRect1.height());

            //View绘制区域
            Rect outRect2 = new Rect();
            activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getDrawingRect(outRect2);
            int viewTop = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop(); //要用这种方法
            viewDrawHeightPixels = outRect2.height();
            viewDrawWidthPixels = outRect2.width();
//            MyLog.d("viewTop" + viewTop);
//            MyLog.d("outRect2" + outRect2.height());
        }
        MyLog.d("statubarHeight:" + statubarHeight);

        MyLog.d("screenHeightPixels:" + screenHeightPixels);
        MyLog.d("screenWidthPixels:" + screenWidthPixels);

        MyLog.d("applicationHeightPixels:" + applicationHeightPixels);
        MyLog.d("applicationWidthPixels:" + applicationWidthPixels);

        MyLog.d("viewDrawHeightPixels:" + viewDrawHeightPixels);
        MyLog.d("viewDrawWidthPixels:" + viewDrawWidthPixels);
    }

    // 获取应用占用的内存(单位为KB)
    public static String getAppMemory() {
        String info = null;
        try {
            int pid = android.os.Process.myPid();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream("/proc/" + pid + "/status")), 1000);
            String load;
            while ((load = reader.readLine()) != null) {
                load = load.replace(" ", "");
                String[] Info = load.split("[: k K]");
                if (Info[0].equals("VmRSS")) {
                    info = Info[1];
                    break;
                }

            }
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return info;
    }
}
