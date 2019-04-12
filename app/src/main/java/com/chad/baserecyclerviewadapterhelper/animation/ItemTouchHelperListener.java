package com.chad.baserecyclerviewadapterhelper.animation;

/**
 * Created by ${Kim} on 19-3-18.
 */
public interface ItemTouchHelperListener {
    //数据删除
    void onItemDissmiss(int position);

    void onItemShield(int position);
}
