package com.giveu.shoppingmall.utils.sharePref;

import android.text.TextUtils;

import com.giveu.shoppingmall.model.bean.response.AdSplashResponse;
import com.giveu.shoppingmall.utils.HardWareUtil;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;

import java.util.UUID;

/**
 * 用于本地持久化存取
 */
public class SharePrefUtil extends AbsSharePref {
    private final String spName = "app_setting";
    private static SharePrefUtil sharePrefUtil;


    @Override
    public String getSharedPreferencesName() {
        return spName;
    }

    @Override
    protected String getUserId() {
        return null;
    }

    public static synchronized SharePrefUtil getInstance() {
        if (sharePrefUtil == null) {
            sharePrefUtil = new SharePrefUtil();
        }
        return sharePrefUtil;
    }


    public static void setNeedWelcome(boolean show) {
        getInstance().putBoolean(SharePrefKeys.SP_WELCOM, show);
    }

    public static boolean getNeedWelcome() {
        return getInstance().getBoolean(SharePrefKeys.SP_WELCOM, true);
    }

    public static String getAppToken() {
        return getInstance().getString(SharePrefKeys.SERVER_TOKEN);
    }

    public synchronized static void setAppToken(String token) {
        getInstance().putString(SharePrefKeys.SERVER_TOKEN, token);
    }


    /**
     * 设置最新版本号
     *
     * @param newVersionCode
     */
    public static void setNewVersionCode(int newVersionCode) {
        getInstance().putInt(SharePrefKeys.NEW_VERSION_CODE, newVersionCode);
    }

    /**
     * 获取最新版本号
     */
    public static int getNewVersionCode() {
        return getInstance().getInt(SharePrefKeys.NEW_VERSION_CODE, 0);
    }

    /**
     * 是否有最新版本
     *
     * @param hasNewVersion
     */
    public static void setNeedUpdateApp(boolean hasNewVersion) {
        getInstance().putBoolean(SharePrefKeys.NeedUpdateApp, hasNewVersion);
    }

    public static boolean needUpdateApp() {
        return getInstance().getBoolean(SharePrefKeys.NeedUpdateApp, false);
    }


    public static String getUUId() {
        String deviceId = getInstance().getString(SharePrefKeys.UUID);
        if (StringUtils.isNull(deviceId)) {
            String imei = HardWareUtil.getIMEI();//获取imei
            if (!TextUtils.isEmpty(imei)) {
                deviceId = imei;
            } else {
                deviceId = UUID.randomUUID().toString();
            }
            getInstance().putString(SharePrefKeys.UUID, deviceId);
        }

        return deviceId;
    }

    public static void setAdSplashImage(AdSplashResponse response) {
        getInstance().putObj(SharePrefKeys.AD_SPLASH_IMAGE, response);
    }

    public static AdSplashResponse getAdSplashImage() {
        return (AdSplashResponse) getInstance().getObj(SharePrefKeys.AD_SPLASH_IMAGE);
    }

    //下载完成标记本地存储
    public static void setDownloadApkFlag(boolean flag) {
        getInstance().putBoolean(SharePrefKeys.DOWNLOAD_APK_FLAG, flag);
    }

    public static boolean getDownloadApkFlag() {
        return getInstance().getBoolean(SharePrefKeys.DOWNLOAD_APK_FLAG, false);
    }

    public static void setLastUpdateApkDialogTime(long l) {
        getInstance().putLong(SharePrefKeys.LAST_APK_UPDATE_DIALOG_SHOW_TIME, l);
    }

    public static long getLastUpdateApkDialogTime() {
        return getInstance().getLong(SharePrefKeys.LAST_APK_UPDATE_DIALOG_SHOW_TIME);
    }

    /**
     * 是否需要显示抽奖活动
     *
     * @param isNeed
     */
    public static void setNeedShowLottery(boolean isNeed) {
        getInstance().putBoolean(SharePrefKeys.SHOW_LOTTERY + LoginHelper.getInstance().getUserId(), isNeed);
    }

    /**
     * 设置是否需要显示抽奖活动
     *
     * @return
     */
    public static boolean isNeedShowLottery() {
        return getInstance().getBoolean(SharePrefKeys.SHOW_LOTTERY + LoginHelper.getInstance().getUserId(), true);
    }

    /**
     * 保存图案密码
     */
    public static void setPatternPwd(String pwd) {
        getInstance().putString(SharePrefKeys.LOCK_PATTERN_PWD + LoginHelper.getInstance().getUserId(), pwd);
    }

    public static void setFingerPrint(boolean hasFingerPrint) {
        getInstance().putBoolean(SharePrefKeys.FINGER_PWD + LoginHelper.getInstance().getUserId(), hasFingerPrint);
    }

    public static boolean hasFingerPrint() {
        return getInstance().getBoolean(SharePrefKeys.FINGER_PWD + LoginHelper.getInstance().getUserId(), false);
    }

    public static String getPatternPwd() {
        return getInstance().getString(SharePrefKeys.LOCK_PATTERN_PWD + LoginHelper.getInstance().getUserId());
    }

    /**
     * 保存商品搜索历史
     *
     * @param searchHistory
     * @return
     */
    public static void saveSearchHistory(String searchHistory) {
        getInstance().putString(SharePrefKeys.SEARCH_HISTORY, searchHistory);
    }

    public static String getSearchHistory() {
        return getInstance().getString(SharePrefKeys.SEARCH_HISTORY);
    }


}
