package com.giveu.shoppingmall.base;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

import com.giveu.shoppingmall.index.activity.AdSplashActivity;
import com.giveu.shoppingmall.index.activity.MainActivity;
import com.giveu.shoppingmall.index.activity.SplashActivity;
import com.giveu.shoppingmall.index.activity.WelcomeActivity;
import com.giveu.shoppingmall.me.activity.CreateGestureActivity;
import com.giveu.shoppingmall.me.activity.GestureLoginActivity;
import com.giveu.shoppingmall.me.activity.VerifyPwdActivity;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.CrashReportUtil;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.sharePref.SharePrefUtil;
import com.giveu.shoppingmall.view.emptyview.CommonLoadingView;
import com.umeng.analytics.MobclickAgent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;


public abstract class BaseActivity extends FragmentActivity implements OnClickListener, IView {
    public BaseLayout baseLayout;
    public BaseActivity mBaseContext;
    private Set<BasePresenter> mAllPresenters = new HashSet<>(1);
    protected Bundle mSavedInstanceState;

    /**
     * 当使用mvp模式时实现这个方法
     */
    protected BasePresenter[] initPresenters() {
        return null;
    }

    private void addPresenters() {
        BasePresenter[] presenters = initPresenters();
        if (presenters != null) {
            mAllPresenters.clear();

            for (int i = 0; i < presenters.length; i++) {
                mAllPresenters.add(presenters[i]);
            }
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
                translateStatusBar();
            }

            initView(mSavedInstanceState);
            addPresenters();
            setListener();
            setData();

        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showShortToast("页面初始化失败，请重试");
            CrashReportUtil.postTryCatchToBugly(e);
            finish();
        }

    }

    /**
     * 点击错误页面的刷新按钮时会调用这个方法
     * {@link CommonLoadingView#setOnClickReloadListener(com.giveu.shoppingmall.view.emptyview.CommonLoadingView.OnClickReloadListener)}
     */
    public void onReload() {

    }

    /**
     * @return true=使用状态栏一体化，false=不使用
     */
    protected boolean isTranslateStatusBar() {
        return false;
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


    private void translateStatusBar() {

        //透明状态栏
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(Color.TRANSPARENT);
                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }
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
        notifyIPresenter(LifeStyle.onResume);
        super.onResume();
    }

    interface LifeStyle{
        String  onCreate = "onCreate";
        String  onStart = "onStart";
        String  onResume = "onResume";
        String  onStop = "onStop";
        String  onPause = "onPause";
        String  onDestroy = "onDestroy";
    }

    private void notifyIPresenter(String methodName) {
        for (BasePresenter presenter : mAllPresenters) {
            if (presenter != null) {
                switch (methodName) {
                    case LifeStyle.onStart:
                        presenter.onStart();
                        break;
                    case LifeStyle.onResume:
                        presenter.onResume();
                        break;
                    case LifeStyle.onStop:
                        presenter.onStop();
                        break;
                    case LifeStyle.onPause:
                        presenter.onPause();
                        break;
                    case LifeStyle.onDestroy:
                        presenter.onDestroy();
                        break;
                }
            }
        }
    }

    @Override
    protected void onStart() {
        notifyIPresenter(LifeStyle.onStart);
        super.onStart();
        //只有Activity不是回收后重建才显示密码框
        if (mSavedInstanceState == null) {
            //判断是否需要输入手势密码
//            dealLockPattern();
        }
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPause(this);
        notifyIPresenter(LifeStyle.onPause);
        super.onPause();

    }

    /**
     * 处理图案锁的逻辑
     */
    private void dealLockPattern() {
        if (this.getClass() == MainActivity.class) {
            String className = getIntent().getStringExtra(MainActivity.lockPatternKey);
            if (SplashActivity.class.getName().equals(className)) {
                //从splash页面过来的
                if (!TextUtils.isEmpty(SharePrefUtil.getInstance().getPatternPwd())) {
                    //如果设置过密码
                    GestureLoginActivity.startIt(this);
                } else {
                    VerifyPwdActivity.startIt(mBaseContext, false);
                }
                clearLockPatternExtraIntent();
                return;
            }
//            else if (LoginActivity.class.getName().equals(className)) {
//                //从登录页面过来的
//                if (TextUtils.isEmpty(SharePrefUtil.getInstance().getPatternPwd())) {
//                    //如果没有设置手势，第一次提示设置
//                    ((MainActivity) this).showPatternLockTipDialog();
//                }
//                clearLockPatternExtraIntent();
//                return;
//            }
        }
        //如果上面页面做个处理了就不处理了
        if (BaseApplication.getInstance().isOverTimeForPattern() && isThisActivityNeedPattern()) {
            if (!TextUtils.isEmpty(SharePrefUtil.getInstance().getPatternPwd())) {
                //如果设置过密码
                GestureLoginActivity.startIt(this);
            } else {
                VerifyPwdActivity.startIt(mBaseContext, false);
            }
        }
    }

    public void clearLockPatternExtraIntent() {
        getIntent().putExtra(MainActivity.lockPatternKey, "");
    }

    /**
     * 判断当前activity超时是否需要锁屏
     */
    private boolean isThisActivityNeedPattern() {
        Class[] classes = new Class[]{AdSplashActivity.class, SplashActivity.class,
                WelcomeActivity.class, GestureLoginActivity.class, CreateGestureActivity.class, VerifyPwdActivity.class};
        List<Class> notNeedPatternActivitys = Arrays.asList(classes);

        if (notNeedPatternActivitys.contains(this.getClass())) {
            return false;
        }
        return true;
    }


    @Override
    protected void onStop() {
        notifyIPresenter(LifeStyle.onStop);
        super.onStop();

        BaseApplication.getInstance().setLastestStopMillis(System.currentTimeMillis());
    }

    @Override
    protected void onDestroy() {
        BaseApplication.getInstance().removeActivity(this);
        notifyIPresenter(LifeStyle.onDestroy);
        //关闭软键盘
        CommonUtils.closeSoftKeyBoard(this);

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
