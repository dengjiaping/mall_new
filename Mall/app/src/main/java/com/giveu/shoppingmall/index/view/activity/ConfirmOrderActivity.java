package com.giveu.shoppingmall.index.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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
import com.giveu.shoppingmall.event.RefreshEvent;
import com.giveu.shoppingmall.index.view.dialog.ChooseCardsDialog;
import com.giveu.shoppingmall.index.widget.MiddleRadioButton;
import com.giveu.shoppingmall.index.widget.StableEditText;
import com.giveu.shoppingmall.me.relative.OrderState;
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
import com.giveu.shoppingmall.utils.EventBusUtils;
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
    TextView dvPayView;
    //买家留言
    @BindView(R.id.confirm_order_msg_edit)
    StableEditText msgEditText;
    //优惠券
    @BindView(R.id.confirm_order_card_text)
    TextView dvCardsView;
    @BindView(R.id.confirm_order_card_layout)
    RelativeLayout rlCardsViewLayout;
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
    TextView tvTotalPrice;
    @BindView(R.id.confirm_order_agreement)
    TextView tvAgreement;
    @BindView(R.id.confirm_order_agreement_checkbox)
    CheckBox cbAgreement;
    @BindView(R.id.tv_ok)
    TextView tvOK;
    @BindView(R.id.confirm_order_empty)
    RelativeLayout rlEmptyView;
    @BindView(R.id.confirm_order_empty_text)
    TextView tvEmptyTextView;

    //地址信息
    private CreateOrderResponse.ReceiverJoBean addressJoBean;
    //优惠券信息
    private List<CreateOrderResponse.CardListBean> cardList;
    private ChooseCardsDialog chooseCardsDialog;
    private PaymentTypeDialog paymentTypeDialog;
    private CustomListDialog firstPayDialog;
    private CustomListDialog monthDialog;
    private DealPwdDialog pwdDialog;

    private int payType = 2; //支付方式,暂时默认用支付宝
    private int cardId = 0; //优惠券Id
    private String cardPrice = "0";
    private String totalPrice = "0";

    //首付列表,暂时不用
    private List<CharSequence> firstPayList;
    //分期列表,暂时不用
    private List<CharSequence> monthList;
    //送货时间,暂时不用
    private String sendTime;
    //安装时间,暂时不用
    private String installTime;

    private String channel;
    private String skuCode;
    private int downPaymentRate;
    private int quantity;
    private String idPerson = LoginHelper.getInstance().getIdPerson();
    private String customerName = null;
    private String customerPhone = null;
    private ConfirmDialog confirmDialog;
    private String orderNo;
    private String paymentNum;
    private boolean canPay = true;

    //订单是否出入确认状态，如果是则不可修改
    private boolean isConfirm = false;
    //界面初始化成功的标志
    private boolean isInitSuccess = false;

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
                if (isInitSuccess) {
                    //界面初始化失败，返回不弹出确认框
                    confirmDialog.show();
                } else {
                    finish();
                }
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
                dvPayView.setText(paymentTypeStr);
                payType = type;
            }
        });

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

        CommonUtils.setTextWithSpan(tvAgreement, false, "已阅读并同意", "消费分期合同", R.color.black, R.color.title_color, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //即有钱包用户注册协议
                CustomWebViewActivity.startIt(mBaseContext, ApiUrl.WebUrl.xFFQLoanStatic, "《消费分期合同》");
            }
        });

        initBackNoticeDialog();

        initMsgEditText();

        showLoading();
    }

    private void initMsgEditText() {
        //第一次进入界面EditText未获取焦点,设置显示内容
        SpannableString desc = new SpannableString("买家留言（选填）：对本次交易的说明");
        desc.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_4a4a4a)), 0, 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        desc.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.title_color)), 4, 8, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        desc.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_4a4a4a)), 8, 9, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        desc.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_cccccc)), 9, desc.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        msgEditText.setText(desc);
        //第一次点击EditText获取焦点，重置显示内容
        msgEditText.setOnFirstFocusedListener(new StableEditText.OnFirstFocusedListener() {
            @Override
            public void firstFocused() {
                msgEditText.setSpannerStableText("买家留言（选填）：", 4, 8, getResources().getColor(R.color.title_color));
            }
        });
    }

    private void initBackNoticeDialog() {
        confirmDialog = new ConfirmDialog(this);
        confirmDialog.setContent(StringUtils.getSizeAndColorSpannable("确定离开吗？好货不等人哦~", R.color.color_767876, 14));
        confirmDialog.setCancleStr(StringUtils.getSizeAndColorSpannable("去意已决", R.color.color_9b9b9b, 14));
        confirmDialog.setConfirmStr(StringUtils.getSizeAndColorSpannable("我再看看", R.color.title_color, 14));
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
                if (response.data != null) {
                    if (rlEmptyView.getVisibility() != View.GONE) {
                        rlEmptyView.setVisibility(View.GONE);
                    }
                    updateUI(response);
                    isInitSuccess = true;
                }
                hideLoding();
            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
                if (rlEmptyView.getVisibility() != View.VISIBLE) {
                    rlEmptyView.setVisibility(View.VISIBLE);
                }
                if (tvEmptyTextView.getVisibility() != View.VISIBLE) {
                    tvEmptyTextView.setVisibility(View.VISIBLE);
                }
                hideLoding();
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

            //默认选择优惠额度最大的优惠券
            double maxCardPrice = 0;
            String maxCardName = "";
            for (CreateOrderResponse.CardListBean cardBean : lists) {
                double curCardPrice = StringUtils.string2Double(cardBean.price);
                if (curCardPrice > maxCardPrice) {
                    maxCardPrice = curCardPrice;
                    cardId = cardBean.id;
                    maxCardName = cardBean.name;
                }
            }
            //更新优惠券显示
            dvCardsView.setText(maxCardName);
            //更新实际付款金额
            cardPrice = StringUtils.format2(maxCardPrice + "");
            setTotalPrice();
            chooseCardsDialog = new ChooseCardsDialog(this, cardList, new ChooseCardsDialog.OnChooseTypeListener() {
                @Override
                public void onChooseType(int id, String price, String name) {
                    dvCardsView.setText(name);
                    cardPrice = price;
                    cardId = id;
                    setTotalPrice();
                }
            });
            if (rlCardsViewLayout.getVisibility() != View.VISIBLE) {
                rlCardsViewLayout.setVisibility(View.VISIBLE);
            }
        } else {
            if (rlCardsViewLayout.getVisibility() != View.GONE) {
                rlCardsViewLayout.setVisibility(View.GONE);
            }
        }
    }

    private void updateSku(CreateOrderResponse result) {
        if (result.data.skuInfo != null) {
            CreateOrderResponse.SkuInfoBean skuInfoBean = result.data.skuInfo;

            if (StringUtils.isNotNull(skuInfoBean.src) && StringUtils.isNotNull(skuInfoBean.srcIp)) {
                String url = skuInfoBean.srcIp + "/" + skuInfoBean.src;
                ImageUtils.loadImage(url, R.drawable.default_img_240_240, ivSkuInfoIcon);
            }

            if (StringUtils.isNotNull(skuInfoBean.name)) {
                tvSkuInfoName.setText(StringUtils.ToAllFullWidthString(skuInfoBean.name));
            }

            if (StringUtils.isNotNull(skuInfoBean.salePrice)) {
                CommonUtils.setTextWithSpanSizeAndColor(tvSkuInfoPrice, "¥ ", StringUtils.format2(skuInfoBean.salePrice), "",
                        19, 13, R.color.black, R.color.black);
            }

            tvSkuInfoQuantity.setText("X  " + skuInfoBean.quantity);

            if (StringUtils.isNotNull(skuInfoBean.totalPrice)) {
                CommonUtils.setTextWithSpanSizeAndColor(tvSkuInfoTotalPrice, "¥ ", StringUtils.format2(skuInfoBean.totalPrice), "",
                        19, 13, R.color.red, R.color.black);

                totalPrice = skuInfoBean.totalPrice;
                setTotalPrice();
            }

        }
    }

    private void setTotalPrice() {
        double tPrice = StringUtils.string2Double(totalPrice);
        double cPrice = StringUtils.string2Double(cardPrice);
        double result = Math.max(tPrice - cPrice, 0);
        CommonUtils.setTextWithSpanSizeAndColor(tvTotalPrice, "¥ ", StringUtils.format2(result + ""), "",
                16, 11, R.color.title_color, R.color.black);
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

    @OnClick({R.id.confirm_order_pay_type, R.id.confirm_order_card_text,
            R.id.confirm_order_first_pay, R.id.confirm_order_month,
            R.id.confirm_order_household,
            R.id.tv_ok, R.id.rl_receiving_address, R.id.confirm_order_empty})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.confirm_order_pay_type:
                paymentTypeDialog.showDialog(payType);
                break;
            case R.id.confirm_order_card_text:
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
                if (isConfirm) {
                    pwdDialog.showDialog();
                } else {
                    //在下单时canPay置为false，当服务器未返回结果时，不会重复创建订单，返回错误时，可再次下单
                    if (canPay) {
                        canPay = false;
                        confirmOrderSc();
                    }
                }
                break;
            case R.id.confirm_order_empty:
                setData();
                break;
            default:
                break;
        }
    }

    private void confirmOrderSc() {

        if (addressJoBean == null) {
            ToastUtils.showShortToast("请添加收货地址");
            return;
        }

        ApiImpl.confirmOrderSc(this, channel, cardId, downPaymentRate, idPerson, "0", 0, 0,
                payType, addressJoBean, 0, new SkuInfo(quantity, skuCode), msgEditText.getFinalText(),
                customerPhone, customerName, new BaseRequestAgent.ResponseListener<ConfirmOrderScResponse>() {
                    @Override
                    public void onSuccess(ConfirmOrderScResponse response) {
                        //刷新订单列表的所有和待付款状态
                        EventBusUtils.poseEvent(new RefreshEvent(OrderState.ALLRESPONSE));
                        EventBusUtils.poseEvent(new RefreshEvent(OrderState.WAITINGPAY));
                        pwdDialog.setPrice(response.data.payMoney);
                        pwdDialog.showDialog();
                        orderNo = response.data.orderNo;
                        paymentNum = response.data.payMoney;
                        isConfirm = true;
                        //订单确认后以下功能不可修改
                        flAddresslayout.setEnabled(false);
                        msgEditText.setEnabled(false);
                        dvPayView.setEnabled(false);
                        dvCardsView.setEnabled(false);
                    }

                    @Override
                    public void onError(BaseBean errorBean) {
                        canPay = true;
                        CommonLoadingView.showErrorToast(errorBean);
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
                            //校验手机验证码,payType为钱包时传入true,否则传入false
                            VerifyActivity.startItForShopping(mBaseContext, orderNo, payType == 0, paymentNum);
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
            if (isInitSuccess) {
                //界面初始化失败，返回不弹出确认框
                confirmDialog.show();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
