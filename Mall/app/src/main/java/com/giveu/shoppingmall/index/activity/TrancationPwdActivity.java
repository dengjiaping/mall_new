package com.giveu.shoppingmall.index.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.view.InputView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 101900 on 2017/6/20.
 */

public class TrancationPwdActivity extends BaseActivity {
    @BindView(R.id.tv_string)
    TextView tvString;
    @BindView(R.id.inputview_dialog)
    InputView inputviewDialog;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_transacation_pwd);
        baseLayout.setTitle("设置交易密码");
    }

    @Override
    public void setData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
