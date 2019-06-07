package com.chad.baserecyclerviewadapterhelper.entity;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.baserecyclerviewadapterhelper.adapter.ExpandableItemAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by luoxw on 2016/8/10.
 */

public class Level1Item implements MultiItemEntity {
    public long time;
    public String title;
    public String content;
    public String subTitle;

    public boolean isExpand;

    public boolean hasExpand;

    public Level1Item() {
    }

    public Level1Item(String title, String subTitle, boolean expand, String content, long time) {
        this.subTitle = subTitle;
        this.title = title;
        this.isExpand = expand;
        this.content = content;
        this.time = time;
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
    public long getTime() {
        return time;
    }


    @Override
    public String toString() {
        return "Level1Item{" +
                "time=" + time +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", isExpand=" + isExpand +
                ", hasExpand=" + hasExpand +
                '}';
    }
}