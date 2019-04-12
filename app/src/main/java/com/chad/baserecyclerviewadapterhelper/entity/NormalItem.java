package com.chad.baserecyclerviewadapterhelper.entity;

import com.chad.baserecyclerviewadapterhelper.adapter.ExpandableItemAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;

public class NormalItem implements MultiItemEntity {

    private String title;
    private Long time;
    private String content;

    private String pkg;

    public NormalItem() {

    }

    public NormalItem(String title, String content, String pkg) {
        this.title = title;
        this.content = content;
        this.pkg = pkg;
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
    public Long getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "NormalItem{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
