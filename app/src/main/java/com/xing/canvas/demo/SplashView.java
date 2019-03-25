package com.xing.canvas.demo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import com.xing.R;

public class SplashView extends View {

    //旋转圆的画笔
    private Paint mPaint;
    //扩散圆的画笔
    private Paint mHolePaint;

    private ValueAnimator valueAnimator;

    private int mBackgroundColor = Color.WHITE;
    private int[] mCircleColors;

    private float centerX;
    private float centerY;

    //表示斜对角线长度的一半，扩散圆最大半径
    private float distance;

    //6个小球的半径
    private float circleRadius = 18;
    //旋转大圆的半径
    private float rotateRadius = 90;

    //当前大圆的旋转角度
    private float currentRotateAngle = 0F;
    //当前大圆的半径
    private float currentRotateRadius = rotateRadius;
    //扩散圆的半径
    private float currentHoleRadius = 0F;
    //标示旋转动画的时长
    private int rotateDuration = 1200;

    private SplashState state;

    public SplashView(Context context) {
        this(context, null);
    }

    public SplashView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SplashView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mHolePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHolePaint.setStyle(Paint.Style.STROKE);
        mHolePaint.setColor(mBackgroundColor);

        mCircleColors = getResources().getIntArray(R.array.splash_circle_colors);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w >> 1;
        centerY = h >> 1;
        distance = (float) (Math.hypot(w, h) / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (state == null) {
            state = new RotateState();
        }
        state.drawState(canvas);
    }

    private void drawCircles(Canvas canvas) {
        float rotateAngle = (float) (Math.PI * 2 / mCircleColors.length);

        for (int i = 0; i < mCircleColors.length; i++) {
            float angle = i * rotateAngle + currentRotateAngle;
            float cx = (float) (Math.cos(angle) * currentRotateRadius + centerX);
            float cy = (float) (Math.sin(angle) * currentRotateRadius + centerY);
            mPaint.setColor(mCircleColors[i]);
            canvas.drawCircle(cx, cy, circleRadius, mPaint);
        }
    }

    private void drawBackground(Canvas canvas) {
        if (currentHoleRadius > 0) {
            float strokeWidth = distance - currentHoleRadius;
            float radius = strokeWidth / 2 + currentHoleRadius;
            mHolePaint.setStrokeWidth(strokeWidth);
            canvas.drawCircle(centerX, centerY, radius, mHolePaint);
        } else {
            canvas.drawColor(mBackgroundColor);
        }
    }

    private abstract class SplashState {
        abstract void drawState(Canvas canvas);
    }

    private class RotateState extends SplashState {

        public RotateState() {
            valueAnimator = ValueAnimator.ofFloat(0, (float) (Math.PI * 2));
            valueAnimator.setRepeatCount(2);
            valueAnimator.setDuration(rotateDuration);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    currentRotateAngle = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    state = new MerginState();
                }
            });
            valueAnimator.start();
        }

        @Override
        void drawState(Canvas canvas) {
            drawBackground(canvas);
            drawCircles(canvas);
        }
    }

    //扩散聚合
    private class MerginState extends SplashState {

        public MerginState() {
            valueAnimator = ValueAnimator.ofFloat(circleRadius, rotateRadius);
            valueAnimator.setDuration(rotateDuration);
            valueAnimator.setInterpolator(new OvershootInterpolator(10f));
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    currentRotateRadius = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });

            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    state = new ExpandState();
                }
            });
            valueAnimator.reverse();
        }

        @Override
        void drawState(Canvas canvas) {
            drawBackground(canvas);
            drawCircles(canvas);
        }
    }

    private class ExpandState extends SplashState {

        public ExpandState() {
            valueAnimator = ValueAnimator.ofFloat(circleRadius, distance);
            valueAnimator.setDuration(rotateDuration);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    currentHoleRadius = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            valueAnimator.start();
        }

        @Override
        void drawState(Canvas canvas) {
            drawBackground(canvas);
        }
    }

}
