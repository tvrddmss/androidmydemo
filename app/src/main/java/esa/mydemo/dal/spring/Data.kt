package esa.mydemo.dal.spring

class Data {
    private var code: Int = -1
    private lateinit var data: Object

    fun getCode(): Int {
        return code;
    }

    fun setCode(code: Int) {
        this.code = code;
    }

    fun getData(): Object {
        return data;
    }

    fun setData(data: Object) {
        this.data = data;
    }

}