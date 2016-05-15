package com.jiahuaandroid.tools;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jiahuaandroid.basetools.adapter.OnRecyclerItemClickListener;
import com.jiahuaandroid.basetools.utils.CUtils;
import com.jiahuaandroid.sample.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ArrayList<String> data;
    private TestAdapter adapter;
    private LinearLayoutManager llmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        data = new ArrayList<>();
        llmanager = new LinearLayoutManager(this);
        binding.mainRecycler.setLayoutManager(llmanager);
        adapter = new TestAdapter(data);
        binding.mainRecycler.setAdapter(adapter);
        initdata();
        initListener();
    }

    private void initListener() {
        binding.mainRecycler.addOnItemTouchListener(new OnRecyclerItemClickListener(binding.mainRecycler) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                CUtils.showMsg("单击" + String.valueOf(vh.getAdapterPosition()));
            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder vh) {
                CUtils.showMsg("长按"+String.valueOf(vh.getAdapterPosition()));
            }
        });
    }


    private void initdata() {
        data.add("test");
        data.add("test");
        data.add("test");
        data.add("test");
        data.add("test");
        data.add("test");
        data.add("test");
        data.add("test");
        data.add("test");
        data.add("test");
        data.add("test");
        data.add("test");
        data.add("test");
        data.add("test");
        data.add("test");
        data.add("test");
        data.add("test");
        data.add("test");
        data.add("test");
        data.add("test");
        data.add("test");
        data.add("test");
        data.add("test");

        adapter.notifyDataSetChanged();
    }
}
