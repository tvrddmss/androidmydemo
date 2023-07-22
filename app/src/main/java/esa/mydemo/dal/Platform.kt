package esa.mydemo.dal

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.core.content.FileProvider
import esa.mydemo.data.code.CodeBean
import esa.mydemo.data.code.CodeDao
import esa.mylibrary.api.ApiCallBack
import esa.mylibrary.api.Http
import esa.mylibrary.apinew.RetrofitCallback
import esa.mylibrary.apinew.RetrofitUtil
import esa.mylibrary.common.CallBack
import esa.mylibrary.config.Config
import esa.mylibrary.info.AppInfo
import esa.mylibrary.info.CodeInfo
import esa.mylibrary.info.UserInfo
import esa.mylibrary.utils.MyJson
import esa.mylibrary.utils.log.MyLog
import esa.myupdate.utils.CheckUpdate.checkUpdate
import org.json.JSONObject
import java.io.File
import java.io.IOException

/**
 * @ProjectName: mydemo
 * @Package: esa.mydemo.dal
 * @ClassName: Platform
 * @Description: java类作用描述
 * @Author: Administrator
 * @CreateDate: 2023/03/30 9:39
 * @UpdateUser: Administrator
 * @UpdateDate: 2023/03/30 9:39
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
object Platform {
    /**
     * @description 基础网络路径
     * @author Administrator
     * @time 2023/03/30 9:51
     */
    var baseUrl = "appVersion/"

    /**
     * @param context
     * @return null
     * @description 检测版本
     * @author Administrator
     * @time 2023/03/29 15:05
     */
    @JvmStatic
    fun checkVersion(activity: Activity, callBack: CallBack<*>) {
        val url = baseUrl + "selectMaxVersionCode"
        try {
            //app版本对比
            val data = JSONObject()
            //            data.put("name", AppInfo.appnameen);
            Config.api.project = "api"
            Http.get(url, data, object : ApiCallBack<Any?>() {
                override fun onSuccess(o: Any?) {
                    try {
                        val jsonObject = MyJson.parse(o.toString()) as JSONObject
                        if (jsonObject.getLong("versionCode") != AppInfo.appVersionCode) {
                            AppInfo.apkurl = jsonObject.getString("url")
                            if (AppInfo.apkurl !== "") {
                                val content = arrayOf(
                                    jsonObject.getString("description"),
                                    jsonObject.getString("note")
                                )

                                checkUpdate(
                                    activity,
                                    AppInfo.apkurl,
                                    content, false,
                                    jsonObject.getString("version"),
                                    "新版本标题"
                                )
                            }
                            callBack.success(null)

                        } else if (jsonObject.getLong("dataDictionartyVersion") != CodeInfo.codeVersion.toLong()) {
                            MyLog.d("获取数据字典")
                            AppInfo.apkurl = ""
                            callBack.success(null)
                        } else {
                            AppInfo.apkurl = ""
                            callBack.success(null)
                        }
                    } catch (ex: Exception) {
                        callBack.error(
                            """
                                ${Platform::class.java.name}
                                $url,
                                网络请求结果解析失败！${ex.message}
                                """.trimIndent()
                        )
                    }
                }

                override fun onError(code: Int) {
                    callBack.error(
                        """
                            ${Platform::class.java.name}
                            $url,
                            网络请求错误！错误编码：$code
                            """.trimIndent()
                    )
                }

                override fun onFailure(e: IOException) {
                    callBack.error(
                        """
                            ${Platform::class.java.name}
                            $url,
                            网络请求失败！失败：$e
                            """.trimIndent()
                    )
                }
            })
        } catch (ex: Exception) {
            callBack.error(
                """
                    ${Platform::class.java.name}
                    $url,
                    ,报错：${ex.message}
                    """.trimIndent()
            )
        }
    }

    @JvmStatic
    fun getNewVersionUrl(context: Context?, callBack: CallBack<Any?>) {
        var apkUrl = ""
        val url = baseUrl + "selectMaxVersionCode"
        try {
            //app版本对比
            val data = JSONObject()
            Config.api.project = "api"
            Http.get(url, data, object : ApiCallBack<Any?>() {
                override fun onSuccess(o: Any?) {
                    try {
                        val jsonObject = MyJson.parse(o.toString()) as JSONObject
                        if (jsonObject.getLong("versionCode") != AppInfo.appVersionCode) {
                            AppInfo.apkurl = jsonObject.getString("url")
                            callBack.success(jsonObject)
                        } else {
                            AppInfo.apkurl = ""
                            callBack.success(null)
                        }
                    } catch (ex: Exception) {
                        callBack.error(
                            """
                                ${Platform::class.java.name}
                                $url,
                                网络请求结果解析失败！${ex.message}
                                """.trimIndent()
                        )
                    }
                }

                override fun onError(code: Int) {
                    callBack.error(
                        """
                            ${Platform::class.java.name}
                            $url,
                            网络请求错误！错误编码：$code
                            """.trimIndent()
                    )
                }

                override fun onFailure(e: IOException) {
                    callBack.error(
                        """
                            ${Platform::class.java.name}
                            $url,
                            网络请求失败！失败：$e
                            """.trimIndent()
                    )
                }
            })
        } catch (ex: Exception) {
            callBack.error(
                """
                    ${Platform::class.java.name}
                    $url,
                    ,报错：${ex.message}
                    """.trimIndent()
            )
        }
    }

    /**
     * @param
     * @return null
     * @description
     * @author Administrator
     * @time 2023/03/29 15:55
     */
    fun installApp(context: Context, appFile: File) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (!appFile.exists()) {
                MyLog.e("apk文件不存在!" + appFile.absolutePath)
                throw Exception("文件不存在！")
            }
            var uri: Uri? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                uri = FileProvider.getUriForFile(
                    context.applicationContext,
                    AppInfo.apppagename + ".provider",
                    appFile
                )
            } else {
                uri = Uri.fromFile(appFile)
            }
            intent.setDataAndType(uri, "application/vnd.android.package-archive")
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            MyLog.e(e.message)
            //网页下载
            val uri = Uri.parse(AppInfo.apkurl)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            //检测是否可以正常打开
            if (intent.resolveActivity(context.packageManager) != null) {
                // 网址正确 跳转成功
                context.startActivity(intent)
            } else {
                //提示
                Toast.makeText(context, "无法打开浏览器", Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * @param context
     * @param callBack
     * @return void
     * @description
     * @author Administrator
     * @time 2023/03/31 11:31
     */
    fun checkCodeVersion(context: Context?, callBack: CallBack<*>) {
        val url = baseUrl + "checkCodeVersion"
        try {
            val data = JSONObject()
            data.put("codeversion", CodeInfo.codeVersion)
            data.put("clientInf", UserInfo.getClientInfo())
            Http.post(url, data, object : ApiCallBack<Any?>() {
                override fun onSuccess(o: Any?) {
                    try {
                        val jsonObject = MyJson.parse(o.toString()) as JSONObject
                        CodeInfo.setCode(jsonObject)
                        callBack.success(null)
                    } catch (ex: Exception) {
                        MyLog.e(this.javaClass.toString() + ":" + ex.message)
                        callBack.error(
                            """
                                ${Platform::class.java.name}
                                $url,
                                网络请求返回结果解析失败：${ex.message}
                                """.trimIndent()
                        )
                    }
                }

                override fun onError(code: Int) {
                    callBack.error(
                        """
                            ${Platform::class.java.name}
                            $url,
                            网络请求错误！错误编码：$code
                            """.trimIndent()
                    )
                }

                override fun onFailure(e: IOException) {
                    callBack.error(
                        """
                            ${Platform::class.java.name}
                            $url,
                            网络请求失败！失败：$e
                            """.trimIndent()
                    )
                }
            })
        } catch (ex: Exception) {
            callBack.error(
                """
                    ${Platform::class.java.name}
                    $url,
                    报错：${ex.message}
                    """.trimIndent()
            )
        }
    }

    /**
     * @param userLoginNameString, userPasswordString,  callBack
     * @return null
     * @description 登录
     * @author Administrator
     * @time 2023/03/30 9:52
     */
    @JvmStatic
    fun login(userLoginNameString: String?, userPasswordString: String?, callBack: CallBack<*>) {
        val url = "login"
        try {
            val data = JSONObject()
            data.put("code", "code")
            data.put("password", userPasswordString)
            data.put("userName", userLoginNameString)
            data.put("uuid", "uuid")
            Http.post(url, data, object : ApiCallBack<JSONObject?>() {
                /**
                 * @description result=true
                 * @param o
                 * @return void
                 * @author Administrator
                 * @time 2023/03/30 9:48
                 */
                override fun onSuccess(o: JSONObject?) {
                    try {
                        Config.api.loginToken = o?.getString("token")

                        //region
                        val userInfString = """      {
            "sys_userid": "3229",
            "sys_username": "数据采集",
            "sys_userloginname": "sjcj",
            "sys_photourl": "",
            "sys_value1": "",
            "sys_value2": "",
            "sys_value3": "",
            "sys_value4": "",
            "sys_value5": "",
            "sys_value6": "",
            "sys_value7": "",
            "sys_value8": "",
            "sys_value9": "",
            "sys_value10": "100",
            "sys_toporgan": "40",
            "sys_organid": "",
            "sys_organcode": "",
            "sys_organname": "",
            "sys_toporganname": "",
            "sys_xzqy": [{
                "id": "022",
                "text": "天津市"
             }],
            "sys_roles": "",
            "sys_rolenames": "",
            "sys_rolenameremarks": "",
            "sys_positionids": "",
            "sys_positionnames": "",
            "sys_groups": [],
            "sys_fields": [],
            "sys_rules": [{
                "f_id": "3319",
                "f_code": "0345",
                "f_name": "app数据采集",
                "f_url": "0102",
                "f_target": "gzrw",
                "f_tile": "",
                "f_rulemodel": "3",
                "f_sys_appcode": "40",
                "f_children": []
                }
            ]
    }"""

                        //endregion
                        val userInf = MyJson.parse(userInfString) as JSONObject
                        val user = o?.getJSONObject("userInfo")?.getJSONObject("user")
                        userInf.put("sys_username", user?.getString("nickName"))
                        userInf.put("sys_id", user?.getInt("id").toString() + "")
                        userInf.put("sys_userid", user?.getInt("id").toString() + "")
                        userInf.put("sys_userloginname", user?.getString("username"))
                        UserInfo.setUserInfo(userInf)
                        callBack.success(null)
                    } catch (ex: Exception) {
                        MyLog.e(this.javaClass.toString() + ":" + ex.message)
                        callBack.error(
                            """
                                ${Platform::class.java.name}
                                $url,
                                网络请求返回结果解析失败：${ex.message}
                                """.trimIndent()
                        )
                    }
                }

                /**
                 * @description 网络错误
                 * @param code
                 * @return void
                 * @author Administrator
                 * @time 2023/03/30 9:49
                 */
                override fun onError(code: Int) {
                    callBack.error(
                        """
                            ${Platform::class.java.name}
                            $url,
                            网络请求错误！错误编码：$code
                            """.trimIndent()
                    )
                }

                /**
                 * @description result=false
                 * @param e
                 * @return void
                 * @author Administrator
                 * @time 2023/03/30 9:48
                 */
                override fun onFailure(e: IOException) {
                    callBack.error(
                        """${e.message}
                            """.trimIndent()
                    )
                }
            })
        } catch (ex: Exception) {
            callBack.error(
                """
                    ${Platform::class.java.name}
                    $url,
                    ,报错：${ex.message}
                    """.trimIndent()
            )
        }
    }


    /**
     * @param
     * @return null
     * @description 获取字典
     * @author Administrator
     * @time 2023/03/30 9:52
     */
    @JvmStatic
    fun getCode(codeVersionNew: Long, context: Context, callBack: CallBack<Any?>) {
        var url = "tcode/GetCode"
        RetrofitUtil.get(url, object : RetrofitCallback() {
            override fun onSuccess(resultJsonString: String?) {
                try {

                    val jsonObject = MyJson.parse(resultJsonString) as JSONObject

                    CodeDao.getInstance(context).clear()
                    for (i in 0 until jsonObject.getJSONArray("rows").length()) {
                        var jsonObject = jsonObject.getJSONArray("rows").getJSONObject(i)
                        var codeBean = CodeBean.Builder()
                            .setId(i)
                            .setGroupId(jsonObject.getString("parentnodeid"))
                            .setCodeValue(jsonObject.getString("nodevalue"))
                            .setCodeText(jsonObject.getString("nodename"))
                            .build()
                        CodeDao.getInstance(context).insert(codeBean)
                    }

                    CodeInfo.initFromSqlite(context)

                    //字典版本号保存
                    CodeInfo.codeVersion = codeVersionNew.toString()
                    CodeInfo.save()

                    callBack.success(null)
                } catch (ex: Exception) {
                    callBack.error(
                        """
                                ${Platform::class.java.name}
                                $url,
                                数据字典解析失败！${ex.message}
                                """.trimIndent()
                    )
                }
            }

            override fun onError(t: Throwable?) {
                callBack.error(
                    """
                            ${Platform::class.java.name}
                            $url,
                            网络请求失败！失败：${t?.message}
                            """.trimIndent()
                )
            }
        })
    }


    /**
     * @param
     * @return null
     * @description 更新密码
     * @author Administrator
     * @time 2023/03/30 9:52
     */
    fun updatePassword(
        userPasswordString: String,
        userNewPasswordString: String,
        callBack: CallBack<Any?>
    ) {

        val url = "tauth/UpdatePassword"
        try {
            var data: MutableMap<String, String> = mutableMapOf()
            data["userLoginNameString"] = UserInfo.getLoginUsername()
            data["userPasswordString"] = userPasswordString!!
            data["newPasswordString"] = userNewPasswordString
            data["clientInf"] = UserInfo.getClientInfo()
            RetrofitUtil.post(url, data, object : RetrofitCallback() {
                override fun onSuccess(resultJsonString: String?) {
                    try {
                        callBack.success(null)
                    } catch (ex: Exception) {
                        MyLog.e(this.javaClass.toString() + ":" + ex.message)
                        callBack.error(
                            """
                                ${Platform::class.java.name}
                                $url,
                                网络请求返回结果解析失败：${ex.message}
                                """.trimIndent()
                        )
                    }
                }

                /**
                 * @description 网络错误
                 * @param code
                 * @return void
                 * @author Administrator
                 * @time 2023/03/30 9:49
                 */
                override fun onError(t: Throwable?) {
                    callBack.error(
                        """
                            ${Platform::class.java.name}
                            $url,
                            网络请求失败！失败：${t?.message}
                            """.trimIndent()
                    )
                }
            })
        } catch (ex: Exception) {
            callBack.error(
                """
                    ${Platform::class.java.name}
                    $url,
                    ,报错：${ex.message}
                    """.trimIndent()
            )
        }
    }
}