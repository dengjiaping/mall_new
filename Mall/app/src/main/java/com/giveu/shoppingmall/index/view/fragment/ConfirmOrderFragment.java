package com.giveu.shoppingmall.index.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseFragment;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.cash.view.activity.AddressManageActivity;
import com.giveu.shoppingmall.index.presenter.ConfirmOrderPresenter;
import com.giveu.shoppingmall.index.view.agent.IConfirmOrderListener;
import com.giveu.shoppingmall.index.view.agent.IConfirmOrderView;
import com.giveu.shoppingmall.index.view.dialog.AnnuityDialog;
import com.giveu.shoppingmall.index.view.dialog.ChooseCardsDialog;
import com.giveu.shoppingmall.index.view.dialog.ChooseDialog;
import com.giveu.shoppingmall.index.widget.MiddleRadioButton;
import com.giveu.shoppingmall.me.view.activity.CustomWebViewActivity;
import com.giveu.shoppingmall.model.bean.response.AddressListResponse;
import com.giveu.shoppingmall.model.bean.response.CreateOrderResponse;
import com.giveu.shoppingmall.model.bean.response.DownPayMonthPayResponse;
import com.giveu.shoppingmall.model.bean.response.MonthSupplyResponse;
import com.giveu.shoppingmall.model.bean.response.SkuInfo;
import com.giveu.shoppingmall.recharge.view.dialog.PaymentTypeDialog;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.Const;
import com.giveu.shoppingmall.utils.ImageUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.widget.NoScrollListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

/**
 * Created by 524202 on 2017/10/11.
 */

public class ConfirmOrderFragment extends BaseFragment implements IConfirmOrderView {
    ViewStub vsAddress;
    ViewStub vsSku;
    ViewStub vsPayment;
    ViewStub vsCards;
    View content;
    private boolean firstInit = true;
    private ConfirmOrderPresenter presenter;
    private IConfirmOrderListener listener;

    private int downPaymentRate; //首付比例
    private int quantity; //商品数量
    private String skuCode;//商品skuCode
    private long idProduct; //分期id
    private int payType; //付款方式

    private AddressViewHolder addressHolder = null;
    private SkuViewHolder skuHolder = null;
    private PaymentViewHolder paymentHolder = null;
    private CardsViewHolder cardsHolder = null;

    private CreateOrderResponse createOrderResponse = null; //订单初始化数据
    private MonthSupplyResponse monthSupplyResponse = null; //月供初始化数据
    private List<CreateOrderResponse.InitListBean> paymentRateList = null;//首付列表
    private List<CreateOrderResponse.AvsListBean> incrementServiceList; //增值服务
    private List<CreateOrderResponse.CardListBean> cardList;
    private int tempDownPaymentRate = 0; //临时首付比例
    private long tempIdProduct = 0; //临时分期Id
    private List<DownPayMonthPayResponse> paymentList;
    private List<DownPayMonthPayResponse> tempPaymentList;
    /************************
     * 客户地址信息
     ****************************/
    private CreateOrderResponse.ReceiverJoBean addressJoBean; //客户送货地址json

    /************************
     * 对话框
     ****************************/
    private PaymentTypeDialog paymentTypeDialog; //支付选择对话框
    private CustomDialog quotaNoticeDialog; // 额度不足提示退化看
    private ChooseDialog<CreateOrderResponse.InitListBean> paymentRateDlg; //首付选择对话框
    private ChooseDialog<DownPayMonthPayResponse> paymentDlg; //分期选择对话框
    private AnnuityDialog annuityDialog; //月供预览对话框
    private ChooseCardsDialog cardDlg;    //优惠券对话框

    private String paymentPrice = "0"; //首付价格
    private String totalPrice = "0"; //商品总价格
    private String annuityPrice = "0"; //月供价格
    private String cardPrice = "0"; //优惠券价格
    private int paymentNum; //分期期数
    private int tempPaymentNum;
    private int insuranceFee = 1; //是否购买人身意外保险 0不购买1购买
    private int tempInsuranceFee = 1;

