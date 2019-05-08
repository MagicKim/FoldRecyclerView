package com.chad.baserecyclerviewadapterhelper.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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

    private PolymerizationItem0 item0 = new PolymerizationItem0("null", "屏蔽列表");

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
        addItemType(TYPE_GROUP_0, R.layout.item_expandable_lv0);
        addItemType(TYPE_GROUP_1, R.layout.item_expandable_lv1);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final MultiItemEntity item) {
        switch (holder.getItemViewType()) {

            case TYPE_NORMAL:
                final NormalItem normalItem = (NormalItem) item;
                holder.setText(R.id.tv_normal_title, normalItem.getTitle());
                holder.setText(R.id.tv_normal_content, normalItem.getContent());
                holder.setText(R.id.tv_normal_pkg, normalItem.getPkg());
                holder.setText(R.id.tv_normal_time, "时间：" + normalItem.getTime());
                TextView textViewDel = holder.getView(R.id.tv_normal_del);
                TextView textViewShield = holder.getView(R.id.tv_normal_shield);
//                textViewDel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        setItemDel(holder.getAdapterPosition(), normalItem);
//                    }
//                });
//
//                textViewShield.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        setItemShield(holder.getAdapterPosition(), normalItem);
//                    }
//                });

                break;
            case TYPE_LEVEL_0:
                switch (holder.getLayoutPosition() % 3) {
                    case 0:
                        holder.setImageResource(R.id.iv_head, R.mipmap.head_img0);
                        break;
                    case 1:
                        holder.setImageResource(R.id.iv_head, R.mipmap.head_img1);
                        break;
                    case 2:
                        holder.setImageResource(R.id.iv_head, R.mipmap.head_img2);
                        break;
                    default:
                        break;
                }
                final Level0Item lv0 = (Level0Item) item;
                final SwipeMenuLayout swipeMenuLayout = holder.getView(R.id.rl_normal_root_layout);
//                final RelativeLayout layoutLandscape = holder.getView(R.id.l0_layout_image);
                final LinearLayout layoutL0 = holder.getView(R.id.l0_layout);
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
                holder.setText(R.id.title, lv0.title)
                        .setText(R.id.sub_title, lv0.subTitle)
                        .setImageResource(R.id.iv, lv0.isExpanded() ? R.mipmap.arrow_b : R.mipmap.arrow_r);
                layoutL0.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getAdapterPosition();
                        Log.e("kim", "Level 0 item pos: " + pos);
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

                holder.getView(R.id.tv_question_del).setOnClickListener(new View.OnClickListener() {
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
                final FoldingLayout foldingPersonLayout = holder.getView(R.id.expandable_person_layout);
                LinearLayout linearLayout1 = holder.getView(R.id.ll_l1_layout);
                TextView textView = holder.getView(R.id.tv_image_expand_text);
                final ImageView imageViewIcon = holder.getView(R.id.iv_arrow);
                holder.setText(R.id.title, lv1.title + "   " + lv1.subTitle + lv1.content);
                textView.setText(lv1.content + "   " + lv1.subTitle);
                holder.getView(R.id.tv_assemble_del).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getAdapterPosition();
                        // 先获取到当前 item 的父 positon，再移除自己
                        int positionAtAll = getParentPositionInAll(pos - getHeaderLayoutCount());
                        Log.w(TAG, "positionAtAll =" + positionAtAll);
                        remove(pos - getHeaderLayoutCount());
                        if (positionAtAll != -1) {
                            IExpandable multiItemEntity = (IExpandable) getData().get(positionAtAll);
                            Log.e(TAG, "multiItemEntity =" + multiItemEntity.toString());
                            if (!hasSubItems(multiItemEntity)) {
                                getData().remove(multiItemEntity);
                                notifyDataSetChanged();
//                                remove(positionAtAll);
                            }
                        }
                    }
                });
                if (lv1.isExpand) {
                    foldingPersonLayout.setVisibility(View.VISIBLE);
//                    imageViewIcon.setVisibility(View.VISIBLE);
//                    if (lv1.hasExpand) {
//                        foldingPersonLayout.expand(false);
//                        imageViewIcon.setSelected(false);
//                    } else {
//                        foldingPersonLayout.collapse(false);
//                        imageViewIcon.setSelected(true);
//                    }
//                    linearLayout1.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (lv1.hasExpand) {
//                                foldingPersonLayout.collapse(true);
//                                imageViewIcon.setSelected(true);
//                            } else {
//                                foldingPersonLayout.expand(true);
//                                imageViewIcon.setSelected(false);
//                            }
//                            lv1.hasExpand = !lv1.hasExpand;
//                        }
//                    });

                } else {
                    foldingPersonLayout.setVisibility(View.GONE);
//                    imageViewIcon.setVisibility(View.GONE);
//                    linearLayout1.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Toast.makeText(mContext, "click not expand item", Toast.LENGTH_SHORT).show();
//                        }
//                    });
                }

                //屏蔽
                holder.getView(R.id.tv_assemble_shield).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setItemShield(holder.getAdapterPosition(), lv1);
                    }
                });


                //取消屏蔽
