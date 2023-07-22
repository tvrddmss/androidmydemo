package esa.mydemo.myrpofile

import android.os.Bundle
import android.view.Window
import androidx.lifecycle.ViewModelProvider
import esa.mydemo.R
import esa.mydemo.base.AppBaseActivity
import esa.mydemo.dal.Platform
import esa.mydemo.databinding.ActivityMyProfilePasswordBinding
import esa.mylibrary.common.CallBack

class MyProfilePasswordActivity : AppBaseActivity() {

    private lateinit var binding: ActivityMyProfilePasswordBinding
    lateinit var viewModel: MyProfilePasswordActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        setContentView(R.layout.activity_my_profile_password)

        binding = ActivityMyProfilePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MyProfilePasswordActivityViewModel::class.java)
        viewModel.setContext(binding.root.context)

        binding.viewModel = viewModel
        init()
    }

    fun init() {
        binding.btnComfirm.isEnabled = false
        viewModel.oldpassword.observe(this) {
            if (viewModel.oldpassword.value!!.isEmpty()) {
                binding.textInputLayoutOldPassword.error = "不能为空"
            } else {
                binding.textInputLayoutOldPassword.error = ""
            }
            valueChange()
        }
        viewModel.newpassword.observe(this) {
            if (viewModel.newpassword.value!!.isEmpty()) {
                binding.textInputLayoutNewPassword.error = "不能为空"
            } else {
                binding.textInputLayoutNewPassword.error = ""
            }
            valueChange()
        }
        viewModel.newpasswordp.observe(this) {
            if (viewModel.newpasswordp.value!!.isEmpty()) {
                binding.textInputLayoutNewPasswordP.error = "不能为空"
            } else {
                binding.textInputLayoutNewPasswordP.error = ""
            }
            valueChange()
        }


        binding.btnComfirm.setOnClickListener {
            Platform.updatePassword(viewModel.oldpassword.value!!, viewModel.newpassword.value!!,
                object : CallBack<Any?>() {
                    override fun success(o: Any?) {
                        showMessage("操作成功")
                        finish()
                    }

                    override fun error(message: String) {
                        showMessage(message)
                    }
                })
        }
        binding.btnCancel.setOnClickListener {
            this.finish()
        }
    }


    fun valueChange() {
        if (!viewModel.oldpassword.value!!.isEmpty()
            && !viewModel.newpassword.value!!.isEmpty()
            && !viewModel.newpasswordp.value!!.isEmpty()
        ) {
            binding.btnComfirm.isEnabled = true
        } else {
            binding.btnComfirm.isEnabled = false
        }
        if (!viewModel.newpassword.value.equals(viewModel.newpasswordp.value)) {
            binding.textInputLayoutNewPasswordP.error = "两次密码不一致"
        } else {
            binding.textInputLayoutNewPasswordP.error = ""
        }
    }
}