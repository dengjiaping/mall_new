package com.giveu.shoppingmall.utils;

import android.graphics.Canvas;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.giveu.shoppingmall.model.bean.response.ShoppingBean;

import java.util.List;


public class ItemHeaderDecoration extends RecyclerView.ItemDecoration {

    private List<ShoppingBean> mDatas;
    private CheckListener mCheckListener;
    public int currentTag = 0;//标记当前左侧选中的position，因为有可能选中的item，右侧不能置顶，所以强制替换掉当前的tag

    public void setCheckListener(CheckListener checkListener) {
        mCheckListener = checkListener;
    }

    public ItemHeaderDecoration(List datas) {
        this.mDatas = datas;
    }


    public ItemHeaderDecoration setData(List mDatas) {
        this.mDatas = mDatas;
        return this;
    }

    public void setCurrentTag(int currentTag) {
        this.currentTag = currentTag;
    }


    @Override
    public void onDrawOver(Canvas canvas, final RecyclerView parent, RecyclerView.State state) {
        int pos = ((GridLayoutManager) (parent.getLayoutManager())).findFirstVisibleItemPosition();
        if (pos == -1) {
            //empty
            return;
        }
        int tag = mDatas.get(pos).getTag();

        if (tag != currentTag) {
            currentTag = tag;
            mCheckListener.check(tag, false);
        }
    }

    public interface CheckListener {
        void check(int position, boolean isScroll);
    }
}
