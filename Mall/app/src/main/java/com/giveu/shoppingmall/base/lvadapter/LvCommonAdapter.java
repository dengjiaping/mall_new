package com.giveu.shoppingmall.base.lvadapter;

import android.content.Context;

import com.giveu.shoppingmall.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * ListView的通用adapter
 *
 * @param <T>
 */
public abstract class LvCommonAdapter<T> extends MultiItemTypeAdapter<T> {

    public LvCommonAdapter(Context context, final int layoutId, List<T> datas) {
        super(context, datas);

        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position) {
                LvCommonAdapter.this.convert(holder, t, position);
            }
        });
    }

    protected abstract void convert(ViewHolder viewHolder, T item, int position);

    public void setData(List<T> dataList) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        if (CommonUtils.isNotNullOrEmpty(dataList)) {
            mDatas.clear();
            mDatas.addAll(dataList);
        } else {
            mDatas.clear();
        }

    }

    public List<T> getData() {
        return mDatas;
    }

}
