package com.giveu.shoppingmall.base;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.android.volley.mynet.ApiUrl;
import com.giveu.shoppingmall.EventBusIndex;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.Const;
import com.giveu.shoppingmall.utils.LogUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.util.LogUtils;
import com.networkbench.agent.impl.NBSAppAgent;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.eventbus.EventBus;

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
        initCrashReport();
        initShareSDK();
        EventBus.builder().addIndex(new EventBusIndex()).installDefaultEventBus();
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
                CrashReport.initCrashReport(context, Const.BUGLY_APPID, DebugConfig.isDebug, strategy);
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
