package com.giveu.shoppingmall.index.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.cash.view.activity.AddAddressActivity;
import com.giveu.shoppingmall.cash.view.activity.VerifyActivity;
import com.giveu.shoppingmall.index.view.dialog.ChooseCouponDialog;
import com.giveu.shoppingmall.index.widget.MiddleRadioButton;
import com.giveu.shoppingmall.index.widget.StableEditText;
import com.giveu.shoppingmall.me.view.dialog.DealPwdDialog;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.PayPwdResponse;
import com.giveu.shoppingmall.recharge.view.dialog.PaymentTypeDialog;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.widget.DetailView;
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
    DetailView dvCouponView;
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
    @BindView(R.id.confirm_order_add_address)
    MiddleRadioButton mAddressBtn;
    @BindView(R.id.confirm_order_address_layout)
    RelativeLayout rvAddressLayout;

    private ChooseCouponDialog couponDialog;
    private PaymentTypeDialog paymentTypeDialog;
    private CustomListDialog firstPayDialog;
    private CustomListDialog monthDialog;
    private DealPwdDialog pwdDialog;

    private int payType = 0;
    private int couponType = 0;

    private List<CharSequence> firstPayList;
    private List<CharSequence> monthList;

    private String sendTime;
    private String installTime;


    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_confirm_order);
        baseLayout.setTitle("订单信息确认");

        paymentTypeDialog = new PaymentTypeDialog(this);
        paymentTypeDialog.setOnChoosePayTypeListener(new PaymentTypeDialog.OnChoosePayTypeListener() {
            @Override
            public void onChooseType(int type, String paymentTypeStr) {
                dvPayView.setRightText(paymentTypeStr);
                payType = type;
            }
        });

        msgEditText.setStableText("买家留言(选填)：");
        msgEditText.setText("提示信息");

        couponDialog = new ChooseCouponDialog(this, Arrays.asList(new CharSequence[]{"满600减30元全品类", "不使用优惠券"}), new ChooseCouponDialog.OnChooseTypeListener() {
            @Override
            public void onChooseType(int type, String typeMsg) {
                couponType = type;
                dvCouponView.setRightText(typeMsg);
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

    }

    @Override
    public void setData() {

    }

    public static void startIt(Context context) {
        Intent intent = new Intent(context, ConfirmOrderActivity.class);
        context.startActivity(intent);
    }

    @OnClick({R.id.confirm_order_pay_type, R.id.confirm_order_coupon,
            R.id.confirm_order_first_pay, R.id.confirm_order_month,
            R.id.confirm_order_household, R.id.confirm_order_add_address,
            R.id.tv_ok})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.confirm_order_pay_type:
                paymentTypeDialog.showDialog(payType);
                break;
            case R.id.confirm_order_coupon:
                couponDialog.show(couponType);
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
            case R.id.confirm_order_add_address:
                mAddressBtn.setVisibility(View.GONE);
                rvAddressLayout.setVisibility(View.VISIBLE);
                AddAddressActivity.startIt(this);
                break;
            case R.id.tv_ok:
                pwdDialog.showDialog();
//                PayChannelActivity.startIt(mBaseContext, "", "");
                break;

            default:
                break;
        }
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
                            VerifyActivity.startItForShopping(mBaseContext, "",false,"");
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
            sendTime = data.getStringExtra("time_send");
            installTime = data.getStringExtra("time_install");
            if (StringUtils.isNotNull(sendTime)) {
                tvSendTimeView.setText("送货：" + sendTime);
            }

            if (StringUtils.isNotNull(installTime)) {
                tvInstallTimeView.setText("安装：" + installTime);
            }
        }
    }
}
