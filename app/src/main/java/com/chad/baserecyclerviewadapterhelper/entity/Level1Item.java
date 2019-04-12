package com.chad.baserecyclerviewadapterhelper.entity;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.baserecyclerviewadapterhelper.adapter.ExpandableItemAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by luoxw on 2016/8/10.
 */

public class Level1Item extends AbstractExpandableItem<Level1Item> implements MultiItemEntity {
    public Long time;
    public String title;
    public String content;
    public String subTitle;

    public boolean isExpand;

    public boolean hasExpand;

    public Level1Item() {
    }

    public Level1Item(String title, String subTitle, boolean expand, String content) {
        this.subTitle = subTitle;
        this.title = title;
        this.isExpand = expand;
        this.content = content;
    }

    @Override
    public int getItemType() {
        return ExpandableItemAdapter.TYPE_LEVEL_1;
    }

    @Override
    public String getPackageName() {
        return subTitle;
    }

    @Override
    public Long getTime() {
        return time;
    }

    @Override
    public int getLevel() {
        return 1;
    }

    @Override
    public String toString() {
        return "Level1Item{" +
                "title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                '}';
    }
}