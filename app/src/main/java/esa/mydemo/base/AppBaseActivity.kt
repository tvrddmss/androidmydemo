package esa.mydemo.base

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.interfaces.OnConfirmListener
import esa.mydemo.MyApplication
import esa.mydemo.dal.Platform.checkVersion
import esa.mylibrary.activity.BaseActivity
import esa.mylibrary.common.CallBack
import esa.mylibrary.info.DeviceInfo
import esa.mylibrary.utils.log.MyLog


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
     * @description 第一个页面创建时，初始化检查各种版本,用于spring
     * @author tvrddmss
     * @time 2023/4/11 17:03
     */
    protected fun setIsFirstActiviyInitForSpring(callBack: CallBack<Any?>) {

        showloading("检查APP版本。。。")
        //检测APP版本
        checkVersion(this, object : CallBack<Any?>() {
            override fun success(o: Any?) {
                callBack.success("")
            }

            override fun error(message: String) {
                callBack.error("检查APP版本失败：$message")
            }
        })

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)


    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        //当第一个活动屏幕展开后，计算屏幕大小
        DeviceInfo.getScreen(this)
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

    //region message
    /**
     * @param message
     * @return null
     * @description
     * @author Administrator
     * @time 2023/03/29 13:44
     */
    protected fun showExceptionMessage(message: String?) {
        MyLog.e(message)
        pop =
            XPopup.Builder(this)
                /*
                        * title:标题
                        * content：内容
                        * cancelBtnText：取消按钮文本(左边按钮文本)
                        * confirmBtnText：确定按钮文本(右边按钮文本)
                        * confirmListener：确定按钮点击事件(右边按钮点击事件)
                        * cancelListener：取消按钮点击事件(左边按钮点击事件)
                        * isHideCancel：是否隐藏左侧按钮(设置单个点击按钮使用)
                        */
                .asConfirm(
                    "错误信息", message,
                    "取消", "确定",
                    OnConfirmListener() {
                        @Override
                        fun onConfirm() {
                            pop?.dismiss();
                        }

                    }, null, true
                )
                .show()
    }

    protected fun showMessage(message: String?) {
        MyLog.i(message)
         pop =
            XPopup.Builder(this)
                /*
                        * title:标题
                        * content：内容
                        * cancelBtnText：取消按钮文本(左边按钮文本)
                        * confirmBtnText：确定按钮文本(右边按钮文本)
                        * confirmListener：确定按钮点击事件(右边按钮点击事件)
                        * cancelListener：取消按钮点击事件(左边按钮点击事件)
                        * isHideCancel：是否隐藏左侧按钮(设置单个点击按钮使用)
                        */
                .asConfirm(
                    "提示信息", message,
                    "取消", "确定",
                    OnConfirmListener() {
                        @Override
                        fun onConfirm() {
                            pop?.dismiss();
                        }

                    }, null, true
                )
                .show()
    }
    //endregion

    //region loading
    var pop: BasePopupView? = null

    /* 显示loading */
    open fun showloading(text: String?) {
        try {
            pop?.dismiss()
            pop = XPopup.Builder(this)
                .asLoading(text)
                .show()
        } catch (ex: Exception) {
            MyLog.e(ex.message)
        }
    }

    /* 关闭loading */
    open fun closeloading() {
        pop?.dismiss()
    }
    //endregion
}