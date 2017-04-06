package com.zhaobo.progressviews.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zhaobo.progressviews.R;
import com.zhaobo.progressviews.widget.PullDrawable;
import com.zhaobo.progressviews.widget.RefreshListView;
import com.zhaobo.progressviews.widget.Rotate3dAnimation;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MainAdapter adapter;
    private List<String> datas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView= (RecyclerView) findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));


        datas=new ArrayList<>();
        datas.add("贝赛尔曲线");
        datas.add("仿今日头条下拉动画");
        datas.add("自定义ListView的下拉和上拉");
        datas.add("自定义可缩放的ImageView");
        datas.add("侧滑的ListView");
        datas.add("Poly折叠菜单");
        datas.add("遥控器");
        datas.add("Camera实现3D");
        datas.add("自定义ViewGroup");
        datas.add("测试权限");
        adapter=new MainAdapter(datas,this);
        recyclerView.setAdapter(adapter);
        adapter.setOnRecycleViewItemClickListener(new MainAdapter.OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position){
                    case 0:
                        Intent bezierIntent=new Intent(MainActivity.this,BezierActivity.class);
                        startActivity(bezierIntent);
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this,TouTiaoActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this,PullToListViewActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(MainActivity.this,ZoomImageViewActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(MainActivity.this,SlideViewActivity.class));
                        break;
                    case 5:
                        startActivity(new Intent(MainActivity.this,PolyActivity.class));
                        break;
                    case 6:
                        startActivity(new Intent(MainActivity.this,RemoteControlActivity.class));
                        break;
                    case 7:
                        startActivity(new Intent(MainActivity.this,Rotate3DAnimationActivity.class));
                        break;
                    case 8:
                        startActivity(new Intent(MainActivity.this,CustomViewgroupActivity.class));
                        break;
                    case 9:
                        startActivity(new Intent(MainActivity.this,PermisionActivity.class));
                        break;
                }
            }
        });

    }



}
