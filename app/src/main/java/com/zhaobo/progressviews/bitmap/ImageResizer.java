package com.zhaobo.progressviews.bitmap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;

import java.io.FileDescriptor;

/**
 * Created by zhaobo on 17/2/18.
 */

public class ImageResizer {

    public Bitmap decodeSampledBitmapFromResourse(Resources resources, @DrawableRes int resId, int reqWidth, int reqHeight){
        BitmapFactory.Options options=new BitmapFactory.Options();
        //inJustDecodeBounds设置成true的话，并不会返回真正的bitmap节省了内存，回返回bitmap的宽高
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeResource(resources,resId,options);
        options.inSampleSize=calculateInSamPleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeResource(resources,resId,options);
    }

    public Bitmap decodeSampledBitmapFromFileDescriptor(FileDescriptor fd,int reqWidth,int reqHeight){
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFileDescriptor(fd,null,options);
        options.inSampleSize=calculateInSamPleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeFileDescriptor(fd,null,options);
    }


    public int calculateInSamPleSize(BitmapFactory.Options options,int reqWidth,int reqHeight){

        if(reqHeight<=0||reqWidth<=0){
            return 1;
        }
        int inSampleSize=1;

        int height=options.outHeight;
        int width=options.outWidth;
        if(height>reqHeight||width>reqWidth){
            int halfWidth=height/2;
            int halfHeight=height/2;
            while((halfWidth/inSampleSize)>=reqWidth&&(halfHeight/inSampleSize)>=reqHeight){
                inSampleSize*=2;
            }
        }

        return inSampleSize;
    }
}
