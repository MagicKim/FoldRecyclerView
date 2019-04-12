package com.chad.baserecyclerviewadapterhelper.adapter;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.animation.FoldingLayout;
import com.chad.baserecyclerviewadapterhelper.entity.ImageItem;
import com.chad.baserecyclerviewadapterhelper.entity.Level0Item;
import com.chad.baserecyclerviewadapterhelper.entity.Level1Item;
import com.chad.baserecyclerviewadapterhelper.entity.NormalItem;
import com.chad.baserecyclerviewadapterhelper.entity.PolymerizationItem0;
import com.chad.baserecyclerviewadapterhelper.entity.PolymerizationItem1;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.IExpandable;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    PolymerizationItem0 item0 = new PolymerizationItem0("null", "屏蔽列表");

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ExpandableItemAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_HEADER, R.layout.head_view);
        addItemType(TYPE_LEVEL_0, R.layout.item_assemble_parent);
        addItemType(TYPE_LEVEL_1, R.layout.item_assemble_child);
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

                holder.setText(R.id.title, lv0.title)
                        .setText(R.id.sub_title, lv0.subTitle)
                        .setImageResource(R.id.iv, lv0.isExpanded() ? R.mipmap.arrow_b : R.mipmap.arrow_r);
                holder.getView(R.id.l0_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getAdapterPosition();
                        Log.d(TAG, "Level 0 item pos: " + pos);
                        if (lv0.isExpanded()) {
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
                        setItemDel(holder.getAdapterPosition(), item);
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
