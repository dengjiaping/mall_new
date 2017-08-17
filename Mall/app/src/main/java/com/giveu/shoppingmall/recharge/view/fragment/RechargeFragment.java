package com.giveu.shoppingmall.recharge.view.fragment;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.base.BaseFragment;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.cash.view.activity.VerifyActivity;
import com.giveu.shoppingmall.event.OrderDialogEvent;
import com.giveu.shoppingmall.event.PwdDialogEvent;
import com.giveu.shoppingmall.event.RechargePayEvent;
import com.giveu.shoppingmall.index.view.activity.MainActivity;
import com.giveu.shoppingmall.index.view.activity.TransactionPwdActivity;
import com.giveu.shoppingmall.me.view.dialog.NotActiveDialog;
import com.giveu.shoppingmall.model.bean.response.ConfirmOrderResponse;
import com.giveu.shoppingmall.model.bean.response.LoginResponse;
import com.giveu.shoppingmall.model.bean.response.RechargeResponse;
import com.giveu.shoppingmall.model.bean.response.SegmentResponse;
import com.giveu.shoppingmall.recharge.presenter.RechargePresenter;
import com.giveu.shoppingmall.recharge.view.activity.RechargeStatusActivity;
import com.giveu.shoppingmall.recharge.view.agent.IRechargeView;
import com.giveu.shoppingmall.recharge.view.dialog.ChargeOrderDialog;
import com.giveu.shoppingmall.recharge.view.dialog.PwdDialog;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.PayUtils;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.widget.NoScrollGridView;
import com.giveu.shoppingmall.widget.dialog.OnlyConfirmDialog;
import com.giveu.shoppingmall.widget.dialog.PermissionDialog;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 充值模块
 * Created by 508632 on 2016/12/13.
 */

public class RechargeFragment extends BaseFragment implements IRechargeView {

    LvCommonAdapter<RechargeResponse.PackageBean> rechargeAdapter;
    private RechargeResponse rechargeResponse;
    @BindView(R.id.rb_bill)
    RadioButton rbBill;
    @BindView(R.id.rb_flow)
    RadioButton rbFlow;
    @BindView(R.id.rg_recharge)
    RadioGroup rgRecharge;
    @BindView(R.id.et_recharge)
    EditText etRecharge;
    @BindView(R.id.tv_message)
    TextView tvMessage;
    @BindView(R.id.gv_recharge)
    NoScrollGridView gvRecharge;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    @BindView(R.id.iv_mail_list)
    ImageView ivMailList;
    private String phoneArea;
    //运营商
    private String salePrice;
    private String mobile;
    private int productType;
    private long productId;
    private String productName;
    //产品id
    private String pid;
    PwdDialog pwdDialog;
    private OnlyConfirmDialog warnningDialog;

