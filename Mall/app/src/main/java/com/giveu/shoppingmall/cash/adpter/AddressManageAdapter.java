package com.giveu.shoppingmall.cash.adpter;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.cash.view.activity.AddAddressActivity;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.AddressListResponse;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.widget.dialog.ConfirmDialog;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

import java.util.List;

/**
 * Created by 513419 on 2017/6/26.
 */

public class AddressManageAdapter extends LvCommonAdapter<AddressListResponse> {

    private ConfirmDialog deleteDialog;
    List<AddressListResponse> datas;
    String mId;

    public AddressManageAdapter(Context context, List<AddressListResponse> datas) {
        super(context, R.layout.lv_add_address_item, datas);
        this.datas = datas;
        initDialog();
    }

    private void initDialog() {
        deleteDialog = new ConfirmDialog((Activity) mContext);
        deleteDialog.setContent("确定要删除该地址吗？");
        deleteDialog.setOnChooseListener(new ConfirmDialog.OnChooseListener() {
            @Override
            public void confirm() {
                ApiImpl.deleteAddress((Activity) mContext, mId, LoginHelper.getInstance().getIdPerson(), new BaseRequestAgent.ResponseListener<BaseBean>() {
                    @Override
                    public void onSuccess(BaseBean response) {
                        ToastUtils.showShortToast("删除成功");
                        deleteDialog.dismiss();
                    }

                    @Override
                    public void onError(BaseBean errorBean) {
                        CommonLoadingView.showErrorToast(errorBean);
                    }
                });
            }

            @Override
            public void cancle() {
                deleteDialog.dismiss();
            }
        });
    }

    @Override
    protected void convert(ViewHolder holder, final AddressListResponse item, final int position) {
        holder.setText(R.id.tv_name, item.custName);
        holder.setText(R.id.tv_phone, item.phone);
        holder.setText(R.id.tv_address, item.address);
        holder.setChecked(R.id.cb_default, 1 == item.isDefault ? true : false);
        holder.setOnClickListener(R.id.tv_edit, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAddressActivity.startIt((Activity) mContext, item);
            }
        });
        holder.setOnClickListener(R.id.tv_delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mId = item.id;
                deleteDialog.show();
            }
        });
        holder.setOnClickListener(R.id.cb_default, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShortToast("默认选中" + position);
            }
        });
    }
}