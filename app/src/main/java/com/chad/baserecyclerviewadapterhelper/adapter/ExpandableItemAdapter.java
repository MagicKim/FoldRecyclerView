package com.chad.baserecyclerviewadapterhelper.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.transition.AutoTransition;
import android.support.transition.Explode;
import android.support.transition.Fade;
import android.support.transition.Scene;
import android.support.transition.Slide;
import android.support.transition.TransitionManager;
import android.support.transition.TransitionSet;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.animation.DeleteLayout;
import com.chad.baserecyclerviewadapterhelper.animation.HeaderDelButton;
import com.chad.baserecyclerviewadapterhelper.animation.SwipeMenuLayout;
import com.chad.baserecyclerviewadapterhelper.entity.Level0Item;
import com.chad.baserecyclerviewadapterhelper.entity.Level1Item;
import com.chad.baserecyclerviewadapterhelper.entity.NormalItem;
import com.chad.baserecyclerviewadapterhelper.entity.TestNotification;
import com.chad.baserecyclerviewadapterhelper.util.SortUtils;
import com.chad.baserecyclerviewadapterhelper.util.TimeUtil;
import com.chad.baserecyclerviewadapterhelper.view.EmptyRecyclerView;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.IExpandable;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by luoxw on 2016/8/9.
 */
