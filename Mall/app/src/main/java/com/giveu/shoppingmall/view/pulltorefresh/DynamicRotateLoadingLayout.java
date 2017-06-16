package com.giveu.shoppingmall.view.pulltorefresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;

import com.giveu.shoppingmall.utils.DensityUtils;

/**
 * Created by 513419 on 2017/5/20.
 * 用于可左右滑动的listview中，以便加载框一直处于水平居中的位置
 */

public class DynamicRotateLoadingLayout extends RotateLoadingLayout {
    private  int x;
    private int y;
    public DynamicRotateLoadingLayout(Context context, PullToRefreshBase.Mode mode, PullToRefreshBase.Orientation scrollDirection, TypedArray attrs) {
        super(context, mode, scrollDirection, attrs);
    }

    @Override
    protected void releaseToRefreshImpl() {
        super.releaseToRefreshImpl();
        moveTo(x,y);
    }

    @Override
    protected void pullToRefreshImpl() {
        super.pullToRefreshImpl();
        moveTo(x,y);
    }

    @Override
    protected void resetImpl() {
        super.resetImpl();
        moveTo(x,y);
    }

    @Override
    protected void onPullImpl(float scaleOfLayout) {
        super.onPullImpl(scaleOfLayout);
        mInnerLayout.getLayoutParams().width= DensityUtils.getWidth();
        moveTo(x,y);
    }

    @Override
    protected void refreshingImpl() {
        super.refreshingImpl();
        mInnerLayout.getLayoutParams().width= DensityUtils.getWidth();
        moveTo(x,y);

    }

    /**
     * 移动加载框的位置
     * @param x
     * @param y
     */
    public void moveTo(int x,int y){
        ((View)mInnerLayout.getParent()).scrollTo(-x,y);
        this.x = x;
        this.y = y;
    }
}
