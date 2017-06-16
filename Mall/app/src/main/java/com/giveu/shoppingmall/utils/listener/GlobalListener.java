 package com.giveu.shoppingmall.utils.listener;

public class GlobalListener {
    private static GlobalListener GlobalListener;

    private GlobalListener() {
        super();
    }

    public static GlobalListener getInstance() {
        if (GlobalListener == null) {
            createInstance();
        }
        return GlobalListener;
    }

    private static synchronized void createInstance() {
        if (GlobalListener == null) {
            GlobalListener = new GlobalListener();
        }
    }

    public static synchronized void destory() {
        GlobalListener = null;
    }

    private WeixinCodeListener weixinCodeListener;
    public interface WeixinCodeListener{
        void success(String code);
        void fail();
    }

    public void setWeixinCodeListener(WeixinCodeListener weixinCodeListener) {
        this.weixinCodeListener = weixinCodeListener;
    }

    public WeixinCodeListener getWeixinCodeListener() {
        return weixinCodeListener;
    }

    private RegistListener registListener;
    public interface RegistListener{
        void onSuccess(String userId);
    }

    public void setRegistListener(RegistListener registListener) {
        this.registListener = registListener;
    }

    public RegistListener getRegistListener() {
        return registListener;
    }






}
