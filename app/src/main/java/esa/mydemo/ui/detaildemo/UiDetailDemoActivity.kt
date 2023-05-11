package esa.mydemo.ui.detaildemo

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ActionMenuView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomappbar.BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
import com.google.android.material.bottomappbar.BottomAppBar.FAB_ALIGNMENT_MODE_END
import com.google.android.material.bottomappbar.BottomAppBar.FabAlignmentMode
import com.google.android.material.snackbar.Snackbar
import esa.mydemo.R
import esa.mydemo.base.AppBaseActivity
import esa.mydemo.databinding.ActivityUiDetailDemoBinding
import esa.mylibrary.info.DeviceInfo


class UiDetailDemoActivity : AppBaseActivity() {


    private lateinit var binding: ActivityUiDetailDemoBinding
    private lateinit var viewModel: UiDetailDemoActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_ui_detail_demo)

        //状态栏透明，不占位
        setStausBarTranslucent()

        binding = ActivityUiDetailDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[UiDetailDemoActivityViewModel::class.java]
        viewModel.setContext(binding.root.context)

        binding.root.setPadding(0, DeviceInfo.statubarHeight, 0, 0)

        binding.viewModel = viewModel

        viewModel.onClickListener = View.OnClickListener {

            //纯文本的
            Snackbar.make(binding.root, "This is Snackbar\n23431241234", Snackbar.LENGTH_SHORT)
                .show();
        }


        binding.fab.setOnClickListener {

            when (binding.bottomappbarView.fabAlignmentMode) {
                FAB_ALIGNMENT_MODE_END -> {
                    binding.bottomappbarView.fabAlignmentMode = FAB_ALIGNMENT_MODE_CENTER
                }

                FAB_ALIGNMENT_MODE_CENTER -> {
                    binding.bottomappbarView.fabAlignmentMode = FAB_ALIGNMENT_MODE_END
                }
            }
//            showMessage("12313123\n34321")
        }
    }
}