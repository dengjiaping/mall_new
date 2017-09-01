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
import java.util.Set;

/**
 * Created by 513419 on 2017/9/1.
 */

public class CreditCommodityDialog extends CustomDialog {
    private LinearLayout llContainer;
    private ImageView ivDismiss;

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
        ArrayList<String> percentList = new ArrayList<>();
        percentList.add("5%");
        percentList.add("15%");
        percentList.add("25%");
        percentList.add("35%");
        addView("首付",percentList);

        ArrayList<String> monthList = new ArrayList<>();
        monthList.add("1");
        monthList.add("3");
        monthList.add("5");
        monthList.add("9");
        monthList.add("12");
        monthList.add("24");
        addView("分期数",monthList);
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

    private void addView(String paramsStr ,final ArrayList<String> paramsList) {
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
                }
            }
        });
        llContainer.addView(speView);
    }

    private BuyCommodityDialog.OnChooseListener listener;

    public void setOnChooseListener(BuyCommodityDialog.OnChooseListener listener) {
        this.listener = listener;
    }

    public interface OnChooseListener {
        void confirm();

        void cancle();
    }
}
