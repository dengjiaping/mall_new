package com.giveu.shoppingmall.widget.pulltorefresh;


import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.utils.DensityUtils;
import com.giveu.shoppingmall.utils.StringUtils;

/**
 * PullToRefresh footer
 */
public class PullFooter extends LinearLayout {
    private Context mContext;

    private LinearLayout ll_loading;
    private ImageView loading;
    private TextView txt_end;
    public RelativeLayout con_rl;

    public PullFooter(Context context) {
        super(context);
        initView(context);
    }

    public PullFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        View.inflate(mContext, R.layout.pull_footer, this);
        ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
        loading = (ImageView) findViewById(R.id.loading);
        txt_end = (TextView) findViewById(R.id.txt_end);
        con_rl = (RelativeLayout) findViewById(R.id.con_rl);

        final AnimationDrawable mLoadingAinm = (AnimationDrawable) loading.getBackground();
        loading.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mLoadingAinm.start();
                return true;
            }
        });
    }

    public void setConHeight(int hieght) {
        LinearLayout.LayoutParams params = (LayoutParams) con_rl.getLayoutParams();
        params.height = DensityUtils.dip2px(hieght);
        con_rl.setLayoutParams(params);
    }


    /**
     * 显示加载
     */
    public void showLoading() {
        ll_loading.setVisibility(View.VISIBLE);
        txt_end.setVisibility(View.GONE);
    }

    /**
     * 数据加载完毕，显示结果
     */
    public void showEnd(String endMsg) {
        ll_loading.setVisibility(View.GONE);
        txt_end.setVisibility(View.VISIBLE);
        if (StringUtils.isNotNull(endMsg)) {
            txt_end.setText(endMsg);
        }
    }

    public void setEndmessage(String endMsg) {
        if (StringUtils.isNotNull(endMsg)) {
            txt_end.setText(endMsg);
        }
    }

    /**
     * 隐藏loading
     */
    public void hide() {
        ll_loading.setVisibility(View.GONE);
        txt_end.setVisibility(View.GONE);
    }

    /**
     * 动态设置loading的位置，目前用于我的团队界面左右滑动的listview中
     *
     * @param x
     * @param y
     */
    public void setDynamic(int x, int y) {
        ll_loading.getLayoutParams().width = DensityUtils.getWidth();
        ((View) ll_loading.getParent()).scrollTo(-x, y);
    }

    /**
     * 设置loading在父布局的gravity
     *
     * @param gravity
     */
    public void setLoadingGravity(int gravity) {
        ll_loading.getLayoutParams().width = DensityUtils.getWidth();
        ((RelativeLayout) ll_loading.getParent()).setGravity(gravity);
    }

    /**
     * 设置在父布局的gravity
     *
     * @param gravity
     */
    public void showEndGravity(int gravity) {
        txt_end.getLayoutParams().width = DensityUtils.getWidth();
        ((RelativeLayout) txt_end.getParent()).setGravity(gravity);
    }
}
