package com.chad.baserecyclerviewadapterhelper.data;

import android.util.Log;

import com.chad.baserecyclerviewadapterhelper.entity.HeaderItem;
import com.chad.baserecyclerviewadapterhelper.entity.ImageItem;
import com.chad.baserecyclerviewadapterhelper.entity.Level0Item;
import com.chad.baserecyclerviewadapterhelper.entity.Level1Item;
import com.chad.baserecyclerviewadapterhelper.entity.NormalItem;
import com.chad.baserecyclerviewadapterhelper.entity.TestNotification;
import com.chad.baserecyclerviewadapterhelper.util.ListUtils;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ${Kim} on 19-3-28.
 */
public class DataManager {

//    private volatile static DataManager mInstance;

    private Map<String, List<TestNotification>> maps = new HashMap<>();

    private List<TestNotification> mOriginData = new ArrayList<>();

//    public static DataManager getInstance() {
//        if (mInstance == null) {
//            synchronized (DataManager.class) {
//                if (mInstance == null) {
//                    mInstance = new DataManager();
//                    Log.e("kim","hash code = "+mInstance.hashCode());
//                }
//            }
//        }
//        return mInstance;
//    }


    public List<TestNotification> getOriginData() {

        return mOriginData;
    }

    public void addOriginData(TestNotification testNotification) {
        Log.w("kim", "testNotification = " + testNotification.toString());
        mOriginData.add(testNotification);
        Log.w("kim", "addOriginData total size  = " + mOriginData.size());
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
            Level0Item level0Item = new Level0Item(key, "---聚合---", 1);
            List<TestNotification> testNotifications = maps.get(key);
            for (TestNotification testNotification : testNotifications) {
                level0Item.addSubItem(new Level1Item(testNotification.getTitle(),
                        testNotification.getPkg(), testNotification.getContent(), testNotification.getTime(), testNotification.getLevel()));
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


    public Map<String, List<TestNotification>> getMaps() {
        return maps;
    }

    public void removeOriginData() {

    }


    private Comparator<MultiItemEntity> mGroupEntityCmp = new Comparator<MultiItemEntity>() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/M/d H:mm:ss");

        public int compare(MultiItemEntity a, MultiItemEntity b) {
            Date d1, d2;
            try {
                d1 = format.parse(format.format(new Date(a.getTime())));
                d2 = format.parse(format.format(b.getTime()));
            } catch (ParseException e) {
                // 解析出错，则不进行排序
                Log.e("kim", "ComparatorDate--compare--SimpleDateFormat.parse--error");
                return 0;
            }
            if (d1.before(d2)) {
                return 1;
            } else {
                return -1;
            }
        }
    };

    public void groupEntity() {
//        getNormalData();
//        Map<String, List<TestNotification>> stringListMap = ListUtils.groupBy(mOriginData, new ListUtils.GroupBy<String, TestNotification>() {
//            @Override
//            public String groupBy(TestNotification row) {
//                String pkg = row.getPkg();
//
//                return pkg;
//            }
//        });
//
//        for (String key : stringListMap.keySet()) {
//            Log.w("kim", "key = " + key + " --- value ---" + stringListMap.get(key));
//        }
    }


}
