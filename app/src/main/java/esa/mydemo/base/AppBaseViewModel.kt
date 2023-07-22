package esa.mydemo.base

import android.content.Context
import androidx.lifecycle.ViewModel
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.interfaces.OnConfirmListener
import esa.mylibrary.utils.log.MyLog
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


/**
 *
 * @ProjectName: mydemo
 * @Package: esa.mydemo.base
 * @ClassName: BaseViewModel
 * @Description: java类作用描述
 * @Author: Administrator
 * @CreateDate: 2023/04/20 17:33
 * @UpdateUser: Administrator
 * @UpdateDate: 2023/04/20 17:33
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
open class AppBaseViewModel : ViewModel() {

    private lateinit var context: Context

    /**
     * @description 初始化
     * @param null
     * @return null
     * @author Administrator
     * @time 2023/04/20 17:42
     */
    fun setContext(context: Context) {
        this.context = context
    }

    fun getContext(): Context {
        return context
    }

    //region message
    var pop: BasePopupView? = null

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
            XPopup.Builder(context)
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
        val pop =
            XPopup.Builder(context)
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

    /**
     * @description 销毁程序
     * @param null
     * @return null
     * @author Administrator
     * @time 2023/04/20 17:02
     */
    override fun onCleared() {
        super.onCleared()
        MyLog.d(this.javaClass.name + "viewmodel销毁")
    }


    fun CoroutineScope.safeLaunch(
        onError: ((Throwable) -> Unit)? = null,
        onLaunchBlock: () -> Unit
    ) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            onError?.invoke(throwable)
        }

        this.launch(exceptionHandler) {
            onLaunchBlock.invoke()
        }
    }
//    onError = { throwable ->
//        showExceptionMessage(throwable.message ?: "UnKnow Error")
//    }

    fun CoroutineScope.safeWithContext(
        coroutineContext: CoroutineContext,
        onError: ((Throwable) -> Unit)? = null,
        onLaunchBlock: () -> Unit
    ) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            onError?.invoke(throwable)
        }

        this.launch(exceptionHandler) {
            onLaunchBlock.invoke()
        }
    }
}