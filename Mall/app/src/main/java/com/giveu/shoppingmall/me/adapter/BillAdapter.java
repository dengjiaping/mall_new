package com.giveu.shoppingmall.me.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.lvadapter.ItemViewDelegate;
import com.giveu.shoppingmall.base.lvadapter.MultiItemTypeAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.model.bean.response.BillBean;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.TypeUtlis;

import java.util.List;


/**
 * Created by 513419 on 2017/6/22.
 */

public class BillAdapter extends MultiItemTypeAdapter<BillBean> {

    public BillAdapter(Context context, List<BillBean> datas) {
        super(context, datas);
        addItemViewDelegate(new ItemViewDelegate<BillBean>() {
            @Override
            public int getItemViewLayoutId() {
                return R.layout.lv_bill_title;
            }

            @Override
            public boolean isForViewType(BillBean item, int position) {
                return item.isTitle;
            }

            @Override
            public void convert(ViewHolder holder, final BillBean item, int position) {
                final CheckBox cbChoose = holder.getView(R.id.cb_choose);
                holder.setText(R.id.tv_title, TypeUtlis.getProductType(item.productType) + "：¥" + StringUtils.format2(item.repayAmount));
                cbChoose.setChecked(item.isChoose);
                cbChoose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            try {
                                double money = Double.parseDouble(item.repayAmount);
                                if (!cbChoose.isChecked()) {
                                    money = -money;
                                }
                                listener.moneyChange(money);
                            } catch (Exception e) {

                            }
                        }
                    }
                });
                holder.setOnClickListener(R.id.ll_title, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cbChoose.setChecked(!cbChoose.isChecked());
                        item.isChoose = cbChoose.isChecked();
                        if (listener != null) {
                            try {
                                double money = Double.parseDouble(item.repayAmount);
                                if (!item.isChoose) {
                                    money = -money;
                                }
                                listener.moneyChange(money);
                            } catch (Exception e) {

                            }
                        }
                    }
                });
            }
        });

        addItemViewDelegate(new ItemViewDelegate<BillBean>() {
            @Override
            public int getItemViewLayoutId() {
                return R.layout.lv_bill_item;
            }

            @Override
            public boolean isForViewType(BillBean item, int position) {
                return !item.isTitle;
            }

            @Override
            public void convert(ViewHolder holder, BillBean item, int position) {
                holder.setText(R.id.tv_date, item.repayDate);
                holder.setText(R.id.tv_money, "¥" + StringUtils.format2(item.repayAmount));
                holder.setText(R.id.tv_type, TypeUtlis.getCreditTypeText(item.creditType));
                ImageView ivOverDue = holder.getView(R.id.iv_overdue);
                if (item.isOverdue) {
                    ivOverDue.setVisibility(View.VISIBLE);
                } else {
                    ivOverDue.setVisibility(View.GONE);
                }
            }
        });
    }

    public interface OnMoneyChangeListener {
        void moneyChange(double money);
    }

    private OnMoneyChangeListener listener;

    public void setOnMoneyChangetListener(OnMoneyChangeListener listener) {
        this.listener = listener;
    }

}
