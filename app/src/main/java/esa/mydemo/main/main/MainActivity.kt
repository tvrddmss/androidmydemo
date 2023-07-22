package esa.mydemo.main.main

import android.os.Bundle
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.transition.MaterialSharedAxis
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import esa.mydemo.R
import esa.mydemo.base.AppBaseActivity
import esa.mydemo.databinding.MainActivityMainBinding
import esa.mydemo.fragment.headerdemo.MyComplexMotion
import esa.mydemo.fragment.menu.UiMenuFragment
import esa.mydemo.fragment.myprofile.MyProfileFragment
import esa.mylibrary.info.DeviceInfo
import esa.mylibrary.utils.AssetManagerTool
import esa.mylibrary.utils.DensityUtil.px2dip
import esa.mylibrary.utils.log.MyLog
import esa.mylibrary.vibrator.MyVibrator

class MainActivity : AppBaseActivity() {


    private lateinit var binding: MainActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

    //上方需要padding的距离dp
    private var paddingTop = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //动画效果-容器转换
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        window.sharedElementsUseOverlay = false

        binding = MainActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        viewModel.setContext(binding.root.context)


//        setContentView(R.layout.main_activity_main);
        //取消补间动画
//        overridePendingTransition(0, 0);

//        setStatusBackColor(getColor(R.color.white));
//        setStatusDarkColor(true);
        isDoubleClickExit = false

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        init()
    }

    fun init() {

        //状态栏透明，不占位
        setStausBarTranslucent()
        //因为状态栏不占位所以上方需要padding单位为dp
        paddingTop = px2dip(binding.root.context, DeviceInfo.statubarHeight)
        osmFragment.paddingTop = paddingTop

        binding = DataBindingUtil.setContentView(this, R.layout.main_activity_main)

        binding.navView.setOnItemSelectedListener {
            //震动
            MyVibrator.vibrate(this, 50)
            //声音
            AssetManagerTool.playButtonSound(binding.root.context)
            //直接创建FragmentTransaction对象即可，然后调用对象中的方法进行Fragment页面布局的加载
            ft = supportFragmentManager.beginTransaction()

            when (it.itemId) {
                R.id.navigation_home -> {
//                    hideAll()
//                    if (!osmFragment.isAdded) {
//                        ft.add(R.id.fragement, osmFragment)
//                    }
//                    ft.show(osmFragment)
                    replaceFragment(osmFragment)
                }

                R.id.navigation_menu -> {
//                    hideAll()
//                    if (!myComplexMotion.isAdded) {
//                        ft.add(R.id.fragement, myComplexMotion) //.addToBackStack("");
//                    }
//                    ft.show(myComplexMotion)

                    replaceFragment(myComplexMotion)
                }

                R.id.navigation_menufragment -> {
//                    hideAll()
//                    if (!uiMenuFragment.isAdded) {
//                        ft.add(R.id.fragement, uiMenuFragment)
//                    }
//                    ft.show(uiMenuFragment)

                    replaceFragment(uiMenuFragment)
                }

                R.id.navigation_account -> {
//                    hideAll()
//                    if (!myProfileFragment.isAdded) {
//                        ft.add(R.id.fragement, myProfileFragment) //.addToBackStack("");
//                    }
//                    ft.show(myProfileFragment)

//                    myProfileFragment.enterTransition =
//                        MaterialSharedAxis(MaterialSharedAxis.Z, true)
                    replaceFragment(myProfileFragment)
                }
            }
            ft.commit()
            true
        }
        binding.navView.selectedItemId = R.id.navigation_home
        currentFragment = osmFragment
        binding.navView.getOrCreateBadge(R.id.navigation_menu).number = 19 // Show badge

        fragmentList.add(osmFragment)
        fragmentList.add(myComplexMotion)
        fragmentList.add(uiMenuFragment)
        fragmentList.add(myProfileFragment)

    }


    private lateinit var ft: FragmentTransaction

    private var fragmentList: ArrayList<Fragment> = ArrayList()
    private var currentFragment: Fragment? = null
    private var osmFragment = esa.mydemo.fragment.main.OsmFragment()
    private var myComplexMotion = MyComplexMotion()
    private var uiMenuFragment = UiMenuFragment()
    private var myProfileFragment = MyProfileFragment()

    private fun hideAll() {
        if (osmFragment.isAdded) {
            ft.hide(osmFragment)
        }
        if (myComplexMotion.isAdded) {
            ft.hide(myComplexMotion)
        }
        if (uiMenuFragment.isAdded) {
            ft.hide(uiMenuFragment)
        }
        if (myProfileFragment.isAdded) {
            ft.hide(myProfileFragment)
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        //共享Z轴动画
        try {
            var myDuration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()

            if (fragmentList.indexOf(currentFragment) > fragmentList.indexOf(fragment)) {
                currentFragment?.returnTransition =
                    MaterialSharedAxis(MaterialSharedAxis.X, true).apply { duration = myDuration }
                fragment.enterTransition =
                    MaterialSharedAxis(MaterialSharedAxis.X, false).apply { duration = myDuration }
            } else {
                currentFragment?.returnTransition =
                    MaterialSharedAxis(MaterialSharedAxis.X, false).apply { duration = myDuration }
                fragment.enterTransition =
                    MaterialSharedAxis(MaterialSharedAxis.X, true).apply { duration = myDuration }
            }
        } catch (ex: Exception) {
            MyLog.e(ex.message)
        }
        currentFragment?.apply {
            ft.hide(currentFragment!!)
        }
        if (!fragment.isAdded) {
            ft.add(R.id.fragement, fragment)
        }
        ft.show(fragment)
        currentFragment = fragment
    }
}