package com.giveu.shoppingmall.index.view.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.StringUtils;

import java.util.List;

/**
 * Created by 524202 on 2017/9/5.
 */

public abstract class ChooseDialog<T> {
    private CustomDialog mDialog;
    private Activity mActivity;
    private ListView listView;
    private List<T> datas;
    public long checkIndex;
    private LvCommonAdapter mAdpter;
    private TextView tvTitle;

    public ChooseDialog(Activity mActivity, List datas) {
        this.mActivity = mActivity;
        View contentView = View.inflate(mActivity, R.layout.dialog_choose_common_layout, null);
        mDialog = new CustomDialog(mActivity, contentView, R.style.login_error_dialog_Style, Gravity.BOTTOM, true);
        this.datas = datas;

        initView(contentView);
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
    }

    public void show() {
        show(0);
    }

    public void show(long index) {
        checkIndex = index;
        mDialog.show();
        mAdpter.notifyDataSetChanged();
    }

    private void initView(final View contentView) {
        listView = (ListView) contentView.findViewById(R.id.dialog_choose_listview);
        listView.setAdapter(mAdpter = new LvCommonAdapter<T>(mActivity, R.layout.dialog_choose_common_item, datas) {
            @Override
            protected void convert(com.giveu.shoppingmall.base.lvadapter.ViewHolder holder, final T item, final int position) {
                convertView(holder, item, position, checkIndex);
            }
        });

        tvTitle = (TextView) contentView.findViewById(R.id.dialog_choose_title);
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
    }

    public void setTitle(String title) {
        if (StringUtils.isNull(title)) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        }
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    public abstract void convertView(ViewHolder holder, T item, int position, long checkIndex);

    public void refreshData(List<T> datas) {
        if (CommonUtils.isNotNullOrEmpty(datas)) {
            this.datas.clear();
            this.datas.addAll(datas);
            mAdpter.notifyDataSetChanged();
        }
    }

    public void refreshData() {
        mAdpter.notifyDataSetChanged();
    }

}
