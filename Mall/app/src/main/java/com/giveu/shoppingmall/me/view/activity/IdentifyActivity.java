package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.widget.EditText;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.index.view.activity.TransactionPwdActivity;
import com.giveu.shoppingmall.me.presenter.IdentifyPresenter;
import com.giveu.shoppingmall.me.view.agent.IIdentifyView;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.listener.TextChangeListener;
import com.giveu.shoppingmall.widget.ClickEnabledTextView;
import com.giveu.shoppingmall.widget.EditView;

import butterknife.BindView;
import butterknife.OnClick;

import static com.giveu.shoppingmall.me.view.activity.RequestPasswordActivity.CHANGE_LOGIN_PWD;
import static com.giveu.shoppingmall.me.view.activity.RequestPasswordActivity.CHANGE_TRADE_PWD;
import static com.giveu.shoppingmall.me.view.activity.RequestPasswordActivity.FIND_LOGIN_PWD;
import static com.giveu.shoppingmall.me.view.activity.RequestPasswordActivity.FIND_TRADE_PWD;

/**
 * Created by 513419 on 2017/6/30.
 */

public class IdentifyActivity extends BaseActivity implements IIdentifyView {

    @BindView(R.id.et_username)
    EditView etUsername;
    @BindView(R.id.et_identNo)
    EditText etIdentNo;
    @BindView(R.id.tv_next)
    ClickEnabledTextView tvNext;
    private String randCode;
    private String mobile;
    private IdentifyPresenter presenter;
    private int type;

    public static void startIt(Activity activity, String randCode, String mobile, String smsCode, int type) {
        Intent intent = new Intent(activity, IdentifyActivity.class);
        intent.putExtra("mobile", mobile);
        intent.putExtra("randCode", randCode);
        intent.putExtra("type",  type);
        intent.putExtra("smsCode", smsCode);
        activity.startActivityForResult(intent, SetPasswordActivity.REQUEST_FINISH);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_identify);
        mobile = getIntent().getStringExtra("mobile");
        randCode = getIntent().getStringExtra("randCode");
        type = getIntent().getIntExtra("type", 1);
        switch (type) {
            case FIND_LOGIN_PWD:
                baseLayout.setTitle("找回登录密码");
                break;
            case CHANGE_LOGIN_PWD:
                baseLayout.setTitle("修改登录密码");
                break;
            case FIND_TRADE_PWD:
                baseLayout.setTitle("找回交易密码");
                break;
            case CHANGE_TRADE_PWD:
                baseLayout.setTitle("修改交易密码");
                break;
        }
        presenter = new IdentifyPresenter(this);
    }

    @Override
    protected BasePresenter[] initPresenters() {
        return new BasePresenter[]{presenter};
    }

    @Override
    public void setData() {

    }

    @OnClick({R.id.tv_next})
    public void nextStep() {
        if (canClick(true)) {
            if (type == FIND_TRADE_PWD || type == CHANGE_TRADE_PWD) {
                //找回交易密码
                String ident = StringUtils.getTextFromView(etIdentNo);
                String userName = StringUtils.getTextFromView(etUsername);
                if (LoginHelper.getInstance() != null) {
                    String localIdent = LoginHelper.getInstance().getIdent();
                    String localRealName = LoginHelper.getInstance().getName();
                    if (StringUtils.isNotNull(localIdent) && StringUtils.isNotNull(localRealName)) {
                        //交易密码验证身份信息
                        if (localIdent.equals(ident) && localRealName.equals(userName)) {
                            //本地验证通过
                            String smsCode = getIntent().getStringExtra("smsCode");
                            TransactionPwdActivity.startItWithCode(mBaseContext, mobile, smsCode);
                        }else{
                            ToastUtils.showShortToast("姓名身份证验证失败！");
                        }
                    }else{
                        ToastUtils.showShortToast("姓名身份证验证失败！");
                    }
                }

            } else {
                //登录密码验证身份信息
                presenter.checkUserInfo(etIdentNo.getText().toString(), mobile, randCode, etUsername.getText().toString());
            }
        }
    }

    @Override
    public void setListener() {
        super.setListener();
        etUsername.addTextChangedListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                canClick(false);
            }
        });
        etIdentNo.addTextChangedListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                canClick(false);
            }
        });
    }

    private boolean canClick(boolean showToast) {
        tvNext.setClickEnabled(false);
        if (!StringUtils.checkUserNameAndTipError(etUsername.getText().toString(), showToast)) {
            return false;
        }
        if (!StringUtils.checkIdCardAndTipError(etIdentNo.getText().toString(), showToast)) {
            return false;
        }
        tvNext.setClickEnabled(true);
        return true;
    }

    @Override
    public void checkSuccess(String randCode) {
        //找回登录密码，身份校验成功，跳转至密码重置
        SetPasswordActivity.startItWithRandCode(mBaseContext, false, mobile, randCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SetPasswordActivity.REQUEST_FINISH && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
