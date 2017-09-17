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
import com.giveu.shoppingmall.cash.view.dialog.QuotaDialog;
import com.giveu.shoppingmall.me.view.dialog.NotActiveDialog;
import com.giveu.shoppingmall.utils.DensityUtils;
import com.giveu.shoppingmall.utils.LoginHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 取现模块
 * Created by 508632 on 2016/12/13.
 */


public class MainCashFragment extends BaseFragment {

    @BindView(R.id.tv_loan)
    TextView tvLoan;
    @BindView(R.id.iv_bg_top)
    ImageView ivBgTop;
    @BindView(R.id.tv_available_credit)
    TextView tvAvailableCredit;
    QuotaDialog quotaDialog;//额度为0的弹窗
    @BindView(R.id.ll_top)
    LinearLayout llTop;
    @BindView(R.id.ll_date)
    LinearLayout llDate;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(mBaseContext, R.layout.fragment_main_cash, null);
        baseLayout.setTitle("我要取现");
        baseLayout.setRightTextColor(R.color.title_color);
        baseLayout.hideBack();
        baseLayout.setRightTextAndListener("取现记录", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoginAndActivation()) {
                    CaseRecordActivity.startIt(mBaseContext);
                }
            }
        });
        quotaDialog = new QuotaDialog(mBaseContext);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void setListener() {
        llTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoginAndActivation()) {
                    //登录并且激活
                    CashTypeActivity.startIt(mBaseContext);
                }
            }
        });
    }

    @Override
    public void initDataDelay() {
//        ivBgTop.setLayoutParams(layoutParams1);
        llDate.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) llTop.getLayoutParams();

                int width = DensityUtils.getWidth() - DensityUtils.dip2px(30);
                LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) ivBgTop.getLayoutParams();
                layoutParams1.height = (208 * width / 708);
                layoutParams2.height = ivBgTop.getHeight() + llDate.getHeight() + DensityUtils.dip2px(15);
                layoutParams2.setMargins(DensityUtils.dip2px(10), DensityUtils.dip2px(6), DensityUtils.dip2px(10), 0);
                llDate.setLayoutParams(layoutParams2);
            }
        });


    }

    /**
     * 是否登录是否激活
     */
    public boolean isLoginAndActivation() {
        //先判断有没登录，然后再判断是否有钱包资质，满足条件后才进入账单
        if (LoginHelper.getInstance().hasLoginAndActivation(mBaseContext)) {
            String availableCylimit = LoginHelper.getInstance().getAvailableCylimit();
            double cylimit = Double.parseDouble(availableCylimit);
            if (0 == cylimit) {
                //取现额度为0
                quotaDialog.showDialog();
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean translateStatusBar() {
        return true;
    }


}
