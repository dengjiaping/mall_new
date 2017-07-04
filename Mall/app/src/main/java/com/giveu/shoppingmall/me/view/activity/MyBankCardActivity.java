package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.BankCardListResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.widget.dialog.CustomDialogUtil;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

import java.util.List;

import butterknife.BindView;


/**
 * 我的银行卡
 * Created by 101900 on 2017/5/2.
 */

public class MyBankCardActivity extends BaseActivity {


    @BindView(R.id.lv_bank_card)
    ListView lvBankCard;
    LvCommonAdapter<BankCardListResponse> bankListAdapter;
    CustomDialogUtil dialog;
    @BindView(R.id.scroll_view)
    ScrollView scrollView;
    @BindView(R.id.tv_defalut_bank_name)
    TextView tvDefalutBankName;
    @BindView(R.id.tv_defalut_bank_card_no)
    TextView tvDefalutBankCardNo;

    public static void startIt(Activity mActivity) {
        Intent intent = new Intent(mActivity, MyBankCardActivity.class);
        mActivity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_bankcard);
        baseLayout.setTitle("我的银行卡");
        baseLayout.setRightImage(R.drawable.ic_add_bank_card);
        //从最上方显示
        scrollView.smoothScrollTo(0, 20);
    }

    @Override
    public void setListener() {
        super.setListener();
        lvBankCard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                dialog = new CustomDialogUtil(mBaseContext);
                dialog.getDialogMode3("删除", "设置默认代扣卡", "取消", new View.OnClickListener() {
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
                                if (bankListAdapter != null && bankListAdapter.getCount() > 0) {
                                    bankListAdapter.getData().remove(position);
                                    bankListAdapter.notifyDataSetChanged();
                                }
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
//                        String billType = banksBean.billType;
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
//                            defalutBank(bankCode, bankName, bankNo, phone, billType, protocolNo);
//                            //传给取现换默认卡用
//                            Intent data = new Intent(mBaseContext, MyCashActivity.class);
//                            data.putExtra(Const.CASHBANKNAME, bankName);
//                            data.putExtra(Const.CASHBANKNO, no);
//                            data.putExtra(Const.CASHBANKICON, banksBean.smallIco);
//                            setResult(RESULT_OK, data);
//                        }
                        dialog.dismissDialog();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismissDialog();
                    }
                }).show();
            }
        });
    }

    @Override
    public void setData() {

        ApiImpl.getBankCardInfo(mBaseContext, "11415969", new BaseRequestAgent.ResponseListener<BankCardListResponse>() {
            @Override
            public void onSuccess(BankCardListResponse response) {
                List<BankCardListResponse> bankList = response.data;
                if (CommonUtils.isNotNullOrEmpty(bankList)) {

                    for (BankCardListResponse bankCardListResponse : bankList) {
                        if ("1".equals(bankCardListResponse.isDefault)) {
                            //默认代扣卡
                            tvDefalutBankName.setText(StringUtils.nullToEmptyString(bankCardListResponse.bankName));
                            tvDefalutBankCardNo.setText(StringUtils.nullToEmptyString(bankCardListResponse.bankNo));
                        }
                    }
                    bankListAdapter = new LvCommonAdapter<BankCardListResponse>(mBaseContext, R.layout.lv_bank_card_item, bankList) {
                        @Override
                        protected void convert(ViewHolder viewHolder, BankCardListResponse item, int position) {
                            TextView tv_bank_name = viewHolder.getView(R.id.tv_bank_name);
                            TextView tv_bank_card_no = viewHolder.getView(R.id.tv_bank_card_no);
                            tv_bank_name.setText(StringUtils.nullToEmptyString(item.bankName));
                            tv_bank_card_no.setText(StringUtils.nullToEmptyString(item.bankNo));
                        }
                    };
                    lvBankCard.setAdapter(bankListAdapter);
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }

}
