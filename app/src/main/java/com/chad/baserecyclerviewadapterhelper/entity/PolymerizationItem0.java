package com.chad.baserecyclerviewadapterhelper.entity;

import com.chad.baserecyclerviewadapterhelper.adapter.ExpandableItemAdapter;
import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by ${Kim} on 19-3-28.
 */
public class PolymerizationItem0 extends AbstractExpandableItem<PolymerizationItem1> implements MultiItemEntity {

    private Long time;
    private String pkg;
    private String title;

    public PolymerizationItem0() {
    }

    public PolymerizationItem0(String pkg, String title) {
        this.pkg = pkg;
        this.title = title;
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

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getItemType() {
        return ExpandableItemAdapter.TYPE_GROUP_0;
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
        return "PolymerizationItem0{" +
                "time=" + time +
                ", pkg='" + pkg + '\'' +
                ", title='" + title + '\'' +
                ", mExpandable=" + mExpandable +
                ", mSubItems=" + mSubItems +
                '}';
    }
}
