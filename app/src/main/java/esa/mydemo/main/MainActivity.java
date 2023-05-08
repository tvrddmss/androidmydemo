package esa.mydemo.main;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.animation.AnimationUtils;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.transition.MaterialSharedAxis;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;
import esa.mydemo.BR;
import esa.mydemo.R;
import esa.mydemo.base.AppBaseActivity;
import esa.mydemo.databinding.MainActivityMainBinding;
import esa.mydemo.fragment.MyProfileFragment;
import esa.mydemo.ui.fragment.UiMenuFragment;
import esa.mylibrary.fragment.ListFragment;
import esa.mylibrary.fragment.OsmFragment;
import esa.mylibrary.info.DeviceInfo;
import esa.mylibrary.info.UserInfo;
import esa.mylibrary.utils.AssetManagerTool;
import esa.mylibrary.utils.DensityUtil;
import esa.mylibrary.vibrator.MyVibrator;

public class MainActivity extends AppBaseActivity {


    private MainActivityMainBinding binding;
    private Entity entity;

    //上方需要padding的距离dp
    private int paddingTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //动画效果-容器转换
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        setExitSharedElementCallback(new MaterialContainerTransformSharedElementCallback());
        getWindow().setSharedElementsUseOverlay(false);



//        setContentView(R.layout.main_activity_main);
        //取消补间动画
//        overridePendingTransition(0, 0);

//        setStatusBackColor(getColor(R.color.white));
//        setStatusDarkColor(true);

        isDoubleClickExit = false;




        //状态栏透明，不占位
        setStausBarTranslucent();
        //因为状态栏不占位所以上方需要padding单位为dp
        paddingTop = DensityUtil.INSTANCE.px2dip(MainActivity.this, DeviceInfo.statubarHeight);

        binding = DataBindingUtil.setContentView(this, R.layout.main_activity_main);

        //新建绑定实例
        entity = new Entity();
        //实例设置到页面绑定
        binding.setEntity(entity);
        //初始化
        entity.init();
    }

    /**
     * 页面数据模型
     */
    public class Entity extends BaseObservable {

        private BottomNavigationView bottomNavigationView;

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

        /**
         * @param
         * @return null
         * @description 初始化
         * @author Administrator
         * @time 2023/03/30 12:11
         */
        public void init() {
            this.setVersion(UserInfo.getUserInfo().toString());
            this.bottomNavigationView = findViewById(R.id.nav_view);
            this.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    onClick(item);
                    return true;
                }
            });
            this.bottomNavigationView.setSelectedItemId(R.id.navigation_home);
            this.bottomNavigationView.getOrCreateBadge(R.id.navigation_menu).setNumber(19); // Show badge


        }

        androidx.fragment.app.FragmentTransaction ft;

        UiMenuFragment uiMenuFragment = new UiMenuFragment();


        private void hideAll() {
            if (OsmFragment.getInstance().isAdded()) {
                ft.hide(OsmFragment.getInstance());
            }
            if (ListFragment.getInstance().isAdded()) {
                ft.hide(ListFragment.getInstance());
            }
            if (uiMenuFragment.isAdded()) {
                ft.hide(uiMenuFragment);
            }
            if (MyProfileFragment.getInstance().isAdded()) {
                ft.hide(MyProfileFragment.getInstance());
            }
        }

        public void onClick(MenuItem item) {
            //震动
            MyVibrator.vibrate(MainActivity.this, 50);
            //声音
            AssetManagerTool.playButtonSound(MainActivity.this);

            ft = getSupportFragmentManager().beginTransaction();//直接创建FragmentTransaction对象即可，然后调用对象中的方法进行Fragment页面布局的加载
            switch (item.getItemId()) {

                case R.id.navigation_home:

                    hideAll();
                    if (!OsmFragment.getInstance().isAdded()) {
                        ft.add(R.id.fragement, OsmFragment.getInstance());
                        OsmFragment.getInstance().paddingTop = paddingTop;
                    }
                    ft.show(OsmFragment.getInstance());
                    break;
                case R.id.navigation_menu:
//                            showMessage("home");
//
//                    //跳转页面
//                    Intent intent = new Intent(MainActivity.this, UiListActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);

                    hideAll();
                    if (!ListFragment.getInstance().isAdded()) {
                        ft.add(R.id.fragement, ListFragment.getInstance());//.addToBackStack("");
                    }
                    ft.show(ListFragment.getInstance());
                    break;
                case R.id.navigation_menufragment:
//                            showMessage("home");
                    hideAll();
                    if (!uiMenuFragment.isAdded()) {
                        ft.add(R.id.fragement, uiMenuFragment);//.addToBackStack("");
                    }
                    ft.show(uiMenuFragment);
                    break;
                case R.id.navigation_account:
                    hideAll();
                    if (!MyProfileFragment.getInstance().isAdded()) {
                        ft.add(R.id.fragement, MyProfileFragment.getInstance());//.addToBackStack("");
                    }
                    ft.show(MyProfileFragment.getInstance());
                    break;
            }
            ft.commit();
        }
    }
}