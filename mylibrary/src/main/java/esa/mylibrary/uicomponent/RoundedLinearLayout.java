package esa.mylibrary.uicomponent;

import android.content.Context;
import android.graphics.Outline;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import esa.mylibrary.R;
import esa.mylibrary.utils.DensityUtil;

/**
 * @ProjectName: mydemo
 * @Package: esa.mylibrary.uicomponent
 * @ClassName: RoundedLinearLayout
 * @Description: java类作用描述
 * @Author: tvrddmss
 * @CreateDate: 2023/4/7 23:56
 * @UpdateUser: tvrddmss
 * @UpdateDate: 2023/4/7 23:56
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class RoundedLinearLayout extends LinearLayout {


    public RoundedLinearLayout(Context context) {
        super(context);
        init(context);
    }


    public RoundedLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RoundedLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public RoundedLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);

    }


    private void init(Context context) {
        this.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 20F);
            }
        });
        this.setClipToOutline(true);

        this.setMinimumHeight(DensityUtil.INSTANCE.dip2px(context,40));
        GradientDrawable gradientDrawable1 = new GradientDrawable();// 形状-圆角矩形
        gradientDrawable1.setShape(GradientDrawable.RECTANGLE);// 圆角
        gradientDrawable1.setCornerRadius(20);
        gradientDrawable1.setStroke(1, getContext().getColor(R.color.gray_400));
        this.setBackground(gradientDrawable1);

    }
}
