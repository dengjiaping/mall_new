package com.giveu.shoppingmall.base.web;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.giveu.shoppingmall.utils.CommonUtils;
import com.lidroid.xutils.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhengyongle on 2016/7/1.
 */
public class NativeMethod {

    public static final String CAMERA = "camera";
    public static final String ALBUM = "album";
    private final static int MESSAGE_JS_CALLBACK = 1003;
    private String callback;
    BaseWebViewActivity mActivity;
    WebView mWebView;
    private boolean hasAccess;

    public NativeMethod(BaseWebViewActivity activity, WebView webView) {
        mActivity = activity;
        mWebView = webView;
    }


    @JavascriptInterface
    public void callNative(String jsonData) {
        LogUtils.e("callNative");
        if (jsonData == null) {
            return;
        }
        LogUtils.e("jsonData");
        try {
            String func = "";
            String data = "";
            JSONObject jsonObject = new JSONObject(jsonData);
            if (!jsonObject.isNull("func")) {
                func = jsonObject.getString("func");
            }
            if (!jsonObject.isNull("data")) {
                data = jsonObject.getString("data");
            }
            if (!jsonObject.isNull("callback")) {
                callback = jsonObject.getString("callback");
            }
            Class<?> cls = getClass();
            Method method = cls.getMethod(func, String.class, String.class);
            method.setAccessible(true);
            method.invoke(this, data, callback);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }





    public void gotoPage(String jsonData, String callback) {
        String url = null;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonData);
            if (!jsonObject.isNull("url")) {
                url = jsonObject.getString("url");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        mActivity.gotoPage(url);
    }


    protected Map<String, String> getWebViewHead() {
        Map<String, String> extraHeaders = new HashMap<String, String>();
        extraHeaders.put("DFDevice", "andriod_" + CommonUtils.getVersionName());
        return extraHeaders;
    }


    public void callJS(String callback, String data) {
        final String jsMethod = "javascript:" + callback + "('" + data + "')";
        if (mWebView != null) {
            mWebView.post(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl(jsMethod, getWebViewHead());
                }
            });
        }
    }

}
