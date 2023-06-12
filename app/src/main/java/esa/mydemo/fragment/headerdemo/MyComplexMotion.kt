package esa.mydemo.fragment.headerdemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import esa.mydemo.R
import esa.mydemo.base.AppBaseFragment
import esa.mydemo.dal.spring.PlatformForSpring
import esa.mydemo.databinding.FragmentMyComplexMotionBinding
import esa.mylibrary.common.CallBack
import esa.mylibrary.info.AppInfo
import esa.mylibrary.info.DeviceInfo
import esa.mylibrary.info.UserInfo
import esa.myupdate.utils.CheckUpdate
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
//            binding.llCheckUpdate.setOnClickListener(View.OnClickListener { checkVersion() })
//
//            binding.tvWeather.typeface =
//                MyIcon.getTypeface(requireContext(), "weather/qweather-icons.ttf")

//            mViewModel.title.observe(viewLifecycleOwner, Observer {
//                binding.tvTitle.text = it
//            })
//            mViewModel.userName.observe(viewLifecycleOwner, Observer {
//                binding.tvUserName.text = it
//            })
//            mViewModel.userId.observe(viewLifecycleOwner, Observer {
//                binding.tvUserId.text = it
//            })
//            mViewModel.appversion.observe(viewLifecycleOwner, Observer {
//                binding.tvAppversion.text = it
//            })
//            mViewModel.devInfo.observe(viewLifecycleOwner, Observer {
//                binding.tvDevInfo.text = it
//            })
//            mViewModel.userInfo.observe(viewLifecycleOwner, Observer {
//                binding.tvUserInfo.text = it
//            })
//            binding.btnLogout.setOnClickListener {
//                mViewModel.onLogoutClick(it)
//            }

//            binding.llWeather.setOnClickListener(object : View.OnClickListener {
//                override fun onClick(v: View) {
//                    if (MyGps.getInstance().location == null) {
//                        return
//                    }
//                    API.getWeatherByLocation(MyGps.getInstance().location, object : Handler() {
//                        override fun handleMessage(msg: Message) {
//                            //更新UI
//                            when (msg.what) {
//                                0 -> {
//                                    try {
//
////                                    showMessage(msg.obj.toString())
//                                        var jsonObject = msg.obj as JSONObject
//                                        var now = jsonObject.getJSONObject("now")
//                                        var txt = StringBuilder().apply {
//
//                                            var localDate =
//                                                LocalDateTime.parse(
//                                                    jsonObject.getString("updateTime"),
//                                                    DateTimeFormatter.ISO_OFFSET_DATE_TIME
//                                                )
//                                            appendLine(
//                                                "更新时间:" + localDate.format(
//                                                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
//                                                )
//                                            )
//                                            append("" + now.getString("temp") + "℃")
//                                            append(
//                                                "  " + API.getIcon(
//                                                    now.getString(
//                                                        "icon"
//                                                    )
//                                                )
//                                            )
//                                            appendLine("  " + now.getString("text"))
//                                            append("风方向:" + now.getString("wind360"))
//                                            append("  " + now.getString("windDir"))
//                                            append("  " + now.getString("windScale") + "级")
//                                            appendLine("  " + now.getString("windSpeed") + "（公里/小时）")
//                                            appendLine("相对湿度:" + now.getString("humidity"))
//                                            appendLine("当前小时累计降水量:" + now.getString("precip") + "（毫米）")
//                                            appendLine("大气压强:" + now.getString("pressure") + "（百帕）")
//                                            appendLine("相对湿度:" + now.getString("humidity") + "%")
//                                            appendLine("云量:" + now.getString("cloud") + "%")
//                                            appendLine("露点温度:" + now.getString("dew") + "℃")
////                                        append("数据来源:" + now.getString("sources"))
////                                        append("数据许可或版权声明:" + now.getString("license"))
//                                        }
//
//                                        binding.tvWeather.text = txt.toString()
//                                    } catch (ex: Exception) {
//                                        showExceptionMessage(ex.message)
//                                    }
//                                }
//
//                                1 -> showExceptionMessage(msg.obj.toString())
//                            }
//                        }
//                    })
//                }
//            })
//
//            binding.llWeather.apply {
//
//                //定时获取天气
//                val timer = Timer()
//                var task: TimerTask? = null
//                var handler: Handler = object : Handler() {
//                    override fun handleMessage(msg: Message) {
//                        // TODO Auto-generated method stub
//                        // 要做的事情
//                        super.handleMessage(msg)
//                        binding.llWeather.performClick()
//
//                    }
//                }
//                task = object : TimerTask() {
//                    override fun run() {
//                        //获取当前时间
//                        var date = Date(System.currentTimeMillis())
//                        if (date.minutes == 0) {
//                            val message = Message()
//                            message.what = 1
//                            handler.sendMessage(message)
//                        }
//                    }
//                }
//                timer.schedule(task, 0, 60 * 1000);
//                binding.llWeather.performClick()
//            }

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


    private fun checkVersion() {
        PlatformForSpring.getNewVersionUrl(context, object : CallBack<Any?>() {
            override fun success(o: Any?) {
                try {
                    if (o == null) {
                        showMessage("已是最新版本")
                        return
                    }
                    val appinfo = o as JSONObject
                    //判断是否有需要安装新版本
                    if (AppInfo.apkurl !== "") {
                        val content = Array<String>(2) { "";"" }
                        content[0] = appinfo.getString("description")
                        content[1] = appinfo.getString("note")
                        CheckUpdate.checkUpdate(
                            (activity)!!,
                            AppInfo.apkurl,
                            content, false,
                            appinfo.getString("version"),
                            "新版本标题"
                        )
                    }
                } catch (ex: Exception) {
                    showExceptionMessage(ex.message)
                }
            }


            override fun error(message: String) {
                showExceptionMessage(message)
            }
        })
    }


}

