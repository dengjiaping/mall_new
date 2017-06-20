package com.giveu.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;
import android.view.View;

import com.giveu.share.listener.ShareListener;

import java.util.ArrayList;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by 513419 on 2017/4/14.
 */
public class SocialShareUtil {
    private static SocialShareUtil instance = new SocialShareUtil();
    protected OnekeyShare oks;
    private ArrayList<Integer> drawables;
    private ArrayList<String> labels;
    private ArrayList<View.OnClickListener> listeners;

    private SocialShareUtil() {
        init();
    }

    private void init() {
        oks = new OnekeyShare();
        drawables = new ArrayList<>();
        labels = new ArrayList<>();
        listeners = new ArrayList<>();
    }

    public static SocialShareUtil getInstance() {
        return instance;
    }

    /**
     * 增加自定义平台
     *
     * @param context
     * @param drawable 图片资源id
     * @param label    平台名称
     * @param listener 事件监听
     */
    public void addCustomPlatform(Context context, @DrawableRes int drawable, String label, View.OnClickListener listener) {
        if (!labels.contains(label)) {
            drawables.add(drawable);
            labels.add(label);
            listeners.add(listener);
        }
        Bitmap enableLogo = BitmapFactory.decodeResource(context.getResources(), drawable);
        oks.setCustomerLogo(enableLogo, label, listener);
    }

    /**
     * 移除自定义的特定平台,删除后之后的分享都不会出现该平台
     */
    public void deleteCustomerLogo(String label) {
        oks.deleteCustomerLogo(label);
        for (int i = 0; i < labels.size(); i++) {
            if (labels.get(i).equals(label)) {
                drawables.remove(i);
                labels.remove(i);
                listeners.remove(i);
            }
        }
    }

    /**
     * 移除所有的自定义平台
     */
    public void deleteAllCustomerPlatform() {
        //仅仅是删掉显示的平台，自定义平台对应的list不作删除以便在需使用自定义平台时进行恢复
        oks.deleteAllCustomerLogo();
    }

    public void share(Context context, String title, String content, String url) {
        share(context, false, title, content, url, null);
    }

    public void share(Context context, String title, String content, String url, ShareListener listener) {
        share(context, false, title, content, url, listener);
    }

    public void shareWithCustomerPlatform(Context context, String title, String content, String url) {
        share(context, true, title, content, url, null);
    }

    public void shareWithCustomerPlatform(Context context, String title, String content, String url, ShareListener listener) {
        share(context, true, title, content, url, listener);
    }

    /**
     * 分享到支持的平台
     *
     * @param context
     * @param title    标题
     * @param content  内容
     * @param imageUrl 图片url
     * @param listener 分享操作的回调，成功或失败
     */
    public void share(Context context, boolean withCustomerPlatform, String title, String content, String imageUrl, ShareListener listener) {
        if (withCustomerPlatform) {
            //先移除自定义平台再添加
            deleteAllCustomerPlatform();
            for (int i = 0; i < labels.size(); i++) {
                addCustomPlatform(context, drawables.get(i), labels.get(i), listeners.get(i));
            }
        } else {
            //删除所有自定义平台
            deleteAllCustomerPlatform();
        }
        oks.setTitle(title);
        // 在自动授权时可以禁用SSO方式
        oks.disableSSOWhenAuthorize();
        oks.setText(content);
        oks.setImageUrl(imageUrl);
        //隐藏编辑页面
        oks.setSilent(true);
        if (listener != null) {
            oks.setCallback(listener);
        }
        oks.show(context);
    }

    /**
     * 单一分享平台
     *
     * @param platformName 平台对应的名称 如:WeChat.NAME
     * @param title
     * @param content
     * @param imageUrl
     * @param listener
     */
    public void shareSinglePlatform(String platformName, String title, String content, String imageUrl, ShareListener listener) {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle(title);
        sp.setText(content);
        sp.setImageUrl(imageUrl);
        Platform platform = ShareSDK.getPlatform(platformName);
        if (listener != null) {
            // 设置分享事件回调
            platform.setPlatformActionListener(listener);
        }
        platform.share(sp);
    }
}
