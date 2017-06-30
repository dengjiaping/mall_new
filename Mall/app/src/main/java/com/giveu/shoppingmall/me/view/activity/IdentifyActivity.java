package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.widget.EditText;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.me.presenter.IdentifyPresenter;
import com.giveu.shoppingmall.me.view.agent.IIdentifyView;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.listener.TextChangeListener;
import com.giveu.shoppingmall.widget.ClickEnabledTextView;
import com.giveu.shoppingmall.widget.EditView;

import butterknife.BindView;
import butterknife.OnClick;

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

    public static void startIt(Activity activity, String randCode, String mobile) {
        Intent intent = new Intent(activity, IdentifyActivity.class);
        intent.putExtra("mobile", mobile);
        intent.putExtra("randCode", randCode);
        activity.startActivityForResult(intent, SetPasswordActivity.REQUEST_FINISH);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_identify);
        baseLayout.setTitle("找回登录密码");
        randCode = getIntent().getStringExtra("randCode");
        presenter = new IdentifyPresenter(this);
    }

    @Override
    protected BasePresenter[] initPresenters() {
        return new BasePresenter[]{presenter};
    }

    @Override
    public void setData() {
        mobile = getIntent().getStringExtra("mobile");
        randCode = getIntent().getStringExtra("randCode");
    }

    @OnClick({R.id.tv_next})
    public void nextStep() {
        if (canClick(true)) {
            presenter.checkUserInfo(etIdentNo.getText().toString(), mobile, randCode, etUsername.getText().toString());
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
        if (StringUtils.checkUserNameAndTipError(etUsername.getText().toString(), showToast)) {
            return false;
        }
        if (StringUtils.checkIdCardAndTipError(etIdentNo.getText().toString(), showToast)) {
            return false;
        }
        tvNext.setClickEnabled(true);
        return true;
    }

    @Override
    public void checkSuccess(String randCode) {
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
