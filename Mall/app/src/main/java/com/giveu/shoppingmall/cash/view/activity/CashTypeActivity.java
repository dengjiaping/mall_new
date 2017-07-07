package com.giveu.shoppingmall.cash.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
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
import com.giveu.shoppingmall.model.bean.response.CostFeeResponse;
import com.giveu.shoppingmall.model.bean.response.ProductResponse;
import com.giveu.shoppingmall.recharge.view.dialog.PwdDialog;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.DensityUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
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
    private LvCommonAdapter<ProductResponse> stagingTypeAdapter;
    double chooseQuota;//选择额度
    List<ProductResponse> data;
    String costFee;//费率
    public final int MAXAMOUNT = 3000;//最大取现金额
    public final int MINAMOUNT = 300;//最小取现金额
    private ViewGroup decorView;
    ProductResponse noStageProduct;
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
                        scaleScrollView.setCurScale((int) chooseQuota);
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

        //按日计息选项
        noStageProduct = new ProductResponse(0, 0, false, false);
        statusBarHeight = DensityUtils.getStatusBarHeight();
        decorView = (ViewGroup) getWindow().getDecorView();
        String availableCylimit = LoginHelper.getInstance().getAvailableCylimit();
        if (StringUtils.isNotNull(availableCylimit)) {
            chooseQuota = Double.parseDouble(availableCylimit);
            chooseQuota = 2000;
        }
    }

    private void initAdapter() {
        data = new ArrayList<>();
        stagingTypeAdapter = new LvCommonAdapter<ProductResponse>(mBaseContext, R.layout.tv_cash_type_item, data) {
            @Override
            protected void convert(ViewHolder viewHolder, ProductResponse item, int position) {
                TextView tvStagingType = viewHolder.getView(R.id.tv_staging_type);
                TextView tv_cost_fee = viewHolder.getView(R.id.tv_cost_fee);
                if (item.paymentNum == 0) {
                    tvStagingType.setText("按日计息");
                } else {
                    tvStagingType.setText(item.paymentNum + "期");
                }

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
                chooseQuota = Double.parseDouble(StringUtils.getTextFromView(etInputAmount));
                setData();
            }
        });
        scaleScrollView.setOnScrollListener(new HorizontalScaleScrollView.OnScrollListener() {
            @Override
            public void onScaleScroll(int scale) {
                etInputAmount.setText("" + scale * 50);
                etInputAmount.setSelection(StringUtils.getTextFromView(etInputAmount).length());
            }
        });

//        etInputAmount.addTextChangedListener(new TextChangeListener() {
//            @Override
//            public void afterTextChanged(Editable s) {
//                etInputAmount.setSelection(s.length());
//                if (StringUtils.isNotNull(s.toString())) {
//                    if (Integer.parseInt(s.toString()) == chooseQuota) {//这一次输入与上一次相同，不做操作
//                        return;
//                    }
//                    chooseQuota = Integer.parseInt(s.toString());
//                }
//            }
//        });


        gvStagingType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int checkId, long id) {
                if (stagingTypeAdapter != null) {
                    if (stagingTypeAdapter.getCount() > 0) {
                        for (int i = 0; i < stagingTypeAdapter.getCount(); i++) {
                            if (checkId == i) {
                                stagingTypeAdapter.getItem(i).isChecked = true;
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

    List<ProductResponse> productList;

    @Override
    public void setData() {

        //选择的到账金额
        //  chooseQuota = 4000;
        //   etInputAmount.setText(String.valueOf(chooseQuota));
        if (CommonUtils.isNullOrEmpty(productList)) {
            ApiImpl.initProduct(mBaseContext, LoginHelper.getInstance().getGlobleLimit(), new BaseRequestAgent.ResponseListener<ProductResponse>() {
                @Override
                public void onSuccess(ProductResponse response) {
                    productList = response.data;
                    setStageNumberData(chooseQuota);
                }

                @Override
                public void onError(BaseBean errorBean) {
                    CommonLoadingView.showErrorToast(errorBean);
                }
            });
        } else {
            setStageNumberData(chooseQuota);
        }
        if (StringUtils.isNull(costFee)) {
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
                MyBankCardActivity.startIt(mBaseContext, true);
                break;
        }

    }

    /**
     * 根据选择的额度显示不同的借款期数
     *
     * @param chooseQuota
     * @return
     */
    public void setStageNumberData(final double chooseQuota) {
        data.clear();
        stagingTypeAdapter.notifyDataSetChanged();

        if (CommonUtils.isNotNullOrEmpty(productList)) {
            for (ProductResponse product : productList) {
                if (product == null) {
                    return;
                }
                //TODO：当选择1000这个分界线时，有待确认需求
                if (chooseQuota > product.creditFrom && chooseQuota <= product.creditTo) {
                    ProductResponse p = new ProductResponse(product.paymentNum, product.idProduct, false, false);
                    data.add(p);
                }

            }
        }
        stagingTypeAdapter.setData(data);
        List<ProductResponse> products = stagingTypeAdapter.getData();
        if (chooseQuota > MAXAMOUNT) {
            //仅支持取现分期
            products.remove(0);
        } else if (chooseQuota >= MINAMOUNT && chooseQuota <= MAXAMOUNT) {
            //支持随借随还及取现分期。用户勾选随借随还时，月供、还款计划、贷款本金字段隐藏
            products.add(noStageProduct);
            products.get(products.size() - 1).isShow = true;
        } else {
            //仅支持随借随还
            products.add(noStageProduct);
            products.get(0).isShow = true;
        }
        stagingTypeAdapter.notifyDataSetChanged();
        setGridViewHeightBasedOnChildren(DensityUtils.dip2px(6), 4, gvStagingType);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            tvBankName.setText(data.getStringExtra("bankName"));
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