public class ExpandableItemAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    private static final String TAG = ExpandableItemAdapter.class.getSimpleName();

    //聚合
    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;
    //普通
    public static final int TYPE_NORMAL = 2;

    private Context mContext;

    //原始数据
    private ArrayList<TestNotification> notificationArrayList = new ArrayList<>();

    private List<TestNotification> noInterestingList = new ArrayList<>();

    private EmptyRecyclerView emptyRecyclerView;

    private RelativeLayout mFooterLayout;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     */
    public ExpandableItemAdapter(Context context) {
        super(null);
        mContext = context;
        addItemType(TYPE_NORMAL, R.layout.item_text_view);
        addItemType(TYPE_LEVEL_0, R.layout.item_assemble_parent);
        addItemType(TYPE_LEVEL_1, R.layout.item_assemble_child);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final MultiItemEntity item) {
        switch (holder.getItemViewType()) {

            case TYPE_NORMAL:
                final NormalItem normalItem = (NormalItem) item;
                holder.setText(R.id.tv_item_title, normalItem.getTitle());
                holder.setText(R.id.tv_item_content, normalItem.getContent());
                holder.setText(R.id.tv_pkg_name, normalItem.getPkg());
                holder.setText(R.id.tv_item_time, TimeUtil.getTime(normalItem.getTime()));
                Button buttonViewDel = holder.getView(R.id.btn_item_delete);
                buttonViewDel.setOnClickListener(v -> deleteNormalItem(holder.getAdapterPosition(), normalItem));
                break;
            case TYPE_LEVEL_0:
                switch (holder.getLayoutPosition() % 3) {
                    case 0:
                        holder.setImageResource(R.id.iv_app_icon, R.mipmap.head_img0);
                        break;
                    case 1:
                        holder.setImageResource(R.id.iv_app_icon, R.mipmap.head_img1);
                        break;
                    case 2:
                        holder.setImageResource(R.id.iv_app_icon, R.mipmap.head_img2);
                        break;
                    default:
                        break;
                }
                final Level0Item lv0 = (Level0Item) item;
                final SwipeMenuLayout swipeMenuLayout = holder.getView(R.id.rl_normal_root_layout);
                final RelativeLayout layoutL0 = holder.getView(R.id.sm_root_view);
                final RelativeLayout rootView = holder.getView(R.id.rl_view);
                final FrameLayout flMorePicture = holder.getView(R.id.iv_more_picture);
                final RelativeLayout rlHeader = holder.getView(R.id.rl_expand_header);
                final TextView textCount = holder.getView(R.id.tv_parent_count);
                final Button btDel = holder.getView(R.id.btn_item_delete);
                final LinearLayout swipeRightLayout = holder.getView(R.id.ll_swipe_right_layout);
                textCount.setText(lv0.getSubItems().size() + "个通知");
                Button buttonCollapse = holder.getView(R.id.bt_header_collapse);
                final HeaderDelButton parentDelButton = holder.getView(R.id.bt_parent_del);
                parentDelButton.setDeleteItemListener(state -> {
                    if (state == 2) {
                        deleteAssembleParent(holder.getAdapterPosition(), lv0);
                    }
                });

                holder.setText(R.id.tv_item_title, lv0.title)
                        .setText(R.id.tv_pkg_name, lv0.pkg);
                holder.setText(R.id.tv_item_time, TimeUtil.getTime(lv0.time));
                if (lv0.getSubItems().size() > 2) {
                    flMorePicture.setBackground(mContext.getResources().getDrawable(R.drawable.basic_elements_two_bg));
                } else {
                    flMorePicture.setBackground(mContext.getResources().getDrawable(R.drawable.basic_elements_one_bg));
                }


                if (lv0.isExpanded()) {
                    swipeMenuLayout.setSwipeEnable(false);
                    rlHeader.setVisibility(View.VISIBLE);
                    layoutL0.setVisibility(View.GONE);
                    swipeRightLayout.setVisibility(View.GONE);
                    flMorePicture.setVisibility(View.GONE);
                } else {
                    swipeMenuLayout.setSwipeEnable(true);
                    layoutL0.setVisibility(View.VISIBLE);
                    rlHeader.setVisibility(View.GONE);
                    swipeRightLayout.setVisibility(View.VISIBLE);
                    flMorePicture.setVisibility(View.VISIBLE);
                }
                //.removeTarget(btDel).removeTarget(btPlace).removeTarget(rlHeader)
                TransitionManager.beginDelayedTransition(swipeMenuLayout,
                        new Slide(Gravity.BOTTOM));
                //todo 展开与折叠分别是两个按钮，所以删除之后swipe状态不会混乱了
                layoutL0.setOnClickListener(v -> {
                    int pos = holder.getAdapterPosition();
                    if (lv0.getSubItems().size() > 2) {
                        flMorePicture.setBackground(mContext.getResources().getDrawable(R.drawable.basic_elements_two_bg));
                    } else {
                        flMorePicture.setBackground(mContext.getResources().getDrawable(R.drawable.basic_elements_one_bg));
                    }
                    expand(pos);
                    swipeMenuLayout.setSwipeEnable(false);
                });
                buttonCollapse.setOnClickListener(v -> {
//                    TransitionManager.beginDelayedTransition(swipeMenuLayout,
//                            new TransitionSet().addTransition(new Slide(Gravity.BOTTOM)).addTransition(new Fade()));
                    TransitionManager.beginDelayedTransition(swipeMenuLayout,new Slide(Gravity.BOTTOM).excludeTarget(rlHeader,true));
                    int pos = holder.getAdapterPosition();
                    textCount.setText(lv0.getSubItems().size() + "个通知");
                    if (lv0.getSubItems().size() > 2) {
                        flMorePicture.setBackground(mContext.getResources().getDrawable(R.drawable.basic_elements_two_bg));
                    } else {
                        flMorePicture.setBackground(mContext.getResources().getDrawable(R.drawable.basic_elements_one_bg));
                    }
                    collapse(pos);
                    swipeMenuLayout.setSwipeEnable(true);
                });
                btDel.setOnClickListener(v -> {
                    int assembleParentPosition = holder.getAdapterPosition();
                    deleteAssembleParent(assembleParentPosition, lv0);
                });
                break;
            case TYPE_LEVEL_1:
                final Level1Item lv1 = (Level1Item) item;
                RelativeLayout rlRoot = holder.getView(R.id.sm_root_view);
                holder.setText(R.id.tv_item_title, lv1.title);
                holder.setText(R.id.tv_item_content, lv1.content);
                holder.setText(R.id.tv_pkg_name, lv1.pkg);
                holder.setText(R.id.tv_item_time, TimeUtil.getTime(lv1.getTime()));
                holder.getView(R.id.btn_item_delete).setOnClickListener(v -> deleteAssembleChild(holder, lv1));
                rlRoot.setOnClickListener(v -> deleteAssembleChild(holder, lv1));
                break;
            default:
                break;
        }
    }

    private void deleteNormalItem(int adapterPosition, NormalItem normalItem) {
        //删除视图数据
        remove(adapterPosition - getHeaderLayoutCount());
        //删除原数据
        Iterator<TestNotification> it = notificationArrayList.iterator();
        while (it.hasNext()) {
            TestNotification next = it.next();
            if (next.getTime() == normalItem.getTime()) {
                addNoInterestList(next, null);
                it.remove();
            }
        }
        notifyNoInterestUI();

        Log.e("kim", "删除默认布局之后 = " + notificationArrayList.toString());

    }


    /**
     * 删除的时候,注意数据同步,默认的删除直接删除position,然后自动移除了getData()里面的数据,需要删除原始数据
     * 如果是折叠列表,需要判断当前删除的child数量,如果数量变为1个时候,需要将那一个单独的child变成默认的布局
     * 这时候getData()里面还有这个折叠的 数据集,需要删除?然后删除默认的数据集,更新界面.
     */

    private void deleteAssembleParent(int adapterPosition, Level0Item level0Item) {
        Log.e("kim", "deleteAssembleParent");
        ArrayList<TestNotification> list = new ArrayList<>();
        remove(adapterPosition - getHeaderLayoutCount());
        //删除原数据
        Iterator<TestNotification> it = notificationArrayList.iterator();
        while (it.hasNext()) {
            TestNotification next = it.next();
            if (TextUtils.equals(next.getPkg(), level0Item.getPackageName())) {
                list.add(next);
                it.remove();
            }
        }
        addNoInterestList(null, list);
        notifyNoInterestUI();
    }

    private void deleteAssembleChild(BaseViewHolder holder, Level1Item lv1) {
        int pos = holder.getAdapterPosition();
        // 先获取到当前 item 的父 positon，再移除自己
        final int positionAtAll = getParentPositionInAll(pos - getHeaderLayoutCount());
        remove(pos - getHeaderLayoutCount());
        //删除原数据
        Iterator<TestNotification> it = notificationArrayList.iterator();
        while (it.hasNext()) {
            TestNotification next = it.next();
            if (next.getTime() == lv1.getTime()) {
                addNoInterestList(next, null);
                it.remove();
            }
        }
        if (positionAtAll != -1) {
            final IExpandable multiItemEntity = (IExpandable) getData().get(positionAtAll);
            List<Level1Item> childList = new ArrayList<>(multiItemEntity.getSubItems());
            if (childList.size() == 1) {
                transformGroupView();
                SwipeMenuLayout viewCache = SwipeMenuLayout.getViewCache();
                if (viewCache != null) {
                    viewCache.quickClose();
                }
                /*
                 * 构造testList 目的是为了同步视图数据getData(),然后获取index,最后根据这个index,插入数据.
                 * 但是有个问题聚合列表没有展开时候,位置是正确的.展开之后,因为MultiItemEntity中插入了Level1 item 所以排序的时候插入的位置就有问题了.
                 *
                 */
//                for (Level1Item level1Item : childList) {
//                    Log.e("kim", "positionAtAll = " + positionAtAll + "   pos = " + pos);
//                    NormalItem normalItem = new NormalItem(level1Item.title, level1Item.content, level1Item.getPackageName(), level1Item.itemLevel);
//                    normalItem.setTime(level1Item.time);
//                    testList.add(normalItem);
//                    Collections.sort(testList, SortUtils.sortDelGroupEntityCmp);
//                    int noPos = testList.indexOf(normalItem);
//                    Log.i(TAG, "NOPOS = " + noPos);
//                    addData(positionAtAll, normalItem);
//                }
            }
            notifyNoInterestUI();
        }
    }

    public void setRecyclerView(EmptyRecyclerView recyclerView) {
        emptyRecyclerView = recyclerView;
    }

    public void setFooterLayout(RelativeLayout footerView) {
        mFooterLayout = footerView;
    }

    //监听不感兴趣动作
    private void addNoInterestList(TestNotification testNotification) {
        noInterestingList.add(testNotification);
        noInterestViewListener.setLoadNoInterestView(noInterestingList);
    }

    private void addNoInterestList(TestNotification testNotification, List<TestNotification> lists) {
        if (lists == null) {
            addNoInterestList(testNotification);
        } else {
            noInterestingList.addAll(lists);
            noInterestViewListener.setLoadNoInterestView(noInterestingList);
        }
    }

    //刷新底部UI的
    public void notifyNoInterestUI() {
        if (getFooterLayoutCount() == 0) {
            addFooterView(mFooterLayout);
        }
        if (noInterestingList.size() == 0) {
            if (getFooterLayoutCount() > 0) {
                removeAllFooterView();
            }
        }

        TextView textView = mFooterLayout.findViewById(R.id.footer_tv);
        ImageView imageView = mFooterLayout.findViewById(R.id.footer_iv);
        textView.setText("当前有" + noInterestingList.size() + "条不重要的通知");
        imageView.setOnClickListener(v -> noInterestViewListener.showNoInterestView());
    }

    public void loadNotificationList(TestNotification xcnRecord, List<TestNotification> curList, boolean isGroup) {
        if (curList == null) {
            Log.i(TAG, "load data");
            loadNotificationList(xcnRecord, isGroup);
        } else {
            Log.i(TAG, "load list");
            notificationArrayList.addAll(curList);
            selectListView(isGroup);
        }
    }


    public void loadNotificationList(TestNotification xcnRecord, boolean isGroup) {
//        int updateListIndex = findUpdateList(xcnRecord);
//        Log.v("updateList", "updateListIndex = " + updateListIndex);
//        //添加原始数据
//        if (updateListIndex != -1) {
//            Log.v("updateList", "update  List");
//            notificationArrayList.set(updateListIndex, xcnRecord);
//        } else {
        notificationArrayList.add(xcnRecord);
//        }

        selectListView(isGroup);
    }

    public void selectListView(boolean isGroup) {
        if (isGroup) {
            transformGroupView();
        } else {
            setNormalData();
        }
    }


    private int findUpdateList(TestNotification newRecord) {
        if (notificationArrayList.size() > 0) {
            for (int i = 0; i < notificationArrayList.size(); i++) {
                TestNotification record = notificationArrayList.get(i);
                if (newRecord.getId() == record.getId()) {
                    return i;
                }
            }
        }
        return -1;
    }

    private void transformGroupView() {
        transformData();
    }

    private void transformData() {
        //构造展开的集合
        List<String> Level0DataList = new ArrayList<>();
        for (MultiItemEntity multiItemEntity : getData()) {
            if (multiItemEntity instanceof Level0Item) {
                Level0Item level0Item = (Level0Item) multiItemEntity;
                if (level0Item.isExpanded()) {
                    Log.e("kim", "-------------->" + level0Item.pkg);
                    //如果展开
                    Level0DataList.add(level0Item.pkg);
                }
            }
        }
        getData().clear();
        ArrayList<MultiItemEntity> entityList = new ArrayList<>();
        Log.w("kim", "切换数据到聚合 = " + notificationArrayList.toString());
        //使用map分组
        Map<String, List<TestNotification>> resultMap = new LinkedHashMap<>();
        for (TestNotification record : notificationArrayList) {
            String pkg = record.getPkg();
            if (resultMap.containsKey(pkg)) {
                resultMap.get(pkg).add(record);
            } else {
                List<TestNotification> tmp = new LinkedList<>();
                tmp.add(record);
                resultMap.put(pkg, tmp);
            }
        }

        //遍历map集合
        for (String key : resultMap.keySet()) {
            List<TestNotification> notificationList = resultMap.get(key);
            Collections.sort(notificationList, SortUtils.sortChildEntityCmp);
            if (notificationList.size() < 2) {
                for (TestNotification notification : notificationList) {
                    NormalItem normalItem = new NormalItem(notification.getTitle(), notification.getContent(),
                            notification.getPkg(), notification.getLevel());
                    normalItem.setTime(notification.getTime());
                    entityList.add(normalItem);
                }
            } else {
                Level0Item foldParentItem = new Level0Item();
                for (TestNotification timeNotification : notificationList) {
                    foldParentItem.addSubItem(new Level1Item(timeNotification.getTitle(), timeNotification.getPkg(),
                            timeNotification.getContent(), timeNotification.getTime(), timeNotification.getLevel()));
                }
                for (TestNotification timeNotification : notificationList) {
                    foldParentItem.time = timeNotification.getTime();
                    foldParentItem.title = timeNotification.getTitle();
                    foldParentItem.pkg = timeNotification.getPkg();
                    foldParentItem.itemLevel = timeNotification.getLevel();
                    break;
                }
                entityList.add(foldParentItem);
            }
        }
        //将转换的集合加入视图需要的数据源getData
        getData().addAll(entityList);
        //排序
        Collections.sort(getData(), SortUtils.sortGroupEntityCmp);
        //通知刷新UI
        setNewData(getData());
        //然后再把之前展开的数据一条一条展开
        for (String beforeExpand : Level0DataList) {
            for (int i = 0; i < getData().size(); i++) {
                MultiItemEntity multiItemEntity = getData().get(i);
                if (multiItemEntity instanceof Level0Item) {
                    Level0Item level0Item = (Level0Item) multiItemEntity;
                    if (level0Item.pkg.equals(beforeExpand)) {
//                        level0Item.setExpanded(true);
                        Log.e("kim", "===============>" + level0Item.pkg);
                        expand(i + getHeaderLayoutCount(), false, true);
                    }
                }
            }
        }
//        Log.e(TAG, "GROUP ------->" + getData().toString());
    }


    private void setNormalData() {
        getData().clear();
        for (TestNotification testNotification : notificationArrayList) {
            NormalItem normalItem = new NormalItem(testNotification.getTitle(), testNotification.getContent(),
                    testNotification.getPkg(), testNotification.getLevel());
            normalItem.setTime(testNotification.getTime());
            addData(normalItem);
        }
    }


    public void deleteAllData() {
        Log.d("kim", "(delete all)");
        getData().clear();
        notificationArrayList.clear();
        notifyItemRangeRemoved(0, 0);
    }

    private LoadNoInterestViewListener noInterestViewListener;

    public void addLoadNoInterestView(LoadNoInterestViewListener listener) {
        noInterestViewListener = listener;
    }

    public interface LoadNoInterestViewListener {
        void setLoadNoInterestView(List<TestNotification> lists);

        void showNoInterestView();
    }
}
