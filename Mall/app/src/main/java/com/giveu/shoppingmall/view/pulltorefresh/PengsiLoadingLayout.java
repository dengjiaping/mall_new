/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.giveu.shoppingmall.view.pulltorefresh;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.view.pulltorefresh.PullToRefreshBase.Mode;
import com.giveu.shoppingmall.view.pulltorefresh.PullToRefreshBase.Orientation;

public class PengsiLoadingLayout extends LoadingLayout {

    static final int ROTATION_ANIMATION_DURATION = 1200;

    private final boolean mRotateDrawableWhilePulling;
    private AnimationDrawable mLoadingAinm;


    public PengsiLoadingLayout(Context context, Mode mode, Orientation scrollDirection, TypedArray attrs) {
        super(context, mode, scrollDirection, attrs);
        mRotateDrawableWhilePulling = attrs.getBoolean(R.styleable.PullToRefresh_ptrRotateDrawableWhilePulling, true);
    }

    public void onLoadingDrawableSet(Drawable imageDrawable) {
    }

    protected void onPullImpl(float scaleOfLayout) {
        int index = (int) (20 * (scaleOfLayout / 2)) % 20;
        mHeaderImage.setImageResource(getDrawableRes(index));
    }

    public int getDrawableRes(int level) {
        Resources res = mContext.getResources();
        final String packageName = mContext.getPackageName();
        return res.getIdentifier("loading_point" + level, "drawable", packageName);
    }

    @Override
    protected void refreshingImpl() {
        mHeaderImage.setImageDrawable(null);
        mHeaderImage.setBackgroundResource(R.drawable.spinner);
        mLoadingAinm = (AnimationDrawable) mHeaderImage.getBackground();
        mLoadingAinm.start();
    }

    @Override
    protected void resetImpl() {
        mHeaderImage.setImageDrawable(null);
        mHeaderImage.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
    }

    @Override
    protected void pullToRefreshImpl() {
        // NO-OP
    }

    @Override
    protected void releaseToRefreshImpl() {
        // NO-OP
    }

    @Override
    protected int getDefaultDrawableResId() {
        return R.drawable.default_ptr_rotate;
    }

}
