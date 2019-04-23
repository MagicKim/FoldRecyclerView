package com.chad.baserecyclerviewadapterhelper.util;

import android.support.v7.util.DiffUtil;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${Kim} on 19-4-22.
 */
public class DataCallback extends DiffUtil.Callback {

    private List<MultiItemEntity> old_me, new_me;

    public DataCallback(List<MultiItemEntity> old_me, List<MultiItemEntity> new_me) {
        this.old_me = old_me;
        this.new_me = new_me;
    }

    @Override
    public int getOldListSize() {
        return old_me.size();
    }

    @Override
    public int getNewListSize() {
        return new_me.size();
    }

    @Override
    public boolean areItemsTheSame(int i, int i1) {
        return old_me.get(i).getTime() == new_me.get(i1).getTime();
    }

    @Override
    public boolean areContentsTheSame(int i, int i1) {
        return old_me.get(i).getTime() == new_me.get(i1).getTime();
    }
}
