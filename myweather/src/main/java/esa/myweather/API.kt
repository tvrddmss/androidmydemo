package esa.myweather

import android.content.Context
import android.graphics.Typeface
import android.location.Location
import android.os.Handler
import android.util.Log
import org.json.JSONObject
import java.io.RandomAccessFile
import java.math.RoundingMode
import java.text.DecimalFormat


object API {

    private val TAG = "esa.myweather.API";
    private val get = Get()

    private val url = "https://devapi.qweather.com/v7/grid-weather/now"
    private val key = "7aa9d4a115ce4df09dcc9bd5f83f1685"
    fun getWeatherByLocation(location: Location, handler: Handler) {

        var par = JSONObject()

        par.put(
            "location",
            getFloatNoMoreThanTwoDigits(location.longitude) + "," + getFloatNoMoreThanTwoDigits(
                location.latitude
            )
        )
        par.put("key", key)
//        par.put("lang", "zh-hans、zh")
//        par.put("unit", "m")

        val thread = Thread { get.doSync(url, par, handler) }
        thread.start()

    }

    fun getFloatNoMoreThanTwoDigits(number: Double): String {
        val format = DecimalFormat("#.##")
//舍弃规则，RoundingMode.FLOOR表示直接舍弃。
        format.roundingMode = RoundingMode.FLOOR
        return format.format(number)
    }

    const val ROOT = "weather/"
    const val FONTAWESOME = ROOT + "qweather-icons.ttf"
    fun getTypeface(context: Context): Typeface? {
        return Typeface.createFromAsset(context.assets, FONTAWESOME)
    }


    /**
     * unicode转中文
     * @param unicode
     * @return
     */
    private fun unicodeToString(unicode: String): String? {
        val string = StringBuffer()
        val hex = unicode.split("\\\\u".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        for (i in 1 until hex.size) {
            // 转换
            val data = hex[i].toInt(16)
            // 拼接成string
            string.append(data.toChar())
        }
        return string.toString()
    }

    /**
     * 中文转unicode
     *
     * @param string
     * @return
     */
    fun stringToUnicode(string: String): String? {
        val unicode = StringBuffer()
        for (i in 0 until string.length) {
            // 取出每一个字符
            val c = string[i]
            // 转换为unicode
            //"\\u只是代号，请根据具体所需添加相应的符号"
            unicode.append("\\u" + Integer.toHexString(c.code))
        }
        Log.d(TAG, "stringToUnicode: $unicode")
        return unicode.toString()
    }


    fun anan(path: RandomAccessFile): String {
        var ttfCodeParse = TTFCodeParse()
        return ttfCodeParse.parseInner(path)
    }

    fun getIcon(icon: String): String{
        var unicode: String = ""
        when (icon) {
            "100" -> {unicode = "\uf101"}
            "101" -> {unicode = "\uf102"}
            "102" -> {unicode = "\uf103"}
            "103" -> {unicode = "\uf104"}
            "104" -> {unicode = "\uf105"}
            "150" -> {unicode = "\uf106"}
            "151" -> {unicode = "\uf107"}
            "152" -> {unicode = "\uf108"}
            "153" -> {unicode = "\uf109"}
            "300" -> {unicode = "\uf10a"}
            "301" -> {unicode = "\uf10b"}
            "302" -> {unicode = "\uf10c"}
            "303" -> {unicode = "\uf10d"}
            "304" -> {unicode = "\uf10e"}
            "305" -> {unicode = "\uf10f"}
            "306" -> {unicode = "\uf110"}
            "307" -> {unicode = "\uf111"}
            "308" -> {unicode = "\uf112"}
            "309" -> {unicode = "\uf113"}
            "310" -> {unicode = "\uf114"}
            "311" -> {unicode = "\uf115"}
            "312" -> {unicode = "\uf116"}
            "313" -> {unicode = "\uf117"}
            "314" -> {unicode = "\uf118"}
            "315" -> {unicode = "\uf119"}
        }

        return unicode
    }
}