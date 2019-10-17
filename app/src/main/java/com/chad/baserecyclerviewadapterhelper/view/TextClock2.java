package com.chad.baserecyclerviewadapterhelper.view;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.database.ContentObserver;
import android.icu.util.LocaleData;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.widget.AppCompatTextView;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.baserecyclerviewadapterhelper.R;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by ${Kim} on 19-10-17.
 */
public class TextClock2 extends LinearLayout {

    public static final String TAG = "TextClock2";

    public static final CharSequence DEFAULT_FORMAT_12_HOUR = "hh:mm";

    public static final CharSequence DEFAULT_FORMAT_24_HOUR = "HH:mm";

    //    private CharSequence mFormat12;
//    private CharSequence mFormat24;
//    private CharSequence mDescFormat12;
//    private CharSequence mDescFormat24;
    public static final char QUOTE = '\'';
    public static final char SECONDS = 's';

    private OnTextClockListener mTextClockListener;

    private CharSequence mFormat;
    private boolean mHasSeconds;

    private boolean mRegistered;
    private boolean mShouldRunTicker;

    private Calendar mTime;
    private String mTimeZone;

    private ContentObserver mFormatChangeObserver;

    private Context mContext;

    private TextView tv1, tv2, tv3, tv4, tv5;


    public TextClock2(Context context) {
        super(context);
        init(context);
    }

    public TextClock2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextClock2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private class FormatChangeObserver extends ContentObserver {

        public FormatChangeObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            Log.e(TAG, "selfChange = " + selfChange);
            chooseFormat();
            onTimeChanged();
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            Log.e(TAG, "selfChange = " + selfChange + "URI = " + uri);
            chooseFormat();
            onTimeChanged();
        }
    }

    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mTimeZone == null && Intent.ACTION_TIMEZONE_CHANGED.equals(intent.getAction())) {
                final String timeZone = intent.getStringExtra("time-zone");
                createTime(timeZone);
            }
            Log.w(TAG, "onReceive ");
            onTimeChanged();
        }
    };

    private final Runnable mTicker = new Runnable() {
        public void run() {

            onTimeChanged();

            long now = SystemClock.uptimeMillis();
            long next = now + (1000 - now % 1000);

            getHandler().postAtTime(mTicker, next);
        }
    };

    private void init(Context context) {
        Log.w(TAG, "init");
        mContext = context;
        View rootView = inflate(context, R.layout.view_text_clock, this);
        tv1 = rootView.findViewById(R.id.tv1);
        tv2 = rootView.findViewById(R.id.tv2);
        tv3 = rootView.findViewById(R.id.tv3);
        tv4 = rootView.findViewById(R.id.tv4);
        tv5 = rootView.findViewById(R.id.tv5);
        createTime(mTimeZone);
        chooseFormat();
        onTimeChanged();
    }

    private void createTime(String timeZone) {
        if (timeZone != null) {
            mTime = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
        } else {
            mTime = Calendar.getInstance();
        }
    }

    public boolean is24HourModeEnabled() {
        return DateFormat.is24HourFormat(mContext);
    }

    public String getTimeZone() {
        return mTimeZone;
    }


    private void chooseFormat() {
        final boolean format24Requested = is24HourModeEnabled();

        if (format24Requested) {
            mFormat = DEFAULT_FORMAT_24_HOUR;
            Log.d(TAG, "24mFormat = " + mFormat);
        } else {
            mFormat = DEFAULT_FORMAT_12_HOUR;
            Log.d(TAG, "12mFormat = " + mFormat);
        }

        boolean hadSeconds = mHasSeconds;
        mHasSeconds = hasSeconds(mFormat);

        if (hadSeconds != mHasSeconds) {
            if (hadSeconds) {
                Log.d(TAG, "hadSeconds = true");
                getHandler().removeCallbacks(mTicker);
            } else {
                Log.d(TAG, "hadSeconds = false");
                mTicker.run();
            }
        }
    }

    private boolean hasSeconds(CharSequence inFormat) {
        return hasDesignator(inFormat, SECONDS);
    }

    private boolean hasDesignator(CharSequence inFormat, char designator) {
        if (inFormat == null) return false;

        final int length = inFormat.length();

        boolean insideQuote = false;
        for (int i = 0; i < length; i++) {
            final char c = inFormat.charAt(i);
            if (c == QUOTE) {
                insideQuote = !insideQuote;
            } else if (!insideQuote) {
                if (c == designator) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 如果如果a不为空则返回a，如果b不为空则返回b，最后返回c。
     */
    private static CharSequence abc(CharSequence a, CharSequence b, CharSequence c) {
        return a == null ? (b == null ? c : b) : a;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (!mRegistered) {
            mRegistered = true;
            registerReceiver();
            registerObserver();
            createTime(mTimeZone);
        }
    }

//    @Override
//    public void onVisibilityAggregated(boolean isVisible) {
//        super.onVisibilityAggregated(isVisible);
//        Log.w(TAG, "isVisible = " + isVisible);
//        if (!mShouldRunTicker && isVisible) {
//            mShouldRunTicker = true;
//            if (mHasSeconds) {
//                mTicker.run();
//            } else {
//                onTimeChanged();
//            }
//        } else if (mShouldRunTicker && !isVisible) {
//            mShouldRunTicker = false;
//            getHandler().removeCallbacks(mTicker);
//        }
//    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mRegistered) {
            unregisterReceiver();
            unregisterObserver();
            mRegistered = false;
        }
    }


    private void registerReceiver() {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        mContext.registerReceiver(mIntentReceiver, filter);
    }

    private void registerObserver() {
        if (mRegistered) {
            if (mFormatChangeObserver == null) {
                Log.e(TAG, "registerObserver");
                mFormatChangeObserver = new FormatChangeObserver(getHandler());
            }
            final ContentResolver resolver = mContext.getContentResolver();
            Uri uri = Settings.System.getUriFor(Settings.System.TIME_12_24);
            resolver.registerContentObserver(uri, true,
                    mFormatChangeObserver);
        }
    }

    private void unregisterReceiver() {
        getContext().unregisterReceiver(mIntentReceiver);
    }

    private void unregisterObserver() {
        if (mFormatChangeObserver != null) {
            final ContentResolver resolver = getContext().getContentResolver();
            resolver.unregisterContentObserver(mFormatChangeObserver);
        }
    }


    private void onTimeChanged() {
        // mShouldRunTicker always equals the last value passed into onVisibilityAggregated
//        if (mShouldRunTicker) {
        Log.e(TAG, "onTimeChanged");
        mTime.setTimeInMillis(System.currentTimeMillis());
        CharSequence text = DateFormat.format(mFormat, mTime);

        Log.d(TAG,"0 = "+text.charAt(0));
        Log.d(TAG,"1 = "+text.charAt(1));
        Log.d(TAG,"2 = "+text.charAt(2));
        Log.d(TAG,"3 = "+text.charAt(3));
        Log.d(TAG,"4 = "+text.charAt(4));


        if (mTextClockListener != null) {
            mTextClockListener.onTextClockChanged(text);
        }
    }

    public void setTextClockListener(OnTextClockListener onTextClockListener) {
        mTextClockListener = onTextClockListener;
    }

    public interface OnTextClockListener {
        void onTextClockChanged(CharSequence text);
    }
}
