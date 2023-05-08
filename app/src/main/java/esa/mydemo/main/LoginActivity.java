package esa.mydemo.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import java.util.Locale;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;
import esa.mydemo.BR;
import esa.mydemo.R;
import esa.mydemo.base.AppBaseActivity;
import esa.mydemo.dal.spring.PlatformForSpring;
import esa.mydemo.databinding.MainActivityLoginBinding;
import esa.mydemo.setting.SettingsActivity;
import esa.mylibrary.common.CallBack;
import esa.mylibrary.info.AppInfo;
import esa.mylibrary.info.DeviceInfo;
import esa.mylibrary.info.UserInfo;
import esa.mylibrary.language.LanguageHelper;
import esa.mylibrary.utils.ClassMethod;
import esa.mylibrary.utils.log.MyLog;

public class LoginActivity extends AppBaseActivity {

    private MainActivityLoginBinding binding;
    private Entity entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //必须在activity中设置才会生效
        //获取当前应用程序设置语言
        SharedPreferences listPreference = android.preference.PreferenceManager.getDefaultSharedPreferences(this);
        String language = listPreference.getString("language", DeviceInfo.local);
        Locale locale = Locale.ENGLISH;
        switch (language) {
            case "cn":
            case "zh":
                locale = Locale.CHINA;
                break;
            case "en":
                locale = Locale.ENGLISH;
                break;
        }
        LanguageHelper.INSTANCE.forceLocale(getApplication(), this, locale);

        //获取当前应用程序设置主题
//        String theme = listPreference.getString("theme", getTheme().toString());
//        setTheme(Reflect.INSTANCE.getStyleId(this, theme));

        //状态栏不占位
//        setStausBarTransparent();//全透明不可设置半透明
        setStausBarTranslucent();//可通过theme设置半透明

        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main_activity_login);
        //获取页面绑定
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity_login);

        //新建绑定实例
        entity = new Entity();
        //实例设置到页面绑定
        binding.setEntity(entity);

        binding.loginTxtBottomSystemName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                entity.loginBtnClick(view);
                return false;
            }
        });