    /**
     * 首付更新标志，如果首付变动->分期查询->月供查询中间任何一个环节失败,isBlockPaymentRate标志置为false
     * 直到月供查询成功才更新首付的值
     */
    private boolean isBlockPaymentRate = false;
    /**
     * 分期更新标志，如果分期变动->月供查询成才更新分期的值
     */
    private boolean isBlockPayment = false;
    /**
     * 增值服务更新标志，如果增值服务变动->月供查询成才更新增值服务的值
     */
    private boolean isBlockIncreaseService = false;
    private LvCommonAdapter<CreateOrderResponse.AvsListBean> incrementServiceAdapter;
    private long serviceId = 0;
    private long cardId;

    public static ConfirmOrderFragment newInstance(String skuCode, int downPaymentRate, int quantity, int payType, long idProduct, int paymentNum) {
        Bundle args = new Bundle();
        args.putString("skuCode", skuCode);
        args.putInt("downPaymentRate", downPaymentRate);
        args.putInt("quantity", quantity);
        args.putInt("payType", payType);
        args.putLong("idProduct", idProduct);
        args.putInt("paymentNum", paymentNum);
        ConfirmOrderFragment fragment = new ConfirmOrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            skuCode = bundle.getString("skuCode");
            downPaymentRate = bundle.getInt("downPaymentRate");
            quantity = bundle.getInt("quantity", 1);
            idProduct = bundle.getLong("idProduct");
            payType = bundle.getInt("payType");
            paymentNum = bundle.getInt("paymentNum");
            tempDownPaymentRate = downPaymentRate;
            tempIdProduct = idProduct;
            tempPaymentNum = paymentNum;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (IConfirmOrderListener) context;
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        content = inflater.inflate(R.layout.fragment_confirm_order_layout, null, false);
        baseLayout.setTitleBarAndStatusBar(false, false);
        baseLayout.setTopBarBackgroundColor(R.color.red);
        return content;
    }

    private void initListView() {
        //初始化过了直接return
        if (!firstInit) {
            return;
        }
        vsAddress = (ViewStub) content.findViewById(R.id.confirm_order_address_stub);
        vsSku = (ViewStub) content.findViewById(R.id.confirm_order_sku_stub);
        vsPayment = (ViewStub) content.findViewById(R.id.confirm_order_payment_stub);
        vsCards = (ViewStub) content.findViewById(R.id.confirm_order_cards_stub);
        vsAddress.inflate();
        vsSku.inflate();
        vsPayment.inflate();
        vsCards.inflate();
        //initView()是不初始化其他view的，占空布局渲染后才初始化，如果在其他位置还有ButterKnife.bind
        //由于占空布局未渲染，会报空指针异常
        ButterKnife.bind(this, content);

        addressHolder = new AddressViewHolder(content);
        skuHolder = new SkuViewHolder(content);
        paymentHolder = new PaymentViewHolder(content);
        cardsHolder = new CardsViewHolder(content);

        initPayType();

        setViewListener();
    }

    private void setViewListener() {
        addressHolder.flAddresslayout.setOnClickListener(chooseAddressListener);
        skuHolder.tvPayTypeView.setOnClickListener(paymentTypeDlgListener);
        paymentHolder.tvPaymentRateView.setOnClickListener(paymentRateDlgListener);
        paymentHolder.tvPaymentView.setOnClickListener(paymentDlgListener);
        paymentHolder.tvAnnuityView.setOnClickListener(annuityDlgListener);
        cardsHolder.tvCardsView.setOnClickListener(cardsDlgListener);
    }

