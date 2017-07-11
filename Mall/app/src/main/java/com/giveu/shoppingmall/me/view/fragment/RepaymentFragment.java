package com.giveu.shoppingmall.me.view.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.base.BaseFragment;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.me.adapter.RepaymentAdapter;
import com.giveu.shoppingmall.me.presenter.InstalmentDetailsPresenter;
import com.giveu.shoppingmall.me.view.activity.RepaymentActivity;
import com.giveu.shoppingmall.me.view.agent.IInstalmentDetailsView;
import com.giveu.shoppingmall.me.view.dialog.IntalmentDetailsDialog;
import com.giveu.shoppingmall.me.view.dialog.RepaymentDetailDialog;
import com.giveu.shoppingmall.me.view.dialog.RepaymentDialog;
import com.giveu.shoppingmall.model.bean.response.RepaymentResponse;
import com.giveu.shoppingmall.model.bean.response.InstalmentDetailResponse;
import com.giveu.shoppingmall.model.bean.response.RepaymentBean;
import com.giveu.shoppingmall.model.bean.response.WxPayParamsResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.HardWareUtil;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.PayUtils;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.TypeUtlis;
import com.giveu.shoppingmall.widget.dialog.OnlyConfirmDialog;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshBase;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshListView;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 513419 on 2017/6/22.
 */

public class RepaymentFragment extends BaseFragment implements IInstalmentDetailsView {

