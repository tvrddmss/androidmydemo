package esa.mylibrary.utils

import android.content.Context

object DensityUtil {

    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    fun dip2px(context: Context, dpValue: Int): Int {
        val scale: Float = context.getResources().getDisplayMetrics().density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    fun px2dip(context: Context, pxValue: Int): Int {
        val scale: Float = context.getResources().getDisplayMetrics().density
        return (pxValue / scale + 0.5f).toInt()
    }
}