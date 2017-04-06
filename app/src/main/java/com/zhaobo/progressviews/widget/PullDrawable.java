package com.zhaobo.progressviews.widget;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

/**
 * Created by zhaobo on 17/2/20.
 */

public class PullDrawable extends Drawable {

    private Paint mPaint;
    private Paint mInnerPaint;

    private Path mBorderDstPath;
    private Path mBorderPath;
    private PathMeasure mBorderPathMeasure;

    private Path mRectDstPath;
    private Path mRectPath;
    private PathMeasure mRectPathMeasure;

    private Path mLine1DstPath;
    private Path mLine1Path;
    private PathMeasure mLine1PathMeasure;

    private Path mLine2DstPath;
    private Path mLine2Path;
    private PathMeasure mLine2PathMeasure;

    private Path mLine3DstPath;
    private Path mLine3Path;
    private PathMeasure mLine3PathMeasure;

    private Path mLine4DstPath;
    private Path mLine4Path;
    private PathMeasure mLine4PathMeasure;

    private Path mLine5DstPath;
    private Path mLine5Path;
    private PathMeasure mLine5PathMeasure;

    private Path mLine6DstPath;
    private Path mLine6Path;
    private PathMeasure mLine6PathMeasure;

    public PullDrawable() {
        initPaint();
    }

    private void initPaint(){
        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(0xffcccccc);
        mPaint.setStrokeWidth(4f);

        mInnerPaint=new Paint();
        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setStyle(Paint.Style.STROKE);
        mInnerPaint.setColor(0xffcccccc);
        mInnerPaint.setStrokeWidth(6f);
    }

    private void initPath(Rect bounds){
        int w=bounds.width();
        int h=bounds.height();
        int r=(int)(w*2/15f);
        int p=(int)(r/3f);
        mBorderDstPath=new Path();
        mBorderPath=new Path();
        mBorderPath.moveTo(w-p,p+r);
        RectF rectFRightTop=new RectF(w-p-2*r,p,w-p,p+2*r);
        mBorderPath.arcTo(rectFRightTop,0,-90);
        mBorderPath.lineTo(p+r,p);
        RectF rectFLeftTop=new RectF(p,p,p+2*r,p+2*r);
        mBorderPath.arcTo(rectFLeftTop,-90,-90);
        mBorderPath.lineTo(p,h-p-r);
        RectF rectFLeftButtom=new RectF(p,h-p-r*2,p+2*r,h-p);
        mBorderPath.arcTo(rectFLeftButtom,-180,-90);
        mBorderPath.lineTo(w-p-r,h-p);
        RectF rectFRightButtom=new RectF(w-p-r*2,h-p-r*2,w-p,h-p);
        mBorderPath.arcTo(rectFRightButtom,-270,-90);
        mBorderPath.lineTo(w-p,p+r);

        mBorderPathMeasure=new PathMeasure(mBorderPath,true);

        float d=p;
        float xp=0.8f*r;
        float yp=0.8f*r;
        mRectDstPath=new Path();
        mRectPath=new Path();
        mRectPath.moveTo(w/2f-p/2f,p+yp);
        mRectPath.lineTo(p+xp,p+yp);
        mRectPath.lineTo(p+xp,(h-2*p-2*yp)*0.4f+p+yp);
        mRectPath.lineTo(w/2f-p/2f,(h-2*p-2*yp)*0.4f+p+yp);
        mRectPathMeasure=new PathMeasure(mRectPath,true);

        mLine1DstPath=new Path();
        mLine1Path=new Path();
        mLine1Path.moveTo(w/2f+d/2f,p+xp);
        mLine1Path.lineTo(w-p-xp,p+xp);
        mLine1PathMeasure=new PathMeasure(mLine1Path,true);

        mLine2DstPath=new Path();
        mLine2Path=new Path();
        mLine2Path.moveTo(w/2f+d/2f,(h-2*p-2*yp)*0.2f+p+yp);
        mLine2Path.lineTo(w-p-xp,(h-2*p-2*yp)*0.2f+p+yp);
        mLine2PathMeasure=new PathMeasure(mLine2Path,true);

        mLine3DstPath=new Path();
        mLine3Path=new Path();
        mLine3Path.moveTo(w/2f+d/2f,(h-2*p-2*yp)*0.4f+p+yp);
        mLine3Path.lineTo(w-p-xp,(h-2*p-2*yp)*0.4f+p+yp);
        mLine3PathMeasure=new PathMeasure(mLine3Path,true);

        mLine4DstPath=new Path();
        mLine4Path=new Path();
        mLine4Path.moveTo(p+xp,(h-2*p-2*yp)*0.6f+p+yp);
        mLine4Path.lineTo(w-p-xp,(h-2*p-2*yp)*0.6f+p+yp);
        mLine4PathMeasure=new PathMeasure(mLine4Path,true);

        mLine5DstPath=new Path();
        mLine5Path=new Path();
        mLine5Path.moveTo(p+xp,(h-2*p-2*yp)*0.8f+p+yp);
        mLine5Path.lineTo(w-p-xp,(h-2*p-2*yp)*0.8f+p+yp);
        mLine5PathMeasure=new PathMeasure(mLine5Path,true);

        mLine6DstPath=new Path();
        mLine6Path=new Path();
        mLine6Path.moveTo(p+xp,(h-2*p-2*yp)*1f+p+yp);
        mLine6Path.lineTo(w-p-xp,(h-2*p-2*yp)*1f+p+yp);
        mLine6PathMeasure=new PathMeasure(mLine6Path,true);

    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        initPath(bounds);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawPath(mBorderDstPath,mPaint);
        canvas.drawPath(mRectDstPath,mInnerPaint);
        canvas.drawPath(mLine1DstPath,mInnerPaint);
        canvas.drawPath(mLine2DstPath,mInnerPaint);
        canvas.drawPath(mLine3DstPath,mInnerPaint);
        canvas.drawPath(mLine4DstPath,mInnerPaint);
        canvas.drawPath(mLine5DstPath,mInnerPaint);
        canvas.drawPath(mLine6DstPath,mInnerPaint);

    }