    @BindView(R.id.ptrlv)
    PullToRefreshListView ptrlv;
    private RepaymentAdapter repaymentAdapter;
    private ArrayList<RepaymentBean> billList;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    private RepaymentDialog repaymentDialog;
    private RepaymentDetailDialog repaymentDetailDialog;
    private boolean isCurrentMonth;
    private RepaymentActivity mActivity;
    private ViewHolder headerHolder;
    private double payMoney;
    private double cycleTotalAmount;//零花钱总欠款
    private double othersTotalAmount;//分期产品总欠款
    private InstalmentDetailsPresenter presenter;
    private IntalmentDetailsDialog intalmentDetailsDialog; //还款明细对话框
    private OnlyConfirmDialog resultDialog;//错误信息提示框
    private String productType;
    private String payId;


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill_list, null);
        ButterKnife.bind(this, view);
        baseLayout.setTitleBarAndStatusBar(false, false);
        mActivity = (RepaymentActivity) mBaseContext;
        View headerView = inflater.inflate(R.layout.lv_bill_header, null);
        headerHolder = new ViewHolder(headerView);
        isCurrentMonth = getArguments().getBoolean("isCurrentMonth", true);
        //根据是否当前月份，显示状态
        if (isCurrentMonth) {
            headerHolder.tvCurrentMonth.setTextColor(ContextCompat.getColor(mBaseContext, R.color.color_00bbc0));
            headerHolder.viewCurrent.setVisibility(View.VISIBLE);
            headerHolder.tvNextMonth.setTextColor(ContextCompat.getColor(mBaseContext, R.color.color_9b9b9b));
            headerHolder.viewNext.setVisibility(View.GONE);
        } else {
            headerHolder.tvCurrentMonth.setTextColor(ContextCompat.getColor(mBaseContext, R.color.color_9b9b9b));
            headerHolder.viewCurrent.setVisibility(View.GONE);
            headerHolder.tvNextMonth.setTextColor(ContextCompat.getColor(mBaseContext, R.color.color_00bbc0));
            headerHolder.viewNext.setVisibility(View.VISIBLE);
        }
        billList = new ArrayList<>();
        repaymentAdapter = new RepaymentAdapter(mBaseContext, billList);
        ptrlv.setAdapter(repaymentAdapter);
        ptrlv.setMode(PullToRefreshBase.Mode.DISABLED);
        ptrlv.setPullLoadEnable(false);
        ptrlv.getRefreshableView().addHeaderView(headerView);
        intalmentDetailsDialog = new IntalmentDetailsDialog(mBaseContext);
        presenter = new InstalmentDetailsPresenter(this);
        repaymentDialog = new RepaymentDialog(mBaseContext);
        repaymentDetailDialog = new RepaymentDetailDialog(mBaseContext);
        resultDialog = new OnlyConfirmDialog(mBaseContext);
        return view;
    }


    @OnClick({R.id.iv_change_money, R.id.tv_confirm})
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.iv_change_money:
                if (canClick()) {
                    repaymentDialog.show();
                }
                break;

            case R.id.tv_confirm:
                if (canClick()) {
                    //判断选中的是什么类型，再判断最大可还金额,并给出提示
                    for (RepaymentBean repaymentBean : billList) {
                        //分期产品
                        if (repaymentBean.isChoose && TypeUtlis.CERDIT_PRODUCT.equalsIgnoreCase(repaymentBean.productType)) {
                            if (payMoney > othersTotalAmount) {
                                SpannableString colorStr = StringUtils.getColorSpannable("还款金额不能大于分期产品剩余待还期款总额", "¥" + StringUtils.format2(othersTotalAmount + ""), R.color.color_4a4a4a, R.color.color_00adb2);
                                resultDialog.setContent(colorStr);
                                resultDialog.show();
                                return;
                            }
                            break;
                        } else if (repaymentBean.isChoose && TypeUtlis.CASH.equalsIgnoreCase(repaymentBean.productType)) {
                            //零花钱产品
                            if (payMoney > cycleTotalAmount) {
                                SpannableString colorStr = StringUtils.getColorSpannable("还款金额不能大于零花钱剩余待还期款总额", "¥" + StringUtils.format2(cycleTotalAmount + ""), R.color.color_4a4a4a, R.color.color_00adb2);
                                resultDialog.setContent(colorStr);
                                resultDialog.show();
                                return;
                            }
                            break;
                        }
                    }
                    presenter.createRepaymentOrder(LoginHelper.getInstance().getIdPerson(), (long) (payMoney * 100), HardWareUtil.getHostIP(), PayUtils.WX, productType);
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void initDataDelay() {

    }

    public boolean canClick() {
        if (payMoney <= 0) {
            tvConfirm.setBackgroundResource(R.drawable.shape_grey_without_corner);
            return false;
        } else {
            tvConfirm.setBackgroundResource(R.drawable.shape_blue_without_corner);
            return true;
        }
    }

    @Override
    protected BasePresenter[] initPresenters() {
        return new BasePresenter[]{presenter};
    }

    @Override
    protected void setListener() {

        repaymentDialog.setOnConfirmListener(new RepaymentDialog.OnConfirmListener() {
            @Override
            public void onConfirm(String money) {
                tvMoney.setText("还款金额：¥" + StringUtils.format2(money));
                payMoney = Double.parseDouble(money);
                canClick();
            }

        });

        headerHolder.tvCurrentMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.setCurrentItem(0);
            }
        });

        headerHolder.tvNextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.setCurrentItem(1);
            }
        });

        repaymentAdapter.setOnMoneyChangetListener(new RepaymentAdapter.OnMoneyChangeListener() {
            @Override
            public void moneyChange(double money, String productType) {
                payMoney = money;
                tvMoney.setText("还款金额：¥" + StringUtils.format2(payMoney + ""));
                RepaymentFragment.this.productType = productType;
                canClick();
            }
        });

        ptrlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position - 2 >= 0 && position - 2 < billList.size()) {
                    if (!billList.get(position - 2).isTitle) {
                        RepaymentBean repaymentBean = billList.get(position - 2);
                        presenter.getInstalmentDetails(repaymentBean.contractId, isCurrentMonth, repaymentBean.numInstalment, repaymentBean.productType, repaymentBean.creditType);
                    }
                }

            }
        });

    }

    public void notifyDataSetChange(RepaymentResponse.HeaderBean headerBean, ArrayList<RepaymentBean> billBeenList) {
        tvMoney.setText("还款金额：¥" + StringUtils.format2(0 + ""));
        payMoney = 0;
        tvConfirm.setBackgroundResource(R.drawable.shape_grey_without_corner);
        if (headerBean != null) {
            cycleTotalAmount = headerBean.cycleTotalAmount;
            othersTotalAmount = headerBean.othersTotalAmount;
            headerHolder.tvTotal.setText("¥" + headerBean.repayAmount);
            if (StringUtils.isNotNull(headerBean.endDate)) {
                headerHolder.tvDate.setText("最后还款日：" + headerBean.endDate);
            } else {
                headerHolder.tvDate.setText("最后还款日：--");
            }
            if (headerBean.isOverduce) {
                headerHolder.ivOverDue.setVisibility(View.VISIBLE);
            } else {
                headerHolder.ivOverDue.setVisibility(View.GONE);
            }
        }
        if (CommonUtils.isNotNullOrEmpty(billBeenList)) {
            billList.clear();
            billList.addAll(billBeenList);
            repaymentAdapter.notifyDataSetChanged();
            baseLayout.hideEmpty();
        } else {
            baseLayout.showEmpty(144, 62, "抱歉，没有账单哦");
        }
    }

    @Override
    public void showInstalmentDetails(InstalmentDetailResponse data, String creditType) {
        intalmentDetailsDialog.setInstalmentDetailsData(data, creditType);
        intalmentDetailsDialog.show();
    }

    @Override
    public void createOrderSuccess(final WxPayParamsResponse response, String payId) {
        this.payId = payId;
        //还款金额
        if (TypeUtlis.CERDIT_PRODUCT.equalsIgnoreCase(productType)) {
            repaymentDetailDialog.setProductAndMoney(TypeUtlis.getProductType(productType), StringUtils.format2(othersTotalAmount + ""));
        } else if (TypeUtlis.CASH.equalsIgnoreCase(productType)) {
            repaymentDetailDialog.setProductAndMoney(TypeUtlis.getProductType(productType), StringUtils.format2(cycleTotalAmount + ""));
        }
        //实际支付
        repaymentDetailDialog.setPayStr(StringUtils.format2(payMoney + ""));
        repaymentDetailDialog.setOnConfirmListener(new RepaymentDetailDialog.OnConfirmListener() {
            @Override
            public void onConfirm() {
                IWXAPI iWxapi = PayUtils.getWxApi();
                PayReq payReq = PayUtils.getRayReq(response.partnerid, response.prepayid, response.packageValue,
                        response.noncestr, response.timestamp, response.sign);
                iWxapi.sendReq(payReq);
                BaseApplication.getInstance().setBeforePayActivity(mBaseContext.getClass().getSimpleName());
            }
        });
        repaymentDetailDialog.show();

    }

    @Override
    public void createOrderFailed(String message) {
        if (StringUtils.isNotNull(message)) {
            resultDialog.setContent(message);
            resultDialog.show();
        }
    }

    public void payQuery() {
        if (StringUtils.isNotNull(payId)) {
            presenter.payQuery(payId);
        }
        //查询后payId置空，使查询时其他fragement，此fragment不执行查询
        payId = "";
    }

    @Override
    public void paySuccess() {
        ToastUtils.showShortToast("还款成功");
        //支付成功后刷新还款数据,需更新额度，因此更新个人信息
        BaseApplication.getInstance().fetchUserInfo();
        mActivity.setData();
    }


    static class ViewHolder {
        @BindView(R.id.tv_total)
        TextView tvTotal;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_current_month)
        TextView tvCurrentMonth;
        @BindView(R.id.view_current)
        View viewCurrent;
        @BindView(R.id.tv_next_month)
        TextView tvNextMonth;
        @BindView(R.id.view_next)
        View viewNext;
        @BindView(R.id.iv_overdue)
        ImageView ivOverDue;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
