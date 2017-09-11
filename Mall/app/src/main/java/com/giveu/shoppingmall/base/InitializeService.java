package com.giveu.shoppingmall.base;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.android.volley.mynet.ApiUrl;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.index.view.activity.MainActivity;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.Const;
import com.giveu.shoppingmall.utils.LogUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.util.LogUtils;
import com.networkbench.agent.impl.NBSAppAgent;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.ShareSDK;

/**
 * Created by 513419 on 2017/8/23.
 */

public class InitializeService extends IntentService {
    private static final String APP_INIT_ACTION = "com.giveu.shoppingmall.base.init";

    public InitializeService() {
        super("InitializeService");
    }

    public static void startIt(Context context) {
        Intent intent = new Intent(context, InitializeService.class);
        intent.setAction(APP_INIT_ACTION);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (APP_INIT_ACTION.equals(action)) {
                performInit();
            }
        }
    }

    private void performInit() {
        initLog();
        initBuglyUpdateApp();
        initCrashReport();
        initShareSDK();
    }

    /**
     * bugly的自动更新app
     */
    public static void initBuglyUpdateApp() {
        /**** Beta高级设置*****/
        /**
         * true表示app启动自动初始化升级模块；
         * false不好自动初始化
         * 开发者如果担心sdk初始化影响app启动速度，可以设置为false
         * 在后面某个时刻手动调用
         */
        Beta.autoInit = true;
        /**
         * true表示初始化时自动检查升级
         * false表示不会自动检查升级，需要手动调用Beta.checkUpgrade()方法
         */
        Beta.autoCheckUpgrade = true;
        Beta.largeIconId = R.mipmap.ic_launcher;
        Beta.smallIconId = R.mipmap.ic_launcher;
        /**
         * 设置更新弹窗默认展示的banner，defaultBannerId为项目中的图片资源Id;
         * 当后台配置的banner拉取失败时显示此banner，默认不设置则展示“loading“;
         */
        Beta.defaultBannerId = R.mipmap.ic_launcher;
        /**
         * 设置sd卡的Download为更新资源保存目录;
         * 后续更新资源会保存在此目录，需要在manifest中添加WRITE_EXTERNAL_STORAGE权限;
         */
        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        /**
         * 点击过确认的弹窗在APP下次启动自动检查更新时会再次显示;
         */
        Beta.showInterruptedStrategy = true;
        /**
         * 只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗;
         * 不设置会默认所有activity都可以显示弹窗;
         */
        Beta.canShowUpgradeActs.add(MainActivity.class);
        /**
         *  设置自定义升级对话框UI布局
         */
        Beta.upgradeDialogLayoutId = R.layout.upgrade_dialog;
    }
    private void initCrashReport() {
        try {
            if (!DebugConfig.isDev) {
                //自定义UncaughtExceptionHandler，崩溃之后应用整体销毁
                CrashHandler crashHandler = new CrashHandler(BaseApplication.getInstance());
                //听云
                if (DebugConfig.isOnline) {
                    NBSAppAgent.setLicenseKey("b7cec1e48dab49b98df327a73e4a08ad").withLocationServiceEnabled(true).startInApplication(this.getApplicationContext());
                }
                //开发环境不上报bugly
                Context context = getApplicationContext();
                // 获取当前包名
                String packageName = context.getPackageName();
                // 获取当前进程名
                String processName = BaseApplication.getProcessName(android.os.Process.myPid());
                // 设置是否为上报进程
                CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
                strategy.setUploadProcess(processName == null || processName.equals(packageName));
                // 初始化Bugly//建议在测试阶段建议设置成true，发布时设置为false。
                Bugly.init(BaseApplication.getInstance(),  Const.BUGLY_APPID, true);
//                CrashReportUtil.setUserIdTobugly(LoginHelper.getInstance().loginPersonInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initShareSDK() {
        ShareSDK.initSDK(this);
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
}
