package esa.mydemo.ui.blog_articles

import androidx.lifecycle.MutableLiveData
import esa.mydemo.base.BaseViewModel
import org.json.JSONObject


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
class UiDetailActivityViewModel : BaseViewModel() {

    lateinit var jsonObject: JSONObject

    var img: MutableLiveData<String> = MutableLiveData("img")
    var title: MutableLiveData<String> = MutableLiveData("title")
    var content: MutableLiveData<String> = MutableLiveData("content")


    /**
     * @description 销毁程序
     * @param null
     * @return null
     * @author Administrator
     * @time 2023/04/20 17:02
     */
    override fun onCleared() {
        super.onCleared()
    }


}


