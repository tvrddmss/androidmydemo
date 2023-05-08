package esa.mylibrary.api;

import androidx.annotation.NonNull;

import java.io.IOException;

import esa.mylibrary.utils.log.MyLog;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

//应用拦截器
public class LogginInterceptor implements Interceptor {
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

//        应用拦截器
//        1：不需要关心像重定向和重试这样的中间响应。
//        2：总是调用一次，即使HTTP响应从缓存中获取服务。
//        3：监视应用原始意图。不关心OkHttp注入的像If-None-Match头。
//        4：允许短路并不调用Chain.proceed()。
//        5：允许重试并执行多个Chain.proceed()调用。

//        网络拦截器
//        1：可以操作像重定向和重试这样的中间响应。
//        2：对于短路网络的缓存响应不会调用。
//        3：监视即将要通过网络传输的数据。
//        4：访问运输请求的Connection。

        Request request = chain.request().newBuilder()
                .addHeader("os", "android")
                .addHeader("version", "1.0").build();
        MyLog.d("---Interceptor拦截器---");
        MyLog.d(request.url().toString());
        return chain.proceed(request);
    }
}
