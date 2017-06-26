package com.giveu.shoppingmall.cash.adpter;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.widget.dialog.ConfirmDialog;

import java.util.List;

/**
 * Created by 513419 on 2017/6/26.
 */

public class AddressManageAdapter extends LvCommonAdapter<String> {

    private ConfirmDialog deleteDialog;

    public AddressManageAdapter(Context context, List<String> datas) {
        super(context, R.layout.lv_add_address_item, datas);
        initDialog();
    }

    private void initDialog() {
        deleteDialog = new ConfirmDialog((Activity) mContext);
        deleteDialog.setContent("确定要删除该地址吗？");
        deleteDialog.setOnChooseListener(new ConfirmDialog.OnChooseListener() {
            @Override
            public void confirm() {
                deleteDialog.dismiss();
            }

            @Override
            public void cancle() {
                deleteDialog.dismiss();
            }
        });
    }

    @Override
    protected void convert(ViewHolder holder, String item, int position) {
        holder.setOnClickListener(R.id.tv_delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.show();
            }
        });

    }
}