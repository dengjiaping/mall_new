package com.giveu.shoppingmall.cash.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.mynet.ApiUrl;
import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.cash.view.dialog.CostDialog;
import com.giveu.shoppingmall.cash.view.dialog.MonthlyDetailsDialog;
import com.giveu.shoppingmall.event.PwdDialogEvent;
import com.giveu.shoppingmall.index.view.activity.PerfectContactsActivity;
import com.giveu.shoppingmall.index.view.activity.TransactionPwdActivity;
import com.giveu.shoppingmall.me.view.activity.AddBankCardFirstActivity;
import com.giveu.shoppingmall.me.view.activity.CustomWebViewActivity;
import com.giveu.shoppingmall.me.view.activity.LivingAddressActivity;
import com.giveu.shoppingmall.me.view.activity.MyBankCardActivity;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.CostFeeResponse;
import com.giveu.shoppingmall.model.bean.response.LoginResponse;
import com.giveu.shoppingmall.model.bean.response.PayPwdResponse;
import com.giveu.shoppingmall.model.bean.response.ProductResponse;
import com.giveu.shoppingmall.model.bean.response.RepayCostResponse;
import com.giveu.shoppingmall.model.bean.response.RpmDetailResponse;
import com.giveu.shoppingmall.recharge.view.dialog.PwdDialog;
import com.giveu.shoppingmall.recharge.view.dialog.PwdErrorDialog;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.Const;
import com.giveu.shoppingmall.utils.DensityUtils;
import com.giveu.shoppingmall.utils.ImageUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.widget.RulerView;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 取现类型页
 * Created by 101900 on 2017/6/26.
 */

public class CashTypeActivity extends BaseActivity {
    RulerView rulerView;
    @BindView(R.id.et_input_amount)
    EditText etInputAmount;
    @BindView(R.id.gv_staging_type)
    GridView gvStagingType;
    @BindView(R.id.rl_add_bank_card)
    RelativeLayout rlAddBankCard;
    @BindView(R.id.tv_monthly_payment)
    TextView tvMonthlyPayment;
    @BindView(R.id.tv_ensure_bottom)
    TextView tvEnsureBottom;
    @BindView(R.id.ll_choose_bank)
    LinearLayout llChooseBank;
    @BindView(R.id.tv_bank_name)
    TextView tvBankName;
    @BindView(R.id.tv_available_credit)
    TextView tvAvailableCredit;
    @BindView(R.id.rl_cash_type)
    RelativeLayout rlCashType;
    @BindView(R.id.tv_draw_money)
    TextView tvDrawMoney;
    @BindView(R.id.tv_cost)
    TextView tvCost;
    @BindView(R.id.ll_show_data)
    LinearLayout llShowData;
    @BindView(R.id.ll_bg_top)
    LinearLayout llBgTop;
    @BindView(R.id.cb_desc)
    CheckBox cbDesc;
    @BindView(R.id.iv_bank)
    ImageView ivBank;
    @BindView(R.id.tv_cost_fee)
    TextView tvCostFee;
    @BindView(R.id.iv_pointer)
    ImageView ivPointer;
    @BindView(R.id.tv_agreement)
    TextView tvAgreement;
    @BindView(R.id.tv_agreement_top)
    TextView tvAgreementTop;
    @BindView(R.id.cb_desc_top)
    CheckBox cbDescTop;
    @BindView(R.id.ll_agreement_top)
    LinearLayout llAgreementTop;
    @BindView(R.id.ll_monthly_payment)
    LinearLayout llMonthlyPayment;
    private LvCommonAdapter<ProductResponse> stagingTypeAdapter;
    double chooseQuota;//选择额度
    public final int MAXAMOUNT = 3000;//3000目前最大额
    public final int MINAMOUNT = 300;//300目前分期产品最小范围（300-1000,1000-3000）
    private ViewGroup decorView;
    private int idProduct = 0;
    private boolean isLargeAmount = false;//是否是大额(下期从入口传过来，判断是大额还是小额，目前默认小额)
    String availableCylimit;
    int localIdProduct = 0;//选择的期数产品id
    boolean isChooseProduct = false;//是否选择了分期产品 false没选择
    boolean keyBordIsShow = false;//键盘是否消失
    boolean isStage = false;//是否是分期，false 随借随还 true 分期
    String chooseBankNo;//选择的银行卡号
    String chooseBankName;//选择的银行卡名
    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener;
    PwdDialog pwdDialog;

