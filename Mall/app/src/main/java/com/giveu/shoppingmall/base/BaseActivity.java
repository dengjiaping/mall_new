package com.giveu.shoppingmall.base;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

import com.giveu.shoppingmall.index.view.activity.AdSplashActivity;
import com.giveu.shoppingmall.index.view.activity.MainActivity;
import com.giveu.shoppingmall.index.view.activity.SplashActivity;
import com.giveu.shoppingmall.index.view.activity.WelcomeActivity;
import com.giveu.shoppingmall.me.view.activity.CreateGestureActivity;
import com.giveu.shoppingmall.me.view.activity.FingerPrintActivity;
import com.giveu.shoppingmall.me.view.activity.GestureLoginActivity;
import com.giveu.shoppingmall.me.view.activity.LoginActivity;
import com.giveu.shoppingmall.me.view.activity.VerifyPwdActivity;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.CrashReportUtil;
import com.giveu.shoppingmall.utils.EventBusUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.SystemBarHelper;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.sharePref.SharePrefUtil;
import com.giveu.shoppingmall.widget.dialog.LoadingDialog;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;
import com.umeng.analytics.MobclickAgent;

import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;


public abstract class BaseActivity extends FragmentActivity implements OnClickListener, IView {
    public BaseLayout baseLayout;
    public BaseActivity mBaseContext;
    private BasePresenter[] mAllPresenters = new BasePresenter[]{};
    protected Bundle mSavedInstanceState;
    private boolean registerEventBus;

    /**
     * 当使用mvp模式时实现这个方法
     */
    protected BasePresenter[] initPresenters() {
        return null;
    }

    private void addPresenters() {
        BasePresenter[] presenters = initPresenters();
        if (presenters != null) {
            mAllPresenters = presenters;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            mSavedInstanceState = savedInstanceState;
            mBaseContext = this;
            BaseApplication.getInstance().addActivity(this);

            //强制竖屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            if (isTranslateStatusBar()) {
                setTranslucentStatus(this);
            }


            initView(mSavedInstanceState);
            addPresenters();
            setListener();
            setData();
            if (registerEventBus) {
                //注册
                EventBusUtils.register(mBaseContext);
            }

        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showShortToast("页面初始化失败，请重试");
            CrashReportUtil.postTryCatchToBugly(e);
            finish();
        }

    }

    /**
     * 点击错误页面的刷新按钮时会调用这个方法
     * {@link CommonLoadingView#setOnClickReloadListener(com.giveu.shoppingmall.widget.emptyview.CommonLoadingView.OnClickReloadListener)}
     */
    public void onReload() {

    }


    /**
     * @return true=使用状态栏一体化，false=不使用
     */
    private boolean isTranslateStatusBar() {
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) && translateStatusBar()) {
            return true;
        }
        return false;
    }

    /**
     * 子类重写此方法来控制是否状态栏一体化
     *
     * @return
     */
    protected boolean translateStatusBar() {
        return true;
    }

