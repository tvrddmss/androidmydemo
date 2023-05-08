package esa.mylibrary.utils

import android.graphics.Color


object MyColor {
//    //获取圆环上颜色
//    private fun interpCircleColor(colors: IntArray, degree: Float): Int {
//        var degree = degree
//        degree -= 90f
//        if (degree < 0) degree += 360f
//        var p = degree * (colors.size - 1) / 360
//        val i = p.toInt()
//        p -= i.toFloat()
//        val c0 = colors[i]
//        val c1 = colors[i + 1]
//        val a: Int = ave(Color.alpha(c0), Color.alpha(c1), p)
//        val r: Int = ave(Color.red(c0), Color.red(c1), p)
//        val g: Int = ave(Color.green(c0), Color.green(c1), p)
//        val b: Int = ave(Color.blue(c0), Color.blue(c1), p)
//        return Color.argb(a, r, g, b)
//    }

    //获取圆环上颜色
    fun getStepColor(step: Int, count: Int): Int {

        var p = (step.toFloat() / (count).toFloat())

        val colorCount = 256 * 3
        val colorStep = (p * colorCount).toInt() % (256 * 3)

        var r: Int = 0
        if (0 <= colorStep && colorStep < 256) {
            r = 255 - colorStep
        } else if (256 * 2 <= colorStep && colorStep < 256 * 3) {
            r = colorStep - 256 * 2
        }

        var g = 0
        if (0 <= colorStep && colorStep < 256) {
            g = colorStep
        } else if (256 <= colorStep && colorStep < 256 * 2) {
            g = 256 * 2 - 1 - colorStep
        }

        var b = 0
        if (256 <= colorStep && colorStep < 256 * 2) {
            b = colorStep - 256
        } else if (256 * 2 <= colorStep && colorStep < 256 * 3) {
            b = 256 * 3 - 1 - colorStep
        }
        return Color.rgb(r, g, b)
    }
}