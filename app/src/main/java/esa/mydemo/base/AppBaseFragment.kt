package esa.mydemo.base

import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.interfaces.OnConfirmListener
import esa.mylibrary.utils.log.MyLog

open class AppBaseFragment : esa.mylibrary.fragment.BaseFragment() {


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
            XPopup.Builder(requireContext())
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
            XPopup.Builder(requireContext())
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
            pop = XPopup.Builder(requireContext())
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