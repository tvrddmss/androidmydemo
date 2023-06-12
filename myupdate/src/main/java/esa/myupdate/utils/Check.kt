package esa.myupdate.utils

import android.app.Activity
import com.hailong.appupdate.AppUpdateManager

object CheckUpdate {

    fun checkUpdate(activity: Activity, apkUrl: String, array: Array<String>, isForce: Boolean,versionName: String,title: String) {
        var builder: AppUpdateManager.Builder = AppUpdateManager.Builder(activity)
        builder.apkUrl(apkUrl)
            .updateContent(array)
            .updateForce(isForce)//是否必须更新
            .newVerName(versionName)//版本
            .title(title)
//            .topResId(R.drawable.updateversion)
            .build()
    }
}