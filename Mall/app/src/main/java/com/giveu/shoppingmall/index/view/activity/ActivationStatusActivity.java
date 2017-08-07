package com.giveu.shoppingmall.index.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.mynet.BaseBean;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.me.view.activity.IdentifyActivity;
import com.giveu.shoppingmall.me.view.activity.RequestPasswordActivity;
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
    String flag;//找回交易密码的标记
    //钱包激活设置交易密码
    public static void startSetPwd(Activity mActivity) {
        Intent intent = new Intent(mActivity, ActivationStatusActivity.class);
        mActivity.startActivity(intent);
    }

    //用于finish之前页面（找回交易密码）
    public static void startSetPwd(Activity mActivity, String flag) {
        Intent intent = new Intent(mActivity, ActivationStatusActivity.class);
        intent.putExtra("flag", flag);
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
    public static void startShowResultFail(Activity mActivity, BaseBean errorBean, String status) {
        Intent intent = new Intent(mActivity, ActivationStatusActivity.class);
        intent.putExtra("message", errorBean.message);
        intent.putExtra("status", status);
        intent.putExtra("code", errorBean.code);
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
                BaseApplication.getInstance().finishActivity(WalletActivationFirstActivity.class);
                break;
            case BACK:
                //返回
                if ("transaction".equals(flag)) {
                    BaseApplication.getInstance().finishActivity(IdentifyActivity.class);
                    BaseApplication.getInstance().finishActivity(TransactionPwdActivity.class);
                    BaseApplication.getInstance().finishActivity(RequestPasswordActivity.class);
                }
                finish();
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
        flag = getIntent().getStringExtra("flag");
        if ("transaction".equals(flag)) {
            baseLayout.setTitle("设置成功");
        }else{
            baseLayout.setTitle("钱包激活");
        }
        switch (status) {
            case "success":
                //激活成功
                String globleLimit = getIntent().getStringExtra("globleLimit");
                String cyLimit = getIntent().getStringExtra("cyLimit");
                String posLimit = getIntent().getStringExtra("posLimit");
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
                String code = StringUtils.nullToEmptyString(getIntent().getStringExtra("code"));
                llDate.setVisibility(View.GONE);
                tvHintMid.setVisibility(View.VISIBLE);
                tvHintMid.setText(message);
                ivStatus.setImageResource(R.drawable.ic_activation_fail);
                tvStatus.setText("激活失败");
                tvSetTransactionPwd.setText("重新激活钱包");
                tvSetTransactionPwd.setTag(REPEAT);
                tvHintBottom.setVisibility(View.GONE);
                if ("sc800115".equals(code) || "sc800705".equals(code)) {
                    //800115:激活页面一跳转过来，没有资质；800705:激活页面二跳转过来，激活多次失败，不能再激活
                    tvSetTransactionPwd.setVisibility(View.GONE);
                } else {
                    tvSetTransactionPwd.setVisibility(View.VISIBLE);
                }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 101) {
            finish();
        }
    }
}
