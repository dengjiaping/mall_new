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
import com.giveu.shoppingmall.utils.DensityUtils;
import com.giveu.shoppingmall.widget.flowlayout.FlowLayout;
import com.giveu.shoppingmall.widget.flowlayout.TagAdapter;
import com.giveu.shoppingmall.widget.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;


/**
 * Created by 513419 on 2017/8/30.
 * 购买商品对话框
 */

public class BuyCommodityDialog extends CustomDialog implements View.OnClickListener {

    private LinearLayout llContainer;
    private ImageView ivDismiss;
    private TextView tvConfirm;
    private LinearLayout llCredit;
    private LinearLayout llChooseCredit;
    private LinkedHashMap<String ,String > attrHashMap;

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
        llContainer = (LinearLayout) contentView.findViewById(R.id.ll_container);
        ivDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        llChooseCredit.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        attrHashMap = new LinkedHashMap<>();
        ArrayList<String> colorList = new ArrayList<>();
        colorList.add("黑色");
        colorList.add("金色");
        colorList.add("银色");
        colorList.add("玫瑰色");
        colorList.add("亮黑色");
        colorList.add("亮黑色");
        colorList.add("亮黑色");
        addView("颜色", colorList);
        attrHashMap.put("颜色","黑色");

        ArrayList<String> memoryList = new ArrayList<>();
        memoryList.add("32G");
        memoryList.add("128G");
        memoryList.add("256G");
        addView("内存", memoryList);
        attrHashMap.put("内存","32G");


        ArrayList<String> operatorList = new ArrayList<>();
        operatorList.add("中国移动");
        operatorList.add("中国电信");
        operatorList.add("中国联通");
        addView("运营商", operatorList);
        attrHashMap.put("运营商","中国移动");

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

    private void addView(final String paramsStr, final ArrayList<String> paramsList) {
        TagAdapter<String> paramsAdapter;
        View speView = View.inflate(getContext(), R.layout.sv_specification_item, null);
        TextView tvParam;
        final TagFlowLayout tfParam = (TagFlowLayout) speView.findViewById(R.id.tf_specification);
        tvParam = (TextView) speView.findViewById(R.id.tv_param);
        tvParam.setText(paramsStr);
        paramsAdapter = new TagAdapter<String>(paramsList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tvTag = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.flowlayout_params_item, tfParam, false);
                tvTag.setText(s);
                return tvTag;
            }
        };
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
                    attrHashMap.put(paramsStr,paramsList.get(integer));
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
            llCredit.setVisibility(View.VISIBLE);
            tvConfirm.setVisibility(View.GONE);
        } else {
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
        }
    }

    public interface OnConfirmListener {
        void confirm(LinkedHashMap<String ,String> attrHashMap);

        void cancle();
    }

}
