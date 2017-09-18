package com.giveu.shoppingmall.index.view.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.model.bean.response.CreateOrderResponse;

import java.util.List;

/**
 * Created by 524202 on 2017/9/5.
 */

public class ChooseCardsDialog {
    private CustomDialog mDialog;
    private Activity mActivity;
    private ListView listView;
    private List<CreateOrderResponse.CardListBean> datas;
    public long checkIndex;
    private OnChooseTypeListener listener;
    private LvCommonAdapter mAdpter;
    private TextView tvBack;

    public ChooseCardsDialog(Activity mActivity, List datas, OnChooseTypeListener listener) {
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

    public void show(long index) {
        checkIndex = index;
        mDialog.show();
        mAdpter.notifyDataSetChanged();
    }

    private void initView(final View contentView) {
        listView = (ListView) contentView.findViewById(R.id.dialog_choose_listview);
        listView.setAdapter(mAdpter = new LvCommonAdapter<CreateOrderResponse.CardListBean>(mActivity, R.layout.dialog_choose_coupon_item, datas) {
            @Override
            protected void convert(com.giveu.shoppingmall.base.lvadapter.ViewHolder holder, final CreateOrderResponse.CardListBean item, final int position) {
                holder.setChecked(R.id.dialog_choose_coupon_item_radio, checkIndex == item.id);
                holder.setText(R.id.dialog_choose_coupon_item_radio, item.name);
                holder.setOnClickListener(R.id.dialog_choose_coupon_item_radio, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onChooseType(item.id, item.price, item.name);
                        mDialog.dismiss();
                    }
                });
            }
        });

        tvBack = (TextView) contentView.findViewById(R.id.dialog_choose_title);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
    }

    public interface OnChooseTypeListener {
        void onChooseType(long id, String price, String name);
    }


}
