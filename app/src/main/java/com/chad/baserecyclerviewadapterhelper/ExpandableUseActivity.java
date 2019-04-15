package com.chad.baserecyclerviewadapterhelper;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.chad.baserecyclerviewadapterhelper.adapter.ExpandableItemAdapter;
import com.chad.baserecyclerviewadapterhelper.adapter.MultipleItemQuickAdapter;
import com.chad.baserecyclerviewadapterhelper.base.BaseActivity;
import com.chad.baserecyclerviewadapterhelper.data.DataManager;
import com.chad.baserecyclerviewadapterhelper.entity.TestNotification;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class ExpandableUseActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private ExpandableItemAdapter expandableAdapter;
    private MultipleItemQuickAdapter multipleAdapter;
    private ArrayList<TestNotification> mData = new ArrayList<>();
    private Context mContext;
    private boolean isChecked = false;
    private int pos;
    private DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setBackBtn();
        setTitle("ExpandableItem Activity");
        setContentView(R.layout.activity_expandable_item_use);

        getNormalData();
        mDataManager = new DataManager();
//        Log.w("kim","-----+++++"+mDataManager.getTransferData().toString());
        mRecyclerView = findViewById(R.id.rv);
        Switch switchGroup = findViewById(R.id.switch1);

        switchGroup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    expandableAdapter();
                } else {
                    setMultipleAdapter();
                    List<TestNotification> originData = mDataManager.getOriginData();
                    multipleAdapter.setOriginListData(originData);
                }
            }
        });

        Button addNotificationButton = findViewById(R.id.bt_add_notification);
        addNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isChecked) {
//                    expandableAdapter.groupAddOriginData();
                } else {
                    switch (pos) {
                        case 0:
                            TestNotification testNotification1 = new TestNotification(1, "com.ecarx.cc", "周杰伦全新专辑发布!",
                                    "音乐消息1", false, false, 1555053240000L);
                            multipleAdapterAdd(testNotification1);
                            break;
                        case 1:
                            TestNotification testNotification2 = new TestNotification(2, "com.ecarx.bb", "AirPods H1芯片有多强？- 你的耳朵里是台iPhone 4",
                                    "新闻消息2", true, false, 1335053240000L);
                            multipleAdapterAdd(testNotification2);
                            break;
                        case 2:
                            TestNotification testNotification3 = new TestNotification(3, "com.ecarx.dd", "您的内存满了哦，请及时清理！",
                                    "系统消息3", false, false, 1455053240000L);
                            multipleAdapterAdd(testNotification3);
                            break;
                    }
                    pos++;

                }
            }
        });


        expandableAdapter();
//        setMultipleAdapter();
    }

    private void multipleAdapterAdd(TestNotification testNotification) {
        multipleAdapter.addAdapterOriginData(testNotification);
        mDataManager.addOriginData(testNotification);
    }


    private void expandableAdapter() {
        isChecked = true;
        expandableAdapter = new ExpandableItemAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        expandableAdapter.setEmptyView(R.layout.loading_view, (ViewGroup) mRecyclerView.getParent());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(expandableAdapter);
    }

    private void setMultipleAdapter() {
        isChecked = false;
        multipleAdapter = new MultipleItemQuickAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        multipleAdapter.setEmptyView(R.layout.loading_view, (ViewGroup) mRecyclerView.getParent());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(multipleAdapter);
    }

    public void getNormalData() {

        mData.add(new TestNotification(1, "com.ecarx.cc", "周杰伦全新专辑发布!",
                "音乐消息1", false, false, 1555053240000L));
        mData.add(new TestNotification(2, "com.ecarx.bb", "AirPods H1芯片有多强？- 你的耳朵里是台iPhone 4",
                "新闻消息2", true, false, 1335053240000L));
        mData.add(new TestNotification(3, "com.ecarx.dd", "您的内存满了哦，请及时清理！",
                "系统消息3", false, false, 1455053240000L));
        mData.add(new TestNotification(4, "com.ecarx.cc", "陈奕迅全新专辑发布!",
                "音乐消息4", true, false, 1655053240000L));
        mData.add(new TestNotification(5, "com.ecarx.bb", "坚果Pro 2系统更新：下线“残废”功能",
                "新闻消息5", true, false, 1555053240000L));
        mData.add(new TestNotification(6, "com.ecarx.dd", "有新的OTA升级包哦！",
                "系统消息6", false, false, 1855053240000L));
        mData.add(new TestNotification(7, "com.ecarx.cc", "蔡依林全新专辑发布!",
                "音乐消息7", true, false, 1955053240000L));
        mData.add(new TestNotification(8, "com.ecarx.cc", "陶喆全新专辑发布!",
                "音乐消息8", false, false, 1555054240000L));
        mData.add(new TestNotification(9, "com.ecarx.cc", "朴树全新专辑发布!",
                "音乐消息9", true, false, 1555073240000L));
        mData.add(new TestNotification(10, "com.ecarx.cc", "LadyGaga全新专辑发布!",
                "音乐消息10", false, false, 1555153240000L));
        mData.add(new TestNotification(11, "com.ecarx.ee", "2019日本小姐冠军出炉 网友：越看越像吴京！",
                "娱乐消息11", false, false, 1255053240000L));
    }

}
