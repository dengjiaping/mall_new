package com.giveu.shoppingmall.me.view.activity;

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
import com.giveu.shoppingmall.cash.view.activity.AddressManageActivity;
import com.giveu.shoppingmall.me.view.dialog.NotActiveDialog;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.ApkUgradeResponse;
import com.giveu.shoppingmall.utils.DensityUtils;
import com.giveu.shoppingmall.utils.DownloadApkUtils;
import com.giveu.shoppingmall.utils.ImageUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.sharePref.SharePrefUtil;
import com.giveu.shoppingmall.widget.dialog.CustomDialogUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 账号管理主页
 * Created by 101900 on 2017/6/27.
 */

public class AccountManagementActivity extends BaseActivity {

    @BindView(R.id.ll_delivery_address)
    LinearLayout llDeliveryAddress;
    @BindView(R.id.ll_bank_card)
    LinearLayout llBankCard;
    @BindView(R.id.ll_security_center)
    LinearLayout llSecurityCenter;
    @BindView(R.id.ll_version_update)
    LinearLayout llVersionUpdate;
    @BindView(R.id.tv_finish)
    TextView tvFinish;
    @BindView(R.id.tv_userName)
    TextView tvUserName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    NotActiveDialog notActiveDialog;//未开通钱包的弹窗
    @BindView(R.id.iv_update)
    ImageView ivUpdate;
    private DownloadApkUtils downloadApkUtils;


    public static void startIt(Activity mActivity) {
        Intent intent = new Intent(mActivity, AccountManagementActivity.class);
        mActivity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_account_management);
        baseLayout.setTitle("账号管理");
        notActiveDialog = new NotActiveDialog(mBaseContext);
        ImageUtils.loadImageWithCorner(LoginHelper.getInstance().getUserPic(), R.drawable.ic_default_avatar, ivAvatar, DensityUtils.dip2px(28));
    }

    @Override
    protected void onResume() {
        super.onResume();
        //优先级别：真实姓名-手机号(方便激活钱包后及时更新信息以便展示，所以在onresume执行)
        if (StringUtils.isNotNull(LoginHelper.getInstance().getName())) {
            tvUserName.setText(LoginHelper.getInstance().getName());
            tvPhone.setVisibility(View.VISIBLE);
            tvPhone.setText(LoginHelper.getInstance().getPhone());
        } else {
            tvUserName.setText(LoginHelper.getInstance().getPhone());
            tvPhone.setVisibility(View.GONE);
        }
        //需要更新则显示更新图标
        if (SharePrefUtil.needUpdateApp()) {
            ivUpdate.setVisibility(View.VISIBLE);
        } else {
            ivUpdate.setVisibility(View.GONE);
        }

    }

    @Override
    public void setData() {
    }

    @OnClick({R.id.ll_delivery_address, R.id.ll_bank_card, R.id.ll_security_center, R.id.ll_version_update, R.id.tv_finish})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ll_delivery_address:
                //地址管理
                AddressManageActivity.startIt(mBaseContext);
                break;
            case R.id.ll_bank_card:
                //我的银行卡
                if (LoginHelper.getInstance().hasQualifications()) {//是否激活钱包
                    MyBankCardActivity.startIt(mBaseContext, false);
                } else {
                    notActiveDialog.showDialog();
                }
                break;
            case R.id.ll_security_center:
                //安全中心
                SecurityCenterActivity.startIt(mBaseContext);
                break;
            case R.id.ll_version_update:
                //版本更新
                doApkUpgrade();
                break;
            case R.id.tv_finish:
                //退出登录
                logout();
                break;
        }
    }

    private void logout() {
        CustomDialogUtil customDialogUtil = new CustomDialogUtil(mBaseContext);
        customDialogUtil.getDialogMode1("提示", "是否要退出登录？", "确定", "取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiImpl.logout(mBaseContext, new BaseRequestAgent.ResponseListener<BaseBean>() {
                    @Override
                    public void onSuccess(BaseBean response) {
                        LoginHelper.getInstance().logout();
                        ToastUtils.showShortToast("已退出登录");
                        finish();
                    }

                    @Override
                    public void onError(BaseBean errorBean) {
                        ToastUtils.showShortToast("退出登录失败");
                    }
                });
            }
        }, null).show();
    }

    /**
     * 处理app版本升级
     */
    private void doApkUpgrade() {
        //apk升级
        ApiImpl.sendApkUpgradeRequest(mBaseContext, new BaseRequestAgent.ResponseListener<ApkUgradeResponse>() {
            @Override
            public void onSuccess(ApkUgradeResponse response) {
                downloadApkUtils = new DownloadApkUtils();
                if (response.data != null && response.data.isNoUpdate()) {
                    ToastUtils.showShortToast("暂无更新");
                } else {
                    downloadApkUtils.showUpdateApkDialog(mBaseContext, response.data);
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
            }
        });
    }

}
