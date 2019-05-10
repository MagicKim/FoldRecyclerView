package com.chad.baserecyclerviewadapterhelper.util;

import android.util.Log;

import com.chad.baserecyclerviewadapterhelper.entity.TestNotification;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by ${Kim} on 19-5-10.
 */
public class SortUtils {

    public static Comparator<MultiItemEntity> sortGroupEntityCmp = new Comparator<MultiItemEntity>() {
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

    public static Comparator<TestNotification> sortChildEntityCmp = new Comparator<TestNotification>() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/M/d H:mm:ss");

        public int compare(TestNotification a, TestNotification b) {
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

}
