package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.me.presenter.IdentifyPresenter;
import com.giveu.shoppingmall.me.view.agent.IIdentifyView;

/**
 * Created by 513419 on 2017/6/30.
 */

public class IdentifyActivity extends BaseActivity implements IIdentifyView {

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
