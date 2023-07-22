package esa.mydemo.ui.blog_articles

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import esa.mydemo.base.AppBaseViewModel
import esa.mydemo.dal.spring.BlogArticelsService
import esa.mylibrary.apiv2.RetrofitUtil
import esa.mylibrary.config.Config
import esa.mylibrary.utils.log.MyLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 *
 * @ProjectName: mydemo
 * @Package: esa.mydemo.ui.blog_articles
 * @ClassName: UiDetailActivityViewModel
 * @Description: java类作用描述
 * @Author: Administrator
 * @CreateDate: 2023/04/23 10:15
 * @UpdateUser: Administrator
 * @UpdateDate: 2023/04/23 10:15
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
class UiDetailActivityViewModel : AppBaseViewModel() {

    lateinit var jsonObject: JsonObject

    var img: MutableLiveData<String> = MutableLiveData("img")
    var title: MutableLiveData<String> = MutableLiveData("title")
    var content: MutableLiveData<String> = MutableLiveData("content")

    fun update() {

        jsonObject.addProperty("img", img.value)
        jsonObject.addProperty("title", title.value)
        jsonObject.addProperty("content", content.value)

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                var data = RetrofitUtil.retrofit.create(BlogArticelsService::class.java)
                    .update(jsonObject, Config.api.loginToken)
                    .execute()
                    .body()!!
                MyLog.d("数据更新结束")
                if (isActive) {
                    if (data.getCode() == 0) {
                        showMessage("更新成功")
                    } else {
                        throw Exception(data.getData().toString())
                    }
                }
            }
            withContext(Dispatchers.Main) {
                MyLog.d("UI刷新结束")
            }

        }
    }
}


