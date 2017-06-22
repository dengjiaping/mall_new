package com.giveu.shoppingmall.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by 508632 on 2017/1/11.
 */

public class SendCodeTextView extends TextView {
    private boolean isCounting;//记录验证码是否开始计时的标志
    CountEndListener mListener;

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

    int MAXCOUNT = 60;
    int count = MAXCOUNT;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (this != null) {
                if (count == 0) {
                    stopCount("重发验证码");
                } else {
                    SendCodeTextView.this.setText("重发(" + (--count) + ")");
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
        if(mListener != null){
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
