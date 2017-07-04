package com.giveu.shoppingmall.cash.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseFragment;
import com.giveu.shoppingmall.cash.view.activity.CaseRecordActivity;
import com.giveu.shoppingmall.cash.view.activity.CashTypeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 消费模块
 * Created by 508632 on 2016/12/13.
 */


public class MainCashFragment extends BaseFragment {

    @BindView(R.id.tv_loan)
    TextView tvLoan;
    @BindView(R.id.iv_bg_top)
    ImageView ivBgTop;
    @BindView(R.id.tv_available_credit)
    TextView tvAvailableCredit;
    Unbinder unbinder;


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(mBaseContext, R.layout.fragment_main_cash, null);
        baseLayout.setTitle("我要取现");
        baseLayout.setRightTextColor(R.color.title_color);
        baseLayout.hideBack();
        baseLayout.setRightTextAndListener("取现记录", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CaseRecordActivity.startIt(mBaseContext);
            }
        });
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void setListener() {
        tvLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CashTypeActivity.startIt(mBaseContext);
            }
        });
    }

    @Override
    public void initWithDataDelay() {
        int width = ivBgTop.getWidth();
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ivBgTop.getLayoutParams();
        layoutParams.height = (208 * width / 708);
        ivBgTop.setLayoutParams(layoutParams);
    }

    @Override
    protected boolean translateStatusBar() {
        return true;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
