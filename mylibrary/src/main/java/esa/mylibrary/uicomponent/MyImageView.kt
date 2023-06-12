package esa.mylibrary.uicomponent

import android.content.Context
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import esa.mylibrary.R

class MyImageView(context: Context, url: String) : PopupWindow(context),
    View.OnTouchListener {

    private lateinit var imageview: ImageView
    private lateinit var context: Context
    private lateinit var imageurl: String

    var current = 0

    init {
        try {
            var imagess: MutableList<String> = ArrayList<String>()
            if (!url.contains("\\^")) {
                imagess = url.split("^").toMutableList()
            } else {
                imagess[0] = url
            }
            imageurl = imagess.get(current);
            // 布局用参数
            var mp: ViewGroup.MarginLayoutParams
            var lp: LinearLayout.LayoutParams
            var lpRel: RelativeLayout.LayoutParams

            this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT)
            this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT)
            this.setOutsideTouchable(true)
            this.setFocusable(true)

            var content = RelativeLayout(context);
            mp = ViewGroup.MarginLayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
            )

            lp = LinearLayout.LayoutParams(mp)
            content.setLayoutParams(lp)
//            content.setOrientation(LinearLayout.HORIZONTAL)
            content.gravity = Gravity.CENTER
            this.setContentView(content)


//            var linearLayout = LinearLayout(context)
//            mp = ViewGroup.MarginLayoutParams(
//                RelativeLayout.LayoutParams.MATCH_PARENT,
//                RelativeLayout.LayoutParams.MATCH_PARENT
//            )
//            lp = LinearLayout.LayoutParams(mp)
//            linearLayout.layoutParams = lp
//            linearLayout.gravity = Gravity.CENTER
//            linearLayout.gravity = 1
//            linearLayout.orientation = LinearLayout.VERTICAL
//            content.addView(linearLayout)


            imageview = ImageView(context).apply {
                mp = ViewGroup.MarginLayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT
                )
                // mp.setMargins(10, 10, 10, 10);
                lp = LinearLayout.LayoutParams(mp)
                lp.gravity = Gravity.CENTER;
                layoutParams = lp
                scaleType = ImageView.ScaleType.FIT_CENTER
                content.addView(this)
            }
           imageview.setOnTouchListener(this)

//            Glide.with(context)
//                .load(imageurl)
//                .skipMemoryCache(true)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .error(R.drawable.base_loading_img)
//                .into(imageview)
//            var tv = TextView(context);
//            mp = ViewGroup.MarginLayoutParams(
//                RelativeLayout.LayoutParams.MATCH_PARENT,
//                RelativeLayout.LayoutParams.WRAP_CONTENT
//            )
//            lp = LinearLayout.LayoutParams(mp)
//            lp.gravity = Gravity.CENTER;
//            tv.setLayoutParams(lp);
//            tv.setGravity(Gravity.CENTER);
//            tv.setBackgroundColor(this.context.getResources().getColor(R.color.white));
//            tv.setText("浏览器打开");
//            tv.setTextColor(this.context.getResources().getColor(R.color.black));
//            tv.setOnClickListener {
//                // TODO Auto-generated method stub
//                var i = Intent(Intent.ACTION_VIEW, Uri.parse(imageurl));
//                i.setData(Uri.parse(imageurl));
//                context.startActivity(i);
//            }
//
//            linearLayout.addView(tv);


            TextView(context).apply {

                mp = ViewGroup.MarginLayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
                lpRel = RelativeLayout.LayoutParams(mp).apply {
                    leftMargin = 20
                    alignWithParent = true
                    addRule(RelativeLayout.CENTER_VERTICAL)
                    addRule(RelativeLayout.ALIGN_PARENT_LEFT)
                }

                val gradientDrawable1 = GradientDrawable() // 形状-圆角矩形
                gradientDrawable1.shape = GradientDrawable.RECTANGLE // 圆角
                gradientDrawable1.cornerRadius = 40f
                gradientDrawable1.setStroke(1, getContext().getColor(R.color.gray_400))
                gradientDrawable1.setColor(Color.parseColor("#55000000"))
                background = gradientDrawable1
                setTextColor(Color.WHITE)
                typeface = Typeface.defaultFromStyle(Typeface.BOLD);
                setPadding(20, 20, 20, 20)
                gravity = Gravity.CENTER
                layoutParams = lpRel
                setText("上一张")
                setOnClickListener {
                    if (current > 0) {
                        current--
                        imageurl = imagess[current]
                        setImage(context)
                    }
                }
                content.addView(this)
            }

            TextView(context).apply {
                mp = ViewGroup.MarginLayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )

                lpRel = RelativeLayout.LayoutParams(mp).apply {
                    rightMargin = 20
                    alignWithParent = true
                    addRule(RelativeLayout.CENTER_VERTICAL)
                    addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                }
                val gradientDrawable1 = GradientDrawable() // 形状-圆角矩形
                gradientDrawable1.shape = GradientDrawable.RECTANGLE // 圆角
                gradientDrawable1.cornerRadius = 40f
                gradientDrawable1.setStroke(1, getContext().getColor(R.color.gray_400))
                gradientDrawable1.setColor(Color.parseColor("#55000000"))
                background = gradientDrawable1
                setTextColor(Color.WHITE)
                typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                setPadding(20, 20, 20, 20)
                layoutParams = lpRel
                setText("下一张")
                setOnClickListener {
                    if (current < imagess.size - 1) {
                        current++
                        imageurl = imagess[current]
                        setImage(context)
                    }
                }
                content.addView(this)
            }

            TextView(context).apply {
                mp = ViewGroup.MarginLayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )

                lpRel = RelativeLayout.LayoutParams(mp).apply {
                    rightMargin = 20
                    topMargin = 20
                    alignWithParent = true
                    addRule(RelativeLayout.ALIGN_PARENT_TOP)
                    addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                }
                val gradientDrawable1 = GradientDrawable() // 形状-圆角矩形
                gradientDrawable1.shape = GradientDrawable.RECTANGLE // 圆角
                gradientDrawable1.cornerRadius = 40f
                gradientDrawable1.setStroke(1, getContext().getColor(R.color.gray_400))
                gradientDrawable1.setColor(Color.parseColor("#55000000"))
                background = gradientDrawable1
                setTextColor(Color.WHITE)
                typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                setPadding(20, 20, 20, 20)
                layoutParams = lpRel
                setText("关闭")
                setOnClickListener {
                    dismiss();
                }
                content.addView(this)
            }

            setImage(context)
        } catch (ex: Exception) {
            //Log.write(context, "展示图片失败！" + ex.getMessage(),4);
        }

    }

    private fun setImage(context: Context) {
        Glide.with(context)
            .load(imageurl)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .error(R.drawable.base_loading_img)
            .into(imageview)
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

        when (event.action) {
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

                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
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
        var x = event.getX(0) - event.getX(1) as Double
        var y = event.getY(0) - event.getY(1) as Double
        return Math.sqrt(x * x + y * y) as Float
    }

    // 计算中点位置
    private fun midPoint(point: PointF, event: MotionEvent) {
        var x = event.getX(0) + event.getX(1)
        var y = event.getY(0) + event.getY(1)
        point.set(x / 2, y / 2)
    }

}