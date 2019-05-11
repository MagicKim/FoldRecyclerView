package com.chad.baserecyclerviewadapterhelper.util;

import android.text.TextUtils;
import android.util.Log;

import com.chad.baserecyclerviewadapterhelper.entity.Level0Item;
import com.chad.baserecyclerviewadapterhelper.entity.Level1Item;
import com.chad.baserecyclerviewadapterhelper.entity.NormalItem;
import com.chad.baserecyclerviewadapterhelper.entity.TestNotification;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ListUtils {
    /**
     * list 集合分组
     *
     * @param list    待分组集合
     * @param groupBy 分组Key算法
     * @param <K>     分组Key类型
     * @param <V>     行数据类型
     * @return 分组后的Map集合
     */
    public static <K, V> Map<K, List<V>> groupBy(List<V> list, GroupBy<K, V> groupBy) {
        return groupBy((Collection<V>) list, groupBy);
    }
    /**
     * list 集合分组
     *
     * @param list    待分组集合
     * @param groupBy 分组Key算法
     * @param <K>     分组Key类型
     * @param <V>     行数据类型
     * @return 分组后的Map集合
     */
    public static <K, V> Map<K, List<V>> groupBy(Collection<V> list, GroupBy<K, V> groupBy) {
        Map<K, List<V>> resultMap = new LinkedHashMap<>();

        for (V e : list) {

            K k = groupBy.groupBy(e);
            if (resultMap.containsKey(k)) {
                resultMap.get(k).add(e);
            } else {
                List<V> tmp = new LinkedList<V>();
                tmp.add(e);
                resultMap.put(k, tmp);
            }
        }
        return resultMap;
    }
    /**
     * List分组
     *
     * @param <K> 返回分组Key
     * @param <V> 分组行
     */
    public interface GroupBy<K, V> {
        K groupBy(V row);
    }

/*    private void assembleData(TestNotification xcnRecord) {
        List<MultiItemEntity> entityList = new ArrayList<>();
        //构建时间集合
        ArrayList<Long> childTimeList = new ArrayList<>();
        //使用map分组
        Map<String, List<TestNotification>> resultMap = new LinkedHashMap<>();
        for (TestNotification record : notificationArrayList) {
            String pkg = record.getPkg();
            if (resultMap.containsKey(pkg)) {
                resultMap.get(pkg).add(record);
            } else {
                List<TestNotification> tmp = new LinkedList<>();
                tmp.add(record);
                resultMap.put(pkg, tmp);
            }
        }
        //同一包名下,2条消息才会聚合
        if (resultMap.get(xcnRecord.getPkg()).size() < 2) {
            Log.e(TAG, "添加默认的布局 同一个包名下面的第一条消息不聚合 " + xcnRecord.getPkg());
            NormalItem normalItem = new NormalItem(xcnRecord.getTitle(), xcnRecord.getContent(), xcnRecord.getPkg());
            normalItem.setTime(xcnRecord.getTime());
            entityList.add(normalItem);
        } else {
            Log.e(TAG, "开始聚合布局------>" + xcnRecord.getPkg());
            Iterator<MultiItemEntity> iterator = getData().iterator();
            while (iterator.hasNext()) {
                MultiItemEntity next = iterator.next();
                if (itemType(next) == TYPE_NORMAL) {
                    NormalItem normalItem = (NormalItem) next;
                    if (normalItem.getPackageName().equals(xcnRecord.getPkg())) {
                        Log.e(TAG, "聚合布局,需要删除视图集合中normal类型的数据 " + xcnRecord.getPkg());
                        iterator.remove();
                    }
                }
            }
            //遍历map集合
            for (String key : resultMap.keySet()) {
                Log.e(TAG, "遍历map");
                //如果遍历的key 与刚刚来的通知的package是一样的才聚合
                if (TextUtils.equals(key, xcnRecord.getPkg())) {
                    //首先删除视图 getData()里面的数据,防止重复添加
                    Iterator<MultiItemEntity> iteratorGroup = getData().iterator();
                    while (iteratorGroup.hasNext()) {
                        MultiItemEntity next = iteratorGroup.next();
                        if (itemType(next) == TYPE_LEVEL_0) {
                            Log.e(TAG, " TYPE_LEVEL_0");
                            Level0Item level0Item = (Level0Item) next;
                            if (level0Item.getPackageName().equals(xcnRecord.getPkg())) {
                                Log.e(TAG, "聚合布局,需要删除原来已经聚合的内容 " + xcnRecord.getPkg());
                                iteratorGroup.remove();
                            }
                        }
                    }
                    Log.e(TAG, "如果这条消息与map中的key值一样,那么聚合");
                    //添加聚合实体类
                    List<TestNotification> notificationList = resultMap.get(key);
                    Collections.sort(notificationList, SortUtils.sortChildEntityCmp);
                    Level0Item foldParentItem = new Level0Item();
                    for (TestNotification timeNotification : notificationList) {
                        childTimeList.add(timeNotification.getTime());
                        foldParentItem.addSubItem(new Level1Item(timeNotification.getTitle(), timeNotification.getPkg(), true, timeNotification.getContent(), timeNotification.getTime()));
                    }
                    //排序
                    Long maxTime = Collections.max(childTimeList);
                    for (TestNotification timeNotification : notificationList) {
                        if (timeNotification.getTime() == maxTime) {
                            foldParentItem.time = maxTime;
                            foldParentItem.setExpanded(false);
                            foldParentItem.title = timeNotification.getTitle();
                            foldParentItem.subTitle = timeNotification.getPkg();
                        }
                    }
                    entityList.add(foldParentItem);
                }
            }
        }
        //将转换的集合加入视图需要的数据源getData
        getData().addAll(entityList);
        //排序
        Collections.sort(getData(), SortUtils.sortGroupEntityCmp);
        //通知刷新UI
        setNewData(getData());
        Log.d("kim", "数据 = " + getData().toString());
        //清空数据转换集合
//        entityList.clear();
    }*/


}
