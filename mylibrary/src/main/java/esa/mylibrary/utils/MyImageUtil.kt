package esa.mylibrary.utils

import android.graphics.Bitmap
import android.util.Base64
import esa.mylibrary.utils.log.MyLog
import java.io.ByteArrayOutputStream

object MyImageUtil {
    fun getBase64FromBitmap(bitmap: Bitmap, maxSize: Int): String {
        var result = "";
        val stream = ByteArrayOutputStream()
        var quality = 100;
        if (bitmap.byteCount > maxSize) {
            quality = quality * (maxSize / bitmap.byteCount)
        }
        bitmap.compress(Bitmap.CompressFormat.PNG, quality, stream)
        val byteArray: ByteArray = stream.toByteArray()
        result = "data:image/png;base64," + Base64.encodeToString(byteArray, Base64.DEFAULT)
        return result
    }

    fun getBitmapBySize(bitmap: Bitmap, maxLength: Int): Bitmap {
        try {

            var bl = maxLength.toDouble() / bitmap.height.toDouble()
            if (bitmap.width > bitmap.height) {
                bl = maxLength.toDouble() / bitmap.width.toDouble()
            }
            if (bl < 1) {
                var width = (bitmap.width * bl).toInt()
                var height = (bitmap.height * bl).toInt()
                val resized = Bitmap.createScaledBitmap(
                    bitmap, width, height, true
                )
                return resized
            } else {
                return bitmap
            }
        } catch (ex: Exception) {
            MyLog.e(ex.message)
            return bitmap
        }
    }
}