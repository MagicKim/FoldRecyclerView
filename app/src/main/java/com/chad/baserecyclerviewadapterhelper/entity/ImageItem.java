package com.chad.baserecyclerviewadapterhelper.entity;

import com.chad.baserecyclerviewadapterhelper.adapter.ExpandableItemAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;

public class ImageItem implements MultiItemEntity {

    private Long time;
    private String title;
    private String content;
    private String pkg;
    private boolean isSelected;
    private boolean isExpanded;

    public ImageItem() {

    }

    public ImageItem(String title, String content, String pkg) {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int getItemType() {
        return ExpandableItemAdapter.TYPE_IMAGE;
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
        return "ImageItem{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", isSelected=" + isSelected +
                ", isExpanded=" + isExpanded +
                '}';
    }
}
