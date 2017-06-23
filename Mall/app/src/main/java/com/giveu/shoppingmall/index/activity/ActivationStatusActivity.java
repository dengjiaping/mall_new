package com.giveu.shoppingmall.index.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.utils.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 钱包激活状态页
 * Created by 101900 on 2017/6/19.
 */

public class ActivationStatusActivity extends BaseActivity {
    @BindView(R.id.tv_set_transaction_pwd)
    TextView tvSetTransactionPwd;
    @BindView(R.id.iv_status)
    ImageView ivStatus;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_hint_bottom)
    TextView tvHintBottom;
    @BindView(R.id.tv_hint_mid)
    TextView tvHintMid;
    @BindView(R.id.ll_date)
    LinearLayout llDate;
    @BindView(R.id.tv_wallet_total_amount)
    TextView tvWalletTotalAmount;
    @BindView(R.id.tv_withdrawals_amount)
    TextView tvWithdrawalsAmount;
    @BindView(R.id.tv_consume_amount)
    TextView tvConsumeAmount;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_activation_status);
        baseLayout.setTitle("钱包激活");
    }

    @Override
    public void setData() {
        String status = StringUtils.nullToEmptyString(getIntent().getStringExtra("status"));
        String walletTotalAmount = StringUtils.nullToEmptyString(getIntent().getStringExtra("walletTotalAmount"));
        String withdrawalsAmount = StringUtils.nullToEmptyString(getIntent().getStringExtra("withdrawalsAmount"));
        String consumeAmount = StringUtils.nullToEmptyString(getIntent().getStringExtra("consumeAmount"));
        String bottomHint = StringUtils.nullToEmptyString(getIntent().getStringExtra("bottomHint"));
        String midHint = StringUtils.nullToEmptyString(getIntent().getStringExtra("midHint"));
        switch (status) {
            //1、激活成功，有额度，不含下方提示语
            case "1":
                //激活成功且有授信额度
                tvHintBottom.setVisibility(View.GONE);
                llDate.setVisibility(View.VISIBLE);
                tvHintMid.setVisibility(View.GONE);
                tvWalletTotalAmount.setText(walletTotalAmount);
                tvWithdrawalsAmount.setText(withdrawalsAmount);
                tvConsumeAmount.setText(consumeAmount);
                ivStatus.setImageResource(R.drawable.ic_activation_success);
                tvStatus.setText("激活成功");
                tvSetTransactionPwd.setText("设置交易密码");
                break;

            //2、激活成功，有额度，含下方提示语
            case "2":
                //激活成功且可用额度为0
            case "3":
                //激活成功且取现额度为0
            case "4":
                //激活成功且消费额度为0
                tvHintBottom.setVisibility(View.VISIBLE);
                llDate.setVisibility(View.VISIBLE);
                tvHintMid.setVisibility(View.GONE);
                tvWalletTotalAmount.setText("0");
                tvWithdrawalsAmount.setText("0");
                tvConsumeAmount.setText("0");
                ivStatus.setImageResource(R.drawable.ic_activation_success);
                tvStatus.setText("激活成功");
                tvHintBottom.setText(bottomHint);
                tvSetTransactionPwd.setText("设置交易密码");
                break;

            //3、激活失败，无额度，含中间提示语
            case "5":
                //身份验证不通过
            case "6":
                //身份证/手机号格式错误
            case "7":
                //激活失败
            case "8":
                //激活失败次数>=3
            case "9":
                //激活号码与登录号码不一致
                tvHintBottom.setVisibility(View.GONE);
                llDate.setVisibility(View.GONE);
                tvHintMid.setVisibility(View.VISIBLE);
                ivStatus.setImageResource(R.drawable.ic_activation_fail);
                tvStatus.setText("激活失败");
                tvHintMid.setText(midHint);
                tvSetTransactionPwd.setText("重新激活钱包");
                break;
            case "100":
                //设置完交易密码后跳转的设置成功页
                tvHintBottom.setVisibility(View.GONE);
                llDate.setVisibility(View.GONE);
                tvHintMid.setVisibility(View.VISIBLE);
                ivStatus.setImageResource(R.drawable.ic_activation_success);
                tvStatus.setText("设置成功");
                tvHintMid.setText(midHint);
                tvSetTransactionPwd.setText("返回");
        }
    }

    public static void startIt(Activity mActivity, String status, String walletTotalAmount, String withdrawalsAmount, String consumeAmount, String bottomHint, String midHint) {
        Intent intent = new Intent(mActivity, ActivationStatusActivity.class);
        intent.putExtra("status", status);
        intent.putExtra("walletTotalAmount", walletTotalAmount);
        intent.putExtra("withdrawalsAmount", withdrawalsAmount);
        intent.putExtra("consumeAmount", consumeAmount);
        intent.putExtra("bottomHint", bottomHint);
        intent.putExtra("midHint", midHint);
        mActivity.startActivity(intent);
    }


    @OnClick(R.id.tv_set_transaction_pwd)
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (StringUtils.getTextFromView(tvSetTransactionPwd)) {
            case "设置交易密码":
                TransactionPwdActivity.startIt(mBaseContext);
                break;
            case "返回":
                MainActivity.startIt(mBaseContext);
                break;
        }
    }

}
