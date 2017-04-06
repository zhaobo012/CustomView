package com.zhaobo.progressviews.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.zhaobo.progressviews.R;
import com.zhaobo.progressviews.widget.PullDrawable;

/**
 * Created by zhaobo on 17/3/4.
 */

public class TouTiaoActivity extends AppCompatActivity {

    private PullDrawable mPullDrawable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toutiao);
        ImageView imageView= (ImageView) findViewById(R.id.image);
        SeekBar seekBar= (SeekBar) findViewById(R.id.sb);
        mPullDrawable=new PullDrawable();
        imageView.setImageDrawable(mPullDrawable);
        seekBar.setMax(100);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mPullDrawable.update(i/100f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
