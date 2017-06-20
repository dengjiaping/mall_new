package com.giveu.shoppingmall.me.presenter;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.me.view.inter.ILoginView;
import com.giveu.shoppingmall.utils.LogUtil;
import com.giveu.shoppingmall.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by 513419 on 2017/6/20.
 */
public class LoginPresenter extends BasePresenter<ILoginView> {
    public LoginPresenter(ILoginView view) {
        super(view);
    }

    public void WechatLogin() {
        if(isWeixinAvailable()) {
            thirdLogin(Wechat.NAME);
        }else {
            ToastUtils.showShortToast("请先安装微信客户端");
            getView().hideLoding();
        }
    }

    public void QQLogin() {
        thirdLogin(QQ.NAME);
    }

    public void SinaWeiboLogin() {
        thirdLogin(SinaWeibo.NAME);
    }

    private void thirdLogin(String platformStr) {
        Platform platform = ShareSDK.getPlatform(platformStr);
        authorize(platform);
    }

    //执行授权,获取用户信息
    private void authorize(final Platform plat) {
        if (plat == null) {
            ToastUtils.showShortToast("无效授权，请使用其他方式登录");
            return;
        }

        plat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int action, HashMap<String, Object> hashMap) {
                if (action == Platform.ACTION_USER_INFOR) {
                    ToastUtils.showShortToast("授权成功");
                    //获取用户信息
                    for (Map.Entry<String, Object> entry : hashMap.entrySet()) {
                        LogUtil.e("key = " + entry.getKey() + "   value = " + entry.getValue());
                    }
                    plat.removeAccount(true);
                    getView().afterThirdLogin();
                }
            }

            @Override
            public void onError(Platform platform, int action, Throwable throwable) {
                if (action == Platform.ACTION_USER_INFOR) {
                    //授权失败
                    ToastUtils.showShortToast("授权失败,请重试");
                    getView().afterThirdLogin();
                }
            }

            @Override
            public void onCancel(Platform platform, int action) {
                if (action == Platform.ACTION_USER_INFOR) {
                    //授权取消
                    ToastUtils.showShortToast("授权取消");
                    getView().afterThirdLogin();
                }

            }
        });
        //关闭SSO授权
        plat.SSOSetting(false);
        plat.showUser(null);
    }

    /**
     * 判断是否已安装微信客户端
     * @return
     */
    public boolean isWeixinAvailable() {
        final PackageManager packageManager = BaseApplication.getInstance().getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }
}