//    /**
//     * @return true=使用点击空白处收起键盘，false=不使用
//     */
//    protected boolean isDispatchTouchEvent() {
//        return true;
//    }
//
//    /**
//     * 点击空白处收起键盘
//     * @return
//     */
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        if (isDispatchTouchEvent()) {
//            if (event.getAction() == MotionEvent.ACTION_DOWN &&
//                    getCurrentFocus() != null &&
//                    getCurrentFocus().getWindowToken() != null) {
//
//                InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                mInputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//
//            }
//        }
//        return super.dispatchTouchEvent(event);
//    }


    public static void setTranslucentStatus(Activity activity) {
        Window window = activity.getWindow();
        // 默认主色调为白色, 如果是6.0或者是miui6、flyme4以上, 设置状态栏文字为黑色, 否则给状态栏着色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        //设置状态栏字体颜色
        SystemBarHelper.setStatusBarDarkMode(activity);
    }


    /**
     * 重写了activity的setContentView
     *
     * @param layoutResID
     */
    @Override
    public void setContentView(int layoutResID) {
        View view = View.inflate(this, layoutResID, null);
        this.setContentView(view);
    }

    /**
     * 重写了activity的setContentView
     *
     * @param view
     */
    @Override
    public void setContentView(View view) {
        baseLayout = new BaseLayout(this);
        baseLayout.addContentView(view);
        baseLayout.setBackClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity.this.finish();
            }
        });
        baseLayout.setOnClickReloadListener(new CommonLoadingView.OnClickReloadListener() {
            @Override
            public void onClickReload() {
                onReload();
            }
        });

        super.setContentView(baseLayout);

        //baseactivity中引入butterknife，子类中直接使用就可以了
        ButterKnife.bind(this);

        baseLayout.setStatusBarVisiable(isTranslateStatusBar());
    }

    /**
     * onCreate第一步
     *
     * @param savedInstanceState
     */
    public abstract void initView(Bundle savedInstanceState);

    /**
     * onCreate 第二步
     * 使用了butterknife之后这个setListener就可以省了
     */
    public void setListener() {

    }

    /**
     * onCreate第三步
     */
    public abstract void setData();


    @Override
    protected void onResume() {
        MobclickAgent.onResume(this);
        BasePresenter.notifyIPresenter(BasePresenter.LifeStyle.onResume, mAllPresenters);
        super.onResume();
    }

    @Override
    protected void onStart() {
        BasePresenter.notifyIPresenter(BasePresenter.LifeStyle.onStart, mAllPresenters);
        super.onStart();
        //只有Activity不是回收后重建才显示密码框
        if (mSavedInstanceState == null) {
            //判断是否需要输入手势密码
            dealLockPattern();
        }
    }

    @Override
    public void showLoading() {
        LoadingDialog.showIfNotExist(mBaseContext, false);
    }

    @Override
    public void hideLoding() {
        LoadingDialog.dismissIfExist();
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPause(this);
        BasePresenter.notifyIPresenter(BasePresenter.LifeStyle.onPause, mAllPresenters);
        super.onPause();

    }


    /**
     * 处理图案锁的逻辑
     */
    private void dealLockPattern() {
        
        if (this.getClass() == MainActivity.class) {
            String className = getIntent().getStringExtra(MainActivity.lockPatternKey);
            if (SplashActivity.class.getName().equals(className)) {
                if (LoginHelper.getInstance().hasLogin()) {
                    //从splash页面过来的
                    if (SharePrefUtil.hasFingerPrint()) {
                        FingerPrintActivity.startIt(mBaseContext, false);
                    } else if (!TextUtils.isEmpty(SharePrefUtil.getPatternPwd())) {
                        //如果设置过密码
                        GestureLoginActivity.startIt(this);
                    } else {
                        VerifyPwdActivity.startIt(mBaseContext, false, true);
                    }
                }
                clearLockPatternExtraIntent();
                return;
            } else if (LoginActivity.class.getName().equals(className) || VerifyPwdActivity.class.getName().equals(className)) {
                //从登录页面过来的或密码解锁界面
                if (TextUtils.isEmpty(SharePrefUtil.getPatternPwd()) && !SharePrefUtil.hasFingerPrint()) {
                    //如果没有设置手势，第一次提示设置
                    ((MainActivity) this).settingPatternOrFingerPrint();
                }
                clearLockPatternExtraIntent();
                return;
            }
        }
        //如果上面页面做个处理了就不处理了
        if (BaseApplication.getInstance().isOverTimeForPattern() && isThisActivityNeedPattern() && LoginHelper.getInstance().hasLogin()) {
            if (SharePrefUtil.hasFingerPrint()) {
                FingerPrintActivity.startIt(mBaseContext, false);
            } else if (!TextUtils.isEmpty(SharePrefUtil.getPatternPwd())) {
                //如果设置过密码
                GestureLoginActivity.startIt(this);
            } else {
                VerifyPwdActivity.startIt(mBaseContext, false, true);
            }
        }
    }

    public void clearLockPatternExtraIntent() {
        getIntent().putExtra(MainActivity.lockPatternKey, "");
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());

        //设置字体大小不随系统字体大小而改变
        return res;
    }

    /**
     * 判断当前activity超时是否需要锁屏
     */
    private boolean isThisActivityNeedPattern() {
        Class[] classes = new Class[]{AdSplashActivity.class, SplashActivity.class, FingerPrintActivity.class, LoginActivity.class,
                WelcomeActivity.class, GestureLoginActivity.class, CreateGestureActivity.class, VerifyPwdActivity.class};
        List<Class> notNeedPatternActivitys = Arrays.asList(classes);

        if (notNeedPatternActivitys.contains(this.getClass())) {
            return false;
        }
        return true;
    }

    /**
     * 注册EventBus
     */
    protected void registerEventBus() {
        registerEventBus = true;
    }


    @Override
    protected void onStop() {
        BasePresenter.notifyIPresenter(BasePresenter.LifeStyle.onStop, mAllPresenters);
        super.onStop();

        BaseApplication.getInstance().setLastestStopMillis(System.currentTimeMillis());
    }

    @Override
    protected void onDestroy() {
        BaseApplication.getInstance().removeActivity(this);
        BasePresenter.notifyIPresenter(BasePresenter.LifeStyle.onDestroy, mAllPresenters);
        //关闭软键盘
        CommonUtils.closeSoftKeyBoard(this);
        if (registerEventBus) {
            //反注册
            EventBusUtils.unregister(mBaseContext);
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        //防止快速点击
        if (CommonUtils.isFastDoubleClick(view.getId())) {
            return;
        }
    }

    /**
     * {@link IView}
     */
    @Override
    public Activity getAct() {
        return mBaseContext;
    }


}
