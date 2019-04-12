package com.chad.baserecyclerviewadapterhelper;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.chad.baserecyclerviewadapterhelper.adapter.ExpandableItemAdapter;
import com.chad.baserecyclerviewadapterhelper.adapter.MultipleItemQuickAdapter;
import com.chad.baserecyclerviewadapterhelper.base.BaseActivity;
import com.chad.baserecyclerviewadapterhelper.data.DataManager;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class ExpandableUseActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private ExpandableItemAdapter expandableAdapter;
    private MultipleItemQuickAdapter multipleAdapter;
    private ArrayList<MultiItemEntity> list;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setBackBtn();
        setTitle("ExpandableItem Activity");
        setContentView(R.layout.activity_expandable_item_use);
        mRecyclerView = findViewById(R.id.rv);
        Button buttonGroup = findViewById(R.id.bt_group);

        buttonGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandableAdapter();
            }
        });
        Button cancelGroup = findViewById(R.id.bt_cancel_group);
        cancelGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMultipleAdapter();
            }
        });
        setMultipleAdapter();
    }

    private void expandableAdapter() {
        expandableAdapter = new ExpandableItemAdapter(DataManager.getInstance().consistGroup());
        final LinearLayoutManager manager = new LinearLayoutManager(mContext);
        expandableAdapter.setEmptyView(R.layout.loading_view, (ViewGroup) mRecyclerView.getParent());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(expandableAdapter);
    }

    private void setMultipleAdapter() {
        multipleAdapter = new MultipleItemQuickAdapter(DataManager.getInstance().getTransferData());
        final LinearLayoutManager manager = new LinearLayoutManager(mContext);
        multipleAdapter.setEmptyView(R.layout.loading_view, (ViewGroup) mRecyclerView.getParent());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(multipleAdapter);
    }

}
