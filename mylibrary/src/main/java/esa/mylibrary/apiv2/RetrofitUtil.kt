package esa.mylibrary.apiv2

import esa.mylibrary.config.Config
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitUtil {
    lateinit var retrofit: retrofit2.Retrofit

    fun init() {
        retrofit = Retrofit.Builder()
            //设置网络请求BaseUrl地址
            .baseUrl(Config.api.http + "://" + Config.api.ip + ":" + Config.api.port + "/" + Config.api.project + "/")
            //设置数据解析器
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }
}