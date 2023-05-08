package esa.mylibrary.icon

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.util.Locale


//import com.joanzapata.iconify.Iconify
//import com.joanzapata.iconify.fonts.FontAwesomeModule
//import com.joanzapata.iconify.fonts.IoniconsModule
//import com.joanzapata.iconify.widget.IconTextView

object MyIcon {

    fun init() {
//        Iconify.with(IoniconsModule())
//            .with(FontAwesomeModule())
    }
//
//    fun getIconView(context: Context, text: String): IconTextView {
//
//        val view = IconTextView(context).apply {
//            this.text = text
//        }
//
//        return view
//    }

    const val ROOT = "fonts/"
    const val FONTAWESOME = ROOT + "iconfont.ttf"
    fun getTypeface(context: Context, font: String?): Typeface? {
        return Typeface.createFromAsset(context.assets, font)
    }

    fun markAsIconContainer(v: View?, typeface: Typeface?) {
        if (v is ViewGroup) {
            val vg = v
            for (i in 0 until vg.childCount) {
                val child = vg.getChildAt(i)
                markAsIconContainer(child,typeface)
            }
        } else if (v is TextView) {
            v.setTypeface(typeface)
        }
    }


}