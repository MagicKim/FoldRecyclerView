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
import android.view.ViewDebug;

import com.chad.baserecyclerviewadapterhelper.R;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by ${Kim} on 19-10-17.
 */
public class TextClock2 extends AppCompatTextView {

    public static final String TAG = "TextClock2";

    public static final CharSequence DEFAULT_FORMAT_12_HOUR = "h:mm a";

    public static final CharSequence DEFAULT_FORMAT_24_HOUR = "H:mm";

    private CharSequence mFormat12;
    private CharSequence mFormat24;
    private CharSequence mDescFormat12;
    private CharSequence mDescFormat24;
    public static final char QUOTE = '\'';
    public static final char SECONDS = 's';

    private CharSequence mFormat;
    private boolean mHasSeconds;

    private boolean mRegistered;
    private boolean mShouldRunTicker;

    private Calendar mTime;
    private String mTimeZone;

    private ContentObserver mFormatChangeObserver;

    private Context mContext;


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
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.TextClock2);
        try {
            mFormat12 = a.getText(R.styleable.TextClock2_format12Hour);
            mFormat24 = a.getText(R.styleable.TextClock2_format24Hour);
            mTimeZone = a.getString(R.styleable.TextClock2_timeZone);
        } finally {
            a.recycle();
        }
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
                Log.w(TAG, "timeZone = " + timeZone);
                createTime(timeZone);
            }
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
        createTime(mTimeZone);
        chooseFormat();
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
            mFormat = abc(mFormat24, mFormat12, null);
        } else {
            mFormat = abc(mFormat12, mFormat24, null);
        }

        boolean hadSeconds = mHasSeconds;
        mHasSeconds = hasSeconds(mFormat);

        if (mShouldRunTicker && hadSeconds != mHasSeconds) {
            if (hadSeconds) getHandler().removeCallbacks(mTicker);
            else mTicker.run();
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

    @Override
    public void onVisibilityAggregated(boolean isVisible) {
        super.onVisibilityAggregated(isVisible);
        Log.w(TAG, "isVisible = " + isVisible);
        if (!mShouldRunTicker && isVisible) {
            mShouldRunTicker = true;
            if (mHasSeconds) {
                mTicker.run();
            } else {
                onTimeChanged();
            }
        } else if (mShouldRunTicker && !isVisible) {
            mShouldRunTicker = false;
            getHandler().removeCallbacks(mTicker);
        }
    }

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
                mFormatChangeObserver = new FormatChangeObserver(getHandler());
            }
            Log.e(TAG, "registerObserver");
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

    /**
     *
     */
    private void onTimeChanged() {
        // mShouldRunTicker always equals the last value passed into onVisibilityAggregated
        if (mShouldRunTicker) {
            Log.e(TAG, "onTimeChanged");
            mTime.setTimeInMillis(System.currentTimeMillis());
            setText(DateFormat.format(mFormat, mTime));
        }
    }
}
