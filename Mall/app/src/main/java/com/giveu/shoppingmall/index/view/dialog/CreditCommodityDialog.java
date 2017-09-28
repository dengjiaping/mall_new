package com.giveu.shoppingmall.index.view.dialog;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.model.bean.response.DownPayMonthPayResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.DensityUtils;
import com.giveu.shoppingmall.utils.ImageUtils;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.widget.flowlayout.FlowLayout;
import com.giveu.shoppingmall.widget.flowlayout.TagAdapter;
import com.giveu.shoppingmall.widget.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by 513419 on 2017/9/1.
 */

public class CreditCommodityDialog extends CustomDialog {
    private LinearLayout llContainer;
    private ImageView ivDismiss;
    private TextView tvConfirm;
    private TextView tvPrice;
    private TextView tvDetail;
    private TextView tvTotalPrice;
    private TextView tvCommodityAmounts;
    private TextView tvCommodityName;
    private TextView tvMonthSupply;
    private TextView tvDownPayment;
    private ImageView ivCommodity;
    private TagAdapter<String> downPayAdapter;
    private TagFlowLayout downPayLayout;
    private int downPayRate = -1;//首付比例
    private int paymentNum = -1;//分期数
    private int commodityAmounts;//商品数量
    private TagAdapter<DownPayMonthPayResponse> monthPayAdapter;
    private TagFlowLayout tfPayment;
    private double totalPrice;//商品总价格 = 单价 x 数量

    public CreditCommodityDialog(Activity context) {
        super(context, R.layout.dialog_credit_commodity, R.style.customerDialog, Gravity.BOTTOM, true);
    }

