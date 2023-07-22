package esa.mylibrary.info

import android.content.Context
import esa.mylibrary.code.CodeDao
import esa.mylibrary.utils.MyJson
import esa.mylibrary.utils.MySharedPreferences
import esa.mylibrary.utils.log.MyLog
import org.json.JSONArray
import org.json.JSONObject

/**
 * @ProjectName: mydemo
 * @Package: esa.mylibrary.info
 * @ClassName: CodeInfo
 * @Description: java类作用描述
 * @Author: Administrator
 * @CreateDate: 2023/03/31 11:28
 * @UpdateUser: Administrator
 * @UpdateDate: 2023/03/31 11:28
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
object CodeInfo {
    var codeVersion: String = ""
    var codeMap: MutableMap<String, JSONArray> = mutableMapOf()

    /**
     * @param jsonObject
     * @return void
     * @description 设置字典--从jsonstring
     * @author Administrator
     * @time 2023/03/31 13:18
     */
    @Throws(Exception::class)
    fun setCode(jsonObject: JSONObject) {
        //保存字典
        MyLog.d(jsonObject.toString())
        val rows = jsonObject.getJSONArray("rows")
        if (rows.length() > 0) {
            codeMap = mutableMapOf()
            for (i in 0 until rows.length()) {
                val o = rows.getJSONObject(i)
                if (!codeMap.containsKey(o.getString("nodeid"))) {
                    codeMap[o.getString("nodeid")] = JSONArray()
                }
                val codeJSONArray = codeMap.get(o.getString("nodeid"))
                codeJSONArray!!.put(o)
            }
            MyLog.d(codeMap.toString())
            //保存版本号
            codeVersion = jsonObject.getString("codeversion")

            //保存到临时存储
            save()
        }
    }

    /**
     * @return void
     * @description 初始化
     * @author Administrator
     * @time 2023/03/31 15:28
     */
    @JvmStatic
    fun init(context: Context) {
        codeVersion = MySharedPreferences.base.getString("codeversion", "-1").toString()
//        val mapString = MySharedPreferences.base.getString("codemap", "")
//        if (!mapString!!.isEmpty()) {
//            codeMap = HashMap()
//            codeMap = MyJson.fromJson(mapString, codeMap.javaClass) as MutableMap<String, JSONArray>
//        } else {
//            codeMap = HashMap()
//        }
        initFromSqlite(context)
        MyLog.d("初始化code:" + codeVersion)
    }

    /**
     * @return void
     * @description 保存
     * @author Administrator
     * @time 2023/03/31 15:27
     */
    fun save() {
        val editor = MySharedPreferences.base.edit()
        editor.putString("codeversion", codeVersion)
//        editor.putString("codemap", codeMap.toString())
        editor.commit()
        MyLog.d("保存code:" + codeVersion)
    }

    /**
     * @return void
     * @description 从数据库初始化数据字典
     * @author Administrator
     * @time 2023/03/31 15:27
     */
    fun initFromSqlite(context: Context) {

        codeMap = mutableMapOf()
        var tempJSONArray = JSONArray()
        var tempGroupid = ""
        var codeBeanList = CodeDao.getInstance(context).selectAll()
        codeBeanList.forEach {
            if (!it.groupId.equals(tempGroupid)) {
                tempJSONArray = JSONArray()
                codeMap.put(it.groupId, tempJSONArray)
                tempGroupid = it.groupId
            }
            tempJSONArray.put(
                JSONObject()
                    .put("codevalue", it.codeValue)
                    .put("codetext", it.codeText)
            )
        }
    }
}