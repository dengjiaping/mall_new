package com.giveu.shoppingmall.base;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.giveu.shoppingmall.widget.dialog.LoadingDialog;

/**
 * Created by 508632 on 2016/12/12.
 * fragment初始化的时候不在自动调用{@link BaseFragment#initWithDataDelay()},需要自己手动调用
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener, IView {
    public Activity mBaseContext;
    private View myXmlView;
    public BaseLayout baseLayout;
    // 是否延迟加载
    protected boolean isLazyLoad = true;
    protected boolean isViewInitiated;
    private boolean isDataInitiated;//是否填充了数据
    private BasePresenter[] mAllPresenters = new BasePresenter[]{};


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBaseContext = this.getActivity();
        baseLayout = new BaseLayout(mBaseContext);
        myXmlView = initView(inflater, container, savedInstanceState);
        baseLayout.addContentView(myXmlView);
        baseLayout.setStatusBarVisiable(isTranslateStatusBar());

        addPresenters();
        setListener();
        return baseLayout;
    }

    /**
     * 当使用mvp模式时实现这个方法
     */
    protected BasePresenter[] initPresenters() {
        return null;
    }

    private void addPresenters() {
        BasePresenter[] presenters = initPresenters();
        if (presenters != null) {
            mAllPresenters = presenters;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        prepareFetchData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        prepareFetchData();
    }

    /**
     * Fragment是否延迟加载，如果延迟加载，只有未获取数据时才去获取数据
     */
    private void prepareFetchData() {
        if ((getUserVisibleHint() || !isLazyLoad) && isViewInitiated && !isDataInitiated) {
            initWithDataDelay();
            isDataInitiated = true;
        }
    }


    /**
     * 是否需要延迟加载，默认是延迟加载，true为延迟加载
     *
     * @param lazyLoad
     */
    public void setLazyLoad(boolean lazyLoad) {
        isLazyLoad = lazyLoad;
    }

    protected abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    protected abstract void setListener();

    /**
     * @return true=使用状态栏一体化，false=不使用
     */
    private boolean isTranslateStatusBar() {
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) && translateStatusBar()) {
            return true;
        }
        return false;
    }

    /**
     * 子类重写此方法来控制是否状态栏一体化
     *
     * @return
     */
    protected boolean translateStatusBar() {
        return false;
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 获取数据填充fragment,不需要手动调用
     */
    public abstract void initWithDataDelay();

    public boolean isDataInitiated() {
        return isDataInitiated;
    }

    public void setDataInitiated(boolean dataInitiated) {
        isDataInitiated = dataInitiated;
    }

    @Override
    public void showLoading() {
        LoadingDialog.showIfNotExist(mBaseContext, false);
    }

    @Override
    public void hideLoding() {
        LoadingDialog.dismissIfExist();
    }

    @Override
    public Activity getAct() {
        return mBaseContext;
    }

    @Override
    public void onDestroy() {
        BasePresenter.notifyIPresenter(BasePresenter.LifeStyle.onDestroy, mAllPresenters);

        super.onDestroy();
    }


}
