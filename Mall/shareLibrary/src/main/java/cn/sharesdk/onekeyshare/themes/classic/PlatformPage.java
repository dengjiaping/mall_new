/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2013年 mob.com. All rights reserved.
 */

package cn.sharesdk.onekeyshare.themes.classic;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.giveu.share.R;
import com.mob.tools.gui.MobViewPager;
import com.mob.tools.utils.ResHelper;

import java.util.ArrayList;
import java.util.HashMap;

import cn.sharesdk.framework.CustomPlatform;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.CustomerLogo;
import cn.sharesdk.onekeyshare.OnekeySharePage;
import cn.sharesdk.onekeyshare.OnekeyShareThemeImpl;

/**
 * 九宫格的抽象类
 */
public abstract class PlatformPage extends OnekeySharePage {
    private ClassicTheme impl;
    /**
     * 点击九格宫，展示编辑界面，要执行的子线程
     */
    private Runnable beforeFinish;
    /**
     * 九宫格显示时的动画
     */
    private Animation animShow;
    /**
     * 九宫格隐藏时的动画
     */
    private Animation animHide;
    private LinearLayout llPanel;
    private boolean finished;

    public PlatformPage(OnekeyShareThemeImpl impl) {
        super(impl);
        this.impl = ResHelper.forceCast(impl);
    }

    public void onCreate() {
        activity.getWindow().setBackgroundDrawable(new ColorDrawable(0x4c000000));
        initAnims();

        LinearLayout llPage = new LinearLayout(activity);
        llPage.setOrientation(LinearLayout.VERTICAL);
        activity.setContentView(llPage);

        TextView vTop = new TextView(activity);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.weight = 1;
        vTop.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        llPage.addView(vTop, lp);

        llPanel = new LinearLayout(activity);
        llPanel.setOrientation(LinearLayout.VERTICAL);
        lp = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        llPanel.setAnimation(animShow);
        llPage.addView(llPanel, lp);

        LinearLayout llContainer = new LinearLayout(activity);
        lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        lp.leftMargin = dp2px(20);
        lp.rightMargin = dp2px(20);
        llContainer.setBackgroundResource(R.drawable.bg_platform);
        MobViewPager mvp = new MobViewPager(activity);
        ArrayList<Object> cells = collectCells();
        PlatformPageAdapter adapter = newAdapter(cells);
        LinearLayout.LayoutParams mvpLp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, adapter.getPanelHeight());
        mvpLp.leftMargin = dp2px(5);
        mvpLp.rightMargin = dp2px(5);
        llContainer.addView(mvp ,mvpLp);
        llPanel.addView(llContainer, lp);

        IndicatorView vInd = new IndicatorView(activity);
        lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, adapter.getBottomHeight());
        llPanel.addView(vInd, lp);
        View translunctView = new View(activity);
        translunctView.setBackgroundColor(Color.TRANSPARENT);
        lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, dp2px(20));
        translunctView.setLayoutParams(lp);
        llPanel.addView(translunctView, lp);
        TextView tvCancle = new TextView(activity);
        tvCancle.setText("取消");
        tvCancle.setBackgroundResource(R.drawable.bg_platform);
        tvCancle.setGravity(Gravity.CENTER);
        tvCancle.setTextColor(Color.BLACK);
        lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, dp2px(50));
        lp.leftMargin = dp2px(20);
        lp.rightMargin = dp2px(20);
        lp.bottomMargin = dp2px(20);
        tvCancle.setLayoutParams(lp);
        tvCancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        llPanel.addView(tvCancle);

        vInd.setScreenCount(adapter.getCount());
        vInd.onScreenChange(0, 0);
        adapter.setIndicator(vInd);
        mvp.setAdapter(adapter);
    }

    protected abstract PlatformPageAdapter newAdapter(ArrayList<Object> cells);

    protected ArrayList<Object> collectCells() {
        ArrayList<Object> cells = new ArrayList<Object>();

        Platform[] platforms = ShareSDK.getPlatformList();
        if (platforms == null) {
            platforms = new Platform[0];
        }
        HashMap<String, String> hides = getHiddenPlatforms();
        if (hides == null) {
            hides = new HashMap<String, String>();
        }
        for (Platform p : platforms) {
            if (!hides.containsKey(p.getName())) {
                cells.add(p);
            }
        }

        ArrayList<CustomerLogo> customers = getCustomerLogos();
        if (customers != null && customers.size() > 0) {
            cells.addAll(customers);
        }

        return cells;
    }

    public final void showEditPage(final Platform platform) {
        beforeFinish = new Runnable() {
            public void run() {
                boolean isSilent = isSilent();
                boolean isCustomPlatform = platform instanceof CustomPlatform;
                boolean isUseClientToShare = isUseClientToShare(platform);
                if (isSilent || isCustomPlatform || isUseClientToShare) {
                    shareSilently(platform);
                } else {
                    ShareParams sp = formateShareData(platform);
                    if (sp != null) {
                        // 编辑分享内容的统计
                        ShareSDK.logDemoEvent(3, null);
                        if (getCustomizeCallback() != null) {
                            getCustomizeCallback().onShare(platform, sp);
                        }
                        impl.showEditPage(activity, platform, sp);
                    }
                }
            }
        };
        finish();
    }

    public final void performCustomLogoClick(final View v, final CustomerLogo logo) {
        beforeFinish = new Runnable() {
            public void run() {
                logo.listener.onClick(v);
            }
        };
        finish();
    }

    private void initAnims() {
        animShow = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1,
                Animation.RELATIVE_TO_SELF, 0);
        animShow.setDuration(300);

        animHide = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1);
        animHide.setDuration(300);
    }

    public boolean onFinish() {
        if (finished) {
            finished = false;
            return false;
        }

        animHide.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {

            }

            public void onAnimationRepeat(Animation animation) {

            }

            public void onAnimationEnd(Animation animation) {
                if (beforeFinish == null) {
                    // 取消分享菜单的统计
                    ShareSDK.logDemoEvent(2, null);
                } else {
                    beforeFinish.run();
                    beforeFinish = null;
                }

                finished = true;
                finish();
            }
        });
        llPanel.clearAnimation();
        llPanel.setAnimation(animHide);
        llPanel.setVisibility(View.GONE);
        return true;
    }

    public int dp2px(float dpValue) {
        final float scale = activity.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
