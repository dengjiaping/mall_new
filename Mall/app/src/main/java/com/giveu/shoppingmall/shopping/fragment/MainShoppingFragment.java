
package com.giveu.shoppingmall.shopping.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseFragment;

/**
 * 新建模块
 * Created by 508632 on 2016/12/13.
 */

public class MainShoppingFragment extends BaseFragment {

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(mBaseContext, R.layout.fragment_main_shopping, null);
        return view;
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void initWithDataDelay() {

    }
}
