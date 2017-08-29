package com.giveu.shoppingmall.index.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.event.LotteryEvent;
import com.giveu.shoppingmall.index.view.dialog.CouponDialog;
import com.giveu.shoppingmall.index.view.dialog.SmallLotteryDialog;
import com.giveu.shoppingmall.me.view.activity.CustomWebViewActivity;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.LotteryResponse;
import com.giveu.shoppingmall.model.bean.response.WalletActivationResponse;
import com.giveu.shoppingmall.model.bean.response.WalletQualifiedResponse;
import com.giveu.shoppingmall.utils.EventBusUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.sharePref.SharePrefUtil;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;
import com.google.gson.Gson;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 钱包激活状态页
 * Created by 101900 on 2017/6/19.
 */

public class ActivationStatusActivity extends BaseActivity {
    @BindView(R.id.tv_set_transaction_pwd)
    TextView tvSetTransactionPwd;
    @BindView(R.id.iv_status)
    ImageView ivStatus;
    @BindView(R.id.tv_hint_bottom)
    TextView tvHintBottom;
    @BindView(R.id.tv_hint_mid)
    TextView tvHintMid;
    @BindView(R.id.ll_date)
    LinearLayout llDate;
    @BindView(R.id.tv_globle_limit)
    TextView tvGlobleLimit;
    @BindView(R.id.tv_cy_limit)
    TextView tvCyLimit;
    @BindView(R.id.tv_pos_limit)
    TextView tvPosLimit;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    public final int BACK = 1;
    public final int SETPWD = 2;
    public final int REPEAT = 3;
    CouponDialog couponDialog;
    private boolean isShow;
    private boolean hasShowCoupon;

    //显示手Q用户激活成功结果
    public static void startShowQQResultSuccess(Activity mActivity, WalletQualifiedResponse response, String idPerson, String status, boolean isShow, boolean hasShowCoupon) {
        Intent intent = new Intent(mActivity, ActivationStatusActivity.class);
        if (response != null) {
            intent.putExtra("status", status);
            if (response.data != null) {
                //总额度
                intent.putExtra("globleLimit", response.data.globleLimit);
                //提现额度
                intent.putExtra("cyLimit", response.data.cyLimit);
                //消费额度
                intent.putExtra("posLimit", response.data.posLimit);
                //是否设置交易密码
                intent.putExtra("isSetPwd", response.data.isSetPwd);
                //额度为0的提示语
                intent.putExtra("lab", response.data.lab);
                //当前日期是否还能领取优惠券
                intent.putExtra("isShow", isShow);
                //实现满足额度为0条件显示领取优惠券弹窗
                intent.putExtra("hasShowCoupon", hasShowCoupon);
            }
        }
        intent.putExtra("idPerson", idPerson);
        mActivity.startActivity(intent);
    }

    //显示成功结果
    public static void startShowResultSuccess(Activity mActivity, WalletActivationResponse response, String idPerson, String status, boolean isShow, boolean hasShowCoupon) {
        Intent intent = new Intent(mActivity, ActivationStatusActivity.class);
        if (response != null) {
            intent.putExtra("status", status);
            if (response.data != null) {
                //总额度
                intent.putExtra("globleLimit", response.data.globleLimit);
                //提现额度
                intent.putExtra("cyLimit", response.data.cyLimit);
                //消费额度
                intent.putExtra("posLimit", response.data.posLimit);
                //额度为0的提示语
                intent.putExtra("lab", response.data.lab);
                //当前日期是否还能领取优惠券
                intent.putExtra("isShow", isShow);
                //实现满足额度为0条件显示领取优惠券弹窗
                intent.putExtra("hasShowCoupon", hasShowCoupon);
            }
        }
        intent.putExtra("idPerson", idPerson);
        mActivity.startActivity(intent);

    }

