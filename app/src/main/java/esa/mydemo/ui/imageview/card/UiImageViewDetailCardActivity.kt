package esa.mydemo.ui.imageview.card

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.lifecycle.Observer
import esa.mydemo.R
import esa.mydemo.base.AppBaseActivity
import esa.mydemo.databinding.ActivityUiImageViewDetailCardBinding


class UiImageViewDetailCardActivity : AppBaseActivity() {
    private lateinit var binding: ActivityUiImageViewDetailCardBinding

    private val viewModel: CreditCardsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ui_image_view_detail_card)

        binding = ActivityUiImageViewDetailCardBinding.inflate(layoutInflater)
        setContentView(binding.root)



        init()
    }

    private fun init() {

        viewModel.modelStream.observe(this, Observer {
                bindCard(it)
            })
        viewModel.swipeRightEnable.observe(this) {
            binding.motionLayout.getTransition(R.id.trans_right).isEnabled = it
        }
        viewModel.swipeLeftEnable.observe(this) {
            binding.motionLayout.getTransition(R.id.trans_left).isEnabled = it
        }
        binding.motionLayout.setTransitionListener(object : TransitionAdapter() {
            override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
                motionLayout.post {
                    when (currentId) {
                        R.id.secondCard -> {
                            motionLayout.progress = 0f
                            viewModel.swipeRight()
                        }

                        R.id.firstCard -> {
                            motionLayout.progress = 0f
                            viewModel.swipeLeft()
                        }

                        R.id.detailCard -> {

                        }

                        R.id.start -> {
//                            motionLayout.progress = 0f
//                            binding.motionLayout.getTransition(R.id.trans_left).isEnabled =
//                                viewModel.getEnableLeft()
//                            binding.motionLayout.getTransition(R.id.trans_right).isEnabled =
//                                viewModel.getEnableRight()
                        }
                    }
                }
            }

            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
                super.onTransitionStarted(motionLayout, startId, endId)

            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
                super.onTransitionChange(motionLayout, startId, endId, progress)
                if (startId == R.id.start && endId == R.id.detailCard) {
                    if (progress > 0.3F) {
                        showDetail()
                    } else {
                        closeDetail()
                    }
                }
            }

        })
    }


    private fun bindCard(it: CreditCardsModel) {
        bindCardDetail(it.cardFourLeft, binding.cardLeft4)
        bindCardDetail(it.cardThreeLeft, binding.cardLeft3)
        bindCardDetail(it.cardTwoLeft, binding.cardLeft2)
        bindCardDetail(it.cardOneLeft, binding.cardLeft1)
        bindCardDetail(it.cardCenter, binding.cardCenter)
        bindCardDetail(it.cardOneRight, binding.cardRight1)
        bindCardDetail(it.cardTwoRight, binding.cardRight2)
        bindCardDetail(it.cardThreeRight, binding.cardRight3)
        bindCardDetail(it.cardFourRight, binding.cardRight4)


    }

    private fun bindCardDetail(
        model: CreditCardModel?,
        card: com.google.android.material.card.MaterialCardView
    ) {
        if (model == null) {
//            card.visibility = View.GONE
            card.setCardBackgroundColor(Color.parseColor("#00000000"))
            (card.getChildAt(0) as ImageView).apply {
                setImageBitmap(null)
            }
        } else {
//            card.visibility = View.INVISIBLE
            card.setCardBackgroundColor(model.backgroundColor)
            (card.getChildAt(0) as ImageView).apply {
//                scaleType = ImageView.ScaleType.CENTER_CROP
                setImageBitmap(model.bitmap)
            }
        }
    }

    private fun showDetail() {
        var view = binding.cardCenter.getChildAt(0)
        if (view is ImageView) {
            view.scaleType = ImageView.ScaleType.FIT_CENTER

//            view.setImageBitmap(viewModel.modelStream.value?.cardCenter?.bitmap)
//            Glide.with(binding.root.context)
//                .load(viewModel.modelStream.value?.cardCenter?.imageUrl)
//                .error(esa.mylibrary.R.drawable.base_loading_img)
//                .into(view)
        }

    }

    private fun closeDetail() {
        var view = binding.cardCenter.getChildAt(0)
        if (view is ImageView) {
            view.scaleType = ImageView.ScaleType.CENTER_CROP

//            view.setImageBitmap(viewModel.modelStream.value?.cardCenter?.bitmap)
//            Glide.with(binding.root.context)
//                .load(viewModel.modelStream.value?.cardCenter?.imageUrl)
//                .error(esa.mylibrary.R.drawable.base_loading_img)
//                .into(view)
        }

    }
}