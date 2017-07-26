package com.giveu.shoppingmall.index.view.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.fastaccess.permission.base.PermissionHelper;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.base.BasePermissionActivity;
import com.giveu.shoppingmall.cash.view.fragment.MainCashFragment;
import com.giveu.shoppingmall.me.view.activity.CreateGestureActivity;
import com.giveu.shoppingmall.me.view.activity.FingerPrintActivity;
import com.giveu.shoppingmall.me.view.activity.RepaymentActivity;
import com.giveu.shoppingmall.me.view.dialog.NotActiveDialog;
import com.giveu.shoppingmall.me.view.fragment.MainMeFragment;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.ApkUgradeResponse;
import com.giveu.shoppingmall.recharge.view.fragment.RechargeFragment;
import com.giveu.shoppingmall.utils.Const;
import com.giveu.shoppingmall.utils.DownloadApkUtils;
import com.giveu.shoppingmall.utils.FingerPrintHelper;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.sharePref.SharePrefUtil;
import com.giveu.shoppingmall.widget.dialog.ConfirmDialog;
import com.giveu.shoppingmall.widget.dialog.PermissionDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

import static java.lang.System.currentTimeMillis;

public class MainActivity extends BasePermissionActivity {
    public RechargeFragment rechargeFragment;
    public MainCashFragment mainCashFragment;
    //    public MainRepayFragment mainRepayFragment;
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

    private PermissionDialog permissionDialog;

    private FragmentManager manager;
    //    private RadioGroup buttomBar;
    long exitTime;
    private ArrayList<Fragment> fragmentList;

