package esa.mylibrary.utils

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import androidx.annotation.RequiresApi
import androidx.camera.core.ImageProxy
import androidx.exifinterface.media.ExifInterface
import esa.mylibrary.gps.MyGps
import esa.mylibrary.sensor.MySensor
import esa.mylibrary.utils.log.MyLog
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.Date
import java.util.Locale


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

    fun imageProxyToBitmap(image: ImageProxy): Bitmap {
        val buffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.capacity()).also { buffer.get(it) }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun saveBitmap(activity: Context, bitmap: Bitmap) {
        //设置图片名称，要保存png，这里后缀就是png，要保存jpg，后缀就用jpg
        val imageName = System.currentTimeMillis().toString() + "code.jpg"
        var fileName = ""
        val brand = Build.BRAND.lowercase(Locale.ROOT)

        if (brand == "xiaomi") { // 小米手机brand.equals("xiaomi")
            fileName = Environment.getExternalStorageDirectory().path + "/DCIM/Camera/"
        } else if (brand.equals("Huawei", ignoreCase = true)) {
            fileName = Environment.getExternalStorageDirectory().path + "/DCIM/Camera/"
        } else { // Meizu 、Oppo
            fileName = Environment.getExternalStorageDirectory().path + "/DCIM/"
        }

        val file = File(fileName!! + "/" + imageName) //创建文件
        //        file.getParentFile().mkdirs();

        try {
            //文件输出流
            val fileOutputStream = FileOutputStream(file)
            //压缩图片，如果要保存png，就用Bitmap.CompressFormat.PNG，要保存jpg就用Bitmap.CompressFormat.JPEG,质量是100%，表示不压缩
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)


            //写入，这里会卡顿，因为图片较大
            fileOutputStream.flush()
            //记得要关闭写入流
            fileOutputStream.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        // 下面的步骤必须有，不然在相册里找不到图片，若不需要让用户知道你保存了图片，可以不写下面的代码。
        // 把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(
                activity.contentResolver,
                file.absolutePath, imageName, null
            )
//            Toast.makeText(this, "保存成功，请您到 相册/图库 中查看", Toast.LENGTH_SHORT).show()
        } catch (e: FileNotFoundException) {
//            Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
        //            // 最后通知图库更新
        activity.sendBroadcast(
            Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(File(file.path))
            )
        )

        kotlin.run {

            var exif = ExifInterface(file)
            //设置经纬度，TAG是可以自定义的
            MyGps.getInstance().location?.apply {

                exif.setAttribute(
                    ExifInterface.TAG_GPS_LONGITUDE,
                    MyGps.getInstance().latLng2DfmForExif(this.longitude)
                )
                exif.setAttribute(
                    ExifInterface.TAG_GPS_LATITUDE,
                    MyGps.getInstance().latLng2DfmForExif(this.latitude)
                )

                var lon_ref = ""
                if (this.longitude > 0) {
                    lon_ref = "E"
                } else {
                    lon_ref = "W"
                }
                exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, lon_ref)

                var lat_ref = ""
                if (this.latitude > 0) {
                    lat_ref = "N"
                } else {
                    lat_ref = "S"
                }
                exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, lat_ref)

                exif.setAttribute(
                    ExifInterface.TAG_GPS_PROCESSING_METHOD,
                    MySensor.getInstance().angle.toString()
                )
            }

            var simpleDateFormat = SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss")
            //获取当前时间
            var date = Date(System.currentTimeMillis())
            exif.setAttribute(ExifInterface.TAG_DATETIME, simpleDateFormat.format(date))
            exif.saveAttributes()

            var exifInterface = ExifInterface(file)
            val lat = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE)
            val longitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)
            MyLog.d(lat + "," + longitude)
        }

    }

    fun getBitmapByUrl(url: String): Bitmap? {
        var bitmap: Bitmap
        try {
            var inputStream = BufferedInputStream(URL(url).openStream(), 1024)
            val dataStream = ByteArrayOutputStream()
            var out = BufferedOutputStream(dataStream, 1024)
            copy(inputStream, out)
            out.flush()
            var data = dataStream.toByteArray()
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
            data = null;

            return bitmap;
        } catch (e: IOException) {
            return null
        }
    }

    private fun copy(inputStream: InputStream, out: OutputStream) {
        var b = ByteArray(1024);
        var read = inputStream.read(b)
        while (read != -1) {
            out.write(b, 0, read);
        }
    }

    /**
     * 网络获取图片
     *
     * @param imgUrl
     * @return
     */
    fun getBitmap(imgUrl: String?): Bitmap? {
        var inputStream: InputStream? = null
        var outputStream: ByteArrayOutputStream? = null
        var url: URL? = null
        try {
            url = URL(imgUrl)
            val httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.requestMethod = "GET"
            httpURLConnection.readTimeout = 2000
            httpURLConnection.connect()
            if (httpURLConnection.responseCode == 200) {
                //网络连接成功
                inputStream = httpURLConnection.inputStream
                outputStream = ByteArrayOutputStream()
                val buffer = ByteArray(1024 * 8)
                var len = -1
                while (inputStream.read(buffer).also { len = it } != -1) {
                    outputStream.write(buffer, 0, len)
                }
                val bu = outputStream.toByteArray()
                return BitmapFactory.decodeByteArray(bu, 0, bu.size)
            } else {
//                Logger.e("网络连接失败----" + httpURLConnection.responseCode)
            }
        } catch (e: Exception) {
            // TODO: handle exception
            e.printStackTrace()
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close()
                } catch (e: IOException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close()
                } catch (e: IOException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }
            }
        }
        return null
    }

    /**
    通过URI获取当前的绝对路径
     */
    fun getPathByUri(contentResolver: ContentResolver, uri: Uri): String {
        var path = ""
        if (uri == null) {
            return ""
        }
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri!!, filePathColumn, null, null, null)!!
        cursor.moveToFirst()
        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
        path = cursor.getString(columnIndex)
        cursor.close()
        return path
    }

    fun getExif(context: Context, path: String): JSONObject {
        val exifInterface: ExifInterface = ExifInterface(path)

        var json = JSONObject()

        json.put("orientation", exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION))
        json.put("dateTime", exifInterface.getAttribute(ExifInterface.TAG_DATETIME))
        json.put("make", exifInterface.getAttribute(ExifInterface.TAG_MAKE))
        json.put("model", exifInterface.getAttribute(ExifInterface.TAG_MODEL))
        json.put("flash", exifInterface.getAttribute(ExifInterface.TAG_FLASH))
        json.put("imageLength", exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH))
        json.put("imageWidth", exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH))
        json.put("latitude", exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE))
        json.put("longitude", exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE))
        json.put("latitudeRef", exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF))
        json.put("longitudeRef", exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF))
        json.put("exposureTime", exifInterface.getAttribute(ExifInterface.TAG_EXPOSURE_TIME))
