package com.chad.baserecyclerviewadapterhelper.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.animation.FoldingLayout;
import com.chad.baserecyclerviewadapterhelper.animation.SwipeMenuLayout;
import com.chad.baserecyclerviewadapterhelper.entity.HeaderItem;
import com.chad.baserecyclerviewadapterhelper.entity.ImageItem;
import com.chad.baserecyclerviewadapterhelper.entity.Level0Item;
import com.chad.baserecyclerviewadapterhelper.entity.Level1Item;
import com.chad.baserecyclerviewadapterhelper.entity.NormalItem;
import com.chad.baserecyclerviewadapterhelper.entity.PolymerizationItem0;
import com.chad.baserecyclerviewadapterhelper.entity.PolymerizationItem1;
import com.chad.baserecyclerviewadapterhelper.entity.TestNotification;
import com.chad.baserecyclerviewadapterhelper.util.DataCallback;
import com.chad.baserecyclerviewadapterhelper.util.ListUtils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.IExpandable;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * Created by luoxw on 2016/8/9.
 */
public class ExpandableItemAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    private static final String TAG = ExpandableItemAdapter.class.getSimpleName();

    public static final int TYPE_HEADER = 4;
    //聚合
    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;
    //普通
    public static final int TYPE_NORMAL = 2;
    //折叠
    public static final int TYPE_IMAGE = 3;
    //屏蔽
    public static final int TYPE_GROUP_0 = 5;
    public static final int TYPE_GROUP_1 = 6;

    private Context mContext;

    //原始数据
    private ArrayList<TestNotification> notificationArrayList = new ArrayList<>();
    //转换的数据
    private ArrayList<MultiItemEntity> entityList = new ArrayList<>();
    private Handler mHandler = new Handler();


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
                holder.setText(R.id.tv_item_time, "时间：" + normalItem.getTime());
                Button buttonViewDel = holder.getView(R.id.btn_item_delete);
                buttonViewDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        setItemDel(holder.getAdapterPosition(), normalItem);
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
//                final RelativeLayout layoutLandscape = holder.getView(R.id.l0_layout_image);
                final RelativeLayout layoutL0 = holder.getView(R.id.sm_root_view);
                final ImageView imageViewMorePicture = holder.getView(R.id.iv_more_picture);
//                imageViewMorePicture.setVisibility(View.VISIBLE);
//                final ImageView imageLandscape = holder.getView(R.id.iv_picture);
//                layoutLandscape.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        imageLandscape.setVisibility(View.GONE);
//                        layoutL0.setVisibility(View.VISIBLE);
//                    }
//                });

//                Picasso.with(mContext).load(R.drawable.picture_landscape).into(imageLandscape);
//                if (lv0.isExpanded()) {
//                    swipeMenuLayout.setSwipeEnable(false);
//                } else {
//                    swipeMenuLayout.setSwipeEnable(true);
//                }
                holder.setText(R.id.tv_item_title, lv0.title)
                        .setText(R.id.tv_pkg_name, lv0.subTitle);
                if (lv0.isExpanded()) {
                    imageViewMorePicture.setVisibility(View.GONE);
                } else {
                    imageViewMorePicture.setVisibility(View.VISIBLE);
                }

                //todo swipeLayout 直接删除下一个无法左滑
                layoutL0.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getAdapterPosition();
                        Log.e("kim", "lv0.isExpanded() = " + lv0.isExpanded());
                        if (lv0.isExpanded()) {
//                            lv0.setExpanded(false);
                            collapse(pos);
                            swipeMenuLayout.setSwipeEnable(false);
                        } else {
//                            lv0.setExpanded(true);
                            expand(pos);
                            swipeMenuLayout.setSwipeEnable(true);
                        }
                    }
                });

                holder.getView(R.id.btn_item_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("kim", "删除");
                        int pos = holder.getAdapterPosition();
                        remove(pos - getHeaderLayoutCount());
                    }
                });
                break;
            case TYPE_LEVEL_1:
                final Level1Item lv1 = (Level1Item) item;
                holder.setText(R.id.tv_item_title, lv1.title);
                holder.setText(R.id.tv_item_content, lv1.content);
                holder.setText(R.id.tv_pkg_name, lv1.subTitle);
                holder.setText(R.id.tv_item_time, "时间：" + lv1.getTime());
                holder.getView(R.id.btn_item_delete).setOnClickListener(new View.OnClickListener() {
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
                it.remove();
            }
        }
        Log.e("kim", "删除默认布局之后 = " + notificationArrayList.toString());

    }


    /**
     * 删除的时候,注意数据同步,默认的删除直接删除position,然后自动移除了getData()里面的数据,需要删除原始数据
     * 如果是折叠列表,需要判断当前删除的child数量,如果数量变为1个时候,需要将那一个单独的child变成默认的布局
     * 这时候getData()里面还有这个折叠的 数据集,需要删除?然后删除默认的数据集,更新界面.
     */

    private void deleteAssembleParent() {

    }

    private void deleteAssembleChild(BaseViewHolder holder, Level1Item lv1) {
        int pos = holder.getAdapterPosition();
        // 先获取到当前 item 的父 positon，再移除自己
        final int positionAtAll = getParentPositionInAll(pos - getHeaderLayoutCount());
        Log.w(TAG, "positionAtAll =" + positionAtAll);
        remove(pos - getHeaderLayoutCount());
        if (positionAtAll != -1) {
            final IExpandable multiItemEntity = (IExpandable) getData().get(positionAtAll);
            List<Level1Item> childList = multiItemEntity.getSubItems();
            if (childList.size() == 1) {
                for (Level1Item level1Item : childList) {
                    Log.e("kim", "1 = " + level1Item.toString());
                    NormalItem normalItem = new NormalItem(level1Item.title, level1Item.content, level1Item.getPackageName());
                    normalItem.setTime(level1Item.time);
                    addData(normalItem);
                }
                remove(positionAtAll);
//                notifyDataSetChanged();

            }

            //todo 需要删除原始数据
            Log.e("kim", "删除折叠子类剩余视图数据 = " + getData().toString());
            Log.e("kim", "multiItemEntity =" + multiItemEntity.toString());
//            if (!hasSubItems(multiItemEntity)) {
//                getData().remove(multiItemEntity);
//                notifyDataSetChanged();
//            }
        }

    }


    //删除
