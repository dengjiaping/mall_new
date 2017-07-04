package com.giveu.shoppingmall.index.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.cash.view.dialog.QuotaDialog;
import com.giveu.shoppingmall.cash.view.fragment.MainCashFragment;
import com.giveu.shoppingmall.me.view.fragment.MainMeFragment;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.ApkUgradeResponse;
import com.giveu.shoppingmall.model.bean.response.PersonInfoResponse;
import com.giveu.shoppingmall.repay.view.fragment.MainRepayFragment;
import com.giveu.shoppingmall.recharge.view.fragment.RechargeFragment;
import com.giveu.shoppingmall.utils.Const;
import com.giveu.shoppingmall.utils.DownloadApkUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.sharePref.SharePrefUtil;
import com.giveu.shoppingmall.widget.dialog.PatternLockSetTipDialog;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

import static java.lang.System.currentTimeMillis;

public class MainActivity extends BaseActivity {
    public RechargeFragment rechargeFragment;
    public MainCashFragment mainCashFragment;
    public MainRepayFragment mainRepayFragment;
    public MainMeFragment mainMeFragment;
    @BindView(R.id.iv_recharge)
    ImageView ivRecharge;
    @BindView(R.id.tv_recharge)
    TextView tvRecharge;
    @BindView(R.id.iv_cash)
    ImageView ivCash;
    @BindView(R.id.tv_cash)
    TextView tvCash;
    @BindView(R.id.iv_repayment)
    ImageView ivRepayment;
    @BindView(R.id.tv_repayment)
    TextView tvRepayment;
    @BindView(R.id.iv_me)
    ImageView ivMe;
    @BindView(R.id.tv_me)
    TextView tvMe;

    private FragmentManager manager;
    private int nowPosition;
    //    private RadioGroup buttomBar;
    long exitTime;
    private ArrayList<Fragment> fragmentList;

