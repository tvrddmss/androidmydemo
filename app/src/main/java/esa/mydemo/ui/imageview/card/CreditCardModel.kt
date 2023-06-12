package esa.mydemo.ui.imageview.card

import android.graphics.Bitmap
import androidx.annotation.ColorInt

data class CreditCardModel(
    @ColorInt val backgroundColor: Int,
    val bitmap: Bitmap
)
