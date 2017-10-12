package com.giveu.shoppingmall.index.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseFragment;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.index.presenter.ConfirmOrderPresenter;
import com.giveu.shoppingmall.index.view.agent.IConfirmOrderListener;
import com.giveu.shoppingmall.index.view.agent.IConfirmOrderView;
import com.giveu.shoppingmall.model.bean.response.CreateOrderResponse;
import com.giveu.shoppingmall.model.bean.response.DownPayMonthPayResponse;
import com.giveu.shoppingmall.model.bean.response.MonthSupplyResponse;
import com.giveu.shoppingmall.utils.Const;
import com.giveu.shoppingmall.utils.LoginHelper;

import butterknife.ButterKnife;

/**
 * Created by 524202 on 2017/10/11.
 */

public class ConfirmOrderFragment extends BaseFragment implements IConfirmOrderView {
    ViewStub vsAddress;
    ViewStub vsSku;
    ViewStub vsPayment;
    ViewStub vsMessage;
    View content;
    private boolean firstInit = true;
    private ConfirmOrderPresenter presenter;
    private IConfirmOrderListener listener;

    private int downPaymentRate; //首付比例
    private int quantity; //商品数量
    private String skuCode;//商品skuCode
    private long idProduct; //分期id
    private int payType; //付款方式

    public static ConfirmOrderFragment newInstance(String skuCode, int downPaymentRate, int quantity, int payType, long idProduct) {
        Bundle args = new Bundle();
        args.putString("skuCode", skuCode);
        args.putInt("downPaymentRate", downPaymentRate);
        args.putInt("quantity", quantity);
        args.putInt("payType", payType);
        args.putLong("idProduct", idProduct);
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
        if (vsAddress != null) {
            return;
        }
        vsAddress = (ViewStub) content.findViewById(R.id.confirm_order_address_stub);
        vsSku = (ViewStub) content.findViewById(R.id.confirm_order_sku_stub);
        vsPayment = (ViewStub) content.findViewById(R.id.confirm_order_payment_stub);
        vsMessage = (ViewStub) content.findViewById(R.id.confirm_order_message_stub);
        vsAddress.inflate();
        vsSku.inflate();
        vsPayment.inflate();
        vsMessage.invalidate();
        //initView()是不初始化其他view的，占空布局渲染后才初始化，如果在其他位置还有ButterKnife.bind
        //由于占空布局未渲染，会报空指针异常
        ButterKnife.bind(this, content);

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
        presenter = new ConfirmOrderPresenter(this);
        presenter.getAppDownPayAndMonthPay(mBaseContext, Const.CHANNEL, LoginHelper.getInstance().getIdPerson(),
                downPaymentRate,skuCode,quantity);
    }

    @Override
    public void getAppDownPayAndMonthPaySuccess(DownPayMonthPayResponse response) {
        initListView();
    }

    @Override
    public void getAppDownPayAndMonthPayFailed() {

    }

    @Override
    public void getCreateOrderScSuccess(CreateOrderResponse response) {

    }

    @Override
    public void getCreateOrderScFailed() {

    }

    @Override
    public void getAppMonthlySupplySuccess(MonthSupplyResponse response) {

    }

    @Override
    public void getAppMonthlySupplyFailed() {

    }
}
