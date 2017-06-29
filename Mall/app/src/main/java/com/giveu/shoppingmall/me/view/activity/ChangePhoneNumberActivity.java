package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.listener.TextChangeListener;
import com.giveu.shoppingmall.widget.ClickEnabledTextView;
import com.giveu.shoppingmall.widget.EditView;
import com.giveu.shoppingmall.widget.SendCodeTextView;
import com.giveu.shoppingmall.widget.dialog.NormalHintDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改手机号
 * Created by 101900 on 2017/6/29.
 */

public class ChangePhoneNumberActivity extends BaseActivity {


    @BindView(R.id.tv_send_code)
    SendCodeTextView tvSendCode;
    @BindView(R.id.et_phone_number)
    EditView etPhoneNumber;
    @BindView(R.id.et_send_code)
    EditView etSendCode;
    @BindView(R.id.tv_finish)
    ClickEnabledTextView tvFinish;

    public static void startIt(Activity mActivity) {
        Intent intent = new Intent(mActivity, ChangePhoneNumberActivity.class);
        mActivity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_change_phone_number);
        baseLayout.setTitle("修改手机号");
        tvSendCode.setSendTextColor(false);
    }

    @Override
    public void setListener() {
        super.setListener();
        etPhoneNumber.checkFormat(11);
        etSendCode.checkFormat(6);
        etPhoneNumber.addTextChangedListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                buttonCanClick(false);
//                if(s.length() == 11){
//                    tvSendCode.setTextColor(getResources().getColor(R.color.title_color));
//                }else{
//                    tvSendCode.setTextColor(getResources().getColor(R.color.grey_a5a5a5));
//                }
            }
        });

        etSendCode.addTextChangedListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                buttonCanClick(false);
            }
        });
    }

    @Override
    public void setData() {

    }

    private void buttonCanClick(boolean showToast) {
        tvFinish.setClickEnabled(false);

        String phoneNumber = StringUtils.getTextFromView(etPhoneNumber);
        String sendCode = StringUtils.getTextFromView(etSendCode);
        if (!StringUtils.checkPhoneNumberAndTipError(phoneNumber, showToast)) {
            return;
        }
        if (StringUtils.isNull(sendCode)) {
            if (showToast) {
                ToastUtils.showShortToast("请输入6位验证码");
            }
            return;
        } else if (sendCode.length() != 6) {
            if (showToast) {
                ToastUtils.showShortToast("请输入6位验证码");
            }
            return;
        }
        if (!showToast) {
            tvFinish.setClickEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tvSendCode != null) {
            tvSendCode.onDestory();
        }
    }


    @OnClick({R.id.tv_send_code, R.id.tv_finish})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_send_code:
                String phoneNumber = StringUtils.getTextFromView(etPhoneNumber);
                if (phoneNumber.length() != 11) {
                    // ToastUtils.showShortToast("请输入11位的手机号");
                    NormalHintDialog normalHintDialog = new NormalHintDialog(mBaseContext, "请输入您本人的手机号码");
                    normalHintDialog.showDialog();
                } else {
                    tvSendCode.startCount(null);
                }
                break;
            case R.id.tv_finish:
                if (tvFinish.isClickEnabled()) {
                    NormalHintDialog dialog = new NormalHintDialog(mBaseContext, "绑定手机修改成功！\n", "登陆手机号已同步，请通过绑定手机+登陆密码登陆");
                    dialog.showDialog();
                } else {
                    buttonCanClick(true);
                }
                break;
        }
    }

}
