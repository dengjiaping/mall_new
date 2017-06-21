package com.giveu.shoppingmall.view.emptyview;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.VolleyErrorHelper;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.utils.DensityUtils;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Description: loadingView
 */
public class CommonLoadingView extends RelativeLayout implements OnClickListener {

    private Activity mContext;

    public static int DEFAULT_ICON = R.drawable.base_empty_default_icon;// 默认显示图标

    @BindView(R.id.ll_loading)
    LinearLayout ll_loading;
    @BindView(R.id.rl_empty)
    RelativeLayout rl_empty;

    @BindView(R.id.iv_loading)
    ImageView iv_loading;
    @BindView(R.id.icon_state)
    ImageView icon_state;
    @BindView(R.id.txt_message)
    TextView txt_message;
    @BindView(R.id.txt_message_samll)
    TextView txt_message_samll;
    @BindView(R.id.tv_refresh)
    TextView tv_refesh;
    @BindView(R.id.tv_empty_next)
    TextView tv_empty_next;

    public boolean isLoading = false;
    private boolean isFirstRequest = true;
    /**
     * 异常码
     */
    private static Map<Integer, String> ExceptionMap = new HashMap<Integer, String>();// 异常code和异常消息对应的map
    /**
     * 错误码
     */
    public static Map<String, String> errorMap = new HashMap<String, String>();
    /**
     * contentView
     */
    private View contentView;

    static {
        ExceptionMap.put(VolleyErrorHelper.NetworkCode.NETWORK_NOLINK_CODE, "网络不给力!");
        ExceptionMap.put(VolleyErrorHelper.NetworkCode.NETWORK_TIMEOUT_CODE, "网络连接超时!");
        ExceptionMap.put(VolleyErrorHelper.NetworkCode.NETWORK_SERVERERROR_CODE, "服务器出错了!");

        errorMap.put(VolleyErrorHelper.NetworkCode.NETWORK_ERROR_CODE1, "客户端提交的参数有误");
        errorMap.put(VolleyErrorHelper.NetworkCode.NETWORK_ERROR_CODE2, "服务端异常");
        errorMap.put(VolleyErrorHelper.NetworkCode.NETWORK_ERROR_CODE3, "用户被禁用");
        errorMap.put(VolleyErrorHelper.NetworkCode.NETWORK_ERROR_CODE4, "调用已过期的接口");
        errorMap.put(VolleyErrorHelper.NetworkCode.NETWORK_ERROR_CODE5, "用户在其他设备登陆");

    }

