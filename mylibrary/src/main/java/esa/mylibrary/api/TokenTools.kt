package esa.mylibrary.api

import esa.mylibrary.config.Config

object TokenTools {

    fun AddFunctionToken(): String? {
        var s = ""
        try {
            for (key in Config.api.functiontoken.keys) {
                s += "&" + key + "=" + Config.api.functiontoken[key].toString()
            }
        } catch (ex: Exception) {
        }
        return s
    }
}