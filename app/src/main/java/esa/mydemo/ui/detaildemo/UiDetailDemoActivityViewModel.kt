package esa.mydemo.ui.detaildemo

import android.view.View
import esa.mydemo.base.BaseViewModel

class UiDetailDemoActivityViewModel : BaseViewModel() {

    lateinit var onClickListener: View.OnClickListener

    /**
     * @description 销毁程序
     * @param null
     * @return null
     * @author Administrator
     * @time 2023/04/20 17:02
     */
    override fun onCleared() {
        super.onCleared()
    }

    /**
     * @param view
     * @return void
     * @description 修改密码
     * @author tvrddmss
     * @time 2023/4/7 21:18
     */
    fun onEditPasswordClick(view: View?) {
        onClickListener?.onClick(view)
    }
}