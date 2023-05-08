package esa.mylibrary.utils

object FastClick {

    private const val MIN_DELAY_TIME = 1000 // 两次点击间隔不能少于1000ms

    private var lastClickTime: Long = 0

    fun isFastClick(): Boolean {
        var flag = true
        val currentClickTime = System.currentTimeMillis()
        if (currentClickTime - lastClickTime > MIN_DELAY_TIME.toLong()) {
            flag = false
        }
        lastClickTime = currentClickTime
        return flag
    }
}