    @Override
    protected void initView(View contentView) {
        ivDismiss = (ImageView) contentView.findViewById(R.id.iv_dismiss);
        llContainer = (LinearLayout) contentView.findViewById(R.id.ll_container);
        ivDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvConfirm = (TextView) contentView.findViewById(R.id.dialog_confirm);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.confirm();
            }
        });
        ivCommodity = (ImageView) contentView.findViewById(R.id.iv_commodity);
        tvPrice = (TextView) contentView.findViewById(R.id.tv_price);
        tvDetail = (TextView) contentView.findViewById(R.id.tv_detail);
        tvTotalPrice = (TextView) contentView.findViewById(R.id.tv_total_price);
        tvCommodityName = (TextView) contentView.findViewById(R.id.tv_commodit_name);
        tvDownPayment = (TextView) contentView.findViewById(R.id.tv_down_payment);
        tvMonthSupply = (TextView) contentView.findViewById(R.id.tv_month_supply);
        tvCommodityAmounts = (TextView) contentView.findViewById(R.id.tv_commodity_amount);
        tvDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShortToast("查看月供详情");
            }
        });
        ArrayList<String> percentList = new ArrayList<>();
        percentList.add("零首付");
        percentList.add("5%");
        percentList.add("10%");
        percentList.add("50%");
        addDownPayView("首付", percentList);
        addPaymentNumView("分期数", new ArrayList<DownPayMonthPayResponse>());
    }

    /**
     * 设置是否可点击分期，服务器未返回数据前是不可点击的
     * @param enable
     */
    public void setConfirmEnable(boolean enable) {
        tvConfirm.setEnabled(enable);
        if (enable) {
            tvConfirm.setBackgroundColor(ContextCompat.getColor(mAttachActivity, R.color.color_00bbc0));
        } else {
            tvConfirm.setBackgroundColor(ContextCompat.getColor(mAttachActivity, R.color.color_d8d8d8));
        }
    }

    /**
     * 初始化数据
     *
     * @param commodityAmounts
     * @param smallIconStr
     * @param commodityName
     * @param commodityPrice
     * @param data
     */
    public void initData(int commodityAmounts, String smallIconStr, String commodityName, String commodityPrice, ArrayList<DownPayMonthPayResponse> data) {
        if (downPayRate == -1) {
            downPayRate = 0;
        }
        this.commodityAmounts = commodityAmounts;
        updateInfo(smallIconStr, commodityName, commodityPrice, commodityAmounts);
        if (CommonUtils.isNotNullOrEmpty(data)) {
            //之前没选过期数
            if (paymentNum == -1) {
                monthPayAdapter.setSelectedList(0);
                DownPayMonthPayResponse downPayMonthPayResponse = data.get(0);
                paymentNum = downPayMonthPayResponse.paymentNum;
                initdownPayMonthPay((downPayRate * totalPrice / 100) + "", StringUtils.string2Double(downPayMonthPayResponse.annuity + "") * commodityAmounts + "");
            } else {
                //已选过期数和现在服务器返回的数据是否还有这个期数的标识
                boolean hasSamePaymentNum = false;
                for (int i = 0; i < data.size(); i++) {
                    DownPayMonthPayResponse downPayMonthPayResponse = data.get(i);
                    //之前选的期数并且现在返回的期数还有这个期数，那么默认还是之前的期数，否则默认选择第一个期数
                    if (paymentNum == downPayMonthPayResponse.paymentNum) {
                        hasSamePaymentNum = true;
                        monthPayAdapter.setSelectedList(i);
                        initdownPayMonthPay((downPayRate * totalPrice / 100) + "", StringUtils.string2Double(downPayMonthPayResponse.annuity + "") * commodityAmounts + "");
                        break;
                    }
                }
                //之前选的期数并且现在返回的期数还有这个期数，那么默认还是之前的期数，否则默认选择第一个期数
                if (!hasSamePaymentNum) {
                    monthPayAdapter.setSelectedList(0);
                    DownPayMonthPayResponse downPayMonthPayResponse = data.get(0);
                    paymentNum = downPayMonthPayResponse.paymentNum;
                    initdownPayMonthPay((downPayRate * totalPrice / 100) + "", StringUtils.string2Double(downPayMonthPayResponse.annuity + "") * commodityAmounts + "");
                }

            }
            monthPayAdapter.setDatas(data);
            setConfirmEnable(true);
        }
    }

    /**
     * 显示首付信息和月供信息
     *
     * @param initPay
     * @param annuity
     */
    private void initdownPayMonthPay(String initPay, String annuity) {
        tvDownPayment.setText("首付 " + StringUtils.format2(initPay) + "元");
        CommonUtils.setTextWithSpanSizeAndColor(tvMonthSupply, "¥", StringUtils.format2(annuity), "",
                15, 11, R.color.color_00bbc0, R.color.color_4a4a4a);
    }

    @Override
    protected void createDialog() {
        super.createDialog();
        Window dialogWindow = getWindow();
        if (dialogWindow != null) {
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            // 设置对话框宽高
            lp.width = DensityUtils.getWidth();
            lp.height = (int) (DensityUtils.getHeight() * (0.76));
            dialogWindow.setAttributes(lp);
            dialogWindow.setGravity(Gravity.BOTTOM);
            dialogWindow.setWindowAnimations(R.style.dialogWindowAnim); // 添加动画
        }
    }

    /**
     * 首付比例流式布局
     *
     * @param paramsStr
     * @param paramsList
     */
    private void addDownPayView(String paramsStr, final ArrayList<String> paramsList) {
        View speView = View.inflate(getContext(), R.layout.sv_specification_item, null);
        TextView tvParam;
        downPayLayout = (TagFlowLayout) speView.findViewById(R.id.tf_specification);
        tvParam = (TextView) speView.findViewById(R.id.tv_param);
        tvParam.setText(paramsStr);
        downPayAdapter = new TagAdapter<String>(paramsList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tvTag = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.flowlayout_params_item, downPayLayout, false);
                tvTag.setText(s);
                return tvTag;
            }
        };
        downPayAdapter.setSelectedList(0);
        downPayLayout.setAdapter(downPayAdapter);
        downPayLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                //首付比例已更改，需要从服务器重新获取期数，这个在activity中完成
                if (onDownPayChangeListener != null) {
                    onDownPayChangeListener.onChange(downPayRate);
                }
                return false;
            }
        });
        downPayLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                for (Integer integer : selectPosSet) {
                    //首付比例赋值
                    switch (integer) {
                        case 0:
                            downPayRate = 0;
                            break;
                        case 1:
                            downPayRate = 5;
                            break;
                        case 2:
                            downPayRate = 10;
                            break;
                        case 3:
                            downPayRate = 50;
                            break;
                    }
                    tvDownPayment.setText("首付 " + StringUtils.format2((downPayRate * totalPrice / 100) + "") + "元");
                }
            }
        });
        llContainer.addView(speView);
    }

    /**
     * 分期数流式布局
     *
     * @param paramsStr
     * @param paramsList
     */
    private void addPaymentNumView(String paramsStr, final ArrayList<DownPayMonthPayResponse> paramsList) {
        View speView = View.inflate(getContext(), R.layout.sv_specification_item, null);
        TextView tvParam;
        tfPayment = (TagFlowLayout) speView.findViewById(R.id.tf_specification);
        tvParam = (TextView) speView.findViewById(R.id.tv_param);
        tvParam.setText(paramsStr);
        monthPayAdapter = new TagAdapter<DownPayMonthPayResponse>(paramsList) {
            @Override
            public View getView(FlowLayout parent, int position, DownPayMonthPayResponse item) {
                TextView tvTag = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.flowlayout_params_item, tfPayment, false);
                tvTag.setText(item.paymentNum + "期");
                return tvTag;
            }
        };
        tfPayment.setAdapter(monthPayAdapter);
        tfPayment.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                //分期数赋值
                paymentNum = monthPayAdapter.getDatas().get(position).paymentNum;
                return false;
            }
        });
        tfPayment.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                for (Integer integer : selectPosSet) {
                    //显示月供金额
                    DownPayMonthPayResponse downPayMonthPayResponse = monthPayAdapter.getDatas().get(integer);
                    initdownPayMonthPay((downPayRate * totalPrice / 100) + "", StringUtils.string2Double(downPayMonthPayResponse.annuity + "") * commodityAmounts + "");
                }
            }
        });
        llContainer.addView(speView);
    }

    /**
     * 更新图片url，商品名称，价格, 商品数量
     *
     * @param url
     * @param commodityName
     * @param price
     * @param commodityAmounts
     */
    public void updateInfo(String url, String commodityName, String price, int commodityAmounts) {
        ImageUtils.loadImageWithCorner(url, R.drawable.ic_defalut_pic_corner, R.drawable.ic_defalut_pic_corner
                , ivCommodity, DensityUtils.dip2px(8));
        tvCommodityName.setText(commodityName);
        tvCommodityAmounts.setText("x " + commodityAmounts + "");
        CommonUtils.setTextWithSpanSizeAndColor(tvPrice, "¥", StringUtils.format2(price), "",
                19, 13, R.color.color_ff2a2a, R.color.color_999999);
        totalPrice = StringUtils.string2Double(price) * commodityAmounts;
        CommonUtils.setTextWithSpanSizeAndColor(tvTotalPrice, "¥", StringUtils.format2(totalPrice + ""), "",
                19, 13, R.color.color_ff2a2a, R.color.color_999999);
    }

    private OnConfirmListener listener;

    public void setOnConfirmListener(OnConfirmListener listener) {
        this.listener = listener;
    }

    public interface OnConfirmListener {
        void confirm();

        void cancle();
    }


    private OnDownPayChangeListener onDownPayChangeListener;

    public interface OnDownPayChangeListener {
        void onChange(int downPayRate);
    }

    public void setOnDownPayChangeListener(OnDownPayChangeListener listener) {
        this.onDownPayChangeListener = listener;
    }

}
