package com.zhaobo.progressviews.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.zhaobo.progressviews.R;
import com.zhaobo.progressviews.base.BaseActivity;
import com.zhaobo.progressviews.widget.RemoteControlVIew;

/**
 * Created by zhaobo on 17/3/17.
 */

public class RemoteControlActivity extends BaseActivity implements RemoteControlVIew.OnButtonsClickLisenter {
    private static final String TAG="ProgressViews";
    private RemoteControlVIew remoteControlView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_control);
        remoteControlView= (RemoteControlVIew) findViewById(R.id.remoteControlView);
        remoteControlView.setListener(this);
    }

    @Override
    public void centerClick() {
        Toast.makeText(RemoteControlActivity.this,"center",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void topClick() {
        Toast.makeText(RemoteControlActivity.this,"top",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void bottomClick() {
        Toast.makeText(RemoteControlActivity.this,"bottom",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void leftClick() {
        Toast.makeText(RemoteControlActivity.this,"left",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void rightClick() {
        Toast.makeText(RemoteControlActivity.this,"right",Toast.LENGTH_SHORT);
    }
}