    @Override
    protected BasePresenter[] initPresenters() {
        return new BasePresenter[]{presenter};
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void initDataDelay() {
        if (presenter == null) {
            presenter = new ConfirmOrderPresenter(this);
        }
        presenter.getCreateOrderSc(mBaseContext, Const.CHANNEL, LoginHelper.getInstance().getIdPerson(),
                downPaymentRate, tempIdProduct, new SkuInfo(quantity, skuCode));
    }

    @Override
    public void getCreateOrderScSuccess(CreateOrderResponse response) {
        createOrderResponse = response;
        paymentRateList = createOrderResponse.initList;
        incrementServiceList = createOrderResponse.avsList;
        cardList = createOrderResponse.cardList;
        if (CommonUtils.isNotNullOrEmpty(paymentRateList)) {    //首付信息不为空，表明是分期商品,查询分期成功才初始化
            isBlockPaymentRate = true;
            presenter.getAppDownPayAndMonthPay(mBaseContext, Const.CHANNEL, LoginHelper.getInstance().getIdPerson(),
                    tempDownPaymentRate, skuCode, quantity);
        } else { //首付信息为空，表示是非分期商品，直接初始化
            initListView();
            updateUI();
            firstInit = false;
            listener.onInitSuccess();
        }
    }

    @Override
    public void getCreateOrderScFailed() {
        listener.onInitFailed();
    }

    @Override
    public void getAppDownPayAndMonthPaySuccess(DownPayMonthPayResponse response) {
        tempPaymentList = response.data;

        if (!checkIdProductAvaliable()) {//如果当前分期不存在，更新分期数据
            DownPayMonthPayResponse lastBean = tempPaymentList.get(tempPaymentList.size() - 1);
            tempIdProduct = StringUtils.string2Long(lastBean.idProduct);
            tempPaymentNum = lastBean.paymentNum;
        }
        presenter.getAppMonthlySupply(mBaseContext, Const.CHANNEL, LoginHelper.getInstance().getIdPerson(), tempDownPaymentRate, tempIdProduct, insuranceFee, quantity, skuCode);
    }

    @Override
    public void getAppDownPayAndMonthPayFailed() {
        if (firstInit) {
            listener.onInitFailed();
        } else {

        }
    }

    @Override
    public void getAppMonthlySupplySuccess(MonthSupplyResponse response) {
        if (firstInit) {
            initListView();
            updateUI();
            firstInit = false;
            listener.onInitSuccess();
        }
        if (isBlockPaymentRate) {
            downPaymentRate = tempDownPaymentRate;
            updatePaymentRateUI();

            if (paymentList == null) {
                paymentList = new ArrayList<>();
            } else {
                paymentList.clear();
            }
            paymentList.addAll(tempPaymentList);
            idProduct = tempIdProduct;
            listener.onDownPaymentChanged(idProduct);
            paymentNum = tempPaymentNum;
            updatePaymentUI();
            isBlockPaymentRate = false;
        }

        if (isBlockPayment) {
            paymentNum = tempPaymentNum;
            idProduct = tempIdProduct;
            updatePaymentUI();
            isBlockPayment = false;
        }

        if (isBlockIncreaseService) {
            isBlockIncreaseService = false;
        }

        updateAnnuityUI(response);
    }

    @Override
    public void getAppMonthlySupplyFailed() {
        if (firstInit) {
            listener.onInitFailed();
        } else {
            if (isBlockIncreaseService) {
                updateIncrementServiceUI();
                isBlockIncreaseService = false;
            }

        }
    }

    /**
     * 初始化支付方式相关UI
     */
    private void initPayType() {

        if (paymentTypeDialog == null) {
            initPayTypeDialog();
        }

        String availableRecharge = LoginHelper.getInstance().getAvailablePoslimit();
        if ("0.00".equals(StringUtils.format2(availableRecharge))) {
            //如果额度为0,隐藏钱包支付,默认选择支付宝支付
            paymentTypeDialog.disableWalletPay();
            payType = 2;
        }

        if (payType == 0) {
            skuHolder.tvPayTypeView.setText("即有钱包");
        } else if (payType == 2) {
            skuHolder.tvPayTypeView.setText("支付宝");
        }
        onPayTypeChanged();
    }

    private void onPayTypeChanged() {
        if (listener != null) {
            listener.onPayTypeChanged(payType);
        }

        if (payType == 0) {
            if (CommonUtils.isNotNullOrEmpty(paymentRateList)) {
                paymentHolder.llIncrementService.setVisibility(View.VISIBLE);
                paymentHolder.paymentLayout.setVisibility(View.VISIBLE);
            }
//            rlCardsViewLayout.setVisibility(View.GONE);

        } else {
            paymentHolder.llIncrementService.setVisibility(View.GONE);
            paymentHolder.paymentLayout.setVisibility(View.GONE);
//            if (CommonUtils.isNotNullOrEmpty(cardList)) {
//                rlCardsViewLayout.setVisibility(View.VISIBLE);
//            }
        }
    }

    private void updateUI() {
        //更新地址界面
        updateAddressUI(createOrderResponse.receiverJo);
        //更新商品界面
        updateSkuUI();
        //更新优惠券界面
        //updateCardUI(result.data.cardList);
        //更新首付比例界面
//        if (CommonUtils.isNotNullOrEmpty(paymentRateList)) {
//            updatePaymentRateUI();
//            //更新分期
//            updatePaymentUI();
//            updateAnnuityUI(monthSupplyResponse);
//        }
        //更新增值服务
        updateIncrementServiceUI();
        //更新优惠券
        updateCardUI();
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
     * 更新优惠券相关UI
     */
    private void updateCardUI() {
        if (CommonUtils.isNotNullOrEmpty(cardList)) {
            //默认选择优惠额度最大的优惠券
            double maxCardPrice = 0;
            String maxCardName = "";
            for (CreateOrderResponse.CardListBean cardBean : cardList) {
                double curCardPrice = StringUtils.string2Double(cardBean.price);
                if (curCardPrice > maxCardPrice) {
                    maxCardPrice = curCardPrice;
                    cardId = cardBean.id;
                    maxCardName = cardBean.name;
                }
            }
            //更新优惠券显示
            cardsHolder.tvCardsView.setText(maxCardName);
            //更新实际付款金额
            cardPrice = StringUtils.format2(maxCardPrice + "");
            listener.onCourtesyCardIdChanged(cardId, cardPrice);
            if (cardDlg == null) {
                initCardsDialog();
            }

            if (cardsHolder.rlCardsViewLayout.getVisibility() != View.VISIBLE && payType != 0) {
                //钱包支付暂不支持优惠券
                cardsHolder.rlCardsViewLayout.setVisibility(View.VISIBLE);
            }
        } else {
            if (cardsHolder.rlCardsViewLayout.getVisibility() != View.GONE) {
                cardsHolder.rlCardsViewLayout.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 更新首付相关UI
     */
    private void updatePaymentRateUI() {
        if (paymentHolder.paymentLayout.getVisibility() != View.VISIBLE && payType == 0) {
            paymentHolder.paymentLayout.setVisibility(View.VISIBLE);
        }
        CharSequence text = "请选择";
        for (CreateOrderResponse.InitListBean bean : paymentRateList) {
            if (bean.id == downPaymentRate) {
                text = formatPaymentRateText(bean);
                listener.onDownPaymentRateChanged(downPaymentRate, bean.price);
                break;
            }
        }
        if (paymentRateDlg == null) {
            initPaymentRateDialog();
        }
        paymentHolder.tvPaymentRateView.setText(text);

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

    private boolean checkIdProductAvaliable() {
        boolean isAvaliable = false;
        for (DownPayMonthPayResponse bean : tempPaymentList) {
            if (bean.paymentNum == paymentNum) {
                return true;
            }
        }
        return isAvaliable;
    }

    /**
     * 更新分期相关UI
     */
    private void updatePaymentUI() {

        String text = paymentNum + "个月";

        paymentHolder.tvPaymentView.setText(text);
        if (paymentDlg == null) {
            initPaymentDialog();
        } else {
            paymentDlg.refreshData();
        }
    }

    /**
     * 更新月供相关UI
     *
     * @param response 月供数据
     */
    private void updateAnnuityUI(MonthSupplyResponse response) {
        if (CommonUtils.isNotNullOrEmpty(response.paymentList)) {
            annuityPrice = response.paymentList.get(0).monthPay;
            CommonUtils.setTextWithSpanSizeAndColor(paymentHolder.tvAnnuityView, "¥", StringUtils.format2(annuityPrice), "",
                    15, 11, R.color.title_color, R.color.title_color);

            if (annuityDialog == null) {
                annuityDialog = new AnnuityDialog(mBaseContext);
            }
            annuityDialog.refreshData(response, true);
        }
    }

    /**
     * 更新增值服务相关UI
     */
    private void updateIncrementServiceUI() {
        if (CommonUtils.isNotNullOrEmpty(incrementServiceList)) {
            if (paymentHolder.llIncrementService.getVisibility() != View.VISIBLE && payType == 0) {
                paymentHolder.llIncrementService.setVisibility(View.VISIBLE);
            }

            if (incrementServiceAdapter == null) {
                incrementServiceAdapter = new LvCommonAdapter<CreateOrderResponse.AvsListBean>(mBaseContext, R.layout.adapter_increment_item, incrementServiceList) {
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
                                            tempInsuranceFee = isChecked ? 1 : 0;
                                            serviceId = item.serviceId;
                                            isBlockIncreaseService = true;
                                            presenter.getAppMonthlySupply(mBaseContext, Const.CHANNEL, LoginHelper.getInstance().getIdPerson(), downPaymentRate, idProduct, tempInsuranceFee, quantity, skuCode);
                                        }
                                    }
                                })
                                .setChecked(R.id.increment_name, insuranceFee == 1);
                        if (position == 0) {
                            serviceId = item.serviceId;
                        }
                        TextView priceViw = viewHolder.getView(R.id.increment_price);
                        CommonUtils.setTextWithSpanSizeAndColor(priceViw, "¥", StringUtils.format2(item.servicePrice), "/月",
                                14, 11, R.color.title_color, R.color.title_color);
                    }
                };
                paymentHolder.inCrementListView.setAdapter(incrementServiceAdapter);
            } else {
                incrementServiceAdapter.notifyDataSetChanged();
            }
        }
    }

    private void updateSkuUI() {
        if (createOrderResponse != null && createOrderResponse.skuInfo != null) {
            CreateOrderResponse.SkuInfoBean skuInfoBean = createOrderResponse.skuInfo;

            if (StringUtils.isNotNull(skuInfoBean.src) && StringUtils.isNotNull(skuInfoBean.srcIp)) {
                String url = skuInfoBean.srcIp + "/" + skuInfoBean.src;
                ImageUtils.loadImage(url, R.drawable.default_img_240_240, skuHolder.ivSkuInfoIcon);
            }

            if (StringUtils.isNotNull(skuInfoBean.name)) {
                skuHolder.tvSkuInfoName.setText(StringUtils.ToAllFullWidthString(skuInfoBean.name));
            }

            if (StringUtils.isNotNull(skuInfoBean.salePrice)) {
                CommonUtils.setTextWithSpanSizeAndColor(skuHolder.tvSkuInfoPrice, "¥ ", StringUtils.format2(skuInfoBean.salePrice), "",
                        19, 13, R.color.black, R.color.black);
            }

            skuHolder.tvSkuInfoQuantity.setText("× " + skuInfoBean.quantity);

            if (StringUtils.isNotNull(skuInfoBean.totalPrice)) {
                CommonUtils.setTextWithSpanSizeAndColor(skuHolder.tvSkuInfoTotalPrice, "¥ ", StringUtils.format2(skuInfoBean.totalPrice), "",
                        19, 13, R.color.red, R.color.black);
                totalPrice = skuInfoBean.totalPrice;
                listener.onTotalPriceChanged(totalPrice);
            }
        }
    }

    private void updateAddressUI(CreateOrderResponse.ReceiverJoBean bean) {
        if (bean != null) {
            addressJoBean = bean;

            listener.onCustomerAddressChanged(addressJoBean);

            if (StringUtils.isNotNull(addressJoBean.custName)) {
                addressHolder.tvAddressName.setText(addressJoBean.custName);
            }

            if (StringUtils.isNotNull(addressJoBean.phone)) {
                addressHolder.tvAddressPhone.setText(addressJoBean.phone);
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
                addressHolder.tvAddressContent.setText(sb.toString());
            }

            if (addressHolder.rvAddressLayout.getVisibility() != View.VISIBLE) {
                addressHolder.mAddressBtn.setVisibility(View.GONE);
                addressHolder.rvAddressLayout.setVisibility(View.VISIBLE);
            }
        } else {
            if (addressHolder.mAddressBtn.getVisibility() != View.VISIBLE) {
                addressHolder.mAddressBtn.setVisibility(View.VISIBLE);
                addressHolder.rvAddressLayout.setVisibility(View.GONE);
            }
        }
    }


    private View.OnClickListener chooseAddressListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mBaseContext, AddressManageActivity.class);
            intent.putExtra("canClick", true);
            startActivityForResult(intent, Const.ADDRESSMANAGE);
        }
    };

    private View.OnClickListener paymentTypeDlgListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (paymentTypeDialog != null) {
                paymentTypeDialog.showDialog(payType);
            }
        }
    };

    private View.OnClickListener paymentRateDlgListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (paymentRateDlg != null && CommonUtils.isNotNullOrEmpty(paymentRateList)) {
                paymentRateDlg.show(downPaymentRate);
            }
        }
    };

    private View.OnClickListener paymentDlgListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (paymentDlg != null && CommonUtils.isNotNullOrEmpty(paymentList)) {
                paymentDlg.show(paymentNum);
            }
        }
    };

    private View.OnClickListener annuityDlgListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (annuityDialog != null) {
                annuityDialog.show();
            }
        }
    };

    private View.OnClickListener cardsDlgListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (cardDlg != null && CommonUtils.isNotNullOrEmpty(cardList)) {
                cardDlg.show(cardId);
            }
        }
    };

    private void initCardsDialog() {
        cardDlg = new ChooseCardsDialog(mBaseContext, cardList, new ChooseCardsDialog.OnChooseTypeListener() {
            @Override
            public void onChooseType(long id, String price, String name) {
                cardsHolder.tvCardsView.setText(name);
                cardPrice = price;
                cardId = id;
                listener.onCourtesyCardIdChanged(id, cardPrice);
            }
        });
    }

    private void initPayTypeDialog() {
        paymentTypeDialog = new PaymentTypeDialog(mBaseContext);
        paymentTypeDialog.setOnChoosePayTypeListener(new PaymentTypeDialog.OnChoosePayTypeListener() {
            @Override
            public void onChooseType(int type, String paymentTypeStr) {
                skuHolder.tvPayTypeView.setText(paymentTypeStr);
                payType = type;
                onPayTypeChanged();
            }
        });
    }

    private void initPaymentRateDialog() {
        paymentRateDlg = new ChooseDialog<CreateOrderResponse.InitListBean>(mBaseContext, paymentRateList) {
            @Override
            public void convertView(ViewHolder holder, final CreateOrderResponse.InitListBean item, int position, long checkIndex) {
                holder.setText(R.id.dialog_choose_item_text, item.name);
                holder.setChecked(R.id.dialog_choose_item_text, item.id == checkIndex);
                holder.setOnClickListener(R.id.dialog_choose_item_text, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        paymentRateDlg.dismiss();
                        if (tempDownPaymentRate != item.id) {
//                        CharSequence text = formatPaymentRateText(item);
//                        paymentHolder.tvPaymentRateView.setText(text);
//                            setTotalPrice(paymentPrice);
                            tempDownPaymentRate = item.id;
                            isBlockPaymentRate = true;
                            presenter.getAppDownPayAndMonthPay(mBaseContext, Const.CHANNEL, LoginHelper.getInstance().getIdPerson(),
                                    tempDownPaymentRate, skuCode, quantity);
                        }
                    }
                });
            }
        };
    }

    private void initPaymentDialog() {
        paymentDlg = new ChooseDialog<DownPayMonthPayResponse>(mBaseContext, paymentList) {
            @Override
            public void convertView(ViewHolder holder, final DownPayMonthPayResponse item, int position, long checkIndex) {
                holder.setText(R.id.dialog_choose_item_text, item.paymentNum + "个月");
                holder.setChecked(R.id.dialog_choose_item_text, item.paymentNum == checkIndex);
                holder.setOnClickListener(R.id.dialog_choose_item_text, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        paymentDlg.dismiss();
                        if (paymentNum != item.paymentNum) {
                            tempIdProduct = StringUtils.string2Long(item.idProduct);
                            tempPaymentNum = item.paymentNum;
                            isBlockPayment = true;
                            presenter.getAppMonthlySupply(mBaseContext, Const.CHANNEL, LoginHelper.getInstance().getIdPerson(), tempDownPaymentRate, tempIdProduct, insuranceFee, quantity, skuCode);
                        }
                    }
                });
            }
        };
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

    static class AddressViewHolder {
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

        AddressViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class SkuViewHolder {
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
        //选择付款方式
        @BindView(R.id.confirm_order_pay_type)
        TextView tvPayTypeView;

        SkuViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class PaymentViewHolder {
        //首付
        @BindView(R.id.confirm_order_first_pay)
        TextView tvPaymentRateView;
        @BindView(R.id.confirm_order_support_installment)
        LinearLayout paymentLayout;
        //分期数
        @BindView(R.id.confirm_order_month)
        TextView tvPaymentView;
        //月供
        @BindView(R.id.confirm_order_annuity)
        TextView tvAnnuityView;
        //增值服务
        @BindView(R.id.confirm_order_increment_service)
        LinearLayout llIncrementService;
        @BindView(R.id.confirm_order_increment_service_list)
        NoScrollListView inCrementListView;

        PaymentViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class CardsViewHolder {
        //优惠券
        @BindView(R.id.confirm_order_card_text)
        TextView tvCardsView;
        @BindView(R.id.confirm_order_card_layout)
        RelativeLayout rlCardsViewLayout;

        CardsViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
