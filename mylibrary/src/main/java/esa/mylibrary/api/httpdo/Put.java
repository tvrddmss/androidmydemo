package esa.mylibrary.api.httpdo;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import esa.mylibrary.api.ApiCallBack;
import esa.mylibrary.api.TokenTools;
import esa.mylibrary.config.Config;
import esa.mylibrary.entity.ApiResult;
import esa.mylibrary.utils.MyJson;
import esa.mylibrary.utils.log.MyLog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @ProjectName: mydemo
 * @Package: esa.mylibrary.api.httpdo
 * @ClassName: Put
 * @Description: java类作用描述
 * @Author: Administrator
 * @CreateDate: 2023/04/23 15:52
 * @UpdateUser: Administrator
 * @UpdateDate: 2023/04/23 15:52
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class Put extends HttpDo {

    @Override
    public synchronized void doSync(HttpDoParameter httpDoParameter) {
        String url = httpDoParameter.url;
        JSONObject data = httpDoParameter.data;
        OkHttpClient okHttpClient = httpDoParameter.okHttpClient;
        ApiCallBack callBack = httpDoParameter.callBack;
        Thread mainThread = httpDoParameter.mainThread;
        Gson mGson = httpDoParameter.mGson;


        url += "?1=1" + TokenTools.INSTANCE.AddFunctionToken();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(data.toString(), JSON);

//        FormBody.Builder builder = new FormBody.Builder();
//        try {
//            Iterator<String> it = data.keys();
//            while (it.hasNext()) { // 遍历JSONObject
//                String key = it.next().toString();
//                builder.add(key, data.getString(key));
//            }
//        } catch (Exception ex) {
//        }
//        RequestBody body = builder.build();

        Request.Builder builder = new Request.Builder();
        if (!Config.api.loginToken.isEmpty()) {
            builder.addHeader("logintoken", Config.api.loginToken);
            builder.addHeader("Authorization", Config.api.loginToken);
        }
        builder.url(url);
        builder.put(body);

        Request request = builder.build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyLog.e("post-failure:" + e.getMessage());
                sendonFailureMessage(httpDoParameter, callBack, call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
//                    MyLog.d("post-success:" + result);

                    ApiResult apiResult = mGson.fromJson(result, ApiResult.class);
                    if (apiResult.getResult() != null) {
                        if (apiResult.getResult().equals("true")) {
                            //几种返回类型，string,JSONObject,JSONArray,class
                            if (callBack.mType == null || callBack.mType == String.class) {
                                sendonSuccessMessage(httpDoParameter, callBack, apiResult.getMessage());
                            } else if (callBack.mType == JSONObject.class) {
                                //避免JSON.parse报错
                                try {
                                    sendonSuccessMessage(httpDoParameter, callBack, MyJson.parse(apiResult.getMessage()));
                                } catch (Exception ex) {
                                    sendonFailureMessage(httpDoParameter, callBack, call, new IOException(apiResult.getMessage()));
                                }
                            } else if (callBack.mType == JSONArray.class) {
                                //避免JSON.parse报错
                                try {
                                    sendonSuccessMessage(httpDoParameter, callBack, MyJson.parse(apiResult.getMessage()));
                                } catch (Exception ex) {
                                    sendonFailureMessage(httpDoParameter, callBack, call, new IOException(apiResult.getMessage()));
                                }
                            } else {
                                sendonSuccessMessage(httpDoParameter, callBack, mGson.fromJson(apiResult.getMessage(), callBack.mType));//MyJson.parse(apiResult.getMessage()));//
                            }
                        } else {
                            sendonFailureMessage(httpDoParameter, callBack, call, new IOException(apiResult.getMessage()));
                        }
                    } else {
                        try {
                            JSONObject jsonObject = (JSONObject) MyJson.parse(result);
                            if (jsonObject.getInt("code") == 0) {
                                sendonSuccessMessage(httpDoParameter, callBack, jsonObject.get("data"));
                            } else {
                                sendonFailureMessage(httpDoParameter, callBack, call, new IOException(jsonObject.getString("msg")));
                            }
                        } catch (JSONException e) {
                            sendonFailureMessage(httpDoParameter, callBack, call, new IOException(e.getMessage()));
                        }
                    }
                    if (response.body() != null) {
                        response.body().close();
                    }
                } else {
                    MyLog.e("post-Response-fail:code:" + response.code() + "\tmessage:" + response.message().toString() + "\tbody:" + response.body().toString());
                    sendonErrorMessage(httpDoParameter, callBack, response.code());
                }
            }
        });

    }


}