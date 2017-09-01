package com.giveu.shoppingmall.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RadioButton;

/**
 * Created by 513419 on 2017/4/25.
 */

public class CustomerRadioButton extends RadioButton {
    public CustomerRadioButton(Context context) {
        super(context);
    }

    public CustomerRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.onTouchEvent(ev);
    }
}
