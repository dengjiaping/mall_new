package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.cash.view.activity.CashTypeActivity;
import com.giveu.shoppingmall.event.AddCardEvent;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.BankCardListResponse;
import com.giveu.shoppingmall.model.bean.response.PayPwdResponse;
import com.giveu.shoppingmall.recharge.view.dialog.PwdDialog;
import com.giveu.shoppingmall.recharge.view.dialog.PwdErrorDialog;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.ImageUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.widget.dialog.CustomDialogUtil;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    LvCommonAdapter<BankCardListResponse.BankInfoListBean> bankListAdapter;
    CustomDialogUtil dialogUtil;
    @BindView(R.id.scroll_view)
    ScrollView scrollView;
    @BindView(R.id.tv_defalut_bank_name)
    TextView tvDefalutBankName;
    @BindView(R.id.tv_defalut_bank_card_no)
    TextView tvDefalutBankCardNo;
    List<BankCardListResponse.BankInfoListBean> bankInfoList;
    @BindView(R.id.ll_default_bank_card)
    LinearLayout llDefaultBankCard;
    String defalutBankName;
    String defalutBankCardNo;
    String defalutBankIconUrl;
    @BindView(R.id.iv_bank)
    ImageView ivBank;
    @BindView(R.id.ll_my_card)
    LinearLayout llMyCard;

    public static void startIt(Activity mActivity) {
        Intent intent = new Intent(mActivity, MyBankCardActivity.class);
//        mActivity.setResult(RESULT_OK);
        mActivity.startActivity(intent);
    }

    public static void startIt(Activity mActivity, boolean needResult) {
        Intent intent = new Intent(mActivity, MyBankCardActivity.class);
        intent.putExtra("needResult", needResult);
        mActivity.setResult(RESULT_OK);
        mActivity.startActivityForResult(intent, 1);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_bankcard);
        baseLayout.setTitle("我的银行卡");
        baseLayout.setRightImageAndListener(R.drawable.ic_add_bank_card, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加银行卡
                AddBankCardFirstActivity.startIt(mBaseContext);
            }
        });
        llDefaultBankCard.requestFocus();
        dialogUtil = new CustomDialogUtil(mBaseContext);
        initAdapter();
        registerEventBus();//注册EventBus
    }

    /**
     * 初始化银行卡列表Adapter
     */
    private void initAdapter() {
        bankInfoList = new ArrayList<>();
        bankListAdapter = new LvCommonAdapter<BankCardListResponse.BankInfoListBean>(mBaseContext, R.layout.lv_bank_card_item, bankInfoList) {
            @Override
            protected void convert(ViewHolder viewHolder, BankCardListResponse.BankInfoListBean item, int position) {
                TextView tvBankName = viewHolder.getView(R.id.tv_bank_name);
                TextView tvBankCardNo = viewHolder.getView(R.id.tv_bank_card_no);
                ImageView ivBankItem = viewHolder.getView(R.id.iv_bank_item);
                tvBankName.setText(StringUtils.nullToEmptyString(item.bankName));
                tvBankCardNo.setText(StringUtils.nullToEmptyString(changeBankNoStyle(item.bankNo)));
                ImageUtils.loadImage(item.bankIconUrl, R.color.transparent, ivBankItem);
            }
        };
        lvBankCard.setAdapter(bankListAdapter);
    }

    @Override
    public void setListener() {
        final boolean needResult = getIntent().getBooleanExtra("needResult", false);
        super.setListener();
        //needResult=true 取现页短按选择默认卡回上一页面显示出来
        llDefaultBankCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (needResult) {
                    Intent data = new Intent(mBaseContext, CashTypeActivity.class);
                    if (defalutBankCardNo.length() >= 4) {
                        defalutBankCardNo = defalutBankCardNo.substring(defalutBankCardNo.length() - 4, defalutBankCardNo.length());
                    }
                    String bankName = defalutBankName + "(尾号" + defalutBankCardNo + ")";
                    data.putExtra("bankName", bankName);
                    data.putExtra("defalutBankIconUrl", defalutBankIconUrl);
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });
        //短按 needResult=true 取现页选择其他卡回上一页面显示出来
        lvBankCard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {
                if (bankListAdapter == null) {
                    return;
                }
                if (position < 0 || position > bankListAdapter.getCount() + 1) {
                    return;
                }
                if (bankListAdapter.getItem(position) == null) {
                    return;
                }
                if (needResult) {
                    showBankNoStyle(position);
                    finish();
                } else {
                    showManageCardDialog(dialogUtil, position);
                }

            }
        });
        //长按设置
        lvBankCard.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long longId) {
                if (bankListAdapter == null) {
                    return true;
                }
                if (position < 0 || position > bankListAdapter.getCount() + 1) {
                    return true;
                }
                if (bankListAdapter.getItem(position) == null) {
                    return true;
                }
                showManageCardDialog(dialogUtil, position);
                return true;
            }
        });
    }

    /**
     * 显示管理银行卡的Dialog
     *
     * @param dialogUtil
     * @param position
     */
    public void showManageCardDialog(CustomDialogUtil dialogUtil, final int position) {

        final String id = String.valueOf(bankListAdapter.getItem(position).id);
        dialogUtil.getDialogMode3("解绑银行卡", "设置为默认代扣卡", "取消", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //删除银行卡
                showDeleteBankCardDialog(bankListAdapter, id, position);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //设置默认代扣卡
                showsetDefaultCardDialog(id);
            }
        }, null).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshBank(AddCardEvent event) {
        setData();
        LoginHelper.getInstance().setBankIconUrl(defalutBankIconUrl);
    }

    /**
     * 传入点击项，返回4位尾数的银行卡号给上一页面
     *
     * @param position
     */
    private void showBankNoStyle(int position) {
        Intent data = new Intent(mBaseContext, CashTypeActivity.class);
        BankCardListResponse.BankInfoListBean bankCardResponse = bankListAdapter.getItem(position);
        String bankNo = bankCardResponse.bankNo;
        if (bankNo.length() >= 4) {
            bankNo = bankNo.substring(bankNo.length() - 4, bankNo.length());
        }
        String bankName = bankCardResponse.bankName + "(尾号" + bankNo + ")";
        data.putExtra("bankName", bankName);
        data.putExtra("defalutBankIconUrl", bankCardResponse.bankIconUrl);
        setResult(RESULT_OK, data);
    }

    /**
     * 设置默认代扣卡
     *
     * @param id
     */
    private void setDefaultCard(final String id) {
        final PwdDialog pwdDialog = new PwdDialog(mBaseContext, PwdDialog.statusType.BANKCARD);
        pwdDialog.showDialog();
        //验证成功的监听，拿到返回的code
        pwdDialog.setOnCheckPwdListener(new PwdDialog.OnCheckPwdListener() {
            @Override
            public void checkPwd(String payPwd) {
                ApiImpl.verifyPayPwd(mBaseContext, LoginHelper.getInstance().getIdPerson(), payPwd, new BaseRequestAgent.ResponseListener<PayPwdResponse>() {
                            @Override
                            public void onSuccess(PayPwdResponse response) {
                                if (response.data != null) {
                                    PayPwdResponse pwdResponse = response.data;
                                    if (pwdResponse.status) {
                                        //交易密码校验成功
                                        ApiImpl.setDefaultCard(mBaseContext, pwdResponse.code, id, LoginHelper.getInstance().getIdPerson(), new BaseRequestAgent.ResponseListener<BaseBean>() {
                                            @Override
                                            public void onSuccess(BaseBean response) {
                                                //设置默认代扣卡之后刷新列表
                                                setData();
                                                ToastUtils.showShortToast("设置默认代扣卡成功！");
                                                pwdDialog.dissmissDialog();
                                            }

                                            @Override
                                            public void onError(BaseBean errorBean) {
                                                CommonLoadingView.showErrorToast(errorBean);
                                            }
                                        });
                                    } else {
                                        // 交易密码校验成功,remainTimes: 1-3 重试密码 0 冻结密码需要找回密码
                                        PwdErrorDialog errorDialog = new PwdErrorDialog();
                                        errorDialog.showDialog(mBaseContext, pwdResponse.remainTimes);
                                    }

                                    CommonUtils.closeSoftKeyBoard(mBaseContext);
                                }
                                //更新用户信息
                                BaseApplication.getInstance().fetchUserInfo();
                            }

                            @Override
                            public void onError(BaseBean errorBean) {
                                CommonLoadingView.showErrorToast(errorBean);
                            }
                        }
                );
            }
        });

    }

    /**
     * 显示设置默认卡的Dialog
     *
     * @param id
     */
    public void showsetDefaultCardDialog(final String id) {
        CustomDialogUtil customDialogUtil = new CustomDialogUtil(mBaseContext);
        customDialogUtil.getDialogMode1("提示", "是否替换默认代扣卡？", "确定", "取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置默认卡
                setDefaultCard(id);
            }
        }, null).show();
    }

    /**
     * 显示删除银行卡的Dialog
     *
     * @param bankListAdapter
     * @param id
     * @param position
     */
    public void showDeleteBankCardDialog(final LvCommonAdapter<BankCardListResponse.BankInfoListBean> bankListAdapter, final String id, final int position) {
        CustomDialogUtil customDialogUtil = new CustomDialogUtil(mBaseContext);
        customDialogUtil.getDialogMode1("提示", "是否要删除该银行卡？", "确定", "取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除
                deleteBankCard(id, position, bankListAdapter);
            }
        }, null).show();
    }

    /**
     * 发送请求删除银行卡，成功刷新列表
     *
     * @param id
     * @param position
     */
    public void deleteBankCard(String id, final int position, final LvCommonAdapter<BankCardListResponse.BankInfoListBean> bankListAdapter) {
        ApiImpl.deleteBankInfo(mBaseContext, id, LoginHelper.getInstance().getIdPerson(), new BaseRequestAgent.ResponseListener<BaseBean>() {
            @Override
            public void onSuccess(BaseBean response) {
                if (bankListAdapter != null && bankListAdapter.getCount() > 0) {
                    bankListAdapter.getData().remove(position);
                    bankListAdapter.notifyDataSetChanged();
                    ToastUtils.showShortToast("删除成功！");
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }


    @Override
    public void setData() {
        ApiImpl.getBankCardInfo(mBaseContext, LoginHelper.getInstance().getIdPerson(), new BaseRequestAgent.ResponseListener<BankCardListResponse>() {
            @Override
            public void onSuccess(BankCardListResponse response) {
                bankInfoList = response.data.bankInfoList;
                if (CommonUtils.isNotNullOrEmpty(bankInfoList)) {
                    llMyCard.setVisibility(View.VISIBLE);
                    //控制初始化默认银行卡界面
                    boolean hasDefaultBank = false;
                    for (int i = 0; i < bankInfoList.size(); i++) {
                        BankCardListResponse.BankInfoListBean item = bankInfoList.get(i);
                        if (1 == item.isDefault) {
                            //默认代扣卡设置
                            defalutBankName = item.bankName;
                            defalutBankCardNo = item.bankNo;
                            defalutBankIconUrl = item.bankIconUrl;

                            bankInfoList.remove(i);
                            hasDefaultBank = true;
                            break;
                        }
                    }
                    if (hasDefaultBank) {
                        llDefaultBankCard.setVisibility(View.VISIBLE);
                        tvDefalutBankName.setText(StringUtils.nullToEmptyString(defalutBankName));
                        tvDefalutBankCardNo.setText(StringUtils.nullToEmptyString(changeBankNoStyle(defalutBankCardNo)));
                        ImageUtils.loadImage(defalutBankIconUrl, R.color.transparent, ivBank);
                    } else {
                        llDefaultBankCard.setVisibility(View.GONE);
                    }
                    //设置其他银行卡列表数据
                    bankListAdapter.setData(bankInfoList);
                    bankListAdapter.notifyDataSetChanged();
                } else {
                    llMyCard.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });

    }


    /**
     * 转换银行卡号的显示方式
     *
     * @return
     */
    public String changeBankNoStyle(String bankNo) {
        if (StringUtils.isNotNull(bankNo)) {
            if (bankNo.length() >= 4) {
                bankNo = bankNo.substring(bankNo.length() - 4, bankNo.length());
            }
        }
        return bankNo;
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 2 && resultCode == RESULT_OK) {
//            setData();
//        }
//    }

}
