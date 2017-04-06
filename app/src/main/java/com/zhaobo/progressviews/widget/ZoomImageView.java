package com.zhaobo.progressviews.widget;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * Created by zhaobo on 17/3/4.
 */

public class ZoomImageView extends ImageView implements ViewTreeObserver.OnGlobalLayoutListener, ScaleGestureDetector.OnScaleGestureListener, View.OnTouchListener {

    private static final String TAG ="ZoomImageView" ;
    private boolean once = true;
    private float initScale = 1.0f;
    public static final float SCALE_MAX = 4.0f;
    public static final float SCALE_MID = 2.5f;
    private final Matrix mMatrix = new Matrix();
    private float[] mValues = new float[9];

    private int lastPointCount = 0;
    private boolean isCanDrag;

    private float mLastX, mLastY;
    private int tapSlop;
    private boolean isCheckRightAndLeft, isCheckTopAndButtom;

    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector mGestureDetector;

    private boolean isAutoScale;

    public ZoomImageView(Context context) {
        this(context, null);
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setScaleType(ScaleType.MATRIX);
        scaleGestureDetector = new ScaleGestureDetector(context, this);
        this.setOnTouchListener(this);
        tapSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mGestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                float x=e.getX();
                float y=e.getY();
                if(isAutoScale){
                    return true;
                }
                float currentScale=getScale();
                if(currentScale<SCALE_MID){
                    postDelayed(new AutoScaleRunnable(SCALE_MID,x,y),16);
                    isAutoScale=true;
                }else if(currentScale<SCALE_MAX){
                    postDelayed(new AutoScaleRunnable(SCALE_MAX,x,y),16);
                    isAutoScale=true;
                }else {
                    postDelayed(new AutoScaleRunnable(initScale,x,y),16);
                    isAutoScale=true;
                }
                return true;
            }
        });
    }


    public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        if (once) {
            once = false;
            Drawable drawable = getDrawable();
            if (drawable == null) {
                return;
            }

            int width = getWidth();
            int height = getHeight();

            int dw = drawable.getIntrinsicWidth();
            int dh = drawable.getIntrinsicHeight();

            float bi = dw * 1.0f / dh;
            float Bi = width * 1.0f / height;

            float scale = 1.0f;

            if (bi > Bi) {
                scale = width * 1.0f / dw;
            } else if (bi < Bi) {
                scale = height * 10f / dh;
            }
            initScale = scale;
            mMatrix.postTranslate((width - dw) / 2, (height - dh) / 2);
            mMatrix.postScale(scale, scale, width / 2, height / 2);
            setImageMatrix(mMatrix);

        }

    }


    private float getScale() {
        mMatrix.getValues(mValues);
        return mValues[Matrix.MSCALE_X];
    }

    /**
     * 获得图片的范围
     *
     * @return
     */
    private RectF getMatrixRectF() {
        Matrix matrix = mMatrix;
        RectF rectF = new RectF();

        Drawable drawable = getDrawable();
        if (drawable != null) {
            rectF.set(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            matrix.mapRect(rectF);
        }

        return rectF;
    }


    @Override
    public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
        if (getDrawable() == null)
            return true;
        float scale = getScale();
        float scaleFactor = scaleGestureDetector.getScaleFactor();

        if (scale < SCALE_MAX || scale > initScale) {
            if (scale * scaleFactor > SCALE_MAX) {
                scaleFactor = SCALE_MAX / scale;
            }
            if (scale * scaleFactor < initScale) {
                scaleFactor = initScale / scale;
            }
            mMatrix.postScale(scaleFactor, scaleFactor, scaleGestureDetector.getFocusX() / 2, scaleGestureDetector.getFocusY() / 2);
            checkBorderAndCenterWhenScale();
            setImageMatrix(mMatrix);
        }

        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (mGestureDetector.onTouchEvent(motionEvent))
            return true;
        scaleGestureDetector.onTouchEvent(motionEvent);
        float x = 0, y = 0;
        int pointerCount = motionEvent.getPointerCount();
        for (int i = 0; i < pointerCount; i++) {
            x += motionEvent.getX(i);
            y += motionEvent.getY(i);
        }
        x = x / pointerCount;
        y = y / pointerCount;
        //移动过程中手指数目变化，置成不能移动
        if (pointerCount != lastPointCount) {
            isCanDrag = false;
            mLastX = x;
            mLastY = y;
        }
        lastPointCount = pointerCount;
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float dx = x - mLastX;
                float dy = y - mLastY;

                if (!isCanDrag) {
                    isCanDrag = isCanDrag(dx, dy);
                }
                if (isCanDrag) {

                    RectF rectF = getMatrixRectF();
                    if (getDrawable() != null) {
                        isCheckRightAndLeft = isCheckTopAndButtom = true;
                        if (rectF.width() < getWidth()) {
                            dx = 0;
                            isCheckRightAndLeft = false;
                        }
                        if (rectF.height() < getHeight()) {
                            dy = 0;
                            isCheckTopAndButtom = false;
                        }
                        Log.e(TAG, "ACTION_MOVE:dx"+dx+" dy:"+dy);
                        mMatrix.postTranslate(dx, dy);
                        checkDragBounds();
                        setImageMatrix(mMatrix);
                    }
                }
                mLastX = x;
                mLastY = y;

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                lastPointCount = 0;
                break;
        }

        return true;
    }

    private void checkDragBounds() {
        RectF rectF = getMatrixRectF();
        float dx = 0, dy = 0;
        int width = getWidth();
        int height = getHeight();
        if (rectF.top > 0 && isCheckTopAndButtom) {
            dy = -rectF.top;
        }
        if (rectF.bottom < height && isCheckTopAndButtom) {
            dy = height - rectF.bottom;
        }
        if (rectF.left > 0 && isCheckRightAndLeft) {
            dx = -rectF.left;
        }
        if (rectF.right < width && isCheckRightAndLeft) {
            dx = width - rectF.right;
        }
        mMatrix.postTranslate(dx, dy);
    }

    private boolean isCanDrag(float dx, float dy) {
//        return Math.sqrt(dx * dx + dy * dy) > tapSlop;
       // Log.i(TAG, "isCanDrag: "+Math.sqrt((dx * dx) + (dy * dy))+" ---tapSlop "+tapSlop);
        return Math.sqrt((dx * dx) + (dy * dy)) >= tapSlop;
    }

    private void checkBorderAndCenterWhenScale() {
        RectF rectF = getMatrixRectF();
        float deltaX = 0;
        float deltaY = 0;

        int width = getWidth();
        int height = getHeight();
        if (rectF.width() >= width) {
            if (rectF.left > 0) {
                deltaX = -rectF.left;
            }
            if (rectF.right < width) {
                deltaY = width - rectF.right;
            }
        }

        if (rectF.height() > height) {
            if (rectF.top > 0) {
                deltaY = -rectF.top;
            }
            if (rectF.bottom < height) {
                deltaY = height - rectF.bottom;
            }
        }

        if (rectF.width() < width) {
            deltaX = width * 1f / 2 - rectF.right + rectF.width() / 2;
        }
        if (rectF.height() < height) {
            deltaY = height * 0.5f - rectF.bottom + rectF.height() / 2;
        }

        mMatrix.postTranslate(deltaX, deltaY);
//        setImageMatrix(mMatrix);
    }

    class AutoScaleRunnable implements Runnable{
        static final float BIGGER = 1.07f;
        static final float SMALLER = 0.93f;
        private float mTargetScale;
        private float x,y;
        private float tempScale=1.0f;

        public AutoScaleRunnable(float mTargetScale, float x, float y) {
            this.mTargetScale = mTargetScale;
            this.x = x;
            this.y = y;
            if(getScale()<mTargetScale){
                tempScale=BIGGER;
            }else{
                tempScale=SMALLER;
            }
        }

        @Override
        public void run() {
            mMatrix.postScale(tempScale,tempScale,x,y);
            checkBorderAndCenterWhenScale();
            setImageMatrix(mMatrix);

            final float currentScale = getScale()+0.01f;
            //如果值在合法范围内，继续缩放
            if (((tempScale > 1f) && (currentScale < mTargetScale))
                    || ((tempScale < 1f) && (mTargetScale < currentScale)))
            {
                ZoomImageView.this.postDelayed(this, 16);
            } else//设置为目标的缩放比例
            {
                float deltaScale = mTargetScale / currentScale+0.01f;
                mMatrix.postScale(deltaScale, deltaScale, x, y);
                checkBorderAndCenterWhenScale();
                setImageMatrix(mMatrix);
                isAutoScale = false;
            }
        }
    }


}
