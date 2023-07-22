package esa.mylibrary.apinew

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap
import retrofit2.http.Url


interface Request {
    /**
     * 不带参数的get请求
     * @param url
     * @return
     */
    @GET
    operator fun get(@Url url: String?): Call<ResponseBody>

    /**
     * 带参数的get请求
     * @param url
     * @param map 参数默认是map
     * @return
     */
    @GET
    operator fun get(@Url url: String?, @QueryMap map: Map<String, String>): Call<ResponseBody>

    /**
     * 不带参数的post请求
     * @param url
     * @return
     */
    @POST
    fun post(@Url url: String?): Call<ResponseBody>

    /**
     * 带参数的post请求
     * @param url
     * @param map
     * @return
     */
    @POST
    @FormUrlEncoded
    fun post(@Url url: String?, @FieldMap map: Map<String, String>): Call<ResponseBody>
}