package com.chad.baserecyclerviewadapterhelper.entity;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.baserecyclerviewadapterhelper.adapter.ExpandableItemAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by luoxw on 2016/8/10.
 */
public class Level0Item extends AbstractExpandableItem<Level1Item> implements MultiItemEntity {

    public long time;
    public String title;
    public String pkg;

    public int itemLevel;


    public Level0Item() {

    }

    public Level0Item(String title, String pkg, int itemLevel) {
        this.pkg = pkg;
        this.title = title;
        this.itemLevel = itemLevel;
    }

    public void setItemLevel(int itemLevel) {
        this.itemLevel = itemLevel;
    }

    @Override
    public int getItemType() {
        return ExpandableItemAdapter.TYPE_LEVEL_0;
    }

    @Override
    public String getPackageName() {
        return pkg;
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public int getItemLevel() {
        return itemLevel;
    }

    @Override
    public int getLevel() {
        return 1;
    }

    @Override
    public String toString() {
        return "Level0Item{" +
                "title='" + title + '\'' +
                ", subTitle='" + pkg + '\'' +
                ", mSubItems=" + mSubItems +
                '}';
    }
}
