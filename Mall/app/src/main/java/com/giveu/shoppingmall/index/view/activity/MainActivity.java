package com.giveu.shoppingmall.index.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.base.BasePermissionActivity;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.base.IView;
import com.giveu.shoppingmall.cash.view.fragment.MainCashFragment;
import com.giveu.shoppingmall.event.LoginSuccessEvent;
import com.giveu.shoppingmall.event.LotteryEvent;
import com.giveu.shoppingmall.index.presenter.SplashAdPresenter;
import com.giveu.shoppingmall.index.view.dialog.LotteryDialog;
import com.giveu.shoppingmall.index.view.fragment.ShoppingFragment;
import com.giveu.shoppingmall.me.view.activity.CreateGestureActivity;
import com.giveu.shoppingmall.me.view.activity.CustomWebViewActivity;
import com.giveu.shoppingmall.me.view.activity.FingerPrintActivity;
import com.giveu.shoppingmall.me.view.activity.RepaymentActivity;
import com.giveu.shoppingmall.me.view.dialog.NotActiveDialog;
import com.giveu.shoppingmall.me.view.fragment.MainMeFragment;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.ApkUgradeResponse;
import com.giveu.shoppingmall.model.bean.response.LotteryResponse;
import com.giveu.shoppingmall.utils.Const;
import com.giveu.shoppingmall.utils.DownloadApkUtils;
import com.giveu.shoppingmall.utils.FingerPrintHelper;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.NetWorkUtils;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.sharePref.SharePrefUtil;
import com.giveu.shoppingmall.widget.dialog.ConfirmDialog;
import com.giveu.shoppingmall.widget.dialog.PermissionDialog;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;
import com.google.gson.Gson;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

import static java.lang.System.currentTimeMillis;

//import com.fastaccess.permission.base.PermissionHelper;

public class MainActivity extends BasePermissionActivity implements IView{
    public MainCashFragment mainCashFragment;
    private ShoppingFragment shoppingFragment;
    //    public MainRepayFragment mainRepayFragment;
    public MainMeFragment mainMeFragment;
    @BindView(R.id.iv_shopping)
    ImageView ivShopping;
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
    @BindView(R.id.ll_me)
    LinearLayout llMe;
    @BindView(R.id.iv_small_lottery)
    ImageView ivSmallLottery;
    private LotteryDialog lotteryDialog;

    private PermissionDialog permissionDialog;

    private FragmentManager manager;
    //    private RadioGroup buttomBar;
    long exitTime;
    private ArrayList<Fragment> fragmentList;

    @BindView(R.id.mainViewPager)
    ViewPager mViewPager;
    private MainActivityAdapter mainAdapter;
    NotActiveDialog notActiveDialog;//未开通钱包的弹窗
    private LotteryResponse lotteryResponse;
    private boolean needRefreshLottery;
    private boolean needSkip2H5;
    private int currentItem;
    SplashAdPresenter splashAdPresenter = null;

    @Override
    protected BasePresenter[] initPresenters() {
        if (splashAdPresenter == null){
            splashAdPresenter = new SplashAdPresenter(this);
        }
        return new BasePresenter[]{splashAdPresenter};
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        baseLayout.setTitleBarAndStatusBar(false, false);
        baseLayout.setTopBarBackgroundColor(R.color.white);
        initViewPagerFragment();
        registerEventBus();
        //跳转至消息列表
        if (getIntent().getBooleanExtra(needTurnToMessageActivity, false)) {
//            Intent intent = new Intent(mBaseContext, MessageActivity.class);
//            startActivity(intent);
        }
        UITest.test(mBaseContext);
    }

    private void fetchUserInfo() {
        BaseApplication.getInstance().fetchUserInfo();
    }

    private void initNotActiveDialog() {
        notActiveDialog = new NotActiveDialog(mBaseContext);
    }

