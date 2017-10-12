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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.model.bean.response.DownPayMonthPayResponse;
import com.giveu.shoppingmall.model.bean.response.GoodsInfoResponse;
import com.giveu.shoppingmall.recharge.view.dialog.PaymentTypeDialog;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.Const;
import com.giveu.shoppingmall.utils.DensityUtils;
import com.giveu.shoppingmall.utils.ImageUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
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
    private TextView tvPayPrice;
    private TextView tvDetail;
    private LinearLayout llAliPay;
    private RelativeLayout rlTotal;
    private LinearLayout llMonthlyPay;
    private RelativeLayout rlPaymentType;
    private TextView tvPaymentType;
    private TextView tvDescription;
    private TextView tvTotalPrice;
    private TextView tvCommodityAmounts;
    private TextView tvCommodityName;
    private TextView tvMonthSupply;
    private TextView tvDownPayment;
    private ImageView ivCommodity;
    private TagAdapter<GoodsInfoResponse> downPayAdapter;
    private TagFlowLayout downPayLayout;
    private int downPayRate = -1;//首付比例
    private int paymentNum = -1;//分期数
    private int commodityAmounts;//商品数量
    private TagAdapter<DownPayMonthPayResponse> monthPayAdapter;
    private TagFlowLayout tfPayment;
    private boolean hasInitDownPaymentRate;

    private PaymentTypeDialog paymentTypeDialog;
    private int paymentType;

    private double totalPrice;//商品总价格 = 单价 x 数量
    private long idProduct;

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
                if (paymentNum != -1 && downPayRate != -1) {
                    listener.confirm(downPayRate, idProduct, paymentType);
                }
            }
        });
        ivCommodity = (ImageView) contentView.findViewById(R.id.iv_commodity);
        tvPrice = (TextView) contentView.findViewById(R.id.tv_price);
        tvPayPrice = (TextView) contentView.findViewById(R.id.tv_pay_price);
        rlTotal = (RelativeLayout) contentView.findViewById(R.id.rl_total);
        llAliPay = (LinearLayout) contentView.findViewById(R.id.ll_ali_pay);
        llMonthlyPay = (LinearLayout) contentView.findViewById(R.id.ll_monthly_pay);
        tvPaymentType = (TextView) contentView.findViewById(R.id.tv_payment_type);
        tvDescription = (TextView) contentView.findViewById(R.id.tv_description);
        rlPaymentType = (RelativeLayout) contentView.findViewById(R.id.rl_payment_type);
        tvDetail = (TextView) contentView.findViewById(R.id.tv_detail);
        tvTotalPrice = (TextView) contentView.findViewById(R.id.tv_total_price);
        tvCommodityName = (TextView) contentView.findViewById(R.id.tv_commodit_name);
        tvDownPayment = (TextView) contentView.findViewById(R.id.tv_down_payment);
        tvMonthSupply = (TextView) contentView.findViewById(R.id.tv_month_supply);
        tvCommodityAmounts = (TextView) contentView.findViewById(R.id.tv_commodity_amount);
        tvDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDownPayChangeListener != null) {
                    onDownPayChangeListener.showAppMonthlySupply(downPayRate, idProduct, commodityAmounts);
                }
            }
        });

        paymentTypeDialog = new PaymentTypeDialog(mAttachActivity);
        paymentTypeDialog.setOnChoosePayTypeListener(new PaymentTypeDialog.OnChoosePayTypeListener() {
            @Override
            public void onChooseType(int type, String paymentTypeStr) {
                paymentType = type;
                initPayType();
            }
        });
        rlPaymentType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentTypeDialog.showDialog(paymentType);
            }
        });

    }

    /**
     * 是否有初始化首付比例
     *
     * @return
     */
    public boolean hasInitSuccess() {
        return hasInitDownPaymentRate;
    }

    public void initDownPaymentRate(ArrayList<GoodsInfoResponse> percentList) {
        llContainer.removeAllViews();
        hasInitDownPaymentRate = true;
        addDownPayView("首付", percentList);
        addPaymentNumView("分期数", new ArrayList<DownPayMonthPayResponse>());
    }

    /**
     * 设置是否可点击分期，服务器未返回数据前是不可点击的
     *
     * @param enable
     */
    public void setConfirmEnable(boolean enable) {
        tvConfirm.setEnabled(enable);
        tvDetail.setEnabled(enable);
        if (enable) {
            tvConfirm.setBackgroundColor(ContextCompat.getColor(mAttachActivity, R.color.color_00bbc0));
        } else {
            tvConfirm.setBackgroundColor(ContextCompat.getColor(mAttachActivity, R.color.color_d8d8d8));
        }
    }

    public int getDownPayRate() {
        return downPayRate == -1 ? 0 : downPayRate;
    }

    /**
     * 初始化支付方式
     */
    private void initPayType() {
        if (paymentType == Const.WALLET) {
            tvPaymentType.setText("即有钱包");
            tvDescription.setVisibility(View.VISIBLE);
            llContainer.setVisibility(View.VISIBLE);
            llAliPay.setVisibility(View.GONE);
            llMonthlyPay.setVisibility(View.VISIBLE);
            rlTotal.setVisibility(View.VISIBLE);
        } else {
            tvPaymentType.setText("支付宝");
            tvDescription.setVisibility(View.INVISIBLE);
            llContainer.setVisibility(View.GONE);
            CommonUtils.setTextWithSpanSizeAndColor(tvPayPrice, "¥ ", StringUtils.format2(totalPrice + ""), "",
                    16, 11, R.color.title_color, R.color.black);
            llAliPay.setVisibility(View.VISIBLE);
            llMonthlyPay.setVisibility(View.GONE);
            rlTotal.setVisibility(View.INVISIBLE);
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
        //默认是没有选中的，那么选第一个位置即零首付
        if (downPayRate == -1) {
            downPayRate = 0;
        }
        this.commodityAmounts = commodityAmounts;
        updateInfo(smallIconStr, commodityName, commodityPrice, commodityAmounts);
        if (CommonUtils.isNotNullOrEmpty(data)) {
            //之前没选过期数
            if (paymentNum == -1) {
                monthPayAdapter.setSelectedList(data.size() - 1);
                DownPayMonthPayResponse downPayMonthPayResponse = data.get(data.size() - 1);
                paymentNum = downPayMonthPayResponse.paymentNum;
                idProduct = StringUtils.string2Long(downPayMonthPayResponse.idProduct);
                initdownPayMonthPay((downPayRate * totalPrice / 100) + "", StringUtils.string2Double(downPayMonthPayResponse.annuity + "") + "");
            } else {
                //已选过期数和现在服务器返回的数据是否还有这个期数的标识
                boolean hasSamePaymentNum = false;
                for (int i = 0; i < data.size(); i++) {
                    DownPayMonthPayResponse downPayMonthPayResponse = data.get(i);
                    //之前选的期数并且现在返回的期数还有这个期数，那么默认还是之前的期数，否则默认选择第一个期数
                    if (paymentNum == downPayMonthPayResponse.paymentNum) {
                        hasSamePaymentNum = true;
                        monthPayAdapter.setSelectedList(i);
                        initdownPayMonthPay((downPayRate * totalPrice / 100) + "", StringUtils.string2Double(downPayMonthPayResponse.annuity + "") + "");
                        break;
                    }
                }
                //之前选的期数并且现在返回的期数还有这个期数，那么默认还是之前的期数，否则默认选择第一个期数
                if (!hasSamePaymentNum) {
                    monthPayAdapter.setSelectedList(data.size() - 1);
                    DownPayMonthPayResponse downPayMonthPayResponse = data.get(data.size() - 1);
                    paymentNum = downPayMonthPayResponse.paymentNum;
                    idProduct = StringUtils.string2Long(downPayMonthPayResponse.idProduct);
                    initdownPayMonthPay((downPayRate * totalPrice / 100) + "", StringUtils.string2Double(downPayMonthPayResponse.annuity + "") + "");
                }

            }
            monthPayAdapter.setDatas(data);
            setConfirmEnable(true);
        } else {
            monthPayAdapter.setDatas(new ArrayList<DownPayMonthPayResponse>());
            initdownPayMonthPay("0", "");
        }
    }

    @Override
    public void show() {
        //额度不足时默认选择支付宝
        double availablePoslimit = StringUtils.string2Double(LoginHelper.getInstance().getAvailablePoslimit());
        if (availablePoslimit > 0) {
            paymentType = Const.WALLET;
            paymentTypeDialog.enableWalletPay();
        } else {
            paymentType = Const.ALI;
            paymentTypeDialog.disableWalletPay();
        }
        initPayType();
        super.show();
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
    private void addDownPayView(String paramsStr, final ArrayList<GoodsInfoResponse> paramsList) {
        View speView = View.inflate(getContext(), R.layout.sv_specification_item, null);
        TextView tvParam;
        downPayLayout = (TagFlowLayout) speView.findViewById(R.id.tf_specification);
        tvParam = (TextView) speView.findViewById(R.id.tv_param);
        tvParam.setText(paramsStr);
        downPayAdapter = new TagAdapter<GoodsInfoResponse>(paramsList) {
            @Override
            public View getView(FlowLayout parent, int position, GoodsInfoResponse item) {
                TextView tvTag = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.flowlayout_params_item, downPayLayout, false);
                tvTag.setText(item.name);
                return tvTag;
            }
        };
        for (int i = 0; i < paramsList.size(); i++) {
            GoodsInfoResponse goodsInfoResponse = paramsList.get(i);
            if (goodsInfoResponse.isSelect == 1) {
                downPayAdapter.setSelectedList(i);
                downPayRate = goodsInfoResponse.id;
                break;
            }
        }
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
                    if (integer < downPayAdapter.getDatas().size()) {
                        //首付比例赋值
                        downPayRate = downPayAdapter.getDatas().get(integer).id;
                        tvDownPayment.setText("首付 " + StringUtils.format2((downPayRate * totalPrice / 100) + "") + "元");
                    }
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
                    idProduct = StringUtils.string2Long(downPayMonthPayResponse.idProduct);
                    initdownPayMonthPay((downPayRate * totalPrice / 100) + "", StringUtils.string2Double(downPayMonthPayResponse.annuity + "") + "");
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
        ImageUtils.loadImageWithCorner(url, R.drawable.default_img_240_240, R.drawable.default_img_240_240
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
        void confirm(int downPayRate, long idProduct, int paymentType);

        void cancle();
    }


    private OnDownPayChangeListener onDownPayChangeListener;

    public interface OnDownPayChangeListener {
        void onChange(int downPayRate);

        void showAppMonthlySupply(int downPaymentRate, long idProduct, int quantity);
    }

    public void setOnDownPayChangeListener(OnDownPayChangeListener listener) {
        this.onDownPayChangeListener = listener;
    }

}
