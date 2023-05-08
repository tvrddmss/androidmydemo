package esa.mydemo.ui.blog_articles

import esa.mydemo.base.BaseViewModel
import esa.mydemo.dal.spring.BlogArticles
import esa.mylibrary.common.CallBack
import esa.mylibrary.utils.MyJson
import org.json.JSONObject
import java.util.*

class UiListActivityViewModel : BaseViewModel() {

    lateinit var myAdapter: UiListActivity.RefreshRecycleAdapter

    private var count = 0

    private val step = 2
    private var index = 0

    /**
     * @description 销毁程序
     * @param null
     * @return null
     * @author Administrator
     * @time 2023/04/20 17:02
     */
    override fun onCleared() {
        super.onCleared()
        //viewModel销毁时调用，可以做一些释放资源的操作
//        if (myAdapter.list!!.length() >= 1) {
//            while (myAdapter.list!!.length() >= 1) {
//                myAdapter.list!!.remove(0)
//            }
//        }
//        myAdapter.notifyDataSetChanged()

    }

    fun refresh() {
        count = 0
        index = 0
        BlogArticles.getList(index, step,
            object : CallBack<JSONObject>() {
                override fun success(o: JSONObject) {
                    count = o.getString("total").toInt()
                    o.getString("current")
                    myAdapter.list = o.getJSONArray("records")
                    index = myAdapter.list.length() / step + 1;
                    myAdapter!!.notifyDataSetChanged()
                }

                override fun error(message: String) {
                    showExceptionMessage("数据刷新失败：$message")
                }
            })

    }

    fun loadMore() {

        BlogArticles.getList(index, step,
            object : CallBack<JSONObject>() {
                override fun success(o: JSONObject) {
                    try {
                        count = o.getString("total").toInt();
//                        myAdapter.list = MyJson.merge(myAdapter.list, o.getJSONArray("rows"));
                        myAdapter.list = MyJson.merge(myAdapter.list, o.getJSONArray("records"));
                        index = myAdapter.list.length() / step + 1;
                        myAdapter!!.notifyDataSetChanged()
                    } catch (ex: Exception) {
                        showExceptionMessage(ex.message)
                    }
                }

                override fun error(message: String) {
                    showExceptionMessage("数据加载更多失败：$message")
                }
            })

//        Handler().postDelayed({
//            val jsonObject = JSONObject()
//            jsonObject.put("test", myAdapter!!.itemCount)
//            myAdapter!!.list.put(jsonObject)
//            MyLog.d("list:" + jsonObject.toString())
//            myAdapter!!.notifyDataSetChanged()
//        }, 3000)
    }

    fun isAllData(): Boolean {
        return myAdapter!!.itemCount >= count
    }


}