    @BindView(R.id.mainViewPager)
    ViewPager mViewPager;
    @BindView(R.id.rb1)
    RadioButton rb1;
    private MainActivityAdapter mainAdapter;
    QuotaDialog quotaDialog;//额度为0的弹窗

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
//        buttomBar = (RadioGroup) findViewById(R.id.buttomBar);
        baseLayout.setTitleBarAndStatusBar(false, false);
        manager = getSupportFragmentManager();
        fragmentList = new ArrayList<>();
        rechargeFragment = new RechargeFragment();
        mainCashFragment = new MainCashFragment();
        mainRepayFragment = new MainRepayFragment();
        mainMeFragment = new MainMeFragment();
        fragmentList.add(rechargeFragment);
        fragmentList.add(mainCashFragment);
        fragmentList.add(mainRepayFragment);
        fragmentList.add(mainMeFragment);
        mainAdapter = new MainActivityAdapter(manager, fragmentList);
        mViewPager.setAdapter(mainAdapter);
        mViewPager.setOffscreenPageLimit(3);
        //跳转至消息列表
        if (getIntent().getBooleanExtra(needTurnToMessageActivity, false)) {
//            Intent intent = new Intent(mBaseContext, MessageActivity.class);
//            startActivity(intent);
        }
        UITest.test(mBaseContext);
        resetIconAndTextColor();
        selectIconAndTextColor(0);
        quotaDialog = new QuotaDialog(mBaseContext);
    }

    public void selectIconAndTextColor(int selectPos) {
        switch (selectPos) {
            case 0:
                setImageView(ivRecharge, R.drawable.ic_recharge_select);
                setTextColor(tvRecharge, R.color.color_00bbc0);
                break;
            case 1:
                setImageView(ivCash, R.drawable.ic_cash_select);
                setTextColor(tvCash, R.color.color_00bbc0);
                break;
            case 2:
                setImageView(ivRepayment, R.drawable.ic_repayment_select);
                setTextColor(tvRepayment, R.color.color_00bbc0);
                break;
            case 3:
                setImageView(ivMe, R.drawable.ic_me_select);
                setTextColor(tvMe, R.color.color_00bbc0);
                break;
        }
    }

    public void resetIconAndTextColor() {
        setImageView(ivRecharge, R.drawable.ic_recharge);
        setImageView(ivCash, R.drawable.ic_cash);
        setImageView(ivRepayment, R.drawable.ic_repayment);
        setImageView(ivMe, R.drawable.ic_me);
        setTextColor(tvRecharge, R.color.color_9b9b9b);
        setTextColor(tvCash, R.color.color_9b9b9b);
        setTextColor(tvRepayment, R.color.color_9b9b9b);
        setTextColor(tvMe, R.color.color_9b9b9b);
    }

    public void setTextColor(TextView textView, int colorId) {
        textView.setTextColor(ContextCompat.getColor(mBaseContext, colorId));
    }

    public void setImageView(ImageView imageView, int imageId) {
        imageView.setImageResource(imageId);
    }

    @OnClick({R.id.ll_recharge,R.id.ll_cash,R.id.ll_repayment,R.id.ll_me})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        resetIconAndTextColor();
        switch (view.getId()) {
            case R.id.ll_recharge:
                mViewPager.setCurrentItem(0, false);
                selectIconAndTextColor(0);
                break;

            case R.id.ll_cash:
                mViewPager.setCurrentItem(1, false);
                selectIconAndTextColor(1);
                ApiImpl.getUserInfo(mBaseContext, 10000923, new BaseRequestAgent.ResponseListener<PersonInfoResponse>() {
                    @Override
                    public void onSuccess(PersonInfoResponse response) {
                        //response.data.availablePosLimit == 0
                        if(true){
                            quotaDialog.showDialog();
                        }
                    }

                    @Override
                    public void onError(BaseBean errorBean) {
                        CommonLoadingView.showErrorToast(errorBean);
                    }
                });
                break;

            case R.id.ll_repayment:
                mViewPager.setCurrentItem(2, false);
                selectIconAndTextColor(2);
                break;

            case R.id.ll_me:
                mViewPager.setCurrentItem(3, false);
                selectIconAndTextColor(3);
                break;

            default:
                break;
        }
    }

    @Override
    public void setListener() {
/*        buttomBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                switch (checkId) {
                    case R.id.rb0:
                        mViewPager.setCurrentItem(0, false);
                        break;
                    case R.id.rb1:
                        mViewPager.setCurrentItem(1, false);
                        break;
                    case R.id.rb2:
                        mViewPager.setCurrentItem(2, false);
                        break;
                    case R.id.rb3:
                        mViewPager.setCurrentItem(3, false);
                        break;
                    default:
                        break;
                }
            }
        });*/
    }

    @Override
    public void setData() {
        doApkUpgrade();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private class MainActivityAdapter extends FragmentStatePagerAdapter {
        private ArrayList<Fragment> fragments;

        public MainActivityAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }


    // 返回键事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((currentTimeMillis() - exitTime) > 2000) {
                ToastUtils.showShortToast(R.string.tips_exit);
                exitTime = currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (downloadApkUtils != null) {
            downloadApkUtils.onActivityResume();
        }

        if (!LoginHelper.getInstance().hasUploadDeviceNumber() && StringUtils.isNotNull(JPushInterface.getRegistrationID(BaseApplication.getInstance()))) {
            //上传设备号至服务器
            ApiImpl.saveDeviceNumber(JPushInterface.getRegistrationID(BaseApplication.getInstance()));
        }

    }

    DownloadApkUtils downloadApkUtils = null;

    /**
     * 处理app版本升级
     */
    private void doApkUpgrade() {
        //apk升级
        ApiImpl.sendApkUpgradeRequest(mBaseContext, new BaseRequestAgent.ResponseListener<ApkUgradeResponse>() {
            @Override
            public void onSuccess(ApkUgradeResponse response) {
                downloadApkUtils = new DownloadApkUtils();
                if (response.data.isUnforceUpdate()) {//安卓升级0无更新1普通2强制
                    long lastShowTime = SharePrefUtil.getLastUpdateApkDialogTime();
                    if (System.currentTimeMillis() - lastShowTime > 24 * 60 * 60 * 1000) {//每隔24小时进应用提示一次
                        SharePrefUtil.setLastUpdateApkDialogTime(System.currentTimeMillis());

                        downloadApkUtils.showUpdateApkDialog(mBaseContext, response.data);
                    }
                } else if (response.data.isForceUpdate()) {
                    downloadApkUtils.showUpdateApkDialog(mBaseContext, response.data);
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
            }
        });
    }

    public static void startIt(Context mContext) {
        startItSelectFragment(0, mContext);
    }

    /**
     * @param whichFragment 0=取现 1=消费 2=我的fragment
     * @param mContext
     */
    public static void startItSelectFragment(int whichFragment, Context mContext) {
        startItSelectFragmentWithData(whichFragment, mContext, null);
    }

    public static void startItSelectFragmentWithData(int whichFragment, Context mContext, String data) {
        Intent i = new Intent(mContext, MainActivity.class);
        i.putExtra(Const.whichFragmentInActMain, whichFragment);
        i.putExtra(extraKey, data);
        mContext.startActivity(i);
    }

    public static void startItDealLock(int whichFragment, Context mContext, String data, boolean needTurn) {
        Intent i = new Intent(mContext, MainActivity.class);
        i.putExtra(Const.whichFragmentInActMain, whichFragment);
        i.putExtra(lockPatternKey, data);
        i.putExtra(needTurnToMessageActivity, needTurn);
        mContext.startActivity(i);
    }

    public static String extraKey = "data";
    public static String lockPatternKey = "lockPatternKey";
    public static String needTurnToMessageActivity = "needTurnToMessageActivity";//是否需要跳转至消息列表
    //找回密码（重置密码）

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String data = intent.getStringExtra(extraKey);

    }

    /**
     * 设置手势密码的提示dialog
     */
    public void showPatternLockTipDialog() {
        new PatternLockSetTipDialog(mBaseContext).show();
    }

}