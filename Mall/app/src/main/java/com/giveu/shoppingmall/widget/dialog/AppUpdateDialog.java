package com.giveu.shoppingmall.widget.dialog;

import android.app.Activity;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.utils.StringUtils;


/**
 * Created by 513419 on 2017/7/25.
 * 应用升级提示框
 */

public class AppUpdateDialog extends CustomDialog {

    private TextView tvContent;
    private TextView tvCancel;
    private TextView tvConfirm;
    private TextView tvTitleHint;
    private TextView tvUpdateHint;
    private TextView tvTitle;


    public AppUpdateDialog(Activity context) {
        super(context, R.layout.dialog_app_update, R.style.date_dialog_style, Gravity.CENTER, false);
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        tvContent = (TextView) contentView.findViewById(R.id.tv_content);
        tvCancel = (TextView) contentView.findViewById(R.id.tv_cancel);
        tvConfirm = (TextView) contentView.findViewById(R.id.tv_confirm);
        tvTitle = (TextView) contentView.findViewById(R.id.tv_title);
        tvTitleHint = (TextView) contentView.findViewById(R.id.tv_title_hint);
        tvUpdateHint = (TextView) contentView.findViewById(R.id.tv_update_hint);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.cancel();
                }
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.confirm();
                }
            }
        });
    }


    /**
     * @param isInstall 安装提示框还是升级提示框
     * @param content   升级内容
     */
    public void initDialogContent(boolean isInstall, String content) {
        if (isInstall) {
            tvConfirm.setText("安装");
        } else {
            tvConfirm.setText("升级");
        }
        if (StringUtils.isNull(content)) {
            tvContent.setText("暂无更新内容");
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                tvContent.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT));
            } else {
                tvContent.setText(Html.fromHtml(content));
            }
        }

    }

    private OnChooseListener listener;

    public void setOnChooseListener(OnChooseListener listener) {
        this.listener = listener;
    }

    public interface OnChooseListener {
        void cancel();

        void confirm();
    }
}
