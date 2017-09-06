package com.giveu.shoppingmall.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

import com.giveu.shoppingmall.utils.StringUtils;

/**
 * Created by 101912 on 2017/8/31.
 * 倒计时textview
 */

public class CountDownTextView extends TextView {

    private boolean isCounting;//记录验证码是否开始计时的标志
    CountEndListener mListener;
    boolean flag;
    long restTime;

    public CountDownTextView(Context context) {
        super(context);
        init(context);
    }


    public CountDownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CountDownTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

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
                if (restTime == 0) {
                    stopCount();
                } else {
                    restTime = restTime - 1000;
                    CountDownTextView.this.setText("剩" + StringUtils.formatRestTime(restTime) + "自动关闭");
                    Message msg2 = Message.obtain();
                    sendMessageDelayed(msg2, 1000);
                }
            }
        }
    };

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
