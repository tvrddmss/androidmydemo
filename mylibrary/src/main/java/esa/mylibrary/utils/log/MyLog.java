package esa.mylibrary.utils.log;

import android.os.Environment;
import android.util.Log;

import esa.mylibrary.config.Config;

/***
 * 作者：chj233
 * 时间：2022/7/7
 * 描述：
 */
public class MyLog {


    //是否打印日志
    public static boolean isPrintLog = true;
    //是否写日志文件
    public static boolean isWriteLog = false;
    //tag
    private static String tag = "";

    //使用接口 避免耦合 后续若要更换日志打印框架 直接new 其它实现类即可
    private static LogInterface log = new LogHandle();

    /**
     * 日志初始化
     */
    public static void init(String tag) {
        isWriteLog = Config.isWriteLogFile;
        MyLog.tag = tag;
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Logs";
        log.init(path, MyLog.tag);
        d("日志初始化成功!");
    }

    /**
     * debug日志
     *
     * @param str
     */
    public static void d(String str) {
        if (isPrintLog) {
            log.d(str);
            Log.d(tag, str);
        }

    }

    /**
     * debug日志
     *
     * @param str
     */
    public static void d(String tag, String str) {
        if (isPrintLog) {
            log.d(str);
            Log.d(tag, str);
        }

    }


    /**
     * info
     *
     * @param str
     */
    public static void i(String str) {
        if (isPrintLog) {
            log.i(str);
            Log.i(tag, str);
        }
    }

    /**
     * info
     *
     * @param str
     */
    public static void i(String tag, String str) {
        if (isPrintLog) {
            log.i(str);
            Log.i(tag, str);
        }
    }

    /**
     * warning
     *
     * @param str
     */
    public static void w(String str) {
        if (isPrintLog) {
            log.w(str);
            Log.w(tag, str);
        }
    }

    /**
     * warning
     *
     * @param str
     */
    public static void w(String tag, String str) {
        if (isPrintLog) {
            log.w(str);
            Log.w(tag, str);
        }
    }

    /**
     * 错误日志
     *
     * @param str
     */
    public static void e(String str) {
        if (isPrintLog) {
            log.e(str);
            Log.e(tag, str);
        }
    }

    /**
     * 错误日志
     *
     * @param str
     */
    public static void e(String tag, String str) {
        if (isPrintLog) {
            log.e(str);
            Log.e(tag, str);
        }
    }

}