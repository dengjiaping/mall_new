package com.giveu.shoppingmall.base;

import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.mynet.BaseBean;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.utils.DensityUtils;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;
import com.giveu.shoppingmall.widget.emptyview.EmptyType;

/**
 * Created by 508632 on 2016/12/8.
 */

/**
 * 默认显示top bar的页面布局框架
 */
public class BaseLayout extends LinearLayout {

    public BaseLayout(Context context) {
        super(context);
        init(context);
    }

    public BaseLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BaseLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    Context mContext;
    /**
     * 包括titlebar和statusbar
     */
    public LinearLayout baselayout_topbar;
    /**
     * title bar 的返回图标LinearLayout
     */
    public LinearLayout ll_tab_left_image;
    public ImageView top_left_image;
    /**
     * baselayout中间包裹view的容器
     */
    public LinearLayout ll_baselayout_content;
    /**
     * title bar 中间的ImageView
     */
    public ImageView top_tab_center_image;
    /**
     * title bar 中间的TextView
     */
    public TextView top_tab_center_title;
    public CommonLoadingView clv;
    private ImageView top_tab_right_image, iv_statusbar;
    public TextView top_tab_right_text;
    public TextView tv_left;
    public RelativeLayout rl_click_right;
    public ImageView iv_close;
    /**
     * title bar
     */
    private RelativeLayout titlebar;


    private void init(Context context) {
        this.mContext = context;

        View base_layout = View.inflate(context, R.layout.base_layout, null);
        baselayout_topbar = (LinearLayout) base_layout.findViewById(R.id.baselayout_topbar);
        tv_left = (TextView) base_layout.findViewById(R.id.tv_left);
        ll_tab_left_image = (LinearLayout) base_layout.findViewById(R.id.ll_tab_left_image);
        iv_close = (ImageView) base_layout.findViewById(R.id.iv_close);
        top_left_image = (ImageView) base_layout.findViewById(R.id.top_left_image);
        rl_click_right = (RelativeLayout) base_layout.findViewById(R.id.rl_click_right);
        top_tab_center_image = (ImageView) base_layout.findViewById(R.id.top_tab_center_image);
        top_tab_center_title = (TextView) base_layout.findViewById(R.id.top_tab_center_title);
        top_tab_right_image = (ImageView) base_layout.findViewById(R.id.top_tab_right_image);
        iv_statusbar = (ImageView) base_layout.findViewById(R.id.iv_statusbar);
        top_tab_right_text = (TextView) base_layout.findViewById(R.id.top_tab_right_text);
        titlebar = (RelativeLayout) base_layout.findViewById(R.id.titlebar);

        ll_baselayout_content = (LinearLayout) base_layout.findViewById(R.id.ll_baselayout_content);
        clv = (CommonLoadingView) base_layout.findViewById(R.id.clv);
        super.addView(base_layout);
        //默认为白色背景
        setWhiteBlackStyle();
    }

    /**
     * 默认titlebar是显示的statusbar是隐藏的
     */
    public void setTitleBarAndStatusBar(boolean showTitleBar, boolean showStatusBar) {
        setStatusBarVisiable(showStatusBar);
        if (showTitleBar) {
            titlebar.setVisibility(VISIBLE);
        } else {
            titlebar.setVisibility(GONE);
        }
    }

    public void setLeftImageListener(OnClickListener listener) {
        iv_close.setVisibility(VISIBLE);
        if (listener != null) {
            iv_close.setOnClickListener(listener);
        }
    }

    /**
     * 隐藏返回按钮
     */
    public void hideBack() {
        ll_tab_left_image.setVisibility(View.GONE);
    }

    public void addContentView(View child) {
        if (ll_baselayout_content.getChildCount() > 0)
            ll_baselayout_content.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        ll_baselayout_content.addView(child, params);
    }

    /**
     * 修改top bar 返回键头的点击事件
     *
     * @param onClickListener
     */
    public void setBackClickListener(OnClickListener onClickListener) {
        if (onClickListener != null) {
            ll_tab_left_image.setOnClickListener(onClickListener);
        }
    }

    /**
     * 修改top bar 右边图片的点击事件
     *
     * @param onClickListener
     */
    public void setRightClickListener(OnClickListener onClickListener) {
        if (onClickListener != null) {
            rl_click_right.setOnClickListener(onClickListener);
        }
    }

    /**
     * 设置页面的title
     *
     * @param pageTitle
     */
    public void setTitle(CharSequence pageTitle) {
        top_tab_center_title.setText(pageTitle);
    }


    /**
     * 设置状态栏背景颜色
     */
    public void setTopBarBackgroundColor(int colorId) {
        baselayout_topbar.setBackgroundColor(mContext.getResources().getColor(colorId));
    }

    /**
     * 设置返回键图片
     */
    public void setBackImage(int imgId) {
        top_left_image.setImageResource(imgId);
    }

    /**
     * 设置title背景图片
     */
    public void setTopBarBgDrawble(int imgId) {
        baselayout_topbar.setBackgroundResource(imgId);
    }

    /**
     * 设置页面的右边的图片
     *
     * @param resid
     */
    public void setRightImage(int resid) {
        top_tab_right_image.setBackgroundResource(resid);
    }

