package com.chad.baserecyclerviewadapterhelper;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.transition.Fade;
import android.support.transition.Slide;
import android.support.transition.TransitionManager;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.chad.baserecyclerviewadapterhelper.adapter.ExpandableItemAdapter;
import com.chad.baserecyclerviewadapterhelper.adapter.MultipleItemQuickAdapter;
import com.chad.baserecyclerviewadapterhelper.adapter.NoInterestAdapter;
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
public class ExpandableUseActivity extends BaseActivity implements ExpandableItemAdapter.
        LoadNoInterestViewListener, NoInterestAdapter.LoadInterestViewListener {
    private EmptyRecyclerView mRecyclerView, mNoInterestRecyclerView;
    private ExpandableItemAdapter expandableAdapter;
    private NoInterestAdapter mNoInterestAdapter;
    private ArrayList<TestNotification> mData = new ArrayList<>();
    private Context mContext;
    private DataManager mDataManager;
    private boolean checked;
    private LinearLayout rootLayout;
    private View maskLayout;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setBackBtn();
        setTitle("ExpandableItem Activity");
        setContentView(R.layout.activity_expandable_item_use);
        rootLayout = findViewById(R.id.root_layout);
        maskLayout = findViewById(R.id.fl_enable_click);
        rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HeaderDelButton viewCache = HeaderDelButton.getViewCache();
                if (viewCache != null) {
                    Log.i(TAG, "click delete layout ");
                    viewCache.restoreUI();
                }
            }
        });
        mNoInterestRecyclerView = findViewById(R.id.no_interest_rv);
        Button buttonExpand = findViewById(R.id.bt_expand);
        buttonExpand.setOnClickListener(v -> {

        });

        findViewById(R.id.bt_del_notification).setOnClickListener(v -> {

            expandableAdapter.deleteAllData();

        });
        LinearLayout emptyView = findViewById(R.id.empty_view);
        getNormalData();
        mDataManager = new DataManager();
        mDataManager.groupEntity();
        mRecyclerView = findViewById(R.id.rv);


        //expand adapter
        expandableAdapter = new ExpandableItemAdapter(mContext);
        //manager
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(manager);
        DividerDecoration mItemDecoration = new DividerDecoration(this, LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(mItemDecoration);
        FadeInDownAnimator adapterAnimator = new FadeInDownAnimator();
        mRecyclerView.setItemAnimator(adapterAnimator);
        mRecyclerView.setAdapter(expandableAdapter);
        expandableAdapter.addLoadNoInterestView(this);
        expandableAdapter.setRecyclerView(mRecyclerView);
        mRecyclerView.setEmptyView(emptyView);
//        expandableAdapter.addHeaderView(getHeaderView());
        expandableAdapter.setFooterLayout(getFooterView());
        expandableAdapter.notifyDataSetChanged();


        if (mNoInterestAdapter == null) {
            mNoInterestAdapter = new NoInterestAdapter(mContext);
            LinearLayoutManager manager1 = new LinearLayoutManager(mContext);

            mNoInterestRecyclerView.setLayoutManager(manager1);

            DividerDecoration mItemDecoration1 = new DividerDecoration(this, LinearLayoutManager.VERTICAL);
            mNoInterestRecyclerView.addItemDecoration(mItemDecoration1);
            FadeInDownAnimator adapterAnimator1 = new FadeInDownAnimator();
            mNoInterestRecyclerView.setItemAnimator(adapterAnimator1);

            mNoInterestRecyclerView.setAdapter(mNoInterestAdapter);

            mNoInterestAdapter.addLoadInterestView(this);
            RelativeLayout noInterestHeaderView = (RelativeLayout) getNoInterestHeaderView();
            mNoInterestAdapter.addHeaderView(noInterestHeaderView);
            ImageView imageView = noInterestHeaderView.findViewById(R.id.iv_back_notification_list);
            imageView.setOnClickListener(v -> {
                mRecyclerView.setVisibility(View.VISIBLE);
                mNoInterestRecyclerView.setVisibility(View.GONE);
                expandableAdapter.notifyDataSetChanged();
                expandableAdapter.notifyNoInterestUI();
            });
        }

        // no interest
        Switch switchGroup = findViewById(R.id.switch1);
        checked = switchGroup.isChecked();
        expandableAdapter.selectListView(checked);

        switchGroup.setOnCheckedChangeListener((buttonView, isChecked) -> {
            checked = isChecked;
            expandableAdapter.selectListView(isChecked);
            mNoInterestAdapter.selectListView(checked);
        });

        findViewById(R.id.bt_auto_flush).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.postDelayed(autoAdd, 1000);
            }
        });

        findViewById(R.id.bt_stop_flush).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.removeCallbacks(autoAdd);
            }
        });

        final int[] group = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};
        Button addNotificationButton = findViewById(R.id.bt_add_notification);
        addNotificationButton.setOnClickListener(v -> {
            int i = group[0]++;
            if (i <= group.length - 1) {
                expandableAdapter.loadNotificationList(mData.get(i), null, checked);
                maskLayout.setClickable(false);
            }
        });

        buttonExpand.setOnClickListener(v -> expandableAdapter.expand(2, false, true));

        Log.e(TAG, "---->" + isSpace("com.eacrx.ut il"));
    }


    private static boolean isSpace(final String s) {
        if (s == null) {
            return true;
        }
        for (int i = 0, len = s.length(); i < len; ++i) {
            //如果字符为空白字符，则返回 true,否则返回 false
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private Runnable autoAdd = new Runnable() {
        @Override
        public void run() {
            maskLayout.setClickable(true);
            expandableAdapter.loadNotificationList(new TestNotification(16, "ecarx.upgrade", "您的mcu需要升级!",
                    "OTA消息3", false, 1557903098235L, 2), null, checked);
            maskLayout.setClickable(false);
            mHandler.postDelayed(this, 1000);
        }
    };

    private HeaderDelButton deleteLayout;

    private View getHeaderView() {
        View view = getLayoutInflater().inflate(R.layout.head_view, (ViewGroup) mRecyclerView.getParent(), false);

        deleteLayout = view.findViewById(R.id.iv_delete_all);

        deleteLayout.setDeleteItemListener(status -> {
            if (status == 2) {
                expandableAdapter.deleteAllData();
            }
        });
        return view;
    }

    private RelativeLayout getFooterView() {
        return (RelativeLayout) getLayoutInflater().inflate(R.layout.footer_view,
                (ViewGroup) mRecyclerView.getParent(), false);
    }


    @Override
    public void setLoadNoInterestView(List<TestNotification> lists) {
        mNoInterestAdapter.setList(lists);
        mNoInterestAdapter.selectListView(checked);
    }

    @Override
    public void showNoInterestView() {
        //控制显示隐藏
        mRecyclerView.setVisibility(View.GONE);
        mNoInterestRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setLoadInterestView(TestNotification testNotification, List<TestNotification> list) {
        if (list == null) {
            expandableAdapter.loadNotificationList(testNotification, checked);
        } else {
            expandableAdapter.loadNotificationList(null, list, checked);
        }

    }

    @Override
    public void showInterestView(boolean isShow) {
        if (isShow) {
            //控制显示隐藏
            mNoInterestRecyclerView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            expandableAdapter.notifyDataSetChanged();
            expandableAdapter.notifyNoInterestUI();
        }
    }

    private View getNoInterestHeaderView() {
        View view = getLayoutInflater().inflate(R.layout.no_interest_head_view, (ViewGroup) mNoInterestRecyclerView.getParent(), false);
        return view;
    }

    public void getNormalData() {
        mData.add(new TestNotification(1, "com.car.cc", "周杰伦全新专辑发布 !",
                "音乐消息1", false, 1557903094210L, 1));
        mData.add(new TestNotification(2, "com.car.bb", "AirPods H1芯片有多强？- 你的耳朵里是台iPhone 4",
                "新闻消息2", false, 1557903095761L, 1));
        mData.add(new TestNotification(24, "com.car.bb", "坚果Pro 2系统更新：下线“残废”功能",
                "新闻消息24", false, 1557903123092L, 1));
//        mData.add(new TestNotification(28, "com.car.cc", "周杰伦全新专辑发布(------>更新的条目<-------)!",
//                "音乐消息28", false, 1557903197323L, 1));
//        mData.add(new TestNotification(46, "com.car.bb", "AirPods H1芯片有多强？- 你的耳朵里是台iPhone 4",
//                "新闻消息46", false, 1557903095561L, 1));
//        mData.add(new TestNotification(45, "com.car.cc", "陈奕迅全新专辑发布!",
//                "音乐消息45", false, 1557903099034L, 1));
//        mData.add(new TestNotification(30, "com.car.kk", "今天天气好!",
//                "天气消息30", false, 1557953696852L, 1));
//        mData.add(new TestNotification(6, "com.car.dd", "有新的OTA升级包哦！",
//                "系统消息6", false, 1557903141000L, 1));
//        mData.add(new TestNotification(49, "com.car.cc", "陈奕迅全新专辑发布!(------>更新的条目<-------)",
//                "音乐消息49", false, 1557983299020L, 1));
//        mData.add(new TestNotification(200, "com.car.kk", "今天天气热!",
//                "天气消息200", false, 1557923095001L, 1));
//        mData.add(new TestNotification(500, "com.car.kk", "今天有大暴雨!",
//                "天气消息500", false, 1557913096002L, 1));
//        mData.add(new TestNotification(70, "com.car.ff", "还有100公里到达目的地!",
//                "导航消息70", false, 1557914992000L, 1));
//        mData.add(new TestNotification(80, "com.car.ff", "还有70公里到达目的地!!",
//                "导航消息80", false, 1557973893000L, 1));
//        mData.add(new TestNotification(90, "com.car.cc", "朴树全新专辑发布!",
//                "音乐消息90", false, 1557913634000L, 1));
//        mData.add(new TestNotification(14, "ecarx.upgrade", "您的系统需要升级!",
//                "OTA消息1", false, 1557903299000L, 2));
//        mData.add(new TestNotification(14, "ecarx.upgrade", "您的系统需要升级!(------>更新的条目<-------)",
//                "OTA消息1", false, 1557903096006L, 2));
//        mData.add(new TestNotification(13, "com.car.ee", "18万只蜜蜂在巴黎圣母院大火幸存 因没有肺脏不怕烟",
//                "娱乐消息13", false, 1557903108000L, 1));
//        mData.add(new TestNotification(100, "com.car.ff", "前方道路拥堵请注意绕行!",
//                "导航消息100", false, 1557923545000L, 1));
//        mData.add(new TestNotification(310, "com.car.bb", "AirPods H1芯片有多强？- 你的耳朵里是台iPhone 4",
//                "新闻消息310", false, 1557905195001L, 1));
        mData.add(new TestNotification(320, "com.car.bb", "坚果Pro 2系统更新：下线“残废”功能",
                "新闻消息320", false, 1557904296002L, 1));
        mData.add(new TestNotification(3, "com.car.dd", "您的内存满了哦，请及时清理！",
                "系统消息3", false, 1557903098200L, 1));
        mData.add(new TestNotification(15, "ecarx.upgrade", "您的屏幕需要升级!",
                "OTA消息2", false, 1557903097000L, 2));
        mData.add(new TestNotification(16, "ecarx.upgrade", "您的mcu需要升级!",
                "OTA消息3", false, 1557903098235L, 2));
        mData.add(new TestNotification(16, "ecarx.upgrade", "您的mcu需要升级!(------>更新的条目<-------)",
                "OTA消息3", false, 1557903098010L, 2));
        mData.add(new TestNotification(11, "com.car.ee", "2019日本小姐冠军出炉 网友：越看越像吴京！",
                "娱乐消息11", false, 1557903106000L, 1));
        mData.add(new TestNotification(12, "com.car.ee", "微软公布Windows 10的活跃设备数已经超过9亿 但可能是个乌龙",
                "娱乐消息12", false, 1557903107000L, 1));

        mData.add(new TestNotification(1212, "com.car.ee", "微软公布Windows 10的活跃设备数已经超过9亿 但可能是个乌龙",
                "娱乐消息12", false, 1557903107000L, 1));

        mData.add(new TestNotification(66, "", "无包名的消息1",
                "未知应用1", false, 1557903107120L, 1));
        mData.add(new TestNotification(63, "", "无包名的消息2",
                "未知应用2", false, 1557903107120L, 1));

        mData.add(new TestNotification(56, " ", "一个空格无包名的消息1",
                "一个空格未知应用1", false, 1557903107120L, 1));
        mData.add(new TestNotification(453, " ", "一个空格无包名的消息2",
                "一个空格未知应用2", false, 1557903107120L, 1));

        mData.add(new TestNotification(126, "  ", "二个空格无包名的消息1",
                "二个空格未知应用1", false, 1557903107120L, 1));
        mData.add(new TestNotification(613, "  ", "二个空格无包名的消息2",
                "二个空格未知应用2", false, 1557903107120L, 1));
    }


}
