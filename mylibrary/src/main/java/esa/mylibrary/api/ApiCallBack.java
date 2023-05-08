package esa.mylibrary.api;


import com.google.gson.internal.$Gson$Types;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


/**
 * 回调函数
 * Created by leict on 2017/6/6.
 */

public abstract class ApiCallBack<T> {
    public Type mType;

    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            return null;
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    public ApiCallBack() {
        mType = getSuperclassTypeParameter(this.getClass());
    }

    /**
     * 成功
     *
     * @param t
     */
    public abstract void onSuccess(T t);

    /**
     * 错误代码
     *
     * @param code
     */
    public abstract void onError(int code);

    /**
     * 失败
     *
     * @param e
     */
    public abstract void onFailure(IOException e);
}
