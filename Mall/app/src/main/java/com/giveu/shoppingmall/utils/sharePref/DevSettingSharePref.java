package com.giveu.shoppingmall.utils.sharePref;

/**
 * 只在{@link com.giveu.shoppingmall.me.view.activity.DevSettingActivity 用来保存设置信息}
 */
public class DevSettingSharePref extends AbsSharePref {

    private static DevSettingSharePref instance;
    private final String spName = "dev_model_setting";
    public final String DEV_NEED_LOG = "DEV_NEED_LOG";
    public final String DEV_BASE_URL = "DEV_BASE_URL";

    public static DevSettingSharePref getInstance() {
        if (instance == null) {
            instance = new DevSettingSharePref();
        }
        return instance;
    }

    public void setNeedLog(boolean needLog) {
        putBoolean(DEV_NEED_LOG, needLog);
    }

    public boolean getNeedLog() {
        return getBoolean(DEV_NEED_LOG);
    }

    public void setDebugBaseUrl(String debugBaseUrl) {
        putString(DEV_BASE_URL, debugBaseUrl);
    }

    public String getDebugBaseUrl() {
        return getString(DEV_BASE_URL);
    }

    @Override
    public String getSharedPreferencesName() {
        return spName;
    }

    @Override
    protected String getUserId() {
        return null;
    }


}