package esa.mydemo.utils

import android.content.Context
import android.content.Intent

object Token {

    //token失效
    fun loseEfficacy(context: Context) {
        // 跳转至 activity
        val intent = Intent(context, esa.mydemo.main.login.LoginActivity::class.java)
        context.startActivity(intent)
    }

}