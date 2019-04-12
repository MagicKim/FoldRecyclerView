package com.chad.baserecyclerviewadapterhelper.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.animation.FoldingLayout;
import com.chad.baserecyclerviewadapterhelper.data.DataManager;
import com.chad.baserecyclerviewadapterhelper.entity.ImageItem;
import com.chad.baserecyclerviewadapterhelper.entity.Level0Item;
import com.chad.baserecyclerviewadapterhelper.entity.Level1Item;
import com.chad.baserecyclerviewadapterhelper.entity.MultipleItem;
import com.chad.baserecyclerviewadapterhelper.entity.NormalItem;
import com.chad.baserecyclerviewadapterhelper.entity.PolymerizationItem0;
import com.chad.baserecyclerviewadapterhelper.entity.PolymerizationItem1;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.Iterator;
import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 * modify by AllenCoder
 */
public class MultipleItemQuickAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    private static final String TAG = "MultipleItemQuickAdapter";

    public static final int TYPE_HEADER = 4;
    //普通
    public static final int TYPE_NORMAL = 2;
    //折叠
    public static final int TYPE_IMAGE = 3;
    //屏蔽
    public static final int TYPE_GROUP_0 = 5;

    public static final int TYPE_GROUP_1 = 6;

    private PolymerizationItem0 item0 = new PolymerizationItem0("null", "屏蔽列表");

    public MultipleItemQuickAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_HEADER, R.layout.head_view);
        addItemType(TYPE_NORMAL, R.layout.item_text_view);
        addItemType(TYPE_IMAGE, R.layout.recycler_expandable_item);
        addItemType(TYPE_GROUP_0, R.layout.item_expandable_lv0);
        addItemType(TYPE_GROUP_1, R.layout.item_expandable_lv1);

    }


    @Override
    protected void convert(final BaseViewHolder holder, final MultiItemEntity item) {
        switch (holder.getItemViewType()) {
            case TYPE_HEADER:
                holder.getView(R.id.iv_delete_all).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("ccdata", "click ---------------");
                        getData().clear();
                        notifyDataSetChanged();
                    }
                });
                break;
            case TYPE_NORMAL:
                final NormalItem normalItem = (NormalItem) item;
                holder.setText(R.id.tv_normal_title, normalItem.getTitle());
                holder.setText(R.id.tv_normal_content, normalItem.getContent());
                holder.setText(R.id.tv_normal_pkg, normalItem.getPkg());
                holder.setText(R.id.tv_normal_time, "时间：" + normalItem.getTime());
                TextView textViewDel = holder.getView(R.id.tv_normal_del);
                TextView textViewShield = holder.getView(R.id.tv_normal_shield);
                textViewDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setItemDel(holder.getAdapterPosition(), normalItem);
                    }
                });

                textViewShield.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setItemShield(holder.getAdapterPosition(), normalItem);
                    }
                });

                break;
            case TYPE_IMAGE:
                final ImageItem imageItem = (ImageItem) item;
                final LinearLayout llContent = holder.getView(R.id.expand_button);
                final FoldingLayout foldingLayout = holder.getView(R.id.expandable_layout);
                final ImageView iv_arrow = holder.getView(R.id.expand_arrow);
                holder.setText(R.id.tv_image_expand_text, imageItem.getContent());
                Log.d("kim", "is EXPANDED = " + imageItem.isExpanded());
                if (imageItem.isExpanded()) {
                    llContent.setSelected(false);
                    foldingLayout.expand(false);
                    iv_arrow.setSelected(false);
                } else {
                    llContent.setSelected(true);
                    foldingLayout.collapse(false);
                    iv_arrow.setSelected(true);
                }

                llContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (imageItem.isExpanded()) {
                            llContent.setSelected(false);
                            foldingLayout.collapse(true);
                            iv_arrow.setSelected(true);
                        } else {
                            llContent.setSelected(true);
                            foldingLayout.expand(true);
                            iv_arrow.setSelected(false);
                        }
                        imageItem.setExpanded(!imageItem.isExpanded());
                        imageItem.setSelected(!imageItem.isSelected());
                    }
                });

                holder.setText(R.id.expand_text, imageItem.getTitle());
                holder.setText(R.id.expand_pkg, imageItem.getPkg());
                holder.setText(R.id.tv_expand_time, "时间：" + imageItem.getTime());
                TextView textViewDelImage = holder.getView(R.id.tv_image_del);
                TextView textViewShieldImage = holder.getView(R.id.tv_image_shield);
                textViewDelImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setItemDel(holder.getAdapterPosition(), imageItem);
                    }
                });

                textViewShieldImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setItemShield(holder.getAdapterPosition(), imageItem);
                    }
                });

                break;
        }
    }

    //删除
    private void setItemDel(int pos, MultiItemEntity item) {
        Log.d("ccc", "click remove");
        remove(pos);
        if (itemType(item) == TYPE_NORMAL) {
        }

    }

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
        }

        int positionAtAll = getParentPositionInAll(pos);
        multiItemEntityLeve0 = getData().get(positionAtAll);
        Log.w("kim", "level1 pkg = " + pkg + "数据---->" + multiItemEntityLeve0.toString());

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
            }
        }
        if (getData().contains(item0)) {
            Log.e("kim", "包含PolymerizationItem0");
        } else {
            getData().add(item0);
            Log.e("kim", "NO NO 包含PolymerizationItem0");
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
            }
        }
        setNewData(getData());
    }

    private int itemType(MultiItemEntity item) {
        if (item instanceof ImageItem) {
            return TYPE_IMAGE;
        } else if (item instanceof NormalItem) {
            return TYPE_NORMAL;
        }
        return -1;
    }

}
