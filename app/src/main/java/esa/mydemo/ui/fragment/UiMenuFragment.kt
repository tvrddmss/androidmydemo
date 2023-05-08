package esa.mydemo.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat.generateViewId
import androidx.core.view.ViewCompat.getRotation
import androidx.core.view.setPadding
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import esa.mydemo.R
import esa.mydemo.databinding.FabButtonsBinding
import esa.mydemo.databinding.FragmentUiMenuBinding
import esa.mylibrary.api.FileDownLoad
import esa.mylibrary.common.CallBack
import esa.mylibrary.fragment.BaseFragment
import esa.mylibrary.icon.MyIcon
import esa.mylibrary.info.DeviceInfo
import esa.mylibrary.utils.MyColor
import esa.mylibrary.utils.MyCommonUtil
import esa.mylibrary.utils.log.MyLog
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.json.JSONObject
import java.io.File


class UiMenuFragment : BaseFragment() {

    companion object {
        fun newInstance() = UiMenuFragment()
    }

    private lateinit var viewModel: UiMenuViewModel

    private lateinit var binding: FragmentUiMenuBinding

    private val intchannel = Channel<Int>(capacity = 1)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(esa.mydemo.R.layout.fragment_ui_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentUiMenuBinding.bind(view)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UiMenuViewModel::class.java)
        // TODO: Use the ViewModel

