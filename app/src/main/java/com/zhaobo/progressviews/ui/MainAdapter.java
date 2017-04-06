package com.zhaobo.progressviews.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhaobo.progressviews.R;

import java.util.List;

/**
 * Created by zhaobo on 17/3/4.
 */

class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private List<String> datas;
    private LayoutInflater inflater;
    private OnRecycleViewItemClickListener listener;


    public MainAdapter(List<String> datas, Context context) {
        this.datas = datas;
        inflater = LayoutInflater.from(context);
    }

    public void setOnRecycleViewItemClickListener(OnRecycleViewItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.main_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, listener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv.setText(datas.get(position));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv;
        private OnRecycleViewItemClickListener listener;

        public ViewHolder(View view, OnRecycleViewItemClickListener listener) {
            super(view);
            tv = (TextView) view.findViewById(R.id.textView);
            this.listener = listener;
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (listener != null) {
                listener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    interface OnRecycleViewItemClickListener {
        void onItemClick(View view, int position);
    }
}