//    private void setItemDel(int pos, MultiItemEntity item) {
//        Log.d(TAG, "click remove");
//        // 先获取到当前 item 的父 positon，再移除自己
//        int positionAtAll = getParentPositionInAll(pos);
//        remove(pos);
//        if (positionAtAll != -1) {
//            IExpandable multiItemEntity = (IExpandable) getData().get(positionAtAll);
//            if (!hasSubItems(multiItemEntity)) {
//                remove(positionAtAll);
//            }
//        }
//    }

    //动态构建聚合转换
    public void addGroupItem(TestNotification xcnRecord) {
        //添加原始数据
        notificationArrayList.add(xcnRecord);
        assembleData(xcnRecord);
    }

    private void assembleData(TestNotification xcnRecord) {
        //构建时间集合
        ArrayList<Long> childTimeList = new ArrayList<>();
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
        //同一包名下,2条消息才会聚合
        if (resultMap.get(xcnRecord.getPkg()).size() < 2) {
            Log.e(TAG, "添加默认的布局 同一个包名下面的第一条消息不聚合 " + xcnRecord.getPkg());
            NormalItem normalItem = new NormalItem(xcnRecord.getTitle(), xcnRecord.getContent(), xcnRecord.getPkg());
            normalItem.setTime(xcnRecord.getTime());
            entityList.add(normalItem);
        } else {
            Log.e(TAG, "开始聚合布局------>" + xcnRecord.getPkg());
            Iterator<MultiItemEntity> iterator = getData().iterator();
            while (iterator.hasNext()) {
                MultiItemEntity next = iterator.next();
                if (itemType(next) == TYPE_NORMAL) {
                    NormalItem normalItem = (NormalItem) next;
                    if (normalItem.getPackageName().equals(xcnRecord.getPkg())) {
                        Log.e(TAG, "聚合布局,需要删除视图集合中normal类型的数据 " + xcnRecord.getPkg());
                        iterator.remove();
                    }
                }
            }
            //遍历map集合
            for (String key : resultMap.keySet()) {
                Log.e(TAG, "遍历map");
                //如果遍历的key 与刚刚来的通知的package是一样的才聚合
                if (TextUtils.equals(key, xcnRecord.getPkg())) {
                    //首先删除视图 getData()里面的数据,防止重复添加
                    Iterator<MultiItemEntity> iteratorGroup = getData().iterator();
                    while (iteratorGroup.hasNext()) {
                        MultiItemEntity next = iteratorGroup.next();
                        if (itemType(next) == TYPE_LEVEL_0) {
                            Log.e(TAG, " TYPE_LEVEL_0");
                            Level0Item level0Item = (Level0Item) next;
                            if (level0Item.getPackageName().equals(xcnRecord.getPkg())) {
                                Log.e(TAG, "聚合布局,需要删除原来已经聚合的内容 " + xcnRecord.getPkg());
                                iteratorGroup.remove();
                            }
                        }
                    }
                    Log.e(TAG, "如果这条消息与map中的key值一样,那么聚合");
                    //添加聚合实体类
                    List<TestNotification> notificationList = resultMap.get(key);
                    Level0Item foldParentItem = new Level0Item();
                    for (TestNotification timeNotification : notificationList) {
                        childTimeList.add(timeNotification.getTime());
                        foldParentItem.addSubItem(new Level1Item(timeNotification.getTitle(), timeNotification.getPkg(), true, timeNotification.getContent(), timeNotification.getTime()));
                    }
                    //排序
                    Long maxTime = Collections.max(childTimeList);
                    for (TestNotification timeNotification : notificationList) {
                        if (timeNotification.getTime() == maxTime) {
                            foldParentItem.time = maxTime;
                            foldParentItem.setExpanded(false);
                            foldParentItem.title = timeNotification.getTitle();
                            foldParentItem.subTitle = timeNotification.getPkg();
                        }
                    }
                    entityList.add(foldParentItem);
                }
            }
        }
        //将转换的集合加入视图需要的数据源getData
        getData().addAll(entityList);
        //排序
        Collections.sort(getData(), mGroupEntityCmp);
        //通知刷新UI
        setNewData(getData());
        //清空数据转换集合
        entityList.clear();
    }

    public void transformGroupView() {
        getData().clear();
        transformData();
    }

    private void transformData() {
        Log.w("kim", "切换数据到聚合 = " + notificationArrayList.toString());
        //构建时间集合
        ArrayList<Long> childTimeList = new ArrayList<>();
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
            Log.e(TAG, "遍历map");
            List<TestNotification> notificationList = resultMap.get(key);
            Collections.sort(notificationList, mChildEntityCmp);
            if (notificationList.size() < 2) {
                for (TestNotification notification : notificationList) {
                    NormalItem normalItem = new NormalItem(notification.getTitle(), notification.getContent(), notification.getPkg());
                    normalItem.setTime(notification.getTime());
                    entityList.add(normalItem);
                }
            } else {
                Level0Item foldParentItem = new Level0Item();
                for (TestNotification timeNotification : notificationList) {
                    childTimeList.add(timeNotification.getTime());
                    foldParentItem.addSubItem(new Level1Item(timeNotification.getTitle(), timeNotification.getPkg(), true, timeNotification.getContent(), timeNotification.getTime()));
                }
                //排序
                Long maxTime = Collections.max(childTimeList);
                for (TestNotification timeNotification : notificationList) {
                    if (timeNotification.getTime() == maxTime) {
                        foldParentItem.time = maxTime;
                        foldParentItem.setExpanded(false);
                        foldParentItem.title = timeNotification.getTitle();
                        foldParentItem.subTitle = timeNotification.getPkg();
                    }
                }
                entityList.add(foldParentItem);
            }
        }
        //将转换的集合加入视图需要的数据源getData
        getData().addAll(entityList);
        //排序
        Collections.sort(getData(), mGroupEntityCmp);
        //通知刷新UI
        setNewData(getData());
        //清空数据转换集合
        entityList.clear();
    }

    public void setNormalData() {
        getData().clear();
        for (TestNotification testNotification : notificationArrayList) {
            NormalItem normalItem = new NormalItem(testNotification.getTitle(), testNotification.getContent(), testNotification.getPkg());
            normalItem.setTime(testNotification.getTime());
            addData(normalItem);
        }
    }


    private Comparator<MultiItemEntity> mGroupEntityCmp = new Comparator<MultiItemEntity>() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/M/d H:mm:ss");

        public int compare(MultiItemEntity a, MultiItemEntity b) {
            Date d1, d2;
            try {
                d1 = format.parse(format.format(new Date(a.getTime())));
                d2 = format.parse(format.format(b.getTime()));
            } catch (ParseException e) {
                // 解析出错，则不进行排序
                Log.e("kim", "ComparatorDate--compare--SimpleDateFormat.parse--error");
                return 0;
            }
            if (d1.before(d2)) {
                return 1;
            } else {
                return -1;
            }
        }
    };

    private Comparator<TestNotification> mChildEntityCmp = new Comparator<TestNotification>() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/M/d H:mm:ss");

        public int compare(TestNotification a, TestNotification b) {
            Date d1, d2;
            try {
                d1 = format.parse(format.format(new Date(a.getTime())));
                d2 = format.parse(format.format(b.getTime()));
            } catch (ParseException e) {
                // 解析出错，则不进行排序
                Log.e("kim", "ComparatorDate--compare--SimpleDateFormat.parse--error");
                return 0;
            }
            if (d1.before(d2)) {
                return 1;
            } else {
                return -1;
            }
        }
    };

    public void deleteAllData() {
        Log.d("kim", "click ---------------");
        getData().clear();
        notifyDataSetChanged();
    }

    private int itemType(MultiItemEntity item) {
        if (item instanceof ImageItem) {
            return TYPE_IMAGE;
        } else if (item instanceof NormalItem) {
            return TYPE_NORMAL;
        } else if (item instanceof Level0Item) {
            return TYPE_LEVEL_0;
        } else if (item instanceof Level1Item) {
            return TYPE_LEVEL_1;
        }
        return -1;
    }
}