    @BindView(R.id.mainViewPager)
    ViewPager mViewPager;
    @BindView(R.id.rb1)
    RadioButton rb1;
    private MainActivityAdapter mainAdapter;
    NotActiveDialog notActiveDialog;//未开通钱包的弹窗

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
//        buttomBar = (RadioGroup) findViewById(R.id.buttomBar);
        baseLayout.setTitleBarAndStatusBar(false, false);
        manager = getSupportFragmentManager();
        fragmentList = new ArrayList<>();
        rechargeFragment = new RechargeFragment();
        mainCashFragment = new MainCashFragment();
//        mainRepayFragment = new MainRepayFragment();
        mainMeFragment = new MainMeFragment();
//        fragmentList.add(rechargeFragment);
        fragmentList.add(mainCashFragment);
//        fragmentList.add(mainRepayFragment);
        fragmentList.add(mainMeFragment);
        mainAdapter = new MainActivityAdapter(manager, fragmentList);
        mViewPager.setAdapter(mainAdapter);
        mViewPager.setOffscreenPageLimit(2);
        //跳转至消息列表
        if (getIntent().getBooleanExtra(needTurnToMessageActivity, false)) {
//            Intent intent = new Intent(mBaseContext, MessageActivity.class);
//            startActivity(intent);
        }
        notActiveDialog = new NotActiveDialog(mBaseContext);
        UITest.test(mBaseContext);
        resetIconAndTextColor();
        selectIconAndTextColor(0);
        mViewPager.setCurrentItem(0);
        BaseApplication.getInstance().fetchUserInfo();
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
            public void cancle() {
                permissionDialog.dismiss();
            }
        });
    }


    /**
     * 6.0以上系统申请通讯录权限
     */
    public void applyContactPermission() {
        if (PermissionHelper.getInstance(this).isPermissionGranted(Manifest.permission.READ_CONTACTS)) {
            skipToContactsContract();
        } else {
            setPermissionHelper(false, new String[]{Manifest.permission.READ_CONTACTS});
        }
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
        //跳转通讯录
        startActivityForResult(new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI), 0);
    }

    @Override
    public void onPermissionReallyDeclined(@NonNull String permissionName) {
        super.onPermissionReallyDeclined(permissionName);
        //禁止不再询问会直接回调这方法
        permissionDialog.show();
    }


    public void selectIconAndTextColor(int selectPos) {
        switch (selectPos) {
         /*   case 0:
                setImageView(ivRecharge, R.drawable.ic_recharge_select);
                setTextColor(tvRecharge, R.color.color_00bbc0);
                break;*/
            case 0:
                setImageView(ivCash, R.drawable.ic_cash_select);
                setTextColor(tvCash, R.color.color_00bbc0);
                break;
            case 1:
                setImageView(ivRepayment, R.drawable.ic_repayment_select);
                setTextColor(tvRepayment, R.color.color_00bbc0);
                break;
            case 2:
//                setImageView(ivRepayment, R.drawable.ic_repayment_select);
//                setTextColor(tvRepayment, R.color.color_00bbc0);
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


    @OnClick({R.id.ll_recharge, R.id.ll_cash, R.id.ll_repayment, R.id.ll_me})
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
                mViewPager.setCurrentItem(0, false);
                selectIconAndTextColor(0);
                break;
            case R.id.ll_repayment:
                //先判断有没登录，然后再判断是否有钱包资质，满足条件后才进入账单
                if (LoginHelper.getInstance().hasLoginAndGotoLogin(mBaseContext)) {
                    if (LoginHelper.getInstance().hasQualifications()) {
                        RepaymentActivity.startIt(mBaseContext);
                    } else {
                        notActiveDialog.showDialog();
                    }
                    selectIconAndTextColor(1);
                }
//                mViewPager.setCurrentItem(2, false);
//                selectIconAndTextColor(2);
                break;

            case R.id.ll_me:
                mViewPager.setCurrentItem(1, false);
                selectIconAndTextColor(2);
                break;

            default:
                break;
        }
    }

    @Override
    public void setListener() {
        notActiveDialog.setdismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                resetIconAndTextColor();
                switch (mViewPager.getCurrentItem()) {
                    case 0:
                        selectIconAndTextColor(0);
                        break;
                    case 1:
                        selectIconAndTextColor(2);
                        break;
                }
            }
        });
    }

    @Override
    public void setData() {
        doApkUpgrade();
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
        //从账单返回主界面时需刷新tab的图标和字体颜色
        resetIconAndTextColor();
        switch (mViewPager.getCurrentItem()) {
            case 0:
                selectIconAndTextColor(0);
                break;
            case 1:
                selectIconAndTextColor(2);
                break;
        }
        if (downloadApkUtils != null) {
            downloadApkUtils.onActivityResume();
        }

        if (!LoginHelper.getInstance().hasUploadDeviceNumber() && StringUtils.isNotNull(JPushInterface.getRegistrationID(BaseApplication.getInstance()))) {
            //上传设备号至服务器
//            ApiImpl.saveDeviceNumber(JPushInterface.getRegistrationID(BaseApplication.getInstance()));
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
        setIntent(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            if (rechargeFragment != null) {
                // ContentProvider展示数据类似一个单个数据库表
                // ContentResolver实例带的方法可实现找到指定的ContentProvider并获取到ContentProvider的数据
                ContentResolver reContentResolverol = getContentResolver();
                // URI,每个ContentProvider定义一个唯一的公开的URI,用于指定到它的数据集
                Uri contactData = data.getData();
                // 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
                Cursor cursor = mBaseContext.managedQuery(contactData, null, null, null, null);
                if (cursor.getCount() == 0) {
                    permissionDialog.show();
                    return;
                }
                cursor.moveToFirst();
                // 条件为联系人ID
                String contactId = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.Contacts._ID));
                // 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
                Cursor phone = reContentResolverol.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                                + contactId, null, null);
                while (phone != null && phone.moveToNext()) {
                    //填入号码
                    String usernumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    final StringBuilder sb = new StringBuilder(usernumber.replaceAll(" ", ""));
                    if (sb.toString().length() != 11) {
                        ToastUtils.showShortToast("手机号码格式有误");
                    } else {
                        sb.insert(3, " ");
                        sb.insert(8, " ");
                        rechargeFragment.setPhoneText(sb.toString());
                    }
                }
                if (phone != null) {
                    phone.close();
                }
            }

        }
    }
}