//        json.put("aperture", exifInterface.getAttribute(ExifInterface.TAG_APERTURE))
//        json.put("isoSpeedRatings", exifInterface.getAttribute(ExifInterface.TAG_ISO))
        json.put(
            "dateTimeDigitized",
            exifInterface.getAttribute(ExifInterface.TAG_DATETIME_DIGITIZED)
        )
        json.put("subSecTime", exifInterface.getAttribute(ExifInterface.TAG_SUBSEC_TIME))
//        json.put("subSecTimeOrig", exifInterface.getAttribute(ExifInterface.TAG_SUBSEC_TIME_ORIG))
//        json.put("subSecTimeDig", exifInterface.getAttribute(ExifInterface.TAG_SUBSEC_TIME_DIG))
        json.put("altitude", exifInterface.getAttribute(ExifInterface.TAG_GPS_ALTITUDE))
        json.put("altitudeRef", exifInterface.getAttribute(ExifInterface.TAG_GPS_ALTITUDE_REF))
        json.put("gpsTimeStamp", exifInterface.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP))
        json.put("gpsDateStamp", exifInterface.getAttribute(ExifInterface.TAG_GPS_DATESTAMP))
        json.put("whiteBalance", exifInterface.getAttribute(ExifInterface.TAG_WHITE_BALANCE))
        json.put("focalLength", exifInterface.getAttribute(ExifInterface.TAG_FOCAL_LENGTH))
        json.put(
            "processingMethod",
            exifInterface.getAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD)
        )

        return json

//        ExifInterface.TAG_ORIENTATION //旋转角度，整形表示，在ExifInterface中有常量对应表示
//        ExifInterface.TAG_DATETIME //拍摄时间，取决于设备设置的时间
//        ExifInterface.TAG_MAKE //设备品牌
//        ExifInterface.TAG_MODEL //设备型号，整形表示，在ExifInterface中有常量对应表示
//        ExifInterface.TAG_FLASH //闪光灯
//        ExifInterface.TAG_IMAGE_LENGTH //图片高度
//        ExifInterface.TAG_IMAGE_WIDTH //图片宽度
//        ExifInterface.TAG_GPS_LATITUDE //纬度
//        ExifInterface.TAG_GPS_LONGITUDE //经度
//        ExifInterface.TAG_GPS_LATITUDE_REF //纬度名（N or S）
//        ExifInterface.TAG_GPS_LONGITUDE_REF //经度名（E or W）
//        ExifInterface.TAG_EXPOSURE_TIME //曝光时间
//        ExifInterface.TAG_APERTURE //光圈值
//        ExifInterface.TAG_ISO //ISO感光度
//        ExifInterface.TAG_DATETIME_DIGITIZED //数字化时间
//        ExifInterface.TAG_SUBSEC_TIME //
//        ExifInterface.TAG_SUBSEC_TIME_ORIG //
//        ExifInterface.TAG_SUBSEC_TIME_DIG //
//        ExifInterface.TAG_GPS_ALTITUDE //海拔高度
//        ExifInterface.TAG_GPS_ALTITUDE_REF //海拔高度
//        ExifInterface.TAG_GPS_TIMESTAMP //时间戳
//        ExifInterface.TAG_GPS_DATESTAMP //日期戳
//        ExifInterface.TAG_WHITE_BALANCE //白平衡
//        ExifInterface.TAG_FOCAL_LENGTH //焦距
//        ExifInterface.TAG_GPS_PROCESSING_METHOD //用于定位查找的全球定位系统处理方法。

    }
}