    public static void startIt(Activity mActivity) {
        Intent intent = new Intent(mActivity, CashTypeActivity.class);
        mActivity.startActivity(intent);
    }

    //
    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_cash_type);
        rulerView = (RulerView) findViewById(R.id.ruler_view);
        initTitle();
        BaseApplication.getInstance().fetchUserInfo();
        initDefaultBankCardLayout();
        decorView = (ViewGroup) getWindow().getDecorView();
        availableCylimit = LoginHelper.getInstance().getAvailableCylimit();
        //初始化一个随借随还的协议
        setTvAgreement(0);
        if (StringUtils.isNotNull(availableCylimit)) {
            int cylimit = (int) Double.parseDouble(availableCylimit);

            tvAvailableCredit.setText(availableCylimit);//接收到的String转成double，填写到textView中
            chooseQuota = Double.parseDouble(availableCylimit);
            etInputAmount.setText(String.valueOf((int) chooseQuota));//接收到的String转成int，填写到editText中

            initScaleScrollView(cylimit);
            initColorByCylimit(isLargeAmount);
            setKeyBordDismissListener();
            initAdapter(isLargeAmount);
            registerEventBus();//注册EventBus
        }

        showPwdDialog();
    }

    /**
     * 协议的控制，localIdProduct为0是随借随还，其他为分期产品
     *
     * @param localIdProduct
     */
    public void setTvAgreement(int localIdProduct) {
        String url = "";
        if (localIdProduct == 0) {
            //随借随还
            url = ApiUrl.WebUrl.cashBLoanStatic;
        } else {
            //分期
            url = ApiUrl.WebUrl.cashLoanStatic;
        }
        CommonUtils.setTextWithSpan(tvAgreementTop, false, "同意参加", "《人身意外伤害保险》", R.color.black, R.color.title_color, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //人身意外伤害保险
                CustomWebViewActivity.startIt(mBaseContext, ApiUrl.WebUrl.xFFQInsuredNotice, "人身意外伤害保险");
            }
        });

        final String finalUrl = url;
        CommonUtils.setTextWithSpan(tvAgreement, false, "已阅读并同意", "借款及服务相关协议", R.color.black, R.color.title_color, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //借款及服务相关协议
                CustomWebViewActivity.startIt(mBaseContext, finalUrl, "借款及服务相关协议");
            }
        });
    }

    /**
     * 根据isLargeAmount初始化当前页的字体背景颜色,true是大额
     *
     * @param isLargeAmount
     */
    public void initColorByCylimit(boolean isLargeAmount) {
        if (isLargeAmount) {
            //大额显示橙色
            etInputAmount.setTextColor(getResources().getColor(R.color.color_fe8d50));
            llBgTop.setBackgroundResource(R.drawable.shape_cash_bg_large);
            baseLayout.setTopBarBgDrawble(R.color.color_febb41);
            tvEnsureBottom.setBackgroundResource(R.color.color_fe984a);
            ivPointer.setImageResource(R.drawable.ic_pointer_orange);
        } else {
            //小额显示蓝色
            etInputAmount.setTextColor(getResources().getColor(R.color.title_color));
            llBgTop.setBackgroundResource(R.drawable.shape_cash_bg_small);
            baseLayout.setTopBarBgDrawble(R.color.color_00c9cd);
            tvEnsureBottom.setBackgroundResource(R.color.color_00bbc0);
            ivPointer.setImageResource(R.drawable.ic_pointer_blue);
        }
    }

    /**
     * 初始化刻度尺控件
     */
    private void initScaleScrollView(final int cylimit) {
        //刻度尺默认最大额度
        rulerView.setEndRange(cylimit);
        rulerView.requestLayout();
        rulerView.post(new Runnable() {
            @Override
            public void run() {
                if (StringUtils.isNotNull(availableCylimit)) {
                    int maxCylimit = (int) Double.parseDouble(availableCylimit);
                    if (maxCylimit > 3000) {
                        maxCylimit = 3000;
                    }
                    rulerView.smoothScrollTo(maxCylimit);//刻度尺选择可用额度最大值
                }
            }
        });
    }

    /**
     * 键盘消失的监听，输完金额键盘消失后显示分期数据
     */

    public void setKeyBordDismissListener() {
        final int statusBarHeight = DensityUtils.getStatusBarHeight();
        globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            int previousKeyboardHeight = -1;

            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                int displayHeight = 0;
                decorView.getWindowVisibleDisplayFrame(rect);
                if (rect.top == 0) {
                    displayHeight = rect.bottom - statusBarHeight;
                } else {
                    displayHeight = rect.bottom - rect.top;
                }
                int height = decorView.getHeight();
                final int keyboardHeight = height - rect.bottom;
                if (previousKeyboardHeight != keyboardHeight) {
                    boolean hide = (double) displayHeight / height > 0.8;
                    if (hide) {
                        //键盘消失监听
                        if (keyBordIsShow) {
                            //第一次进来
                            String input = StringUtils.getTextFromView(etInputAmount);
                            if (StringUtils.isNotNull(input)) {
                                chooseQuota = Double.parseDouble(input);
                                if (ensureBtnCanclick(chooseQuota)) {//满足条件
                                    if (lastEditTextValue != chooseQuota) {
                                        rulerView.smoothScrollTo((int) chooseQuota);
                                        RefreshData();
                                    }
                                }
                            }
                        } else {
                            keyBordIsShow = true;
                        }
                    }
                }
                previousKeyboardHeight = keyboardHeight;
            }
        };
    }

    /**
     * 显示默认银行卡界面，如果没有显示添加银行卡
     */
    private void initDefaultBankCardLayout() {
        LoginHelper instance = LoginHelper.getInstance();
        String bankName = instance.getBankName();
        String bankNo = instance.getDefaultCard();
        String bankIcon = instance.getBankIconUrl();
        boolean hasDefaultCard = instance.hasDefaultCard();
        if (hasDefaultCard) {
            //默认卡可用
            rlAddBankCard.setVisibility(View.GONE);
            llChooseBank.setVisibility(View.VISIBLE);
            if (bankNo.length() >= 4) {
                bankNo = bankNo.substring(bankNo.length() - 4, bankNo.length());
            }
            bankName = bankName + "(尾号" + bankNo + ")";
            tvBankName.setText(bankName);
            ImageUtils.loadImage(bankIcon, R.color.transparent, ivBank);
        } else {
            //默认卡不可用
            rlAddBankCard.setVisibility(View.VISIBLE);
            llChooseBank.setVisibility(View.GONE);
        }
    }

    /**
     * 设置标题的背景字体颜色
     */
    private void initTitle() {
        baseLayout.setTitle("我要取现");
        baseLayout.setTopBarBgDrawble(R.color.title_color);
        baseLayout.setRightTextColor(R.color.white);
        baseLayout.setTitleTextColor(R.color.white);
        baseLayout.setBackImage(R.drawable.back);
        baseLayout.setRightTextAndListener("取现记录", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CaseRecordActivity.startIt(mBaseContext);
            }
        });
    }

    private void initAdapter(final boolean isLargeAmount) {
        stagingTypeAdapter = new LvCommonAdapter<ProductResponse>(mBaseContext, R.layout.tv_cash_type_item, new ArrayList<ProductResponse>()) {
            @Override
            protected void convert(ViewHolder viewHolder, ProductResponse item, int position) {
                TextView tvStagingType = viewHolder.getView(R.id.tv_staging_type);

                if (item.paymentNum == 0) {
                    tvStagingType.setText("按日计息");
                } else {
                    tvStagingType.setText(item.paymentNum + "期");
                }

                if (item.isChecked) {
                    //设置分期按钮颜色，区分大额小额
                    setGvItemClick(tvStagingType, isLargeAmount);

                } else {
                    setGvItemNotClick(tvStagingType, isLargeAmount);
                }

            }

            /**
             * 设置分期数按钮的颜色(点击)
             *
             * @param isLargeAmount
             */
            private void setGvItemClick(TextView textView, boolean isLargeAmount) {
                if (isLargeAmount) {
                    //大额
                    textView.setTextColor(getResources().getColor(R.color.white));
                    textView.setBackgroundResource(R.drawable.shape_cash_item_press_large);
                } else {
                    //小额
                    textView.setTextColor(getResources().getColor(R.color.white));
                    textView.setBackgroundResource(R.drawable.shape_cash_item_press_small);
                }
            }

            /**
             * 设置分期数按钮的颜色(未点击)
             *
             * @param isLargeAmount
             */
            private void setGvItemNotClick(TextView textView, boolean isLargeAmount) {
                if (isLargeAmount) {
                    //大额
                    textView.setTextColor(getResources().getColor(R.color.color_fe8d50));
                    textView.setBackgroundResource(R.drawable.shape_cash_item_normal_large);
                } else {
                    //小额
                    textView.setTextColor(getResources().getColor(R.color.title_color));
                    textView.setBackgroundResource(R.drawable.shape_cash_item_normal_small);
                }
            }
        };
        gvStagingType.setAdapter(stagingTypeAdapter);
    }

    public boolean ensureBtnCanclick(double chooseQuota) {
        if (chooseQuota < 100) {
            ToastUtils.showShortToast("取现不少于100元");
            return false;
        }
        if (chooseQuota % 10 != 0) {
            ToastUtils.showShortToast("仅支持取现整数，请调整取现金额");
            return false;
        }
        if (chooseQuota > 3000) {
            ToastUtils.showShortToast("提款额不能大于3000");
            return false;
        }
        if (StringUtils.isNotNull(availableCylimit)) {
            if (chooseQuota > Double.parseDouble(availableCylimit)) {
                ToastUtils.showShortToast("取现额度不足");
                return false;
            }
        }

        return true;
    }

    boolean isExecuteOnScaleChanged = false;//是否执行了OnScaleChanged（滑动监听）
    double lastEditTextValue;

    @Override
    public void setListener() {
        super.setListener();
        cbDescTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoanAmount(localIdProduct, (int) chooseQuota);
            }
        });
        rulerView.setOnAdjustIndicateListener(new RulerView.OnAdjustIndicateListener() {
            @Override
            public void stop() {
                if (isExecuteOnScaleChanged) {
                    //第一次次回弹且值相等调接口
                    if (chooseQuota < 100) {
                        ToastUtils.showShortToast("取现不少于100元");
                        rulerView.smoothScrollTo(100);//低于100滑到100，并提示
                    }
                    if (lastEditTextValue != chooseQuota) {
                        RefreshData();
                    }
                }
            }
        });
        //滑动监听，设值
        rulerView.setOnScaleListener(new RulerView.OnScaleListener() {
            @Override
            public void onScaleChanged(int scale) {
                isExecuteOnScaleChanged = true;
                chooseQuota = scale;
                etInputAmount.setText(String.valueOf((int) chooseQuota));
                etInputAmount.setSelection(StringUtils.getTextFromView(etInputAmount).length());
            }
        });

        gvStagingType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int checkId, long id) {
                if (stagingTypeAdapter != null) {
                    if (stagingTypeAdapter.getCount() > 0) {
                        if (stagingTypeAdapter.getItem(checkId) == null) {
                            return;
                        }
                        if (0 == stagingTypeAdapter.getItem(checkId).idProduct) {
                            //随借随还则隐藏保险
                            llAgreementTop.setVisibility(View.GONE);
                        } else {
                            llAgreementTop.setVisibility(View.VISIBLE);
                        }
                        for (int i = 0; i < stagingTypeAdapter.getCount(); i++) {
                            if (stagingTypeAdapter.getItem(i) == null) {
                                return;
                            }
                            if (checkId == i) {
                                stagingTypeAdapter.getItem(i).isChecked = true;
                                idProduct = stagingTypeAdapter.getItem(i).idProduct;
                                localIdProduct = idProduct;
                                showLoanAmount(idProduct, (int) chooseQuota);
                            } else {
                                stagingTypeAdapter.getItem(i).isChecked = false;
                            }
                        }
                        setTvAgreement(localIdProduct);
                        stagingTypeAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    /**
     * 显示或隐藏月控和贷款本金
     *
     * @param idProduct
     * @param chooseQuota
     */
    private void showLoanAmount(int idProduct, int chooseQuota) {
        if (idProduct != 0) {
            llShowData.setVisibility(View.VISIBLE);
            String insuranceFee = cbDescTop.isChecked() ? "1" : "0";
            ApiImpl.repayCost(mBaseContext, idProduct, insuranceFee, chooseQuota, new BaseRequestAgent.ResponseListener<RepayCostResponse>() {
                @Override
                public void onSuccess(RepayCostResponse response) {
                    if (response.data != null) {
                        RepayCostResponse product = response.data;
                        tvDrawMoney.setText("贷款本金：" + product.drawMoney);
                        tvCost.setText("（含咨询费￥" + product.cost + "）");
                        tvMonthlyPayment.setText("每月还款额：" + (int) product.monthPay + "元");
                    }
                }

                @Override
                public void onError(BaseBean errorBean) {
                    CommonLoadingView.showErrorToast(errorBean);
                }
            });
        } else {
            llShowData.setVisibility(View.GONE);
        }
    }

    /**
     * 如果显示的数据和本地不一样则刷新
     */
    public void refrashUI() {
        String cylimit = StringUtils.getTextFromView(tvAvailableCredit);
        if (!cylimit.equals(LoginHelper.getInstance().getAvailableCylimit())) {
            initView(null);
            RefreshData();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
        //取现完成回来刷新数据
        refrashUI();
    }

    @Override
    public void onStop() {
        super.onStop();
        decorView.getViewTreeObserver().removeGlobalOnLayoutListener(globalLayoutListener);
    }

    List<ProductResponse> productList;

    @Override
    public void setData() {
        //获取一次
        //获取产品数据（分期数）
        ApiImpl.initProduct(mBaseContext, LoginHelper.getInstance().getAvailableCylimit(), new BaseRequestAgent.ResponseListener<ProductResponse>() {
            @Override
            public void onSuccess(ProductResponse response) {
                productList = response.data;
                RefreshData();
            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
        //获取费率
        getCostFee();

    }

    public void RefreshData() {
        //选中不同金额调取本地数据，不请求接口
//        if (lastEditTextValue != chooseQuota) {
//            lastEditTextValue = chooseQuota;
//            setStageNumberData(chooseQuota);
//        }
        lastEditTextValue = chooseQuota;
        setStageNumberData(chooseQuota);
    }

    //获取按日计息费率
    private void getCostFee() {
        String costFee = StringUtils.getTextFromView(tvCostFee);
        if (StringUtils.isNull(costFee)) {
            ApiImpl.getCostFee(mBaseContext, new BaseRequestAgent.ResponseListener<CostFeeResponse>() {
                @Override
                public void onSuccess(CostFeeResponse response) {
                    String costFee = String.valueOf(response.data.costFee);//按日计息费率
                    costFee = StringUtils.isNull(costFee) ? "" : "日综合息费" + costFee + "%";
                    tvCostFee.setText(costFee);
                }

                @Override
                public void onError(BaseBean errorBean) {
                    CommonLoadingView.showErrorToast(errorBean);
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUserInfo(LoginResponse response) {
        //取现完成返回刷新数据（VerifyActivity）
        initDefaultBankCardLayout();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showPwdDialog(PwdDialogEvent event) {
        //填写完居住地址回来弹出密码框（LivingAddressActivity）
        pwdDialog.showDialog();
    }

    @OnClick({R.id.ll_monthly_payment, R.id.rl_add_bank_card, R.id.tv_ensure_bottom, R.id.ll_choose_bank, R.id.tv_cost})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ll_monthly_payment:
                //查看月供
                String insuranceFee = cbDescTop.isChecked() ? "1" : "0";
                ApiImpl.rpmDetail(mBaseContext, LoginHelper.getInstance().getIdPerson(), insuranceFee, localIdProduct, (int) chooseQuota, new BaseRequestAgent.ResponseListener<RpmDetailResponse>() {
                    @Override
                    public void onSuccess(RpmDetailResponse response) {
                        MonthlyDetailsDialog monthlyDetailsDialog = new MonthlyDetailsDialog(mBaseContext, response.data);
                        monthlyDetailsDialog.showDialog();
                    }

                    @Override
                    public void onError(BaseBean errorBean) {
                        CommonLoadingView.showErrorToast(errorBean);
                    }
                });
                break;
            case R.id.rl_add_bank_card:
                //添加银行卡
                AddBankCardFirstActivity.startIt(mBaseContext);
                break;
            case R.id.tv_ensure_bottom:
                //确定
                if (!ensureBtnCanclick(chooseQuota)) {
                    return;
                }
                if (!isChooseProduct) {
                    ToastUtils.showShortToast("请选择分期产品！");
                    return;
                }
                if (rlAddBankCard.getVisibility() == View.VISIBLE) {
                    ToastUtils.showShortToast("请添加银行卡！");
                    return;
                }
                if (!cbDesc.isChecked()) {
                    ToastUtils.showShortToast("请勾选借款及服务相关协议！");
                    return;
                }
                canShowPwdDialog();
                break;
            case R.id.ll_choose_bank:
                //银行卡列表
                MyBankCardActivity.startIt(mBaseContext, true);
                break;
            case R.id.tv_cost:
                //什么是咨询费
                CostDialog dialog = new CostDialog(mBaseContext);
                //显示密码框完成取现
                dialog.showDialog();
                break;
        }
    }

    /**
     * 资料是否完善的判断
     */
    public void canShowPwdDialog() {
        if (LoginHelper.getInstance().hasExistOther()) {
            //添加了联系人
            if (LoginHelper.getInstance().hasExistLive()) {
                //添加了居住地址,判断是否设置了交易密码
                if (LoginHelper.getInstance().hasSetPwd()) {
                    pwdDialog.showDialog();
                } else {
                    TransactionPwdActivity.startIt(mBaseContext, LoginHelper.getInstance().getIdPerson());
                }
            } else {
                //未添加地址
                LivingAddressActivity.startIt(mBaseContext);
            }
        } else {
            //未添加联系人
            PerfectContactsActivity.startIt(mBaseContext, Const.CASH);
        }
    }

    /**
     * 初始化交易密码dialog，判断是否有交易密码，有显示密码框，没有则跳转设置交易密码
     */
    public void showPwdDialog() {
        pwdDialog = new PwdDialog(mBaseContext, PwdDialog.statusType.CASH);
        pwdDialog.setOnCheckPwdListener(new PwdDialog.OnCheckPwdListener() {
            @Override
            public void checkPwd(String payPwd) {
                ApiImpl.verifyPayPwd(mBaseContext, LoginHelper.getInstance().getIdPerson(), payPwd, new BaseRequestAgent.ResponseListener<PayPwdResponse>() {
                            @Override
                            public void onSuccess(PayPwdResponse response) {
                                if (response.data != null) {
                                    PayPwdResponse pwdResponse = response.data;
                                    if (pwdResponse.status) {
                                        String creditType;
                                        //取现需要验证手机
                                        if (localIdProduct == 0) {//自行判断，idProduct=0则是随借随还（SH），有idProduct则是分期（SQ）
                                            creditType = "SH";
                                        } else {
                                            creditType = "SQ";
                                        }
                                        pwdDialog.dissmissDialog();
                                        //没有切换银行卡时，传默认银行卡
                                        if (StringUtils.isNull(chooseBankName)) {
                                            chooseBankName = LoginHelper.getInstance().getBankName();
                                        }
                                        if (StringUtils.isNull(chooseBankNo)) {
                                            chooseBankNo = LoginHelper.getInstance().getDefaultCard();
                                        }
                                        String insuranceFee;
                                        if (localIdProduct == 0) {
                                            //localIdProduct = 0，是随借随还状态
                                            insuranceFee = "0";
                                        } else {
                                            insuranceFee = cbDescTop.isChecked() ? "1" : "0";
                                        }
                                        VerifyActivity.startIt(mBaseContext, insuranceFee, VerifyActivity.CASH, String.valueOf((int) chooseQuota), creditType, String.valueOf(localIdProduct), response.data.code, chooseBankName, chooseBankNo);
                                    } else {
                                        //remainTimes: 1-3 重试密码 0 冻结密码需要找回密码
                                        PwdErrorDialog errorDialog = new PwdErrorDialog();
                                        errorDialog.showDialog(mBaseContext, pwdResponse.remainTimes);
                                    }
                                    CommonUtils.closeSoftKeyBoard(mBaseContext);
                                }
                            }

                            @Override
                            public void onError(BaseBean errorBean) {
                                CommonLoadingView.showErrorToast(errorBean);
                            }
                        }
                );
            }
        });


    }

    /**
     * 根据选择的额度显示不同的借款期数
     *
     * @param chooseQuota
     * @return
     */
    public void setStageNumberData(final double chooseQuota) {
        List<ProductResponse> data = new ArrayList();
        if (CommonUtils.isNotNullOrEmpty(productList)) {
            for (ProductResponse product : productList) {
                //300-1000,1000-3000,选择1000，属于1000-3000
                if (chooseQuota == 3000) {
                    //特殊情况，等于3000
                    if (chooseQuota >= product.creditFrom && chooseQuota <= product.creditTo) {
                        ProductResponse p = new ProductResponse(product.paymentNum, product.idProduct, false);
                        data.add(p);
                    }
                } else {
                    if (chooseQuota >= product.creditFrom && chooseQuota < product.creditTo) {
                        ProductResponse p = new ProductResponse(product.paymentNum, product.idProduct, false);
                        data.add(p);
                    }
                }
            }
            sortProduct(data);
        }


        ProductResponse p = null;
        //按日计息选项
        ProductResponse noStageProduct = new ProductResponse(0, 0, false);
        //默认显示分期gridView，和费率
        gvStagingType.setVisibility(View.VISIBLE);
        tvCostFee.setVisibility(View.VISIBLE);
        //默认把保险显示出来，只有随借随还（按日计息）不显示
        llAgreementTop.setVisibility(View.VISIBLE);
        if (chooseQuota > MAXAMOUNT) {
            //仅支持取现分期(>3000)
            llShowData.setVisibility(View.VISIBLE);
            if (CommonUtils.isNullOrEmpty(data)) {
                isChooseProduct = false;
                //没有产品隐藏gv和费率
                gvStagingType.setVisibility(View.GONE);
                tvCostFee.setVisibility(View.INVISIBLE);
                llShowData.setVisibility(View.GONE);
            } else {
                p = data.get(stagingTypeAdapter.getCount() - 1);
                if (p != null) {
                    localIdProduct = p.idProduct;
                }
                for (ProductResponse product : data) {
                    if (product.paymentNum == 18) {
                        product.isChecked = true;
                    } else {
                        product.isChecked = false;
                    }
                }
                isChooseProduct = true;
                showLoanAmount(localIdProduct, (int) chooseQuota);
                tvCostFee.setVisibility(View.GONE);
            }
            //     isLargeAmount = true;//标记大额
        } else if (chooseQuota >= MINAMOUNT && chooseQuota <= MAXAMOUNT) {
            //支持随借随还及取现分期(300-3000)
            if (CommonUtils.isNullOrEmpty(data)) {
                //分期产品list为空
                data.add(noStageProduct);
                data.get(0).isChecked = true;
                llShowData.setVisibility(View.GONE);
                isChooseProduct = true;//默认选择按日计息
                tvCostFee.setVisibility(View.VISIBLE);
            } else {
                data.add(noStageProduct);
                if (data.size() > 2) {
                    data.get(data.size() - 2).isChecked = true;
                    p = data.get(data.size() - 2);
                }
                data.get(data.size() - 1).isChecked = false;
                localIdProduct = p.idProduct;
                showLoanAmount(localIdProduct, (int) chooseQuota);
                tvCostFee.setVisibility(View.VISIBLE);
            }
            tvCostFee.setLayoutParams(getCostFeeLayoutParams(data));
            isChooseProduct = true;//默认会选择按日计息，即使没有分期产品
            //     isLargeAmount = false;//标记小额
        } else {
            //仅支持随借随还(<300)
            data.add(noStageProduct);
            data.get(0).isChecked = true;
            llShowData.setVisibility(View.GONE);
            //  isLargeAmount = false;//标记小额
            isChooseProduct = true;//默认选择按日计息
            tvCostFee.setVisibility(View.VISIBLE);
            tvCostFee.setLayoutParams(getCostFeeLayoutParams(data));
            localIdProduct = 0;
            //隐藏保险
            llAgreementTop.setVisibility(View.GONE);
        }
        //更新显示哪个协议
        setTvAgreement(localIdProduct);
        stagingTypeAdapter.setData(data);
        stagingTypeAdapter.notifyDataSetChanged();
        setGridViewHeightBasedOnChildren(DensityUtils.dip2px(6), 4, gvStagingType);
    }

    /**
     * 给分期产品按顺序排序
     *
     * @param data
     */
    public void sortProduct(List<ProductResponse> data) {
        ProductResponse tempProduct;
        for (int i = 0; i < data.size() - 1; i++) {
            for (int j = i + 1; j < data.size(); j++) {
                if (data.get(i).paymentNum > data.get(j).paymentNum) {
                    tempProduct = data.get(j);
                    data.set(j, data.get(i));
                    data.set(i, tempProduct);
                }
            }
        }
    }

    /**
     * 计算按日计息的费率的显示位置
     *
     * @return
     */
    public LinearLayout.LayoutParams getCostFeeLayoutParams(List<ProductResponse> data) {
        if (CommonUtils.isNotNullOrEmpty(data)) {
            LinearLayout.LayoutParams layoutParams;
            if (data.size() % 4 == 0) {
                layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, DensityUtils.dip2px(5), DensityUtils.dip2px(10), DensityUtils.dip2px(21));
                tvCostFee.setGravity(Gravity.RIGHT);
            } else {
                int width = (data.size() % 4 - 1) * (DensityUtils.getWidth() / 4) + DensityUtils.dip2px(10);
                layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(width, DensityUtils.dip2px(5), 0, DensityUtils.dip2px(21));
            }
            return layoutParams;
        }
        return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            chooseBankName = data.getStringExtra("chooseBankName");
            chooseBankNo = data.getStringExtra("chooseBankNo");
            String defalutBankIconUrl = data.getStringExtra("defalutBankIconUrl");
            String newBankNo = "";
            if (chooseBankNo.length() >= 4) {
                newBankNo = chooseBankNo.substring(chooseBankNo.length() - 4, chooseBankNo.length());
            }
            String bankName = chooseBankName + "(尾号" + newBankNo + ")";
            tvBankName.setText(bankName);
            if (StringUtils.isNotNull(defalutBankIconUrl)) {
                ImageUtils.loadImage(defalutBankIconUrl, R.color.transparent, ivBank);
            }

            //更新用户信息
            RefreshData();
        }
    }

    // 动态加载GridView 高度
    public static void setGridViewHeightBasedOnChildren(int verticalSpacing, int columnCount, GridView myGridView) {
        ListAdapter adapter = myGridView.getAdapter();
        if (adapter == null) {
            return;
        }
        int totalHeight = 0;
        final int itemCount = adapter.getCount();
        int lineCount = 0;
        if (itemCount % columnCount == 0) {
            lineCount = itemCount / columnCount;
        } else {
            lineCount = itemCount / columnCount + 1;
        }
        for (int i = 0; i < lineCount; i++) {
            int lineHeight = 0;
            for (int j = 0; j < columnCount; j++) {
                int position = (i * columnCount) + j;
                if (position > itemCount - 1) {
                    break;
                }

                View itemView = adapter.getView(position, null, myGridView);
                itemView.measure(0, 0);
                int childHeight = itemView.getMeasuredHeight();
                lineHeight = childHeight > lineHeight ? childHeight : lineHeight;
            }
            totalHeight = totalHeight + lineHeight + verticalSpacing;
        }
        ViewGroup.LayoutParams params = myGridView.getLayoutParams();
        params.height = totalHeight - verticalSpacing;
        myGridView.setLayoutParams(params);
    }
}
