package com.giveu.shoppingmall.me.view.activity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.mynet.ApiUrl;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.base.DebugConfig;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.sharePref.DevSettingSharePref;
import com.giveu.shoppingmall.widget.IosSwitch;
import com.giveu.shoppingmall.widget.dialog.CustomDialogUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 开发人员调试
 */

public class DevSettingActivity extends BaseActivity {

    @BindView(R.id.tv_app_info)
    TextView tvAppInfo;
    @BindView(R.id.switch_log)
    IosSwitch switchLog;
    @BindView(R.id.rb_api_dev)
    RadioButton rbApiDev;
    @BindView(R.id.rb_api_test)
    RadioButton rbApiTest;
    @BindView(R.id.rb_api_online)
    RadioButton rbApiOnline;
    @BindView(R.id.rg)
    RadioGroup rg;
    @BindView(R.id.et_my_api)
    EditText etMyApi;
    @BindView(R.id.et_web_address)
    EditText etWebAddress;
    @BindView(R.id.tv_confirm_api)
    TextView tvConfirmApi;


    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_dev_setting);
        baseLayout.setTitle("开发人员调试");

        baseLayout.setBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialogUtil dialogUtil = new CustomDialogUtil(mBaseContext);
                dialogUtil.getDialogMode1("提示", "如果你切换了环境，请杀死进程重新登录，你想执行什么操作？", "上一页", "杀死进程", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LoginHelper.getInstance().logout();
                        BaseApplication.getInstance().finishActivityAndKillProcess();
                    }
                }).show();
            }
        });

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
        try {
            ApplicationInfo info = this.getPackageManager()
                    .getApplicationInfo(getPackageName(),
                            PackageManager.GET_META_DATA);
            String channel = info.metaData.getString("UMENG_CHANNEL");
            map.put("channel", channel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Gson gson2 = new GsonBuilder().setPrettyPrinting().create();
        String log = gson2.toJson(map);
        tvAppInfo.setText(log);
        switchLog.setChecked(LogUtils.allowI);
        rbApiOnline.setText("正式：" + DebugConfig.BASE_URL_ONLINE);
        rbApiDev.setText("开发：" + DebugConfig.BASE_URL_DEV);
        rbApiTest.setText("测试：" + DebugConfig.BASE_URL_TEST);

    }


    @Override
    public void setListener() {
        super.setListener();

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_api_dev:
                        DevSettingSharePref.getInstance().setDebugBaseUrl(DebugConfig.BASE_URL_DEV);
                        break;

                    case R.id.rb_api_test:
                        DevSettingSharePref.getInstance().setDebugBaseUrl(DebugConfig.BASE_URL_TEST);
                        break;

                    case R.id.rb_api_online:
                        DevSettingSharePref.getInstance().setDebugBaseUrl(DebugConfig.BASE_URL_ONLINE);
                        break;
                }
            }
        });

        switchLog.setOnToggleListener(new IosSwitch.OnToggleListener() {
            @Override
            public void onSwitchChangeListener(boolean isChecked) {
                if (isChecked) {
                    DevSettingSharePref.getInstance().setNeedLog(true);
                }
            }
        });
    }

    @Override
    public void setData() {

    }

    @OnClick(R.id.tv_confirm_api)
    public void clickConfirmApi() {
        final String api = etMyApi.getText().toString().trim();
        if (TextUtils.isEmpty(api)) {
            ToastUtils.showShortToast("请输入域名");
            return;
        }

        new CustomDialogUtil(mBaseContext).getDialogMode1("提示", "请输入正确的域名，否则后果自负", "取消", "确定", null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DevSettingSharePref.getInstance().setDebugBaseUrl(api);
            }
        }).show();
    }

    @OnClick(R.id.tv_confirm_web)
    public void clickConfirmWeb() {
        final String url = etWebAddress.getText().toString().trim();
        if (TextUtils.isEmpty(url)) {
            ToastUtils.showShortToast("请输入地址");
            return;
        }
        CustomWebViewActivity.startIt(mBaseContext, url, "");
    }

}
