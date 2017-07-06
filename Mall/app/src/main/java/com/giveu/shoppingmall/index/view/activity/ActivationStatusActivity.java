package com.giveu.shoppingmall.index.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.model.bean.response.WalletActivationResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.StringUtils;

import butterknife.BindView;

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
    @BindView(R.id.tv_globle_limit)
    TextView tvGlobleLimit;
    @BindView(R.id.tv_cy_limit)
    TextView tvCyLimit;
    @BindView(R.id.tv_pos_limit)
    TextView tvPosLimit;

    //钱包激活设置交易密码
    public static void startSetPwd(Activity mActivity) {
        Intent intent = new Intent(mActivity, ActivationStatusActivity.class);
        mActivity.startActivity(intent);
    }

    //显示结果
    public static void startShowResult(Activity mActivity, WalletActivationResponse response, int idPerson) {
        Intent intent = new Intent(mActivity, ActivationStatusActivity.class);
        if (response != null) {
            intent.putExtra("status", response.result);
            if (response.data != null) {
                //总额度
                intent.putExtra("globleLimit", response.data.globleLimit);
                //提现额度
                intent.putExtra("cyLimit", response.data.cyLimit);
                //消费额度
                intent.putExtra("posLimit", response.data.posLimit);
                //额度为0的提示语
                intent.putExtra("midHint", response.data.lab);
            }
        }
        intent.putExtra("idPerson", idPerson);
        mActivity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_activation_status);
        baseLayout.setTitle("钱包激活");
    }


    @Override
    public void setListener() {
        super.setListener();
//防止快速点击
        if (CommonUtils.isFastDoubleClick(R.id.tv_set_transaction_pwd)) {
            return;
        }
        switch (StringUtils.getTextFromView(tvSetTransactionPwd)) {
            case "设置交易密码":
                tvSetTransactionPwd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int idPerson = getIntent().getIntExtra("idPerson", 0);
                        TransactionPwdActivity.startIt(mBaseContext, idPerson);
                    }
                });
                break;
            case "返回":
                tvSetTransactionPwd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity.startIt(mBaseContext);
                    }
                });
                break;
        }
    }

    @Override
    public void setData() {
        String status = StringUtils.nullToEmptyString(getIntent().getStringExtra("status"));
        double globleLimit = getIntent().getDoubleExtra("globleLimit", 0.00);
        double cyLimit = getIntent().getDoubleExtra("cyLimit", 0.00);
        double posLimit = getIntent().getDoubleExtra("posLimit", 0.00);
        String midHint = StringUtils.nullToEmptyString(getIntent().getStringExtra("midHint"));
        // status = null;
        switch (status) {
            case "success":
                //激活成功
                llDate.setVisibility(View.VISIBLE);
                tvHintMid.setVisibility(View.GONE);
                tvGlobleLimit.setText(String.valueOf(globleLimit));
                tvCyLimit.setText(String.valueOf(cyLimit));
                tvPosLimit.setText(String.valueOf(posLimit));
                ivStatus.setImageResource(R.drawable.ic_activation_success);
                tvStatus.setText("激活成功");
                tvSetTransactionPwd.setText("设置交易密码");
                break;
            case "fail":
                // 激活失败，无额度，含中间提示语
                llDate.setVisibility(View.GONE);
                tvHintMid.setVisibility(View.VISIBLE);
                ivStatus.setImageResource(R.drawable.ic_activation_fail);
                tvStatus.setText("激活失败");
                tvHintMid.setText(midHint);
                tvSetTransactionPwd.setText("重新激活钱包");
                break;
            default:
                //设置完交易密码后跳转的设置成功页
                tvHintBottom.setVisibility(View.GONE);
                llDate.setVisibility(View.GONE);
                tvHintMid.setVisibility(View.VISIBLE);
                ivStatus.setImageResource(R.drawable.ic_activation_success);
                tvStatus.setText("设置成功");
                tvHintMid.setText("设置成功！请牢记你的交易密码");
                tvSetTransactionPwd.setText("返回");
                break;
        }
    }
}
