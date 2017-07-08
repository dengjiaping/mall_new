package com.giveu.shoppingmall.cash.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.me.presenter.SendSmsPresenter;
import com.giveu.shoppingmall.me.view.agent.ISendSmsView;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.widget.PassWordInputView;
import com.giveu.shoppingmall.widget.SendCodeTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 513419 on 2017/6/26.
 */

public class VerifyActivity extends BaseActivity implements ISendSmsView {

    @BindView(R.id.tv_send_code)
    SendCodeTextView tvSendCode;
    @BindView(R.id.input_view_pwd)
    PassWordInputView inputViewPwd;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    private SendSmsPresenter<ISendSmsView> presenter;
    public static final int REUQEST_FINISH = 10000;
    private String statusType;
    private String codeType = "";

    public static final String CASH = "cash";
    public static final String RECHARGE = "recharge";
    public static final String BANKCARD = "bankCard";

    public static void startIt(Activity activity, String statusType) {
        Intent intent = new Intent(activity, VerifyActivity.class);
        intent.putExtra("statusType", statusType);
        activity.startActivityForResult(intent, REUQEST_FINISH);
    }

    public static void startIt(Fragment fragment, String statusType) {
        Intent intent = new Intent(fragment.getActivity(), VerifyActivity.class);
        intent.putExtra("statusType", statusType);
        fragment.startActivityForResult(intent, REUQEST_FINISH);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_verify);
        baseLayout.setTitle("验证");
        //   tvSendCode.startCount(null);
        tvSendCode.setSendTextColor(false);
        presenter = new SendSmsPresenter<ISendSmsView>(this);
        statusType = getIntent().getStringExtra("statusType");
        tvPhone.setText(LoginHelper.getInstance().getPhone());
        switch (statusType) {
            case CASH:
                break;

            case RECHARGE:
                codeType = "recharge";
                break;
        }
        presenter.sendSMSCode(LoginHelper.getInstance().getPhone(), codeType);
    }


    @Override
    public void setListener() {
        super.setListener();
        inputViewPwd.setInputCallBack(new PassWordInputView.InputCallBack() {
            @Override
            public void onInputFinish(String result) {
                if (6 == result.length()) {
                    presenter.checkSMSCode(LoginHelper.getInstance().getPhone(), result, codeType);
       /*             if ("123456".equals(result)) {
                        Random random = new Random();
                        int randomNum = random.nextInt(2) + 1;
                        switch (statusType) {
                            case PwdDialog.statusType.CASH:
                                //跳转取现状态页
                                if (randomNum == 1) {
                                    CashFinishStatusActivity.startIt(mBaseContext, "fail", "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx", null, null, null);
                                } else {
                                    CashFinishStatusActivity.startIt(mBaseContext, "success", null, "3000.00元", "9期", "2017/05/18");
                                }
                                break;
                            case PwdDialog.statusType.RECHARGE:
                                //跳转充值状态页
                                if (randomNum == 1) {
                                    RechargeStatusActivity.startIt(mBaseContext, "fail", "很抱歉，本次支付失败，请重新发起支付", "100.00元", "98.00元", null);
                                } else {
                                    RechargeStatusActivity.startIt(mBaseContext, "success", null, "100.00元", "98.00元", "温馨提示：预计10分钟到账，充值高峰可能会有延迟，可在个人中心-我的订单查看充值订单状态");
                                }
                                break;
                        }
                        finish();
                    } else {
                        ToastUtils.showShortToast("验证码错误");
                    }*/
                }
            }
        });
    }

    @OnClick({R.id.tv_send_code})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_send_code:
                presenter.sendSMSCode(LoginHelper.getInstance().getPhone(), codeType);
                break;
        }
    }

    @Override
    public void setData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tvSendCode != null) {
            tvSendCode.onDestory();
        }
    }

    @Override
    public void sendSMSSuccess() {
        tvSendCode.startCount(null);
        switch (statusType) {
            case CASH:
                break;

            case RECHARGE:
                break;
        }
    }

    @Override
    public void checkSMSSuccess() {
        setResult(RESULT_OK);
        finish();
    }
}
