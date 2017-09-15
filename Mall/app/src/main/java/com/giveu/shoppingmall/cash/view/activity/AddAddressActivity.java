package com.giveu.shoppingmall.cash.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.fastaccess.permission.base.PermissionHelper;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BasePermissionActivity;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.AddressListResponse;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.listener.TextChangeListener;
import com.giveu.shoppingmall.widget.EditView;
import com.giveu.shoppingmall.widget.dialog.ChooseCityDialog;
import com.giveu.shoppingmall.widget.dialog.ConfirmDialog;
import com.giveu.shoppingmall.widget.dialog.PermissionDialog;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by 513419 on 2017/6/26.
 */

public class AddAddressActivity extends BasePermissionActivity {
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
    @BindView(R.id.iv_add_phone)
    ImageView ivAddPhone;
    private String province;
    private String city;
    private String region;
    private String street;
    private ChooseCityDialog chooseCityDialog;
    boolean canCommit = false;//默认不满足条件添加地址
    public static final String ADD = "add";
    public static final String EDIT = "edit";
    private PermissionDialog permissionDialog;
    AddressListResponse addressResponse;

    public static void startItForResult(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, AddAddressActivity.class);
        intent.putExtra("type", ADD);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void startItForResult(Activity activity, AddressListResponse item, int requestCode) {
        Intent intent = new Intent(activity, AddAddressActivity.class);
        intent.putExtra("item", item);
        intent.putExtra("type", EDIT);
        activity.startActivityForResult(intent, requestCode);
    }

    private void initPermissionDialog() {
        permissionDialog = new PermissionDialog(mBaseContext);
        permissionDialog.setPermissionStr("请开启读取通讯录权限后重试");
        permissionDialog.setNeedFinish(false);
        permissionDialog.setConfirmStr("去设置");
        permissionDialog.setCancleStr("暂不");
        permissionDialog.setTitle("读取通讯录权限未开启");
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_address);
        chooseCityDialog = new ChooseCityDialog(mBaseContext);
        baseLayout.setTitle("添加收货地址");
        baseLayout.setRightTextColor(R.color.color_00bbc0);
        permissionDialog = new PermissionDialog(mBaseContext);
        permissionDialog.setPermissionStr("需要通讯录权限才可正常使用");
        permissionDialog.setConfirmStr("去开启");
        permissionDialog.setOnChooseListener(new ConfirmDialog.OnChooseListener() {
            @Override
            public void confirm() {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
                //进入设置了，下次onResume时继续判断申请权限
                permissionDialog.dismiss();
            }

            @Override
            public void cancle() {
                permissionDialog.dismiss();
            }
        });
        initPermissionDialog();
        final AddressListResponse item = (AddressListResponse) getIntent().getSerializableExtra("item");
        if (item != null) {
            //修改地址带过来的参数
            etReceivingName.setText(StringUtils.nullToEmptyString(item.custName));
            etReceivingPhone.setText(StringUtils.nullToEmptyString(item.phone));
            etDetailAddress.setText(StringUtils.nullToEmptyString(item.address));
            tvAddress.setText(StringUtils.nullToEmptyString(item.province) + StringUtils.nullToEmptyString(item.city) + StringUtils.nullToEmptyString(item.region) + StringUtils.nullToEmptyString(item.street));
            tvAddress.setTextColor(ContextCompat.getColor(mBaseContext, R.color.color_282828));
            cbDefault.setChecked("1".equals(item.isDefault));
        }
        baseLayout.setRightTextAndListener("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canCommit) {
                    //满足条件
                    final String building = etDetailAddress.getText().toString();
                    final String phone = etReceivingPhone.getText().toString();
                    final String name = etReceivingName.getText().toString();
                    final String isDefault = cbDefault.isChecked() ? "1" : "0";
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
                                        addressResponse = new AddressListResponse(building, city, name, isDefault, phone, province, region, street);
                                        Intent intent = new Intent();
                                        intent.putExtra("addressResponse", addressResponse);
                                        setResult(RESULT_OK, intent);
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
                                if (item != null) {
                                    city = StringUtils.isNull(city) ? item.city : city;
                                    province = StringUtils.isNull(province) ? item.province : province;
                                    region = StringUtils.isNull(region) ? item.region : region;
                                    street = StringUtils.isNull(street) ? item.street : street;
                                    ApiImpl.updateAddress(mBaseContext, building, "5", city, name, item.id, LoginHelper.getInstance().getIdPerson(), isDefault, phone, province, region, street, new BaseRequestAgent.ResponseListener<BaseBean>() {
                                        @Override
                                        public void onSuccess(BaseBean response) {
                                            ToastUtils.showShortToast("修改地址成功");
                                            addressResponse = new AddressListResponse(building, city, name, isDefault, phone, province, region, street);
                                            Intent intent = new Intent();
                                            intent.putExtra("addressResponse", addressResponse);
                                            setResult(RESULT_OK, intent);
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
                    }
                } else {
                    canClick(true);
                }
            }
        });
    }

    @OnClick({R.id.ll_choose_address, R.id.iv_add_phone})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ll_choose_address:
                //选择地址
                chooseCityDialog.show();
                break;
            case R.id.iv_add_phone:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    applyContactPermission();
                } else {
                    skipToContactsContract();
                }
                break;
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

    /**
     * 6.0以上系统申请通讯录权限
     */
    public void applyContactPermission() {
        if (PermissionHelper.getInstance(this).isPermissionGranted(Manifest.permission.READ_CONTACTS)) {
            skipToContactsContract();
        } else {
            setPermissionHelper(false, new String[]{Manifest.permission.READ_CONTACTS});
        }
    }

    public void skipToContactsContract() {
        try {
            //跳转通讯录
            startActivityForResult(new Intent(Intent.ACTION_PICK,
                    ContactsContract.Contacts.CONTENT_URI), 0);
        } catch (Exception e) {

        }
    }

    @Override
    public void onPermissionReallyDeclined(@NonNull String permissionName) {
        super.onPermissionReallyDeclined(permissionName);
        //禁止不再询问会直接回调这方法
        permissionDialog.show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            try {
                // ContentProvider展示数据类似一个单个数据库表
                // ContentResolver实例带的方法可实现找到指定的ContentProvider并获取到ContentProvider的数据
                ContentResolver reContentResolverol = getContentResolver();
                // URI,每个ContentProvider定义一个唯一的公开的URI,用于指定到它的数据集
                Uri contactData = data.getData();
                // 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
                Cursor cursor = mBaseContext.managedQuery(contactData, null, null, null, null);
                //线上发现cursor有可能为空，如果为空不作任何处理
                if (cursor == null) {
                    ToastUtils.showShortToast("获取通讯录失败");
                    return;
                }
                if (cursor.getCount() == 0) {
                    permissionDialog.show();
                    return;
                }
                cursor.moveToFirst();
                // 条件为联系人ID
                String contactId = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.Contacts._ID));
                // 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
                Cursor phone = reContentResolverol.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                                + contactId, null, null);
                while (phone != null && phone.moveToNext()) {
                    //填入号码
                    String usernumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    final StringBuilder sb = new StringBuilder(usernumber.replaceAll(" ", ""));
                    if (sb.toString().length() != 11) {
                        ToastUtils.showShortToast("手机号码格式有误");
                    } else {
                        etReceivingPhone.setText(sb.toString());
                    }
                }
                if (phone != null) {
                    phone.close();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
