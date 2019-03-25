package com.xing.xfermode滤镜;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.xing.R;

public class XfermodeEaserView extends View {

    private Paint mPaint;
    private Path mPath;
    private Bitmap mTextBitmap;
    private Bitmap mSrcBitmap;
    private Bitmap mDestBitmap;

    public XfermodeEaserView(Context context) {
        this(context, null);
    }

    public XfermodeEaserView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XfermodeEaserView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(80);

        setLayerType(LAYER_TYPE_SOFTWARE, null);

        mTextBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.result);
        mSrcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.easer);

        mDestBitmap = Bitmap.createBitmap(mSrcBitmap.getWidth(), mSrcBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        mTextBitmap = Bitmap.createBitmap(mTextBitmap, 0, 0, mSrcBitmap.getWidth(), mSrcBitmap.getHeight());
        mPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(mTextBitmap, 0, 0, mPaint);

        //使用离屏绘制
        int layerId = canvas.saveLayer(0, 0, getWidth(), getHeight(), mPaint, Canvas.ALL_SAVE_FLAG);


        //先将路径绘制到bitmap上
        Canvas dstCanvas = new Canvas(mDestBitmap);
        dstCanvas.drawPath(mPath, mPaint);

        //绘制 目标图像
        canvas.drawBitmap(mDestBitmap, 0, 0, mPaint);
        //设置 模式为SRC_OUT,擦橡皮区域为交集区域需要清除掉像素
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        //绘制源图像
        canvas.drawBitmap(mSrcBitmap, 0, 0, mPaint);

        mPaint.setXfermode(null);
        canvas.restoreToCount(layerId);
    }

    private float mEventX, mEventY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mEventX = event.getX();
                mEventY = event.getY();
                mPath.moveTo(mEventX, mEventY);
                break;
            case MotionEvent.ACTION_MOVE:
                float endX = (event.getX() - mEventX) / 2 + mEventX;
                float endY = (event.getY() - mEventY) / 2 + mEventY;

                mPath.quadTo(mEventX, mEventY, endX, endY);
                mEventX = event.getX();
                mEventY = event.getY();
                break;
        }
        invalidate();
        //消费事件
        return true;
    }
}
