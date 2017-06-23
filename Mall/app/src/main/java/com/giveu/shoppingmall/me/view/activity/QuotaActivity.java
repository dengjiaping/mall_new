package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.CustomDialog;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by 513419 on 2017/6/22.
 */

public class QuotaActivity extends BaseActivity {
    @BindView(R.id.tv_available_credit)
    TextView tvAvailableCredit;
    @BindView(R.id.tv_available_money)
    TextView tvAvailableMoney;
    @BindView(R.id.tv_avaiable_withdrawals)
    TextView tvAvaiableWithdrawals;
    @BindView(R.id.tv_withdrawals_money)
    TextView tvWithdrawalsMoney;
    @BindView(R.id.tv_consumable)
    TextView tvConsumable;
    @BindView(R.id.tv_consumable_money)
    TextView tvConsumableMoney;
    private CustomDialog totalDialog;
    private TextView tvWithdrawals;
    private TextView tvLargeWithdrawals;
    private TextView tvHint;
    private TextView tvKnow;

    public static void startIt(Activity activity) {
        Intent intent = new Intent(activity, QuotaActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_quota);
        baseLayout.setTitle("我的额度");
        baseLayout.setBlueWhiteStyle();
    }

    @Override
    public void setData() {

    }

    /**
     * 1为总授信额度，2为取现总额度，3为消费总额度
     *
     * @param flag
     */
    private void showTotalDialog(int flag) {

        if (totalDialog == null) {
            totalDialog = new CustomDialog(mBaseContext, R.layout.dialog_avaialbe_credit
                    , R.style.customerDialog, Gravity.CENTER, false);
            totalDialog.setCanceledOnTouchOutside(false);
            tvWithdrawals = (TextView) totalDialog.findViewById(R.id.tv_withdrawals);
            tvLargeWithdrawals = (TextView) totalDialog.findViewById(R.id.tv_large_withdrawals);
            tvHint = (TextView) totalDialog.findViewById(R.id.tv_hint);
            tvKnow = (TextView) totalDialog.findViewById(R.id.tv_know);
        }
        switch (flag) {
            case 1:
                tvWithdrawals.setText("可用额度：" + "¥4000.00" + "\n总授信额度：" + " ¥8000.00");
                tvLargeWithdrawals.setVisibility(View.GONE);
                tvHint.setVisibility(View.GONE);
                break;

            case 2:
                tvWithdrawals.setText("取现可用额度：" + "¥4000.00" + "\n取现总额度：" + " ¥8000.00");
                tvLargeWithdrawals.setVisibility(View.VISIBLE);
                tvLargeWithdrawals.setText("大额现金分期可用额度：" + "¥4000.00" + "\n大额现金分期总额度：" + " ¥8000.00");
                tvHint.setVisibility(View.GONE);
                break;

            case 3:
                tvWithdrawals.setText("消费可用额度：" + "¥4000.00" + "\n消费总额度：" + " ¥8000.00");
                tvLargeWithdrawals.setVisibility(View.GONE);
                tvHint.setVisibility(View.GONE);
                break;
        }
        tvKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalDialog.dismiss();
            }
        });
        totalDialog.show();
    }


    @OnClick({R.id.tv_available_credit, R.id.tv_avaiable_withdrawals, R.id.tv_consumable})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_available_credit:
                showTotalDialog(1);
                break;

            case R.id.tv_avaiable_withdrawals:
                showTotalDialog(2);
                break;

            case R.id.tv_consumable:
                showTotalDialog(3);
                break;
        }
    }
}
