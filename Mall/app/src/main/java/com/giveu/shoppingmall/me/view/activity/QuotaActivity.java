package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.cash.view.activity.CashTypeActivity;
import com.giveu.shoppingmall.cash.view.dialog.QuotaDialog;
import com.giveu.shoppingmall.me.presenter.QuotaPresenter;
import com.giveu.shoppingmall.me.view.agent.IQuotaView;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by 513419 on 2017/6/22.
 */

public class QuotaActivity extends BaseActivity implements IQuotaView {
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
    private QuotaPresenter presenter;
    private QuotaDialog quotaDialog;//额度为0的弹窗

    public static void startIt(Activity activity) {
        Intent intent = new Intent(activity, QuotaActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_quota);
        baseLayout.setTitle("我的额度");
        baseLayout.setBlueWhiteStyle();
        presenter = new QuotaPresenter(this);
        tvWithdrawalsMoney.setText("¥" + StringUtils.format2(LoginHelper.getInstance().getAvailableCylimit()));
        tvConsumableMoney.setText("¥" + StringUtils.format2(LoginHelper.getInstance().getAvailablePoslimit()));
        tvAvailableMoney.setText("¥" + StringUtils.format2(LoginHelper.getInstance().getTotalCost()));
        quotaDialog = new QuotaDialog(mBaseContext);
    }

    @Override
    protected BasePresenter[] initPresenters() {
        return new BasePresenter[]{presenter};
    }

    @Override
    public void setData() {

    }

    /**
     * 1为总授信额度，2为取现总额度，3为消费总额度
     *
     * @param flag
     */
    private void showTotalDialog(final int flag) {

        if (totalDialog == null) {
            totalDialog = new CustomDialog(mBaseContext, R.layout.dialog_avaialbe_credit
                    , R.style.customerDialog, Gravity.CENTER, false);
            totalDialog.setCanceledOnTouchOutside(false);
            tvWithdrawals = (TextView) totalDialog.findViewById(R.id.tv_withdrawals);
            tvLargeWithdrawals = (TextView) totalDialog.findViewById(R.id.tv_large_withdrawals);
            tvHint = (TextView) totalDialog.findViewById(R.id.tv_hint);
            tvKnow = (TextView) totalDialog.findViewById(R.id.tv_confirm);
        }
        switch (flag) {
            case 1:
                tvWithdrawals.setText("可用额度：" + "¥" + StringUtils.format2(LoginHelper.getInstance().getTotalCost()) + "\n总授信额度：" + "¥" + StringUtils.format2(LoginHelper.getInstance().getGlobleLimit()));
                tvLargeWithdrawals.setVisibility(View.GONE);
                tvHint.setVisibility(View.GONE);
                tvKnow.setText("知道了");
                break;

            case 2:
                tvWithdrawals.setText("取现可用额度：" + "¥" + StringUtils.format2(LoginHelper.getInstance().getAvailableCylimit())
                        + "\n取现总额度：" + "¥" + StringUtils.format2(LoginHelper.getInstance().getCylimit()));
//                tvLargeWithdrawals.setVisibility(View.VISIBLE);
//                tvLargeWithdrawals.setText("大额现金分期可用额度：" + "¥4000.00" + "\n大额现金分期总额度：" + " ¥8000.00");
                tvHint.setVisibility(View.GONE);
                tvKnow.setText("去取现");
                break;

            case 3:
                tvWithdrawals.setText("消费可用额度：" + "¥" + StringUtils.format2(LoginHelper.getInstance().getAvailablePoslimit())
                        + "\n消费总额度：" + "¥" + StringUtils.format2(LoginHelper.getInstance().getPosLimit()));
                tvLargeWithdrawals.setVisibility(View.GONE);
                tvHint.setVisibility(View.VISIBLE);
                tvKnow.setText("知道了");
                break;
        }
        tvKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 2) {
                    String availableCylimit = LoginHelper.getInstance().getAvailableCylimit();
                    if ("0".equals(availableCylimit)) {
                        //取现额度为0
                        quotaDialog.showDialog();
                    } else {
                        CashTypeActivity.startIt(mBaseContext);
                    }
                }
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
