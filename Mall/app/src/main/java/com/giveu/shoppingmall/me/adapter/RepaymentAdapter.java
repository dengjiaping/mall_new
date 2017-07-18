package com.giveu.shoppingmall.me.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.lvadapter.ItemViewDelegate;
import com.giveu.shoppingmall.base.lvadapter.MultiItemTypeAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.model.bean.response.RepaymentBean;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.TypeUtlis;

import java.util.List;


/**
 * Created by 513419 on 2017/6/22.
 */

public class RepaymentAdapter extends MultiItemTypeAdapter<RepaymentBean> {

    public RepaymentAdapter(Context context, final List<RepaymentBean> datas) {
        super(context, datas);
        addItemViewDelegate(new ItemViewDelegate<RepaymentBean>() {
            @Override
            public int getItemViewLayoutId() {
                return R.layout.lv_bill_title;
            }

            @Override
            public boolean isForViewType(RepaymentBean item, int position) {
                return item.isTitle;
            }

            @Override
            public void convert(final ViewHolder holder, final RepaymentBean item, int position) {
                final CheckBox cbChoose = holder.getView(R.id.cb_choose);
                holder.setText(R.id.tv_title, TypeUtlis.getProductType(item.productType) + "：¥" + StringUtils.format2(item.repayAmount));
                cbChoose.setChecked(item.isChoose);
                cbChoose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //遍历查询当前点击产品类型是否与上次勾选的一致，不一致给予提醒
                        for (RepaymentBean repaymentBean : datas) {
                            if (repaymentBean.isChoose && !item.productType.equals(repaymentBean.productType)) {
                                cbChoose.setChecked(!cbChoose.isChecked());
                                ToastUtils.showShortToast("只能勾选分期产品和取现随借随还其中一项");
                                return;
                            }
                        }
                        item.isChoose = cbChoose.isChecked();
                        //更新activity还款数目，选中为加，取消勾选为减
                        if (listener != null) {
                            double money = 0;
                            //计算选中项的总金额
                            for (RepaymentBean repaymentBean : mDatas) {
                                if (repaymentBean.isChoose) {
                                    money += StringUtils.string2Double(repaymentBean.repayAmount);
                                }
                            }
                            listener.moneyChange(money, item.productType);
                        }
                    }
                });
                holder.setOnClickListener(R.id.ll_title, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (RepaymentBean repaymentBean : datas) {
                            if (repaymentBean.isChoose && !item.productType.equals(repaymentBean.productType)) {
                                ToastUtils.showShortToast("只能勾选分期产品和取现随借随还其中一项");
                                return;
                            }
                        }
                        cbChoose.setChecked(!cbChoose.isChecked());
                        item.isChoose = cbChoose.isChecked();
                        if (listener != null) {
                            double money = 0;
                            //计算选中项的总金额
                            for (RepaymentBean repaymentBean : mDatas) {
                                if (repaymentBean.isChoose) {
                                    money += StringUtils.string2Double(repaymentBean.repayAmount);
                                }
                            }
                            listener.moneyChange(money, item.productType);
                        }
                    }
                });
            }
        });

        addItemViewDelegate(new ItemViewDelegate<RepaymentBean>() {
            @Override
            public int getItemViewLayoutId() {
                return R.layout.lv_bill_item;
            }

            @Override
            public boolean isForViewType(RepaymentBean item, int position) {
                return !item.isTitle;
            }

            @Override
            public void convert(ViewHolder holder, RepaymentBean item, int position) {
                if (StringUtils.isNotNull(item.paymentNum)) {
                    holder.setText(R.id.tv_date, item.numInstalment + "/" + item.paymentNum + "期");
                } else {
                    holder.setText(R.id.tv_date, item.repayDate);
                }
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
        void moneyChange(double money, String productType);
    }

    private OnMoneyChangeListener listener;

    public void setOnMoneyChangetListener(OnMoneyChangeListener listener) {
        this.listener = listener;
    }

}
