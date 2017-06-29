package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.mynet.BaseBean;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.widget.dialog.CustomDialogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * 我的银行卡
 * Created by 101900 on 2017/5/2.
 */

public class MyBankCardActivity extends BaseActivity {


    @BindView(R.id.lv_bank_card)
    ListView lvBankCard;
    LvCommonAdapter<BaseBean> bankListAdapter;
    CustomDialogUtil dialog;
    public static void startIt(Activity mActivity) {
        Intent intent = new Intent(mActivity, MyBankCardActivity.class);
        mActivity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_bankcard);
        baseLayout.setTitle("我的银行卡");
    }

    @Override
    public void setListener() {
        super.setListener();
        lvBankCard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                dialog = new CustomDialogUtil(mBaseContext);
                dialog.getDialogMode2("删除", "设置默认代扣卡", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CustomDialogUtil customDialogUtil = new CustomDialogUtil(mBaseContext);
                        customDialogUtil.getDialogMode1("提示", "是否要删除该银行卡？", "确定", "取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //删除
//                                BankResponseBean.BanksBean banksBean = bankAdapter.getItem(position);
//                                List<BankResponseBean.BanksBean> list1 = bankAdapter.getItemList();
//                                String bankCode = banksBean.bankCode;
//                                String bankName = banksBean.bankName;
//                                String bankNo = banksBean.bankNo;
//                                if (list1.size() > 0 && list1 != null) {
//                                    deleteBank(bankAdapter, position, bankCode, bankName, bankNo);
//                                }
                                dialog.dismissDialog();
                            }
                        }, null).show();

                    }
                }, new View.OnClickListener() {
                    //设置默认代扣卡
                    @Override
                    public void onClick(View view) {
//                        clickDefalutBankPosition = position;
//                        BankResponseBean.BanksBean banksBean = bankAdapter.getItem(position);
//                        List<BankResponseBean.BanksBean> list1 = bankAdapter.getItemList();
//                        String bankCode = banksBean.bankCode;
//                        String bankName = banksBean.bankName;
//                        String bankNo = banksBean.bankNo;
//                        String protocolNo = banksBean.protocolNo;
//                        String type = banksBean.type;
//                        if (list1.size() > 0 && list1 != null) {
//                            phone = bankAdapter.getItemList().get(position).phone;
//                            if (phone == null || phone.equals("")) {
//                                phone = " ";
//                            }
//                            String no = bankNo;
//                            if (no != null && no.length() > 4) {
//                                no = no.substring(no.length() - 4, no.length());
//                                tv_cardnumber.setText("(尾号" + no + ")");
//                            }
//                            defalutBank(bankCode, bankName, bankNo, phone, type, protocolNo);
//                            //传给取现换默认卡用
//                            Intent data = new Intent(mBaseContext, MyCashActivity.class);
//                            data.putExtra(Const.CASHBANKNAME, bankName);
//                            data.putExtra(Const.CASHBANKNO, no);
//                            data.putExtra(Const.CASHBANKICON, banksBean.smallIco);
//                            setResult(RESULT_OK, data);
//                        }
                        dialog.dismissDialog();
                    }
                }).show();
            }
        });
    }

    @Override
    public void setData() {
        BaseBean bean = new BaseBean();
        List<BaseBean> data = new ArrayList<>();
        data.add(bean);
        data.add(bean);
        data.add(bean);
        bankListAdapter = new LvCommonAdapter<BaseBean>(mBaseContext, R.layout.lv_bank_card_item,  data) {
            @Override
            protected void convert(ViewHolder viewHolder, BaseBean item, int position) {

            }
        };
        lvBankCard.setAdapter(bankListAdapter);
    }

}
