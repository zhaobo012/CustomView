package com.zhaobo.progressviews.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.zhaobo.progressviews.R;
import com.zhaobo.progressviews.base.BaseActivity;
import com.zhaobo.progressviews.ui.fragment.PermisionFragment;

public class PermisionActivity extends BaseActivity implements PermisionFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permision);
         FragmentManager manager=getSupportFragmentManager();
        PermisionFragment fragment=PermisionFragment.newInstance("","");
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.add(R.id.activity_permision,fragment);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        Toast.makeText(this,"done activity",Toast.LENGTH_SHORT).show();
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
