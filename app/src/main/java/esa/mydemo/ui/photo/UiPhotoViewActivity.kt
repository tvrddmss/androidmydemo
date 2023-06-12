package esa.mydemo.ui.photo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import esa.mydemo.R
import esa.mydemo.base.AppBaseActivity
import esa.mydemo.databinding.ActivityUiPhotoViewBinding
import esa.mylibrary.info.DeviceInfo
import esa.mylibrary.utils.MyImageUtil

class UiPhotoViewActivity : AppBaseActivity() {

    private val OPENPICCODE = 1

    private val TAG = "UiPhotoViewActivity";
    private lateinit var binding: ActivityUiPhotoViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ui_photo_view)

        //状态栏透明，不占位
        setStausBarTranslucent()

        binding = ActivityUiPhotoViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.setPadding(0, DeviceInfo.statubarHeight, 0, 0)
        init()
    }

    private fun init() {
        binding.btnImageInfo.apply {
            setOnClickListener({
                openImageUtils()
            })
        }
    }


    private fun openImageUtils() {
        val intent: Intent
        if (Build.VERSION.SDK_INT < 19) {
            intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
        } else {
            intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
        }
        startActivityForResult(intent, OPENPICCODE)
    }

    @SuppressLint("NewApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            OPENPICCODE
            -> {
                data?.apply {
                    val uri = this.data!!
                    val path = MyImageUtil.getPathByUri(contentResolver, uri)
                    var json = MyImageUtil.getExif(applicationContext, path)
                    binding.tvMessage.text = json.toString()
                }
            }
        }
    }
}