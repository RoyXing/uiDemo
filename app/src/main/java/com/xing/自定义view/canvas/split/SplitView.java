package com.xing.自定义view.canvas.split;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.xing.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author roy.xing
 * @date 2019/3/25
 */
public class SplitView extends View {

    private Paint mPaint;
    private Bitmap bitmap;
    //粒子直径
    private float d = 3;
    private ValueAnimator mAnimator;
    private List<Ball> mBalls = new ArrayList<>();

    public SplitView(Context context) {
        this(context, null);
    }

    public SplitView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SplitView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mPaint = new Paint();
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.split_pic);
        for (int i = 0; i < bitmap.getWidth(); i++) {
            for (int j = 0; j < bitmap.getHeight(); j++) {
                Ball ball = new Ball();
                ball.color = bitmap.getPixel(i, j);
                ball.x = i * d + d / 2;
                ball.y = j * d + d / 2;
                ball.r = d / 2;

                ball.vX = (float) (Math.pow(-1, Math.ceil(Math.random() * 1000)) * 20 * Math.random());
                ball.vY = rangInt(i, j);

                ball.aX = 0;
                ball.aY = 0.98f;
                mBalls.add(ball);
            }
        }

        mAnimator = ValueAnimator.ofFloat(0, 1);
        mAnimator.setRepeatCount(-1);
        mAnimator.setDuration(2000);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                updateBall();
                invalidate();
            }
        });
    }

    private void updateBall() {
        for (Ball ball : mBalls) {
            ball.x += ball.vX;
            ball.y += ball.vY;

            ball.vX += ball.aX;
            ball.vY += ball.aY;
        }
    }

    private int rangInt(int i, int j) {
        int max = Math.max(i, j);
        int min = Math.min(i, j) - 1;
        //在0到(max - min)范围内变化，取大于x的最小整数 再随机
        return (int) (min + Math.ceil(Math.random() * (max - min)));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(500, 500);
        for (Ball ball : mBalls) {
            mPaint.setColor(ball.color);
            canvas.drawCircle(ball.x, ball.y, ball.r, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //执行动画
            mAnimator.start();
        }
        return super.onTouchEvent(event);
    }
}
