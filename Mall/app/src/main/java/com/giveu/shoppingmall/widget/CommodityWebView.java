package com.giveu.shoppingmall.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * item页底部的webview
 */
public class CommodityWebView extends WebView {
    public float oldY;
    private int t;
    private float oldX;
    private boolean fromCommodityDetail;

    public CommodityWebView(Context context) {
        super(context);
    }

    public CommodityWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommodityWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float Y = ev.getY();
                float Ys = Y - oldY;
                float X = ev.getX();
                float Xs = X - oldX;
                //滑动到顶部让父控件重新获得触摸事件
                if (Math.abs(Ys) > Math.abs(Xs) && Ys > 0 && t == 0) {
                    getParent().getParent().requestDisallowInterceptTouchEvent(false);
                } else if (Math.abs(Xs) > Math.abs(Ys) && fromCommodityDetail) {
                    getParent().getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;

            case MotionEvent.ACTION_DOWN:
                getParent().getParent().requestDisallowInterceptTouchEvent(true);
                oldY = ev.getY();
                oldX = ev.getX();
                break;

            case MotionEvent.ACTION_UP:
                getParent().getParent().requestDisallowInterceptTouchEvent(true);
                break;

            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void fromCommodityDetail(boolean fromCommodityDetail) {
        this.fromCommodityDetail = fromCommodityDetail;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        this.t = t;
        super.onScrollChanged(l, t, oldl, oldt);
    }

}
