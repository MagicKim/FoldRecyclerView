package com.chad.baserecyclerviewadapterhelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.chad.baserecyclerviewadapterhelper.language.LanguageInfo;
import com.chad.baserecyclerviewadapterhelper.language.adapter.LanguageAdapter;
import com.chad.baserecyclerviewadapterhelper.language.controller.LanguageController;

import java.util.List;
import java.util.Locale;

public class Main6Activity extends AppCompatActivity {
    public static final String TAG = "LanguageActivity";
    private ListView mListView;
    private Button mNextStep;
    private LanguageAdapter mLanguageAdapter;
    private List<LanguageInfo> mListLanguageInfo;
    private LanguageController mLanguageController;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main6);
        Log.d(TAG, "---onCreate()");
        mLanguageController = LanguageController.getInstance(getApplicationContext());
        //初始化列表数据
        mListLanguageInfo = mLanguageController.getListData();

    }

    /**
     * 初始化View
     */
    private void initView() {
        mListView = (ListView) findViewById(R.id.language_listview);
        mNextStep = (Button) findViewById(R.id.next);
        mNextStep.setText(getResources().getString(R.string.button_next_step));
        mLanguageAdapter = new LanguageAdapter(getApplicationContext(), mListLanguageInfo);
//		mListView.setLayoutAnimation(ListViewUtils.setListAnim());
        mListView.setAdapter(mLanguageAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {

                LanguageInfo mInfo = mListLanguageInfo.get(position);
                //更改系统语言
                mLanguageController.updateLanguage(mInfo);
            }
        });

        mNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    /**
     * 初始化本地语言
     */
    private void initLanguage() {
        Locale currentLocale = mLanguageController.getCurrentLocale(getApplicationContext());
        for (int i = 0; i < mListLanguageInfo.size(); i++) {
            Locale temp = mListLanguageInfo.get(i).getLocale();
            if (temp.equals(currentLocale)) {
                //标记当前item
                mLanguageAdapter.setSelectItem(i);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //初始化View
        initView();
        //初始化本地语言
        initLanguage();
    }
}
