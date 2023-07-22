package esa.mydemo.main.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import esa.mydemo.R
import esa.mydemo.base.AppBaseActivity
import esa.mydemo.dal.Platform.login
import esa.mydemo.databinding.MainActivityLoginBinding
import esa.mydemo.setting.SettingsActivity
import esa.mylibrary.common.CallBack
import esa.mylibrary.info.AppInfo
import esa.mylibrary.info.UserInfo
import esa.mylibrary.utils.log.MyLog


class LoginActivity : AppBaseActivity() {

    private lateinit var binding: esa.mydemo.databinding.MainActivityLoginBinding
    private lateinit var viewModel: LoginActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_ui_detail_blog_articles)
        binding = MainActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[LoginActivityViewModel::class.java]
        viewModel.setContext(binding.root.context)

        binding.viewModel = viewModel

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        init()
    }

    fun init() {
        try {
            when (resources.configuration.locale.language) {
                "zh" -> viewModel.version.value = "当前程序版本:" + AppInfo.appVersionName
                "en" -> viewModel.version.value = "CurrentVersion:" + AppInfo.appVersionName
            }

            //获取当前应用程序设置主题
//        String theme = listPreference.getString("theme", getTheme().toString());
//        setTheme(Reflect.INSTANCE.getStyleId(this, theme));

            //状态栏不占位
//        setStausBarTransparent();//全透明不可设置半透明

            //获取当前应用程序设置主题
//        String theme = listPreference.getString("theme", getTheme().toString());
//        setTheme(Reflect.INSTANCE.getStyleId(this, theme));

            //状态栏不占位
//        setStausBarTransparent();//全透明不可设置半透明
//            setStausBarTranslucent() //可通过theme设置半透明


            viewModel.systemname.value = resources.getString(R.string.app_name)
            viewModel.userName.value = UserInfo.getLoginUsername()
            viewModel.password.value = UserInfo.getPassword()


            binding.btnLogin.setOnClickListener() {
                showloading("登录中。。。")

                login(
                    viewModel.userName.value,
                    viewModel.password.value,
                    object : CallBack<Any?>() {
                        override fun success(o: Any?) {
//                            closeloading()
                            //保存登录名及密码
                            UserInfo.setLoginUsername(viewModel.userName.value)
                            UserInfo.setPassword(viewModel.password.value)
                            //跳转页面
                            val intent = Intent(
                                binding.root.context,
                                esa.mydemo.main.main.MainActivity::class.java
                            )
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            startActivity(intent)
                            finish()

                            overridePendingTransition(
                                android.R.anim.fade_in,
                                android.R.anim.fade_out
                            )
                        }

                        override fun error(message: String) {
                            closeloading()
//                            binding.textInputLayoutPassword.error = message
                            showMessage(message)
                        }
                    })
                false
            }

            binding.txtSystemName.setOnLongClickListener() {
                //跳转页面
                val intent = Intent(binding.root.context, SettingsActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                false
            }

            viewModel.userName.observe(this, Observer {
                if (it.isEmpty()) {
                    binding.textInputLayoutLoginName.error = "不能为空"
                } else {
                    binding.textInputLayoutLoginName.error = ""
                }
                binding.btnLogin.isEnabled = viewModel.enableLogin()
            })

            viewModel.password.observe(this, Observer {
                if (it.isEmpty()) {
                    binding.textInputLayoutPassword.error = "不能为空"
                } else {
                    binding.textInputLayoutPassword.error = ""
                }
                binding.btnLogin.isEnabled = viewModel.enableLogin()
            })
            showloading("初始化。。。")
            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                //第一个Activity初始化
                setIsFirstActiviyInitForSpring(object : CallBack<Any?>() {
                    override fun success(o: Any?) {

                        //判断登录名及密码不能为空时，自动登录
                        if (viewModel.userName.value.toString().isNotEmpty()
                            && viewModel.password.value.toString().isNotEmpty()
                        ) {

                            binding.btnLogin.performClick()

                        }
                        closeloading()
                    }

                    override fun error(message: String) {
                        closeloading()
                        showExceptionMessage(message)
                    }
                })
            }, 1000)


        } catch (ex: Exception) {
            MyLog.e("登录页面初始化失败！" + ex.message)
        }

    }
}