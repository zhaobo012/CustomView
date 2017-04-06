package com.zhaobo.progressviews.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhaobo.progressviews.R;
import com.zhaobo.progressviews.widget.RefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaobo on 17/2/23.
 */

public class PullToListViewActivity extends AppCompatActivity {
    private List<String> datas;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pulltorefresh);
        RefreshListView listView= (RefreshListView) findViewById(R.id.listview);
        initDatas();
        listView.setAdapter(new MyAdapter());
    }

    private void initDatas(){
        datas=new ArrayList<>();
        for (int i = 0; i <20 ; i++) {
            datas.add("this is "+i) ;
        }
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int i) {
            return datas.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if(view==null){
                view= LayoutInflater.from(PullToListViewActivity.this).inflate(R.layout.list_item,viewGroup,false);
                viewHolder=new ViewHolder();
                viewHolder.tv= (TextView) view.findViewById(R.id.textView);
                view.setTag(viewHolder);
            }else{
                viewHolder= (ViewHolder) view.getTag();
            }
            viewHolder.tv.setText(datas.get(i));
            return view;
        }

        class ViewHolder {
            TextView tv;
        }

    }

}
