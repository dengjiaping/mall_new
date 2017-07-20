package com.giveu.shoppingmall.recharge.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.event.OrderDialogEvent;
import com.giveu.shoppingmall.utils.EventBusUtils;
import com.giveu.shoppingmall.widget.ClickEnabledTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 充值成功页
 * Created by 101900 on 2017/6/28.
 */

public class RechargeStatusActivity extends BaseActivity {

    @BindView(R.id.tv_btn_top)
    ClickEnabledTextView tvBtnTop;
    @BindView(R.id.tv_back)
    ClickEnabledTextView tvBack;
    @BindView(R.id.iv_status)
    ImageView ivStatus;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_hint_mid)
    TextView tvHintMid;
    @BindView(R.id.tv_date_top)
    TextView tvDateTop;
    @BindView(R.id.tv_date_mid)
    TextView tvDateMid;
    @BindView(R.id.tv_hint_bottom)
    TextView tvHintBottom;
    String status;

    public static void startIt(Activity mActivity, String status, String hintMid, String rechargeAmount, String paymentAmount, String hintBottom) {
        Intent intent = new Intent(mActivity, RechargeStatusActivity.class);
        intent.putExtra("status", status);
        intent.putExtra("hintMid", hintMid);
        intent.putExtra("rechargeAmount", rechargeAmount);
        intent.putExtra("paymentAmount", paymentAmount);
        intent.putExtra("hintBottom", hintBottom);
        mActivity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_recharge_status);
        baseLayout.setTitle("充值");
    }

    @Override
    public void setData() {
        //支付成功或失败

        status = getIntent().getStringExtra("status");
        //中间状态提示语
        String hintMid = getIntent().getStringExtra("hintMid");
        //充值金额
        String rechargeAmount = getIntent().getStringExtra("rechargeAmount");
        //支付金额
        String paymentAmount = getIntent().getStringExtra("paymentAmount");
        //下面的提示语
        String hintBottom = getIntent().getStringExtra("hintBottom");
        switch (status) {
            case "success":
                ivStatus.setImageResource(R.drawable.ic_activation_success);
                tvStatus.setText("支付成功");
                tvDateTop.setText(rechargeAmount);
                tvDateMid.setText(paymentAmount);
                tvHintBottom.setVisibility(View.VISIBLE);
                tvHintBottom.setText(hintBottom);
                tvHintMid.setVisibility(View.INVISIBLE);
                tvBtnTop.setText("查看充值订单");
                tvBtnTop.setVisibility(View.GONE);
                tvBack.setVisibility(View.VISIBLE);
                tvBtnTop.setBackgroundResource(R.color.title_color);
                tvBack.setBackgroundResource(R.drawable.shape_back_btn_blue);
                break;
            case "fail":
                ivStatus.setImageResource(R.drawable.ic_activation_fail);
                tvStatus.setText("支付失败");
                tvDateTop.setText(rechargeAmount);
                tvDateMid.setText(paymentAmount);
                tvHintBottom.setVisibility(View.GONE);
                tvHintMid.setVisibility(View.VISIBLE);
                tvHintMid.setText(hintMid);
                tvBtnTop.setVisibility(View.VISIBLE);
                tvBtnTop.setText("重新支付");
                tvBtnTop.setBackgroundResource(R.color.title_color);
                tvBack.setVisibility(View.GONE);
                break;
        }
    }

    @OnClick({R.id.tv_btn_top, R.id.tv_back})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_btn_top:
                //查看充值订单
                switch (status) {
                    case "success":
                        //跳转订单页
                        break;
                    case "fail":
                        EventBusUtils.poseEvent(new OrderDialogEvent());
                        finish();
                        break;
                }
                break;
            case R.id.tv_back:
                //返回
                finish();
                break;
        }
    }

}
