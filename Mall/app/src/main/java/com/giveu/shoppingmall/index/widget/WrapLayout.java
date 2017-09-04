package com.giveu.shoppingmall.index.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by 524202 on 2017/9/1.
 */

public class WrapLayout extends LinearLayout {
    private View lChild;
    private View rChild;
    private Context context;

    private int lChildWidth;
    private int lChildTempW;

    private boolean isFirst = true;
    private final static int touchSnap = 5;

    public WrapLayout(Context context) {
        super(context);
    }

    public WrapLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void initView() {
        if (getChildCount() < 2) {
            throw new IllegalArgumentException("WrapLayout must have at least two children!");
        }

        setOrientation(HORIZONTAL);
        lChild = getChildAt(0);
        rChild = getChildAt(1);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (isFirst) {
            isFirst = false;
            initView();
            lChildWidth = lChild.getMeasuredWidth();
            lChildTempW = lChild.getMeasuredWidth();
        }
    }

    int posX, posY;
    int lastX;
    boolean isSwap = false;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                intercept = false;
                posX = x;
                posY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(x - posX) > touchSnap && Math.abs(x - posX) >= 2 * Math.abs(y - posY)) {
                    intercept = true;
                } else {
                    intercept = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                break;
            default:
                break;
        }
        posX = x;
        posY = y;
        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int deltX = (int) (event.getX() - lastX);
                if (Math.abs(deltX) > touchSnap) {
                    if (!isSwap) {
                        isSwap = true;
                        lastX = (int) event.getX();
                    } else {
                        dealingAction(deltX / 3);
                        lastX = (int) event.getX();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (isSwap) {
                    autoDealingAction();
                    isSwap = false;
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private void autoDealingAction() {
        int targetWidth = 0;
        if (lChildTempW < lChildWidth / 2) {
            targetWidth = 0;
        } else if (lChildTempW > lChildWidth / 2) {
            targetWidth = lChildWidth;
        }

        if (lChildTempW != targetWidth) {
            animateChild(targetWidth);
        }
    }

    private void dealingAction(int deltX) {
        int targetWidth = 0;
        if (deltX > 0 && lChildTempW < lChildWidth) {
            targetWidth = lChildTempW + deltX;
            if (targetWidth >= lChildWidth) {
                targetWidth = lChildWidth;
            }
            resetChildWidth(targetWidth);
        } else if (deltX < 0 && lChildTempW > 0) {
            targetWidth = lChildTempW + deltX;
            if (targetWidth <= 0) {
                targetWidth = 0;
            }
            resetChildWidth(targetWidth);
        }
    }

    private void resetChildWidth(int targetWidth) {
        LayoutParams params = (LayoutParams) lChild.getLayoutParams();
        params.width = targetWidth;
        lChild.setLayoutParams(params);
        lChildTempW = targetWidth;
        lChild.setAlpha(lChildTempW * 1.0f / lChildWidth);
        requestLayout();
    }

    private void animateChild(int targetWidth) {
        animateChild(targetWidth, 300);
    }

    private void animateChild(int targetWidth, int duration) {

        ValueAnimator animator = ValueAnimator.ofInt(lChildTempW, targetWidth);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lChildTempW = (int) animation.getAnimatedValue();
                lChild.getLayoutParams().width = lChildTempW;
                lChild.setAlpha(lChildTempW * 1.0f / lChildWidth);
                requestLayout();
            }
        });
        animator.setDuration(duration);
        animator.start();
    }
}
