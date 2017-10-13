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
import com.giveu.shoppingmall.model.bean.response.SkuIntroductionResponse;
import com.giveu.shoppingmall.recharge.view.dialog.PaymentTypeDialog;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.Const;
import com.giveu.shoppingmall.utils.DensityUtils;
import com.giveu.shoppingmall.utils.ImageUtils;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.widget.flowlayout.FlowLayout;
import com.giveu.shoppingmall.widget.flowlayout.TagAdapter;
import com.giveu.shoppingmall.widget.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by 513419 on 2017/8/30.
 * 购买商品对话框
 */

public class BuyCommodityDialog extends CustomDialog implements View.OnClickListener {

    private LinearLayout llContainer;
    private ImageView ivDismiss;
    private TextView tvConfirm;
    private TextView tvAmounts;
    private TextView tvPrice;
    private ImageView ivReduce;
    private ImageView ivPlus;
    private TextView tvCommodityAmounts;
    private RelativeLayout rlPaymentType;
    private LinearLayout llPaymentType;
    private TextView tvPaymentType;
    private TextView tvDescription;
    private LinearLayout llCredit;
    private TextView tvCommodityName;
    private ImageView ivCommodity;
    private LinearLayout llChooseCredit;
    private TextView tvMonthAmount;
    private LinkedHashMap<String, SkuIntroductionResponse.SpecValuesBean> attrHashMap = new LinkedHashMap<>();//存储选中的属性
    private ArrayList<ArrayList<SkuIntroductionResponse.SpecValuesBean>> allAttrMap = new ArrayList<>();//所有属性
    private HashMap<String, TagFlowLayout> tagFHashMap = new HashMap<>();
    private PaymentTypeDialog paymentTypeDialog;
    private int paymentType;
    private boolean isCredit;


    public BuyCommodityDialog(Activity context) {
        super(context, R.layout.dialog_buy_commodity, R.style.customerDialog, Gravity.BOTTOM, true);
    }

