package com.giveu.shoppingmall.index.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.explosionfield.ExplosionField;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;


/**
 * Created by 101900 on 2017/3/6.
 */

public class CouponDialog {
    private CustomDialog mDialog;
    private Activity mActivity;
    private ExplosionField mExplosionField;
    ImageView ivCoupon, ivReceive, ivClose;

    public CouponDialog(final Activity mActivity) {
        this.mActivity = mActivity;
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.activity_receive_coupon, null);
        ivClose = (ImageView) convertView.findViewById(R.id.iv_close);
        ivCoupon = (ImageView) convertView.findViewById(R.id.iv_coupon);
        ivReceive = (ImageView) convertView.findViewById(R.id.iv_receive);
        mDialog = new CustomDialog(mActivity, convertView, R.style.login_error_dialog_Style, Gravity.CENTER, true);
        mDialog.setCancelable(false);
        mExplosionField = ExplosionField.attach2Window(mDialog);
        Window window = mDialog.getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.dialogWindowCouponAnim); //设置窗口弹出动画
        }
        initView(convertView);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //运行爆炸动画,关闭dialog
                startExplosionField(mActivity,mDialog,view,ivClose,ivCoupon,ivReceive);
            }
        });

        ivReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                ApiImpl.receiveCoupon(LoginHelper.getInstance().getIdPerson(), LoginHelper.getInstance().getUserId(), new BaseRequestAgent.ResponseListener<BaseBean>() {
                    @Override
                    public void onSuccess(BaseBean response) {
                        ToastUtils.showLongToast("领取成功！可在个人中心-我的优惠里查看");
                        //运行爆炸动画,关闭dialog
                        startExplosionField(mActivity,mDialog,view,ivClose,ivCoupon,ivReceive);
                    }

                    @Override
                    public void onError(BaseBean errorBean) {
                        CommonLoadingView.showErrorToast(errorBean);
                    }
                });

            }
        });
    }

    public void showDialog() {
        mDialog.show();
    }

    public void initView(View view) {

    }

    /**
     * 执行爆炸动画,并关闭dialog
     */
    public void startExplosionField(final Activity mActivity, final CustomDialog mDialog, View view, ImageView ivClose, ImageView ivCoupon, ImageView ivReceive){
        mExplosionField.explode(ivClose);
        mExplosionField.explode(ivCoupon);
        mExplosionField.explode(ivReceive);
        view.setOnClickListener(null);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mActivity != null && !mActivity.isFinishing() && mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        }, 1000);
    }
}
