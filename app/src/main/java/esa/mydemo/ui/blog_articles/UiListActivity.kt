package esa.mydemo.ui.blog_articles

import android.app.ActionBar.LayoutParams
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.JsonArray
import esa.mydemo.R
import esa.mydemo.base.AppBaseActivity
import esa.mydemo.databinding.ActivityUiListBinding
import esa.mylibrary.info.DeviceInfo
import esa.mylibrary.uicomponent.MyRecyclerView
import esa.mylibrary.utils.DensityUtil
import esa.mylibrary.utils.FastClick
import esa.mylibrary.utils.log.MyLog


class UiListActivity : AppBaseActivity() {
    private lateinit var binding: ActivityUiListBinding
    private lateinit var viewModel: UiListActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        setContentView(R.layout.activity_ui_list)
        binding = ActivityUiListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(UiListActivityViewModel::class.java)
        UiListActivityViewModel.setInstance(viewModel)
        viewModel.setContext(applicationContext)


        init()
    }

    override fun onResume() {
        super.onResume()
//        binding.myRecyclerView.showHeader()
//        binding.myRecyclerView.refresh()
    }

    fun init() {
//        binding.lltitle.text = "ceshi"
        MyLog.d(this.javaClass.toString())

        binding.myRecyclerView.canLoadMore = true
        binding.myRecyclerView.canRefresh = true

        viewModel.myAdapter = RefreshRecycleAdapter(JsonArray())

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

            //加载数据时不跳转，避免多次点击
            if (!FastClick.isFastClick() && !binding.myRecyclerView.isIsloading()) {
                // 跳转至 activity
                val intent = Intent(binding.root.context, UiDetailActivity::class.java)
                intent.putExtra(
                    "data",
                    (it.tag as Int)
                )
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
        }

        binding.myRecyclerView.refresh()

    }

    override fun onDestroy() {
        super.onDestroy()
        //页面关闭后，不再监听
        binding.myRecyclerView.listener = null

    }

    /**
     * @description 列表适配器
     * @author Administrator
     * @time 2023/04/20 16:29
     */
    class RefreshRecycleAdapter(var list: com.google.gson.JsonArray) :
        RecyclerView.Adapter<RefreshRecycleAdapter.ViewHolder>() {

        //每行有多少
        val columsCount = 3
        var onViewClickListener: View.OnClickListener? = null

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ViewHolder {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(esa.mydemo.R.layout.activity_ui_list_item, parent, false)
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
                layoutParamsItemView.height = LayoutParams.WRAP_CONTENT
                holder.itemView.layoutParams = layoutParamsItemView

                var layoutParamsImage = holder.image.layoutParams
                layoutParamsImage.width =
                    layoutParamsItemView.width - (2 * DensityUtil.dip2px(
                        holder.itemView.context,
                        10
                    ))
                layoutParamsImage.height = layoutParamsImage.width

                holder.text.text = list.get(position).asJsonObject.get("title").asString
                val option = RequestOptions.placeholderOf(R.drawable.baseline_photo_24)
                    .error(R.drawable.baseline_photo_24)
                    .circleCrop()
                Glide.with(holder.itemView)
                    .load(list.get(position).asJsonObject.get("img").asString)
                    .apply(option)
                    .into(holder.image)

                holder.itemView.setOnClickListener(onViewClickListener)

            } catch (ex: Exception) {
            }
        }

        override fun getItemCount(): Int {
            return list.size()
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val text: TextView
            val image: ImageView

            init {
                image = itemView.findViewById(R.id.imItem)
                text = itemView.findViewById(R.id.tvItem)
            }
        }
    }

}