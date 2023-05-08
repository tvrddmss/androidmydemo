package esa.mydemo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.transition.MaterialSharedAxis;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.Iterator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import esa.mydemo.R;
import esa.mydemo.databinding.FragmentMyProfileBinding;
import esa.mylibrary.fragment.BaseFragment;
import esa.mylibrary.info.DeviceInfo;
import esa.mylibrary.info.UserInfo;

public class MyProfileFragment extends BaseFragment {


    //数据模型
    private MyProfileViewModel mViewModel;
    //xml绑定实例
    private FragmentMyProfileBinding binding;
    //当前页面view
    private View view;


//    public static MyProfileFragment newInstance() {
//        return new MyProfileFragment();
//    }

    //region 静态内部类单例模式

    /**
     * 静态内部类单例
     * 优点：外部类加载的时候并不需要立即去加载内部类，内部类不被加载则不会实例化mInstance，不占内存资源，
     * 保证单例的唯一性，同时也延迟了单例的实例化。
     */
    private static class MyProfileFragmentHolder {
        private static MyProfileFragment mInstance = new MyProfileFragment();
    }

    /**
     * @return
     * @description
     * @author Administrator
     * @time 2023/04/04 16:44
     */
    public static MyProfileFragment getInstance() {
        return MyProfileFragment.MyProfileFragmentHolder.mInstance;
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        mViewModel = new ViewModelProvider(this).get(MyProfileViewModel.class);
        // TODO: Use the ViewModel

    }
    //endregion
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//
//        MaterialSharedAxis materialSharedAxis=new MaterialSharedAxis(MaterialSharedAxis.Z, true);
//        materialSharedAxis.setDuration(2000l);
//        this.setExitTransition(materialSharedAxis);
//
//        MaterialSharedAxis materialSharedAxis1=new MaterialSharedAxis(MaterialSharedAxis.Z, false);
//        materialSharedAxis1.setDuration(2000L);
//        this.setReenterTransition(materialSharedAxis1);

        //根据xml创建数据绑定
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_profile, container, false);
        //设置数据绑定对象生命周期的拥有者
        binding.setLifecycleOwner(this);
        binding.contentHeader.setPadding(10, 10 + DeviceInfo.statubarHeight, 10, 0);
        //获取当前View
        view = binding.getRoot();

        init();
        return view;
    }


    private void init() {
        //实例设置到页面绑定
        binding.setViewModel(mViewModel);

        mViewModel.init(view, this);

        try {
            mViewModel.userName.postValue(UserInfo.getUserInfo().getString("sys_username"));
            mViewModel.userId.postValue(UserInfo.getUserInfo().getString("sys_userid"));

            {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("IP", DeviceInfo.ip);
                jsonObject.put("MAC", DeviceInfo.mac);
                jsonObject.put("平台", DeviceInfo.platform);
                jsonObject.put("deviceToken", DeviceInfo.deviceTokenString);
                jsonObject.put("设备型号", DeviceInfo.model);
                jsonObject.put("状态栏高度", DeviceInfo.statubarHeight);
                jsonObject.put("设备设置语言", DeviceInfo.local);

                String txt = "";
                for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
                    String key = it.next();
                    txt += key + ":" + jsonObject.getString(key) + "\r\n";
                }
                mViewModel.devInfo.postValue(txt);
            }

            {
                String txt = "";
                for (Iterator<String> it = UserInfo.getUserInfo().keys(); it.hasNext(); ) {
                    String key = it.next();
                    txt += key + ":" + UserInfo.getUserInfo().getString(key) + "\r\n";
                }
                mViewModel.userInfo.postValue(txt);
            }


        } catch (Exception ex) {
            ShowToastMessageLongTime(ex.getMessage());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

}