package com.giveu.shoppingmall.index.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.mynet.ApiUrl;
import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.cash.view.activity.AddressManageActivity;
import com.giveu.shoppingmall.cash.view.activity.VerifyActivity;
import com.giveu.shoppingmall.index.view.dialog.ChooseCardsDialog;
import com.giveu.shoppingmall.index.widget.MiddleRadioButton;
import com.giveu.shoppingmall.index.widget.StableEditText;
import com.giveu.shoppingmall.me.view.activity.CustomWebViewActivity;
import com.giveu.shoppingmall.me.view.dialog.DealPwdDialog;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.AddressListResponse;
import com.giveu.shoppingmall.model.bean.response.ConfirmOrderScResponse;
import com.giveu.shoppingmall.model.bean.response.CreateOrderResponse;
import com.giveu.shoppingmall.model.bean.response.PayPwdResponse;
import com.giveu.shoppingmall.model.bean.response.SkuInfo;
import com.giveu.shoppingmall.recharge.view.dialog.PaymentTypeDialog;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.Const;
import com.giveu.shoppingmall.utils.ImageUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.widget.DetailView;
import com.giveu.shoppingmall.widget.dialog.ConfirmDialog;
import com.giveu.shoppingmall.widget.dialog.CustomListDialog;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 101900 on 2017/9/1.
 */

public class ConfirmOrderActivity extends BaseActivity {
    //选择付款方式
    @BindView(R.id.confirm_order_pay_type)
    DetailView dvPayView;
    //买家留言
    @BindView(R.id.confirm_order_msg_edit)
    StableEditText msgEditText;
    //优惠券
    @BindView(R.id.confirm_order_coupon)
    DetailView dvCardsView;
    //首付
    @BindView(R.id.confirm_order_first_pay)
    DetailView dvFirstPayView;
    //分期数
    @BindView(R.id.confirm_order_month)
    DetailView dvMonthView;
    //大家电配送
    @BindView(R.id.confirm_order_household)
    LinearLayout llHouseHold;
    @BindView(R.id.confirm_order_send_time)
    TextView tvSendTimeView;
    @BindView(R.id.confirm_order_install_time)
    TextView tvInstallTimeView;
    //添加收货地址
    @BindView(R.id.rl_receiving_address)
    FrameLayout flAddresslayout;
    @BindView(R.id.confirm_order_add_address)
    MiddleRadioButton mAddressBtn;
    @BindView(R.id.confirm_order_address_layout)
    RelativeLayout rvAddressLayout;
    @BindView(R.id.confirm_order_address_name)
    TextView tvAddressName;
    @BindView(R.id.confirm_order_address_phone)
    TextView tvAddressPhone;
    @BindView(R.id.confirm_order_address_content)
    TextView tvAddressContent;
    //sku商品信息
    @BindView(R.id.confirm_order_skuinfo_icon)
    ImageView ivSkuInfoIcon;
    @BindView(R.id.confirm_order_skuinfo_name)
    TextView tvSkuInfoName;
    @BindView(R.id.confirm_order_skuinfo_price)
    TextView tvSkuInfoPrice;
    @BindView(R.id.confirm_order_skuinfo_quantity)
    TextView tvSkuInfoQuantity;
    @BindView(R.id.confirm_order_skuinfo_total_price)
    TextView tvSkuInfoTotalPrice;
    @BindView(R.id.confirm_order_total_price)
    TextView totalPrice;
    @BindView(R.id.confirm_order_agreement)
    TextView tvAgreement;
    @BindView(R.id.confirm_order_agreement_checkbox)
    CheckBox cbAgreement;
    @BindView(R.id.tv_ok)
    TextView tvOK;

    //地址信息
    private CreateOrderResponse.ReceiverJoBean addressJoBean;
    //优惠券信息
    private List<CreateOrderResponse.CardListBean> cardList;
    private ChooseCardsDialog chooseCardsDialog;
    private PaymentTypeDialog paymentTypeDialog;
    private CustomListDialog firstPayDialog;
    private CustomListDialog monthDialog;
    private DealPwdDialog pwdDialog;

    private int payType = 2; //支付方式
    private int cardId = 0; //优惠券Id

