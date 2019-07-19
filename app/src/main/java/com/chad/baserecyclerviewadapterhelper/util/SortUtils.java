package com.chad.baserecyclerviewadapterhelper.util;

import android.util.Log;

import com.chad.baserecyclerviewadapterhelper.entity.Level0Item;
import com.chad.baserecyclerviewadapterhelper.entity.Level1Item;
import com.chad.baserecyclerviewadapterhelper.entity.TestNotification;
import com.chad.library.adapter.base.entity.IExpandable;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ${Kim} on 19-5-10.
 */
public class SortUtils {

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d H:mm:ss SSS", Locale.CHINA);

    public static Comparator<MultiItemEntity> sortGroupEntityCmp = new Comparator<MultiItemEntity>() {
        public int compare(MultiItemEntity a, MultiItemEntity b) {
            int sort = 0;
            int level = b.getItemLevel() - a.getItemLevel();
            if (level != 0) {
                sort = (level > 0) ? 2 : -1;
            } else {
                if (a instanceof Level0Item || b instanceof Level0Item) {
                    Log.e("SortUtils", "----------find Level0Item");
                }
                Date d1, d2;
                try {
                    d1 = format.parse(format.format(a.getTime()));
                    d2 = format.parse(format.format(b.getTime()));
                    sort = (d1.before(d2)) ? 1 : -2;
                } catch (ParseException e) {
                    // 解析出错，则不进行排序
                    Log.e("kim", "ComparatorDate--compare--SimpleDateFormat.parse--error");
                    return sort;
                }
            }
            return sort;
        }
    };


    public static Comparator<TestNotification> sortChildEntityCmp = new Comparator<TestNotification>() {
        public int compare(TestNotification a, TestNotification b) {
            int sort = 0;
            int level = b.getLevel() - a.getLevel();
            if (level != 0) {
                sort = (level > 0) ? 2 : -1;
            } else {
                Date d1, d2;
                try {
                    d1 = format.parse(format.format(a.getTime()));
                    d2 = format.parse(format.format(b.getTime()));
                    sort = (d1.before(d2)) ? 1 : -2;
                } catch (ParseException e) {
                    // 解析出错，则不进行排序
                    Log.e("kim", "ComparatorDate--compare--SimpleDateFormat.parse--error");
                    return sort;
                }
            }
            return sort;
        }
    };

}
