package com.giveu.shoppingmall.cash.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.CashRecordsResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by 513419 on 2017/6/26.
 */

public class CaseRecordActivity extends BaseActivity {

    @BindView(R.id.lv)
    ListView lv;
    private LvCommonAdapter<CashRecordsResponse> caseRecordAdapter;
    private List<CashRecordsResponse> caseRecordList;

    public static void startIt(Activity activity) {
        Intent intent = new Intent(activity, CaseRecordActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_case_record);
        baseLayout.setTitle("取现记录");
        caseRecordList = new ArrayList<>();

        caseRecordAdapter = new LvCommonAdapter<CashRecordsResponse>(mBaseContext, R.layout.lv_case_record_item, caseRecordList) {
            @Override
            protected void convert(ViewHolder viewHolder, CashRecordsResponse item, int position) {
                TextView tvLoan = viewHolder.getView(R.id.tv_loan);
                TextView tvStatus = viewHolder.getView(R.id.tv_status);
                TextView tvSource = viewHolder.getView(R.id.tv_source);
                TextView tvDate = viewHolder.getView(R.id.tv_date);
                if(item != null){
                    tvLoan.setText( StringUtils.nullToEmptyString(item.loan));
                    tvStatus.setText(StringUtils.nullToEmptyString(item.status));
                    tvSource.setText(StringUtils.nullToEmptyString(item.source));
                    //转换格式
                    String appDate = StringUtils.cashFormatDate(item.appDate);
                    tvDate.setText(StringUtils.nullToEmptyString(appDate));
                    if(item.status.contains("失败")){
                        tvStatus.setTextColor(getResources().getColor(R.color.color_ff2a2a));
                    }else{
                        tvStatus.setTextColor(getResources().getColor(R.color.title_color));
                    }
                }
            }
        };
        lv.setAdapter(caseRecordAdapter);
    }

    @Override
    public void setData() {
        ApiImpl.qxRecords(mBaseContext, LoginHelper.getInstance().getIdPerson(), new BaseRequestAgent.ResponseListener<CashRecordsResponse>() {
            @Override
            public void onSuccess(CashRecordsResponse response) {
                if (response != null) {
                    caseRecordList = response.data;
                    if(CommonUtils.isNullOrEmpty(caseRecordList)){
                        baseLayout.showEmpty("暂无取现记录");
                        lv.setVisibility(View.GONE);
                    }else{
                        lv.setVisibility(View.VISIBLE);
                        caseRecordAdapter.setData(caseRecordList);
                        caseRecordAdapter.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }

    @Override
    public void setListener() {
    }
}
