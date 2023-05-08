package esa.mydemo.fragment;

import android.content.Intent;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import esa.mydemo.main.LoginActivity;
import esa.mylibrary.info.UserInfo;

public class MyProfileViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    //数据绑定
    public MutableLiveData<String> title = new MutableLiveData<>("个人信息");
    public MutableLiveData<String> userName = new MutableLiveData<>("用户名");
    public MutableLiveData<String> userId = new MutableLiveData<>("ID");
    public MutableLiveData<String> devInfo = new MutableLiveData<>("设备信息");
    public MutableLiveData<String> userInfo = new MutableLiveData<>("用户详细信息");
    private View view;
    private Fragment fragment;


    public void init(View view, Fragment myProfileFragment) {
        this.view = view;
        this.fragment = myProfileFragment;

    }

    /**
     * @param view
     * @return void
     * @description 注销
     * @author tvrddmss
     * @time 2023/4/7 21:18
     */
    public void onLogoutClick(View view) {
        UserInfo.setPassword("");

        //跳转页面
        Intent intent = new Intent(this.view.getContext(), LoginActivity.class);
        this.view.getContext().startActivity(intent);
        this.fragment.getActivity().finish();
    }


    /**
     * @param view
     * @return void
     * @description 修改密码
     * @author tvrddmss
     * @time 2023/4/7 21:18
     */
    public void onEditPasswordClick(View view) {

    }
}