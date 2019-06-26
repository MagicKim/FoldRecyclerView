package com.chad.baserecyclerviewadapterhelper.entity;

import com.chad.baserecyclerviewadapterhelper.adapter.ExpandableItemAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;

public class NormalItem implements MultiItemEntity {

    private String title;
    private long time;
    private String content;
    private String pkg;
    private int itemLevel;

    public NormalItem() {

    }

    public void setItemLevel(int itemLevel) {
        this.itemLevel = itemLevel;
    }

    public NormalItem(String title, String content, String pkg, int itemLevel) {
        this.title = title;
        this.content = content;
        this.pkg = pkg;
        this.itemLevel = itemLevel;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int getItemType() {
        return ExpandableItemAdapter.TYPE_NORMAL;
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
        return "NormalItem{" +
                "title='" + title + '\'' +
                ", time=" + time +
                ", content='" + content + '\'' +
                ", pkg='" + pkg + '\'' +
                '}';
    }
}
