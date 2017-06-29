package com.giveu.shoppingmall.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.TextView;

import com.giveu.shoppingmall.R;

/**
 * Created by 508632 on 2017/1/11.
 */

public class SendCodeTextView extends TextView {
    private boolean isCounting;//记录验证码是否开始计时的标志
    CountEndListener mListener;
    boolean flag;

    public SendCodeTextView(Context context) {
        super(context);
        init(context);
    }

    public SendCodeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SendCodeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {

    }

    /**
     * false 不带背景框的验证码
     * true 带背景框的验证码
     * @param flag
     */
    public void setSendTextColor(boolean flag) {
        this.flag = flag;
    }

    int MAXCOUNT = 60;
    int count = MAXCOUNT;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (this != null) {
                if (count == 0) {
                    stopCount("重发验证码");
                    if (!flag) {
                        SendCodeTextView.this.setTextColor(ContextCompat.getColor(getContext(), R.color.color_00bbc0));
                    }

                } else {
                    SendCodeTextView.this.setText("重新发送(" + (--count) + "s)");
                    if (!flag) {
                        SendCodeTextView.this.setTextColor(ContextCompat.getColor(getContext(), R.color.grey_a5a5a5));
                    }
                    SendCodeTextView.this.setEnabled(false);
                    Message msg2 = Message.obtain();
                    handler.sendMessageDelayed(msg2, 1000);

                }
            }
        }
    };


    public void startCount(CountEndListener listener) {
        this.setEnabled(false);
        mListener = listener;
        isCounting = true;
        count = MAXCOUNT;
        handler.sendEmptyMessage(1);
    }

    public void stopCount(String codeStr) {
        this.setEnabled(true);
        this.setText(codeStr);
        if (mListener != null) {
            mListener.onEnd();
        }
        onDestory();
    }

    /**
     * Activity销毁的时候调用，不再回调onEnd（）方法
     */
    public void onDestory() {
        mListener = null;
        isCounting = false;
        if (handler != null)
            handler.removeCallbacksAndMessages(null);
    }

    //验证码是否开始计时的标志
    public boolean isCounting() {
        return isCounting;
    }

    public interface CountEndListener {
        void onEnd();
    }
}
