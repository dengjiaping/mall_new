package com.giveu.shoppingmall.index.view.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;

import java.util.List;

/**
 * Created by 524202 on 2017/9/5.
 */

public class ChooseCouponDialog {
    private CustomDialog mDialog;
    private Activity mActivity;
    private ListView listView;
    private List<String> datas;
    public int checkIndex;
    private OnChooseTypeListener listener;
    private LvCommonAdapter mAdpter;
    private ImageView ivBack;

    public ChooseCouponDialog(Activity mActivity, List datas, OnChooseTypeListener listener) {
        this.mActivity = mActivity;
        View contentView = View.inflate(mActivity, R.layout.dialog_choose_coupon, null);
        mDialog = new CustomDialog(mActivity, contentView, R.style.login_error_dialog_Style, Gravity.BOTTOM, true);
        this.listener = listener;
        this.datas = datas;
        initView(contentView);
        mDialog.setCancelable(false);
    }

    public void show() {
        show(0);
    }

    public void show(int index) {
        checkIndex = index;
        mDialog.show();
        mAdpter.notifyDataSetChanged();
    }

    private void initView(final View contentView) {
        listView = (ListView) contentView.findViewById(R.id.dialog_choose_listview);
        listView.setAdapter(mAdpter = new LvCommonAdapter<String>(mActivity, R.layout.dialog_choose_coupon_item, datas) {
            @Override
            protected void convert(com.giveu.shoppingmall.base.lvadapter.ViewHolder holder, final String title, final int position) {
                holder.setChecked(R.id.dialog_choose_coupon_item_radio, checkIndex == position);
                holder.setText(R.id.dialog_choose_coupon_item_radio, title);
                holder.setOnClickListener(R.id.dialog_choose_coupon_item_radio, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onChooseType(position, title);
                        mDialog.dismiss();
                    }
                });
            }
        });

        ivBack = (ImageView) contentView.findViewById(R.id.dialog_choose_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
    }

    public interface OnChooseTypeListener {
        void onChooseType(int type, String typeMsg);
    }


}
