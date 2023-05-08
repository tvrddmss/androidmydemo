package esa.mydemo.base

import android.app.AlertDialog
import android.content.Context
import androidx.lifecycle.ViewModel
import esa.mylibrary.utils.log.MyLog


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
open class BaseViewModel : ViewModel() {

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
//        AlertDialog.Builder(context).setCancelable(false).setTitle("提示").setMessage(message)
//            .setPositiveButton("返回登录页",
//                DialogInterface.OnClickListener { dialog, which ->
//                    {
//                        UserInfo.setPassword("")
//                        Token.loseEfficacy(getContext())
//                    }
//                })
//            .setNegativeButton("取消",
//                DialogInterface.OnClickListener { dialog, which -> }).show()

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

}