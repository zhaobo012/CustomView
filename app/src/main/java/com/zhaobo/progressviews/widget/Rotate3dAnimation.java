package com.zhaobo.progressviews.widget;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by zhaobo on 17/3/23.
 */

public class Rotate3dAnimation extends Animation {
    private float mFromDegrees;
    private float mToDegrees;
    private float mCenterX;
    private float mCenterY;
    private float mDepthZ;
    private boolean mReverse;
    private float scale;

    private Camera mCamera;

    public Rotate3dAnimation(Context context,float mFromDegrees, float mToDegrees, float mCenterX, float mCenterY, float mDepthZ, boolean mReverse){
        this.mFromDegrees=mFromDegrees;
        this.mToDegrees=mToDegrees;
        this.mCenterX=mCenterX;
        this.mCenterY=mCenterY;
        this.mDepthZ=mDepthZ;
        this.mReverse=mReverse;
        scale=context.getResources().getDisplayMetrics().density;
    }


    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mCamera=new Camera();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float fromDegrees=mFromDegrees;
        float degrees=fromDegrees+(mToDegrees-fromDegrees)*interpolatedTime;
        float centerX=mCenterX;
        float centerY=mCenterY;
        Camera camera=mCamera;
        Matrix matrix=t.getMatrix();
        float[] values=new float[9];
        camera.save();
        if(mReverse){
            camera.translate(0f,0f,mDepthZ*interpolatedTime);
        }else{
            camera.translate(0f,0f,mDepthZ*(1-interpolatedTime));
        }
        camera.rotateY(degrees);
        camera.getMatrix(matrix);
        camera.restore();
        matrix.getValues(values);
        values[6]=values[6]/scale;
        values[7]=values[7]/scale;
        matrix.setValues(values);

        matrix.preTranslate(-centerX,-centerY);
        matrix.postTranslate(centerX,centerY);
    }
}
