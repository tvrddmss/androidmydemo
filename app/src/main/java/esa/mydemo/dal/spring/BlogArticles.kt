package esa.mydemo.dal.spring

import esa.mylibrary.api.ApiCallBack
import esa.mylibrary.api.Http
import esa.mylibrary.common.CallBack
import org.json.JSONObject
import java.io.IOException


object BlogArticles {
    private var baseUrl = "article/"

    /**
     * @param
     * @return null
     * @description 查询
     * @author Administrator
     * @time 2023/03/29 15:05
     */
    fun getList(
        indexInt: Int?,
        sizeInt: Int?,
        callBack: CallBack<JSONObject>
    ) {
        val url = baseUrl + "selectPage"
        try {
            val data = JSONObject()
            data.put("index", indexInt)
            data.put("size", sizeInt)
            Http.get(url, data, object : ApiCallBack<JSONObject>() {
                override fun onSuccess(o: JSONObject) {
                    try {
                        callBack.success(o)
                    } catch (ex: java.lang.Exception) {
                        callBack.error(
                            """
                            ${this::class.java.name}
                            $url,
                            网络请求结果解析失败！${ex.message}
                            """.trimIndent()
                        )
                    }
                }

                override fun onError(code: Int) {
                    callBack.error(
                        """
                        ${this::class.java.name}
                        $url,
                        网络请求错误！错误编码：$code
                        """.trimIndent()
                    )

                }

                override fun onFailure(e: IOException) {
                    callBack.error(
                        """
                        ${this::class.java.name}
                        $url,
                        网络请求失败！失败：$e
                        """.trimIndent()
                    )
                }
            })
        } catch (ex: java.lang.Exception) {
            callBack.error(
                """
                ${this::class.java.name}
                $url,
                ,报错：${ex.message}
                """.trimIndent()
            )
        }
    }

    fun update(
        jsonObject: JSONObject,
        callBack: CallBack<Int>
    ) {
        val url = baseUrl + "update"
        try {
            //app版本对比
            val data = jsonObject
            Http.put(url, data, object : ApiCallBack<Int>() {
                override fun onSuccess(o: Int) {
                    try {
                        callBack.success(o)
                    } catch (ex: java.lang.Exception) {
                        callBack.error(
                            """
                            ${this::class.java.name}
                            $url,
                            网络请求结果解析失败！${ex.message}
                            """.trimIndent()
                        )
                    }
                }

                override fun onError(code: Int) {
                    callBack.error(
                        """
                        ${this::class.java.name}
                        $url,
                        网络请求错误！错误编码：$code
                        """.trimIndent()
                    )
                }

                override fun onFailure(e: IOException) {
                    callBack.error(
                        """
                        ${this::class.java.name}
                        $url,
                        网络请求失败！失败：$e
                        """.trimIndent()
                    )
                }
            })
        } catch (ex: java.lang.Exception) {
            callBack.error(
                """
                ${this::class.java.name}
                $url,
                ,报错：${ex.message}
                """.trimIndent()
            )
        }
    }
}