    //显示失败结果
    public static void startShowResultFail(Activity mActivity, BaseBean errorBean, String status) {
        Intent intent = new Intent(mActivity, ActivationStatusActivity.class);
        intent.putExtra("message", errorBean.message);
        intent.putExtra("status", status);
        intent.putExtra("code", errorBean.code);
        mActivity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_activation_status);
        baseLayout.setTitle("钱包激活");
        couponDialog = new CouponDialog(mBaseContext);
        isShow = getIntent().getBooleanExtra("isShow", false);
        hasShowCoupon = getIntent().getBooleanExtra("hasShowCoupon", false);
        //判断是否可进行抽奖
        doLottery();
    }

    private void doLottery() {
        ApiImpl.getActivityInfo(mBaseContext, LoginHelper.getInstance().getIdPerson(), new BaseRequestAgent.ResponseListener<BaseBean>() {
            @Override
            public void onSuccess(BaseBean response) {

            }

            @Override
            public void onError(BaseBean errorBean) {
                String originalStr = errorBean == null ? "" : errorBean.originResultString;
                //后台返回空字符串，直接返回
                if (StringUtils.isNull(originalStr)) {
                    if (errorBean != null) {
                        ToastUtils.showShortToast(errorBean.message);
                    }
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(originalStr);
                    if ("success".equals(jsonObject.getString("status"))) {
                        Gson gson = new Gson();
                        final LotteryResponse lotteryResponse = gson.fromJson(originalStr, LotteryResponse.class);
                        //活动有效期，可以进入抽奖，且直接进行领取优惠券活动（直接调接口）
                        if (lotteryResponse != null && lotteryResponse.data != null && lotteryResponse.data.activatyStatus == 1) {
                            //领取优惠券
                            if (isShow && hasShowCoupon) {
                                receiveCoupon();
                            }
                            if (lotteryResponse.data.justDo == 1) {
                                SharePrefUtil.setNeedShowLottery(false);
                                final SmallLotteryDialog lotteryDialog = new SmallLotteryDialog(mBaseContext);
                                lotteryDialog.setOnConfirmListener(new SmallLotteryDialog.OnConfirmListener() {
                                    @Override
                                    public void onConfirm() {
                                        EventBusUtils.poseEvent(new LotteryEvent());
                                        if (lotteryResponse.data != null && StringUtils.isNotNull(lotteryResponse.data.activityUrl)) {
                                            CustomWebViewActivity.startIt(mBaseContext, lotteryResponse.data.activityUrl, "");
                                        }
                                        lotteryDialog.dismissDialog();
                                    }
                                });
                                lotteryDialog.showDialog();
                            }
                        } else {
                            if (isShow && hasShowCoupon) {
                                //当前日期还能领取优惠券 && 取现和消费额度都为0
                                couponDialog.showDialog();
                            }
                        }
                    } else {
                        CommonLoadingView.showErrorToast(errorBean);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void receiveCoupon() {
        //这里是直接进行领取优惠券活动，不需要任何处理
        ApiImpl.receiveCoupon(LoginHelper.getInstance().getIdPerson(), LoginHelper.getInstance().getUserId(), new BaseRequestAgent.ResponseListener<BaseBean>() {
            @Override
            public void onSuccess(BaseBean response) {
            }

            @Override
            public void onError(BaseBean errorBean) {
            }
        });
    }


    @Override
    public void setListener() {
        super.setListener();
    }

    @OnClick(R.id.tv_set_transaction_pwd)
    @Override
    public void onClick(View view) {
        super.onClick(view);
        int tag = (int) view.getTag();
        switch (tag) {
            case SETPWD:
                //设置交易密码
                String idPerson = getIntent().getStringExtra("idPerson");
                TransactionPwdActivity.startIt(mBaseContext, idPerson);
                BaseApplication.getInstance().finishActivity(WalletActivationFirstActivity.class);
                break;
            case BACK:
                //返回
                finish();
                break;
            case REPEAT:
                //重新激活钱包
                WalletActivationFirstActivity.startIt(mBaseContext);
                break;
        }
    }

    @Override
    public void setData() {
        String status = StringUtils.nullToEmptyString(getIntent().getStringExtra("status"));
        switch (status) {
            case "success":
                //激活成功
                String globleLimit = getIntent().getStringExtra("globleLimit");
                String cyLimit = getIntent().getStringExtra("cyLimit");
                String posLimit = getIntent().getStringExtra("posLimit");
                boolean isSetPwd = getIntent().getBooleanExtra("isSetPwd", false);
                String lab = StringUtils.nullToEmptyString(getIntent().getStringExtra("lab"));
                llDate.setVisibility(View.VISIBLE);
                tvHintMid.setVisibility(View.GONE);
                tvGlobleLimit.setText(String.valueOf(globleLimit));
                tvCyLimit.setText(String.valueOf(cyLimit));
                tvPosLimit.setText(String.valueOf(posLimit));
                ivStatus.setImageResource(R.drawable.ic_activation_success);
                tvStatus.setText("激活成功");
                if (isSetPwd) {
                    //设置过交易密码
                    tvSetTransactionPwd.setText("确定");
                    tvSetTransactionPwd.setTag(BACK);
                } else {
                    tvSetTransactionPwd.setText("设置交易密码");
                    tvSetTransactionPwd.setTag(SETPWD);
                }

                tvHintBottom.setVisibility(View.VISIBLE);
                tvHintBottom.setText(lab);
                break;
            case "fail":
                // 激活失败，无额度，含中间提示语
                String message = StringUtils.nullToEmptyString(getIntent().getStringExtra("message"));
                String code = StringUtils.nullToEmptyString(getIntent().getStringExtra("code"));
                llDate.setVisibility(View.GONE);
                tvHintMid.setVisibility(View.VISIBLE);
                tvHintMid.setText(message);
                ivStatus.setImageResource(R.drawable.ic_activation_fail);
                tvStatus.setText("激活失败");
                tvSetTransactionPwd.setText("重新激活钱包");
                tvSetTransactionPwd.setTag(REPEAT);
                tvHintBottom.setVisibility(View.GONE);
                if ("sc800115".equals(code) || "sc800705".equals(code)) {
                    //800115:激活页面一跳转过来，没有资质；800705:激活页面二跳转过来，激活多次失败，不能再激活
                    tvSetTransactionPwd.setVisibility(View.GONE);
                } else {
                    tvSetTransactionPwd.setVisibility(View.VISIBLE);
                }
                break;
            default:
                //异常隐藏按钮
                tvSetTransactionPwd.setVisibility(View.GONE);
                break;
        }
    }
}
