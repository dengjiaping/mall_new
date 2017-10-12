package com.giveu.shoppingmall.index.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.cash.view.activity.AddressManageActivity;
import com.giveu.shoppingmall.cash.view.activity.VerifyActivity;
import com.giveu.shoppingmall.event.RefreshEvent;
import com.giveu.shoppingmall.index.view.dialog.AnnuityDialog;
import com.giveu.shoppingmall.index.view.dialog.ChooseCardsDialog;
import com.giveu.shoppingmall.index.view.dialog.ChooseDialog;
import com.giveu.shoppingmall.index.view.dialog.QuotaNoticeDialog;
import com.giveu.shoppingmall.index.widget.MiddleRadioButton;
import com.giveu.shoppingmall.index.widget.StableEditText;
import com.giveu.shoppingmall.me.relative.OrderState;
import com.giveu.shoppingmall.me.view.activity.CustomWebViewActivity;
import com.giveu.shoppingmall.me.view.dialog.DealPwdDialog;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.AddressListResponse;
import com.giveu.shoppingmall.model.bean.response.ConfirmOrderScResponse;
import com.giveu.shoppingmall.model.bean.response.CreateOrderResponse;
import com.giveu.shoppingmall.model.bean.response.DownPayMonthPayResponse;
import com.giveu.shoppingmall.model.bean.response.InsuranceFee;
import com.giveu.shoppingmall.model.bean.response.MonthSupplyResponse;
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
import com.giveu.shoppingmall.widget.NoScrollListView;
import com.giveu.shoppingmall.widget.dialog.ConfirmDialog;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 101900 on 2017/9/1.
 */

public class ConfirmOrderActivity extends BaseActivity {
    //选择付款方式
    @BindView(R.id.confirm_order_pay_type)
    TextView tvPayTypeView;
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
    TextView tvPaymentRateView;
    @BindView(R.id.confirm_order_support_installment)
    LinearLayout paymentLayout;
    //分期数
    @BindView(R.id.confirm_order_month)
    TextView tvPaymentView;
    @BindView(R.id.confirm_order_annuity)
    TextView tvAnnuityView;
    //大家电配送
    @BindView(R.id.confirm_order_support_household)
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
    @BindView(R.id.tv_ok)
    TextView tvOK;
    @BindView(R.id.confirm_order_empty)
    RelativeLayout rlEmptyView;
    @BindView(R.id.confirm_order_empty_text)
    TextView tvEmptyTextView;
    //增值服务
    @BindView(R.id.confirm_order_increment_service)
    LinearLayout llIncrementService;
    @BindView(R.id.confirm_order_increment_service_list)
    NoScrollListView inCrementListView;
    //消费者分期合同
    @BindView(R.id.confirm_order_agreement_layout)
    LinearLayout llAgreementLayout;
    @BindView(R.id.confirm_order_agreement)
    TextView tvAgreement;
    @BindView(R.id.confirm_order_agreement_checkbox)
    CheckBox cbAgreement;
    //顶层布局，用于Disable一些控件,防止订单确认后仍可点击
    @BindView(R.id.confirm_order_outer_layout)
    LinearLayout outerLayout;

    //地址信息
    private CreateOrderResponse.ReceiverJoBean addressJoBean;
    //优惠券信息
    private List<CreateOrderResponse.CardListBean> cardList;
    private ChooseCardsDialog chooseCardsDialog;    //优惠券对话框
    private PaymentTypeDialog paymentTypeDialog; //支付方式选择对话框
    private DealPwdDialog pwdDialog; //支付密码输入对话框
    private ConfirmDialog backConfirmDialog; //退出确认对话框
    private QuotaNoticeDialog quotaNoticeDialog; //额度

