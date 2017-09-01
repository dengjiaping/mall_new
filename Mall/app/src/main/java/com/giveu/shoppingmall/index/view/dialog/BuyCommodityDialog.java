package com.giveu.shoppingmall.index.view.dialog;

import android.app.Dialog;
import android.content.Context;
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
import com.giveu.shoppingmall.utils.DensityUtils;
import com.giveu.shoppingmall.widget.flowlayout.FlowLayout;
import com.giveu.shoppingmall.widget.flowlayout.TagAdapter;
import com.giveu.shoppingmall.widget.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.Set;


/**
 * Created by 513419 on 2017/8/30.
 * 购买商品对话框
 */

public class BuyCommodityDialog extends Dialog implements View.OnClickListener {

    private LinearLayout llContainer;
    private ImageView ivDismiss;

    public BuyCommodityDialog(Context context) {
        super(context);
        init();
    }

    public BuyCommodityDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    protected BuyCommodityDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_buy_commodity);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        Window dialogWindow = getWindow();
        if (dialogWindow != null) {
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            // 设置对话框宽度
            lp.width = DensityUtils.getWidth();
            dialogWindow.setGravity(Gravity.BOTTOM);
            dialogWindow.setWindowAnimations(R.style.dialogWindowAnim); // 添加动画
        }
        ivDismiss = (ImageView) findViewById(R.id.iv_dismiss);
        llContainer = (LinearLayout) findViewById(R.id.ll_container);
        ivDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ArrayList<String> colorList = new ArrayList<>();
        colorList.add("黑色");
        colorList.add("金色");
        colorList.add("银色");
        colorList.add("玫瑰色");
        colorList.add("亮黑色");
        colorList.add("亮黑色");
        colorList.add("亮黑色");
        addView("颜色",colorList);

        ArrayList<String> memoryList = new ArrayList<>();
        memoryList.add("32G");
        memoryList.add("128G");
        memoryList.add("256G");
        addView("内存",memoryList);


        ArrayList<String> operatorList = new ArrayList<>();
        operatorList.add("中国移动");
        operatorList.add("中国电信");
        operatorList.add("中国联通");
        addView("运营商",operatorList);

    }

    private void addView(String paramsStr ,final ArrayList<String> paramsList) {
        TagAdapter<String> paramsAdapter;
        View speView = View.inflate(getContext(), R.layout.sv_specification_item, null);
        TextView tvParam;
        final  TagFlowLayout tfParam = (TagFlowLayout) speView.findViewById(R.id.tf_specification);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }

    private OnChooseListener listener;

    public void setOnChooseListener(OnChooseListener listener) {
        this.listener = listener;
    }

    public interface OnChooseListener {
        void confirm();

        void cancle();
    }

}
