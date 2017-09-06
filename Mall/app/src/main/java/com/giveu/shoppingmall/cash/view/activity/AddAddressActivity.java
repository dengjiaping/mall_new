package com.giveu.shoppingmall.cash.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.AddressListResponse;
import com.giveu.shoppingmall.utils.Const;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.listener.TextChangeListener;
import com.giveu.shoppingmall.widget.EditView;
import com.giveu.shoppingmall.widget.dialog.ChooseCityDialog;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 513419 on 2017/6/26.
 */

public class AddAddressActivity extends BaseActivity {
    @BindView(R.id.et_receiving_name)
    EditView etReceivingName;
    @BindView(R.id.et_receiving_phone)
    EditView etReceivingPhone;
    @BindView(R.id.et_detail_address)
    EditText etDetailAddress;
    @BindView(R.id.cb_default)
    CheckBox cbDefault;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    private String province;
    private String city;
    private String region;
    private String street;
    private ChooseCityDialog chooseCityDialog;
    boolean canCommit = false;//默认不满足条件添加地址
    public static final String ADD = "add";
    public static final String EDIT = "edit";

    public static void startIt(Activity activity) {
        Intent intent = new Intent(activity, AddAddressActivity.class);
        intent.putExtra("type", ADD);
        activity.startActivityForResult(intent, Const.ADDRESSMANAGE);
    }

    public static void startIt(Activity activity, AddressListResponse item) {
        Intent intent = new Intent(activity, AddAddressActivity.class);
        intent.putExtra("item", item);
        intent.putExtra("type", EDIT);
        activity.startActivityForResult(intent, Const.ADDRESSMANAGE);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_address);
        chooseCityDialog = new ChooseCityDialog(mBaseContext);
        baseLayout.setTitle("订单信息确认");
        baseLayout.setRightTextColor(R.color.color_00bbc0);
        final AddressListResponse item = (AddressListResponse) getIntent().getSerializableExtra("item");
        if (item != null) {
            //修改地址带过来的参数
            etReceivingName.setText(StringUtils.nullToEmptyString(item.custName));
            etReceivingPhone.setText(StringUtils.nullToEmptyString(item.phone));
            etDetailAddress.setText(StringUtils.nullToEmptyString(item.address));
            tvAddress.setText(StringUtils.nullToEmptyString(item.province) + StringUtils.nullToEmptyString(item.city) + StringUtils.nullToEmptyString(item.region) + StringUtils.nullToEmptyString(item.street));
            tvAddress.setTextColor(ContextCompat.getColor(mBaseContext, R.color.color_282828));
            cbDefault.setChecked(1 == item.isDefault ? true : false);
        }
        baseLayout.setRightTextAndListener("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canCommit) {
                    //满足条件
                    String building = etDetailAddress.getText().toString();
                    String phone = etReceivingPhone.getText().toString();
                    String name = etReceivingName.getText().toString();
                    String isDefault = cbDefault.isChecked() ? "1" : "0";
                    //添加居住地址
                    String type = getIntent().getStringExtra("type");

                    if (StringUtils.isNotNull(type)) {
                        switch (type) {
                            case ADD:
                                //添加地址
                                ApiImpl.addAddress(mBaseContext, building, "5", city, name, LoginHelper.getInstance().getIdPerson(), isDefault, phone, province, region, street, new BaseRequestAgent.ResponseListener<BaseBean>() {
                                    @Override
                                    public void onSuccess(BaseBean response) {
                                        ToastUtils.showShortToast("添加地址成功");
                                        setResult(RESULT_OK);
                                        finish();
                                    }

                                    @Override
                                    public void onError(BaseBean errorBean) {
                                        CommonLoadingView.showErrorToast(errorBean);
                                    }
                                });
                                break;
                            case EDIT:
                                //修改地址，如果地址选择器没有选，才使用带过来的数据
                                city = StringUtils.isNull(city) ? item.city : city;
                                province = StringUtils.isNull(province) ? item.province : province;
                                region = StringUtils.isNull(region) ? item.region : region;
                                street = StringUtils.isNull(street) ? item.street : street;
                                ApiImpl.updateAddress(mBaseContext, building, "5", city, name, item.id, LoginHelper.getInstance().getIdPerson(), isDefault, phone, province, region, street, new BaseRequestAgent.ResponseListener<BaseBean>() {
                                    @Override
                                    public void onSuccess(BaseBean response) {
                                        ToastUtils.showShortToast("修改地址成功");
                                        setResult(RESULT_OK);
                                        finish();
                                    }

                                    @Override
                                    public void onError(BaseBean errorBean) {
                                        CommonLoadingView.showErrorToast(errorBean);
                                    }
                                });
                                break;
                        }
                    }
                } else {
                    canClick(true);
                }
            }
        });
    }

    @OnClick(R.id.ll_choose_address)
    @Override
    public void onClick(View view) {
        super.onClick(view);
        chooseCityDialog.show();
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
        etReceivingName.checkFormat(EditView.Style.NAME);
        etReceivingPhone.checkFormat(EditView.Style.PHONE);
        etReceivingName.addTextChangedListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                canClick(false);
            }
        });

        etReceivingPhone.addTextChangedListener(new TextChangeListener() {
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

    @Override
    public void setData() {

    }

    private boolean canClick(boolean showToast) {
        canCommit = false;
        if (!StringUtils.checkUserNameAndTipError(etReceivingName.getText().toString(), showToast)) {
            return false;
        }
        if (etReceivingPhone.getText().toString().length() != 11) {
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
        canCommit = true;
        return true;
    }
}
