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
    public String pkg;
    public int itemLevel;

    public Level1Item() {
    }

    public Level1Item(String title, String pkg, String content, long time, int itemLevel) {
        this.pkg = pkg;
        this.title = title;
        this.content = content;
        this.time = time;
        this.itemLevel = itemLevel;
    }

    @Override
    public int getItemType() {
        return ExpandableItemAdapter.TYPE_LEVEL_1;
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
    public String toString() {
        return "Level1Item{" +
                "time=" + time +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", subTitle='" + pkg + '\'' +
                '}';
    }
}