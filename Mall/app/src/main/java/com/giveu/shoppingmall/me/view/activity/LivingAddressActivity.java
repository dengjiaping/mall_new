package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.widget.EditText;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.event.PwdDialogEvent;
import com.giveu.shoppingmall.me.presenter.LivingAddressPresenter;
import com.giveu.shoppingmall.me.view.agent.ILivingAddressView;
import com.giveu.shoppingmall.model.bean.response.AddressBean;
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

import static com.giveu.shoppingmall.R.id.tv_commit;

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
    @BindView(tv_commit)
    ClickEnabledTextView tvCommit;
    @BindView(R.id.tv_address)
    TextView tvAddress;
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
    }

    @Override
    protected BasePresenter[] initPresenters() {
        return new BasePresenter[]{presenter};
    }

    @Override
    public void setData() {
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
                        ArrayList<AddressBean> addressList = gson.fromJson(addressJson,
                                new TypeToken<List<AddressBean>>() {
                                }.getType());
                        getAddListJsonSuccess(addressList);
                    } catch (Exception e) {
                        presenter.getAddListJson();
                        SharePrefUtil.getInstance().putString(Const.ADDRESS_JSON, "");
                    } finally {
                        hideLoding();
                    }
                }
            }).start();

        } else {
            presenter.getAddListJson();
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
            presenter.addLiveAddress(LoginHelper.getInstance().getIdPerson(), province, city, region, street, building);
        }
    }

    @Override
    public void addSuccess() {
        ToastUtils.showShortToast("居住地址添加成功");
        EventBusUtils.poseEvent(new PwdDialogEvent());
        finish();
    }

    @Override
    public void getAddListJsonSuccess(ArrayList<AddressBean> addressList) {
        //获取地址陈宫
        chooseCityDialog.initProvince(addressList);
    }
}
