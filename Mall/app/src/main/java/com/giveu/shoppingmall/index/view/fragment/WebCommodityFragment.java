package com.giveu.shoppingmall.index.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseFragment;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.widget.CommodityWebView;


/**
 * Created by 513419 on 2017/8/30.
 * 商品介绍
 */

public class WebCommodityFragment extends BaseFragment {
    private View view;
    private CommodityWebView wvCommodity;
    private WebSettings webSettings;
    private boolean fromCommodityDetail;
    private ImageView fab_up_slide;
    private LinearLayout mScrollView;
    private RelativeLayout mContainer;
    private ProgressBar pBar;
    private String htmlUrl;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_web_commodity, null);
        baseLayout.setTitleBarAndStatusBar(false, false);
        initView();
        if (StringUtils.isNotNull(htmlUrl)) {
            wvCommodity.loadUrl(htmlUrl);
        }
        return view;
    }

    @Override
    protected void setListener() {

    }

/*    @Override
    protected void onVisible() {
        super.onVisible();
        if (wvCommodity != null) {
            wvCommodity.onResume();
        }
    }

    @Override
    protected void onInvisible() {
        super.onInvisible();
        if (wvCommodity != null) {
            wvCommodity.onPause();
        }
    }*/

    @Override
    public void initDataDelay() {

    }

    private void initView() {
        mContainer = (RelativeLayout) view.findViewById(R.id.mContainer);
        fab_up_slide = (ImageView) view.findViewById(R.id.fab_up_slide);
        mScrollView = (LinearLayout) view.findViewById(R.id.mScrollView);
        pBar = (ProgressBar) view.findViewById(R.id.pb_web);
//        wvCommodity = (CommodityWebView) view.findViewById(R.id.wv_commodity);
        initWebView();
        if (fromCommodityDetail) {
            fab_up_slide.setVisibility(View.VISIBLE);
        } else {
            fab_up_slide.setVisibility(View.GONE);
        }


        fab_up_slide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mScrollView.smoothScrollTo(0, 0);
            }
        });
    }

    private void initWebView() {
        wvCommodity = new CommodityWebView(getContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        wvCommodity.setLayoutParams(layoutParams);
        mScrollView.addView(wvCommodity);
        wvCommodity.fromCommodityDetail(fromCommodityDetail);
        wvCommodity.setFocusable(false);
        webSettings = wvCommodity.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        wvCommodity.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        wvCommodity.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    pBar.setVisibility(View.GONE);
                } else {
                    if (pBar.getVisibility() == View.GONE) {
                        pBar.setVisibility(View.VISIBLE);
                    }
                    pBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

        });
    }


    public void setFromCommodityDetail(boolean fromCommodityDetail) {
        this.fromCommodityDetail = fromCommodityDetail;
    }

    /**
     * 刷新商品详情
     * @param url
     */
    public void refreshCommodityDetail(String url) {
        htmlUrl = url;
        if (wvCommodity != null) {
            wvCommodity.loadUrl(htmlUrl);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (wvCommodity != null) {
            wvCommodity.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (wvCommodity != null) {
            wvCommodity.onResume();
        }
    }

    @Override
    public void onDestroy() {
        if (wvCommodity != null) {
            wvCommodity.removeAllViews();
            wvCommodity.destroy();
            if (wvCommodity.getParent() != null) {
                ((ViewGroup) wvCommodity.getParent()).removeView(wvCommodity);
            }
        }
        wvCommodity = null;
        super.onDestroy();
    }
}
