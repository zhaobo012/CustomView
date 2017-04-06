package com.zhaobo.progressviews.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zhaobo on 17/3/13.
 */

public class FoldLayout extends ViewGroup {
    private static final int NUM_OF_POINT = 8;
    private Bitmap bitmap;
    private Canvas mCanvas = new Canvas();

    private float mFactor = 0.6f;
    private int mNumOfFolds = 8;

    private float mFlodWidth;
    private float mTranslateDisPerFlod;
    private float mTranslateDis;

    private Matrix[] mMatrix = new Matrix[mNumOfFolds];

    private Paint mSolidPaint;

    private Paint mShadowPaint;
    private Matrix mShadowGradientMatrix;
    private LinearGradient mShadowGradientShader;

    private GestureDetector mScrollGestureDetector;

    public FoldLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mSolidPaint = new Paint();
        mShadowPaint = new Paint();
        mShadowGradientMatrix = new Matrix();
        mShadowGradientShader = new LinearGradient(0f, 0.5f, 0f, 0f, Color.BLACK, Color.TRANSPARENT, Shader.TileMode.CLAMP);
        mShadowPaint.setShader(mShadowGradientShader);
        this.willNotDraw();

        mScrollGestureDetector = new GestureDetector(context,
                new ScrollGestureDetector());
    }

    public FoldLayout(Context context) {
        this(context, null);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        View child = getChildAt(0);
        measureChild(child, widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(child.getMeasuredWidth(), child.getMeasuredHeight());
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        View child = getChildAt(0);
        child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
        bitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        mCanvas.setBitmap(bitmap);
        updateFold();
    }

    private void updateFold() {
        int w = getMeasuredHeight();
        int h = getMeasuredHeight();

        mTranslateDis = w * mFactor;
        mFlodWidth = w / mNumOfFolds;
        mTranslateDisPerFlod = mTranslateDis / mNumOfFolds;

        int alpha = (int) (255 * (1 - mFactor));
        mSolidPaint.setColor(Color.argb((int) (alpha * 0.8F), 0, 0, 0));

        mShadowGradientMatrix.setScale(mFlodWidth, 1);
        mShadowGradientShader.setLocalMatrix(mShadowGradientMatrix);

        mShadowPaint.setAlpha(alpha);

        float depth = (float) (Math.sqrt(mFlodWidth * mFlodWidth
                - mTranslateDisPerFlod * mTranslateDisPerFlod) / 2);


        float[] src = new float[NUM_OF_POINT];
        float[] dst = new float[NUM_OF_POINT];

        for (int i = 0; i < mNumOfFolds; i++) {
            mMatrix[i] = new Matrix();
            src[0] = i * mFlodWidth;
            src[1] = 0;
            src[2] = (i + 1) * mFlodWidth;
            src[3] = 0;
            src[4] = i * mFlodWidth;
            src[5] = bitmap.getHeight();
            src[6] = (i + 1) * mFlodWidth;
            src[7] = bitmap.getHeight();
            boolean isEven = i % 2 == 0;

            dst[0] = i * mTranslateDisPerFlod;
            dst[1] = isEven ? 0 : depth;
            dst[2] = dst[0] + mTranslateDisPerFlod;
            dst[3] = isEven ? depth : 0;
            dst[4] = i * mTranslateDisPerFlod;
            dst[5] = isEven ? bitmap.getHeight() - depth : bitmap
                    .getHeight();
            ;
            dst[6] = dst[2];
            dst[7] = isEven ? bitmap.getHeight() : bitmap
                    .getHeight() - depth;

            mMatrix[i].setPolyToPoly(src, 0, dst, 0, src.length >> 1);

        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        return mScrollGestureDetector.onTouchEvent(event);
    }

    private boolean isReady;
    private int mTranslation=-1;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mTranslation == -1)
            mTranslation = w;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {

        if (mFactor == 0)
            return;
        if (mFactor == 1) {
            super.dispatchDraw(canvas);
            return;
        }
        for (int i = 0; i < mNumOfFolds; i++) {
            canvas.save();

            canvas.concat(mMatrix[i]);
            canvas.clipRect(mFlodWidth * i, 0, mFlodWidth * i + mFlodWidth,
                    getHeight());
            if (isReady) {
                canvas.drawBitmap(bitmap, 0, 0, null);
            } else {
                // super.dispatchDraw(canvas);
                super.dispatchDraw(mCanvas);
                canvas.drawBitmap(bitmap, 0, 0, null);
                isReady = true;
            }
            canvas.translate(mFlodWidth * i, 0);
            if (i % 2 == 0) {
                canvas.drawRect(0, 0, mFlodWidth, getHeight(), mSolidPaint);
            } else {
                canvas.drawRect(0, 0, mFlodWidth, getHeight(), mShadowPaint);
            }
            canvas.restore();
        }
    }

    public void setFactor(float factor) {
        this.mFactor = factor;
        updateFold();
        invalidate();
    }

    class ScrollGestureDetector extends GestureDetector.SimpleOnGestureListener
    {
        @Override
        public boolean onDown(MotionEvent e)
        {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY)
        {
            mTranslation -= distanceX;

            if (mTranslation < 0)
            {
                mTranslation = 0;
            }
            if (mTranslation > getWidth())
            {
                mTranslation = getWidth();
            }

            float factor = Math.abs(((float) mTranslation)
                    / ((float) getWidth()));

            setFactor(factor);

            return true;
        }
    }
}
