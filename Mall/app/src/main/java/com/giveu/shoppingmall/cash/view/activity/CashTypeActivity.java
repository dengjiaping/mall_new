package com.giveu.shoppingmall.cash.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.cash.view.dialog.MonthlyDetailsDialog;
import com.giveu.shoppingmall.me.view.activity.AddBankCardFirstActivity;
import com.giveu.shoppingmall.me.view.activity.MyBankCardActivity;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.CashTypeResponse;
import com.giveu.shoppingmall.model.bean.response.CostFeeResponse;
import com.giveu.shoppingmall.recharge.view.dialog.PwdDialog;
import com.giveu.shoppingmall.utils.DensityUtils;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.listener.TextChangeListener;
import com.giveu.shoppingmall.widget.NoScrollGridView;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;
import com.lichfaker.scaleview.HorizontalScaleScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 取现类型页
 * Created by 101900 on 2017/6/26.
 */

public class CashTypeActivity extends BaseActivity {
    HorizontalScaleScrollView scaleScrollView;
    @BindView(R.id.et_input_amount)
    EditText etInputAmount;
    @BindView(R.id.gv_staging_type)
    NoScrollGridView gvStagingType;
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
    private LvCommonAdapter<CashTypeResponse> stagingTypeAdapter;
    int chooseQuota;//选择额度
    List<CashTypeResponse> data;
    String costFee;//费率
    public final int MAXAMOUNT = 3000;//最大取现金额
    public final int MINAMOUNT = 300;//最小取现金额
    private ViewGroup decorView;

