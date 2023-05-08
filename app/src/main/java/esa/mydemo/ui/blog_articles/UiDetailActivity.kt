package esa.mydemo.ui.blog_articles

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import esa.mydemo.MyApplication
import esa.mydemo.R
import esa.mydemo.base.AppBaseActivity
import esa.mydemo.dal.spring.BlogArticles
import esa.mydemo.databinding.ActivityUiDetailBlogArticlesBinding
import esa.mylibrary.common.CallBack
import esa.mylibrary.utils.MyImageUtil
import org.json.JSONObject

class UiDetailActivity : AppBaseActivity() {

    private lateinit var binding: ActivityUiDetailBlogArticlesBinding
    private lateinit var viewModel: UiDetailActivityViewModel
    private val glideOption = RequestOptions.placeholderOf(R.drawable.baseline_photo_24)
        .error(R.drawable.baseline_photo_24)
        .circleCrop()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_ui_detail_blog_articles)
        binding = ActivityUiDetailBlogArticlesBinding.inflate(layoutInflater)
        setContentView(binding.root)



        viewModel = ViewModelProvider(this).get(UiDetailActivityViewModel::class.java)
        viewModel.setContext(binding.root.context)

        binding.viewModel = viewModel


    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        init()
    }

    fun init() {

        //接收参数
        var index = intent.getIntExtra("data", 0)

        viewModel.jsonObject =
            ((application as MyApplication).activities.get((application as MyApplication).activities.size - 2) as UiListActivity).viewModel.myAdapter.list.getJSONObject(
                index
            )

//        viewModel.jsonObject = MyJson.parse(intent.getStringExtra("data")) as JSONObject
        viewModel.img.value = (viewModel.jsonObject.getString("img"))
        viewModel.title.value = (viewModel.jsonObject.getString("title"))
        viewModel.content.value = (viewModel.jsonObject.getString("content"))



        Glide.with(binding.root)
            .load(viewModel.img.value)
            .apply(glideOption)
            .into(binding.image)

        //图片
        binding.image.setOnClickListener(View.OnClickListener {
            var intent = Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, 2)
        })

//        viewModel.title.postValue(viewModel.jsonObject.getString("title"))
//        viewModel.context.postValue(viewModel.jsonObject.getString("content"))

//        binding.textInputLayoutTitle.editText?.apply {
//            setText(viewModel.jsonObject.getString("title"))
//        }
//        binding.textInputLayoutContent.editText?.apply {
//            setText(viewModel.jsonObject.getString("content"))
//        }

        //返回
        binding.imageButton.setOnClickListener(View.OnClickListener {
            finish()
            onBackPressed()
        })

        //保存
        binding.bottomTextViews.setOnClickListener(View.OnClickListener {
//            var bitmap = binding.image.drawable.toBitmap()
//            val resized = Bitmap.createScaledBitmap(
//                bitmap, bitmap.width, bitmap.height, true
//            )
//            val stream = ByteArrayOutputStream()
//            resized.compress(Bitmap.CompressFormat.PNG, 100, stream)
//            val byteArray: ByteArray = stream.toByteArray()
//
//            viewModel.jsonObject.put(
//                "img",
//                "data:image/png;base64," + Base64.encodeToString(byteArray, Base64.DEFAULT)
//                    .toString()
//            )
            viewModel.jsonObject.put("img", viewModel.img.value)
            viewModel.jsonObject.put("title", viewModel.title.value)
            viewModel.jsonObject.put("content", viewModel.content.value)
            BlogArticles.update(viewModel.jsonObject as JSONObject,
                object : CallBack<Int>() {
                    override fun success(o: Int) {
                        showMessage("保存成功")
                    }

                    override fun error(message: String) {
                        showExceptionMessage("数据刷新失败：$message")
                    }
                })
        })

    }

    override fun onResume() {
        super.onResume()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2) {
            // 从相册返回的数据
//            Log.e(this.getClass().getName(), "Result:" + data.toString());
            if (data != null) {
                // 得到图片的全路径
                var uri = data.getData()
                var bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);


//                val stream = ByteArrayOutputStream()
//                var quality = 100;
//                val picMaxSize = 100000;
//                if (bitmap.byteCount > picMaxSize) {
//                    quality = quality * (picMaxSize / bitmap.byteCount)
//                }
//                bitmap.compress(Bitmap.CompressFormat.PNG, quality, stream)
//                val byteArray: ByteArray = stream.toByteArray()
//
//                viewModel.img.value = (
//                        "data:image/png;base64," + Base64.encodeToString(byteArray, Base64.DEFAULT)
//                            .toString()
//                        )
//
                bitmap = MyImageUtil.getBitmapBySize(bitmap, 1000)
                viewModel.img.value = MyImageUtil.getBase64FromBitmap(bitmap, 10000)
                Glide.with(binding.root).clear(binding.image);
                Glide.with(binding.root)
                    .load(viewModel.img.value)
                    .apply(glideOption)
                    .into(binding.image)
            }
        }
    }

}