    @Override
    protected void initView(View contentView) {
        Window dialogWindow = getWindow();
        if (dialogWindow != null) {
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            // 设置对话框宽度
            lp.width = DensityUtils.getWidth();
            dialogWindow.setGravity(Gravity.BOTTOM);
            dialogWindow.setWindowAnimations(R.style.dialogWindowAnim); // 添加动画
        }
        ivDismiss = (ImageView) contentView.findViewById(R.id.iv_dismiss);
        ivCommodity = (ImageView) contentView.findViewById(R.id.iv_commodity);
        tvConfirm = (TextView) contentView.findViewById(R.id.tv_confirm);
        tvMonthAmount = (TextView) contentView.findViewById(R.id.tv_monthAmount);
        tvPrice = (TextView) contentView.findViewById(R.id.tv_price);
        llChooseCredit = (LinearLayout) contentView.findViewById(R.id.ll_choose_credit);
        tvCommodityName = (TextView) contentView.findViewById(R.id.tv_commodit_name);
        llCredit = (LinearLayout) contentView.findViewById(R.id.ll_credit);
        llPaymentType = (LinearLayout) contentView.findViewById(R.id.ll_payment_type);
        ivReduce = (ImageView) contentView.findViewById(R.id.iv_reduce);
        tvAmounts = (TextView) contentView.findViewById(R.id.tv_amounts);
        tvCommodityAmounts = (TextView) contentView.findViewById(R.id.tv_commodity_amount);
        tvPaymentType = (TextView) contentView.findViewById(R.id.tv_payment_type);
        tvDescription = (TextView) contentView.findViewById(R.id.tv_description);
        rlPaymentType = (RelativeLayout) contentView.findViewById(R.id.rl_payment_type);
        ivPlus = (ImageView) contentView.findViewById(R.id.iv_plus);
        llCredit = (LinearLayout) contentView.findViewById(R.id.ll_credit);
        llContainer = (LinearLayout) contentView.findViewById(R.id.ll_container);
        ivDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        llChooseCredit.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        ivPlus.setOnClickListener(this);
        ivReduce.setOnClickListener(this);
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

    @Override
    public void show() {
/*        //额度不足时默认选择支付宝
        double availablePoslimit = StringUtils.string2Double(LoginHelper.getInstance().getAvailablePoslimit());
        if (availablePoslimit > 0) {
            paymentType = Const.WALLET;
            paymentTypeDialog.enableWalletPay();
        } else {
            paymentType = Const.ALI;
            paymentTypeDialog.disableWalletPay();
        }*/
        //这一期只能使用支付宝支付
        paymentType = Const.ALI;
        paymentTypeDialog.disableWalletPay();
        initPayType();
        super.show();
    }

    /**
     * 初始化支付方式
     */
    private void initPayType() {
        if (paymentType == Const.WALLET) {
            tvPaymentType.setText("即有钱包");
            tvDescription.setVisibility(View.VISIBLE);
        } else {
            tvPaymentType.setText("支付宝");
            tvDescription.setVisibility(View.INVISIBLE);
        }
    }


    private OnChooseCompleteListener completeListener;

    public interface OnChooseCompleteListener {
        void onComplete(String skuCode);
    }

    public void setOnChooseCompleteListener(OnChooseCompleteListener listener) {
        this.completeListener = listener;
    }

    public void setData(boolean isCredit, String skuCode, SkuIntroductionResponse response) {
        attrHashMap.clear();
        allAttrMap.clear();
        llContainer.removeAllViews();
        List<SkuIntroductionResponse.SkuSpecsBean> skuSpecs = response.skuSpecs;
        if (CommonUtils.isNullOrEmpty(skuSpecs)) {
            return;
        }
        this.isCredit = isCredit;
        //显示分期按钮还是一次性商品按钮
        initCreditView();
        //分期产品显示月供
        if (isCredit) {
            CommonUtils.setTextWithSpanSizeAndColor(tvMonthAmount, "¥",
                    StringUtils.format2(response.skuInfo.monthAmount), " 起",
                    13, 9, R.color.color_00bbc0, R.color.color_4a4a4a);
            llPaymentType.setVisibility(View.GONE);
        } else {
            //一次性产品显示支付方式
            llPaymentType.setVisibility(View.VISIBLE);
        }
        //显示商品信息，图片，名称，价格
        updateInfo(response.skuInfo.srcIp + ImageUtils.ImageSize.img_size_200_200 + response.skuInfo.src, response.skuInfo.name, response.skuInfo.salePrice);
        //存储默认选中的位置，根据skuCodes遍历查找位置
        ArrayList<Integer> defaultList = new ArrayList<>();
        for (int i = 0; i < skuSpecs.size(); i++) {
            //商品得属性（如颜色，大小），是一个类别
            SkuIntroductionResponse.SkuSpecsBean specValue = skuSpecs.get(i);
            for (int j = 0; j < specValue.specValues.size(); j++) {
                //具体的一个属性值（如金色）
                SkuIntroductionResponse.SpecValuesBean specValuesBean = specValue.specValues.get(j);
                //这个属性是否包含skuCode，是的话已查找出默认的一个位置，继续查找其他属性
                if (specValuesBean.skuCodes.contains(skuCode)) {
                    defaultList.add(j);
                    //存储选中的属性值
                    attrHashMap.put(specValue.name, specValue.specValues.get(j));
                    break;
                }
            }

        }
        for (int i = 0; i < skuSpecs.size(); i++) {
            //存储一个属性下的所有值（如颜色对应的，金色，银色，玫瑰色等）
            SkuIntroductionResponse.SkuSpecsBean skuSpecBean = skuSpecs.get(i);
            ArrayList<SkuIntroductionResponse.SpecValuesBean> attrList = new ArrayList<>();
            attrList.addAll(skuSpecBean.specValues);
            allAttrMap.add(attrList);
            //添加这个属性的流式布局
            if (i < defaultList.size()) {
                addView(defaultList.get(i), skuSpecBean.name, attrList);
            } else {
                addView(-1, skuSpecBean.name, attrList);
            }
        }
    }

    /**
     * 获取选择的属性值
     *
     * @return
     */
    public String getAttrStr() {
        String attrStr = "";
        //没有将所有属性选择完成，返回空字符串
        if (attrHashMap.size() != allAttrMap.size()) {
            return "";
        }
        //遍历得出选择的属性
        for (Map.Entry<String, SkuIntroductionResponse.SpecValuesBean> entry : attrHashMap.entrySet()) {
            attrStr += entry.getValue().specValue + " ";
        }
        return attrStr;
    }

    /**
     * 获取选中的属性对应的skuCode
     *
     * @return
     */
    public String getSkuCode() {
        String skuCode = "";
        //没有将所有属性选择完成，返回空字符串
        if (attrHashMap.size() != allAttrMap.size()) {
            return "";
        }
        ArrayList<String> filterList = new ArrayList<>();
        Iterator<Map.Entry<String, SkuIntroductionResponse.SpecValuesBean>> iterator = attrHashMap.entrySet().iterator();
        //得到选择的第一个属性所对应的skuCode
        if (iterator.hasNext()) {
            filterList.addAll(iterator.next().getValue().skuCodes);
        } else {
            return "";
        }
        for (Map.Entry<String, SkuIntroductionResponse.SpecValuesBean> entry : attrHashMap.entrySet()) {
            //取交集，最终得到唯一的skuCode
            filterList.retainAll(entry.getValue().skuCodes);
        }
        //如果filterList不为空，那么已经得到了选择完属性后对应的skuCode
        if (CommonUtils.isNotNullOrEmpty(filterList)) {
            skuCode = filterList.get(0);
        }
        return skuCode;
    }

    /**
     * 获取是否有货前，购买按钮时不允许点击的
     */
    public void setBuyDisable() {
        tvConfirm.setEnabled(false);
        llChooseCredit.setEnabled(false);
        tvConfirm.setBackgroundColor(ContextCompat.getColor(mAttachActivity, R.color.color_d8d8d8));
        llChooseCredit.setBackgroundColor(ContextCompat.getColor(mAttachActivity, R.color.color_d8d8d8));
    }

    public void setBuyEnable(int state) {
        switch (state) {
            case 0:
                tvConfirm.setText("确认");
                //可点击购买
                tvConfirm.setEnabled(true);
                llChooseCredit.setEnabled(true);
                break;

            case 1:
                tvConfirm.setText("所在地区暂时无货");
                //不可点击立即购买或在购买对话框中不可点击下一步
                tvConfirm.setEnabled(false);
                llChooseCredit.setEnabled(false);

                break;

            case 2:
                tvConfirm.setText("商品售罄");
                //不可点击立即购买或在购买对话框中不可点击下一步
                tvConfirm.setEnabled(false);
                llChooseCredit.setEnabled(false);
                break;
        }
        if (state == 0) {
            tvConfirm.setBackgroundColor(ContextCompat.getColor(mAttachActivity, R.color.color_00bbc0));
            llChooseCredit.setBackgroundColor(ContextCompat.getColor(mAttachActivity, R.color.color_00bbc0));
        } else {
            tvConfirm.setBackgroundColor(ContextCompat.getColor(mAttachActivity, R.color.color_d8d8d8));
            llChooseCredit.setBackgroundColor(ContextCompat.getColor(mAttachActivity, R.color.color_d8d8d8));
        }
    }

    @Override
    protected void createDialog() {
        super.createDialog();
        Window dialogWindow = getWindow();
        if (dialogWindow != null) {
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            // 设置对话框宽度
            lp.width = DensityUtils.getWidth();
            lp.height = (int) (DensityUtils.getHeight() * (0.76));
            dialogWindow.setAttributes(lp);
            dialogWindow.setGravity(Gravity.BOTTOM);
            dialogWindow.setWindowAnimations(R.style.dialogWindowAnim); // 添加动画
        }
    }

    /**
     * 添加流式布局
     *
     * @param defaultPos 默认选中的位置
     * @param paramsStr  对应的属性类别
     * @param paramsList 数据源
     */
    private void addView(final int defaultPos, final String paramsStr, final ArrayList<SkuIntroductionResponse.SpecValuesBean> paramsList) {
        //包含流式布局的view
        View speView = View.inflate(getContext(), R.layout.sv_specification_item, null);
        final TagFlowLayout tfParam = (TagFlowLayout) speView.findViewById(R.id.tf_specification);
        TextView tvParam = (TextView) speView.findViewById(R.id.tv_param);
        tvParam.setText(paramsStr);
        TagAdapter<SkuIntroductionResponse.SpecValuesBean> paramsAdapter = new TagAdapter<SkuIntroductionResponse.SpecValuesBean>(paramsList) {
            @Override
            public View getView(FlowLayout parent, int position, SkuIntroductionResponse.SpecValuesBean specValuesBean) {
                TextView tvTag = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.flowlayout_params_item, tfParam, false);
                tvTag.setText(specValuesBean.specValue);
                ArrayList<String> filterList = new ArrayList<>();
                //取出这个position对应的数据
                filterList.addAll(specValuesBean.skuCodes);
                //匹配选中的属性对应的skuCode，看当前view对应数据是否包含选中属性对应的skuCode
                for (Map.Entry<String, SkuIntroductionResponse.SpecValuesBean> entry : attrHashMap.entrySet()) {
                    //当前属性的排除在外
                    if (!entry.getKey().equals(paramsStr) && entry.getValue().skuCodes != null) {
                        filterList.retainAll(entry.getValue().skuCodes);
                    }
                }
                //当前view对应的数据不包含选中的skuCode
                if (CommonUtils.isNullOrEmpty(filterList)) {
                    //虚线框，不能点击
                    tvTag.setBackgroundResource(R.drawable.selector_params_disable);
                    tvTag.setTag(false);
                } else {
                    //可点击状态
                    tvTag.setBackgroundResource(R.drawable.selector_params);
                    tvTag.setTag(true);
                }
                return tvTag;
            }
        };
        //存储流式布局，以便点击的时候做notifyDataChange操作
        tagFHashMap.put(paramsStr, tfParam);
        //设置默认选中位置
        paramsAdapter.setSelectedList(defaultPos);
        tfParam.setAdapter(paramsAdapter);
        //点击某个位置的回调
        tfParam.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                boolean clickEnable = (boolean) view.getTag();
                //当前点击的view是否可点击，不是的话清空之前所有选中的位置
                if (!clickEnable) {
                    attrHashMap.clear();
                }
                //存储选中该属性的具体莫个人
                attrHashMap.put(paramsStr, paramsList.get(position));
                for (Map.Entry<String, TagFlowLayout> entry : tagFHashMap.entrySet()) {
                    //不是点中位置对应的属性
                    if (!paramsStr.equals(entry.getKey())) {
                        //可点击的view只需要重新刷新数据，否则的话清空选中位置，并重新绘制
                        if (clickEnable) {
                            entry.getValue().notifyDataSetChange();
                        } else {
                            entry.getValue().resetView();
                        }
                    } else if (!clickEnable) {
                        //如果是不可点击的，那么该属性对应的流式布局也要重新绘制
                        // 因为已经重新开始选择了，即成为第一个选中的属性
                        entry.getValue().notifyDataSetChange();
                    }

                }
                //所有属性都选完了，做一个回调
                if (attrHashMap.size() == allAttrMap.size() && completeListener != null) {
                    completeListener.onComplete(getSkuCode());
                }
                return false;
            }
        });
        tfParam.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
            }
        });
        llContainer.addView(speView);
    }

    /**
     * 更新图片url，商品名称，价格
     *
     * @param url
     * @param commodityName
     * @param price
     */
    public void updateInfo(String url, String commodityName, String price) {
        ImageUtils.loadImageWithCorner(url, R.drawable.default_img_240_240, R.drawable.default_img_240_240
                , ivCommodity, DensityUtils.dip2px(8));
        tvCommodityName.setText(StringUtils.ToAllFullWidthString(commodityName));
        CommonUtils.setTextWithSpanSizeAndColor(tvPrice, "¥", StringUtils.format2(price), "",
                19, 13, R.color.color_ff2a2a, R.color.color_999999);
    }


    /**
     * 是否分期产品
     *
     */
    public void showDialog() {
        initCreditView();
        show();
    }

    private void initCreditView(){
        if (isCredit) {
            //分期购买应显示的内容
            llCredit.setVisibility(View.VISIBLE);
            tvConfirm.setVisibility(View.GONE);
        } else {
            //一次性购买应显示的内容
            llCredit.setVisibility(View.GONE);
            tvConfirm.setVisibility(View.VISIBLE);
        }
    }

    private OnConfirmListener listener;

    public void setOnConfirmListener(OnConfirmListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_choose_credit:
            case R.id.tv_confirm:
                if (listener != null) {
                    if (StringUtils.isNotNull(getSkuCode())) {
                        String amountsStr = tvAmounts.getText().toString();
                        int amounts = StringUtils.string2Int(amountsStr);
                        listener.confirm(amounts,paymentType);
                        dismiss();
                    } else {
                        ToastUtils.showShortToast("请完成属性选择");
                    }
                }
                break;

            case R.id.iv_reduce:
                String amountsStr = tvAmounts.getText().toString();
                int amounts = StringUtils.string2Int(amountsStr);
                if (amounts > 1) {
                    tvAmounts.setText((amounts - 1) + "");
                    tvCommodityAmounts.setText("x " + (amounts - 1));
                }
                break;

            case R.id.iv_plus:
                amountsStr = tvAmounts.getText().toString();
                amounts = StringUtils.string2Int(amountsStr);
                tvAmounts.setText((amounts + 1) + "");
                tvCommodityAmounts.setText("x " + (amounts + 1));
                break;
        }
    }

    public interface OnConfirmListener {
        void confirm(int amounts,int paymentType);

        void cancle();
    }

}
