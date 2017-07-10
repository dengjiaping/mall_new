package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.BankListResponse;
import com.giveu.shoppingmall.utils.ImageUtils;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * 支持银行卡列表
 * Created by 101900 on 2016/12/27.
 */

public class BankActivity extends BaseActivity {
    public LvCommonAdapter<BankListResponse> bankListAdapter;
    public List<BankListResponse> bankList;
    @BindView(R.id.lv_bank)
    ListView lvBank;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_bank);
        baseLayout.setTitle("支持银行卡列表");
        bankList = new ArrayList<>();
        bankListAdapter = new LvCommonAdapter<BankListResponse>(mBaseContext, R.layout.bank_item, bankList) {
            @Override
            protected void convert(ViewHolder viewHolder, BankListResponse item, int position) {
                TextView tvBank = viewHolder.getView(R.id.tv_bank);
                ImageView ivBank = viewHolder.getView(R.id.iv_bank);
                if (item != null) {
                    tvBank.setText(item.dicName);
                    ImageUtils.loadImage(item.url, R.drawable.defalut_img_88_88, ivBank);
                }
            }
        };
        lvBank.setAdapter(bankListAdapter);
    }

    @Override
    public void setListener() {
    }


    @Override
    public void setData() {
        ApiImpl.getUsableBankList(mBaseContext, new BaseRequestAgent.ResponseListener<BankListResponse>() {
            @Override
            public void onSuccess(BankListResponse response) {
                if (response != null) {
                    bankList = response.data;
                }
                bankListAdapter.setData(bankList);
                bankListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });

    }

    public static void startIt(Activity mActivity) {
        Intent intent = new Intent(mActivity, BankActivity.class);
        mActivity.startActivity(intent);
    }
}