    private int tabIndex;//0=话费 1=流量
    private boolean isVailable = false;//是否可点击
    private ArrayList<RechargeResponse.PackageBean> productList;
    private String currentOperator = "";
    private RechargePresenter presenter;
    private String orderNo;
    private int paymentType;
    private ChargeOrderDialog orderDialog;
    NotActiveDialog notActiveDialog;//未开通钱包的弹窗
    private PermissionDialog permissionDialog;
    private MainActivity mainActivity;
    private boolean pwdDismissByCancel = true;//密码框是否因为点击返回键而消失的标识

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(mBaseContext, R.layout.fragment_main_recharge, null);
        baseLayout.setTitle("手机充值");
        baseLayout.hideBack();
        ButterKnife.bind(this, view);
        gvRecharge.setEnabled(false);
        presenter = new RechargePresenter(this);
        warnningDialog = new OnlyConfirmDialog(mBaseContext);
        notActiveDialog = new NotActiveDialog(mBaseContext);
        pwdDialog = new PwdDialog(mBaseContext);
        initPermissionDialog();
        registerEventBus();
        mainActivity = (MainActivity) mBaseContext;
        return view;
    }

    @Override
    protected BasePresenter[] initPresenters() {
        return new BasePresenter[]{presenter};
    }


    @Override
    protected void setListener() {
        etRecharge.addTextChangedListener(textWatcher);
        ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //清除输入的手机号
                etRecharge.setText("");
            }
        });
        ivMailList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (mainActivity != null) {
                        mainActivity.applyContactPermission();
                    }
                } else {
                    if (mainActivity != null) {
                        mainActivity.skipToContactsContract();
                    }
                }
            }
        });
        pwdDialog.setdismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //当密码弹框消失时，判断是不是因为点击返回键消失的
                // 如果是那么需要展示订单弹框，避免因为订单消失而重复创建订单
                if (pwdDismissByCancel && orderDialog != null) {
                    ToastUtils.showShortToast("支付取消");
                    orderDialog.showDialog();
                }
                pwdDismissByCancel = true;
            }
        });

        gvRecharge.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int checkId, long l) {
                //先判断有没登录，然后再判断是否有钱包资质，满足条件后才进入充值
                if (LoginHelper.getInstance().hasLoginAndGotoLogin(mBaseContext)) {
                    if (LoginHelper.getInstance().hasQualifications()) {
                        //判断是否设置了交易密码
                        if (LoginHelper.getInstance().hasSetPwd()) {
                            salePrice = StringUtils.format2(rechargeAdapter.getItem(checkId).salePrice + "");
                            mobile = etRecharge.getText().toString();
                            productType = rechargeAdapter.getItem(checkId).productType;
                            productId = rechargeAdapter.getItem(checkId).callTrafficId;
                            productName = rechargeAdapter.getItem(checkId).name;
                            presenter.createRechargeOrder(LoginHelper.getInstance().getIdPerson(), etRecharge.getText().toString().replace(" ", ""),
                                    rechargeAdapter.getItem(checkId).callTrafficId);
                        } else {
                            TransactionPwdActivity.startIt(mBaseContext, LoginHelper.getInstance().getIdPerson());
                        }
                    } else {
                        notActiveDialog.showDialog();
                    }
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUserInfo(LoginResponse response) {
        //更新手机号
        initPhoneAndQuery();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showPwdDialog(PwdDialogEvent event) {
        //填写完居住地址回来弹出密码框（PwdDialog）
        if (pwdDialog != null) {
            pwdDialog.showDialog();
        }
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
    public void initDataDelay() {
        productList = new ArrayList<>();
        rechargeAdapter = new LvCommonAdapter<RechargeResponse.PackageBean>(mBaseContext, R.layout.gv_recharge_item, productList) {
            @Override
            protected void convert(ViewHolder viewHolder, RechargeResponse.PackageBean item, int position) {
                TextView tv1 = viewHolder.getView(R.id.tv_recharge_t1);
                TextView tv2 = viewHolder.getView(R.id.tv_recharge_t2);
                tv1.setText(item.name);
                tv2.setText("售价￥" + item.salePrice);
                LinearLayout ll_recharge_item = viewHolder.getView(R.id.ll_recharge_item);
                if (!item.clickEnable) {
                    tv1.setTextColor(getContext().getResources().getColor(R.color.color_edittext));
                    tv2.setTextColor(getContext().getResources().getColor(R.color.color_edittext));
                    ll_recharge_item.setBackgroundResource(R.drawable.shape_recharge_default);
                } else {
                    tv1.setTextColor(getContext().getResources().getColor(R.color.title_color));
                    tv2.setTextColor(getContext().getResources().getColor(R.color.android_default_dialog_title));
                    ll_recharge_item.setBackgroundResource(R.drawable.shape_recharge_press);
                }
            }
        };

        gvRecharge.setAdapter(rechargeAdapter);
        rgRecharge.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                switch (checkId) {
                    case R.id.rb_bill:
                        tabIndex = 0;
                        showContentData();
                        break;
                    case R.id.rb_flow:
                        tabIndex = 1;
                        showContentData();
                        break;
                    default:
                        break;
                }
            }
        });
        //获取充值产品
        presenter.getProducts();
    }

    @Override
    protected boolean translateStatusBar() {
        return true;
    }

    //对手机号的格式指定
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s == null) {
                return;
            }
            //判断是否是在中间输入，需要重新计算
            boolean isMiddle = (start + count) < (s.length());
            //在末尾输入时，是否需要加入空格
            boolean isNeedSpace = false;
            if (!isMiddle && isNeedSpace(s.length())) {
                isNeedSpace = true;
            }
            if (isMiddle || isNeedSpace) {
                String newStr = s.toString();
                etRecharge.removeTextChangedListener(textWatcher);
                StringBuilder sb = phoneNumberFormat(newStr);
                etRecharge.setText(sb);

                //如果是在末尾的话,或者加入的字符个数大于零的话（输入或者粘贴）
                if (!isMiddle || count > 1) {
                    etRecharge.setSelection(sb.length());
                } else if (isMiddle) {
                    //如果是删除
                    if (count == 0) {
                        //如果删除时，光标停留在空格的前面，光标则要往前移一位
                        if (isNeedSpace(start - before + 1)) {
                            etRecharge.setSelection((start - before) > 0 ? start - before : 0);
                        } else {
                            etRecharge.setSelection((start - before + 1) > sb.length() ? sb.length() : (start - before + 1));
                        }
                    }
                    //如果是增加
                    else {
                        if (isNeedSpace(start - before + count)) {
                            etRecharge.setSelection((start + count - before + 1) < sb.length() ? (start + count - before + 1) : sb.length());
                        } else {
                            etRecharge.setSelection(start + count - before);
                        }
                    }
                }
                etRecharge.addTextChangedListener(textWatcher);
            }

            if (etRecharge.getText().toString().length() > 0) {
                ivClear.setVisibility(View.VISIBLE);
            } else {
                ivClear.setVisibility(View.GONE);
            }

        }

        private boolean isNeedSpace(int length) {
            if (length < 4) {
                return false;
            } else if (length == 4) {
                return true;
            } else return (length + 1) % 5 == 0;
        }

        //1表示显示地区 0表示隐藏
        @Override
        public void afterTextChanged(Editable editable) {
            currentOperator = "";
            if (editable.length() == 13) {
                String phone = editable.toString().replace(" ", "");
                CommonUtils.closeSoftKeyBoard(mBaseContext);
                //获取该手机号运营商
                presenter.getPhoneInfo(phone);
                tvMessage.setText("");
            } else {
                isVailable = false;
                tvMessage.setTextColor(ContextCompat.getColor(mBaseContext, R.color.color_ff2a2a));
                tvMessage.setText("错误号码");
                if (rechargeAdapter != null) {
                    changeItemCanClick(rechargeAdapter.getData());
                }
            }
        }
    };


    /**
     * 展示流量还是话费，并且区分运营商
     */
    private void showContentData() {
        if (rechargeAdapter == null) {
            return;
        }

        switch (tabIndex) {
            case 0:
                //话费充值
                //中国移动
                if (rechargeResponse != null && rechargeResponse.call != null) {
                    if ("0".equals(currentOperator)) {
                        rechargeAdapter.setData(rechargeResponse.call.cmccs);
                    } else if ("1".equals(currentOperator)) {
                        //中国联通
                        rechargeAdapter.setData(rechargeResponse.call.cuccs);
                    } else if ("2".equals(currentOperator)) {
                        //中国电信
                        rechargeAdapter.setData(rechargeResponse.call.ctcs);
                    }
                } else {
                    rechargeAdapter.setData(null);
                }

                break;

            case 1:
                //流量充值
                //中国移动
                if (rechargeResponse != null && rechargeResponse.traffic != null) {
                    if ("0".equals(currentOperator)) {
                        rechargeAdapter.setData(rechargeResponse.traffic.cmccs);
                    } else if ("1".equals(currentOperator)) {
                        //中国联通
                        rechargeAdapter.setData(rechargeResponse.traffic.cuccs);

                    } else if ("2".equals(currentOperator)) {
                        //中国电信
                        rechargeAdapter.setData(rechargeResponse.traffic.ctcs);
                    }
                } else {
                    rechargeAdapter.setData(null);
                }

                break;
        }
        //如果当前并没有输入手机号，默认显示移动的产品
        if (StringUtils.isNull(currentOperator)) {
            if (tabIndex == 0) {
                if (rechargeResponse != null && rechargeResponse.call != null) {
                    rechargeAdapter.setData(rechargeResponse.call.cmccs);
                } else {
                    rechargeAdapter.setData(null);
                }
            } else {
                if (rechargeResponse != null && rechargeResponse.traffic != null) {
                    rechargeAdapter.setData(rechargeResponse.traffic.cmccs);
                } else {
                    rechargeAdapter.setData(null);
                }
            }
        }
        if (rechargeAdapter != null) {
            changeItemCanClick(rechargeAdapter.getData());
        }
    }

    //设置gridView的Item是否可点击
    private void changeItemCanClick(List<RechargeResponse.PackageBean> data) {
        if (data != null) {
            if (!isVailable) {
                for (RechargeResponse.PackageBean products : data) {
                    products.clickEnable = false;
                }
                gvRecharge.setEnabled(false);
            } else {//绿色
                for (RechargeResponse.PackageBean products : data) {
                    products.clickEnable = true;
                }
                gvRecharge.setEnabled(true);
            }
        }
        rechargeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            // ContentProvider展示数据类似一个单个数据库表
            // ContentResolver实例带的方法可实现找到指定的ContentProvider并获取到ContentProvider的数据
            ContentResolver reContentResolverol = mBaseContext.getContentResolver();
            // URI,每个ContentProvider定义一个唯一的公开的URI,用于指定到它的数据集
            Uri contactData = data.getData();
            // 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
            Cursor cursor = mBaseContext.managedQuery(contactData, null, null, null, null);
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
                StringBuilder sb = new StringBuilder(usernumber.replaceAll(" ", ""));
                if (sb.toString().length() != 11) {
                    ToastUtils.showShortToast("手机号码格式有误");
                } else {
                    sb.insert(3, " ");
                    sb.insert(8, " ");
                    etRecharge.setText(sb.toString());
                    etRecharge.setSelection(sb.length());
                }
            }
            if (phone != null) {
                phone.close();
            }
        }
    }

    public void setPhoneText(String phone) {
        if (etRecharge != null) {
            etRecharge.setText(phone);
            etRecharge.setSelection(phone.length());
        }
    }

    /**
     * 手机号码格式化，空格隔开
     *
     * @return
     */
    public StringBuilder phoneNumberFormat(String phoneNumber) {
        phoneNumber = phoneNumber.replace(" ", "");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < phoneNumber.length(); ) {
            if (i == 0) {
                sb.append(phoneNumber.length() > 3 ? phoneNumber.substring(0, 3) : phoneNumber);
                i += 3;
                continue;
            } else if (i > 0) {
                sb.append(" ");
                if (i + 4 <= phoneNumber.length()) {
                    sb.append(phoneNumber.substring(i, i + 4));
                } else {
                    sb.append(phoneNumber.substring(i, phoneNumber.length()));
                }
                i += 4;
            }
        }
        return sb;
    }

    @Override
    public void showProducts(RechargeResponse data) {
        //默认显示中国移动话费充值
        if (data == null || data.call == null) {
            return;
        }
        rechargeResponse = data;
        if (CommonUtils.isNotNullOrEmpty(data.call.cmccs)) {
            productList.addAll(data.call.cmccs);
            rechargeAdapter.notifyDataSetChanged();
            initPhoneAndQuery();
        }
    }

    /**
     * 自动填充手机并查询归属地
     */
    public void initPhoneAndQuery() {
        //避免重复请求相同手机号归属地需加判断
        if (StringUtils.isNotNull(LoginHelper.getInstance().getPhone())
                && !LoginHelper.getInstance().getPhone().equals(etRecharge.getText().toString().replaceAll(" ", ""))) {
            //自动填充空格，并查询手机信息
            StringBuilder ownerPhone = new StringBuilder(LoginHelper.getInstance().getPhone());
            ownerPhone.insert(3, " ");
            ownerPhone.insert(8, " ");
            etRecharge.setText(ownerPhone.toString());
            etRecharge.setSelection(ownerPhone.length());
        }
    }


    @Override
    public void showPhoneInfo(SegmentResponse data) {
        //查询手机归属地成功的回调
        isVailable = true;
        String phone = etRecharge.getText().toString();
        if (StringUtils.isNotNull(LoginHelper.getInstance().getPhone())
                && LoginHelper.getInstance().getPhone().equals(phone.replaceAll(" ", ""))) {
            tvMessage.setText("账户绑定用户(" + data.city + data.isp + ")");
        } else {
            tvMessage.setText(data.city + data.isp);
        }
        tvMessage.setTextColor(ContextCompat.getColor(mBaseContext, R.color.color_00adb2));
        currentOperator = data.code;
        showContentData();
        phoneArea = data.city + data.isp;
    }

    @Override
    public void showErrorInfo(String message) {
        //查询手机归属地失败后的回调
        ToastUtils.showShortToast(message);
    }

    @Override
    public void createOrderSuccess(final String orderNoResponse) {
        //创建订单成功
        orderDialog = new ChargeOrderDialog(mBaseContext, phoneArea, productName, mobile, salePrice);
        orderDialog.setProductType(productType);
        //可用消费额度小于当前要充值的金额，默认选择微信支付
        if (StringUtils.string2Double(LoginHelper.getInstance().getAvailablePoslimit()) < StringUtils.string2Double(salePrice)) {
            orderDialog.setDefaultPay(1);
        }
        orderDialog.setOnConfirmListener(new ChargeOrderDialog.OnConfirmListener() {
            @Override
            public void onConfirm(int paymentType) {
                //是否超过500额度充值上限
                if (paymentType == 0 && StringUtils.string2Double(LoginHelper.getInstance().getAvailableRechargeLimit()) < StringUtils.string2Double(salePrice)) {
                    ToastUtils.showLongToast("您已超出每月500元充值上限，请下个月进行充值");
                } else {
                    RechargeFragment.this.paymentType = paymentType;
                    if (paymentType == 0) {
                        //钱包支付
                        if (StringUtils.string2Double(LoginHelper.getInstance().getAvailablePoslimit()) >= StringUtils.string2Double(salePrice)) {
                            pwdDialog.setOnCheckPwdListener(new PwdDialog.OnCheckPwdListener() {
                                @Override
                                public void checkPwd(String payPwd) {
                                    presenter.checkPwd(LoginHelper.getInstance().getIdPerson(), payPwd);
                                }
                            });
                            pwdDialog.showDialog();
                        } else {
                            ToastUtils.showLongToast("可用消费额度不足，请使用其他支付方式");
                        }

                    } else {
                        //微信支付或支付宝支付
                        presenter.confirmRechargeOrder(LoginHelper.getInstance().getIdPerson(), mobile.replace(" ", ""), productId, orderNoResponse, paymentType, "", "");
                    }
                    orderDialog.dissmissDialog();
                    RechargeFragment.this.orderNo = orderNoResponse;
                }
            }
        });
        orderDialog.showDialog();
    }


    @Override
    public void pwdSuccess() {
        pwdDismissByCancel = false;
        //交易密码校验成功
        pwdDialog.dissmissDialog();
        VerifyActivity.startItForRecharge(mBaseContext, mobile.replace(" ", ""), productId, orderNo, paymentType, salePrice);
    }

    @Override
    public void pwdError(int remainTimes) {
        //交易密码错误,弹出密码错误框
        pwdDialog.showPwdError(remainTimes);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void orderDialogShow(OrderDialogEvent event) {
        if (orderDialog != null) {
            orderDialog.showDialog();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void wxPayStatus(RechargePayEvent event) {
        //微信支付后收到通知
        if (event != null) {
            if (event.payStatus == 0) {
                //微信支付成功
                RechargeStatusActivity.startIt(mBaseContext, "success", null, salePrice + "元", salePrice + "元");
            } else if (event.payStatus == -1) {
                //微信支付失败
                RechargeStatusActivity.startIt(mBaseContext, "fail", "很抱歉，本次支付失败，请重新发起支付", salePrice + "元", salePrice + "元");
            } else if (event.payStatus == -2) {
                //微信支付取消,显示订单信息
                if (orderDialog != null) {
                    orderDialog.showDialog();
                }
            }
        }

    }

    @Override
    public void confirmOrderSuccess(ConfirmOrderResponse data) {
        if (data != null && data.payType == 1) {
            BaseApplication.getInstance().setBeforePayActivity(mBaseContext.getClass().getSimpleName());
            IWXAPI iWxapi = PayUtils.getWxApi();
            PayReq payReq = PayUtils.getRayReq(data.partnerid, data.prepayid, data.packageValue, data.noncestr, data.timestamp, data.sign);
            iWxapi.sendReq(payReq);
        } else if (data != null && data.payType == 2) {
            PayUtils.AliPay(mBaseContext, data.alipay, new PayUtils.AliPayThread.OnPayListener() {
                @Override
                public void onSuccess() {
                    RechargeStatusActivity.startIt(mBaseContext, "success", null, salePrice + "元", salePrice + "元");
                }

                @Override
                public void onFail() {
                    RechargeStatusActivity.startIt(mBaseContext, "fail", "很抱歉，本次支付失败，请重新发起支付", salePrice + "元", salePrice + "元");
                }

                @Override
                public void onCancel() {
                    ToastUtils.showShortToast("支付取消");
                    //支付宝支付取消,显示订单信息
                    if (orderDialog != null) {
                        orderDialog.showDialog();
                    }
                }
            });
        }
    }

    @Override
    public void confirmOrderFail() {
        RechargeStatusActivity.startIt(mBaseContext, "fail", "很抱歉，本次支付失败，请重新发起支付", salePrice + "元", salePrice + "元");
    }
}
