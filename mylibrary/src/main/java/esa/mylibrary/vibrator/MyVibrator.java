package esa.mylibrary.vibrator;

import android.app.Activity;
import android.app.Service;
import android.os.Vibrator;

/**
 * @ProjectName: mydemo
 * @Package: esa.mylibrary.vibrator
 * @ClassName: MyVibrator
 * @Description: java类作用描述
 * @Author: tvrddmss
 * @CreateDate: 2023/4/9 20:59
 * @UpdateUser: tvrddmss
 * @UpdateDate: 2023/4/9 20:59
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class MyVibrator {

    /**
     * 静态内部类单例
     * 优点：外部类加载的时候并不需要立即去加载内部类，内部类不被加载则不会实例化mInstance，不占内存资源，
     * 保证单例的唯一性，同时也延迟了单例的实例化。
     */
    private static class MyVibratorHolder {
        private static MyVibrator mInstance = new MyVibrator();
    }

    /**
     * @return
     * @description
     * @author Administrator
     * @time 2023/04/04 16:44
     */
    public static MyVibrator getInstance() {
        return MyVibrator.MyVibratorHolder.mInstance;
    }


    public static void vibrate(final Activity activity, long milliseconds) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);

        vib.vibrate(milliseconds);

    }

    //以pattern[]方式震动
    public static void vibrate(final Activity activity, long[] pattern, int repeat) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);

        vib.vibrate(pattern, repeat);

    }

    //取消震动
    public static void virateCancle(final Activity activity) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);

        vib.cancel();

    }
}
