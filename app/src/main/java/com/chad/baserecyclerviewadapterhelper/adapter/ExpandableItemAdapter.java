package com.chad.baserecyclerviewadapterhelper.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.transition.Explode;
import android.support.transition.Fade;
import android.support.transition.Slide;
import android.support.transition.TransitionManager;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.baserecyclerviewadapterhelper.R;
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

    private static final String OTA_PACKAGE = "ecarx.upgrade";
    private static final long OTA_TIME_MARK = 864000000L;

    //原始数据
    private ArrayList<TestNotification> notificationArrayList = new ArrayList<>();

    private List<TestNotification> noInterestingList = new ArrayList<>();

    private Handler mHandler = new Handler();


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
                holder.setText(R.id.tv_item_time, "时间：" + TimeUtil.getTime(normalItem.getTime()));
                Button buttonViewDel = holder.getView(R.id.btn_item_delete);
                buttonViewDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteNormalItem(holder.getAdapterPosition(), normalItem);
                    }
                });
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
                final FrameLayout flMorePicture = holder.getView(R.id.iv_more_picture);
                final RelativeLayout rlHeader = holder.getView(R.id.rl_expand_header);
                final Button btDel = holder.getView(R.id.btn_item_delete);
                final Button btPlace = holder.getView(R.id.btn_item_place);

                Button buttonCollapse = holder.getView(R.id.bt_header_collapse);
                final HeaderDelButton headerDelButton = holder.getView(R.id.bt_parent_del);
                headerDelButton.setDeleteItemListener(new HeaderDelButton.OnDeleteItemListener() {
                    @Override
                    public void setDeleteItem(int state) {
                        if (state == 1) {
                            HeaderDelButton headerDelButton1 = HeaderDelButton.getViewCache();
                            if (headerDelButton1 != null) {
//                                headerDelButton1.restoreUI();
                            }
                        } else {
                            deleteAssembleParent(holder.getAdapterPosition(), lv0);
                        }
                    }
                });

                holder.setText(R.id.tv_item_title, lv0.title)
                        .setText(R.id.tv_pkg_name, lv0.pkg);
                holder.setText(R.id.tv_item_time, "时间:" + TimeUtil.getTime(lv0.time));
                if (lv0.getSubItems().size() > 2) {
                    flMorePicture.setBackground(mContext.getResources().getDrawable(R.drawable.basic_elements_two_bg));
                } else {
                    flMorePicture.setBackground(mContext.getResources().getDrawable(R.drawable.basic_elements_one_bg));
                }

                if (lv0.isExpanded()) {
                    swipeMenuLayout.setSwipeEnable(false);
                    rlHeader.setVisibility(View.VISIBLE);
                    layoutL0.setVisibility(View.GONE);
                    btDel.setVisibility(View.GONE);
                    btPlace.setVisibility(View.GONE);
                    flMorePicture.setVisibility(View.GONE);
                } else {
                    swipeMenuLayout.setSwipeEnable(true);
                    layoutL0.setVisibility(View.VISIBLE);
                    rlHeader.setVisibility(View.GONE);
                    btDel.setVisibility(View.VISIBLE);
                    btPlace.setVisibility(View.VISIBLE);
                    flMorePicture.setVisibility(View.VISIBLE);
                }
                //.removeTarget(btDel).removeTarget(btPlace).removeTarget(rlHeader)
                TransitionManager.beginDelayedTransition(swipeMenuLayout,
                        new Slide(Gravity.BOTTOM));

                //todo 展开与折叠分别是两个按钮，所以删除之后swipe状态不会混乱了
                layoutL0.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int pos = holder.getAdapterPosition();
                        if (lv0.getSubItems().size() > 2) {
                            flMorePicture.setBackground(mContext.getResources().getDrawable(R.drawable.basic_elements_two_bg));
                        } else {
                            flMorePicture.setBackground(mContext.getResources().getDrawable(R.drawable.basic_elements_one_bg));
                        }
                        expand(pos);
                        swipeMenuLayout.setSwipeEnable(false);

                        Log.w(TAG, "EXPAND -----?" + getData().toString());

                    }
                });
                buttonCollapse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int pos = holder.getAdapterPosition();
                        if (lv0.getSubItems().size() > 2) {
                            flMorePicture.setBackground(mContext.getResources().getDrawable(R.drawable.basic_elements_two_bg));
                        } else {
                            flMorePicture.setBackground(mContext.getResources().getDrawable(R.drawable.basic_elements_one_bg));
                        }
                        collapse(pos);
                        swipeMenuLayout.setSwipeEnable(true);
                        Log.w(TAG, "collapse >>>>>>>?" + getData().toString());

                    }
                });
                btDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int assembleParentPosition = holder.getAdapterPosition();
                        deleteAssembleParent(assembleParentPosition, lv0);
                    }
                });
                break;
            case TYPE_LEVEL_1:
                final Level1Item lv1 = (Level1Item) item;
                RelativeLayout rlRoot = holder.getView(R.id.sm_root_view);
                holder.setText(R.id.tv_item_title, lv1.title);
                holder.setText(R.id.tv_item_content, lv1.content);
                holder.setText(R.id.tv_pkg_name, lv1.pkg);
                if (TextUtils.equals(lv1.getPackageName(), OTA_PACKAGE)) {
                    holder.setText(R.id.tv_item_time, "时间：" + TimeUtil.getTime(lv1.getTime() - OTA_TIME_MARK));
                } else {
                    holder.setText(R.id.tv_item_time, "时间：" + TimeUtil.getTime(lv1.getTime()));
                }
                holder.getView(R.id.btn_item_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteAssembleChild(holder, lv1);
                    }
                });
                rlRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteAssembleChild(holder, lv1);
                    }
                });

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
                noInterestingList.add(next);
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
        remove(adapterPosition - getHeaderLayoutCount());
        //删除原数据
        Iterator<TestNotification> it = notificationArrayList.iterator();
        while (it.hasNext()) {
            TestNotification next = it.next();
            if (TextUtils.equals(next.getPkg(), level0Item.getPackageName())) {
                it.remove();
            }
        }

        Log.e("kim", "(parent)视图数据 = " + getData().toString());
        Log.w("kim", "(parent)真实数据 = " + notificationArrayList.toString());

    }

    private void deleteAssembleChild(BaseViewHolder holder, Level1Item lv1) {
        int pos = holder.getAdapterPosition();
        // 先获取到当前 item 的父 positon，再移除自己
        final int positionAtAll = getParentPositionInAll(pos - getHeaderLayoutCount());
        Log.w(TAG, "positionAtAll =" + positionAtAll);
        remove(pos - getHeaderLayoutCount());
        /*
        //todo
         * 构造testList 目的是为了同步视图数据getData(),然后获取index,最后根据这个index,插入数据.
         * 但是有个问题聚合列表没有展开时候,位置是正确的.展开之后,插入的位置就有问题了.
         * 展开之后,直接插入到了展开的数据中了.原因是排序排的这个位置是展开的位置.
         * 那么怎么解决这个问题呢?
         * */
        if (positionAtAll != -1) {
            final IExpandable multiItemEntity = (IExpandable) getData().get(positionAtAll);
            List<Level1Item> childList = new ArrayList<>(multiItemEntity.getSubItems());
            if (childList.size() == 1) {
                remove(positionAtAll);

//                List<MultiItemEntity> testList = new ArrayList<>(getData());
                for (Level1Item level1Item : childList) {
                    Log.e("kim", "positionAtAll = " + positionAtAll + "   pos = " + pos);
                    NormalItem normalItem = new NormalItem(level1Item.title, level1Item.content, level1Item.getPackageName(), level1Item.itemLevel);
                    normalItem.setTime(level1Item.time);
//                    testList.add(normalItem);
//                    Collections.sort(testList, SortUtils.sortGroupEntityCmp);
//                    getData().clear();
//                    getData().addAll(testList);
//                    setNewData(getData());

                    addData(positionAtAll, normalItem);

//                    Collections.sort(getData(), SortUtils.sortGroupEntityCmp);
//                    notifyDataSetChanged();
                    Log.e("kim", "视图数据 = " + getData().toString());
                }

            }
            //删除原数据
            Iterator<TestNotification> it = notificationArrayList.iterator();
            while (it.hasNext()) {
                TestNotification next = it.next();
                if (next.getTime() == lv1.getTime()) {
                    noInterestingList.add(next);
                    it.remove();
                }
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


    //刷新底部UI的
    public void notifyNoInterestUI() {
//        if (noInterestingList.size() > 0) {
        if (getFooterLayoutCount() == 0) {
            addFooterView(mFooterLayout);
        }
        TextView textView = mFooterLayout.findViewById(R.id.footer_tv);
        ImageView imageView = mFooterLayout.findViewById(R.id.footer_iv);
        textView.setText("当前有" + noInterestingList.size() + "条不重要的通知");
        Log.e("jin", "noInterestingList.size() = " + noInterestingList.size());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emptyRecyclerView.setVisibility(View.GONE);
                noInterestViewListener.setLoadNoInterestView(noInterestingList);
            }
        });
//        }
    }


    /**
     * add:
     * 默认是非聚合的状态，所以有这几种情况。1.直接添加默认布局 2.添加聚合布局
     * 3.默认布局里面有了数据然后点击为聚合   4.聚合布局下点击为默认布局
     * <p>
     * del:
     * 直接删除原始数据，然后刷新界面。1.默认布局下，刷新布局。2.聚合布局下，刷新布局
     */

    //动态构建聚合转换
    public synchronized void addGroupItem(TestNotification xcnRecord) {
        int updateListIndex = findUpdateList(xcnRecord);
        Log.v("updateList", "updateListIndex = " + updateListIndex);
        //添加原始数据
        if (updateListIndex != -1) {
            Log.v("updateList", "update  List");
            notificationArrayList.set(updateListIndex, xcnRecord);
        } else {
            notificationArrayList.add(xcnRecord);
        }

        Collections.sort(notificationArrayList, SortUtils.sortChildEntityCmp);
        transformData();
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

    public void transformGroupView() {
        transformData();
    }


    private void transformData() {
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
        //清空数据转换集合
        entityList.clear();
        Log.e(TAG, "GROUP ------->" + getData().toString());
    }

    public void setNormalData() {
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
        notifyDataSetChanged();
    }

    private LoadNoInterestViewListener noInterestViewListener;

    public void addLoadNoInterestView(LoadNoInterestViewListener listener) {
        noInterestViewListener = listener;
    }

    public interface LoadNoInterestViewListener {
        void setLoadNoInterestView(List<TestNotification> lists);
    }
}
