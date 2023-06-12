package esa.mydemo.ui.data

import android.app.ActionBar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import esa.mydemo.R
import esa.mydemo.base.AppBaseActivity
import esa.mydemo.data.StudentBean
import esa.mydemo.data.StudentDao
import esa.mydemo.databinding.ActivityUiDataDemoBinding
import esa.mylibrary.info.DeviceInfo
import esa.mylibrary.uicomponent.MyRecyclerView
import esa.mylibrary.utils.DensityUtil
import esa.mylibrary.utils.FastClick

class UiDataDemoActivity : AppBaseActivity() {


    private lateinit var binding: ActivityUiDataDemoBinding

    private lateinit var viewModel: UiDataDemoActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ui_data_demo)

        //状态栏透明，不占位
        setStausBarTranslucent()

        binding = ActivityUiDataDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.root.setPadding(0, DeviceInfo.statubarHeight, 0, 0)


        viewModel = ViewModelProvider(this).get(UiDataDemoActivityViewModel::class.java)
        viewModel.setContext(binding.root.context)

        init()
    }

    override fun onResume() {
        super.onResume()
//        binding.myRecyclerView.showHeader()
        binding.myRecyclerView.refresh()
    }

    override fun onDestroy() {
        super.onDestroy()
        //页面关闭后，不再监听
        binding.myRecyclerView.listener = null
    }


    fun init() {
//        binding.lltitle.text = "ceshi"

        binding.myRecyclerView.canLoadMore = true
        binding.myRecyclerView.canRefresh = true

        viewModel.logDao = StudentDao.getInstance(binding.root.context)
        viewModel.myAdapter = UiDataDemoActivity.RefreshRecycleAdapter(ArrayList<StudentBean>())

        binding.myRecyclerView.adapter = viewModel.myAdapter
        binding.myRecyclerView.listener = object : MyRecyclerView.IOnScrollListener {
            override fun onRefresh() {
                viewModel.refresh()
            }

            override fun onLoadMore() {
                viewModel.loadMore()
            }

            override fun isAllData(): Boolean {
                return viewModel.isAllData()
            }
        }

        //间隔线
//        binding.myRecyclerView.addItemDecoration(DividerItemDecoration(this, 1))

        //item点击监听
        viewModel.myAdapter.onViewClickListener = View.OnClickListener {

            val index = it.tag as Int
            //加载数据时不跳转，避免多次点击
            if (!FastClick.isFastClick() && !binding.myRecyclerView.isIsloading) {
                viewModel.myAdapter.list[index].id?.let { it1 ->
                    viewModel.delete(it1)
                }
            }
        }

        binding.fab.setOnClickListener(View.OnClickListener {
            viewModel.insert()
        })

        binding.clear.setOnClickListener(View.OnClickListener {
            viewModel.clear()
        })
    }


    /**
     * @description 列表适配器
     * @author Administrator
     * @time 2023/04/20 16:29
     */
    class RefreshRecycleAdapter(var list: List<StudentBean>) :
        RecyclerView.Adapter<RefreshRecycleAdapter.ViewHolder>() {

        //每行有多少
        val columsCount = 3
        var onViewClickListener: View.OnClickListener? = null

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ViewHolder {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(esa.mydemo.R.layout.activity_ui_data_demo_item, parent, false)
            return ViewHolder(view)
        }

        override fun getItemViewType(position: Int): Int {

            return super.getItemViewType(position)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            try {
                holder.itemView.tag = (position)
                var layoutParamsItemView = holder.itemView.layoutParams
                layoutParamsItemView.width =
                    (DeviceInfo.screenWidthPixels / columsCount) - (2 * DensityUtil.dip2px(
                        holder.itemView.context,
                        5
                    ))
                layoutParamsItemView.height = ActionBar.LayoutParams.WRAP_CONTENT
                holder.itemView.layoutParams = layoutParamsItemView

                holder.text.text =
                    list[position].name + "\n" + list[position].timestamp + "\n" + list[position].date


                holder.itemView.setOnClickListener(onViewClickListener)

            } catch (ex: Exception) {
            }
        }

        override fun getItemCount(): Int {
            return list.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val text: TextView

            init {
                text = itemView.findViewById(R.id.tvItem)
            }
        }
    }

}

