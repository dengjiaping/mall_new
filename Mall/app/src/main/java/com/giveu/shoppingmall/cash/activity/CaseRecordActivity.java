package com.giveu.shoppingmall.cash.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.cash.adpter.CaseRecordAdapter;
import com.giveu.shoppingmall.view.pulltorefresh.PullToRefreshBase;
import com.giveu.shoppingmall.view.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by 513419 on 2017/6/26.
 */

public class CaseRecordActivity extends BaseActivity {

    @BindView(R.id.ptrlv)
    PullToRefreshListView ptrlv;
    private CaseRecordAdapter caseRecordAdapter;
    private ArrayList<String> caseRecordList;
    private int pageIndex = 1;
    private final int pageSize = 10;

    public static void startIt(Activity activity) {
        Intent intent = new Intent(activity, CaseRecordActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_case_record);
        baseLayout.setTitle("取现记录");
        caseRecordList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            caseRecordList.add(i + "");
        }
        caseRecordAdapter = new CaseRecordAdapter(mBaseContext, caseRecordList);
        ptrlv.setAdapter(caseRecordAdapter);
        ptrlv.setMode(PullToRefreshBase.Mode.BOTH);
        ptrlv.setPullLoadEnable(false);
    }

    @Override
    public void setData() {

    }

    @Override
    public void setListener() {
        super.setListener();
        ptrlv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex = 1;
                setData();
                ptrlv.setPullLoadEnable(false);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                setData();
                ptrlv.setPullRefreshEnable(true);
            }
        });
    }
}