        init()
    }


    private fun init() {

        //共享轴动画
//        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
//            duration = 2000L
//        }
//        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
//            duration = 2000L
//        }

        viewModel.init()
        val gap = viewModel.gap

        binding.flowView.paddingBottom = gap
        binding.flowView.paddingLeft = gap
        binding.flowView.paddingRight = gap
        binding.flowView.paddingTop = DeviceInfo.statubarHeight + gap


        binding.flowView.setHorizontalGap(gap)
        binding.flowView.setVerticalGap(gap)
        binding.constraint.setBackgroundResource(R.color.background)

        /*
        *
        //宽度要指定，否则会顶出屏幕外面
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginStart="10dp"
        android:layout_marginEnd="10dp"
        //引用的id（内部的view的id）
        app:constraint_referenced_ids="tv_type,tv_online_state,tv_switch_state"
        //chain或者aligned，chain：链形式，依次挨着排，aligned会两端对齐
        app:flow_wrapMode="chain"
        //首行的对齐方式，packed:靠最左侧挨着排，水平间隔：horizontalGap生效，
        // spread：分散对齐，两端不贴边。spread_inside：分散对齐，两头贴边
        app:flow_firstHorizontalStyle="packed"
        //最后一行的对齐方式，其他属性参考firstHorizontalStyle
        app:flow_lastHorizontalStyle="packed"
        // 全局水平bias，为0时，每行都贴左边，可解决中间行单独占一行时，不贴最左侧的问题
        app:flow_horizontalBias="0"
        // 第一行水平bias，为0时，贴最左边
        app:flow_firstHorizontalBias="0"
        // 最后一行水平bias，为0时，贴最左边
        app:flow_lastHorizontalBias="0"
        // 控件水平方向上的间隔
        app:flow_horizontalGap="10dp"
        // 行间隔
        app:flow_verticalGap="8dp"

         */

        CoroutineScope(Dispatchers.Main).launch {
            for (i in 0 until viewModel.jsonArray.length()) {
                var view = withContext(Dispatchers.IO) {
                    delay(10L)
                    val jsonObject = viewModel.jsonArray.getJSONObject(i)

                    LinearLayout(context).apply {
                        orientation = LinearLayout.VERTICAL
                        //颜色减轻--设置透明度
                        var color = MyColor.getStepColor(i, viewModel.jsonArray.length())
                        this.setBackgroundColor(color)
                        background.alpha = 40

                        val itemWidth =
                            ((DeviceInfo.screenWidthPixels - gap) / viewModel.columsCount) - gap
                        this.setPadding(itemWidth / 10)
                        this.gravity = Gravity.CENTER

                        val iconView = TextView(context).apply {
//                            setImageResource(R.drawable.baseline_photo_24)

                            gravity = Gravity.CENTER
                            typeface = MyIcon.getTypeface(context, MyIcon.FONTAWESOME)
                            text = jsonObject.getString("icon")
                            textSize = 40F
                            setTextColor(color)

                        }

                        this.addView(iconView)
                        iconView.layoutParams.width = itemWidth / 2
                        iconView.layoutParams.height = itemWidth / 2


                        var textView = TextView(context).apply {
                            gravity = Gravity.CENTER
                            text = jsonObject.getString("label")


//                            var color = MyColor.getStepColor(i, viewModel.jsonArray.length())
//                            var hsv = FloatArray(3)
//                            Color.colorToHSV(color, hsv);
//                            hsv[2] *= 0.8f; // value component
//                            color = Color.HSVToColor(hsv)

                            setTextColor(color)


                        }
                        this.addView(textView)

                        this.setOnClickListener(View.OnClickListener {
                            onClick(this, jsonObject)
                        })

                    }
                }

                view.id = generateViewId()
                binding.constraint.addView(view, i)
                binding.flowView.addView(view)


                val itemWidth = ((DeviceInfo.screenWidthPixels - gap) / viewModel.columsCount) - gap

                val params = view.layoutParams
                params.width = itemWidth
                params.height = itemWidth
                view.layoutParams = params

            }

        }

        binding.fab.setOnClickListener(View.OnClickListener {
            //带按钮的
//            Snackbar.make(binding.root, "这是massage", Snackbar.LENGTH_LONG)
//                .setAction("这是action", View.OnClickListener() {
//
//                    Toast.makeText(activity, "你点击了action", Toast.LENGTH_SHORT).show();
//
//                }).show();


//            //纯文本的
//            Snackbar.make(binding.root, "This is Snackbar\n23431241234", Snackbar.LENGTH_SHORT)
//                .show();

            //fragment的返回
//            activity?.onBackPressed()


            if (binding.fabs.fab1.visibility == View.INVISIBLE) {
                val animation = AnimationUtils.loadAnimation(context, esa.mydemo.R.anim.ab_expand)
                it.startAnimation(animation);
            }
            else
            {
                val animation = AnimationUtils.loadAnimation(context, esa.mydemo.R.anim.ab_shrink)
                it.startAnimation(animation);
            }

            binding.fabs.fab1.apply {
                if (this.visibility == View.INVISIBLE) {
                    fabShow(
                        this,
                        (binding.fab.width * 1.50).toInt(),
                        (binding.fab.width * 0.00).toInt(),
                        AnimationUtils.loadAnimation(context, esa.mydemo.R.anim.ab1_show)
                    )
                } else {
                    fabHide(
                        this,
                        (binding.fab.width * 1.50).toInt(),
                        (binding.fab.width * 0.00).toInt(),
                        AnimationUtils.loadAnimation(context, esa.mydemo.R.anim.ab1_hide)
                    )
                }
            }

            binding.fabs.fab2.apply {
                if (this.visibility == View.INVISIBLE) {
                    fabShow(
                        this,
                        (binding.fab.width * 1.00).toInt(),
                        (binding.fab.width * 1.00).toInt(),
                        AnimationUtils.loadAnimation(context, esa.mydemo.R.anim.ab2_show)
                    )
                } else {
                    fabHide(
                        this,
                        (binding.fab.width * 1.00).toInt(),
                        (binding.fab.width * 1.00).toInt(),
                        AnimationUtils.loadAnimation(context, esa.mydemo.R.anim.ab2_hide)
                    )
                }
            }

            binding.fabs.fab3.apply {
                if (this.visibility == View.INVISIBLE) {
                    fabShow(
                        this,
                        (binding.fab.width * 0.00).toInt(),
                        (binding.fab.width * 1.50).toInt(),
                        AnimationUtils.loadAnimation(context, esa.mydemo.R.anim.ab3_show)
                    )
                } else {
                    fabHide(
                        this,
                        (binding.fab.width * 0.00).toInt(),
                        (binding.fab.width * 1.50).toInt(),
                        AnimationUtils.loadAnimation(context, esa.mydemo.R.anim.ab3_hide)
                    )
                }
            }

        })

        binding.fabs.fab1.setOnClickListener(View.OnClickListener {
            //带按钮的
            Snackbar.make(binding.root, "这是massage", Snackbar.LENGTH_LONG)
                .setAction("这是action", View.OnClickListener() {

                    Toast.makeText(activity, "你点击了action", Toast.LENGTH_SHORT).show();

                }).show();
        })
    }

    private fun fabShow(fab: FloatingActionButton, right: Int, bottom: Int, animation: Animation) {
        var layoutParams =
            fab.layoutParams as android.widget.FrameLayout.LayoutParams
        layoutParams.setMargins(
            layoutParams.leftMargin,
            layoutParams.topMargin,
            layoutParams.rightMargin + right.toInt(),
            layoutParams.bottomMargin + bottom.toInt(),
        )
        fab.layoutParams = layoutParams
        fab.visibility = View.VISIBLE
        fab.startAnimation(animation);
        fab.setClickable(true)
    }

    private fun fabHide(fab: FloatingActionButton, right: Int, bottom: Int, animation: Animation) {
        var layoutParams =
            fab.layoutParams as android.widget.FrameLayout.LayoutParams
        layoutParams.setMargins(
            layoutParams.leftMargin,
            layoutParams.topMargin,
            layoutParams.rightMargin - right,
            layoutParams.bottomMargin - bottom,
        )
        fab.layoutParams = layoutParams
        fab.visibility = View.INVISIBLE
        fab.startAnimation(animation);
        fab.setClickable(false)
    }

    private fun onClick(view: View, jsonObject: JSONObject) {

        when (jsonObject.getString("type")) {
            "activity" -> {
                MyLog.d(jsonObject.getString("class"))
                var objClass = Class.forName(jsonObject.getString("class"))


                toActivity(view, objClass)

//                val intent = Intent(activity, objClass)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//
//                startActivity(intent)

//                activity?.overridePendingTransition(R.anim.`in`, R.anim.out);
                return
            }

            "devclass" -> {
                FileDownLoad.downloadFile(
                    activity,
                    jsonObject.getString("url"),
                    object : CallBack<Any?>() {
                        override fun success(o: Any?) {
                            MyLog.d((o as File).absolutePath)
                            CoroutineScope(Dispatchers.Main).launch {
//                            ShowToastMessage((o as File).absolutePath)
                                var loader = MyCommonUtil.loadDexClass(
                                    binding.root.context,
                                    (o as File)
                                )
                                var dexClass =
                                    MyCommonUtil.getClass(loader, jsonObject.getString("class"))

                                dexClass?.run {
                                    val aa =
                                        getMethod(jsonObject.getString("method")).invoke(newInstance()) as String
                                    MyLog.d(aa)
                                    ShowToastMessage(aa)
                                }
                            }
                        }

                        override fun error(message: String) {
                            ShowToastMessage(message)
                        }
                    })

                return
            }
        }

        ShowToastMessage(jsonObject.toString())


    }


    fun toActivity(sharedElement: View, objClass: Class<*>) {
        val intent = Intent(binding.root.context, objClass)
//        var options = ActivityOptions.makeSceneTransitionAnimation(
//            activity,
//            sharedElement,
//            "shared_element_end_root"
//        );

        val compat: ActivityOptionsCompat? = activity?.let {
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                it,
                sharedElement,
                "detaildemo"
            )
        }
        if (compat != null) {
//            startActivity(intent, compat.toBundle())
            ActivityCompat.startActivity(
                binding.root.context,
                Intent(binding.root.context, objClass),
                compat.toBundle()
            )
        }
    }
}