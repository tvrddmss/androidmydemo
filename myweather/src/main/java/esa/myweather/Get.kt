package esa.myweather

import android.os.Handler
import android.os.Message
import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.io.IOException
import java.util.concurrent.TimeUnit

class Get {

    private val TAG = "WeatherGet"
    private val okHttpClient: OkHttpClient = OkHttpClient.Builder() //应用拦截器
        //            .addInterceptor(new LogginInterceptor())
        //网络拦截器
//        .addNetworkInterceptor(LogginInterceptor())
        .connectTimeout(30000, TimeUnit.MILLISECONDS) //连接超时
        .readTimeout(30000, TimeUnit.MILLISECONDS) //读取超时
        .writeTimeout(30000, TimeUnit.MILLISECONDS) //写入超时
        //                .sslSocketFactory(SSLSocketManager.getSSLSocketFactory())//配置（https请求才需要配置，http请求不用）
        //                .hostnameVerifier(SSLSocketManager.getHostnameVerifier())//配置（https请求才需要配置，http请求不用）
        //                .proxy(Proxy.NO_PROXY)
        .build()

    fun doSync(url: String, par: JSONObject, handler: Handler) {

        var urlString = url + "?1=1"
//        val JSON: MediaType = parse.parse("application/json; charset=utf-8")
//        val body: RequestBody = RequestBody.create(data.toString(), JSON)

        try {
            val it: Iterator<String> = par.keys()
            while (it.hasNext()) { // 遍历JSONObject
                val key = it.next()
                urlString += "&" + key + "=" + par.getString(key)
            }
        } catch (ex: Exception) {
        }

        val builder = Request.Builder()
        builder.url(urlString)
        builder.get()

        val request: Request = builder.build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, e.message.toString())

                var message = Message()
                message.what = 1
                message.obj = e.message
                handler.sendMessage(message)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                try {
                    if (response.isSuccessful) {
                        val result = response.body!!.string()
                        var jsonObject = parse(result)
                        var message = Message()
                        message.what = 0
                        message.obj = jsonObject
                        handler.sendMessage(message)
                        Log.d(TAG, result)
                    } else {

                        var message = Message()
                        message.what = 1
                        message.obj = response.message
                        handler.sendMessage(message)
                    }
                } catch (ex: Exception) {
                    Log.e(TAG, ex.toString())

                    var message = Message()
                    message.what = 1
                    message.obj = ex.message
                    handler.sendMessage(message)
                }
            }
        })
    }

    @Throws(JSONException::class)
    fun parse(str: String?): Any? {
        val jsonTokener = JSONTokener(str)
        return jsonTokener.nextValue()
    }
}