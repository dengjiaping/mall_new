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
import com.giveu.shoppingmall.utils.Const;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.explosionfield.ExplosionField;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

import org.json.JSONObject;


/**
 * Created by 101900 on 2017/3/6.
 */

public class CouponDialog {
    private CustomDialog mDialog;
    private Activity mActivity;
    private ExplosionField mExplosionField;
    ImageView ivCoupon, ivReceive, ivClose;
    String type;
    public CouponDialog(final Activity mActivity) {
        this.mActivity = mActivity;
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.activity_receive_coupon, null);
        ivClose = (ImageView) convertView.findViewById(R.id.iv_close);
        ivCoupon = (ImageView) convertView.findViewById(R.id.iv_coupon);
        ivReceive = (ImageView) convertView.findViewById(R.id.iv_receive);
        mDialog = new CustomDialog(mActivity, convertView, R.style.login_error_dialog_Style, Gravity.CENTER, true);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mExplosionField = ExplosionField.attach2Window(mDialog);
        Window window = mDialog.getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.dialogWindowCouponAnim); //设置窗口弹出动画
        }
        initView(convertView);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //领取优惠券
                receiveCoupon(mActivity, mDialog, view, ivClose, ivCoupon, ivReceive, Const.CLOSE);
            }
        });

        ivReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //领取优惠券
                receiveCoupon(mActivity, mDialog, view, ivClose, ivCoupon, ivReceive,Const.RECEIVE);
            }
        });
    }

    /**
     * 领取优惠券调用接口
     * @param mActivity
     * @param mDialog
     * @param view
     * @param ivClose
     * @param ivCoupon
     * @param ivReceive
     */
    public void receiveCoupon(final Activity mActivity, final CustomDialog mDialog, final View view, final ImageView ivClose, final ImageView ivCoupon, final ImageView ivReceive, final String type){

        ApiImpl.receiveCoupon(mActivity,LoginHelper.getInstance().getIdPerson(), LoginHelper.getInstance().getUserId(), new BaseRequestAgent.ResponseListener<BaseBean>() {
            @Override
            public void onSuccess(BaseBean response) {
            }

            @Override
            public void onError(BaseBean errorBean) {
                boolean status = false;
                String originalStr = errorBean == null ? "" : errorBean.originResultString;
                //后台返回空字符串，直接返回
                if (StringUtils.isNull(originalStr)) {
                    if(Const.CLOSE.equals(type)){
                        startExplosionField(mActivity, mDialog, view, ivClose, ivCoupon, ivReceive);
                    }
                    if (errorBean != null) {
                        ToastUtils.showShortToast(errorBean.message);
                    }
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(originalStr);
                    if ("success".equals(jsonObject.getString("status"))) {
                        status = true;
                        ToastUtils.showLongToast("领取成功！可在个人中心-我的优惠里查看");
                    } else {
                        CommonLoadingView.showErrorToast(errorBean);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if(status){
                        //运行爆炸动画,关闭dialog
                        startExplosionField(mActivity, mDialog, view, ivClose, ivCoupon, ivReceive);
                    }else{
                        if(Const.CLOSE.equals(type)){
                            startExplosionField(mActivity, mDialog, view, ivClose, ivCoupon, ivReceive);
                        }
                    }
                }
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
    public void startExplosionField(final Activity mActivity, final CustomDialog mDialog, View view, ImageView ivClose, ImageView ivCoupon, ImageView ivReceive) {
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
