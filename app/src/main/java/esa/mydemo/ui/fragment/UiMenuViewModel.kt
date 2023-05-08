package esa.mydemo.ui.fragment

import esa.mydemo.base.BaseViewModel
import org.json.JSONArray
import org.json.JSONObject

class UiMenuViewModel : BaseViewModel() {
    // TODO: Implement the ViewModel
    lateinit var jsonArray: JSONArray
    var gap: Int = 0
    var columsCount: Int = 0

    fun init() {
        gap = 20
        columsCount = 4

        jsonArray = JSONArray().apply {

            put(JSONObject().apply {
                put("icon", "\ue996")
                put("type", "activity")
                put("class", "esa.mydemo.ui.blog_articles.UiListActivity")
                put("label", "list流式")
            })
            put(JSONObject().apply {
                put("icon", "\ue997")
                put("type", "devclass")
                put("url", "http://152.136.180.178/MyDex.dex")
                put("class", "com.example.mydex.MyDex")
                put("method", "getName")
                put("label", "DEV包测试")
            })
            put(JSONObject().apply {
                put("icon", "\ue994")
                put("type", "activity")
                put("class", "esa.mydemo.ui.threed.ThreeActivity")
                put("label", "3D测试")
            })
            put(JSONObject().apply {
                put("icon", "\ue98f")
                put("type", "activity")
                put("class", "esa.mydemo.ui.chart.ChartActivity")
                put("label", "chart")
            })
            put(JSONObject().apply {
                put("icon", "\uE98D")
                put("type", "activity")
                put("class", "esa.mydemo.ui.detaildemo.UiDetailDemoActivity")
                put("label", "Material样例")
            })

            var icons = ("\uE998\n" +
                    "\uE999\n" +
                    "\uE99A\n" +
                    "\uE99B\n" +
                    "\uE99C\n" +
                    "\uE98B\n" +
                    "\uE98C\n" +
                    "\uE98D\n" +
                    "\uE98E\n" +
                    "\uE98F\n" +
                    "\uE990\n" +
                    "\uE991\n" +
                    "\uE992\n" +
                    "\uE993\n" +
                    "\uE994\n" +
                    "\uE995\n" +
                    "\uE996\n" +
                    "\uE997\n" +
                    "\uE666\n" +
                    "\uE67C").toString().split("\n")
            for (i in 0 until icons.size) {
                val length = this.length()
                put(JSONObject().apply {
                    put("icon", icons[i])
                    put("type", "")
                    put("label", length)
                })
            }
            while (this.length() < 32) {
                val length = this.length()
                put(JSONObject().apply {
                    put("icon", "test")
                    put("type", "")
                    put("label", length)
                })
            }
        }
    }
}