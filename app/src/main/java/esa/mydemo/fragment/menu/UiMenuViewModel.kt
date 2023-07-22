package esa.mydemo.fragment.menu

import esa.mydemo.base.AppBaseViewModel
import org.json.JSONArray
import org.json.JSONObject

class UiMenuViewModel : AppBaseViewModel() {
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

            put(JSONObject().apply {
                put("icon", "\uE998")
                put("type", "activity")
                put("class", "esa.mydemo.ui.data.UiDataDemoActivity")
                put("label", "sqlite")
            })

            put(JSONObject().apply {
                put("icon", "\uE999")
                put("type", "activity")
                put("class", "esa.mydemo.ui.code.UiCodeActivity")
                put("label", "code-sqlite")
            })

            put(JSONObject().apply {
                put("icon", "\uE99A")
                put("type", "activity")
                put("class", "esa.mydemo.ui.tv.UiTvActivity")
                put("label", "tv")
            })

            put(JSONObject().apply {
                put("icon", "\uE99B")
                put("type", "activity")
                put("par", JSONObject().put("type","image"))
                put("class", "esa.mydemo.ui.camera.UiCameraActivity")
                put("label", "camera-image")
            })
            put(JSONObject().apply {
                put("icon", "\uE99C")
                put("type", "activity")
                put("par", JSONObject().put("type","video"))
                put("class", "esa.mydemo.ui.camera.UiCameraActivity")
                put("label", "camera-video")
            })

            put(JSONObject().apply {
                put("icon", "\uE98B")
                put("type", "activity")
                put("class", "esa.mydemo.ui.imageview.UiImageViewActivity")
                put("label", "imageview---")
            })

            put(JSONObject().apply {
                put("icon", "\uE98C")
                put("type", "activity")
                put("class", "esa.mydemo.ui.imageview.UiImageViewDetailNewActivity")
                put("label", "MotionLayout")
            })

            put(JSONObject().apply {
                put("icon", "\uE98D")
                put("type", "activity")
                put("class", "esa.mydemo.ui.imageview.card.UiImageViewDetailCardActivity")
                put("label", "CardView")
            })
            put(JSONObject().apply {
                put("icon", "\uE98E")
                put("type", "activity")
                put("class", "esa.mydemo.ui.photo.UiPhotoViewActivity")
                put("label", "照片文件查看")
            })
            put(JSONObject().apply {
                put("icon", "\uE98F")
                put("type", "activity")
                put("class", "esa.mydemo.ui.audio.UiAudioActivity")
                put("label", "音乐")
            })
            put(JSONObject().apply {
                put("icon", "\uE990")
                put("type", "activity")
                put("class", "esa.mydemo.ui.blog_articles.UiListActivity")
                put("label", "博客")
            })
            var icons = (
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