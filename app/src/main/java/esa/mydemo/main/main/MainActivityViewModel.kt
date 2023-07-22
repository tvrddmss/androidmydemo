package esa.mydemo.main.main

import androidx.lifecycle.MutableLiveData
import esa.mydemo.base.AppBaseViewModel
import org.json.JSONObject

class MainActivityViewModel : AppBaseViewModel() {

    lateinit var jsonObject: JSONObject

    var img: MutableLiveData<String> = MutableLiveData("img")
    var title: MutableLiveData<String> = MutableLiveData("title")
    var content: MutableLiveData<String> = MutableLiveData("content")

}