    private void initViewPagerFragment() {
//        buttomBar = (RadioGroup) findViewById(R.id.buttomBar);
        manager = getSupportFragmentManager();
        fragmentList = new ArrayList<>();
        shoppingFragment = new ShoppingFragment();
        fragmentList.add(shoppingFragment);
        mainAdapter = new MainActivityAdapter(manager, fragmentList);
        mViewPager.setAdapter(mainAdapter);
        mViewPager.setOffscreenPageLimit(2);
        resetIconAndTextColor();
        selectIconAndTextColor(0);
    }

    private void initLotteryDialog() {
        lotteryDialog = new LotteryDialog(mBaseContext);
        lotteryDialog.setOnJoinLitener(new LotteryDialog.OnJoinListener() {
            @Override
            public void join() {
                if (LoginHelper.getInstance().hasLoginAndActivation(mBaseContext)) {
                    if (lotteryResponse != null && lotteryResponse.data != null
                            && StringUtils.isNotNull(lotteryResponse.data.activityUrl)) {
                        needRefreshLottery = true;
                        CustomWebViewActivity.startIt(mBaseContext, lotteryResponse.data.activityUrl, "", true);
                    }
                } else {
                    ivSmallLottery.setVisibility(View.VISIBLE);
                }
                lotteryDialog.dismissDialog();
            }

            @Override
            public void cancel() {
                ivSmallLottery.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initPermissionDialog() {
        permissionDialog = new PermissionDialog(mBaseContext);
        permissionDialog.setPermissionStr("需要通讯录权限才可正常使用");
        permissionDialog.setConfirmStr("去开启");
        permissionDialog.setOnChooseListener(new ConfirmDialog.OnChooseListener() {
            @Override
            public void confirm() {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
                //进入设置了，下次onResume时继续判断申请权限
                permissionDialog.dismiss();
            }

            @Override
            public void cancel() {
                permissionDialog.dismiss();
            }
        });
    }

    /**
     * 退出登录，登录成功都应该重新获取周年庆活动状态
     *
     * @param lotteryEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void resetLottery(LotteryEvent lotteryEvent) {
        if (lotteryEvent.skip2H5) {
            needSkip2H5 = true;
//            doLottery();
            needRefreshLottery = false;
        } else {
            needRefreshLottery = true;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginSuccess(LoginSuccessEvent successEvent) {
        setIntent(new Intent());
    }

    /**
     * 6.0以上系统申请通讯录权限
     */
    public void applyContactPermission() {
//        if (PermissionHelper.getInstance(this).isPermissionGranted(Manifest.permission.READ_CONTACTS)) {
//            skipToContactsContract();
//        } else {
//            setPermissionHelper(false, new String[]{Manifest.permission.READ_CONTACTS});
//        }
    }


    @Override
    public void onPermissionGranted(@NonNull String[] permissionName) {
        super.onPermissionGranted(permissionName);
        for (String permissonStr : permissionName) {
            if (Manifest.permission.READ_CONTACTS.equals(permissonStr)) {
                skipToContactsContract();
                break;
            }
        }
    }

    public void skipToContactsContract() {
        try {
            //跳转通讯录
            startActivityForResult(new Intent(Intent.ACTION_PICK,
                    ContactsContract.Contacts.CONTENT_URI), 0);
        } catch (Exception e) {

        }
    }

    @Override
    public void onPermissionReallyDeclined(@NonNull String permissionName) {
        super.onPermissionReallyDeclined(permissionName);
        //禁止不再询问会直接回调这方法
        permissionDialog.show();
    }


    public void selectIconAndTextColor(int selectPos) {
        switch (selectPos) {
            case 0:
                setImageView(ivShopping, R.drawable.ic_shopping_select);
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
//                setImageView(ivRepayment, R.drawable.ic_repayment_select);
//                setTextColor(tvRepayment, R.color.color_00bbc0);
                setImageView(ivMe, R.drawable.ic_me_select);
                setTextColor(tvMe, R.color.color_00bbc0);
                break;

        }
    }

    public void resetIconAndTextColor() {
        setImageView(ivShopping, R.drawable.ic_shopping);
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


    @OnClick({R.id.iv_small_lottery, R.id.ll_recharge, R.id.ll_cash, R.id.ll_repayment, R.id.ll_me})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        resetIconAndTextColor();
        switch (view.getId()) {
            case R.id.iv_small_lottery:
                if (LoginHelper.getInstance().hasLoginAndActivation(mBaseContext)) {
                    if (lotteryResponse != null && lotteryResponse.data != null
                            && StringUtils.isNotNull(lotteryResponse.data.activityUrl)) {
                        needRefreshLottery = true;
                        CustomWebViewActivity.startIt(mBaseContext, lotteryResponse.data.activityUrl, "", true);
                    }
                }
                break;
            case R.id.ll_recharge:
                currentItem = 0;
                mViewPager.setCurrentItem(0, false);
                selectIconAndTextColor(0);
                break;

            case R.id.ll_cash:
                currentItem = 1;
                mViewPager.setCurrentItem(1, false);
                selectIconAndTextColor(1);
                break;
            case R.id.ll_repayment:
                //先判断有没登录，然后再判断是否有钱包资质，满足条件后才进入账单
                if (LoginHelper.getInstance().hasLoginAndGotoLogin(mBaseContext)) {
                    if (LoginHelper.getInstance().hasQualifications()) {
                        RepaymentActivity.startIt(mBaseContext);
                    } else {
                        notActiveDialog.showDialog();
                    }
                    selectIconAndTextColor(2);
                }
//                mViewPager.setCurrentItem(2, false);
//                selectIconAndTextColor(2);
                break;

            case R.id.ll_me:
                currentItem = 2;
                mViewPager.setCurrentItem(2, false);
                selectIconAndTextColor(3);
                break;

            default:
                break;
        }
    }

    @Override
    public void setListener() {

    }

    private void initListener() {
        notActiveDialog.setdismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                resetIconAndTextColor();
                if (mViewPager.getCurrentItem() == 2) {
                    selectIconAndTextColor(3);
                } else {
                    selectIconAndTextColor(mViewPager.getCurrentItem());
                }
            }
        });
    }

    @Override
    public void setData() {
//        doLottery();
        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                addFragment();
                initNotActiveDialog();
                initPermissionDialog();
                initLotteryDialog();
                initListener();
                doApkUpgrade();
                fetchUserInfo();
                fetchSplashAd();
                return false;
            }
        });
    }

	/**
     * 每次创建mainActivity, 获取一次adSplash广告数据
     */
    private void fetchSplashAd() {
        splashAdPresenter.getAdSplashImage();
    }

    private void addFragment() {
        mainCashFragment = new MainCashFragment();
        mainMeFragment = new MainMeFragment();
        fragmentList.add(mainCashFragment);
        fragmentList.add(mainMeFragment);
        mainAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(currentItem, false);
    }

    private void doLottery() {
        String idPerson = LoginHelper.getInstance().getIdPerson();
        //未登录用户idPerson传空即可
        if ("0".equals(idPerson)) {
            idPerson = "";
        }
        ApiImpl.getActivityInfo(null, idPerson, new BaseRequestAgent.ResponseListener<BaseBean>() {
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
                        lotteryResponse = gson.fromJson(originalStr, LotteryResponse.class);
                        //展示活动对话框
                        showLotteryDialog();
                    } else {
                        CommonLoadingView.showErrorToast(errorBean);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        needRefreshLottery = false;
    }

    private void showLotteryDialog() {
        //活动有效期内
        if (lotteryResponse != null && lotteryResponse.data != null && lotteryResponse.data.activatyStatus == 1) {
            //未抽奖，且第一次展示，使用大图
            if (lotteryResponse.data.justDo == 1 && SharePrefUtil.isNeedShowLottery()) {
                //跳到h5页面的时候就不展示对话框了
                if (needSkip2H5) {
                    if (StringUtils.isNotNull(lotteryResponse.data.activityUrl)) {
                        needRefreshLottery = true;
                        CustomWebViewActivity.startIt(mBaseContext, lotteryResponse.data.activityUrl, "", true);
                    }
                } else {
                    lotteryDialog.showDialog();
                }
                SharePrefUtil.setNeedShowLottery(false);
                ivSmallLottery.setVisibility(View.GONE);
            } else if (lotteryResponse.data.justDo == 1 || (lotteryResponse.data.justDo == 0 && lotteryResponse.data.winning == 1)) {
                //未抽奖或不可抽奖但已中奖
                ivSmallLottery.setVisibility(View.VISIBLE);
                if (needSkip2H5) {
                    if (StringUtils.isNotNull(lotteryResponse.data.activityUrl)) {
                        needRefreshLottery = true;
                        CustomWebViewActivity.startIt(mBaseContext, lotteryResponse.data.activityUrl, "", true);
                    }
                }
            } else {
                //其余情况不显示抽奖入口
                ivSmallLottery.setVisibility(View.GONE);
            }

        } else {
            ivSmallLottery.setVisibility(View.GONE);
        }
        needSkip2H5 = false;
    }


    private class MainActivityAdapter extends FragmentPagerAdapter {
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
        //初始化时只有购物一个fragment，不需要做这些操作
        if (fragmentList.size() > 1) {
            //从账单返回主界面时需刷新tab的图标和字体颜色
            resetIconAndTextColor();
            if (mViewPager.getCurrentItem() == 2) {
                selectIconAndTextColor(3);
            } else {
                selectIconAndTextColor(mViewPager.getCurrentItem());
            }

            if (downloadApkUtils != null) {
                downloadApkUtils.onActivityResume();
            }
            if (needRefreshLottery) {
//            doLottery();
            }

            if (!LoginHelper.getInstance().hasUploadDeviceNumber() && StringUtils.isNotNull(JPushInterface.getRegistrationID(BaseApplication.getInstance()))) {
                //上传设备号至服务器
//            ApiImpl.saveDeviceNumber(JPushInterface.getRegistrationID(BaseApplication.getInstance()));
            }
        }


    }

    DownloadApkUtils downloadApkUtils = null;

    /**
     * 处理app版本升级
     */
    private void doApkUpgrade() {
        //apk升级
        ApiImpl.sendApkUpgradeRequest(null, new BaseRequestAgent.ResponseListener<ApkUgradeResponse>() {
            @Override
            public void onSuccess(ApkUgradeResponse response) {
                downloadApkUtils = new DownloadApkUtils();
                if (response.data.isUnforceUpdate()) {//安卓升级0无更新1普通2强制
                    long lastShowTime = SharePrefUtil.getLastUpdateApkDialogTime();
                    if (System.currentTimeMillis() - lastShowTime > 24 * 60 * 60 * 1000) {//每隔24小时进应用提示一次
                        SharePrefUtil.setLastUpdateApkDialogTime(System.currentTimeMillis());
                        //WIFI环境自动下载apk
                        if (NetWorkUtils.getCurrentNetworkType() == NetWorkUtils.NETWORK_STATE_WIFI) {
                            downloadApkUtils.downloadApkSilence(mBaseContext,response.data);
                        } else {
                            downloadApkUtils.showUpdateApkDialog(mBaseContext, response.data);
                        }

                    }
                    SharePrefUtil.setNeedUpdateApp(true);
                } else if (response.data.isForceUpdate()) {
                    downloadApkUtils.showUpdateApkDialog(mBaseContext, response.data);
                    SharePrefUtil.setNeedUpdateApp(true);
                } else {
                    //没有新版本app
                    SharePrefUtil.setNeedUpdateApp(false);
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

    /**
     * 设置手势密码的提示dialog
     */
    public void settingPatternOrFingerPrint() {
        if (LoginHelper.getInstance().shouldShowSetting()) {
            FingerPrintHelper fingerHelper = new FingerPrintHelper(mBaseContext);
            if (fingerHelper.isHardwareEnable()) {
                FingerPrintActivity.startIt(mBaseContext, true);
            } else {
                CreateGestureActivity.startIt(mBaseContext);
            }
            LoginHelper.getInstance().reduceRemingTimes();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int whichFragmentInActMain = intent.getIntExtra(Const.whichFragmentInActMain, 0);
        mViewPager.setCurrentItem(whichFragmentInActMain, false);
        setIntent(intent);
    }
}