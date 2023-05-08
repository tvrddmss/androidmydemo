package esa.mylibrary.api.httpdo;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;

import org.json.JSONObject;

import esa.mylibrary.api.ApiCallBack;
import okhttp3.OkHttpClient;

/**
 * @ProjectName: mydemo
 * @Package: esa.mylibrary.api.httpdo
 * @ClassName: HttpDoParameter
 * @Description: java类作用描述
 * @Author: tvrddmss
 * @CreateDate: 2023/4/28 22:22
 * @UpdateUser: tvrddmss
 * @UpdateDate: 2023/4/28 22:22
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class HttpDoParameter {

    public OkHttpClient okHttpClient;
    public String url;
    public JSONObject data;

    public ApiCallBack callBack;
    protected Handler mHnadler = new Handler(Looper.getMainLooper());

    protected Gson mGson = new Gson();


    public Thread mainThread;

}
