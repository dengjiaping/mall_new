package com.giveu.shoppingmall.cash.adpter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

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
    String mId;//删除项的id
    int mPosition;//删除项的位置

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
                        if (datas.size() > 0) {
                            datas.remove(mPosition);
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(BaseBean errorBean) {
                        CommonLoadingView.showErrorToast(errorBean);
                    }
                });
                deleteDialog.dismiss();
            }

            @Override
            public void cancle() {
                deleteDialog.dismiss();
            }
        });
    }

    @Override
    protected void convert(ViewHolder holder, final AddressListResponse item, final int position) {
        TextView tvDefaultCheck = holder.getView(R.id.tv_default_check);
        holder.setText(R.id.tv_name, item.custName);
        holder.setText(R.id.tv_phone, item.phone);
        holder.setText(R.id.tv_address, item.province + item.city + item.region + item.street + item.address);
        //holder.setChecked(R.id.cb_default, 1 == item.isDefault ? true : false);
        holder.setOnClickListener(R.id.tv_edit, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAddressActivity.startIt((Activity) mContext, item);
            }
        });
        holder.setOnClickListener(R.id.tv_delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPosition = position;
                mId = item.id;
                deleteDialog.show();
            }
        });
        holder.setOnClickListener(R.id.tv_default_check, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (0 == item.isDefault) {
                    //不是默认地址设置成默认地址
                    ApiImpl.setDefaultAddress((Activity) mContext, item.id, LoginHelper.getInstance().getIdPerson(), new BaseRequestAgent.ResponseListener<BaseBean>() {
                        @Override
                        public void onSuccess(BaseBean response) {
                            mPosition = position;
                            for (int i = 0; i < mDatas.size(); i++) {
                                if (i != mPosition) {
                                    //其他项取消勾选
                                    mDatas.get(i).isDefault = 0;
                                } else {
                                    //选择项勾选
                                    mDatas.get(mPosition).isDefault = 1;
                                }
                            }
                            notifyDataSetChanged();
                            ToastUtils.showShortToast("设置默认地址成功");
                        }

                        @Override
                        public void onError(BaseBean errorBean) {
                            CommonLoadingView.showErrorToast(errorBean);
                        }
                    });
                }
            }
        });
        if (1 == item.isDefault) {
            //选择项选中状态
            Drawable drawableLeft = mContext.getResources().getDrawable(R.drawable.ic_round_checkbox_checked);
            tvDefaultCheck.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null);
            tvDefaultCheck.setCompoundDrawablePadding(10);
        } else {
            //未选中状态
            Drawable drawableLeft = mContext.getResources().getDrawable(R.drawable.ic_round_checkbox_unchecked);
            tvDefaultCheck.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null);
            tvDefaultCheck.setCompoundDrawablePadding(10);
        }

    }
}