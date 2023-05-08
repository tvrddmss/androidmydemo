package esa.mydemo.ui.chart

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import java.text.DecimalFormat

class PercentageAxisValueFormatter : IAxisValueFormatter {

    private lateinit var mFormat: DecimalFormat

    fun PercentageAxisValueFormatter() {
        mFormat = DecimalFormat("0.0");
    }

    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
        TODO("Not yet implemented")
        return mFormat.format(value * 100) + "%";
    }
}