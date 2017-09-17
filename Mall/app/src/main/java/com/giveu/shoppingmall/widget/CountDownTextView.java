package com.giveu.shoppingmall.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

import com.giveu.shoppingmall.R;

/**
 * Created by 101912 on 2017/8/31.
 * 倒计时textview
 */

public class CountDownTextView extends TextView {

    private boolean isCounting;//记录验证码是否开始计时的标志
    CountEndListener mListener;
    boolean flag;
    long restTime;
    public final int MINUTE = 1;
    public final int HOUR = 2;
    public int countDownStyle;
    private String hourDivider;
    private String minuteDivider;
    private String secondDivider;
    private String beforeTime;
    private String afterTime;
    private final int SECOND = 1000;

    /**
     * 以毫秒为单位
     * @param context
     */
    public CountDownTextView(Context context) {
        super(context);
    }


    public CountDownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CountDownTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.CountDownTextView);
        countDownStyle = attributes.getInteger(R.styleable.CountDownTextView_countDownStyle, 0x0);
        hourDivider = attributes.getString(R.styleable.CountDownTextView_hourDivider);
        minuteDivider = attributes.getString(R.styleable.CountDownTextView_minuteDivider);
        secondDivider = attributes.getString(R.styleable.CountDownTextView_secondDivider);
        beforeTime = attributes.getString(R.styleable.CountDownTextView_beforeTime);
        afterTime = attributes.getString(R.styleable.CountDownTextView_afterTime);
        hourDivider = getDefalutStr(hourDivider);
        minuteDivider = getDefalutStr(minuteDivider);
        secondDivider = getDefalutStr(secondDivider);
        beforeTime = getDefalutStr(beforeTime);
        afterTime = getDefalutStr(afterTime);
        attributes.recycle();
    }

    private String getDefalutStr(String orginalStr) {
        if (orginalStr == null) {
            return "";
        }
        return orginalStr;
    }

    public void setCountDownTextColor(boolean flag) {
        this.flag = flag;
    }

    public void setRestTime(long restTime) {
        this.restTime = restTime;
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (this != null) {
                if (restTime <= 0) {
                    stopCount();
                } else {
                    restTime = restTime - SECOND;
                    if (restTime <= 0) {
                        stopCount();
                    } else {
                        CountDownTextView.this.setText(beforeTime + formatTime(restTime) + afterTime);
                        Message msg2 = Message.obtain();
                        sendMessageDelayed(msg2, SECOND);
                    }
                }
            }
        }
    };

    /**
     * 格式化时间
     *
     * @param time
     * @return
     */
    private String formatTime(long time) {
        String showText = "";
        switch (countDownStyle) {
            case SECOND:
                showText = time + secondDivider;
                break;
            case MINUTE:
                showText = String.format("%02d", time / (60 * SECOND))
                        + minuteDivider + String.format("%02d", time % (60 * SECOND) / SECOND) + secondDivider;
                break;
            case HOUR:
                showText = String.format("%02d", time / (3600 * SECOND)) + hourDivider
                        + String.format("%02d", time % (3600 * SECOND) / (60 * 1000)) + minuteDivider;
                break;
        }
        return showText;
    }

    public void startCount(CountEndListener listener) {
        this.mListener = listener;
        handler.sendEmptyMessage(1);
    }

    public void stopCount() {
        if (mListener != null) {
            mListener.onEnd();
        }
        onDestory();
    }

    public void onDestory() {
        mListener = null;
        isCounting = false;
        if (handler != null)
            handler.removeCallbacksAndMessages(null);
    }


    public interface CountEndListener {
        void onEnd();
    }
}
