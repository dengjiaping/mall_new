package com.giveu.shoppingmall.cash.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.model.bean.response.RpmDetailResponse;
import com.giveu.shoppingmall.widget.ClickEnabledTextView;

import java.util.List;


/**
 * 月供详情Dialog
 * Created by 101900 on 2017/6/29.
 */

public class MonthlyDetailsDialog {
    private CustomDialog mDialog;
    private Activity mActivity;
    private ListView lv_monthly_details;
    private ClickEnabledTextView tv_commit;
    LvCommonAdapter<RpmDetailResponse> monthlyDetailsAdapter;
    List<RpmDetailResponse> rpmDetailList;
    public MonthlyDetailsDialog(Activity mActivity, List<RpmDetailResponse> rpmDetailList) {
        this.rpmDetailList = rpmDetailList;
        this.mActivity = mActivity;
    }

    public void showDialog() {
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.dialog_monthly_details, null);
        initView(convertView);
        mDialog = new CustomDialog(mActivity, convertView, R.style.login_error_dialog_Style, Gravity.CENTER, false);
        mDialog.setCancelable(false);
        mDialog.show();
        setListener();
    }

    private void setListener() {
        tv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
    }


    private void initView(View convertView) {
        tv_commit = (ClickEnabledTextView) convertView.findViewById(R.id.tv_commit);
        lv_monthly_details = (ListView) convertView.findViewById(R.id.lv_monthly_details);

        monthlyDetailsAdapter = new LvCommonAdapter<RpmDetailResponse>(mActivity, R.layout.lv_monthly_details_item, rpmDetailList) {
            @Override
            protected void convert(ViewHolder viewHolder, RpmDetailResponse item, int position) {
                TextView tv_stage_number =viewHolder.getView(R.id.tv_stage_number);
                TextView tv_price =viewHolder.getView(R.id.tv_price);
                TextView tv_date =viewHolder.getView(R.id.tv_date);
                tv_stage_number.setText(String.valueOf(item.repayNum));
                tv_price.setText(String.valueOf(item.monthPay));
                tv_date.setText(item.repayDate);
            }
        };
        lv_monthly_details.setAdapter(monthlyDetailsAdapter);
    }

}
