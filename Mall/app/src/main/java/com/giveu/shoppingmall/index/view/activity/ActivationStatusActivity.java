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
    @BindView(R.id.tv_status)
    TextView tvStatus;
    public final int BACK = 1;
    public final int SETPWD = 2;
    public final int REPEAT = 3;

    //钱包激活设置交易密码
    public static void startSetPwd(Activity mActivity) {
        Intent intent = new Intent(mActivity, ActivationStatusActivity.class);
        mActivity.startActivity(intent);
    }

    //显示成功结果
    public static void startShowResultSuccess(Activity mActivity, WalletActivationResponse response, String idPerson) {
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
                intent.putExtra("lab", response.data.lab);
            }
        }
        intent.putExtra("idPerson", idPerson);
        mActivity.startActivity(intent);
    }

    //显示失败结果
    public static void startShowResultFail(Activity mActivity, String message, String status) {
        Intent intent = new Intent(mActivity, ActivationStatusActivity.class);
        intent.putExtra("message", message);
        intent.putExtra("status", status);
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
    }

    @OnClick(R.id.tv_set_transaction_pwd)
    @Override
    public void onClick(View view) {
        super.onClick(view);
        int tag = (int) view.getTag();
        switch (tag) {
            case SETPWD:
                //设置交易密码
                String idPerson = getIntent().getStringExtra("idPerson");
                TransactionPwdActivity.startIt(mBaseContext, idPerson);

                break;
            case BACK:
                //返回
                MainActivity.startIt(mBaseContext);
                break;
            case REPEAT:
                //重新激活钱包
                WalletActivationFirstActivity.startIt(mBaseContext);
                break;
        }
    }

    @Override
    public void setData() {
        String status = StringUtils.nullToEmptyString(getIntent().getStringExtra("status"));
        switch (status) {
            case "success":
                //激活成功
                double globleLimit = getIntent().getDoubleExtra("globleLimit", 0.00);
                double cyLimit = getIntent().getDoubleExtra("cyLimit", 0.00);
                double posLimit = getIntent().getDoubleExtra("posLimit", 0.00);
                String lab = StringUtils.nullToEmptyString(getIntent().getStringExtra("lab"));
                llDate.setVisibility(View.VISIBLE);
                tvHintMid.setVisibility(View.GONE);
                tvGlobleLimit.setText(String.valueOf(globleLimit));
                tvCyLimit.setText(String.valueOf(cyLimit));
                tvPosLimit.setText(String.valueOf(posLimit));
                ivStatus.setImageResource(R.drawable.ic_activation_success);
                tvStatus.setText("激活成功");
                tvSetTransactionPwd.setText("设置交易密码");
                tvSetTransactionPwd.setTag(SETPWD);
                tvHintBottom.setVisibility(View.VISIBLE);
                tvHintBottom.setText(lab);
                break;
            case "fail":
                // 激活失败，无额度，含中间提示语
                String message = StringUtils.nullToEmptyString(getIntent().getStringExtra("message"));
                llDate.setVisibility(View.GONE);
                tvHintMid.setVisibility(View.VISIBLE);
                tvHintMid.setText(message);
                ivStatus.setImageResource(R.drawable.ic_activation_fail);
                tvStatus.setText("激活失败");
                tvSetTransactionPwd.setTag(REPEAT);
                tvHintBottom.setVisibility(View.GONE);
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
                tvSetTransactionPwd.setTag(BACK);
                break;
        }
    }
}
