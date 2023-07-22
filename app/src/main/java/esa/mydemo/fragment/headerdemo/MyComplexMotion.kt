package esa.mydemo.fragment.headerdemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import esa.mydemo.R
import esa.mydemo.base.AppBaseFragment
import esa.mydemo.databinding.FragmentMyComplexMotionBinding
import esa.mylibrary.info.AppInfo
import esa.mylibrary.info.DeviceInfo
import esa.mylibrary.info.UserInfo
import org.json.JSONObject

class MyComplexMotion : AppBaseFragment() {
    //数据模型
    private lateinit var mViewModel: MyComplexMotionViewModel

    //xml绑定实例
    private lateinit var binding: FragmentMyComplexMotionBinding

    private lateinit var view: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        exitTransition = MaterialSharedAxis(
//            MaterialSharedAxis.Z,
//            /* forward= */ true
//        ).apply {
//            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
//        }
//        enterTransition = MaterialSharedAxis(
//            MaterialSharedAxis.Z,
//            /* forward= */ false
//        ).apply {
//            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
//        }

        return inflater.inflate(esa.mydemo.R.layout.fragment_my_complex_motion, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(MyComplexMotionViewModel::class.java)
        // TODO: Use the ViewModel

        init()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.view = view
        binding = FragmentMyComplexMotionBinding.bind(view)
    }

    private fun init() {




        binding.motionLayout.setPadding(0, DeviceInfo.statubarHeight, 0, 0)
        binding.root.getConstraintSet(R.id.start).apply {
            getConstraint(R.id.motionLayout).layout.mHeight += DeviceInfo.statubarHeight
        }
        binding.root.getConstraintSet(R.id.end).apply {
            getConstraint(R.id.motionLayout).layout.mHeight += DeviceInfo.statubarHeight
        }
//        binding.toolbar.minimumHeight = DeviceInfo.statubarHeight//+100
        mViewModel.init(view, this)
        try {
            mViewModel.appversion.postValue("当前版本：" + AppInfo.appVersionName)
            mViewModel.userName.postValue(UserInfo.getUserInfo().getString("sys_username"))
            mViewModel.userId.postValue(UserInfo.getUserInfo().getString("sys_userid"))
            run {
                val jsonObject: JSONObject = JSONObject()
                jsonObject.put("IP", DeviceInfo.ip)
                jsonObject.put("MAC", DeviceInfo.mac)
                jsonObject.put("平台", DeviceInfo.platform)
                jsonObject.put("deviceToken", DeviceInfo.deviceTokenString)
                jsonObject.put("设备型号", DeviceInfo.model)
                jsonObject.put("状态栏高度", DeviceInfo.statubarHeight)
                jsonObject.put("设备设置语言", DeviceInfo.local)
                var txt: String? = ""
                val it: Iterator<String> = jsonObject.keys()
                while (it.hasNext()) {
                    val key: String = it.next()
                    txt += key + ":" + jsonObject.getString(key) + "\r\n"
                }
                mViewModel!!.devInfo.postValue(txt)
            }
            run {
                var txt: String? = ""
                val it: Iterator<String> = UserInfo.getUserInfo().keys()
                while (it.hasNext()) {
                    val key: String = it.next()
                    txt += key + ":" + UserInfo.getUserInfo().getString(key) + "\r\n"
                }
                mViewModel!!.userInfo.postValue(txt)
            }
        } catch (ex: Exception) {
            showExceptionMessage(ex.message)
        }
    }




}

