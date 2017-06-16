package com.giveu.shoppingmall.index.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.index.fragment.MainMeFragment;
import com.giveu.shoppingmall.index.fragment.MainCashFragment;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.ApkUgradeResponse;
import com.giveu.shoppingmall.ordercreate.fragment.MainShoppingFragment;
import com.giveu.shoppingmall.riskcontrol.fragment.MainRepayFragment;
import com.giveu.shoppingmall.utils.Const;
import com.giveu.shoppingmall.utils.DownloadApkUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.sharePref.SharePrefUtil;
import com.giveu.shoppingmall.view.dialog.PatternLockSetTipDialog;

import java.util.ArrayList;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;

import static java.lang.System.currentTimeMillis;

public class MainActivity extends BaseActivity {
    public MainShoppingFragment mainShoppingFragment;
    public MainCashFragment mainCashFragment;
    public MainRepayFragment mainRepayFragment;
    public MainMeFragment mainMeFragment;

    private FragmentManager manager;
    private int nowPosition;
    private RadioGroup buttomBar;
    long exitTime;
    private ArrayList<Fragment> fragmentList;

    @BindView(R.id.mainViewPager)
    ViewPager mViewPager;
    @BindView(R.id.rb1)
    RadioButton rb1;
    private MainActivityAdapter mainAdapter;


    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        buttomBar = (RadioGroup) findViewById(R.id.buttomBar);
        baseLayout.setTitleBarAndStatusBar(false, false);
        manager = getSupportFragmentManager();
        fragmentList = new ArrayList<>();
        mainShoppingFragment = new MainShoppingFragment();
        mainCashFragment = new MainCashFragment();
        mainRepayFragment = new MainRepayFragment();
        mainMeFragment = new MainMeFragment();
        fragmentList.add(mainShoppingFragment);
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
    }


    @Override
    public void setListener() {
        buttomBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
        });
    }

    @Override
    public void setData() {
        doApkUpgrade();
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