package esa.mydemo.base

import android.os.Bundle
import esa.mydemo.MyApplication
import esa.mydemo.dal.Platform
import esa.mydemo.dal.spring.PlatformForSpring.checkVersion
import esa.mylibrary.activity.BaseActivity
import esa.mylibrary.common.CallBack
import esa.mylibrary.utils.log.MyLog
import java.io.File

/**
 * @ProjectName: mydemo
 * @Package: esa.mydemo.base
 * @ClassName: AppBaseActivity
 * @Description: java类作用描述
 * @Author: Administrator
 * @CreateDate: 2023/03/29 12:41
 * @UpdateUser: Administrator
 * @UpdateDate: 2023/03/29 12:41
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
open class AppBaseActivity : BaseActivity() {
    /**
     * @description 是否已检查版本信息
     * @author Administrator
     * @time 2023/04/03 10:27
     */
    protected var isFirstActiviyInitComplete = false

    /**
     * @param
     * @return null
     * @description 初始化设置，页面不可见
     * @author Administrator
     * @time 2023/03/29 15:33
     */
    override fun onCreate(arg0: Bundle?) {
        // TODO Auto-generated method stub
        super.onCreate(arg0)

//        //设置状态栏背景色
//        setStatusBackColor(getResources().getColor(R.color.main));
//        //设置状态栏字体色为深色
//        setStatusDarkColor(true);
//        //以上都有样式设置

        //添加本页面到集合
        (application as MyApplication).addActivity(this)
        MyLog.d(this.javaClass.name + "移入Activity集合")
    }

    /**
     * @param callBack
     * @return void
     * @description 第一个页面创建时，初始化检查各种版本,用于esa
     * @author tvrddmss
     * @time 2023/4/11 17:03
     */
    protected fun setIsFirstActiviyInit(callBack: CallBack<*>) {
        showloading("检查APP版本。。。")

        //检测APP版本
        Platform.checkVersion(this, object : CallBack<Any?>() {
            override fun success(o: Any?) {
                //判断是否有需要安装新版本
                if (o == null) {
                    MyLog.d("检测APP版本完成，不需要更新！")
                    //检测Code版本
                    showloading("检查数据字典版本。。。")
                    Platform.checkCodeVersion(this@AppBaseActivity, object : CallBack<Any?>() {
                        override fun success(o: Any?) {
                            MyLog.d("code更新完成")
                            isFirstActiviyInitComplete = true
                            callBack.success("" as Nothing?)
                        }

                        override fun error(message: String) {
                            callBack.error("检查Code版本失败：$message")
                        }
                    })
                } else {
                    MyLog.d("检测APP版本完成，更新中！")
                    Platform.installApp(this@AppBaseActivity, o as File)
                }
            }

            override fun error(message: String) {
                callBack.error("检查APP版本失败：$message")
            }
        })
    }

    /**
     * @param callBack
     * @return void
     * @description 第一个页面创建时，初始化检查各种版本,用于spring
     * @author tvrddmss
     * @time 2023/4/11 17:03
     */
    protected fun setIsFirstActiviyInitForSpring(callBack: CallBack<String>) {
        showloading("检查APP版本。。。")

        //检测APP版本
        checkVersion(this, object : CallBack<Any?>() {
            override fun success(o: Any?) {
                //判断是否有需要安装新版本
                if (o == null) {
                    MyLog.d("检测版本完成，不需要更新！")
                    //检测Code版本
                    isFirstActiviyInitComplete = true
                    callBack.success("")
                } else {
                    MyLog.d("检测APP版本完成，更新中！")
                    Platform.installApp(this@AppBaseActivity, o as File)
                }
            }

            override fun error(message: String) {
                callBack.error("检查APP版本失败：$message")
            }
        })
    }

    /**
     * @param
     * @return null
     * @description 由不可见变为可见的时候调用
     * @author Administrator
     * @time 2023/03/29 15:34
     */
    override fun onStart() {
        super.onStart()
    }

    /**
     * @param
     * @return null
     * @description 获取焦点时
     * @author Administrator
     * @time 2023/03/29 15:30
     */
    override fun onResume() {
        super.onResume()
    }

    /**
     * @param
     * @return null
     * @description 被覆盖或锁屏
     * @author Administrator
     * @time 2023/03/29 15:35
     */
    override fun onPause() {
        super.onPause()
    }

    /**
     * @param
     * @return null
     * @description 不可见后台运行
     * @author Administrator
     * @time 2023/03/29 15:35
     */
    override fun onStop() {
        super.onStop()
    }

    /**
     * @param
     * @return null
     * @description 停止状态再次启动
     * @author Administrator
     * @time 2023/03/29 15:35
     */
    override fun onRestart() {
        super.onRestart()
    }

    /**
     * @param
     * @return null
     * @description 页面销毁时
     * @author Administrator
     * @time 2023/03/29 15:30
     */
    override fun onDestroy() {
        super.onDestroy()
        //本页面移除集合
        (application as MyApplication).removeActivity(this)
        MyLog.d(this.javaClass.name + "移出Activity集合")
    }
}