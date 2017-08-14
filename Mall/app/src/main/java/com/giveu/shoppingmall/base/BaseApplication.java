package com.giveu.shoppingmall.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.android.volley.mynet.ApiUrl;
import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.EventBusIndex;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.index.view.activity.MainActivity;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.LoginResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.Const;
import com.giveu.shoppingmall.utils.EventBusUtils;
import com.giveu.shoppingmall.utils.LogUtil;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.listener.SuccessOrFailListener;
import com.giveu.shoppingmall.utils.sharePref.SharePrefUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.util.LogUtils;
import com.networkbench.agent.impl.NBSAppAgent;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;


/**
 */
public class BaseApplication extends MultiDexApplication {
    private static BaseApplication mInstance;
    public List<Activity> undestroyActivities;
    private static PauseOnScrollListener imageLoaderPauseOnScrollListener;
    private Timer tokenTimer;
    private String beforePayActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        mInstance = this;
        undestroyActivities = new ArrayList<Activity>();

        initLog();
        initPush();
        initCrashReport();
        initImageLoader();

        initTokenDaemon();
        initProvinceList();
        initShareSDK();
        // fetchUserInfo();
        //添加索引到EventBus默认的单例中
        EventBus.builder().addIndex(new EventBusIndex()).installDefaultEventBus();
    }

    private void initShareSDK() {
        ShareSDK.initSDK(this);
    }


    /**
     * 10分钟调一次获取token
     */
    private void initTokenDaemon() {
        tokenTimer = new Timer();
        tokenTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                ApiImpl.getAppToken(null, null);
            }
        }, 0, 10 * 60 * 1000);//10分钟调一次获取token 10 * 60 * 1000
    }

    public void initProvinceList() {
        getProvinceCityBean(null, null);
    }

    /**
     * 获取省市区信息
     * 有缓存从缓存中取，没有缓存从网络获取
     */
    public void getProvinceCityBean(Activity loadingDialogContext, final SuccessOrFailListener listener) {
//        if (provinceCityBean == null) {
//
//        } else {
//            if (listener != null) {
//                listener.onSuccess(provinceCityBean);
//            }
//        }
    }

    public String getBeforePayActivity() {
        return beforePayActivity;
    }

    public void setBeforePayActivity(String beforePayActivity) {
        this.beforePayActivity = beforePayActivity;
    }

    public void fetchUserInfo() {
        if (LoginHelper.getInstance().hasLogin()) {
            ApiImpl.getUserInfo(null, LoginHelper.getInstance().getIdPerson(), LoginHelper.getInstance().getUserId(), new BaseRequestAgent.ResponseListener<LoginResponse>() {
                @Override
                public void onSuccess(LoginResponse response) {
                    response.data.accessToken = SharePrefUtil.getAppToken();
                    LoginHelper.getInstance().saveLoginStatus(response.data);
                    //发送通知，使订阅者更新ui
                    EventBusUtils.poseEvent(response.data);
                }

                @Override
                public void onError(BaseBean errorBean) {
                }
            });
        }
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    private void initLog() {
        try {
            LogUtil.allowLog = DebugConfig.isDebug;
            LogUtils.allowI = DebugConfig.isDebug;
            LogUtils.allowD = DebugConfig.isDebug;
            LogUtils.allowE = DebugConfig.isDebug;
            LogUtils.allowW = DebugConfig.isDebug;

            //检查apk相关信息是否正确
            Map<String, Object> map = new HashMap<>();
            if (DebugConfig.isOnline) {
                map.put("isOnline", DebugConfig.isOnline);
            }
            if (DebugConfig.isDev) {
                map.put("isDev", DebugConfig.isDev);
            }
            if (DebugConfig.isTest) {
                map.put("isTest", DebugConfig.isTest);
            }
            map.put("versionCode", CommonUtils.getVersionCode());
            map.put("versionName", CommonUtils.getVersionName());
            map.put("sampleApi", ApiUrl.personCenter_account_login);
            map.put("updateApi", ApiUrl.personCenter_account_getVersion);
            String log = new Gson().toJson(map);
            LogUtil.onlineLog(log);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initCrashReport() {
        try {

            if (!DebugConfig.isDev) {
                //自定义UncaughtExceptionHandler，崩溃之后应用整体销毁
                CrashHandler crashHandler = new CrashHandler(this);

                //听云
                if (DebugConfig.isOnline){
                    NBSAppAgent.setLicenseKey("b7cec1e48dab49b98df327a73e4a08ad").withLocationServiceEnabled(true).startInApplication(this.getApplicationContext());
                }

                //开发环境不上报bugly
                Context context = getApplicationContext();
                // 获取当前包名
                String packageName = context.getPackageName();
                // 获取当前进程名
                String processName = getProcessName(android.os.Process.myPid());
                // 设置是否为上报进程
                CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
                strategy.setUploadProcess(processName == null || processName.equals(packageName));
                // 初始化Bugly//建议在测试阶段建议设置成true，发布时设置为false。
                CrashReport.initCrashReport(context, Const.BUGLY_APPID, DebugConfig.isDebug, strategy);

//                CrashReportUtil.setUserIdTobugly(LoginHelper.getInstance().loginPersonInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initPush() {
//        JPushInterface.setDebugMode(DebugConfig.isDebug);    // 设置开启日志,发布时请关闭日志
//        JPushInterface.init(this);
//        //如果用户未登录，那么不会收到推送
//        if (!LoginHelper.getInstance().hasLogin()) {
//            JPushInterface.stopPush(mInstance);
//        }
    }


    public static BaseApplication getInstance() {
        return mInstance;
    }


    /**
     * 初始化ImageLoader
     */
    private void initImageLoader() {
        File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());
        // LogUtils.i(cacheDir.getAbsolutePath());
        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.color.white).showImageForEmptyUri(R.color.white).showImageOnFail(R.color.white).cacheInMemory(false).cacheOnDisk(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT).bitmapConfig(Bitmap.Config.RGB_565).build();

        ImageLoaderConfiguration cofig = new ImageLoaderConfiguration.Builder(getApplicationContext()).threadPoolSize(4)
                // default
                .threadPriority(Thread.NORM_PRIORITY - 2)
                // default
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                // default
                .denyCacheImageMultipleSizesInMemory().memoryCache(new LRULimitedMemoryCache(2 * 1024 * 1024))
                // .memoryCache(new WeakMemoryCache())
                // .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13)
                // default
                // .diskCache(new UnlimitedDiscCache(cacheDir))
                // default
                .diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(100).diskCacheFileNameGenerator(new Md5FileNameGenerator())
                // default
                // .imageDownloader(new
                // BaseImageDownloader(getApplicationContext())) // default
                .imageDownloader(new BaseImageDownloader(getApplicationContext(), 5 * 1000, 30 * 1000))// connectTimeout 5s readTimeout 30s
                // .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                // // default
                // .writeDebugLogs()
                .defaultDisplayImageOptions(options)
                .build();

        ImageLoader.getInstance().init(cofig);

    }

    public static PauseOnScrollListener getImageLoaderPauseOnScrollListener() {
        if (imageLoaderPauseOnScrollListener == null) {
            imageLoaderPauseOnScrollListener = new PauseOnScrollListener(ImageLoader.getInstance(), false, false);
        }

        return imageLoaderPauseOnScrollListener;
    }

    public void addActivity(Activity activity) {
        undestroyActivities.add(activity);
    }

    public void removeActivity(Activity activity) {
        undestroyActivities.remove(activity);
    }

    /**
     * finish指定activity
     *
     * @param clzz
     */
    public void finishActivity(Class clzz) {
        for (Activity activity : undestroyActivities) {
            if (activity.getClass() == clzz) {
                activity.finish();
            }
        }
    }

    /**
     * 退出登录后需finsh所有activity
     */
    public void finishAllActivity() {
        for (Activity activity : undestroyActivities) {
            if (activity != null) {
                activity.finish();
            }
        }
    }

    /**
     * 关闭除MainActivity外的所有界面，用于服务器返回登录过期时的操作
     */
    public void finishAllExceptMainActivity() {
        for (Activity activity : undestroyActivities) {
            if (activity != null && activity.getClass() != MainActivity.class) {
                activity.finish();
            }
        }
    }

    /**
     * 关闭Activity列表中的所有Activity
     */
    public void finishActivityAndKillProcess() {
        finishAllActivity();
        //杀死该应用进程
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public boolean isOverTimeForPattern() {
        long nowMillis = System.currentTimeMillis();
        if (nowMillis - lastestStopMillis > 10 * 60 * 1000) {//10分钟
//        if (nowMillis - lastestStopMillis > 5 * 1000){
            return true;
        }
        return false;
    }

    private long lastestStopMillis = -1;//最近一次的页面stop时间

    /**
     * 最近一次的页面stop时间
     */
    public void setLastestStopMillis(long millis) {
        lastestStopMillis = millis;
    }


}
