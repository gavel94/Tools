package com.jiahuaandroid.tools;

import android.view.View;
import android.view.ViewGroup;

import com.jiahuaandroid.basetools.adapter.BaseRecyclerViewAdapter;

import java.util.List;

/**
 * Created by jhhuang on 2016/5/13.
 * QQ:781913268
 * 作用：xxx
 */
public class TestAdapter extends BaseRecyclerViewAdapter<String,TestAdapter.MyViewHolder> {
    /**
     * @param list the datas to attach the adapter
     */
    public TestAdapter(List<String> list) {
        super(list);
    }

    @Override
    protected void bindDataToItemView(MyViewHolder myViewHolder, String item) {
        myViewHolder.setText(android.R.id.text1,item+myViewHolder.getLayoutPosition());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(inflateItemView(parent,android.R.layout.activity_list_item));
    }

    public class MyViewHolder extends BaseRecyclerViewAdapter.SparseArrayViewHolder {
        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
