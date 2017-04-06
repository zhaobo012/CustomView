package com.zhaobo.progressviews.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zhaobo on 17/3/16.
 */

public class RemoteControlVIew extends View {
    private static final String TAG="ProgressView";
    private static final int DEFAULT=0;
    private static final int CENTER=1;
    private static final int TOP=2;
    private static final int BUTTOM=3;
    private static final int LEFT=4;
    private static final int RIGHT=5;

    private int curType;

    private int outterRadius, innerRadius, centerRadius;

    private int centerX, centerY;

    private int midButton;

    private Path mMidP;

    private Paint mForcePaint,mMidPaint;

    private Region center,top, bottom, left, right;;
    private Path centerCricle, topP, bottomP, leftP, rightP;
    private RectF bigCircle, smallCircle;
    //平移画布后重新映射触摸点
    private Matrix mMatrix;

    private OnButtonsClickLisenter listener;

    public RemoteControlVIew(Context context) {
        this(context, null);
    }

    public RemoteControlVIew(Context context, AttributeSet attrs) {
        super(context, attrs);
        initdatas();
    }

    private void initdatas() {
        mForcePaint = new Paint();
        mForcePaint.setAntiAlias(true);
        mForcePaint.setStyle(Paint.Style.FILL);
        mForcePaint.setColor(Color.BLUE);

        mMidPaint = new Paint();
        mMidPaint.setAntiAlias(true);
        mMidPaint.setStyle(Paint.Style.FILL);
        mMidPaint.setColor(Color.YELLOW);

        mMatrix=new Matrix();
    }

    public void setListener(OnButtonsClickLisenter listener){
        this.listener=listener;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2;
        centerY = h / 2;
        mMatrix.reset();
        int minLength = Math.min(w, h);
        outterRadius = (int) (0.8f * minLength) / 2;
        innerRadius = (int) (0.5f * minLength) / 2;

        centerRadius = (int) (0.3f * minLength) / 2;

        midButton=(outterRadius+innerRadius)/2;

        Region gloabalRegion = new Region(-w, -h, w, h);

        bigCircle = new RectF(-outterRadius, -outterRadius, outterRadius, outterRadius);
        smallCircle = new RectF(-innerRadius, -innerRadius, innerRadius, innerRadius);



        initPaths(gloabalRegion);

    }

    private void initPaths(Region gloabalRegion) {

        mMidP=new Path();
        mMidP.moveTo(-40,midButton+40);
        mMidP.lineTo(40,midButton+40);
        mMidP.lineTo(0,midButton-40);
        mMidP.close();

        centerCricle = new Path();
        center = new Region();
        centerCricle.addCircle(0, 0, centerRadius, Path.Direction.CCW);
        center.setPath(centerCricle, gloabalRegion);
        rightP = new Path();
        right=new Region();
        rightP.addArc(bigCircle,-40,80);
        rightP.arcTo(smallCircle,40,-80);
        rightP.close();
        right.setPath(rightP,gloabalRegion);

        topP = new Path();
        top=new Region();
        topP.addArc(bigCircle,50,80);
        topP.arcTo(smallCircle,130,-80);
        topP.close();
        top.setPath(topP,gloabalRegion);

        leftP = new Path();
        left=new Region();
        leftP.addArc(bigCircle,140,80);
        leftP.arcTo(smallCircle,220,-80);
        leftP.close();
        left.setPath(leftP,gloabalRegion);

        bottomP = new Path();
        bottom=new Region();
        bottomP.addArc(bigCircle,230,80);
        bottomP.arcTo(smallCircle,310,-80);
        bottomP.close();
        bottom.setPath(bottomP,gloabalRegion);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(centerX,centerY);
        canvas.scale(1f,-1f);
        if(mMatrix.isIdentity()){
            canvas.getMatrix().invert(mMatrix);
        }
        canvas.drawPath(centerCricle,mForcePaint);
        canvas.drawPath(rightP,mForcePaint);
        canvas.drawPath(topP,mForcePaint);
        canvas.drawPath(leftP,mForcePaint);
        canvas.drawPath(bottomP,mForcePaint);

        mForcePaint.setColor(Color.GRAY);
        if(curType==1){
            canvas.drawPath(centerCricle,mForcePaint);
        }else if(curType==2){
            canvas.drawPath(topP,mForcePaint);
        }
        else if(curType==3){
            canvas.drawPath(bottomP,mForcePaint);
        }
        else if(curType==4){
            canvas.drawPath(leftP,mForcePaint);
        }
        else if(curType==5){
            canvas.drawPath(rightP,mForcePaint);
        }

        mForcePaint.setColor(Color.BLUE);

        for (int i = 0; i <4 ; i++) {
            canvas.save();
            canvas.rotate(90f*i);
            canvas.drawPath(mMidP,mMidPaint);
            canvas.restore();
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float[] myTouchPoints=new float[2];
        myTouchPoints[0]=event.getX();
        myTouchPoints[1]=event.getY();
        mMatrix.mapPoints(myTouchPoints);
        int x=(int)myTouchPoints[0];
        int y=(int)myTouchPoints[1];

        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                getOntouchType(x,y);
                break;
            case MotionEvent.ACTION_MOVE:
                getOntouchType(x,y);
                break;
            case MotionEvent.ACTION_UP:
                getOntouchType(x,y);

                if(curType!=0&&listener!=null){
                    switch (curType){
                        case CENTER:
                            Log.i(TAG,"center");
                            listener.centerClick();
                            break;
                        case TOP:
                            listener.topClick();
                            break;
                        case BUTTOM:
                            listener.bottomClick();
                            break;
                        case LEFT:
                            listener.leftClick();
                            break;
                        case RIGHT:
                            listener.rightClick();
                            break;

                    }
                }
                curType=0;
                break;
            case MotionEvent.ACTION_CANCEL:
                curType=0;
                break;
        }

        invalidate();
        return true;
    }

    private void getOntouchType(int x,int y){

        if(center.contains(x,y)){
            curType=CENTER;

        }else if(top.contains(x,y)){
            curType=TOP;
        }else if(bottom.contains(x,y)){
            curType=BUTTOM;
        }else if(left.contains(x,y)){
            curType=LEFT;
        }else if(right.contains(x,y)){
            curType=RIGHT;
        }else {
            curType=DEFAULT;
        }
        Log.i(TAG,"curType:"+curType);
    }

    public interface OnButtonsClickLisenter{
        void centerClick();
        void topClick();
        void bottomClick();
        void leftClick();
        void rightClick();
    }
}
