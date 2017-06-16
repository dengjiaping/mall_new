package com.giveu.shoppingmall.view.dialog;

import android.app.Activity;
import android.app.ProgressDialog;

import com.giveu.shoppingmall.R;

/**
 * 上传图片进度框
 * Created by 513419 on 2017/3/21.
 */

public class UploadProgressDialog extends ProgressDialog {
    private String title = "";
    private Activity mContext;

    public UploadProgressDialog(Activity context) {
        super(context);
        mContext = context;
        init();
    }

    public UploadProgressDialog(Activity context, String title) {
        super(context);
        mContext = context;
        this.title = title;
        init();
    }

    public UploadProgressDialog(Activity context, int theme) {
        super(context, theme);
        mContext = context;
        init();
    }

    private void init() {
        setIcon(R.mipmap.ic_launcher);//设置图标，与为Title左侧
        setCancelable(false);
        setCanceledOnTouchOutside(false);//点击空白处不可取消
        setProgressStyle(ProgressDialog.STYLE_SPINNER);// 水平线进度条,STYLE_SPINNER:圆形进度条
        if (title.length() > 0) {
            setTitle(title);
        }
    }

    public void hideLoading() {
        if (mContext != null && !mContext.isFinishing() && isShowing()) {
            dismiss();
        }
    }

    public void showLoading(){
        if (mContext != null && !mContext.isFinishing() && !isShowing()) {
            show();
        }
    }

    public void setUploadContent(String content) {
        setMessage("正在上传" + content + ",请耐心等待...");
    }
}
