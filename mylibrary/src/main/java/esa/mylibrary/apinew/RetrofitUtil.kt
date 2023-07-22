package esa.mylibrary.apinew

import esa.mylibrary.config.Config
import esa.mylibrary.utils.MyJson
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import kotlin.reflect.KParameter


/**
 *
 * @ProjectName: mydemo
 * @Package: esa.myapi.apinew
 * @ClassName: RetrofitUtil
 * @Description: java类作用描述
 * @Author: Administrator
 * @CreateDate: 2023/07/07 13:53
 * @UpdateUser: Administrator
 * @UpdateDate: 2023/07/07 13:53
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
object RetrofitUtil {

    /**
     * 无参的get请求
     * @param url
     * @param callback
     */
    fun get(url: String, callback: RetrofitCallback) {
        sendRequest(getRequest().get(addToken(url)), callback);
    }

    /**
     * 有参的get请求
     * @param url  请求的url
     * @param map  参数
     * @param callback  请求结束的回调
     */
    fun get(url: String, map: Map<String, String>, callback: RetrofitCallback) {
        sendRequest(getRequest().get(addToken(url), map), callback);
    }

    /**
     * 无参的post请求
     * @param url
     * @param callback
     */
    fun post(url: String, callback: RetrofitCallback) {
        sendRequest(getRequest().post(addToken(url)), callback);
    }

    /**
     * 有参的post请求
     * @param url
     * @param map
     * @param callback
     */
    public fun post(url: String, map: Map<String, String>, callback: RetrofitCallback) {
        sendRequest(getRequest().post(addToken(url), map), callback);
    }


    /**
     * 获取Request实例
     * @return
     */
    private fun getRequest(): Request {
        var retrofit = RetrofitManager.getRetrofit()
        return retrofit.create(Request::class.java)
    }

    /**
     * 发送请求的共通方法，并对响应结果进行处理
     * @param call
     * @param callback 自定义的Callback
     */
    private fun sendRequest(call: Call<ResponseBody>, callback: RetrofitCallback) {

        //开启loading
        callback.onStart();
        //异步请求
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                //关闭loading
                callback.onCompleted();
                if (response.isSuccessful()) {
                    //执行RetrofitCallback的onSuccess方法，获取响应结果的json字符串
                    try {
                        var result = response.body()?.string()
                        var resultJSONObject = MyJson.parse(result) as JSONObject

                        if (resultJSONObject.getString("result").equals("true")) {
                            callback.onSuccess(resultJSONObject.getString("message"))
                        } else {
                            throw Exception(resultJSONObject.getString("message"))
                        }

                        //响应成功
                        if (result == "") {
                            callback.okMsg();
                        }
                    } catch (e: Exception) {
                        e.printStackTrace();

                        //请求失败
                        callback.onError(Throwable(e.message))
                        callback.reqErrMsg()
                    }
                } else {
                    //服务异常
//                    callback.serverErrMsg();
                    callback.onError(Throwable(response.message()))
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callback.onCompleted();
                //请求失败
                callback.onError(t);

                callback.reqErrMsg();
            }
        })
    }


    private fun addToken(url: String): String {
        var result = url + "?1=1"
        for (key in Config.api.functiontoken.keys) {
            result += "&" + key + "=" + Config.api.functiontoken[key].toString()
        }
        return result
    }


}