    /**
     * 隐藏页面的右边的图片
     */
    public void goneRightImage() {
        rl_click_right.setVisibility(GONE);
    }

    /**
     * 显示页面的右边的图片
     */
    public void showRightImage() {
        rl_click_right.setVisibility(VISIBLE);
    }

    /**
     * 设置页面的右边的文字
     *
     * @param rightText
     */
    public void setRightText(CharSequence rightText) {
        top_tab_right_text.setText(rightText);
    }

    public void setRightTextAndListener(CharSequence rightText, View.OnClickListener onClickListener) {
        top_tab_right_text.setText(rightText);
        top_tab_right_text.setOnClickListener(onClickListener);
    }

    public void setRightTextColor(@ColorRes int color) {
        top_tab_right_text.setTextColor(ContextCompat.getColor(getContext(), color));
    }

    /**
     * 右边文字点击事件
     *
     * @param onClickListener
     */
    public void setRightTextListener(View.OnClickListener onClickListener) {
        top_tab_right_text.setOnClickListener(onClickListener);
    }

    /**
     * 设置标题字体颜色
     *
     * @param color
     */
    public void setTitleTextColor(int color) {
        top_tab_center_title.setTextColor(ContextCompat.getColor(mContext, color));
    }

    public void setRightImageAndListener(int resid, View.OnClickListener onClickListener) {
        setRightImage(resid);
        setRightClickListener(onClickListener);
    }

    public void setLeftTextAndListener(CharSequence rightText, View.OnClickListener onClickListener) {
        tv_left.setVisibility(View.VISIBLE);
        tv_left.setText(rightText);
        tv_left.setOnClickListener(onClickListener);
    }

    /**
     * 显示loading页面
     */
    public void showLoading() {
        clv.showLoading();
    }

    /**
     * 隐藏loading页面
     */
    public void disLoading() {
        clv.disLoading();
    }

    public void showEmpty() {
        clv.showEmpty();
    }

    public void hideEmpty() {
        clv.hideEmpty();
    }

    /**
     * @param marginTop    空布局需要距离顶部高度
     * @param marginBottom 空布局需要距离底部高度
     * @param icon         图标
     * @param message      提示信息
     */
    public void showEmpty(int marginTop, int marginBottom, @DrawableRes int icon, String message) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) clv.getLayoutParams();
        params.topMargin = DensityUtils.dip2px(marginTop);
        params.bottomMargin = DensityUtils.dip2px(marginBottom);
        clv.showEmpty(icon, message, "", false, null);
    }

    public void showEmpty(int marginTop, int marginBottom, String message) {
        showEmpty(marginTop, marginBottom, R.drawable.ic_defalut_empty, message);
    }

    public void showEmpty(@DrawableRes int icon, String message) {
        showEmpty(0, 0, icon, message);
    }

    public void showEmpty(String message) {
        showEmpty(0, 0, R.drawable.ic_defalut_empty, message);
    }

    public void showEmpty(EmptyType emptyType) {
        clv.showEmpty(emptyType);
    }

    public void showError(BaseBean errorBean) {
        clv.showError(errorBean);
    }

    public void setOnClickReloadListener(CommonLoadingView.OnClickReloadListener reloadListener) {
        clv.setOnClickReloadListener(reloadListener);
    }

    /**
     * 设置是否显示StatusBar高度，默认不显示
     *
     * @param visiable
     */
    public void setStatusBarVisiable(boolean visiable) {
        if (iv_statusbar != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (visiable) {
                    iv_statusbar.setVisibility(View.VISIBLE);
                    ViewGroup.LayoutParams layoutParams = iv_statusbar.getLayoutParams();
                    layoutParams.height = DensityUtils.getStatusBarHeight();
                    iv_statusbar.setLayoutParams(layoutParams);
                } else {
                    iv_statusbar.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * 设置背景白色，文字黑色，返回图标黑色
     */
    public void setWhiteBlackStyle() {
        int color = getResources().getColor(R.color.color_4a4a4a);
        top_tab_center_title.setTextColor(color);
        top_tab_right_text.setTextColor(color);
        setBackImage(R.drawable.back_press);
        setTopBarBgDrawble(R.drawable.shape_topbar_bg_bottom_line);
    }

    /**
     * 设置背景白色，文字黑色，返回图标浅蓝色
     */
    public void setWhiteBlueStyle() {
        int color = getResources().getColor(R.color.color_4a4a4a);
        top_tab_center_title.setTextColor(color);
        top_tab_right_text.setTextColor(color);
        setBackImage(R.drawable.back_blue);
        setTopBarBgDrawble(R.drawable.shape_topbar_bg_bottom_line);
    }

    /**
     * 设置背景蓝色，文字白色，返回图标浅蓝色
     */
    public void setBlueWhiteStyle() {
        int color = getResources().getColor(R.color.white);
        top_tab_center_title.setTextColor(color);
        top_tab_right_text.setTextColor(color);
        setBackImage(R.drawable.selector_back);
        setTopBarBgDrawble(R.color.title_color);
    }


}
