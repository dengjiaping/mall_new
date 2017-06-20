package com.giveu.share.listener;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * Created by 513419 on 2017/4/14.
 */

public abstract class ShareListener implements PlatformActionListener {

//    onComplete()为分享完成回调
//    onCancel()为取消分享回调
    /**
     * 分享失败的回调，默认空实现
     * @param platform
     * @param i
     * @param throwable
     */
    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
    }
}

