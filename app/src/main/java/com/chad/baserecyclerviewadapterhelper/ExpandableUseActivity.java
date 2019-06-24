package com.chad.baserecyclerviewadapterhelper;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.chad.baserecyclerviewadapterhelper.adapter.ExpandableItemAdapter;
import com.chad.baserecyclerviewadapterhelper.adapter.MultipleItemQuickAdapter;
import com.chad.baserecyclerviewadapterhelper.animation.DeleteLayout;
import com.chad.baserecyclerviewadapterhelper.animation.FadeInDownAnimator;
import com.chad.baserecyclerviewadapterhelper.animation.HeaderDelButton;
import com.chad.baserecyclerviewadapterhelper.base.BaseActivity;
import com.chad.baserecyclerviewadapterhelper.data.DataManager;
import com.chad.baserecyclerviewadapterhelper.decoration.DividerDecoration;
import com.chad.baserecyclerviewadapterhelper.entity.HeaderItem;
import com.chad.baserecyclerviewadapterhelper.entity.Level0Item;
import com.chad.baserecyclerviewadapterhelper.entity.Level1Item;
import com.chad.baserecyclerviewadapterhelper.entity.TestNotification;
import com.chad.baserecyclerviewadapterhelper.view.EmptyRecyclerView;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class ExpandableUseActivity extends BaseActivity {
    private EmptyRecyclerView mRecyclerView;
    private ExpandableItemAdapter expandableAdapter;
    private ArrayList<TestNotification> mData = new ArrayList<>();
    private Context mContext;
    private DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setBackBtn();
        setTitle("ExpandableItem Activity");
        setContentView(R.layout.activity_expandable_item_use);
        LinearLayout emptyView = findViewById(R.id.empty_view);
        getNormalData();
        mDataManager = new DataManager();
        mDataManager.groupEntity();
        mRecyclerView = findViewById(R.id.rv);
        expandableAdapter = new ExpandableItemAdapter(mContext);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);

        mRecyclerView.setLayoutManager(manager);
        DividerDecoration itemDecoration = new DividerDecoration(this, LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
        FadeInDownAnimator adapterAnimator = new FadeInDownAnimator();
        mRecyclerView.setItemAnimator(adapterAnimator);
        mRecyclerView.setAdapter(expandableAdapter);
        mRecyclerView.setEmptyView(emptyView);
        expandableAdapter.addHeaderView(getHeaderView());
        expandableAdapter.addFooterView(getFooterView());
        expandableAdapter.notifyDataSetChanged();
        Switch switchGroup = findViewById(R.id.switch1);

        switchGroup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    expandableAdapter.transformGroupView();
                } else {
                    expandableAdapter.setNormalData();
                }
            }
        });

        final int[] group = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
        Button addNotificationButton = findViewById(R.id.bt_add_notification);
        addNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandableAdapter.addGroupItem(mData.get(group[0]++));
            }
        });
    }

    private HeaderDelButton deleteLayout;

    private View getHeaderView() {
        View view = getLayoutInflater().inflate(R.layout.head_view, (ViewGroup) mRecyclerView.getParent(), false);

        deleteLayout = view.findViewById(R.id.iv_delete_all);

        deleteLayout.setDeleteItemListener(new HeaderDelButton.OnDeleteItemListener() {
            @Override
            public void setDeleteItem(int status) {
                if (status == 2) {
                    expandableAdapter.deleteAllData();
                }
            }
        });
        return view;
    }

    private View getFooterView() {
        View view = getLayoutInflater().inflate(R.layout.footer_view, (ViewGroup) mRecyclerView.getParent(), false);
        return view;
    }


    public void getNormalData() {
        mData.add(new TestNotification(1, "com.car.cc", "周杰伦全新专辑发布 !",
                "音乐消息1", false, false, 1557903094000L));
        mData.add(new TestNotification(2, "com.car.bb", "AirPods H1芯片有多强？- 你的耳朵里是台iPhone 4",
                "新闻消息2", true, false, 1557903095001L));
        mData.add(new TestNotification(24, "com.car.bb", "坚果Pro 2系统更新：下线“残废”功能",
                "新闻消息24", true, false, 1557903123002L));
        mData.add(new TestNotification(28, "com.car.cc", "周杰伦全新专辑发布(------>更新的条目<-------)!",
                "音乐消息28", false, false, 1557903197323L));

        mData.add(new TestNotification(46, "com.car.bb", "AirPods H1芯片有多强？- 你的耳朵里是台iPhone 4",
                "新闻消息46", true, false, 1557903095561L));
        mData.add(new TestNotification(45, "com.car.cc", "陈奕迅全新专辑发布!",
                "音乐消息45", true, false, 1557903099034L));
        mData.add(new TestNotification(30, "com.car.bb", "坚果Pro 2系统更新：下线“残废”功能",
                "新闻消息30", true, false, 1557953096852L));
        mData.add(new TestNotification(49, "com.car.cc", "陈奕迅全新专辑发布!(------>更新的条目<-------)",
                "音乐消息49", true, false, 1557983299020L));

        mData.add(new TestNotification(200, "com.car.bb", "AirPods H1芯片有多强？- 你的耳朵里是台iPhone 4",
                "新闻消息200", true, false, 1557923095001L));
        mData.add(new TestNotification(500, "com.car.bb", "坚果Pro 2系统更新：下线“残废”功能",
                "新闻消息500", true, false, 1557913096002L));
        mData.add(new TestNotification(70, "com.car.cc", "蔡依林全新专辑发布!",
                "音乐消息70", true, false, 1557914992000L));
        mData.add(new TestNotification(80, "com.car.cc", "陶喆全新专辑发布!",
                "音乐消息80", false, false, 1557973893000L));
        mData.add(new TestNotification(90, "com.car.cc", "朴树全新专辑发布!",
                "音乐消息90", true, false, 1557913634000L));
        mData.add(new TestNotification(100, "com.car.cc", "LadyGaga全新专辑发布!",
                "音乐消息100", false, false, 1557923545000L));
        mData.add(new TestNotification(310, "com.car.bb", "AirPods H1芯片有多强？- 你的耳朵里是台iPhone 4",
                "新闻消息310", true, false, 1557905095001L));
        mData.add(new TestNotification(320, "com.car.bb", "坚果Pro 2系统更新：下线“残废”功能",
                "新闻消息320", true, false, 1557904096002L));


        mData.add(new TestNotification(3, "com.car.dd", "您的内存满了哦，请及时清理！",
                "系统消息3", false, false, 1557903098000L));
        mData.add(new TestNotification(14, "ecarx.upgrade", "您的系统需要升级!",
                "OTA消息1", true, false, 1557903099000L));
        mData.add(new TestNotification(14, "ecarx.upgrade", "您的系统需要升级!(------>更新的条目<-------)",
                "OTA消息1", true, false, 1557903096006L));
        mData.add(new TestNotification(15, "ecarx.upgrade", "您的屏幕需要升级!",
                "OTA消息2", true, false, 1557903097000L));
        mData.add(new TestNotification(16, "ecarx.upgrade", "您的mcu需要升级!",
                "OTA消息3", true, false, 1557903098000L));
        mData.add(new TestNotification(16, "ecarx.upgrade", "您的mcu需要升级!(------>更新的条目<-------)",
                "OTA消息3", true, false, 1557903098010L));
        mData.add(new TestNotification(6, "com.car.dd", "有新的OTA升级包哦！",
                "系统消息6", false, false, 1557903101000L));
        mData.add(new TestNotification(11, "com.car.ee", "2019日本小姐冠军出炉 网友：越看越像吴京！",
                "娱乐消息11", false, false, 1557903106000L));
        mData.add(new TestNotification(12, "com.car.ee", "微软公布Windows 10的活跃设备数已经超过9亿 但可能是个乌龙",
                "娱乐消息12", false, false, 1557903107000L));
        mData.add(new TestNotification(13, "com.car.ee", "18万只蜜蜂在巴黎圣母院大火幸存 因没有肺脏不怕烟",
                "娱乐消息13", false, false, 1557903108000L));
    }

}
