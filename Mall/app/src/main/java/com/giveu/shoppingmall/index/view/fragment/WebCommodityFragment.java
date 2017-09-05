package com.giveu.shoppingmall.index.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.widget.CommodityWebView;


/**
 * Created by 513419 on 2017/8/30.
 * 商品介绍
 */

public class WebCommodityFragment extends Fragment {
    private View view;
    private CommodityWebView wvCommodity;
    private WebSettings webSettings;
    private String url = "http://m.okhqb.com/item/description/1000334264.html?fromApp=true";
    private boolean fromCommodityDetail;
    private ImageView fab_up_slide;
    private ScrollView mScrollView;
    private RelativeLayout mContainer;
    private ProgressBar pBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_web_commodity, null);
        initView();
        return view;
    }

    private void initView() {
        mContainer = (RelativeLayout) view.findViewById(R.id.mContainer);
        fab_up_slide = (ImageView) view.findViewById(R.id.fab_up_slide);
        mScrollView = (ScrollView) view.findViewById(R.id.mScrollView);
        pBar = (ProgressBar) view.findViewById(R.id.pb_web);
//        wvCommodity = (CommodityWebView) view.findViewById(R.id.wv_commodity);
        wvCommodity = new CommodityWebView(getContext());
        wvCommodity.clearCache(true);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        wvCommodity.setLayoutParams(layoutParams);
        if (fromCommodityDetail) {
            mScrollView.addView(wvCommodity);
        } else {
            mContainer.addView(wvCommodity);
            mScrollView.setVisibility(View.GONE);
            mContainer.removeView(pBar);
            mContainer.addView(pBar);
        }
        wvCommodity.fromCommodityDetail(fromCommodityDetail);
        wvCommodity.setFocusable(false);
        wvCommodity.loadUrl(url);
        webSettings = wvCommodity.getSettings();
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setBlockNetworkImage(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        wvCommodity.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webSettings.setBlockNetworkImage(false);
            }
        });
        if (fromCommodityDetail) {
            fab_up_slide.setVisibility(View.VISIBLE);
        } else {
            fab_up_slide.setVisibility(View.GONE);
        }

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
        fab_up_slide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScrollView.smoothScrollTo(0, 0);
            }
        });
    }

    public void setFromCommodityDetail(boolean fromCommodityDetail) {
        this.fromCommodityDetail = fromCommodityDetail;
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