package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.me.presenter.LivingAddressPresenter;
import com.giveu.shoppingmall.me.view.agent.ILivingAddressView;
import com.giveu.shoppingmall.widget.ClickEnabledTextView;
import com.giveu.shoppingmall.widget.dialog.ChooseCityDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 513419 on 2017/8/9.
 * 居住地址
 */

public class LivingAddressActivity extends BaseActivity implements ILivingAddressView {
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_detail_address)
    EditText etDetailAddress;
    @BindView(R.id.tv_commit)
    ClickEnabledTextView tvCommit;
    @BindView(R.id.tv_address)
    TextView tvAddress;

    private ChooseCityDialog chooseCityDialog;

    private LivingAddressPresenter presenter;

    public static void startIt(Activity activity) {
        Intent intent = new Intent(activity, LivingAddressActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_living_address);
        baseLayout.setTitle("我的居住地址");
        chooseCityDialog = new ChooseCityDialog(mBaseContext);
        presenter = new LivingAddressPresenter(this);
    }

    @Override
    protected BasePresenter[] initPresenters() {
        return new BasePresenter[]{presenter};
    }

    @Override
    public void setData() {

    }

    @Override
    public void setListener() {
        super.setListener();
        chooseCityDialog.setOnConfirmListener(new ChooseCityDialog.OnConfirmListener() {
            @Override
            public void onConfirm(String province, String city, String region, String street) {
                tvAddress.setText(province + city + region + street);
                chooseCityDialog.dismiss();
            }
        });
    }

    @OnClick(R.id.ll_choose_address)
    public void chooseCity() {
        chooseCityDialog.show();
    }

}