    //首付列表
    private List<CreateOrderResponse.InitListBean> paymentRateList = null;
    //首付选择对话框
    private ChooseDialog<CreateOrderResponse.InitListBean> paymentRateDlg = null;
    //分期列表,暂时不用
    private List<DownPayMonthPayResponse> paymentList = null;
    //分期选择对话框
    private ChooseDialog<DownPayMonthPayResponse> paymentDlg = null;
    //月供对话框
    private AnnuityDialog annuityDialog = null;
    //送货时间
    private List<CreateOrderResponse.ReservingListBean> sendTimesList;
    //安装时间
    private List<CreateOrderResponse.DateListBean> installTimesList;
    //增值服务
    private List<CreateOrderResponse.AvsListBean> incrementServiceList = null;
    private LvCommonAdapter incrementServiceAdapter = null;
    private int insuranceFee = 1; //是否购买人身意外险,默认购买
    private long serviceId = 0; //人身意外险Id

    private int payType = 0; //支付方式Id
    private long cardId = 0; //优惠券Id
    private int downPaymentRate = 0;//首付Id
    private String cardPrice = "0"; //优惠券金额
    private String totalPrice = "0"; //总支付金额
    private String annuityPrice = "0";//月供金额
    private String paymentPrice = "0";//首付金额
    private int paymentNum = 0;//分期期数
    private long idProduct; //分期Id
    private long sendTimesId = -1;//配送家电Id
    private long installTimesId = -1; //安装家电Id

    private String channel; //用户身份
    private String skuCode; //商品SkuCode
    private int quantity; //商品数量
    private String idPerson = LoginHelper.getInstance().getIdPerson();
    private String customerName = null; //客户姓名
    private String customerPhone = null; //客户电话
    private String orderNo;
    private String paymentMoney; //最终支付金额

    private boolean isConfirm = false;//订单是否处于确认状态，如果是则不可修改
    private boolean isInitSuccess = false;//界面初始化成功的标志
    private boolean canPay = true;//是否可以下单支付

    public static void startIt(Context context) {
        Intent intent = new Intent(context, ConfirmOrderActivity.class);
        context.startActivity(intent);
    }

