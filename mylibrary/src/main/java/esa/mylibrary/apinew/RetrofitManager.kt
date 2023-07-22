package esa.mylibrary.apinew

import retrofit2.Retrofit

object RetrofitManager {

    /**
     *
     * 后端接口的baseUrl，且只考虑一个url的情况（ip+端口，或者域名）
     */
    private var BASE_URL = " Your BASE_URL"

    private lateinit var retrofit: Retrofit

    fun init(baseUrl: String) {
        BASE_URL = baseUrl
    }

    /**
     * 返回Retrofit实例，不添加转换器
     * @return
     */
    fun getRetrofit(): Retrofit {
        if (!this::retrofit.isInitialized) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .build()
        }
        return retrofit
    }


}