package com.giveu.shoppingmall.index.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.utils.DensityUtils;


/**
 * Created by 513419 on 2017/8/30.
 * 购买商品对话框
 */

public class BuyCommodityDialog extends Dialog implements View.OnClickListener {


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
