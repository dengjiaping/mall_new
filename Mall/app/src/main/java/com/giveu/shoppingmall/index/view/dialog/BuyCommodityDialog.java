package com.giveu.shoppingmall.index.view.dialog;

import android.app.Activity;
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
import com.giveu.shoppingmall.model.bean.response.SkuIntroductionResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.DensityUtils;
import com.giveu.shoppingmall.utils.ImageUtils;
import com.giveu.shoppingmall.utils.StringUtils;
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
    private LinearLayout llCredit;
    private TextView tvCommodityName;
    private ImageView ivCommodity;
    private LinearLayout llChooseCredit;
    private LinkedHashMap<String, SkuIntroductionResponse.SpecValuesBean> attrHashMap;//存储选中的属性
    private ArrayList<ArrayList<String>> filterList;
    private ArrayList<ArrayList<SkuIntroductionResponse.SpecValuesBean>> allAttrMap;
    private HashMap<String, TagAdapter> tagHashMap = new HashMap<>();
    private HashMap<String, TagFlowLayout> tagFHashMap = new HashMap<>();

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
        tvPrice = (TextView) contentView.findViewById(R.id.tv_price);
        llChooseCredit = (LinearLayout) contentView.findViewById(R.id.ll_choose_credit);
        tvCommodityName = (TextView) contentView.findViewById(R.id.tv_commodit_name);
        llCredit = (LinearLayout) contentView.findViewById(R.id.ll_credit);
        ivReduce = (ImageView) contentView.findViewById(R.id.iv_reduce);
        tvAmounts = (TextView) contentView.findViewById(R.id.tv_amounts);
        tvCommodityAmounts = (TextView) contentView.findViewById(R.id.tv_commodity_amount);
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
        attrHashMap = new LinkedHashMap<>();

    }


    private OnChooseCompleteListener completeListener;

    public interface OnChooseCompleteListener {
        void onComplete(String skuCode);
    }

    public void setOnChooseCompleteListener(OnChooseCompleteListener listener) {
        this.completeListener = listener;
    }

    public void setData(SkuIntroductionResponse response) {
        List<SkuIntroductionResponse.SkuSpecsBean> skuSpecs = response.skuSpecs;
        if (CommonUtils.isNullOrEmpty(skuSpecs)) {
            return;
        }
        tvCommodityName.setText(response.skuInfo.name);
        CommonUtils.setTextWithSpanSizeAndColor(tvPrice, "¥", StringUtils.format2(response.skuInfo.salePrice), "", 19, 17, R.color.color_ff2a2a, R.color.color_999999);
        loadImage(response.skuInfo.srcIp+"/"+response.skuInfo.src);
        filterList = new ArrayList<>();
        allAttrMap = new ArrayList<>();
        ArrayList<Integer> defaultList = new ArrayList<>();
        SkuIntroductionResponse.SkuSpecsBean skuSpec = skuSpecs.get(0);
        String defalutSkucode = skuSpec.specValues.get(0).skuCodes.get(0);
        defaultList.add(0);
        attrHashMap.put(skuSpecs.get(0).name, skuSpec.specValues.get(0));
        for (int i = 1; i < skuSpecs.size(); i++) {
            SkuIntroductionResponse.SkuSpecsBean specValue = skuSpecs.get(i);
            for (int j = 0; j < specValue.specValues.size(); j++) {
                SkuIntroductionResponse.SpecValuesBean specValuesBean = specValue.specValues.get(j);
                if (specValuesBean.skuCodes.contains(defalutSkucode)) {
                    defaultList.add(j);
                    attrHashMap.put(specValue.name, specValue.specValues.get(j));
                    break;
                }
            }

        }
        for (int i = 0; i < skuSpecs.size(); i++) {
            SkuIntroductionResponse.SkuSpecsBean skuSpecBean = skuSpecs.get(i);
            ArrayList<SkuIntroductionResponse.SpecValuesBean> attrList = new ArrayList<>();
            attrList.addAll(skuSpecBean.specValues);
            allAttrMap.add(attrList);
            addView(defaultList.get(i), skuSpecBean.name, attrList);
        }
    }

    public String getAttrStr() {
        String attrStr = "";
        for (Map.Entry<String, SkuIntroductionResponse.SpecValuesBean> entry : attrHashMap.entrySet()) {
            attrStr += entry.getValue().specValue + " ";
        }
        return attrStr;
    }

    public String getSkuCode() {
        String skuCode = "";
        ArrayList<String> filterList = new ArrayList<>();
        Iterator<Map.Entry<String, SkuIntroductionResponse.SpecValuesBean>> iterator = attrHashMap.entrySet().iterator();
        if (iterator.hasNext()) {
            filterList.addAll(iterator.next().getValue().skuCodes);
        } else {
            return "";
        }
        for (Map.Entry<String, SkuIntroductionResponse.SpecValuesBean> entry : attrHashMap.entrySet()) {
            filterList.retainAll(entry.getValue().skuCodes);
        }
        if (CommonUtils.isNotNullOrEmpty(filterList)) {
            skuCode = filterList.get(0);
        }
        return skuCode;

    }

    @Override
    protected void createDialog() {
        super.createDialog();
        Window dialogWindow = getWindow();
        if (dialogWindow != null) {
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            // 设置对话框宽度
            lp.width = DensityUtils.getWidth();
            dialogWindow.setGravity(Gravity.BOTTOM);
            dialogWindow.setWindowAnimations(R.style.dialogWindowAnim); // 添加动画
        }
    }

    private void addView(final int defaultPos, final String paramsStr, final ArrayList<SkuIntroductionResponse.SpecValuesBean> paramsList) {
        View speView = View.inflate(getContext(), R.layout.sv_specification_item, null);
        TextView tvParam;
        final TagFlowLayout tfParam = (TagFlowLayout) speView.findViewById(R.id.tf_specification);
        tvParam = (TextView) speView.findViewById(R.id.tv_param);
        tvParam.setText(paramsStr);
        TagAdapter<SkuIntroductionResponse.SpecValuesBean> paramsAdapter = new TagAdapter<SkuIntroductionResponse.SpecValuesBean>(paramsList) {
            @Override
            public View getView(FlowLayout parent, int position, SkuIntroductionResponse.SpecValuesBean specValuesBean) {
                TextView tvTag = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.flowlayout_params_item, tfParam, false);
                tvTag.setText(specValuesBean.specValue);
                ArrayList<String> filterList = new ArrayList<>();
                filterList.addAll(specValuesBean.skuCodes);
                for (Map.Entry<String, SkuIntroductionResponse.SpecValuesBean> entry : attrHashMap.entrySet()) {
                    if (!entry.getKey().equals(paramsStr) && entry.getValue().skuCodes != null) {
                        filterList.retainAll(entry.getValue().skuCodes);
                    }
                }
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
        tagHashMap.put(paramsStr, paramsAdapter);
        tagFHashMap.put(paramsStr, tfParam);
        //设置默认选中位置
        paramsAdapter.setSelectedList(defaultPos);
        tfParam.setAdapter(paramsAdapter);
        tfParam.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                boolean clickEnable = (boolean) view.getTag();
                if (!clickEnable) {
                    attrHashMap.clear();
                }
                attrHashMap.put(paramsStr, paramsList.get(position));

                for (Map.Entry<String, TagFlowLayout> entry : tagFHashMap.entrySet()) {
                    if (!paramsStr.equals(entry.getKey())) {
                        if (clickEnable) {
                            entry.getValue().notifyDataSetChange();
                        } else {
                            entry.getValue().resetView();
                        }
                    } else if (!clickEnable) {
                        entry.getValue().notifyDataSetChange();
                    }

                }
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

    public void loadImage(String url){
        ImageUtils.loadImageWithCornerRadius(url, ivCommodity,DensityUtils.dip2px(8));

    }


    /**
     * 是否分期产品
     *
     * @param isCredit
     */
    public void showDialog(boolean isCredit) {
        if (isCredit) {
            //分期购买应显示的内容
            llCredit.setVisibility(View.VISIBLE);
            tvConfirm.setVisibility(View.GONE);
        } else {
            //一次性购买应显示的内容
            llCredit.setVisibility(View.GONE);
            tvConfirm.setVisibility(View.VISIBLE);
        }
        show();
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
                    dismiss();
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
        void confirm(LinkedHashMap<String, String> attrHashMap);

        void cancle();
    }

}
