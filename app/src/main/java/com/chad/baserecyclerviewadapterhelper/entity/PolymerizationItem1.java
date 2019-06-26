package com.chad.baserecyclerviewadapterhelper.entity;

import com.chad.baserecyclerviewadapterhelper.adapter.ExpandableItemAdapter;
import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by ${Kim} on 19-3-28.
 */
public class PolymerizationItem1 extends AbstractExpandableItem<Level1Item> implements MultiItemEntity {

    private long time;

    private String title;
    private String content;
    private String pkg;
    private boolean isExpand;

    public PolymerizationItem1(String title, String content, String pkg, boolean isExpand) {
        this.title = title;
        this.content = content;
        this.pkg = pkg;
        this.isExpand = isExpand;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    @Override
    public int getItemType() {
        return 4;
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
        return 0;
    }

    @Override
    public String toString() {
        return "PolymerizationItem1{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", pkg='" + pkg + '\'' +
                ", isExpand=" + isExpand +
                '}';
    }

    @Override
    public int getLevel() {
        return 1;
    }
}
