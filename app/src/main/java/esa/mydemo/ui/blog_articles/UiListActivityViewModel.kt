package esa.mydemo.ui.blog_articles

import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import esa.mydemo.base.AppBaseViewModel
import esa.mydemo.dal.spring.BlogArticelsService
import esa.mydemo.dal.spring.Data
import esa.mylibrary.apiv2.RetrofitUtil
import esa.mylibrary.config.Config
import esa.mylibrary.utils.log.MyLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class UiListActivityViewModel : AppBaseViewModel() {

    lateinit var myAdapter: UiListActivity.RefreshRecycleAdapter

    private var count = 0
    private val step = 3
    private var index = 0

    //伴生类-用于单例
    companion object {
        private lateinit var instance: UiListActivityViewModel
        fun getInstance(): UiListActivityViewModel {
            return instance
        }

        fun setInstance(instance: UiListActivityViewModel) {
            this.instance = instance
        }
    }

    fun refresh() {
        count = 0
        index = 0
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                var data = call()
                MyLog.d("数据查询结束")
                if (isActive) {
                    analysisResult(true, data)
                    MyLog.d("数据拼接结束")
                }
            }
            withContext(Dispatchers.Main) {
                myAdapter!!.notifyDataSetChanged()
                MyLog.d("UI刷新结束")
            }

        }

    }

    fun loadMore() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                var data = call()
                MyLog.d("数据查询结束")
                if (isActive) {
                    analysisResult(false, data)
                    MyLog.d("数据拼接结束")
                }
            }
            withContext(Dispatchers.Main) {
                myAdapter!!.notifyDataSetChanged()
            }
        }
    }

    fun isAllData(): Boolean {
        return myAdapter!!.itemCount >= count
    }

    //拼接查询参数
    private fun getParMap(): HashMap<String, String> {
        return HashMap<String, String>().apply {
            put("index", index.toString())
            put("size", step.toString())
            put("functiontoken", Config.api.functiontoken.get("functiontoken").toString())
        }
    }

    //执行查询
    private fun call(): Data {
        return RetrofitUtil.retrofit.create(BlogArticelsService::class.java)
            .getList(getParMap(), Config.api.loginToken)
            .execute()
            .body()!!
    }

    //解析查询结果
    private fun analysisResult(isRefresh: Boolean, data: Data) {
        if (data.getCode() == 0) {
            var result = Gson().toJsonTree(data.getData()).asJsonObject
            count = result.get("total").asInt
            if (isRefresh) {
                myAdapter.list = result.getAsJsonArray("records")
            } else {
                myAdapter.list.addAll(result.getAsJsonArray("records"))
            }
            index = myAdapter.list.size() / step + 1
        } else {
            if (data != null) {
                throw Exception(data?.getData().toString())
            } else {
                throw Exception("发生错误")
            }
        }

    }


}