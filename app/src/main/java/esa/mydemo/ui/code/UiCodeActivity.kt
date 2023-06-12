package esa.mydemo.ui.code

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bin.david.form.data.column.Column
import com.bin.david.form.data.style.FontStyle
import com.bin.david.form.data.table.TableData
import esa.mydemo.base.AppBaseActivity
import esa.mydemo.data.code.CodeDao
import esa.mydemo.databinding.ActivityUiCodeBinding
import esa.mylibrary.info.DeviceInfo


class UiCodeActivity : AppBaseActivity() {


    private lateinit var binding: ActivityUiCodeBinding

    private lateinit var viewModel: UiCodeActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(esa.mydemo.R.layout.activity_ui_data_demo)

        //状态栏透明，不占位
        setStausBarTranslucent()

        binding = ActivityUiCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.root.setPadding(0, DeviceInfo.statubarHeight, 0, 0)


        viewModel = ViewModelProvider(this).get(UiCodeActivityViewModel::class.java)
        viewModel.setContext(binding.root.context)

        init()
    }

    override fun onResume() {
        super.onResume()
//        binding.myRecyclerView.showHeader()
//        binding.myRecyclerView.refresh()
    }

    override fun onDestroy() {
        super.onDestroy()
        //页面关闭后，不再监听
//        binding.myRecyclerView.listener = null
    }


    fun init() {

        viewModel.logDao = CodeDao.getInstance(binding.root.context)

        viewModel.refresh()

        var groupId: Column<String> = Column<String>("组ID", "groupId")
        var codeValue: Column<String> = Column<String>("下拉值", "codeValue")
        var codeText: Column<String> = Column<String>("下拉文本", "codeText")

        var table: TableData<SmartTableBean> =
            TableData<SmartTableBean>("表格名", viewModel.list, groupId, codeValue, codeText)
//        binding.table.setData(viewModel.list)
        binding.table.setTableData(table)
        binding.table.getConfig().setContentStyle(FontStyle(50, Color.BLUE))
        binding.table.getConfig().setContentStyle(FontStyle(20, Color.BLUE))
        binding.table.getConfig().setFixedXSequence(false)
        binding.table.getConfig().setShowXSequence(false)
        binding.table.getConfig().setShowYSequence(false)
        binding.table.getConfig().setShowTableTitle(false)

        binding.clear.setOnClickListener(View.OnClickListener {
            viewModel.clear()
            viewModel.refresh()
//            binding.table.setData(viewModel.list)
//            binding.table.notifyDataChanged()
            refresh()
        })

        binding.fab.setOnClickListener(View.OnClickListener {
            viewModel.insert()
            viewModel.refresh()
//            binding.table.setData(viewModel.list)
//            binding.table.notifyDataChanged()
            refresh()
        })
    }

    fun refresh() {
        var groupId: Column<String> = Column<String>("组ID", "groupId")
        var codeValue: Column<String> = Column<String>("下拉值", "codeValue")
        var codeText: Column<String> = Column<String>("下拉文本", "codeText")

        var table: TableData<SmartTableBean> =
            TableData<SmartTableBean>("表格名", viewModel.list, groupId, codeValue, codeText)
//        binding.table.setData(viewModel.list)
        binding.table.setTableData(table)
    }


}