//        binding = MainActivityLoginBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        LoginActivityViewModel viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(LoginActivityViewModel.class);
//        viewModel.setContext(binding.getRoot().getContext());


        //第一个Activity初始化
        showloading("初始化。。。");
        setIsFirstActiviyInitForSpring(new CallBack() {
            @Override
            public void success(Object o) {
                closeloading();
                //初始化
                entity.init();
            }

            @Override
            public void error(String message) {
                closeloading();
                showExceptionMessage(message);
                //初始化
                entity.init();

            }
        });

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //当第一个活动屏幕展开后，计算屏幕大小
        DeviceInfo.getScreen(this);
    }

    /**
     * @return void
     * @description
     * @author Administrator
     * @time 2023/03/31 14:37
     */
    private void loginSuccess() {
        try {

//            Animation animation= AnimationUtils.loadAnimation(this, R.anim.out);
//            animation.setAnimationListener(new Animation.AnimationListener() {
//                @Override
//                public void onAnimationStart(Animation animation) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animation animation) {
//                    //保存登录名及密码
//                    UserInfo.setLoginUsername(entity.getUserName());
//                    UserInfo.setPassword(entity.getPassword());
//                    //跳转页面
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    LoginActivity.this.startActivity(intent);
//                    LoginActivity.this.finish();
////            overridePendingTransition(R.anim.out,R.anim.in);
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation animation) {
//
//                }
//            });
//
//            binding.getRoot().startAnimation(animation);

            //保存登录名及密码
            UserInfo.setLoginUsername(entity.getUserName());
            UserInfo.setPassword(entity.getPassword());
            //跳转页面
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            LoginActivity.this.startActivity(intent);
            LoginActivity.this.finish();

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


        } catch (Exception ex) {
            showExceptionMessage(this.getClass().getName() + ClassMethod.getCurrentMethod() + "页面跳转失败：" + ex.getMessage());
        }

    }

    private void login() {
        if (entity.getUserName().isEmpty() || entity.getPassword().isEmpty()) {
            showExceptionMessage("登录名或密码不能为空！");
            return;
        }
        showloading();

//            try {
//                Config.api.loginToken = "logintoken";
//                String userinfo = "{\"sys_userid\":\"3229\",\"sys_username\":\"数据采集\",\"sys_userloginname\":\"sjcj\",\"sys_photourl\":\"\",\"sys_value1\":\"\",\"sys_value2\":\"\",\"sys_value3\":\"\",\"sys_value4\":\"\",\"sys_value5\":\"\",\"sys_value6\":\"\",\"sys_value7\":\"\",\"sys_value8\":\"\",\"sys_value9\":\"\",\"sys_value10\":\"100\",\"sys_toporgan\":\"40\",\"sys_organid\":\"\",\"sys_organcode\":\"\",\"sys_organname\":\"\",\"sys_toporganname\":\"\",\"sys_xzqy\":[{\"id\":\"022\",\"text\":\"天津市\"}],\"sys_roles\":\"\",\"sys_rolenames\":\"\",\"sys_rolenameremarks\":\"\",\"sys_positionids\":\"\",\"sys_positionnames\":\"\",\"sys_groups\":[],\"sys_fields\":[],\"sys_rules\":[{\"f_id\":\"3319\",\"f_code\":\"0345\",\"f_name\":\"app数据采集\",\"f_url\":\"0102\",\"f_target\":\"gzrw\",\"f_tile\":\"\",\"f_rulemodel\":\"3\",\"f_sys_appcode\":\"40\",\"f_children\":[]}]}";
//                JSONObject jsonObject = (JSONObject) MyJson.parse(userinfo);
//                UserInfo.setUserInfo(jsonObject);
//            } catch (Exception ex) {
//                showExceptionMessage(ex.getMessage());
//            }

//            closeloading();
//            loginSuccess();

        PlatformForSpring.Login(entity.userName, entity.password, new CallBack() {
            @Override
            public void success(Object o) {
                closeloading();
                loginSuccess();
            }

            @Override
            public void error(String message) {
                closeloading();
                showExceptionMessage("登录失败：" + message);
            }
        });
    }

    /**
     * 页面数据模型
     */
    public class Entity extends BaseObservable {
        public Entity() {
        }

        //region version
        private String version;

        @Bindable
        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
            notifyPropertyChanged(BR.version);
        }
        //endregion

        //region userName


        @Bindable
        public String getUserName() {
            return userName;
        }

        public void setUserName(String username) {
            this.userName = username;
            notifyPropertyChanged(BR.userName);
        }

        private String userName;

        //endregion

        //region password

        @Bindable
        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
            notifyPropertyChanged(BR.password);
        }

        private String password;

        //endregion

        //region systemname

        @Bindable
        public String getSystemname() {
            return systemname;
        }

        public void setSystemname(String systemname) {
            this.systemname = systemname;
            notifyPropertyChanged(BR.systemname);
        }

        private String systemname;

        //endregion


        /**
         * @param
         * @return null
         * @description 初始化
         * @author Administrator
         * @time 2023/03/30 12:11
         */
        public void init() {
            try {
                switch (getResources().getConfiguration().locale.getLanguage()) {
                    case "zh":
                        this.setVersion("当前程序版本:" + AppInfo.appVersionName);
                        break;
                    case "en":
                        this.setVersion("CurrentVersion:" + AppInfo.appVersionName);
                        break;
                }
                this.setSystemname(getResources().getString(R.string.app_name));
                this.setUserName(UserInfo.getLoginUsername());
                this.setPassword(UserInfo.getPassword());

                //判断登录名及密码不能为空时，自动登录
                if (!this.getUserName().isEmpty() && !this.getPassword().isEmpty()) {
                    this.loginBtnClick(null);
                }
            } catch (Exception ex) {
                MyLog.e("登录页面初始化失败！" + ex.getMessage());
            }
        }


        /**
         * @param view
         * @return void
         * @description 登录按钮
         * @author Administrator
         * @time 2023/03/30 12:12
         */
        public void loginBtnClick(View view) {
            login();
        }

        /**
         * @param view
         * @return void
         * @description 系统名称长按
         * @author Administrator
         * @time 2023/03/30 12:12
         */
        public boolean onLongClick(View view) {
            //跳转页面
            Intent intent = new Intent(LoginActivity.this, SettingsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            LoginActivity.this.startActivity(intent);
            return false;
        }
    }
}