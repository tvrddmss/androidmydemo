package esa.mydemo.base

import android.app.AlertDialog
import esa.mylibrary.utils.log.MyLog

open class AppBaseFragment : esa.mylibrary.fragment.BaseFragment() {


    //endregion
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
        val a = AlertDialog.Builder(context)
            .setTitle("提示")
            .setMessage(message)
            .setPositiveButton("确定", null)
            .show()
    }

    protected fun showMessage(message: String?) {
        MyLog.i(message)
        val a = AlertDialog.Builder(context)
            .setTitle("提示")
            .setMessage(message)
            .setPositiveButton("确定", null)
            .show()
    }
    //endregion
}