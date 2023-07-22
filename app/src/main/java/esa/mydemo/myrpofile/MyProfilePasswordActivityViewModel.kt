package esa.mydemo.myrpofile

import androidx.lifecycle.MutableLiveData
import esa.mydemo.base.AppBaseViewModel


/**
 *
 * @ProjectName: mydemo
 * @Package: esa.mydemo.myrpofile
 * @ClassName: MyProfilePasswordActivityViewModel
 * @Description: java类作用描述
 * @Author: Administrator
 * @CreateDate: 2023/07/10 11:59
 * @UpdateUser: Administrator
 * @UpdateDate: 2023/07/10 11:59
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
class MyProfilePasswordActivityViewModel : AppBaseViewModel() {

    var oldpassword: MutableLiveData<String> = MutableLiveData("")
    var newpassword: MutableLiveData<String> = MutableLiveData("")
    var newpasswordp: MutableLiveData<String> = MutableLiveData("")

}