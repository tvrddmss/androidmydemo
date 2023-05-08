package esa.mylibrary.api;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import esa.mylibrary.api.httpdo.Get;
import esa.mylibrary.api.httpdo.HttpDo;
import esa.mylibrary.api.httpdo.HttpDoParameter;
import esa.mylibrary.api.httpdo.Post;
import esa.mylibrary.api.httpdo.Put;
import esa.mylibrary.config.Config;
import esa.mylibrary.utils.log.MyLog;
import okhttp3.OkHttpClient;

public class Http {
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            //应用拦截器
//            .addInterceptor(new LogginInterceptor())
            //网络拦截器
            .addNetworkInterceptor(new LogginInterceptor())
            .connectTimeout(30000, TimeUnit.MILLISECONDS) //连接超时
            .readTimeout(30000, TimeUnit.MILLISECONDS) //读取超时
            .writeTimeout(30000, TimeUnit.MILLISECONDS) //写入超时
//                .sslSocketFactory(SSLSocketManager.getSSLSocketFactory())//配置（https请求才需要配置，http请求不用）
//                .hostnameVerifier(SSLSocketManager.getHostnameVerifier())//配置（https请求才需要配置，http请求不用）
//                .proxy(Proxy.NO_PROXY)
            .build();

    public static void setUrlContainsProject(Boolean urlContainsProject) {
        Http.urlContainsProject = urlContainsProject;
    }

    private static Boolean urlContainsProject = true;

    private static HttpDo get;
    private static HttpDo post;
    private static HttpDo put;

    public static void post( String url, JSONObject data, ApiCallBack callBack) {
        if (post == null) {
            post = new Post();
        }
        doHttp(post,  url, data, callBack);
    }

    public static void get( String url, JSONObject data, ApiCallBack callBack) {
        if (get == null) {
            get = new Get();
        }
        doHttp(get, url, data, callBack);
    }

    public static void put( String url, JSONObject data, ApiCallBack callBack) {
        if (put == null) {
            put = new Put();
        }
        doHttp(put,  url, data, callBack);
    }

    private static void doHttp(HttpDo httpDo,  String url, JSONObject data, ApiCallBack callBack) {
        try {
            HttpDoParameter httpDoParameter = new HttpDoParameter();
            httpDoParameter.url = Config.api.http + "://" + Config.api.ip + ":" + Config.api.port + "/";
            if (urlContainsProject) {
                httpDoParameter.url += Config.api.project + "/";
            }
            httpDoParameter.url += url;
            httpDoParameter.okHttpClient = okHttpClient;
            httpDoParameter.data = data;
            httpDoParameter.callBack = callBack;
            httpDoParameter.mainThread = Thread.currentThread();
            Thread thread = new Thread(() -> httpDo.run(httpDoParameter));
            thread.start();
        } catch (Exception ex) {
            MyLog.e(ex.getMessage());
        }
    }
}
