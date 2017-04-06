package com.zhaobo.progressviews.widget;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

/**
 * Created by zhaobo on 17/3/27.
 */

public class Custom3DView extends ViewGroup {
    private Camera mCamera;
    private Matrix mMatrix;

    private Scroller mScroller;

    private int mStartScreen = 1;//item start
    private int mHeight = 0;// view height

    // 旋转的角度，可以进行修改来观察效果
    private float angle = 90;
    //三种状态
    private static final int STATE_PRE = 0;
    private static final int STATE_NEXT = 1;
    private static final int STATE_NORMAL = 2;
    private int STATE = -1;

    public Custom3DView(Context context) {
        this(context, null);
    }

    public Custom3DView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mCamera = new Camera();
        mMatrix = new Matrix();
        if (mScroller == null) {
            mScroller = new Scroller(context, new LinearInterpolator());
        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(measureWidth, measureHeight);
        MarginLayoutParams layoutParams = (MarginLayoutParams) getLayoutParams();

        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(measureWidth - (layoutParams.leftMargin + layoutParams.rightMargin), MeasureSpec.EXACTLY);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(measureHeight - (layoutParams.topMargin + layoutParams.bottomMargin), MeasureSpec.EXACTLY);
        measureChildren(childWidthMeasureSpec, childHeightMeasureSpec);
        getMeasuredHeight();
        mHeight = getMeasuredHeight();
        scrollTo(0, mStartScreen * mHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        MarginLayoutParams params = (MarginLayoutParams) this.getLayoutParams();
        int childCount = getChildCount();
        int childTop = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (i == 0) {
                childTop += params.topMargin;
            }
            if (child.getVisibility() != View.GONE) {
                child.layout(left + params.leftMargin, childTop, right - params.rightMargin, child.getMeasuredHeight());
                childTop += child.getMeasuredHeight();
            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {

        for (int i = 0; i < getChildCount(); i++) {
            drawScreen(canvas, i, getDrawingTime());
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());

        }
    }

    private void drawScreen(Canvas canvas, int screen, long drawingTime) {
        int height = getHeight();
        int scrollHeight = screen * height;
        int scrollY = this.getScrollY();

        // 偏移量不足的时
        if (scrollHeight > scrollY + height || scrollHeight + height < scrollY) {
            return;
        }
        final View child = getChildAt(screen);
        final int faceIndex = screen;
        final float currentDegree = getScrollY() * (angle / getMeasuredHeight());
        final float faceDegree = currentDegree - faceIndex * angle;
        if (faceDegree > 90 || faceDegree < -90) {
            return;
        }
        final float centerY = (scrollHeight < scrollY) ? scrollHeight + height
                : scrollHeight;
        final float centerX = getWidth() / 2;
        final Camera camera = mCamera;
        final Matrix matrix = mMatrix;
        canvas.save();
        mCamera.save();
        camera.rotateX(faceDegree);
        camera.getMatrix(matrix);

        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
        canvas.concat(matrix);
        drawChild(canvas, getChildAt(screen), drawingTime);
        camera.restore();


    }
}
