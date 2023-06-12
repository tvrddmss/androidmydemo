package esa.mydemo.ui.imageview

import android.graphics.Color
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import esa.mydemo.R
import esa.mydemo.databinding.ActivityUiImageViewDetailBinding
import esa.mylibrary.utils.MyJson
import esa.mylibrary.utils.log.MyLog
import org.json.JSONArray
import kotlin.math.sqrt

class UiImageViewDetailActivity : AppCompatActivity(), View.OnTouchListener {

    private lateinit var binding: ActivityUiImageViewDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ui_image_view_detail)

        binding = ActivityUiImageViewDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private var imageurls: JSONArray = JSONArray()
    private var index = 0;
    private fun init() {

        imageurls = MyJson.parse(intent.extras?.getString("imageurls"))!! as JSONArray
        index = intent.extras?.getInt("index", 0)!!

        refreshImage()

        val gradientDrawable1 = GradientDrawable() // 形状-圆角矩形
        gradientDrawable1.shape = GradientDrawable.RECTANGLE // 圆角
        gradientDrawable1.cornerRadius = 60f
        gradientDrawable1.setStroke(
            1,
            binding.root.context.getColor(esa.mylibrary.R.color.black)
        )
        gradientDrawable1.setColor(Color.parseColor("#88000000"))
        binding.txtClose.apply {
            background = gradientDrawable1
            setOnClickListener {
                onBackPressed()
            }
        }
        binding.txtPre.apply {
            background = gradientDrawable1
            setOnClickListener {
                if (index > 0) {
                    index--
                    refreshImage()
                }
            }
        }
        binding.txtNext.apply {
            background = gradientDrawable1
            setOnClickListener {
                if (index < imageurls.length() - 1) {
                    index++
                    refreshImage()
                }
            }
        }
        binding.imageview1.setOnTouchListener(this)


    }

    private fun refreshImage() {
        Glide.with(binding.root.context)
            .load(imageurls.getJSONObject(index).getString("img"))
            .error(esa.mylibrary.R.drawable.base_loading_img)
            .into(binding.imageview1)

    }


    /**
     * Called when the activity is first created.
     */

    // 放大缩小
    var matrix = Matrix()
    var savedMatrix = Matrix()

    var start = PointF()
    var mid = PointF()
    var oldDist: Float = 0F
    var oldtime: Long = 0L
    var isclick = false

    // 模式
    val NONE = 0
    val DRAG = 1
    val ZOOM = 2
    var mode = NONE


    override fun onTouch(v: View, event: MotionEvent): Boolean {
        var myImageView = v as ImageView
        MyLog.d("action:" + event.action.toString())
        MyLog.d("actionMasked:" + event.actionMasked.toString())
//        MyLog.d(MotionEvent.ACTION_MASK.toString())
        when (event.actionMasked) {
            // 设置拖拉模式
            MotionEvent.ACTION_DOWN -> {
                matrix.set(myImageView.getImageMatrix())
                savedMatrix.set(matrix)
                start.set(event.getX(), event.getY())
                mode = DRAG
                oldtime = System.currentTimeMillis();// 获取当前时间
                isclick = true
            }

            MotionEvent.ACTION_UP -> {
                mode = NONE;
                if (isclick && System.currentTimeMillis() - oldtime < 200) {
                    //this.dismiss();
                }
                reset()
            }

            MotionEvent.ACTION_POINTER_UP -> {

                mode = NONE;
                // matrix.set(myImageView.getImageMatrix());
                // savedMatrix.set(matrix);
                // start.set(event.getX(), event.getY());
                // mode = DRAG;
                // if (matrix.equals(savedMatrix)) {
                // this.dismiss();
                // }
            }

            // 设置多点触摸模式
            MotionEvent.ACTION_POINTER_DOWN -> {
                isclick = false;
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                }
            }
            // 若为DRAG模式，则点击移动图片
            MotionEvent.ACTION_MOVE -> {
                // isclick = false;
                if (mode == DRAG) {
                    matrix.set(savedMatrix);

                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y)
                }
                // 若为ZOOM模式，则点击触摸缩放
                else if (mode == ZOOM) {


//                    int height =imageview.getMeasuredHeight();
//                    int width =imageview.getMeasuredWidth();
//
//
//                    float newDist = spacing(event);
//                    if (newDist > 10f) {
//                        matrix.set(savedMatrix);
//                        float scale = newDist / oldDist;
//                        // 设置硕放比例和图片的中点位置
//                        matrix.postScale(scale, scale, width/2, height/2);
//                    }
                    var newDist = spacing(event);
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        var scale = newDist / oldDist
                        // 设置硕放比例和图片的中点位置
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
            }
        }
        myImageView.setScaleType(ImageView.ScaleType.MATRIX);
        myImageView.setImageMatrix(matrix);
        return true;
    }

    // 计算移动距离
    private fun spacing(event: MotionEvent): Float {
        var x = event.getX(0) - event.getX(1)
        var y = event.getY(0) - event.getY(1)
        return sqrt(x * x + y * y) as Float
    }

    // 计算中点位置
    private fun midPoint(point: PointF, event: MotionEvent) {
        var x = event.getX(0) + event.getX(1)
        var y = event.getY(0) + event.getY(1)
        point.set(x / 2, y / 2)
    }

    private fun reset() {
        val values = FloatArray(9)
        matrix.getValues(values)
        val leftPosition = values[2]
        val topPosition = values[5]
        if (leftPosition > 0) {
            matrix.postTranslate(-leftPosition, 0F)
        }
        if (topPosition > 0) {
            matrix.postTranslate(0F, -topPosition)
        }

        var w = values[Matrix.MSCALE_X]
        var h = values[Matrix.MSCALE_Y]

        var xTar = (binding.imageview1.width - w) / 2
        var yTar = (binding.imageview1.height - h) / 2

        if(xTar>0)
        {

        }
    }

}