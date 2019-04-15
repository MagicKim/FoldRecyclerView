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
    public String subTitle;

    public Level0Item() {

    }

    public Level0Item(String title, String subTitle) {
        this.subTitle = subTitle;
        this.title = title;
        this.time = time;
    }

    @Override
    public int getItemType() {
        return ExpandableItemAdapter.TYPE_LEVEL_0;
    }

    @Override
    public String getPackageName() {
        return subTitle;
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public String toString() {
        return "Level0Item{" +
                "title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", mSubItems=" + mSubItems +
                '}';
    }
}
