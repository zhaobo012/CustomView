package com.zhaobo.progressviews.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 定义四个块来测试自定义view group的一些属性
 * Created by zhaobo on 17/3/28.
 */

public class CustomViewgroup extends ViewGroup {

    public CustomViewgroup(Context context) {
        this(context, null);
    }

    public CustomViewgroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        //主要是为了测试这个方法自定义的viewgroup需要提供layoutprams方法，就像Linelayout和RelativeLayout的layoutparams一样他们都有自己的专属性
        // 比如Linelayout的weiget了等等，这里只需要用到Margin，如果有别的需求可以自定义类继承layoutparams
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
        int tw = params.leftMargin + params.rightMargin;
        int bw = params.leftMargin + params.rightMargin;
        int lh = params.topMargin + params.bottomMargin;
        int rh = params.topMargin + params.bottomMargin;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (i == 0 || i == 1) {
                tw += child.getWidth();
            }
            if (i == 2 || i == 3) {
                bw += child.getWidth();
            }
            if (i == 0 || i == 2) {
                lh += child.getHeight();
            }
            if (i == 1 || i == 3) {
                rh += child.getHeight();
            }
        }
        int w = Math.max(tw, bw);
        int h = Math.max(lh, rh);
        //如果是精确模式的话将宽高直接设置成精确模式的值，否则设置成子视图的宽高
        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? width : w, heightMode == MeasureSpec.EXACTLY ? height : h);


    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        for (int j = 0; j < getChildCount(); j++) {

            View child = getChildAt(j);
            int cW = child.getMeasuredWidth();
            int cH = child.getMeasuredHeight();
            MarginLayoutParams childParams = (MarginLayoutParams) child.getLayoutParams();
            int cl = 0, ct = 0, cr = 0, cb = 0;
            switch (j) {
                case 0:
                    cl = childParams.leftMargin;
                    ct = childParams.topMargin;
                    cr = cW + cl;
                    cb = cH + ct;
                    break;
                case 1:
                    cl = getWidth() - childParams.rightMargin - cW;
                    ct = childParams.topMargin;
                    cr = cl + cW;
                    cb = cH + ct;
                    break;
                case 2:
                    cl = childParams.leftMargin;
                    ct = getHeight() - childParams.bottomMargin - cH;
                    cr = cW + cl;
                    cb = ct + cH;
                    break;
                case 3:
                    cl = getWidth() - childParams.rightMargin - cW;
                    ct = getHeight() - childParams.bottomMargin - cH;
                    cr = cl + cW;
                    cb = ct + cH;
                    break;
            }
            child.layout(cl, ct, cr, cb);

        }
    }
}
