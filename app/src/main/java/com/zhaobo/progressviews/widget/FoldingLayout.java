package com.zhaobo.progressviews.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.zhaobo.progressviews.R;

/**
 * Created by zhaobo on 17/3/9.
 */

public class FoldingLayout extends View {
    private Bitmap bitmap;
    private Matrix mShadowGradientMatrix;
    //mShadowGradientShader
    private Paint mShadowPaint,mSolidPaint;
    private LinearGradient mShadowGradientShader;

    /**
     * 折叠后的总宽度与原图宽度的比例
     */
    private float mFactor = 0.8f;
    private int piece=8;
    private int mTotalFolderWidth;
    private int mPerPieceWidth,mFolderWidth;
    private Matrix[] mMatrix=new Matrix[piece];

    public FoldingLayout(Context context) {
        this(context, null);
    }

    public FoldingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tianai);

        mTotalFolderWidth= (int) (bitmap.getWidth()*mFactor);
        mPerPieceWidth=mTotalFolderWidth/piece;
        mFolderWidth=bitmap.getWidth()/piece;

        mSolidPaint = new Paint();
        int alpha = (int) (255 * mFactor * 0.8f) ;
        mSolidPaint
                .setColor(Color.argb((int) (alpha*0.8F), 0, 0, 0));

        mShadowPaint = new Paint();
        mShadowPaint.setStyle(Paint.Style.FILL);
        mShadowGradientShader = new LinearGradient(0, 0, 0.5f, 0,
                Color.BLACK, Color.TRANSPARENT, Shader.TileMode.CLAMP);
        mShadowPaint.setShader(mShadowGradientShader);
        mShadowGradientMatrix = new Matrix();
        mShadowGradientMatrix.setScale(mFolderWidth, 1);
        mShadowGradientShader.setLocalMatrix(mShadowGradientMatrix);
        mShadowPaint.setAlpha((int) (0.9*255));
        //mShadowGradientShader.setAlpha((int) (0.9*255));

        //纵轴减小的那个高度，用勾股定理计算下
        int depth = (int) Math.sqrt(mFolderWidth * mFolderWidth
                - mPerPieceWidth * mPerPieceWidth)/2;

        //转换点
        float[] src = new float[piece];
        float[] dst = new float[piece];
        for (int i = 0; i <piece ; i++) {
            mMatrix[i]=new Matrix();
            src[0]=i*mFolderWidth;
            src[1]=0;
            src[2]=(i+1)*mFolderWidth;
            src[3]=0;
            src[4]=i*mFolderWidth;
            src[5]=bitmap.getHeight();
            src[6]=(i+1)*mFolderWidth;
            src[7]=bitmap.getHeight();
            boolean isEven = i % 2 == 0;

            dst[0]=i*mPerPieceWidth;
            dst[1]=isEven ? 0 : depth;
            dst[2]=dst[0]+mPerPieceWidth;
            dst[3]=isEven ? depth : 0;
            dst[4]=i*mPerPieceWidth;
            dst[5]=isEven ? bitmap.getHeight() - depth : bitmap
                    .getHeight();;
            dst[6]=dst[2];
            dst[7]=isEven ? bitmap.getHeight(): bitmap
                    .getHeight() - depth ;

            mMatrix[i].setPolyToPoly(src,0,dst,0,src.length>>1);

        }



    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i <piece ; i++) {
            canvas.save();
            canvas.concat(mMatrix[i]);
            //控制显示的大小
            canvas.clipRect(mFolderWidth * i, 0, mFolderWidth * i + mFolderWidth,
                    bitmap.getHeight());
            canvas.drawBitmap(bitmap,0,0,null);
            canvas.translate(mFolderWidth * i, 0);
            if(i%2==0){
                canvas.drawRect(0,0,mFolderWidth,
                        bitmap.getHeight(),mSolidPaint);
            }else{
                canvas.drawRect(0,0,mFolderWidth,
                        bitmap.getHeight(),mShadowPaint);
            }

            canvas.restore();

        }


    }

    public void setmFactor(float mFactor){
        this.mFactor=mFactor;
    }
}