    @Override
    public void setAlpha(int i) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public void update(float percent){
        mBorderDstPath.reset();
        mBorderPathMeasure.getSegment(0,percent*mBorderPathMeasure.getLength(),mBorderDstPath,true);

        float rectLength=mRectPathMeasure.getLength();
        float line1Length=mLine1PathMeasure.getLength();
        float line2Length=mLine2PathMeasure.getLength();
        float line3Length=mLine3PathMeasure.getLength();
        float line4Length=mLine4PathMeasure.getLength();
        float line5Length=mLine5PathMeasure.getLength();
        float line6Length=mLine6PathMeasure.getLength();

        float mTotalLength=rectLength+line1Length+line2Length+line3Length+line4Length+line5Length+line6Length;

        float pRect=rectLength/mTotalLength;
        float pLine1=line1Length/mTotalLength+pRect;
        float pLine2=line2Length/mTotalLength+pLine1;
        float pLine3=line3Length/mTotalLength+pLine2;
        float pLine4=line4Length/mTotalLength+pLine3;
        float pLine5=line5Length/mTotalLength+pLine4;
        float pLine6=line6Length/mTotalLength+pLine5;

        mRectDstPath.reset();
        mRectPathMeasure.getSegment(0,rectLength*(percent/pRect),mRectDstPath,true);
        mLine1DstPath.reset();
        mLine1PathMeasure.getSegment(0,line1Length*((percent-pRect)/(pLine1-pRect)),mLine1DstPath,true);
        mLine2DstPath.reset();
        mLine2PathMeasure.getSegment(0,line2Length*((percent-pLine1)/(pLine2-pLine1)),mLine2DstPath,true);
        mLine3DstPath.reset();
        mLine3PathMeasure.getSegment(0,line3Length*((percent-pLine2)/(pLine3-pLine2)),mLine3DstPath,true);
        mLine4DstPath.reset();
        mLine4PathMeasure.getSegment(0,line4Length*((percent-pLine3)/(pLine4-pLine3)),mLine4DstPath,true);
        mLine5DstPath.reset();
        mLine5PathMeasure.getSegment(0,line5Length*((percent-pLine4)/(pLine5-pLine4)),mLine5DstPath,true);
        mLine6DstPath.reset();
        mLine6PathMeasure.getSegment(0,line6Length*((percent-pLine5)/(pLine6-pLine5)),mLine6DstPath,true);

        invalidateSelf();

    }
    public void clear(){
        mBorderDstPath.reset();
        mRectDstPath.reset();
        mLine1DstPath.reset();
        mLine2DstPath.reset();
        mLine3DstPath.reset();
        mLine4DstPath.reset();
        mLine5DstPath.reset();
        mLine6DstPath.reset();

        invalidateSelf();
    }
}
