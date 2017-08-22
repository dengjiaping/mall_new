package com.giveu.shoppingmall.me.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.base.lvadapter.ItemViewDelegate;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.MultiItemTypeAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.model.bean.response.CouponListResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 101912 on 2017/8/16.
 */

public class CouponAdapter extends MultiItemTypeAdapter<CouponListResponse> {


    private CustomDialog dialog;
    private LvCommonAdapter<String> dialogAdapter;
    private ArrayList<String> rulesData;

    public CouponAdapter(Context context, final List<CouponListResponse> datas) {
        super(context, datas);
        addItemViewDelegate(new ItemViewDelegate<CouponListResponse>() {
            @Override
            public int getItemViewLayoutId() {
                return R.layout.lv_coupon_item;
            }

            @Override
            public boolean isForViewType(CouponListResponse item, int position) {
                return !item.isDivider;
            }

            @Override
            public void convert(ViewHolder holder, final CouponListResponse couponBean, int position) {
                //status为0表示未激活，1表示已激活，两者都是为使用状态，显示样式一致
                //status为2表示已使用，3表示已失效
                if ("0".equals(couponBean.status) || "1".equals(couponBean.status)) {
                    holder.setBackgroundRes(R.id.rl_item_left, R.drawable.bg_valid);
                    holder.setVisible(R.id.iv_used, false);
                    holder.setVisible(R.id.iv_overdue, false);
                    holder.setVisible(R.id.rl_oval_button, true);
                } else {
                    holder.setBackgroundRes(R.id.rl_item_left, R.drawable.bg_invalid);
                    holder.setVisible(R.id.rl_oval_button, false);
                    if ("2".equals(couponBean.status)) {
                        holder.setVisible(R.id.iv_used, true);
                        holder.setVisible(R.id.iv_overdue, false);
                    } else {
                        holder.setVisible(R.id.iv_used, false);
                        holder.setVisible(R.id.iv_overdue, true);
                    }
                }
                holder.setText(R.id.tv_descpt, couponBean.descpt);
                if (StringUtils.isNotNull(couponBean.price))
                    holder.setText(R.id.tv_price, couponBean.price);
                holder.setText(R.id.iv_subtitle, couponBean.subtitle);
                String startTime = StringUtils.convertTime(String.valueOf(couponBean.beginTime));
                String endTime = StringUtils.convertTime(String.valueOf(couponBean.endTime));
                holder.setText(R.id.tv_date, startTime + "-" + endTime);
                //立即使用button
                holder.setOnClickListener(R.id.ll_root, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.showLongToast(couponBean.useRuleDesc);
                    }
                });
                //使用规则dialog
                holder.setOnClickListener(R.id.rl_detail, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (CommonUtils.isNotNullOrEmpty(couponBean.useRule)) {
                            initDialog(couponBean.useRule);
                            dialog.show();
                        }
                    }
                });
            }
        });

        addItemViewDelegate(new ItemViewDelegate<CouponListResponse>() {
            @Override
            public int getItemViewLayoutId() {
                return R.layout.lv_invalid_item;
            }

            @Override
            public boolean isForViewType(CouponListResponse item, int position) {
                return item.isDivider;
            }

            @Override
            public void convert(ViewHolder holder, CouponListResponse couponBean, int position) {
                holder.setOnClickListener(R.id.rl_divider, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });

            }
        });
    }

    //初始化使用规则dialog
    private void initDialog(String useRule) {
        dialog = new CustomDialog((Activity) mContext, R.layout.dialog_coupon_rule, R.style.customerDialog, Gravity.CENTER, false);
        ListView lvDialogRule = (ListView) dialog.findViewById(R.id.lv_dialog_rule);
        TextView tvConfirm = (TextView) dialog.findViewById(R.id.tv_confirm);
        rulesData = new ArrayList<>();
        String[] rules = useRule.split("；");
        for (int i = 0; i < rules.length; i++) {
            rulesData.add(rules[i]);
        }
        dialogAdapter = new LvCommonAdapter<String>(mContext, R.layout.lv_dialog_item, rulesData) {
            @Override
            protected void convert(ViewHolder viewHolder, String item, int position) {
                viewHolder.setText(R.id.tv_rule, item);
            }
        };
        lvDialogRule.setAdapter(dialogAdapter);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
