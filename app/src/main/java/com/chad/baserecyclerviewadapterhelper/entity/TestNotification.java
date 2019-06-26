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
    private boolean isShield;
    private int level;


    public TestNotification(int id, String pkg, String content, String title, boolean isShield, long time, int level) {
        this.id = id;
        this.pkg = pkg;
        this.content = content;
        this.title = title;
        this.isShield = isShield;
        this.time = time;
        this.level = level;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "TestNotification{" +
                "time=" + time +
                ", id=" + id +
                ", pkg='" + pkg + '\'' +
                ", content='" + content + '\'' +
                ", title='" + title + '\'' +
                ", isShield=" + isShield +
                '}';
    }
}