    public static void startIt(Context context, int downPaymentRate, long idProduct, int quantity, String skuCode, int paymentType) {
        Intent intent = new Intent(context, ConfirmOrderActivity.class);
        intent.putExtra("downPaymentRate", downPaymentRate);
        intent.putExtra("idProduct", idProduct);
        intent.putExtra("quantity", quantity);
        intent.putExtra("paymentType", paymentType);
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
                    backConfirmDialog.show();
                } else {
                    finish();
                }
            }
        });

        downPaymentRate = getIntent().getIntExtra("downPaymentRate", 0);
        idProduct = getIntent().getLongExtra("idProduct", 0);
        quantity = getIntent().getIntExtra("quantity", 0);
        skuCode = getIntent().getStringExtra("skuCode");
        payType = getIntent().getIntExtra("paymentType", 0);
        channel = Const.CHANNEL;
        idPerson = LoginHelper.getInstance().getIdPerson();

        initPayTypeUI();

        pwdDialog = new DealPwdDialog(mBaseContext);

        CommonUtils.setTextWithSpan(tvAgreement, false, "已阅读并同意", "消费分期合同", R.color.black, R.color.title_color, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //即有钱包用户注册协议
                CustomWebViewActivity.startIt(mBaseContext, ApiUrl.WebUrl.xFFQLoanStatic, "《消费分期合同》");
            }
        });

        //初始化退出提示对话框
        initBackNoticeDialog();
        //初始化买家留言编辑框
        initMsgEditText();
        //初始化额度不足提示对话框
        initQuotaDialog();

    }

    private void initQuotaDialog() {
        quotaNoticeDialog = new QuotaNoticeDialog(this);
        quotaNoticeDialog.setTitleText("您的钱包余额不足");
        quotaNoticeDialog.setConfirmText("我知道了");
        String price = LoginHelper.getInstance().getAvailablePoslimit();
        quotaNoticeDialog.setContentText(formatQuotaNotice(price));
    }

    private CharSequence formatQuotaNotice(String price) {
        price = StringUtils.format2(price);
        String text = "您的钱包可用消费余额¥" + price + "\n" + "可调整首付比例，支付本次订单";
        SpannableString spanText = new SpannableString(text);
        int pos1 = text.indexOf("¥");
        int pos2 = text.indexOf(".");
        spanText.setSpan(new AbsoluteSizeSpan(18, true), pos1 + 1, pos2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanText.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.title_color)), pos1, pos2 + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanText;
    }


    /**
     * 初始化支付方式相关UI
     */
    private void initPayTypeUI() {
        paymentTypeDialog = new PaymentTypeDialog(this);
        paymentTypeDialog.setOnChoosePayTypeListener(new PaymentTypeDialog.OnChoosePayTypeListener() {
            @Override
            public void onChooseType(int type, String paymentTypeStr) {
                tvPayTypeView.setText(paymentTypeStr);
                payType = type;
                onChangePayType();
            }
        });

        String availableRecharge = LoginHelper.getInstance().getAvailablePoslimit();
        if ("0.00".equals(StringUtils.format2(availableRecharge))) {
            //如果额度为0,隐藏钱包支付,默认选择支付宝支付
            paymentTypeDialog.disableWalletPay();
            payType = 2;
        }

        if (payType == 0) {
            tvPayTypeView.setText("即有钱包");
        } else if (payType == 2) {
            tvPayTypeView.setText("支付宝");
        }

        onChangePayType();
    }

    /**
     * 初始化买家留言编辑框
     */
    private void initMsgEditText() {
        //第一次进入界面EditText未获取焦点,设置显示内容
        final String stableText = "买家留言（选填）：";
        String hintText = "对本次交易的说明";
        SpannableString desc = new SpannableString(stableText + hintText);
        desc.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_4a4a4a)), 0, 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        desc.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.title_color)), 4, 8, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        desc.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_4a4a4a)), 8, 9, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        desc.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_cccccc)), 9, desc.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        msgEditText.setText(desc);
        //限制输入字符长度为100
        msgEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100 + stableText.length())});
        //第一次点击EditText获取焦点，重置显示内容
        msgEditText.setOnFirstFocusedListener(new StableEditText.OnFirstFocusedListener() {
            @Override
            public void firstFocused() {
                msgEditText.setSpannerStableText(stableText, 4, 8, getResources().getColor(R.color.title_color));
            }
        });
    }

    /**
     * 初始化退出提示对话框
     */
    private void initBackNoticeDialog() {
        backConfirmDialog = new ConfirmDialog(this);
        backConfirmDialog.setContent(StringUtils.getSizeAndColorSpannable("确定离开吗？好货不等人哦~", R.color.color_767876, 14));
        backConfirmDialog.setCancleStr(StringUtils.getSizeAndColorSpannable("去意已决", R.color.color_9b9b9b, 14));
        backConfirmDialog.setConfirmStr(StringUtils.getSizeAndColorSpannable("我再看看", R.color.title_color, 14));
        backConfirmDialog.setOnChooseListener(new ConfirmDialog.OnChooseListener() {
            @Override
            public void confirm() {
                backConfirmDialog.dismiss();
            }

            @Override
            public void cancel() {
                finish();
                backConfirmDialog.dismiss();
            }
        });
    }

    @Override
    public void setData() {
        ApiImpl.getAppDownPayAndMonthPay(this, channel, idPerson, downPaymentRate, skuCode, quantity, new BaseRequestAgent.ResponseListener<DownPayMonthPayResponse>() {
            @Override
            public void onSuccess(DownPayMonthPayResponse response) {
                if (CommonUtils.isNotNullOrEmpty(response.data)) {
                    paymentList = response.data;
                }
                createOrderSc();
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
            }
        });
    }

    /**
     * 更新分期数据
     */
    private void updatePaymentList() {
        ApiImpl.getAppDownPayAndMonthPay(this, channel, idPerson, downPaymentRate, skuCode, quantity, new BaseRequestAgent.ResponseListener<DownPayMonthPayResponse>() {
            @Override
            public void onSuccess(DownPayMonthPayResponse response) {
                if (CommonUtils.isNotNullOrEmpty(response.data)) {
                    paymentList.clear();
                    paymentList.addAll(response.data);
                    updatePaymentListUI();
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }

    private void updateUI(CreateOrderResponse result) {
        //更新地址界面
        updateAddressUI(result.data.receiverJo);
        //更新商品界面
        updateSkuUI(result);
        //更新优惠券界面
        updateCardUI(result.data.cardList);
        //更新首付比例界面
        updatePaymentRateUI(result.data.initList);
        //更新分期
        updatePaymentListUI();
        //更新增值服务
        updateIncrementServiceUI(result.data.avsList);
        //更新月供金额
//        updateAnnuity();
        /* 大家电配送功能暂时不开发
        //更新配送时间
        if (result.data.reserving) {
            updateReservingUI(result.data.reservingList);
        }
        //更新安装时间
        if (result.data.install) {
            updateInstallUI(result.data.installList);
        }
        */
    }

    /**
     * 更新配送时间UI
     *
     * @param list 配送时间列表
     */
    private void updateReservingUI(List<CreateOrderResponse.ReservingListBean> list) {
        if (CommonUtils.isNotNullOrEmpty(list)) {
            llHouseHold.setVisibility(View.VISIBLE);
            tvSendTimeView.setVisibility(View.VISIBLE);
            sendTimesList = list;
            sendTimesId = list.get(0).id;
            tvSendTimeView.setText("送货:" + list.get(0).date + "[" + list.get(0).week + "]");
        }
    }

    /**
     * 更新安装时间UI
     *
     * @param list 安装时间列表
     */
    private void updateInstallUI(List<CreateOrderResponse.DateListBean> list) {
        if (CommonUtils.isNotNullOrEmpty(list)) {
            llHouseHold.setVisibility(View.VISIBLE);
            tvInstallTimeView.setVisibility(View.VISIBLE);
            installTimesList = list;
            List<CreateOrderResponse.ReservingListBean> dateList = list.get(0).dateList;
            if (CommonUtils.isNotNullOrEmpty(dateList)) {
                installTimesId = dateList.get(0).id;
                tvInstallTimeView.setText("安装:" + dateList.get(0).date + "[" + dateList.get(0).week + "]");
            }
        }
    }

    /**
     * 更新增值服务相关UI
     *
     * @param avsList 增值服务列表
     */
    private void updateIncrementServiceUI(List<CreateOrderResponse.AvsListBean> avsList) {
        if (CommonUtils.isNotNullOrEmpty(avsList)) {
            incrementServiceList = avsList;
            if (llIncrementService.getVisibility() != View.VISIBLE && payType == 0) {
                llIncrementService.setVisibility(View.VISIBLE);
            }

            if (incrementServiceAdapter == null) {
                incrementServiceAdapter = new LvCommonAdapter<CreateOrderResponse.AvsListBean>(this, R.layout.adapter_increment_item, incrementServiceList) {
                    @Override
                    protected void convert(ViewHolder viewHolder, final CreateOrderResponse.AvsListBean item, final int position) {
                        viewHolder.setText(R.id.increment_name, item.serviceName)
                                .setText(R.id.increment_price, item.servicePrice)
                                .setOnClickListener(R.id.increment_url, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        CustomWebViewActivity.startIt(mBaseContext, item.serviceUrl, item.serviceName);
                                    }
                                })
                                .setOnCheckChangedListener(R.id.increment_name, new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        if (position == 0) { //目前只有人身意外险
                                            insuranceFee = isChecked ? 1 : 0;
                                            serviceId = item.serviceId;
                                            updateAnnuity();
                                        }
                                    }
                                });
                        if (position == 0) {
                            serviceId = item.serviceId;
                        }
                        TextView priceViw = viewHolder.getView(R.id.increment_price);
                        CommonUtils.setTextWithSpanSizeAndColor(priceViw, "¥", StringUtils.format2(item.servicePrice), "/月",
                                14, 11, R.color.title_color, R.color.title_color);
                    }
                };
                inCrementListView.setAdapter(incrementServiceAdapter);
            }
        }
    }

    /**
     * 更新首付相关UI
     *
     * @param list 首付列表
     */
    private void updatePaymentRateUI(final List<CreateOrderResponse.InitListBean> list) {
        if (list != null && list.size() > 0) {
            paymentRateList = list;
            if (paymentLayout.getVisibility() != View.VISIBLE && payType == 0) {
                paymentLayout.setVisibility(View.VISIBLE);
            }
            CharSequence text = "请选择";
            for (CreateOrderResponse.InitListBean bean : list) {
                if (bean.id == downPaymentRate) {
                    text = formatPaymentRateText(bean);
                    setTotalPrice(paymentPrice);
                    break;
                }
            }
            tvPaymentRateView.setText(text);
            paymentRateDlg = new ChooseDialog<CreateOrderResponse.InitListBean>(this, paymentRateList) {
                @Override
                public void convertView(ViewHolder holder, final CreateOrderResponse.InitListBean item, int position, long checkIndex) {
                    holder.setText(R.id.dialog_choose_item_text, item.name);
                    holder.setChecked(R.id.dialog_choose_item_text, item.id == checkIndex);
                    holder.setOnClickListener(R.id.dialog_choose_item_text, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CharSequence text = formatPaymentRateText(item);
                            tvPaymentRateView.setText(text);
                            setTotalPrice(paymentPrice);
                            downPaymentRate = item.id;
                            paymentRateDlg.dismiss();
                            updatePaymentList();
                        }
                    });
                }
            };
        }
    }

    // 检查首付金额是否在额度范围内
    private boolean checkPaymentRate(String price) {
        String availableRecharge = LoginHelper.getInstance().getAvailablePoslimit();
        double available = StringUtils.string2Double(availableRecharge);
        double paymentRatePrice = StringUtils.string2Double(price);
        if (available < paymentRatePrice && quotaNoticeDialog != null) {
            quotaNoticeDialog.show();
            return false;
        }
        return true;
    }

    private CharSequence formatPaymentRateText(CreateOrderResponse.InitListBean bean) {
        String text = bean.name;
        SpannableString spanText = null;
        paymentPrice = bean.price;
        if (bean.id != 0) {
            text = text + "（¥" + StringUtils.format2(bean.price) + "）";
            spanText = new SpannableString(text);
            int pos1 = text.indexOf("¥");
            int pos2 = text.indexOf(".");
            int pos3 = text.indexOf("）");
            spanText.setSpan(new AbsoluteSizeSpan(11, true), pos1, pos1 + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanText.setSpan(new AbsoluteSizeSpan(11, true), pos2, pos3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spanText;
        }
        return text;
    }

    /**
     * 更新分期相关UI
     */
    private void updatePaymentListUI() {
        if (CommonUtils.isNotNullOrEmpty(paymentList)) {
            String text = "请选择";
            boolean isFind = false;
            for (DownPayMonthPayResponse bean : paymentList) {
                if (StringUtils.string2Long(bean.idProduct) == idProduct) {
                    paymentNum = bean.paymentNum;
                    text = paymentNum + "个月";
                    isFind = true;
                }
            }

            if (!isFind) {
                idProduct = StringUtils.string2Long(paymentList.get(0).idProduct);
                paymentNum = paymentList.get(0).paymentNum;
                text = paymentNum + "个月";
            }

            tvPaymentView.setText(text);
            if (paymentDlg == null) {
                paymentDlg = new ChooseDialog<DownPayMonthPayResponse>(this, paymentList) {
                    @Override
                    public void convertView(ViewHolder holder, final DownPayMonthPayResponse item, int position, long checkIndex) {
                        holder.setText(R.id.dialog_choose_item_text, item.paymentNum + "个月");
                        holder.setChecked(R.id.dialog_choose_item_text, StringUtils.string2Long(item.idProduct) == checkIndex);
                        holder.setOnClickListener(R.id.dialog_choose_item_text, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                paymentDlg.dismiss();
                                tvPaymentView.setText(item.paymentNum + "个月");
                                idProduct = StringUtils.string2Long(item.idProduct);
                                paymentNum = item.paymentNum;
                                updateAnnuity();
                            }
                        });
                    }
                };
            } else {
                paymentDlg.refreshData();
            }
            updateAnnuity();
        }
    }

    /**
     * 更新月供数据
     */
    private void updateAnnuity() {
        ApiImpl.getAppMonthlySupply(this, channel, idPerson, downPaymentRate, idProduct, insuranceFee, quantity, skuCode, new BaseRequestAgent.ResponseListener<MonthSupplyResponse>() {
            @Override
            public void onSuccess(MonthSupplyResponse response) {
                updateAnnuityUI(response);
            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }

    /**
     * 更新月供相关UI
     *
     * @param response 月供数据
     */
    private void updateAnnuityUI(MonthSupplyResponse response) {
        if (CommonUtils.isNotNullOrEmpty(response.data.paymentList)) {
            annuityPrice = response.data.paymentList.get(0).monthPay;
            CommonUtils.setTextWithSpanSizeAndColor(tvAnnuityView, "¥", StringUtils.format2(annuityPrice), "",
                    15, 11, R.color.title_color, R.color.title_color);

            if (annuityDialog == null) {
                annuityDialog = new AnnuityDialog(mBaseContext);
            }
            annuityDialog.refreshData(response.data, true);
        }
    }

    /**
     * 更新优惠券相关UI
     *
     * @param lists 优惠券列表
     */
    private void updateCardUI(List<CreateOrderResponse.CardListBean> lists) {

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
                public void onChooseType(long id, String price, String name) {
                    dvCardsView.setText(name);
                    cardPrice = price;
                    cardId = id;
                    setTotalPrice();
                }
            });
            if (rlCardsViewLayout.getVisibility() != View.VISIBLE && payType != 0) {
                //钱包支付暂不支持优惠券
                rlCardsViewLayout.setVisibility(View.VISIBLE);
            }
        } else {
            if (rlCardsViewLayout.getVisibility() != View.GONE) {
                rlCardsViewLayout.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 更新商品相关UI
     *
     * @param result 商品数据
     */
    private void updateSkuUI(CreateOrderResponse result) {
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

            tvSkuInfoQuantity.setText("× " + skuInfoBean.quantity);

            if (StringUtils.isNotNull(skuInfoBean.totalPrice)) {
                CommonUtils.setTextWithSpanSizeAndColor(tvSkuInfoTotalPrice, "¥ ", StringUtils.format2(skuInfoBean.totalPrice), "",
                        19, 13, R.color.red, R.color.black);

                totalPrice = skuInfoBean.totalPrice;
                setTotalPrice();
            }

        }
    }

    private void setTotalPrice(String price) {
        double tPrice = StringUtils.string2Double(totalPrice);
        double cPrice = StringUtils.string2Double(price);
        double result = Math.max(tPrice - cPrice, 0);
        CommonUtils.setTextWithSpanSizeAndColor(tvTotalPrice, "¥ ", StringUtils.format2(result + ""), "",
                16, 11, R.color.title_color, R.color.black);
    }


    private void setTotalPrice() {
        double tPrice = StringUtils.string2Double(totalPrice);
        double cPrice = StringUtils.string2Double(cardPrice);
        double result = Math.max(tPrice - cPrice, 0);
        CommonUtils.setTextWithSpanSizeAndColor(tvTotalPrice, "¥ ", StringUtils.format2(result + ""), "",
                16, 11, R.color.title_color, R.color.black);
    }

    private void updateAddressUI(CreateOrderResponse.ReceiverJoBean bean) {
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

    /**
     * payType为0时为钱包支付，此时应显示分期信息、增值服务和消费者分期合同,不显示优惠券；
     */
    private void onChangePayType() {
        if (payType == 0) {
            if (CommonUtils.isNotNullOrEmpty(paymentRateList)) {
                llIncrementService.setVisibility(View.VISIBLE);
                paymentLayout.setVisibility(View.VISIBLE);
            }
            llAgreementLayout.setVisibility(View.VISIBLE);
            rlCardsViewLayout.setVisibility(View.GONE);
            setTotalPrice(paymentPrice);
        } else {
            llIncrementService.setVisibility(View.GONE);
            paymentLayout.setVisibility(View.GONE);
            llAgreementLayout.setVisibility(View.INVISIBLE);
            if (CommonUtils.isNotNullOrEmpty(cardList)) {
                rlCardsViewLayout.setVisibility(View.VISIBLE);
            }
            setTotalPrice();
        }
    }

    @OnClick({R.id.confirm_order_pay_type, R.id.confirm_order_card_text,
            R.id.confirm_order_first_pay, R.id.confirm_order_month,
            R.id.confirm_order_support_household, R.id.confirm_order_annuity,
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
                if (paymentRateList != null && paymentRateList.size() > 0) {
                    paymentRateDlg.show(downPaymentRate);
                }
                break;
            case R.id.confirm_order_month:
                if (paymentList != null && paymentList.size() > 0) {
                    paymentDlg.show(idProduct);
                }
                break;
            case R.id.confirm_order_support_household:
                ConfirmHouseHoldActivity.startItForResult(this, 0, sendTimesList, installTimesList, sendTimesId, installTimesId);
                break;
            case R.id.rl_receiving_address:
                AddressManageActivity.startItForResult(this, Const.ADDRESSMANAGE);
                break;
            case R.id.tv_ok:
                if (isConfirm) {
                    if (LoginHelper.getInstance().hasSetPwd()) {
                        //设置了交易密码)
                        pwdDialog.showDialog();
                    } else {
                        TransactionPwdActivity.startIt(mBaseContext, LoginHelper.getInstance().getIdPerson());
                    }
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
            case R.id.confirm_order_annuity:
                if (annuityDialog != null) {
                    annuityDialog.show();
                }
            default:
                break;
        }
    }

    //创建订单
    private void createOrderSc() {
        ApiImpl.createOrderSc(this, channel, idPerson, downPaymentRate, idProduct, new SkuInfo(quantity, skuCode), new BaseRequestAgent.ResponseListener<CreateOrderResponse>() {
            @Override
            public void onSuccess(CreateOrderResponse response) {
                if (response.data != null) {
                    if (rlEmptyView.getVisibility() != View.GONE) {
                        rlEmptyView.setVisibility(View.GONE);
                    }
                    updateUI(response);
                    isInitSuccess = true;
                }
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
            }

        });
    }

    //订单确认
    private void confirmOrderSc() {

        if (addressJoBean == null) {
            canPay = true;
            ToastUtils.showShortToast("请添加收货地址");
            return;
        }

        if (StringUtils.isNotNull(addressJoBean.phone)) {
            //清除收货人电话的空格
            addressJoBean.phone = addressJoBean.phone.trim().replaceAll(" ", "");
            if (addressJoBean.phone.length() != 11) {
                canPay = true;
                ToastUtils.showShortToast("收货人联系电话格式不正确");
                return;
            }
        } else {
            canPay = true;
            ToastUtils.showShortToast("收货人联系电话格式不正确");
            return;
        }

        String message = msgEditText.getFinalText();
        if (StringUtils.isNotNull(message) && StringUtils.contailEmoji(message)) {
            ToastUtils.showShortToast("买家留言暂不支持表情或特殊符号");
            canPay = true;
            return;
        }

        if(payType == 0 && !cbAgreement.isChecked()){
            ToastUtils.showShortToast("请阅读并同意消费分期合同");
            canPay = true;
            return;
        }

        //如果是分期产品,检查额度支付是否大于总价格与首付的差
        double price = StringUtils.string2Double(totalPrice) - StringUtils.string2Double(paymentPrice);
        price = Math.max(price, 0);
        if (payType == 0 && CommonUtils.isNotNullOrEmpty(paymentRateList)
                && !checkPaymentRate(StringUtils.format2(price + ""))) {
            canPay = true;
            return;
        }

        //切换支付方式时，下列变量会随之改变,为了避免下单失败后重试数据异常，所以用一些临时变量代替
        long realCardId = cardId;
        int realDownPaymentRate = downPaymentRate;
        long realIdProduct = idProduct;
        InsuranceFee feeBean = new InsuranceFee(insuranceFee, serviceId);

        if (payType == 0) { //钱包支付不支持优惠券
            realCardId = 0;
        } else {//其它支付不支持分期和增值服务
            realDownPaymentRate = 0;
            realIdProduct = 0;
            feeBean = null;
        }

        ApiImpl.confirmOrderSc(this, channel, realCardId, realDownPaymentRate, idPerson, realIdProduct, installTimesId, feeBean,
                payType, addressJoBean, sendTimesId, new SkuInfo(quantity, skuCode), message,
                customerPhone, customerName, new BaseRequestAgent.ResponseListener<ConfirmOrderScResponse>() {
                    @Override
                    public void onSuccess(ConfirmOrderScResponse response) {
                        //刷新订单列表的所有和待付款状态
                        EventBusUtils.poseEvent(new RefreshEvent(OrderState.ALLRESPONSE));
                        EventBusUtils.poseEvent(new RefreshEvent(OrderState.WAITINGPAY));
                        pwdDialog.setPrice(response.data.payMoney);
                        if (LoginHelper.getInstance().hasSetPwd()) {
                            //设置了交易密码)
                            pwdDialog.showDialog();
                        } else {
                            TransactionPwdActivity.startIt(mBaseContext, LoginHelper.getInstance().getIdPerson());
                        }
                        orderNo = response.data.orderNo;
                        paymentMoney = response.data.payMoney;
                        isConfirm = true;
                        //订单确认后tag内容为”allowDisabled“控件不可点击，防止订单被修改
                        enableViewByTag(outerLayout, getString(R.string.view_allow_disabled), false);
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
                            //校验手机验证码,零首付用钱包

                            boolean isWallet = false;
                            if (payType == 0 && StringUtils.format2(paymentPrice).equals("0.00")) {
                                isWallet = true;
                            }
                            VerifyActivity.startItForShopping(mBaseContext, orderNo, isWallet, paymentMoney);
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == 100) { //大家电配送
            sendTimesId = data.getLongExtra("sendTimesId", 0);
            installTimesId = data.getLongExtra("installTimesId", 0);
            if (CommonUtils.isNotNullOrEmpty(sendTimesList)) {
                for (CreateOrderResponse.ReservingListBean bean : sendTimesList) {
                    if (sendTimesId == bean.id) {
                        tvSendTimeView.setText("送货:" + bean.date + "[" + bean.week + "]");
                    }
                }
            }

            if (CommonUtils.isNotNullOrEmpty(installTimesList)) {
                for (CreateOrderResponse.DateListBean bean : installTimesList) {
                    if (sendTimesId == bean.id) {
                        for (CreateOrderResponse.ReservingListBean bean1 : bean.dateList) {
                            if (installTimesId == bean1.id) {
                                tvInstallTimeView.setText("安装:" + bean1.date + "[" + bean1.week + "]");
                            }
                        }
                    }
                }
            }
        }

        if (requestCode == Const.ADDRESSMANAGE && resultCode == RESULT_OK) {//地址修改
            if (data.getSerializableExtra("address") != null) {
                AddressListResponse address = (AddressListResponse) data.getSerializableExtra("address");
                updateAddressUI(convertAddressBean(address));
            }
        }
    }

    private CreateOrderResponse.ReceiverJoBean convertAddressBean(AddressListResponse response) {
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
                backConfirmDialog.show();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void enableViewByTag(View view, String tag, boolean enable) {
        if (view instanceof ViewGroup) {
            int childCount = ((ViewGroup) view).getChildCount();
            for (int i = 0; i < childCount; i++) {
                enableViewByTag(((ViewGroup) view).getChildAt(i), tag, enable);
            }

        }
        if (view.getTag() != null && tag.equals(view.getTag().toString())) {
            view.setEnabled(enable);
            if (view instanceof TextView) {
                //去掉Textview的右箭头图标
                ((TextView) view).setCompoundDrawables(null, null, null, null);
            } else if (view instanceof ImageView) {
                //去掉ImageView的右箭头图标
                view.setVisibility(View.GONE);
            }
        }
    }
}
