package esa.mydemo

import android.app.Activity
import android.app.Application
import android.content.res.Configuration
import androidx.preference.PreferenceManager
import com.github.mikephil.charting.BuildConfig
import esa.mylibrary.config.Config
import esa.mylibrary.info.DeviceInfo
import java.util.LinkedList

class MyApplication : Application() {
    private lateinit var activities: MutableList<Activity>

    /**
     * 在创建应用程序时调用，可以重写这个方法来实例化应用程序单态，以及创建和实例化任何应用
     * 程序状态变量或共享资源
     */
    override fun onCreate() {
        super.onCreate()

        //初始化页面集合
        activities = LinkedList()

        // 获取配置值
        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        var server = preferences.getString("server", "")
        if (server!!.isEmpty()) { //默认下拉第一个
            val editor = preferences.edit()
            editor.putString("server", this.resources.getStringArray(R.array.arry_server)[0])
            editor.commit()
            server = preferences.getString("server", "")
        }

        //网络接口设置
        Config.api.http = server!!.split(":".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()[0]
        Config.api.ip = server.split(":".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()[1].replace("//".toRegex(), "")
        Config.api.port = server.split(":".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()[2].replace("/".toRegex(), "")
        Config.api.functiontoken["functiontoken"] =
            resources.getString(R.string.api_functiontoken)
        Config.api.project = resources.getString(R.string.api_project)
        //是否写日志文件到公共区域
        Config.isWriteLogFile = false
        //初始化--日志、轻量级存储工具、app奔溃信息收集、app信息、设备信息
        Config.init(
            this.packageName, this,
            BuildConfig.APPLICATION_ID,
            resources.getString(R.string.app_code),
            resources.getString(R.string.app_name),
            resources.getString(R.string.app_name_en)
        )

        //设置当前设备语言显示
        val sharedPreferences =
            android.preference.PreferenceManager.getDefaultSharedPreferences(this)
        val editor = sharedPreferences.edit()
        editor.putString("devlanguage", DeviceInfo.local.toString())
        editor.commit()
    }

    /**
     * 添加activity
     *
     * @param a
     */
    fun addActivity(a: Activity) {
        activities.add(a)
    }

    /**
     * 移除Activity
     */
    fun removeActivity(a: Activity) {
        activities.remove(a)
    }

    /**
     * 遍历所有Activity并finish
     */
    fun finishActivity() {
        for (activity in activities!!) {
            if (activity != null && !activity.isFinishing) {
                activity.finish()
            }
        }
    }

    /**
     * 所有Activity重新创建，用于语言更新
     */
    fun recreateAllActivity() {
        for (activity in activities!!) {
            if (activity != null && !activity.isFinishing) {
                activity.recreate()
            }
        }
    }

    /**
     * 获取Activity
     */
    fun getActivities(): List<Activity> {
        return activities
    }

    /**
     * 作为onLowMemory的一个特定于应用程序的替代选择，在android4.0时引入，
     * 在程序运行时决定当前应用程序应该尝试减少其内存开销时（通常在它进入后台时）调用
     * 它包含一个level参数，用于提供请求的上下文
     */
    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
    }

    /**
     * 与Activity不同，在配置改变时，应用程序对象不会被终止和重启。
     * 如果应用程序使用的值依赖于特定的配置，则重写这个方法来重新加载这些值，或者在应用程序级别处理这些值的改变
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    /**
     * 当系统处于资源匮乏时，具有良好行为的应用程序可以释放额外的内存。
     * 这个方法一般只会在后台进程已经终止，但是前台应用程序仍然缺少内存时调用。
     * 我们可以重写这个程序来清空缓存或者释放不必要的资源
     */
    override fun onLowMemory() {
        super.onLowMemory()
    }
}