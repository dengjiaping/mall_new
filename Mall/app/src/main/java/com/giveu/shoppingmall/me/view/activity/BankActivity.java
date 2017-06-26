package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.me.adapter.BankListAdapter;
import com.giveu.shoppingmall.model.bean.response.BankListBean;

import java.util.ArrayList;


/**
 * 支持银行卡列表
 * Created by 101900 on 2016/12/27.
 */

public class BankActivity extends BaseActivity {
    private ListView lv_bank;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_bank);
        lv_bank = (ListView) findViewById(R.id.lv_bank);
        baseLayout.setTitle("支持银行卡列表");
    }

    @Override
    public void setListener() {
    }

    BankListAdapter bankListAdapter = null;

    @Override
    public void setData() {
        bankListAdapter = new BankListAdapter(this);
//        ApiImpl.getBankList(mBaseContext, new ResponseListener<BankListBean>() {
//            @Override
//            public void onSuccess(BankListBean response) {
//                bankListAdapter.setItemList(response.data.list);
//            }
//
//            @Override
//            public void onError(BaseBean errorBean) {
//            }
//        });
        BankListBean response = new BankListBean();
        response.list = new ArrayList<>();
        BankListBean.ListBean listBean = new BankListBean.ListBean();
        listBean.smallIco = "";
        listBean.val = "工商银行";
        response.list.add(listBean);
        response.list.add(listBean);
        response.list.add(listBean);
        response.list.add(listBean);
        response.list.add(listBean);
        bankListAdapter.setItemList(response.list);
        lv_bank.setAdapter(bankListAdapter);
    }
    public static void startIt(Activity mActivity){
        Intent intent = new Intent(mActivity, BankActivity.class);
        mActivity.startActivity(intent);
    }
}
