package com.zhaobo.progressviews.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zhaobo.progressviews.R;
import com.zhaobo.progressviews.widget.ZoomImageView;

/**
 * Created by zhaobo on 17/3/4.
 */

public class ZoomImageViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoomimageview);
        ZoomImageView zoomImageView= (ZoomImageView) findViewById(R.id.zoomImageView);
        zoomImageView.setImageResource(R.drawable.kobe);
    }
}
