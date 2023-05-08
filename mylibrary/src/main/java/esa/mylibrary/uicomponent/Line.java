package esa.mylibrary.uicomponent;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import esa.mylibrary.R;

/**
 * @ProjectName: mydemo
 * @Package: esa.mylibrary.uicomponent
 * @ClassName: Line
 * @Description: java类作用描述
 * @Author: tvrddmss
 * @CreateDate: 2023/4/8 0:50
 * @UpdateUser: tvrddmss
 * @UpdateDate: 2023/4/8 0:50
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class Line extends LinearLayout {
    public Line(Context context) {
        super(context);
        init();
    }

    public Line(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Line(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public Line(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        this.setBackground(this.getContext().getDrawable(R.color.gray_400));
        MarginLayoutParams marginLayoutParams= new MarginLayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                1);
        marginLayoutParams.setMargins(100, 0, 100,0);
        LayoutParams llp = new LayoutParams((MarginLayoutParams) marginLayoutParams);
        this.setLayoutParams(llp);
    }
}
