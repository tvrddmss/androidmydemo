package esa.mydemo;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import androidx.preference.PreferenceManager;

import com.github.mikephil.charting.BuildConfig;

import java.util.LinkedList;
import java.util.List;

import esa.mylibrary.config.Config;
import esa.mylibrary.info.DeviceInfo;

public class MyApplication extends Application {

    private List<Activity> activities;

    /**
     * 在创建应用程序时调用，可以重写这个方法来实例化应用程序单态，以及创建和实例化任何应用
     * 程序状态变量或共享资源
     */
    @Override
    public void onCreate() {
        super.onCreate();

        //初始化页面集合
        activities = new LinkedList<Activity>();

        // 获取配置值
        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String server = preferences.getString("server", "");
        if (server.isEmpty()) {//默认下拉第一个
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("server", this.getResources().getStringArray(R.array.arry_server)[0]);
            editor.commit();
            server = preferences.getString("server", "");
        }

        //网络接口设置
        Config.api.http = server.split(":")[0];
        Config.api.ip = server.split(":")[1].replaceAll("//", "");
        Config.api.port = server.split(":")[2].replaceAll("/", "");
        Config.api.functiontoken.put("functiontoken", getResources().getString(R.string.api_functiontoken));
        Config.api.project = getResources().getString(R.string.api_project);
        //是否写日志文件到公共区域
        Config.isWriteLogFile = false;
        //初始化--日志、轻量级存储工具、app奔溃信息收集、app信息、设备信息
        Config.init(this.getPackageName(), this,
                BuildConfig.APPLICATION_ID,
                getResources().getString(R.string.app_code),
                getResources().getString(R.string.app_name),
                getResources().getString(R.string.app_name_en));

        //设置当前设备语言显示
        SharedPreferences sharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("devlanguage", DeviceInfo.local.toString());
        editor.commit();

    }

    /**
     * 添加activity
     *
     * @param a
     */
    public void addActivity(Activity a) {
        activities.add(a);
    }

    /**
     * 移除Activity
     */
    public void removeActivity(Activity a) {
        activities.remove(a);
    }

    /**
     * 遍历所有Activity并finish
     */
    public void finishActivity() {
        for (Activity activity : activities) {
            if (activity != null && !activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    /**
     * 所有Activity重新创建，用于语言更新
     */
    public void recreateAllActivity() {

        for (Activity activity : activities) {
            if (activity != null && !activity.isFinishing()) {
                activity.recreate();
            }
        }
    }

    /**
     * 获取Activity
     */
    public List<Activity> getActivities() {
        return activities;
    }


    /**
     * 作为onLowMemory的一个特定于应用程序的替代选择，在android4.0时引入，
     * 在程序运行时决定当前应用程序应该尝试减少其内存开销时（通常在它进入后台时）调用
     * 它包含一个level参数，用于提供请求的上下文
     */
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    /**
     * 与Activity不同，在配置改变时，应用程序对象不会被终止和重启。
     * 如果应用程序使用的值依赖于特定的配置，则重写这个方法来重新加载这些值，或者在应用程序级别处理这些值的改变
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 当系统处于资源匮乏时，具有良好行为的应用程序可以释放额外的内存。
     * 这个方法一般只会在后台进程已经终止，但是前台应用程序仍然缺少内存时调用。
     * 我们可以重写这个程序来清空缓存或者释放不必要的资源
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }


}