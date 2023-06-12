package esa.mydemo.fragment.myprofile

import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import esa.mydemo.base.BaseViewModel
import esa.mydemo.main.LoginActivity
import esa.mylibrary.info.UserInfo

class MyProfileViewModel : BaseViewModel() {
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

    /**
     * @param view
     * @return void
     * @description 注销
     * @author tvrddmss
     * @time 2023/4/7 21:18
     */
    fun onLogoutClick(view: View?) {
        UserInfo.setPassword("")

        //跳转页面
        val intent = Intent(this.view!!.context, LoginActivity::class.java)
        this.view!!.context.startActivity(intent)
        fragment.activity?.finish()
    }

}