    public CommonLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {//在ide的layout视图中不解析
            return;
        }
        mContext = (Activity) context;
        LayoutInflater mInflater = mContext.getLayoutInflater();
        contentView = mInflater.inflate(R.layout.view_common_loading, this, true);
        ButterKnife.bind(this);
        initView();

    }

    public void initView() {
        // 设置监听事件
        if (tv_refesh != null) {
            RelativeLayout.LayoutParams params = (LayoutParams) tv_refesh.getLayoutParams();
            params.width = (int) DensityUtils.getViewHeight(340);
            params.height = (int) DensityUtils.getViewHeight(120);
            tv_refesh.setLayoutParams(params);
            tv_refesh.setOnClickListener(this);
        }
        ll_loading.setOnClickListener(this);
        rl_empty.setOnClickListener(this);

        final AnimationDrawable mLoadingAinm = (AnimationDrawable) iv_loading.getBackground();
        iv_loading.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mLoadingAinm.start();
                return true;
            }
        });
    }

    /**
     * 显示加载框
     */
    public void showLoading() {
        if (isFirstRequest) {
            ll_loading.setVisibility(View.VISIBLE);
            rl_empty.setVisibility(View.GONE);
            isLoading = true;
        }
    }

    /**
     * 隐藏加载框，加载结束
     */
    public void disLoading() {
        ll_loading.setVisibility(View.GONE);
        rl_empty.setVisibility(View.GONE);
        isLoading = false;
        isFirstRequest = false;
    }


    /**
     * 数据加载为空，显示全屏
     *
     * @param iconResId    显示的图片资源
     * @param message      显示的信息
     * @param isError
     * @param layoutParams
     */
    public void showEmpty(int iconResId, String message, String meaageSamll, boolean isError, LayoutParams layoutParams) {
        if (layoutParams == null) {
            layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        }
        rl_empty.setLayoutParams(layoutParams);
        rl_empty.setBackgroundColor(mContext.getResources().getColor(R.color.white));

        setViewState(iconResId, message, meaageSamll, isError);
    }

    public void showEmpty(String message, String meaageSamll) {
        showEmpty(DEFAULT_ICON, message, meaageSamll, false, null);
    }

    public void showEmpty(EmptyType emptyType) {
        showEmpty(emptyType.icon, emptyType.message, emptyType.messageSub, false, null);
    }

    public void showEmpty() {
        showEmpty(EmptyType.OPT_DEFAULT);
    }


    /**
     * 控件显示状态
     *
     * @param iconResId
     * @param message
     * @param meaageSamll
     * @param isError
     */
    private void setViewState(int iconResId, String message, String meaageSamll, boolean isError) {
        isLoading = false;
        ll_loading.setVisibility(View.GONE);
        rl_empty.setVisibility(View.VISIBLE);

        tv_empty_next.setVisibility(View.GONE);
        tv_refesh.setVisibility(View.GONE);
        if (isError) {
            if (onClickReloadListener != null) {
                tv_refesh.setVisibility(View.VISIBLE);
            }
        } else {

        }

        if (iconResId == 0) {
            icon_state.setImageResource(DEFAULT_ICON);
        } else {
            icon_state.setImageResource(iconResId);
        }
        if (StringUtils.isNull(message)) {
            txt_message.setText(mContext.getString(R.string.tip_nodata));
        } else {
            txt_message.setText(message);
        }
        if (!StringUtils.isNull(meaageSamll)) {
            txt_message_samll.setText(meaageSamll);
        }
    }


    /**
     * 当返回信息错误时的处理,弹出错误页面或者toast
     */
    public void showError(BaseBean errorBean) {
        if (errorBean == null) {
            return;
        }
        int resultCode = BaseBean.getResultCode(errorBean.result);
        if (isLoading) {
            try {
                if (ExceptionMap.containsKey(resultCode)) {
                    if (resultCode == VolleyErrorHelper.NetworkCode.NETWORK_NOLINK_CODE || resultCode == VolleyErrorHelper.NetworkCode.NETWORK_TIMEOUT_CODE) {// 没有网络
                        showEmpty(R.drawable.icon_network_error, ExceptionMap.get(resultCode), null, false, null);
                    } else if (resultCode == VolleyErrorHelper.NetworkCode.NETWORK_SERVERERROR_CODE) {// 服务器错误
                        showEmpty(DEFAULT_ICON, ExceptionMap.get(resultCode), null, false, null);
                    }
                } else {
                    showEmpty(DEFAULT_ICON, errorBean.message, null, false, null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                disLoading();
            }
        } else {
            showErrorToast(errorBean);
        }
    }

    /**
     * 弹出错误toast
     *
     * @param error
     */
    public static void showErrorToast(BaseBean error) {
        try {
            if (error == null) {
                return;
            }
            int resultCode = BaseBean.getResultCode(error.result);
            String message = "";
            if (ExceptionMap.containsKey(resultCode)) {
                message = ExceptionMap.get(resultCode);
            } else {
                message = error.message;
            }
            ToastUtils.showShortToast(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        switch (vId) {
            case R.id.tv_refresh:// 点击刷新
                if (onClickReloadListener != null) {
                    onClickReloadListener.onClickReload();
                }
                break;
            case R.id.tv_empty_next:// 下一步

                break;
        }
    }

    // 点击刷新按钮的回调
    private OnClickReloadListener onClickReloadListener;

    public interface OnClickReloadListener {
        public void onClickReload();
    }

    public void setOnClickReloadListener(OnClickReloadListener onClickReloadListener) {
        this.onClickReloadListener = onClickReloadListener;
    }


    //暂时没用
    private OnDealEmptyContentListener onDealEmptyContentListener;

    public void setOnDealEmptyContentListener(OnDealEmptyContentListener onDealEmptyContentListener) {
        this.onDealEmptyContentListener = onDealEmptyContentListener;
    }

    /**
     * 自己处理空白内容
     */
    public interface OnDealEmptyContentListener {
        public void onDealEmptyContent(View contentView);
    }

    public void showEmptyCustom() {
        if (onDealEmptyContentListener != null && contentView != null) {
            onDealEmptyContentListener.onDealEmptyContent(contentView);
        }
    }


}
