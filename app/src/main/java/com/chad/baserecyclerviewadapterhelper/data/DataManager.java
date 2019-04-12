package com.chad.baserecyclerviewadapterhelper.data;

import android.util.Log;

import com.chad.baserecyclerviewadapterhelper.entity.HeaderItem;
import com.chad.baserecyclerviewadapterhelper.entity.ImageItem;
import com.chad.baserecyclerviewadapterhelper.entity.Level0Item;
import com.chad.baserecyclerviewadapterhelper.entity.Level1Item;
import com.chad.baserecyclerviewadapterhelper.entity.NormalItem;
import com.chad.baserecyclerviewadapterhelper.entity.TestNotification;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ${Kim} on 19-3-28.
 */
public class DataManager {

    private volatile static DataManager mInstance;

    private Map<String, List<TestNotification>> maps = new HashMap<>();

    private static ArrayList<TestNotification> mOriginData = new ArrayList<>();

    public static DataManager getInstance() {
        if (mInstance == null) {
            synchronized (DataManager.class) {
                if (mInstance == null) {
                    getNormalData();
                    mInstance = new DataManager();
                }
            }
        }
        return mInstance;
    }

    private static void getNormalData() {

        mOriginData.add(new TestNotification(1, "com.ecarx.cc", "周杰伦全新专辑发布!",
                "音乐消息1", false, false, 1555053240000L));
        mOriginData.add(new TestNotification(2, "com.ecarx.bb", "AirPods H1芯片有多强？- 你的耳朵里是台iPhone 4",
                "新闻消息2", true, false, 1335053240000L));
        mOriginData.add(new TestNotification(3, "com.ecarx.dd", "您的内存满了哦，请及时清理！",
                "系统消息3", false, false, 1455053240000L));
        mOriginData.add(new TestNotification(4, "com.ecarx.cc", "陈奕迅全新专辑发布!",
                "音乐消息4", true, false, 1655053240000L));
        mOriginData.add(new TestNotification(5, "com.ecarx.bb", "坚果Pro 2系统更新：下线“残废”功能",
                "新闻消息5", true, false, 1555053240000L));
        mOriginData.add(new TestNotification(6, "com.ecarx.dd", "有新的OTA升级包哦！",
                "系统消息6", false, false, 1855053240000L));
        mOriginData.add(new TestNotification(7, "com.ecarx.cc", "蔡依林全新专辑发布!",
                "音乐消息7", true, false, 1955053240000L));
        mOriginData.add(new TestNotification(8, "com.ecarx.cc", "陶喆全新专辑发布!",
                "音乐消息8", false, false, 1555054240000L));
        mOriginData.add(new TestNotification(9, "com.ecarx.cc", "朴树全新专辑发布!",
                "音乐消息9", true, false, 1555073240000L));
        mOriginData.add(new TestNotification(10, "com.ecarx.cc", "LadyGaga全新专辑发布!",
                "音乐消息10", false, false, 1555153240000L));
        mOriginData.add(new TestNotification(11, "com.ecarx.ee", "2019日本小姐冠军出炉 网友：越看越像吴京！",
                "娱乐消息11", false, false, 1255053240000L));
    }

    //聚合转换
    public ArrayList<MultiItemEntity> consistGroup() {
        ArrayList<MultiItemEntity> me = new ArrayList<>();

        for (TestNotification notification : mOriginData) {
            List<TestNotification> testNotifications = maps.get(notification.getPkg());
            if (testNotifications == null) {
                testNotifications = new ArrayList<>();
                testNotifications.add(notification);
                maps.put(notification.getPkg(), testNotifications);
            } else {
                testNotifications.add(notification);
            }
        }
        for (String key : maps.keySet()) {
            Log.w("kim", "key= " + key + " and value= " + maps.get(key));
            Level0Item level0Item = new Level0Item(key, "---聚合---");
            List<TestNotification> testNotifications = maps.get(key);
            for (TestNotification testNotification : testNotifications) {
                level0Item.addSubItem(new Level1Item(testNotification.getTitle(),
                        testNotification.getPkg(), testNotification.isExpandItem(), testNotification.getContent()));
            }
            me.add(level0Item);
        }
        HeaderItem headerItem = new HeaderItem("通知头部", 2555053240000L);
        me.add(headerItem);
        return me;
    }


//    public void addOriginGroupData(TestNotification notification) {
//        boolean needSort = true;
//        for (ExpandableGroupEntity2 expandableGroupEntity2 : groupEntity2s) {
//            if (expandableGroupEntity2.getHeader().equals(childEntity2.getDate())) {
//                needSort = false;
//                expandableGroupEntity2.getChildren().add(childEntity2);
//                break;
//            }
//        }
//        if (needSort) {
//            ExpandableGroupEntity2 expandableGroupEntity2 = new ExpandableGroupEntity2();
//            expandableGroupEntity2.setHeader(childEntity2.getDate());
//            ArrayList<ChildEntity2> list = new ArrayList<>();
//            list.add(childEntity2);
//            expandableGroupEntity2.setChildren(list);
//            groupEntity2s.add(expandableGroupEntity2);
//            sort();
//        }
//    }

    public void sort() {

    }

    private final Comparator<MultiItemEntity> mGroupEntityCmp = new Comparator<MultiItemEntity>() {
        public int compare(MultiItemEntity a, MultiItemEntity b) {
            final long na = Long.valueOf(a.getTime());
            final long nb = Long.valueOf(b.getTime());
            return (int) (na - nb);
        }
    };

    public Map<String, List<TestNotification>> getMaps() {
        return maps;
    }

    public void removeOriginData() {

    }


    //非聚合数据
    public ArrayList<MultiItemEntity> getTransferData() {
        ArrayList<MultiItemEntity> res = new ArrayList<>();
        for (TestNotification testNotification : mOriginData) {
            if (testNotification.isExpandItem()) {
                ImageItem iItem = new ImageItem(testNotification.getTitle(), testNotification.getContent(), testNotification.getPkg());
                iItem.setTime(testNotification.getTime());
                res.add(iItem);
            } else {
                NormalItem nItem = new NormalItem(testNotification.getTitle(), testNotification.getContent(), testNotification.getPkg());
                nItem.setTime(testNotification.getTime());
                res.add(nItem);
            }
        }
        HeaderItem headerItem = new HeaderItem("通知中心", 2555053240000L);
        res.add(headerItem);
        Collections.sort(res, mGroupEntityCmp);
        return res;
    }


}
