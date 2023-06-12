package esa.mydemo.ui.imageview

import android.app.ActionBar
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import esa.mydemo.R
import esa.mydemo.base.AppBaseActivity
import esa.mydemo.databinding.ActivityUiImageViewBinding
import esa.mylibrary.info.DeviceInfo
import esa.mylibrary.uicomponent.MyRecyclerView
import esa.mylibrary.utils.DensityUtil
import esa.mylibrary.utils.FastClick
import esa.mylibrary.utils.MyJson
import org.json.JSONArray
import org.json.JSONObject

class UiImageViewActivity : AppBaseActivity() {

    private lateinit var binding: ActivityUiImageViewBinding
    private lateinit var imageurls: JSONArray

    private lateinit var myAdapter: UiImageViewActivity.RefreshRecycleAdapter


    private var count = 0
    private var index = 0
    private var step = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ui_image_view)

        binding = ActivityUiImageViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

//
//        Glide.with(binding.root.context)
//            .load(imageurls[0])
//            .skipMemoryCache(true)
//            .diskCacheStrategy(DiskCacheStrategy.NONE)
//            .error(esa.mylibrary.R.drawable.base_loading_img)
//            .into(binding.imagev1)
//        binding.imagev1.setOnClickListener {
//            try {
//                val intent = Intent(binding.root.context, UiImageViewDetailActivity::class.java)
//                intent.putExtra("imageurls", imageurls.joinToString("^"))
//                intent.putExtra("index", 0)
//                var sharedElement = it
//                val compat: ActivityOptionsCompat? = this.let {
//                    ActivityOptionsCompat.makeSceneTransitionAnimation(
//                        it,
//                        sharedElement,
//                        "detailview"
//                    )
//                }
//                ActivityCompat.startActivity(binding.root.context, intent, compat?.toBundle())
//            } catch (ex: Exception) {
//                MyLog.e(ex.message)
//            }
//        }

        imageurls = JSONArray().apply {
            JSONObject().apply {
                put("title", "1111")
                put(
                    "img",
                    "https://www.isanxia.com/zb_users/upload/2021/01/202101231611384413161374.jpg"
                )
                put(this)
            }
            JSONObject().apply {
                put("title", "1111")
                put(
                    "img",
                    "https://img1.baidu.com/it/u=2134924737,3616458382&fm=253&fmt=auto&app=138&f=JPG?w=889&h=500"
                )
                put(this)
            }
        }


        myAdapter = UiImageViewActivity.RefreshRecycleAdapter(JSONArray())

        binding.myRecyclerView.apply {

            canLoadMore = false
            canRefresh = true


            adapter = myAdapter
            listener = object : MyRecyclerView.IOnScrollListener {
                override fun onRefresh() {
                    count = 0
                    index = 0

                    count = 2
                    myAdapter.list = imageurls
                    index = myAdapter.list.length() / step + 1
                    myAdapter!!.notifyDataSetChanged()
                }

                override fun onLoadMore() {

                    count = 3
                    myAdapter.list = MyJson.merge(myAdapter.list, imageurls)
                    myAdapter!!.notifyDataSetChanged()
                }

                override fun isAllData(): Boolean {
                    return myAdapter!!.itemCount >= count
                }
            }

        }

        binding.myRecyclerView.refresh()


        //item点击监听
        myAdapter.onViewClickListener = View.OnClickListener {

            //加载数据时不跳转，避免多次点击
            if (!FastClick.isFastClick() && !binding.myRecyclerView.isIsloading()) {

                val intent = Intent(binding.root.context, UiImageViewDetailActivity::class.java)
                intent.putExtra("imageurls", imageurls.toString())
                intent.putExtra("index", (it.tag as Int))
                var sharedElement = ((it as ViewGroup).get(0) as ViewGroup).get(0) as ImageView
                val compat: ActivityOptionsCompat? = this.let {
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        it,
                        sharedElement,
                        "detailview"
                    )
                }
                ActivityCompat.startActivity(binding.root.context, intent, compat?.toBundle())
            }
        }
    }


    class RefreshRecycleAdapter(var list: JSONArray) :
        RecyclerView.Adapter<UiImageViewActivity.RefreshRecycleAdapter.ViewHolder>() {

        //每行有多少
        private val columsCount = 3
        public var onViewClickListener: View.OnClickListener? = null

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
                layoutParamsItemView.height = ActionBar.LayoutParams.WRAP_CONTENT
                holder.itemView.layoutParams = layoutParamsItemView

                var layoutParamsImage = holder.image.layoutParams
                layoutParamsImage.width =
                    layoutParamsItemView.width - (2 * DensityUtil.dip2px(
                        holder.itemView.context,
                        10
                    ))
                layoutParamsImage.height = layoutParamsImage.width

                holder.text.text = list.getJSONObject(position).getString("title")
                val option = RequestOptions.placeholderOf(R.drawable.baseline_photo_24)
                    .error(R.drawable.baseline_photo_24)
//                    .circleCrop()
                Glide.with(holder.itemView)
                    .load(list.getJSONObject(position).getString("img"))
                    .apply(option)
                    .into(holder.image)

                holder.itemView.setOnClickListener(onViewClickListener)

            } catch (ex: Exception) {
            }
        }

        override fun getItemCount(): Int {
            return list.length()
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