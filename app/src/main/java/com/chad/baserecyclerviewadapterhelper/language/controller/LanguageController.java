
package com.chad.baserecyclerviewadapterhelper.language.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import com.chad.baserecyclerviewadapterhelper.language.LanguageInfo;

import android.content.Context;

public class LanguageController {
	
	private static LanguageController instance;// 单列模式
	private Context mContext;
	
	/**
	 * @param mContext
	 */
	public LanguageController(Context mContext) {
		this.mContext = mContext;
	}

	/**
	 * 单列模式
	 * 
	 * @param mContext 上下文
	 * @return UpdateController mUpdateController
	 */
	public static LanguageController getInstance(Context mContext) {
		if (instance == null) {
			instance = new LanguageController(mContext.getApplicationContext());
		}
		return instance;
	}
	
	
	
	/**
	 * 初始化列表数据
	 */
	public List<LanguageInfo> getListData() {
			List<LanguageInfo> mListLanguageInfo = new ArrayList<LanguageInfo>();
	        LanguageInfo mLanguageInfo1 = new LanguageInfo();
	        LanguageInfo mLanguageInfo2 = new LanguageInfo();
	        LanguageInfo mLanguageInfo3 = new LanguageInfo();
	        LanguageInfo mLanguageInfo4 = new LanguageInfo();
	        LanguageInfo mLanguageInfo5 = new LanguageInfo();
	        mLanguageInfo1.setLanguage("中文（简体）");
	        mLanguageInfo1.setLocale(Locale.SIMPLIFIED_CHINESE);
	        mLanguageInfo2.setLanguage("中文（繁體）");
	        mLanguageInfo2.setLocale(Locale.TRADITIONAL_CHINESE);
	        mLanguageInfo3.setLanguage("English");
	        mLanguageInfo3.setLocale(Locale.US);
	        mLanguageInfo4.setLanguage("한국어");
	        mLanguageInfo4.setLocale(Locale.KOREA);
	        mLanguageInfo5.setLanguage("日本語");
	        mLanguageInfo5.setLocale(Locale.JAPAN);
	        mListLanguageInfo.add(mLanguageInfo1);
	        mListLanguageInfo.add(mLanguageInfo2);
	        mListLanguageInfo.add(mLanguageInfo3);
	        mListLanguageInfo.add(mLanguageInfo4);
	        mListLanguageInfo.add(mLanguageInfo5);
	        return mListLanguageInfo;
	}
	
	/**
	 * 更换语言
	 * @param info
	 */
	public void updateLanguage(final LanguageInfo info){
		ThreadPoolManager.getInstance().executeTask(new Runnable() {
			@Override
			public void run() {
				// 更改系统语言
				UpdateLanguageUtils.updateLanguage(info.getLocale());
			}
		});
		
	}
	
	/**
	 * 获取当前系统语言
	 * @param mContext
	 * @return
	 */
	public Locale getCurrentLocale(Context mContext){
		Locale locale = mContext.getResources().getConfiguration().locale;
		return locale;
	}
}
