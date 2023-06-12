package esa.mydemo.ui.chart

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import esa.mylibrary.utils.log.MyLog
import java.text.SimpleDateFormat
import java.util.Date


class XAxisValueFormatter : ValueFormatter() {

    override fun getFormattedValue(
        value: Float,
        axis: AxisBase
    ): String? {

        MyLog.d(value.toString())
        var date = Date(value.toLong())
        val f1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
        val s2 = f1.format(date) // 2020-06-30 11:00:26.401
        MyLog.d(s2)
        return s2
    }
}