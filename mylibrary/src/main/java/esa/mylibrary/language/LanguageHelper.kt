package esa.mylibrary.language;

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import java.util.Locale

object LanguageHelper {


    fun forceLocale(application: Application, context:Context, locale: Locale) {
        val conf = context.resources.configuration
        updateConfiguration(conf, locale)
        context.resources.updateConfiguration(conf, context.resources.displayMetrics)
        val systemConf = Resources.getSystem().configuration
        updateConfiguration(systemConf, locale)
        Resources.getSystem().updateConfiguration(conf, context.resources.displayMetrics)
        Locale.setDefault(locale)
        application.onConfigurationChanged(systemConf)

    }

    private fun updateConfiguration(conf: Configuration, locale: Locale) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            conf.setLocale(locale)
        } else {
            conf.locale = locale
        }
    }

}