//                holder.getView(R.id.tv_no_shield).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        int pos = holder.getAdapterPosition();
//                        // 先获取到当前 item 的父 positon，再移除自己
//                        int positionAtAll = getParentPositionInAll(pos);
//                        remove(pos);
//                        if (lv1.isExpand) {
//                            ImageItem imageItem = new ImageItem(lv1.title, lv1.subTitle, lv1.content);
//                            addData(imageItem);
//                        } else {
//                            addData(new NormalItem(lv1.title, lv1.content, lv1.subTitle));
//                        }
//
//                        if (positionAtAll != -1) {
//                            IExpandable multiItemEntity = (IExpandable) getData().get(positionAtAll);
//                            if (!hasSubItems(multiItemEntity)) {
//                                remove(positionAtAll);
//                            }
//                        }
//                    }
//                });
                break;
            case TYPE_GROUP_0:
                holder.setImageResource(R.id.iv_head, R.mipmap.head_img0);
                final PolymerizationItem0 p0 = (PolymerizationItem0) item;
                holder.setText(R.id.title, p0.getTitle())
                        .setText(R.id.sub_title, p0.getPkg())
                        .setImageResource(R.id.iv, p0.isExpanded() ? R.mipmap.arrow_b : R.mipmap.arrow_r);
                holder.getView(R.id.l0_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getAdapterPosition();
                        Log.d(TAG, "Level 0 item pos: " + pos);
                        if (p0.isExpanded()) {
                            collapse(pos);
                        } else {
                            expand(pos);
                        }
                    }
                });

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int pos = holder.getAdapterPosition();
                        // 先获取到当前 item 的父 positon，再移除自己
                        int positionAtAll = getParentPositionInAll(pos);
                        remove(pos);
                        if (positionAtAll != -1) {
                            IExpandable multiItemEntity = (IExpandable) getData().get(positionAtAll);
                            if (!hasSubItems(multiItemEntity)) {
                                remove(positionAtAll);
                            }
                        }
                        return true;
                    }
                });
                break;
            case TYPE_GROUP_1:
                final PolymerizationItem1 p1 = (PolymerizationItem1) item;
                final FoldingLayout foldingLayout1 = holder.getView(R.id.expandable_person_layout);
                LinearLayout linearLayout = holder.getView(R.id.ll_l1_layout);
                TextView textView1 = holder.getView(R.id.tv_image_expand_text);
                final ImageView imageView = holder.getView(R.id.iv_arrow);
                holder.setText(R.id.title, p1.getTitle() + "   " + p1.getPkg());
                textView1.setText(p1.getContent());
                holder.getView(R.id.tv_no_shield).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setNotShield(holder.getAdapterPosition(), item);
                    }
                });
                holder.getView(R.id.tv_shield_del).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setItemDel(holder.getAdapterPosition(), item);
                    }
                });
                break;
            default:
                break;
        }
    }

    public void groupAddOriginData() {

    }

    //删除
    private void setItemDel(int pos, MultiItemEntity item) {
        Log.d(TAG, "click remove");
        // 先获取到当前 item 的父 positon，再移除自己
        int positionAtAll = getParentPositionInAll(pos);
        remove(pos);
        if (positionAtAll != -1) {
            IExpandable multiItemEntity = (IExpandable) getData().get(positionAtAll);
            if (!hasSubItems(multiItemEntity)) {
                remove(positionAtAll);
            }
        }
    }


    //聚合转换
