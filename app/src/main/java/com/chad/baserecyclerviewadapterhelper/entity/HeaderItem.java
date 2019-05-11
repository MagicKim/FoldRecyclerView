package com.chad.baserecyclerviewadapterhelper.entity;

import com.chad.baserecyclerviewadapterhelper.adapter.ExpandableItemAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by ${Kim} on 19-3-20.
 */
public class HeaderItem implements MultiItemEntity {

    private long time;
    private String title;

    public HeaderItem(String title, Long time) {
        this.title = title;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    @Override
    public int getItemType() {
        return 5;
    }

    @Override
    public String getPackageName() {
        return null;
    }

    @Override
    public String toString() {
        return "HeaderItem{" +
                "time=" + time +
                ", title='" + title + '\'' +
                '}';
    }
}
