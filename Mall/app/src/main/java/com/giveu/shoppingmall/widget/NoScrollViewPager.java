package com.giveu.shoppingmall.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by RudyJun on 2017/3/9.
 * 可设置ViewPager是否可滑动
 */

public class NoScrollViewPager extends ViewPager {
    private boolean scrollDisabled = true;

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public NoScrollViewPager(Context context) {
        super(context);
    }

    public void setScrollDisabled(boolean scrollDisabled) {
        this.scrollDisabled = scrollDisabled;
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        try {
            if (scrollDisabled) {
                return false;
            } else {
                return super.onTouchEvent(arg0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onTouchEvent(arg0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        try {
            if (scrollDisabled) {
                return false;
            } else {
                return super.onInterceptTouchEvent(arg0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onInterceptTouchEvent(arg0);
    }
}