    //首付列表,暂时不用
    private List<CharSequence> firstPayList;
    //分期列表,暂时不用
    private List<CharSequence> monthList;
    //送货时间,暂时不用
    private String sendTime;
    //安装时间,暂时不用
    private String installTime;

    private CreateOrderResponse result;

    private String channel;
    private String skuCode;
    private int downPaymentRate;
    private int quantity;
    private String idPerson = LoginHelper.getInstance().getIdPerson();
    private String customerName = null;
    private String customerPhone = null;
    private ConfirmDialog confirmDialog;

    public static void startIt(Context context) {
        Intent intent = new Intent(context, ConfirmOrderActivity.class);
        context.startActivity(intent);
    }

    public static void startIt(Context context, int downPaymentRate, int quantity, String skuCode) {
        Intent intent = new Intent(context, ConfirmOrderActivity.class);
        intent.putExtra("downPaymentRate", downPaymentRate);
        intent.putExtra("quantity", quantity);
        intent.putExtra("skuCode", skuCode);
        context.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_confirm_order);
        baseLayout.setTitle("订单信息确认");
        baseLayout.setBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.show();
            }
        });

        downPaymentRate = getIntent().getIntExtra("downPaymentRate", 0);
        quantity = getIntent().getIntExtra("quantity", 0);
        skuCode = getIntent().getStringExtra("skuCode");
        channel = Const.CHANNEL;
        idPerson = LoginHelper.getInstance().getIdPerson();

        paymentTypeDialog = new PaymentTypeDialog(this);
        paymentTypeDialog.disableWalletPay();
        paymentTypeDialog.disableWechatPay();
        paymentTypeDialog.setOnChoosePayTypeListener(new PaymentTypeDialog.OnChoosePayTypeListener() {
            @Override
            public void onChooseType(int type, String paymentTypeStr) {
                dvPayView.setRightText(paymentTypeStr);
                payType = type;
            }
        });

        msgEditText.setSpannerStableText("买家留言(选填)：", 4, 8, getResources().getColor(R.color.title_color));
        msgEditText.setText("提示信息");

        firstPayDialog = new CustomListDialog(this, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dvFirstPayView.setRightText(firstPayList.get(position).toString());
            }
        });
        firstPayDialog.setData(firstPayList = Arrays.asList(new CharSequence[]{"零首付", "首付5%", "首付15%", "首付25%", "首付35%"}));

        monthDialog = new CustomListDialog(this, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dvMonthView.setRightText(monthList.get(position).toString());
            }
        });
        monthDialog.setData(monthList = Arrays.asList(new CharSequence[]{"3个月", "6个月", "9个月", "12个月", "18个月", "24个月",}));
        pwdDialog = new DealPwdDialog(mBaseContext);

        CommonUtils.setTextWithSpan(tvAgreement, false, "已阅读并同意", "《消费分期合同》", R.color.black, R.color.title_color, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //即有钱包用户注册协议
                CustomWebViewActivity.startIt(mBaseContext, ApiUrl.WebUrl.xFFQLoanStatic, "《消费分期合同》");
            }
        });

        initDialog();
    }

    private void initDialog() {
        confirmDialog = new ConfirmDialog(this);
        confirmDialog.setContent("确定离开吗？好货不等人哦~");
        confirmDialog.setCancleStr("去意已决");
        confirmDialog.setConfirmStr("我再看看");
        confirmDialog.setOnChooseListener(new ConfirmDialog.OnChooseListener() {
            @Override
            public void confirm() {
                confirmDialog.dismiss();
            }

            @Override
            public void cancle() {
                finish();
                confirmDialog.dismiss();
            }
        });
    }

    @Override
    public void setData() {
        ApiImpl.createOrderSc(this, channel, idPerson, downPaymentRate, new SkuInfo(quantity, skuCode), new BaseRequestAgent.ResponseListener<CreateOrderResponse>() {
            @Override
            public void onSuccess(CreateOrderResponse response) {
                updateUI(response);
            }

            @Override
            public void onError(BaseBean errorBean) {
                ToastUtils.showShortToast("创建订单失败");
            }
        });
    }

    private void updateUI(CreateOrderResponse result) {
        //更新地址界面
        updateAddress(result.data.receiverJo);
        //更新商品界面
        updateSku(result);
        //更新优惠券界面
        updateCard(result.data.cardList);

    }

    private void updateCard(List<CreateOrderResponse.CardListBean> lists) {
        if (lists != null && lists.size() > 0) {
            cardList = lists;
            chooseCardsDialog = new ChooseCardsDialog(this, cardList, new ChooseCardsDialog.OnChooseTypeListener() {
                @Override
                public void onChooseType(int id, String name) {
                    dvCardsView.setRightText(name);
                    cardId = id;
                }
            });
        } else {
            dvCardsView.setRightText("没有可用优惠券");
        }
    }

    private void updateSku(CreateOrderResponse result) {
        if (result.data.skuInfo != null) {
            CreateOrderResponse.SkuInfoBean skuInfoBean = result.data.skuInfo;

            if (StringUtils.isNotNull(skuInfoBean.src) && StringUtils.isNotNull(skuInfoBean.srcIp)) {
                String url = skuInfoBean.srcIp + "/" + skuInfoBean.src;
                ImageUtils.loadImage(url, R.drawable.defalut_img_88_88, ivSkuInfoIcon);
            }

            if (StringUtils.isNotNull(skuInfoBean.name)) {
                tvSkuInfoName.setText(skuInfoBean.name);
            }

            if (StringUtils.isNotNull(skuInfoBean.salePrice)) {
                CommonUtils.setTextWithSpanSizeAndColor(tvSkuInfoPrice, "¥ ", StringUtils.format2(skuInfoBean.salePrice), "",
                        15, 13, R.color.black, R.color.black);
            }

            tvSkuInfoQuantity.setText("X  " + skuInfoBean.quantity);

            if (StringUtils.isNotNull(skuInfoBean.totalPrice)) {
                CommonUtils.setTextWithSpanSizeAndColor(tvSkuInfoTotalPrice, "¥ ", StringUtils.format2(skuInfoBean.totalPrice), "",
                        15, 13, R.color.black, R.color.black);
                totalPrice.setText("支付金额：¥" + StringUtils.format2(skuInfoBean.totalPrice));
            }

        }
    }

    private void updateAddress(CreateOrderResponse.ReceiverJoBean bean) {
        if (bean != null) {
            addressJoBean = bean;

            if (customerName == null || customerPhone == null) {
                //第一次初始化的时候保存客户姓名和电话，之后手动修改地址就不改变
                customerName = addressJoBean.custName;
                customerPhone = addressJoBean.phone;
            }

            if (StringUtils.isNotNull(addressJoBean.custName)) {
                tvAddressName.setText(addressJoBean.custName);
            }

            if (StringUtils.isNotNull(addressJoBean.phone)) {
                tvAddressPhone.setText(addressJoBean.phone);
            }

            StringBuilder sb = new StringBuilder();
            if (StringUtils.isNotNull(addressJoBean.province)) {
                sb.append(addressJoBean.province);
            }

            if (StringUtils.isNotNull(addressJoBean.city)) {
                sb.append(addressJoBean.city);
            }

            if (StringUtils.isNotNull(addressJoBean.region)) {
                sb.append(addressJoBean.region);
            }

            if (StringUtils.isNotNull(addressJoBean.street)) {
                sb.append(addressJoBean.street);
            }

            if (StringUtils.isNotNull(addressJoBean.address)) {
                sb.append(addressJoBean.address);
            }

            if (sb.length() > 0) {
                tvAddressContent.setText(sb.toString());
            }

            if (rvAddressLayout.getVisibility() != View.VISIBLE) {
                mAddressBtn.setVisibility(View.GONE);
                rvAddressLayout.setVisibility(View.VISIBLE);
            }
        } else {
            if (mAddressBtn.getVisibility() != View.VISIBLE) {
                mAddressBtn.setVisibility(View.VISIBLE);
                rvAddressLayout.setVisibility(View.GONE);
            }
        }
    }

    @OnClick({R.id.confirm_order_pay_type, R.id.confirm_order_coupon,
            R.id.confirm_order_first_pay, R.id.confirm_order_month,
            R.id.confirm_order_household,
            R.id.tv_ok, R.id.rl_receiving_address})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.confirm_order_pay_type:
                paymentTypeDialog.showDialog(payType);
                break;
            case R.id.confirm_order_coupon:
                if (cardList != null && cardList.size() > 0) {
                    chooseCardsDialog.show(cardId);
                }
                break;
            case R.id.confirm_order_first_pay:
                firstPayDialog.show();
                break;
            case R.id.confirm_order_month:
                monthDialog.show();
                break;
            case R.id.confirm_order_household:
                ConfirmHouseHoldActivity.startItForResult(this, 0, installTime, sendTime);
                break;
            case R.id.rl_receiving_address:
                AddressManageActivity.startItForResult(this, Const.ADDRESSMANAGE);
                break;
            case R.id.tv_ok:
                confirmOrderSc();
                break;
            default:
                break;
        }
    }

    private void confirmOrderSc() {
        ApiImpl.confirmOrderSc(this, channel, cardId, downPaymentRate, idPerson, "0", 0, 0,
                payType, addressJoBean, 0, new SkuInfo(quantity, skuCode), msgEditText.getFinalText(),
                customerPhone, customerName, new BaseRequestAgent.ResponseListener<ConfirmOrderScResponse>() {
                    @Override
                    public void onSuccess(ConfirmOrderScResponse response) {
                        pwdDialog.showDialog();
                    }

                    @Override
                    public void onError(BaseBean errorBean) {
                        ToastUtils.showShortToast("订单确认失败!");
                    }
                });
    }

    @Override
    public void setListener() {
        super.setListener();
        //校验交易密码
        pwdDialog.setOnCheckPwdListener(new DealPwdDialog.OnCheckPwdListener() {
            @Override
            public void checkPwd(String payPwd) {
                ApiImpl.verifyPayPwd(mBaseContext, LoginHelper.getInstance().getIdPerson(), payPwd, new BaseRequestAgent.ResponseListener<PayPwdResponse>() {
                    @Override
                    public void onSuccess(PayPwdResponse response) {
                        //密码校验成功
                        if (response.data.status) {
                            pwdDialog.dissmissDialog();
                            //校验手机验证码
                            VerifyActivity.startItForShopping(mBaseContext, "", false, "");
                        } else {
                            //密码错误提示
                            pwdDialog.showPwdError(response.data.remainTimes);
                        }
                    }

                    @Override
                    public void onError(BaseBean errorBean) {
                        CommonLoadingView.showErrorToast(errorBean);
                    }
                });
            }

        });

        cbAgreement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tvOK.setEnabled(isChecked);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == 100) { //大家电配送
            sendTime = data.getStringExtra("time_send");
            installTime = data.getStringExtra("time_install");
            if (StringUtils.isNotNull(sendTime)) {
                tvSendTimeView.setText("送货：" + sendTime);
            }

            if (StringUtils.isNotNull(installTime)) {
                tvInstallTimeView.setText("安装：" + installTime);
            }
        }

        if (requestCode == Const.ADDRESSMANAGE && resultCode == RESULT_OK) {//地址修改
            if (data.getSerializableExtra("address") != null) {
                AddressListResponse address = (AddressListResponse) data.getSerializableExtra("address");
                updateAddress(converAddressBean(address));
            }
        }
    }

    private CreateOrderResponse.ReceiverJoBean converAddressBean(AddressListResponse response) {
        CreateOrderResponse.ReceiverJoBean receiverJoBean = new CreateOrderResponse.ReceiverJoBean();
        receiverJoBean.custName = response.custName;
        receiverJoBean.address = response.address;
        receiverJoBean.phone = response.phone;
        receiverJoBean.province = response.province;
        receiverJoBean.provinceCode = response.provinceCode;
        receiverJoBean.city = response.city;
        receiverJoBean.cityCode = response.cityCode;
        receiverJoBean.region = response.region;
        receiverJoBean.regionCode = response.regionCode;
        receiverJoBean.street = response.street;
        receiverJoBean.streetCode = response.streetCode;
        return receiverJoBean;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            confirmDialog.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
