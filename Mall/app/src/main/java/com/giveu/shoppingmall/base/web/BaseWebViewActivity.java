package com.giveu.shoppingmall.base.web;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.utils.Const;
import com.giveu.shoppingmall.utils.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 用的是系统webview
 */
public abstract class BaseWebViewActivity extends BaseActivity {

    //平台可以识别的 外部url 的正则
    public static java.lang.String urlRegularExpression = "(((http|ftp|https)://)|www)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?";
    protected WebView webview;
    protected TextView tv_title;
    protected LinearLayout ll_bottom;
    protected boolean isWebPageFinished = false;
    ProgressBar pBar;
    protected NativeMethod nativeMethod;


    @Override
    public void initView(Bundle savedInstanceState) {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        setContentView(R.layout.base_webview);
        initLayout();
        afterInitView();
    }

    @Override
    public void setListener() {}

    @Override
    public void setData() {}

    public String getActivityTitle() {
        return "";
    }

    public abstract void afterInitView();

    public abstract String getWebviewUrl();

    private String getUrlNotNull(){
        return getUrlAddHttp(getWebviewUrl());
    }

    /**
     * @return 兼容www.baidu.com->http://www.baidu.com
     */
    private String getUrlAddHttp(String rawUrl){
        String url = StringUtils.isNull(rawUrl) ? "" : rawUrl;
        if (url.indexOf("www.") == 0 || url.indexOf("wap.") == 0){//兼容www.baidu.com
            url = "http://" + url;
        }
        return url;
    }



    private void initLayout() {

        pBar = (ProgressBar) findViewById(R.id.pb_web);
        tv_title = baseLayout.top_tab_center_title;
        tv_title.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isWebPageFinished && webview != null){
                    webview.loadUrl("javascript:scrollTo(0,0);");
                }
            }
        });


        this.ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        this.webview = (WebView) findViewById(R.id.webview);
        initWebview(webview);

        if ( !fillWebviewWithHtmlData(webview) ) {
            Intent intent = getIntentFromUrl(mBaseContext, getUrlNotNull());
            if (intent != null){
                startActivity(intent);
                finish();
            }else {
                this.webview.loadUrl(getUrlNotNull());
            }
        }
    }

    private void initWebview(final WebView webview) {
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setDisplayZoomControls(false);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setDefaultTextEncodingName("UTF-8");
        String userAgentString = webview.getSettings().getUserAgentString() + Const.myWebAgentPlus;
        webview.getSettings().setUserAgentString(userAgentString);
        webview.getSettings().setDomStorageEnabled(true);

        webview.getSettings().setAppCacheEnabled(false);
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setPluginState(WebSettings.PluginState.ON);
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        nativeMethod = new NativeMethod(this, webview);
        webview.addJavascriptInterface(nativeMethod, "df");

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (TextUtils.isEmpty(getActivityTitle())) {
                    tv_title.setText(title);
                } else {
                    tv_title.setText(getActivityTitle());
                }
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    pBar.setVisibility(View.GONE);
                } else {
                    if (pBar.getVisibility() == View.GONE)
                        pBar.setVisibility(View.VISIBLE);
                    pBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

        });
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Intent intent = getIntentFromUrl(mBaseContext, url);

                if (intent != null){
                    startActivity(intent);
                    return true;
                }

                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                onPageStartedInBase(webview, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                isWebPageFinished = true;

//                view.loadUrl("javascript:window.pengsiid=" + 111);
//				view.loadUrl("javascript:window.demo.clickDemo1(" + "document.getElementsByTagName('title')[0].innerText" + ");");
//				view.loadUrl("javascript:window.demo.clickDemo2(" + "document.getElementsByName('description')[0].attributes['content'].value" + ");");
//				view.loadUrl("javascript:window.demo.clickDemo3(" + "document.getElementsByTagName('img')[0].attributes['src'].value" + ");");
//				view.loadUrl("javascript:window.demo.clickDemo4(" + "title" + ");");
//				view.loadUrl("javascript:window.demo.clickDemo5(" + "description" + ");");
//				view.loadUrl("javascript:window.demo.clickDemo6(" + "imgPath" + ");");
//				view.loadUrl("javascript:window.demo.clickDemo7(" + "isHideLink" + ");");
//				view.loadUrl("javascript:window.demo.clickDemo8(" + "pengsi_url" + ");");

                onPageFinishedInBase(view, url);
            }
        });

    }

    private Intent getIntentFromUrl(Context mBaseContext, String url) {
        return null;
    }


    /**
     * 上一个activity是谁
     * @return
     */
    public static Activity getLastActivity(){
        int size = BaseApplication.getInstance().undestroyActivities.size();
        if (size > 1){
            return BaseApplication.getInstance().undestroyActivities.get(size - 1);
        }

        return null;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        if (v == baseLayout.ll_tab_left_image){
            finish();
        }

//        if (isWebPageFinished) {
//            share();
//        } else {
//            ToastUtils.showShortToast("请稍等，网页还未加载完成！");
//        }
    }

    private static boolean isCustomUrl(String str){
        Pattern urlPattern = Pattern.compile("^(((http|ftp|https)://)|www)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?", Pattern.CASE_INSENSITIVE);

        Matcher urlMatcher = urlPattern.matcher(str);
        while (urlMatcher.find()) {
            return true;
        }

        return false;
    }

    @Override
    public void onResume() {
        super.onResume();

        webview.onResume();
    }

    @Override
    public void onPause() {
        webview.onPause();

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (webview!=null && webview.getParent()!=null){
            ((ViewGroup)webview.getParent()).removeView(webview);
            webview.destroy();
            webview=null;
        }

        super.onDestroy();
    }

    public void onPageStartedInBase(WebView view, String url, Bitmap favicon) {

    }

    public void onPageFinishedInBase(WebView view, String url) {

    }


    @Override
    public void onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack();
        } else {
            finish();
        }
    }

    public boolean fillWebviewWithHtmlData(WebView webview) {
        return false;
    }


}
