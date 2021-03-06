package com.giveu.shoppingmall.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.giveu.shoppingmall.R;

/**
 * Created by 513419 on 2017/6/21.
 */

public class ClickEnabledTextView extends TextView {
    private boolean clickEnabled;

    public ClickEnabledTextView(Context context) {
        super(context);
        init();
    }

    public ClickEnabledTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClickEnabledTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setClickEnabled(false);
    }


    public void setClickEnabled(boolean clickEnabled) {
        this.clickEnabled = clickEnabled;
        if (clickEnabled) {
            setBackgroundResource(R.drawable.selector_login);
        } else {
            setBackgroundResource(R.drawable.shape_button_disabled);
        }
    }

    public boolean isClickEnabled() {
        return clickEnabled;
    }
}
