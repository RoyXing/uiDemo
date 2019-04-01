package com.xing.屏幕适配;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.xing.R;

public class PercentRelativeLayout extends RelativeLayout {

    public PercentRelativeLayout(Context context) {
        super(context);
    }

    public PercentRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PercentRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
            if (checkLayoutParams(layoutParams)) {
                LayoutParams params = (LayoutParams) layoutParams;
                float percentWidth = params.percentWidth;
                float percentHeight = params.percentHeight;
                float percentMarginLeft = params.percentMarginLeft;
                float percentMarginRight = params.percentMarginRight;
                float percentMarginTop = params.percentMarginTop;
                float percentMarginBottom = params.percentMarginBottom;

                if (percentWidth > 0) {
                    params.width = (int) (widthSize * percentWidth);
                }
                if (percentHeight > 0) {
                    params.height = (int) (heightSize * percentHeight);
                }
                if (percentMarginLeft > 0) {
                    params.leftMargin = (int) (widthSize * percentMarginLeft);
                }
                if (percentMarginRight > 0) {
                    params.rightMargin = (int) (widthSize * percentMarginRight);
                }
                if (percentMarginTop > 0) {
                    params.topMargin = (int) (heightSize * percentMarginRight);
                }
                if (percentMarginBottom > 0) {
                    params.bottomMargin = (int) (heightSize * percentMarginRight);
                }
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    public RelativeLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    public class LayoutParams extends RelativeLayout.LayoutParams {

        public float percentWidth;
        public float percentHeight;
        public float percentMarginLeft;
        public float percentMarginRight;
        public float percentMarginTop;
        public float percentMarginBottom;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray array = c.obtainStyledAttributes(attrs, R.styleable.PercentLayout);
            percentWidth = array.getFloat(R.styleable.PercentLayout_percent_width, 0);
            percentHeight = array.getFloat(R.styleable.PercentLayout_percent_height, 0);
            percentMarginLeft = array.getFloat(R.styleable.PercentLayout_percentMarginLeft, 0);
            percentMarginRight = array.getFloat(R.styleable.PercentLayout_percentMarginRight, 0);
            percentMarginTop = array.getFloat(R.styleable.PercentLayout_percentMarginTop, 0);
            percentMarginBottom = array.getFloat(R.styleable.PercentLayout_percentMarginBottom, 0);
            array.recycle();
        }
    }
}
