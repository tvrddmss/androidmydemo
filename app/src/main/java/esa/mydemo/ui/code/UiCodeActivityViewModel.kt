package esa.mydemo.ui.code

import esa.mydemo.base.AppBaseViewModel
import esa.mydemo.data.code.CodeBean
import esa.mydemo.data.code.CodeDao

class UiCodeActivityViewModel : AppBaseViewModel() {

    lateinit var logDao: CodeDao

    var list: MutableList<SmartTableBean> = ArrayList<SmartTableBean>()

    fun refresh() {
        var list_code = logDao.selectAll()
        list.clear()
        for (model in list_code) {
            list.add(SmartTableBean(model.groupId, model.codeValue, model.codeText))
        }
    }

    fun clear() {
        logDao.clear()
    }

    fun insert() {

        var code = list.count().toString()
        while (code.length < 4) {
            code = "0" + code
        }


        logDao.insert(
            CodeBean.Builder().setGroupId("0001").setCodeValue("0001$code")
                .setCodeText("测试")
                .build()
        )
    }

}