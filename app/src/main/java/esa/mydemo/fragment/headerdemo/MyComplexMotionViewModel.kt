package esa.mydemo.fragment.headerdemo

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import esa.mydemo.base.AppBaseViewModel

class MyComplexMotionViewModel : AppBaseViewModel() {
    // TODO: Implement the ViewModel
    //数据绑定
    var appversion = MutableLiveData("版本")
    var title = MutableLiveData("个人信息")
    var userName = MutableLiveData("用户名")
    var userId = MutableLiveData("ID")
    var devInfo = MutableLiveData("设备信息")
    var userInfo = MutableLiveData("用户详细信息")
    private lateinit var view: View
    private lateinit var fragment: Fragment
    fun init(view: View, myProfileFragment: Fragment) {
        this.view = view
        fragment = myProfileFragment
    }


}