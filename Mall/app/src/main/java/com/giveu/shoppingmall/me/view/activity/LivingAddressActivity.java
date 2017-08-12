package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.event.PwdDialogEvent;
import com.giveu.shoppingmall.me.presenter.LivingAddressPresenter;
import com.giveu.shoppingmall.me.view.agent.ILivingAddressView;
import com.giveu.shoppingmall.model.bean.response.AddressBean;
import com.giveu.shoppingmall.model.bean.response.LivingAddressBean;
import com.giveu.shoppingmall.utils.Const;
import com.giveu.shoppingmall.utils.DateUtil;
import com.giveu.shoppingmall.utils.EventBusUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.listener.TextChangeListener;
import com.giveu.shoppingmall.utils.sharePref.SharePrefUtil;
import com.giveu.shoppingmall.widget.ClickEnabledTextView;
import com.giveu.shoppingmall.widget.EditView;
import com.giveu.shoppingmall.widget.dialog.ChooseCityDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by 513419 on 2017/8/9.
 * 居住地址
 */

public class LivingAddressActivity extends BaseActivity implements ILivingAddressView {
    @BindView(R.id.et_name)
    EditView etName;
    @BindView(R.id.et_phone)
    EditView etPhone;
    @BindView(R.id.et_detail_address)
    EditText etDetailAddress;
    @BindView(R.id.tv_commit)
    ClickEnabledTextView tvCommit;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.iv_detail)
    ImageView ivDetail;
    @BindView(R.id.ll_choose_address)
    LinearLayout llChooseAddress;
    @BindView(R.id.tv_syncAddress)
    TextView tvSyncAddress;
    @BindView(R.id.tv_addressTag)
    TextView tvAddressTag;
    private String province;
    private String city;
    private String region;
    private String street;

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
        //已经有居住地址，那么只展示，否则进行填写
        if (LoginHelper.getInstance().hasExistLive()) {
            llChooseAddress.setEnabled(false);
            ivDetail.setVisibility(View.GONE);
            tvAddressTag.setText("我的居住地址");
            tvAddress.setTextColor(ContextCompat.getColor(mBaseContext, R.color.color_282828));
            tvCommit.setVisibility(View.GONE);
            tvSyncAddress.setVisibility(View.GONE);
            setEditDisabled(etPhone);
            setEditDisabled(etName);
            setEditDisabled(etDetailAddress);
            //获取居住地址信息
            presenter.getLiveAddress(LoginHelper.getInstance().getIdPerson());
        } else {
            if (StringUtils.isNull(LoginHelper.getInstance().getReceiveProvince())) {
                tvSyncAddress.setVisibility(View.GONE);
            }
        }
    }

    private void setEditDisabled(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setFocusableInTouchMode(false);
    }

    @Override
    protected BasePresenter[] initPresenters() {
        return new BasePresenter[]{presenter};
    }

    @Override
    public void setData() {
        //只是展示的话不需要获取地址列表
        if (!LoginHelper.getInstance().hasExistLive()) {

            final String addressJson = SharePrefUtil.getInstance().getString(Const.ADDRESS_JSON, "");
            String cacheTime = SharePrefUtil.getInstance().getString(Const.ADDRESS_TIME, "");
            //当本地缓存不为空，并且还是当天缓存的，那么读取本地缓存，出现异常时获取服务器数据
            if (StringUtils.isNotNull(addressJson) && cacheTime.equals(DateUtil.getCurrentTime2yyyyMMdd())) {
                showLoading();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Gson gson = new Gson();
                            final ArrayList<AddressBean> addressList = gson.fromJson(addressJson,
                                    new TypeToken<List<AddressBean>>() {
                                    }.getType());
                            if (mBaseContext != null && !mBaseContext.isFinishing()) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        getAddListJsonSuccess(addressList);
                                    }
                                });
                            }
                        } catch (Exception e) {
                            SharePrefUtil.getInstance().putString(Const.ADDRESS_JSON, "");
                            if (mBaseContext != null && !mBaseContext.isFinishing()) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        presenter.getAddListJson();
                                    }
                                });
                            }
                        } finally {
                            if (mBaseContext != null && !mBaseContext.isFinishing()) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideLoding();
                                    }
                                });
                            }

                        }
                    }
                }).start();

            } else {
                presenter.getAddListJson();
            }
        }
    }

    @Override
    public void setListener() {
        super.setListener();
        chooseCityDialog.setOnConfirmListener(new ChooseCityDialog.OnConfirmListener() {
            @Override
            public void onConfirm(String p, String c, String r, String s) {
                province = p;
                city = c;
                region = r;
                street = s;
                tvAddress.setText(province + city + region + street);
                tvAddress.setTextColor(ContextCompat.getColor(mBaseContext, R.color.color_282828));
                canClick(false);
                chooseCityDialog.dismiss();
            }
        });
        etName.checkFormat(EditView.Style.NAME);
        etPhone.checkFormat(EditView.Style.PHONE);
        etName.addTextChangedListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                canClick(false);
            }
        });

        etPhone.addTextChangedListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                canClick(false);
            }
        });
        etDetailAddress.addTextChangedListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                canClick(false);
            }
        });
    }

    private boolean canClick(boolean showToast) {
        tvCommit.setClickEnabled(false);
        if (!StringUtils.checkUserNameAndTipError(etName.getText().toString(), showToast)) {
            return false;
        }
        if (etPhone.getText().toString().length() != 11) {
            if (showToast) {
                ToastUtils.showShortToast("请输入11位的手机号");
            }
            return false;
        }

        if ("请选择".equals(tvAddress.getText().toString())) {
            if (showToast) {
                ToastUtils.showShortToast("请选择所在地区");
            }
            return false;
        }

        if (StringUtils.isNull(etDetailAddress.getText().toString())) {
            if (showToast) {
                ToastUtils.showShortToast("请补充详细居住地址");
            }
            return false;
        }
        tvCommit.setClickEnabled(true);
        return true;
    }

    @OnClick(R.id.ll_choose_address)
    public void chooseCity() {
        chooseCityDialog.show();
    }

    @OnClick(R.id.tv_commit)
    public void commitAddress() {
        if (canClick(true)) {
            String building = etDetailAddress.getText().toString();
            String phone = etPhone.getText().toString();
            String name = etName.getText().toString();
            //添加居住地址
            presenter.addLiveAddress(LoginHelper.getInstance().getIdPerson(), phone, name, province, city, region, street, building);
        }
    }

    @Override
    public void addSuccess() {
        LoginHelper.getInstance().setHasExistLive("1");
        ToastUtils.showShortToast("居住地址添加成功");
        EventBusUtils.poseEvent(new PwdDialogEvent());
        finish();
    }

    @Override
    public void getAddListJsonSuccess(ArrayList<AddressBean> addressList) {
        //获取地址陈宫
        chooseCityDialog.initProvince(addressList);
    }

    @Override
    public void getLiveAddressSuccess(LivingAddressBean data) {
        if (StringUtils.isNull(data.phone)) {
            etPhone.setHint("");
        } else {
            etPhone.setText(data.phone);
        }
        if (StringUtils.isNull(data.name)) {
            etName.setHint("");
        } else {
            etName.setText(data.name);
        }
        String address = "";
        if (StringUtils.isNotNull(data.province)) {
            address += data.province;
        }
        if (StringUtils.isNotNull(data.city)) {
            address += data.city;
        }
        if (StringUtils.isNotNull(data.region)) {
            address += data.region;
        }
        if (StringUtils.isNotNull(data.town)) {
            address += data.town;
        }
        tvAddress.setText(address);
        if (StringUtils.isNull(data.street)) {
            etDetailAddress.setHint("");
        } else {
            etDetailAddress.setText(data.street);
        }
    }

    @OnClick(R.id.tv_syncAddress)
    public void syncAddress() {
        etPhone.setText(LoginHelper.getInstance().getReceivePhone());
        etName.setText(LoginHelper.getInstance().getReceiveName());
        province = LoginHelper.getInstance().getReceiveProvince();
        city = LoginHelper.getInstance().getReceiveCity();
        region = LoginHelper.getInstance().getReceiveRegion();
        street = LoginHelper.getInstance().getReceiveStreet();
        String address = province + city + region + street;
        tvAddress.setText(address);
        tvAddress.setTextColor(ContextCompat.getColor(mBaseContext, R.color.color_282828));
        etDetailAddress.setText(LoginHelper.getInstance().getReceiveDetailAddress());
        etName.requestFocus();
        etName.setSelection(etName.getText().toString().length());
    }
}