/*    public void addGroupItem(TestNotification notification) {
        notificationArrayList.add(notification);
        ArrayList<Long> childTimeList = new ArrayList<>();
        ArrayList<MultiItemEntity> entityList = new ArrayList<>();
        Map<String, List<TestNotification>> resultMap = new LinkedHashMap<>();
        for (TestNotification testN : notificationArrayList) {
            String pkg = testN.getPkg();
            if (resultMap.containsKey(pkg)) {
                resultMap.get(pkg).add(testN);
            } else {
                List<TestNotification> tmp = new LinkedList<>();
                tmp.add(testN);
                resultMap.put(pkg, tmp);
            }
        }
        for (String key : resultMap.keySet()) {
            Level0Item level0Item = new Level0Item(key, "---聚合---");
            List<TestNotification> notificationList = resultMap.get(key);
            for (TestNotification testNotification : notificationList) {
                childTimeList.add(testNotification.getTime());
            }
            Long maxTime = Collections.max(childTimeList);
            level0Item.time = maxTime;

            for (TestNotification testNotification : notificationList) {
                level0Item.addSubItem(new Level1Item(testNotification.getTitle(),
                        testNotification.getPkg(), testNotification.isExpandItem(), testNotification.getContent()));
            }
            entityList.add(level0Item);

        }
        Collections.sort(entityList, mGroupEntityCmp);
        setNewData(entityList);
    }*/

    //原始数据
    private ArrayList<TestNotification> notificationArrayList = new ArrayList<>();
    //转换的数据
    private ArrayList<MultiItemEntity> entityList = new ArrayList<>();

    //聚合转换
    public void addGroupItem(TestNotification xcnRecord) {
        //添加原始数据
        notificationArrayList.add(xcnRecord);
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
                    Level0Item foldParentItem = new Level0Item(xcnRecord.getTitle(), xcnRecord.getPkg());
                    for (TestNotification timeNotification : notificationList) {
                        childTimeList.add(timeNotification.getTime());
                        foldParentItem.addSubItem(new Level1Item(timeNotification.getTitle(), timeNotification.getPkg(), true, timeNotification.getContent()));
                    }
                    //排序
                    Long maxTime = Collections.max(childTimeList);
                    foldParentItem.time = maxTime;
                    foldParentItem.title = xcnRecord.getPkg();
                    foldParentItem.setExpanded(false);
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


    private ArrayList<TestNotification> testNotificationList = new ArrayList<>();

    public void add(TestNotification notification) {
        testNotificationList.add(notification);


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

    //屏蔽
    private void setItemShield(int pos, MultiItemEntity item) {

//        PolymerizationItem0 polymerizationItem0 = null;

        //获取包名
        String pkg = null;
        MultiItemEntity multiItemEntityLeve0;
        if (itemType(item) == TYPE_IMAGE) {
            ImageItem imItem = (ImageItem) item;
            pkg = imItem.getPkg();
        } else if (itemType(item) == TYPE_NORMAL) {
            NormalItem normalItem = (NormalItem) item;
            pkg = normalItem.getPkg();
        } else if (item.getItemType() == TYPE_LEVEL_0) {

        } else if (item.getItemType() == TYPE_LEVEL_1) {
            Level1Item level1Item = (Level1Item) item;
            pkg = level1Item.subTitle;
        }

        int positionAtAll = getParentPositionInAll(pos);
        multiItemEntityLeve0 = getData().get(positionAtAll);
        Log.w(TAG, "level1 pkg = " + pkg + "数据---->" + multiItemEntityLeve0.toString());

        for (MultiItemEntity entity : getData()) {
            if (itemType(entity) == TYPE_IMAGE) {
                ImageItem imItem = (ImageItem) entity;
                if (imItem.getPkg().equals(pkg)) {
                    item0.addSubItem(new PolymerizationItem1(imItem.getTitle(), imItem.getContent(), imItem.getPkg(), true));
                }
            } else if (itemType(entity) == TYPE_NORMAL) {
                NormalItem normalItem = (NormalItem) entity;
                if (normalItem.getPkg().equals(pkg)) {
                    item0.addSubItem(new PolymerizationItem1(normalItem.getTitle(), normalItem.getContent(), normalItem.getPkg(), false));
                }
            } else if (itemType(entity) == TYPE_LEVEL_0) {

            } else if (itemType(entity) == TYPE_LEVEL_1) {
                Level1Item level1Item = (Level1Item) item;
                if (level1Item.getPackageName().equals(pkg)) {
                    item0.addSubItem(new PolymerizationItem1(level1Item.title, level1Item.content, level1Item.subTitle, level1Item.hasExpand));
                }
            }
        }
        if (getData().contains(item0)) {
            Log.e(TAG, "PolymerizationItem0");
        } else {
            getData().add(item0);
            Log.e(TAG, "NO NO PolymerizationItem0");
        }
        Iterator<MultiItemEntity> iterator = getData().iterator();
        while (iterator.hasNext()) {
            MultiItemEntity next = iterator.next();
            if (itemType(next) == TYPE_NORMAL) {
                NormalItem normalItem = (NormalItem) next;
                if (normalItem.getPkg().equals(pkg)) {
                    iterator.remove();
                }
            } else if (itemType(next) == TYPE_IMAGE) {
                ImageItem imItem = (ImageItem) next;
                if (imItem.getPkg().equals(pkg)) {
                    iterator.remove();
                }
            } else if (itemType(next) == TYPE_LEVEL_0) {


            } else if (itemType(next) == TYPE_LEVEL_1) {
                Level1Item level1Item = (Level1Item) item;
                if (level1Item.getPackageName().equals(pkg)) {
                    iterator.remove();
                }
            }
        }
        getData().remove(multiItemEntityLeve0);
        setNewData(getData());
    }

    //取消屏蔽
    private void setNotShield(int pos, MultiItemEntity item) {
        List<PolymerizationItem1> newData = new ArrayList<>();
        PolymerizationItem1 p1 = (PolymerizationItem1) item;
        String pkg = p1.getPkg();
        Log.e("kim", "pkg = " + pkg);
        Level0Item level0Item = new Level0Item(pkg, pkg);
        int positionAtAll = getParentPositionInAll(pos);
        for (MultiItemEntity multiItemEntity : getData()) {
            if (multiItemEntity.getItemType() == TYPE_GROUP_1) {
                PolymerizationItem1 item1 = (PolymerizationItem1) multiItemEntity;
                if (item1.getPkg().equals(pkg)) {
                    level0Item.addSubItem(new Level1Item(item1.getTitle(), item1.getPkg(), item1.isExpand(), item1.getContent()));
                    newData.add(item1);
                }
            }
        }
        for (PolymerizationItem1 item1 : newData) {
            removeDataFromParent(item1);
            IExpandable multiItemEntity = (IExpandable) getData().get(positionAtAll);
            if (!hasSubItems(multiItemEntity)) {
                remove(positionAtAll);
            }
            getData().remove(item1);
        }
        getData().add(level0Item);
        Log.w(TAG, "get data = " + getData().toString());
        setNewData(getData());
        newData.clear();
    }

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
