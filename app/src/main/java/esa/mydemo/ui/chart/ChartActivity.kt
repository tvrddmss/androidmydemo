package esa.mydemo.ui.chart

import android.graphics.Color
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import esa.mydemo.base.AppBaseActivity
import esa.mydemo.databinding.ActivityChartBinding
import esa.mylibrary.info.DeviceInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime


class ChartActivity : AppBaseActivity() {

    private lateinit var binding: ActivityChartBinding
    private lateinit var viewModel: ChartActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(esa.mydemo.R.layout.activity_chart)

        binding = ActivityChartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(ChartActivityViewModel::class.java)
        viewModel.setContext(binding.root.context)


        //状态栏透明，不占位
        setStausBarTranslucent()

        binding.linearLayout.setPadding(0, DeviceInfo.statubarHeight, 0, 0)

        init()
    }

    fun init() {
        var chart = binding.chart1
        // 背景色
        chart.setBackgroundColor(Color.WHITE)

        // 图表的文本描述
        chart.getDescription().setEnabled(false)

        // 手势设置
        chart.setTouchEnabled(true)

        // 添加监听器
//        chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
//            override fun onValueSelected(e: Entry?, h: Highlight?) {
//                TODO("Not yet implemented")
//                showMessage(h.toString() + e.toString())
//            }
//
//            override fun onNothingSelected() {
//                TODO("Not yet implemented")
//            }
//
//        })
        chart.setDrawGridBackground(false)

        // 自定义 MarkView，当数据被选择时会展示
//        val mv = MyMarkerView(this, R.layout.custom_marker_view)
//        mv.setChartView(chart)
//        chart.setMarker(mv)

        // 设置拖拽、缩放等
        chart.setDragEnabled(true)
        chart.setScaleEnabled(true)
        chart.setScaleXEnabled(true)
        chart.setScaleYEnabled(true)

        // 设置双指缩放
        chart.setPinchZoom(true)


        // 获取 X 轴
        var xAxis = chart.getXAxis();
        // 允许显示 X 轴的垂直网格线
//        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.spaceMin = 0F
        xAxis.spaceMax = 0F
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f // only intervals of 1 day
        xAxis.textSize = 8f
//        xAxis.textColor = ContextCompat.getColor(this, R.color.colorYellow)
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axisBase: AxisBase?): String? {
//                val dateInMillis = Date(value.toLong())
//                var ss = SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(dateInMillis)
//                MyLog.d(ss)
                var result = String.format("%02d", (value % 60).toInt())
                result = String.format("%02d", (value / 60 % 60).toInt()) + ":" + result
                result = String.format("%02d", (value / 60 / 60 % 60).toInt()) + ":" + result
                return result
            }
        }
        xAxis.labelRotationAngle = 0F
//        xAxis.mLabelHeight=50
//        xAxis.mLabelRotatedHeight=50


        // 获取 Y 轴
        var yAxis = chart.getAxisLeft();
        // 禁止右轴
        chart.getAxisRight().setEnabled(false);
        // Y 轴的水平网格线
//        yAxis.enableGridDashedLine(10f, 10f, 0f);
        // 设置 Y 轴的数值范围
//        yAxis.setAxisMaximum(200f);
//        yAxis.setAxisMinimum(0f);


        var count = 20
        var range = 200
        // 1. 每个数据是一个 Entry
        val values = ArrayList<Entry>()

//        for (i in 0 until count) {
//            val `val` = (Math.random() * range).toFloat() - 30
//            values.add(
//                Entry(
//                    i.toFloat(),
//                    `val`,
//                    null//resources.getDrawable(esa.mydemo.R.drawable.baseline_apps_24)
//                )
//            )
//        }
        // 2. 创建一个数据集 DataSet ，用来添加 Entry。一个图中可以包含多个数据集
        var set1 = LineDataSet(values, "当前应用内存占用（MB）：线状图（可缩放）")
        // 3. 通过数据集设置数据的样式，如字体颜色、线型、线型颜色、填充色、线宽等属性
        // draw dashed line
        set1.enableDashedLine(10f, 0f, 0f)
        // black lines and points
        set1.setColor(Color.BLUE)
        set1.setCircleColor(Color.BLUE)
        // line thickness and point size
        set1.setLineWidth(1f)
        set1.setCircleRadius(1f)
        set1.setDrawCircles(false)
        // draw points as solid circles
        set1.setDrawCircleHole(false)
        set1.setDrawValues(false)
        // 4.将数据集添加到数据 ChartData 中
        var data = LineData(set1)
        // 5. 将数据添加到图表中
        chart.data = data

        CoroutineScope(Dispatchers.Main).launch {

            var count = chart.data.getDataSetByIndex(0).entryCount
            while (chart.data.getDataSetByIndex(0).entryCount < 100) {
//                values.removeAt(0)
//                val `val` = (Math.random() * range).toFloat() - 30
//                values.add(
//                    Entry(
//                        values.get(values.size - 1).x + 1,
//                        `val`,
//                        null//resources.getDrawable(esa.mydemo.R.drawable.baseline_apps_24)
//                    )
//                )
//                set1.values = values
//                data.dataSets.set(0, set1)
                chart.data.getDataSetByIndex(0).addEntry(
                    Entry(
//                        count.toFloat(),
//                        System.currentTimeMillis().toFloat(),
                        (LocalDateTime.now().hour * 60 * 60 + LocalDateTime.now().minute * 60 + LocalDateTime.now().second.toFloat()).toFloat(),
                        DeviceInfo.getAppMemory().toFloat() / 1000,
                        LocalDateTime.now()
                    )
                )
                if (chart.data.getDataSetByIndex(0).entryCount > 20) {
                    chart.data.getDataSetByIndex(0).removeEntry(0)
                }
                chart.data.notifyDataChanged()
                chart.notifyDataSetChanged()
                chart.invalidate()
                delay(1000)
                count++
            }
        }
    }

}