    private int statusBarHeight;
    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
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
                    setData();
                    if (chooseQuota >= 0 && chooseQuota <= 3000 && (chooseQuota % 50 == 0)) {
                        scaleScrollView.setCurScale(chooseQuota);
                    }
                }
            }

            previousKeyboardHeight = keyboardHeight;

        }
    };

    public static void startIt(Activity mActivity) {
        Intent intent = new Intent(mActivity, CashTypeActivity.class);
        mActivity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_cash_type);
        baseLayout.setTitle("我要取现");
        scaleScrollView = (HorizontalScaleScrollView) findViewById(R.id.horizontalScale);
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
        initAdapter();
        statusBarHeight = DensityUtils.getStatusBarHeight();
        decorView = (ViewGroup) getWindow().getDecorView();
    }

    private void initAdapter() {
        data = new ArrayList<>();
        stagingTypeAdapter = new LvCommonAdapter<CashTypeResponse>(mBaseContext, R.layout.tv_cash_type_item, data) {
            @Override
            protected void convert(ViewHolder viewHolder, CashTypeResponse item, int position) {
                TextView tvStagingType = viewHolder.getView(R.id.tv_staging_type);
                TextView tv_cost_fee = viewHolder.getView(R.id.tv_cost_fee);
                tvStagingType.setText(item.month);

                if (item.isChecked) {
                    tvStagingType.setTextColor(getResources().getColor(R.color.white));
                    tvStagingType.setBackgroundResource(R.drawable.shape_ordinary_pressed);
                } else {
                    tvStagingType.setTextColor(getResources().getColor(R.color.title_color));
                    tvStagingType.setBackgroundResource(R.drawable.shape_ordinary_normal);
                }
                if (item.isShow) {
                    tv_cost_fee.setVisibility(View.VISIBLE);
                    if (StringUtils.isNotNull(costFee)) {
                        tv_cost_fee.setText("费率" + costFee + "元/天");
                    }
                } else {
                    tv_cost_fee.setVisibility(View.GONE);
                }
            }
        };
        gvStagingType.setAdapter(stagingTypeAdapter);
    }

    @Override
    public void setListener() {
        super.setListener();
        scaleScrollView.setOnMoveStopListener(new HorizontalScaleScrollView.OnMoveStopListener() {
            @Override
            public void stop() {
                setData();
            }
        });
        scaleScrollView.setOnScrollListener(new HorizontalScaleScrollView.OnScrollListener() {
            @Override
            public void onScaleScroll(int scale) {
                etInputAmount.setText("" + scale * 50);
            }
        });

        etInputAmount.addTextChangedListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                etInputAmount.setSelection(s.length());
                if (StringUtils.isNotNull(s.toString())) {
                    if (Integer.parseInt(s.toString()) == chooseQuota) {//这一次输入与上一次相同，不做操作
                        return;
                    }
                    chooseQuota = Integer.parseInt(s.toString());
                }
            }
        });


        gvStagingType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int checkId, long id) {
                ToastUtils.showShortToast("" + stagingTypeAdapter.getItem(checkId).isShow);
                if (stagingTypeAdapter != null) {
                    if (stagingTypeAdapter.getCount() > 0) {
                        for (int i = 0; i < stagingTypeAdapter.getCount(); i++) {
                            if (checkId == i) {
                                stagingTypeAdapter.getItem(checkId).isChecked = true;
                            } else {
                                stagingTypeAdapter.getItem(i).isChecked = false;
                            }
                        }
                        stagingTypeAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        decorView.getViewTreeObserver().removeGlobalOnLayoutListener(globalLayoutListener);
    }


    @Override
    public void setData() {
        //选择的到账金额
        //  chooseQuota = 4000;
        //   etInputAmount.setText(String.valueOf(chooseQuota));
        setStageNumberData(chooseQuota);
        ApiImpl.getCostFee(mBaseContext, new BaseRequestAgent.ResponseListener<CostFeeResponse>() {
            @Override
            public void onSuccess(CostFeeResponse response) {
                costFee = String.valueOf(response.data.costFee);
            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }

    @OnClick({R.id.tv_monthly_payment, R.id.rl_add_bank_card, R.id.tv_ensure_bottom, R.id.ll_choose_bank})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_monthly_payment:
                MonthlyDetailsDialog monthlyDetailsDialog = new MonthlyDetailsDialog(mBaseContext);
                monthlyDetailsDialog.showDialog();
                //查看月供
                break;
            case R.id.rl_add_bank_card:
                //添加银行卡
                AddBankCardFirstActivity.startIt(mBaseContext);
                break;
            case R.id.tv_ensure_bottom:
                //确定
                PwdDialog pwdDialog = new PwdDialog(mBaseContext, PwdDialog.statusType.CASH);
                pwdDialog.showDialog();
                break;
            case R.id.ll_choose_bank:
                //银行卡列表
                Intent intent = new Intent(mBaseContext, MyBankCardActivity.class);
                startActivityForResult(intent, 1);
                break;
        }

    }

    /**
     * 根据选择的额度显示不同的借款期数
     *
     * @param chooseQuota
     * @return
     */
    public void setStageNumberData(int chooseQuota) {
        CashTypeResponse c1 = new CashTypeResponse("按日计息", false, false);
        CashTypeResponse c2 = new CashTypeResponse("9期", false, false);
        CashTypeResponse c3 = new CashTypeResponse("12期", false, false);
        CashTypeResponse c4 = new CashTypeResponse("15期", false, false);
        CashTypeResponse c5 = new CashTypeResponse("18期", false, false);
        CashTypeResponse c6 = new CashTypeResponse("21期", false, false);
        CashTypeResponse c7 = new CashTypeResponse("24期", false, false);
        CashTypeResponse c8 = new CashTypeResponse("27期", false, false);
        CashTypeResponse c9 = new CashTypeResponse("30期", false, false);
        CashTypeResponse c10 = new CashTypeResponse("36期", false, false);
        data = new ArrayList<>();
        data.add(c1);
        data.add(c2);
        data.add(c3);
        data.add(c4);
        data.add(c5);
        data.add(c6);
        data.add(c7);
        data.add(c8);
        data.add(c9);
        data.add(c10);
        if (chooseQuota > MAXAMOUNT) {
            //仅支持取现分期
            data.remove(0);
        } else if (chooseQuota >= MINAMOUNT && chooseQuota <= MAXAMOUNT) {
            //支持随借随还及取现分期。用户勾选随借随还时，月供、还款计划、贷款本金字段隐藏
            int size = data.size() % 4;
            for (int i = data.size() - 1; i > data.size() - size - 1; i--) {
                data.get(i).isShow = true;
            }
        } else {
            //仅支持随借随还
            CashTypeResponse cashData = data.get(data.size() - 1);
            data = new ArrayList<>();
            data.add(cashData);
            data.get(0).isShow = true;
        }
        stagingTypeAdapter.setData(data);
        stagingTypeAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            tvBankName.setText(data.getStringExtra("bankName"));
        }
    }
}
