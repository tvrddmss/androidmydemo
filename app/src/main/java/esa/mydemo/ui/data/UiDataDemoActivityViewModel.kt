package esa.mydemo.ui.data

import esa.mydemo.base.BaseViewModel
import esa.mydemo.data.StudentBean
import esa.mydemo.data.StudentDao
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UiDataDemoActivityViewModel : BaseViewModel() {

    lateinit var logDao: StudentDao
    lateinit var myAdapter: UiDataDemoActivity.RefreshRecycleAdapter

    private var count = 0

    private val step = 3
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

    }

    fun refresh() {
        count = 0
        index = 0

        myAdapter.list = logDao.select(0, step)
        count = logDao.count
        index = myAdapter.list.size / step + 1;
        myAdapter!!.notifyDataSetChanged()
    }

    fun loadMore() {

        myAdapter.list = myAdapter.list + logDao.select(myAdapter.list.size, step)
        count = logDao.count
        index = myAdapter.list.size / step + 1;
        myAdapter!!.notifyDataSetChanged()

    }

    fun isAllData(): Boolean {
        return myAdapter!!.itemCount >= count
    }


    fun insert() {
        var studentBean: StudentBean = StudentBean();
        studentBean.name = "小明";
        studentBean.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a"))
        studentBean.timestamp = Instant.now().epochSecond
        logDao.insert(studentBean)

        refresh()
    }


    fun clear() {
        logDao.clear()

        refresh()
    }

    fun delete(id: Int) {
        logDao.delete(id)

        refresh()
    }
}