package esa.mylibrary.uicomponent

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

class MyTitleLayout(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    init {
//        LayoutInflater.from(context).inflate(R.layout.title, this)
        addView(DefaultPreview())
    }

    fun DefaultPreview(): View {

        val textView = TextView(context)
        textView.setText("1231")
        return textView
    }
}