package com.chad.baserecyclerviewadapterhelper.language.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.language.LanguageInfo;


public class LanguageAdapter extends BaseAdapter {

    private Context mContext;

    private List<LanguageInfo> mListLanguageInfo;

    private int selectItem = -1;

    /**
     * @param mContext
     * @param mListLanguageInfo
     */
    public LanguageAdapter(Context mContext,
                           List<LanguageInfo> mListLanguageInfo) {
        super();
        this.mContext = mContext;
        this.mListLanguageInfo = mListLanguageInfo;
    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

    @Override
    public int getCount() {
        return mListLanguageInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final LanguageInfo mLanguageInfo = mListLanguageInfo.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.language_item, null);
            viewHolder.mImageView = (ImageView) convertView
                    .findViewById(R.id.img_label);
            viewHolder.mLanguage = (TextView) convertView
                    .findViewById(R.id.txt_language);
            viewHolder.mView = (RelativeLayout) convertView.findViewById(R.id.item_lv);
            viewHolder.mView.setTag(viewHolder);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mLanguage.setText(mLanguageInfo.getLanguage());
        //当前选中项
        if (position == selectItem) {
            viewHolder.mImageView.setVisibility(View.VISIBLE);
            viewHolder.mLanguage.setTextColor(mContext.getResources().getColor(
                    R.color.font_orange));
        } else {
            viewHolder.mImageView.setVisibility(View.INVISIBLE);
            viewHolder.mLanguage.setTextColor(mContext.getResources().getColor(
                    R.color.black));
        }
        return convertView;
    }

    class ViewHolder {
        ImageView mImageView;
        TextView mLanguage;
        RelativeLayout mView;
    }

}
