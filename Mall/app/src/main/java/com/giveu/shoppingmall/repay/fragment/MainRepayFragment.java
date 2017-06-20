package com.giveu.shoppingmall.repay.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseFragment;


public class MainRepayFragment extends BaseFragment {

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(mBaseContext, R.layout.fragment_main_repay, null);
        return view;
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void initWithDataDelay() {

    }
}
