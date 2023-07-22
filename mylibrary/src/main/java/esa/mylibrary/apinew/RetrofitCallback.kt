package esa.mylibrary.apinew

abstract class RetrofitCallback {


    /**
     * 开始执行的方法
     */
    fun onStart() {
        //开启loading
    }

    /**
     * 结束执行的方法
     */
    fun onCompleted() {
        //关闭loading
    }

    /**
     * 执行成功
     * @param resultJsonString  返回的json字符串
     */
    abstract fun onSuccess(resultJsonString: String?)

    /**
     * 失败
     * @param t 异常
     */
    abstract fun onError(t: Throwable?)

    /**
     * 提示：服务异常
     */
    fun serverErrMsg() {
        //xxx
    }

    /**
     * 提示：请求失败
     */
    fun reqErrMsg() {
        //xxx
    }


    /**
     * 提示：成功
     */
    fun okMsg() {
        //xxx
    }

}