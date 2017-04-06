package com.zhaobo.progressviews.ui;

import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.zhaobo.progressviews.R;
import com.zhaobo.progressviews.base.BaseActivity;
import com.zhaobo.progressviews.widget.Rotate3dAnimation;

/**
 * Created by zhaobo on 17/3/23.
 */

public class Rotate3DAnimationActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rotate3danimation);
        ImageView view = (ImageView) findViewById(R.id.imageview);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float centerX = view.getWidth() / 2f;
                float centerY = view.getHeight() / 2f;

                Rotate3dAnimation animation = new Rotate3dAnimation(Rotate3DAnimationActivity.this, 0f, 180f, centerX, centerY, -500f, true);
                animation.setDuration(3000);
                animation.setFillAfter(true);
                animation.setInterpolator(new LinearInterpolator());
                view.startAnimation(animation);


            }
        });
    }
}
