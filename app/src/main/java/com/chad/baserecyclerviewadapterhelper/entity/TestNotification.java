package com.chad.baserecyclerviewadapterhelper.entity;

import java.util.Collection;

/**
 * Created by ${Kim} on 19-3-22.
 */
public class TestNotification {

    private long time;
    private int id;
    private String pkg;
    private String content;
    private String title;
    private boolean isExpandItem;
    private boolean isShield;


    public TestNotification(int id, String pkg, String content, String title, boolean isExpandItem, boolean isShield, long time) {
        this.id = id;
        this.pkg = pkg;
        this.content = content;
        this.title = title;
        this.isExpandItem = isExpandItem;
        this.isShield = isShield;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean isExpandItem() {
        return isExpandItem;
    }

    public void setExpandItem(boolean expandItem) {
        isExpandItem = expandItem;
    }

    public boolean isShield() {
        return isShield;
    }

    public void setShield(boolean shield) {
        isShield = shield;
    }

    public long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }


    @Override
    public String toString() {
        return "TestNotification{" +
                "time=" + time +
                ", id=" + id +
                ", pkg='" + pkg + '\'' +
                ", content='" + content + '\'' +
                ", title='" + title + '\'' +
                ", isExpandItem=" + isExpandItem +
                ", isShield=" + isShield +
                '}';
    }
}
