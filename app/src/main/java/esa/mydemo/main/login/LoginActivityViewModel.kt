package esa.mydemo.main.login

import androidx.lifecycle.MutableLiveData
import esa.mydemo.base.AppBaseViewModel

class LoginActivityViewModel : AppBaseViewModel() {

    var version: MutableLiveData<String> = MutableLiveData("version")
    var userName: MutableLiveData<String> = MutableLiveData("userName")
    var password: MutableLiveData<String> = MutableLiveData("password")
    var systemname: MutableLiveData<String> = MutableLiveData("systemname")

    fun enableLogin(): Boolean {
      return !(userName.value?.isEmpty() ?: true ||
                password.value?.isEmpty() ?: true)
    }
}