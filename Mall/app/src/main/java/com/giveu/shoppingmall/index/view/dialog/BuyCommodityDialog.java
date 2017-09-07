package com.giveu.shoppingmall.index.view.dialog;

import android.app.Activity;
import android.util.Log;
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
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.widget.flowlayout.FlowLayout;
import com.giveu.shoppingmall.widget.flowlayout.TagAdapter;
import com.giveu.shoppingmall.widget.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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
    private ImageView ivReduce;
    private ImageView ivPlus;
    private LinearLayout llCredit;
    private LinearLayout llChooseCredit;
    private LinkedHashMap<String, String> attrHashMap;//存储选中的属性
    private LinkedHashMap<String, List<SkuIntroductionResponse.SpecValuesBean>> filterMap;

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
        tvConfirm = (TextView) contentView.findViewById(R.id.tv_confirm);
        llChooseCredit = (LinearLayout) contentView.findViewById(R.id.ll_choose_credit);
        llCredit = (LinearLayout) contentView.findViewById(R.id.ll_credit);
        ivReduce = (ImageView) contentView.findViewById(R.id.iv_reduce);
        tvAmounts = (TextView) contentView.findViewById(R.id.tv_amounts);
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

    public void setData(List<SkuIntroductionResponse.SkuSpecsBean> skuSpecs) {
        if (CommonUtils.isNullOrEmpty(skuSpecs)) {
            return;
        }
        filterMap = new LinkedHashMap<>();
        for (SkuIntroductionResponse.SkuSpecsBean skuSpec : skuSpecs) {
            ArrayList<SkuIntroductionResponse.SpecValuesBean> attrList = new ArrayList<>();
            attrList.addAll(skuSpec.specValues);
            filterMap.put(skuSpec.name, attrList);
            addView(skuSpec.name, attrList);
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
            dialogWindow.setGravity(Gravity.BOTTOM);
            dialogWindow.setWindowAnimations(R.style.dialogWindowAnim); // 添加动画
        }
    }

    private void addView(final String paramsStr, final ArrayList<SkuIntroductionResponse.SpecValuesBean> paramsList) {
        TagAdapter<SkuIntroductionResponse.SpecValuesBean> paramsAdapter;
        View speView = View.inflate(getContext(), R.layout.sv_specification_item, null);
        TextView tvParam;
        final TagFlowLayout tfParam = (TagFlowLayout) speView.findViewById(R.id.tf_specification);
        tvParam = (TextView) speView.findViewById(R.id.tv_param);
        tvParam.setText(paramsStr);
        paramsAdapter = new TagAdapter<SkuIntroductionResponse.SpecValuesBean>(paramsList) {
            @Override
            public View getView(FlowLayout parent, int position, SkuIntroductionResponse.SpecValuesBean specValuesBean) {
                TextView tvTag = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.flowlayout_params_item, tfParam, false);
                tvTag.setText(specValuesBean.specValue);
                if (position == 1) {
                    //虚线框，不能点击
                    tvTag.setBackgroundResource(R.drawable.shape_dash_rect);
                } else {
                    //可点击状态
                    tvTag.setBackgroundResource(R.drawable.selector_params);
                }
                return tvTag;
            }
        };
        //设置默认选中位置
        paramsAdapter.setSelectedList(0);
        tfParam.setAdapter(paramsAdapter);
        tfParam.setAdapter(paramsAdapter);
        tfParam.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                return false;
            }
        });
        tfParam.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                for (Integer integer : selectPosSet) {
                    Log.e("TAG", "选择了" + paramsList.get(integer));
                }
            }
        });
        llContainer.addView(speView);
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
                    listener.confirm(attrHashMap);
                    dismiss();
                }
                break;

            case R.id.iv_reduce:
                String amountsStr = tvAmounts.getText().toString();
                int amounts = StringUtils.string2Int(amountsStr);
                if (amounts > 1) {
                    tvAmounts.setText((amounts - 1)+"");
                }
                break;

            case R.id.iv_plus:
                amountsStr = tvAmounts.getText().toString();
                amounts = StringUtils.string2Int(amountsStr);
                tvAmounts.setText((amounts + 1)+"");
                break;
        }
    }

    public interface OnConfirmListener {
        void confirm(LinkedHashMap<String, String> attrHashMap);

        void cancle();
    }

}
