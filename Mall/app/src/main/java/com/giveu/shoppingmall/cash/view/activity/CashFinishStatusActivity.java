package com.giveu.shoppingmall.cash.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.widget.ClickEnabledTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 取现完成状态页
 * Created by 101900 on 2017/6/28.
 */

public class CashFinishStatusActivity extends BaseActivity {
    String status;
    @BindView(R.id.iv_status)
    ImageView ivStatus;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_hint_mid)
    TextView tvHintMid;
    @BindView(R.id.tv_date_bottom)
    TextView tvDateBottom;
    @BindView(R.id.tv_hint_bottom)
    TextView tvHintBottom;
    @BindView(R.id.tv_btn_top)
    ClickEnabledTextView tvBtnTop;
    @BindView(R.id.tv_back)
    ClickEnabledTextView tvBack;
    @BindView(R.id.tv_date_name_bottom)
    TextView tvDateNameBottom;
    @BindView(R.id.tv_date_top)
    TextView tvDateTop;
    @BindView(R.id.tv_date_mid)
    TextView tvDateMid;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.tv_date_name_top)
    TextView tvDateNameTop;
    @BindView(R.id.tv_date_name_mid)
    TextView tvDateNameMid;
    @BindView(R.id.ll_date_mid)
    LinearLayout llDateMid;
    @BindView(R.id.ll_date)
    LinearLayout llDate;


    public static void startIt(Activity mActivity, String status, String hintMid, int cashAmount, int stageNumber, String cashDate, String creditType) {
        Intent intent = new Intent(mActivity, CashFinishStatusActivity.class);
        intent.putExtra("status", status);
        intent.putExtra("hintMid", hintMid);
        intent.putExtra("cashAmount", cashAmount);
        intent.putExtra("stageNumber", stageNumber);
        intent.putExtra("cashDate", cashDate);
        intent.putExtra("creditType", creditType);
        mActivity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_recharge_status);
        baseLayout.setTitle("我要取现");
        llBottom.setVisibility(View.VISIBLE);
        tvDateNameTop.setText("取现金额：");
        tvDateNameMid.setText("分期期数：");
        tvDateNameBottom.setText("下期还款日：");
        tvHintBottom.setText("温馨提示：保持良好的还款行为有利于提升信用额度");
    }

    @Override
    public void setData() {
        //支付成功或失败的状态
        status = getIntent().getStringExtra("status");
        //中间状态提示语
        String hintMid = getIntent().getStringExtra("hintMid");
        //取现金额
        int cashAmount = getIntent().getIntExtra("cashAmount", 0);
        //分期期数
        int stageNumber = getIntent().getIntExtra("stageNumber", 0);
        //最迟还款日/下期还款日
        String cashDate = getIntent().getStringExtra("cashDate");
        //SH:随借随还，SQ：分期
        String creditType = getIntent().getStringExtra("creditType");
        switch (status) {
            case "success":
                if ("SH".equals(creditType)) {
                    //不分期
                    llDateMid.setVisibility(View.GONE);
                    tvDateNameBottom.setText("最迟还款日：");
                }else if("SQ".equals(creditType)){
                    llDateMid.setVisibility(View.VISIBLE);
                    tvDateNameBottom.setText("下期还款日：");
                }
                llDate.setVisibility(View.VISIBLE);
                ivStatus.setImageResource(R.drawable.ic_activation_success);
                tvStatus.setText("取现操作成功");
                llDateMid.setVisibility(View.VISIBLE);
                llBottom.setVisibility(View.VISIBLE);
                tvDateTop.setText((double) cashAmount+"元");
                tvDateMid.setText(stageNumber+"期");
                cashDate = cashDate.substring(0, cashDate.indexOf("T"));//  "deductDate": "2017-08-21T00:00:00+08:00"
                cashDate = cashDate.replaceAll("-", "/");
                tvDateBottom.setText(cashDate);
                tvHintBottom.setVisibility(View.VISIBLE);
                tvHintMid.setVisibility(View.INVISIBLE);
                tvBtnTop.setText("查看放款进度");
                tvBack.setVisibility(View.VISIBLE);
                tvBtnTop.setBackgroundResource(R.color.title_color);
                tvBack.setBackgroundResource(R.drawable.shape_back_btn_blue);


                break;
            case "fail":
                llDate.setVisibility(View.GONE);
                ivStatus.setImageResource(R.drawable.ic_activation_fail);
                tvStatus.setText("取现操作失败");
                tvHintBottom.setVisibility(View.GONE);
                tvHintMid.setVisibility(View.VISIBLE);
                tvHintMid.setText(hintMid);
                tvBtnTop.setText("返回");
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
                //查看放款进度
                switch (status) {
                    case "success":
                        //跳转放款进度
                        CaseRecordActivity.startIt(mBaseContext);
                        break;
                    case "fail":
                        CashTypeActivity.startIt(mBaseContext,null);
                        break;
                }
                break;
            case R.id.tv_back:
                //返回
                CashTypeActivity.startIt(mBaseContext,null);
                break;
        }
    }

}
