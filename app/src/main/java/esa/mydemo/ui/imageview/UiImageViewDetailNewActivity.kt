package esa.mydemo.ui.imageview

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.helper.widget.Carousel
import com.bumptech.glide.Glide
import esa.mydemo.R
import esa.mydemo.base.AppBaseActivity
import esa.mydemo.databinding.ActivityUiImageViewDetailNewBinding

class UiImageViewDetailNewActivity : AppBaseActivity() {

    private lateinit var binding: ActivityUiImageViewDetailNewBinding

    private var imageurls = mutableListOf<String>(
        "https://img1.baidu.com/it/u=4009907374,3946217754&fm=253&fmt=auto?w=130&h=170",
        "https://img0.baidu.com/it/u=3755874652,3813878829&fm=253&fmt=auto?w=130&h=170",
        "https://img1.baidu.com/it/u=3608048080,3001838149&fm=253&fmt=auto?w=130&h=170",
        "https://img1.baidu.com/it/u=1580512397,3098559695&fm=253&fmt=auto?w=130&h=170",
        "https://img1.baidu.com/it/u=2498840130,1714231518&fm=253&fmt=auto?w=130&h=170",
        "https://img2.baidu.com/it/u=216361273,198906387&fm=253&fmt=auto?w=130&h=170"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ui_image_view_detail_new)

        binding = ActivityUiImageViewDetailNewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        binding.carousel.setAdapter(object : Carousel.Adapter {
            override fun count(): Int {
                // need to return the number of items we have in the carousel
                return 4
            }

            override fun populate(view: View, index: Int) {
                // need to implement this to populate the view at the given index

                var aa = index
                var ss = aa.toString() + aa.toString()

                (view as ImageView).apply {

                    Glide.with(binding.root.context)
                        .load(imageurls[index])
                        .error(esa.mylibrary.R.drawable.base_loading_img)
                        .into(this)
//                    this.setImageDrawable(getDrawable(R.drawable.baseline_photo_24))


                }

            }

            override fun onNewItem(index: Int) {
                // called when an item is set

            }
        })
    }
}