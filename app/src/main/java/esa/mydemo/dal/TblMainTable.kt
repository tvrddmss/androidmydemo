package esa.mydemo.dal

import esa.mylibrary.api.ApiCallBack
import esa.mylibrary.api.Http
import esa.mylibrary.common.CallBack
import org.json.JSONObject
import java.io.IOException

object TblMainTable {
    private var baseUrl = "tblmaintableapp/"

    /**
     * @param
     * @return null
     * @description 查询
     * @author Administrator
     * @time 2023/03/29 15:05
     */
    fun getList(
        whereString: String?,
        orderByString: String?,
        columnsString: String?,
        stepString: String?,
        queryString: String?,
        lx: String?,
        countString: String?, callBack: CallBack<JSONObject>
    ) {
        val url = baseUrl + "getlistwithimgforapp"
        try {
            //app版本对比
            val data = JSONObject()

            data.put("whereString", whereString)
            data.put("orderByString", orderByString)
            data.put("columnsString", columnsString)
            data.put("stepString", stepString)
            data.put("countString", countString)
            data.put("queryString", queryString)
            data.put("lx", lx)
            data.put("clientInf", "")//UserInfo.getClientInfo())
            Http.post(url, data, object : ApiCallBack<JSONObject>() {
                override fun onSuccess(o: JSONObject) {
                    try {
                        callBack.success(o)
                    } catch (ex: java.lang.Exception) {
                        callBack.error(
                            """
                            ${TblMainTable::class.java.name}
                            $url,
                            网络请求结果解析失败！${ex.message}
                            """.trimIndent()
                        )
                    }
                }

                override fun onError(code: Int) {
                    callBack.error(
                        """
                        ${TblMainTable::class.java.name}
                        $url,
                        网络请求错误！错误编码：$code
                        """.trimIndent()
                    )
                }

                override fun onFailure(e: IOException) {
                    callBack.error(
                        """
                        ${TblMainTable::class.java.name}
                        $url,
                        网络请求失败！失败：$e
                        """.trimIndent()
                    )
                }
            })
        } catch (ex: java.lang.Exception) {
            callBack.error(
                """
                ${TblMainTable::class.java.name}
                $url,
                ,报错：${ex.message}
                """.trimIndent()
            )
        }
    }
}