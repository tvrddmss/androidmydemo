package esa.mydemo.dal.spring

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.QueryMap


interface BlogArticelsService {
    //get请求
    @GET("article/selectPage")
    fun getList(
        @QueryMap map: Map<String, String>,
        @Header("Authorization") loginToken: String?
    ): Call<esa.mydemo.dal.spring.Data>


    //put请求
    @PUT("article/update")
    fun update(
        @Body map: JsonObject,
        @Header("Authorization") loginToken: String?
    ): Call<esa.mydemo.dal.spring.Data>
}