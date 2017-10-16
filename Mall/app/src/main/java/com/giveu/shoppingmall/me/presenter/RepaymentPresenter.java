package com.giveu.shoppingmall.me.presenter;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.me.view.agent.IRepaymentView;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.RepaymentBean;
import com.giveu.shoppingmall.model.bean.response.RepaymentResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.TypeUtlis;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

import java.util.ArrayList;

/**
 * Created by 513419 on 2017/7/3.
 */

public class RepaymentPresenter extends BasePresenter<IRepaymentView> {
    public RepaymentPresenter(IRepaymentView view) {
        super(view);
    }

    public void getRepayment(String idPerson) {
        if (!LoginHelper.getInstance().hasAverageUser()) {
            //不是假数据的激活用户，才去调还款数据
            ApiImpl.getRepayment(getView().getAct(), idPerson, new BaseRequestAgent.ResponseListener<RepaymentResponse>() {
                        @Override
                        public void onSuccess(RepaymentResponse response) {
                            if (getView() != null) {
                                ArrayList<RepaymentBean> currentMonthList = new ArrayList<>();//当期账单列表
                                ArrayList<RepaymentBean> nextMonthList = new ArrayList<>();//下期账单列表
                                if (response.data.important == null) {
                                    response.data.important = new RepaymentResponse.HeaderBean();
                                }
                                //先处理分期产品再处理零花钱最后是红包
                                if (response.data.product != null && CommonUtils.isNotNullOrEmpty(response.data.product.billList)) {
                                    for (RepaymentResponse.BillListBean billListBean : response.data.product.billList) {
                                        //listview的头部bean
                                        RepaymentBean titileBean = new RepaymentBean();
                                        titileBean.isTitle = true;
                                        titileBean.productType = TypeUtlis.CERDIT_PRODUCT;//产品类型为分期产品
                                        titileBean.isOverdue = billListBean.isOverduce;
                                        titileBean.repayAmount = billListBean.repayAmount;
                                        titileBean.isWithholding = billListBean.isWithholding;
                                        if (billListBean.isCurrent) {
                                            currentMonthList.add(titileBean);//当期应还添加标题bean
                                            response.data.important.repayDate = billListBean.repayBillDate;
                                        } else {
                                            nextMonthList.add(titileBean);//下期应还添加标题bean
                                            response.data.important.nextRepayAmount = billListBean.repayAmount;
                                            response.data.important.nextRepayDate = billListBean.repayBillDate;
                                        }
                                        for (RepaymentResponse.ContractListBean contractListBean : billListBean.contractList) {
                                            RepaymentBean contentBean = new RepaymentBean();
                                            contentBean.isTitle = false;
                                            contentBean.contractId = contractListBean.contractId;
                                            contentBean.contractNo = contractListBean.contractNo;
                                            contentBean.appDate = contractListBean.appDate;
                                            contentBean.creditType = contractListBean.creditType;
                                            contentBean.paymentNum = contractListBean.paymentNum;
                                            contentBean.isWithholding = contractListBean.isWithholding;
                                            contentBean.isOverdue = contractListBean.isOverduce;
                                            contentBean.numInstalment = contractListBean.numInstalment;
                                            contentBean.repayAmount = contractListBean.repayAmount;
                                            contentBean.productType = TypeUtlis.CERDIT_PRODUCT;
                                            contentBean.repayDate = contractListBean.repayDate;
                                            if (billListBean.isCurrent) {
                                                currentMonthList.add(contentBean);//添加当期标题下的bean
                                            } else {
                                                nextMonthList.add(contentBean);//添加当期标题下的bean
                                            }
                                        }
                                    }
                                }
                                //零花钱
                                if (response.data.product != null && response.data.product.cycleMoney != null && CommonUtils.isNotNullOrEmpty(response.data.product.cycleMoney.contractList)) {
                                    //listview的头部bean
                                    RepaymentBean titileBean = new RepaymentBean();
                                    titileBean.isTitle = true;
                                    titileBean.productType = TypeUtlis.CASH;//产品类型为取现随借随还
                                    titileBean.isOverdue = response.data.product.cycleMoney.isOverduce;
                                    titileBean.repayAmount = response.data.product.cycleMoney.repayAmount;
                                    titileBean.creditType = response.data.product.cycleMoney.productType;
                                    titileBean.isWithholding = response.data.product.cycleMoney.isWithholding;
                                    currentMonthList.add(titileBean);//添加标题bean
                                    for (RepaymentResponse.ContractListBean contractListBean : response.data.product.cycleMoney.contractList) {
                                        RepaymentBean contentBean = new RepaymentBean();
                                        contentBean.isTitle = false;
                                        contentBean.paymentNum = contractListBean.paymentNum;
                                        contentBean.appDate = contractListBean.loanDate;
                                        contentBean.contractId = contractListBean.contractId;
                                        contentBean.contractNo = contractListBean.contractNo;
                                        contentBean.creditType = contractListBean.creditType;
                                        contentBean.isWithholding = contractListBean.isWithholding;
                                        contentBean.isOverdue = contractListBean.isOverduce;
                                        contentBean.productType = TypeUtlis.CASH;
                                        contentBean.repayAmount = contractListBean.repayAmount;
                                        contentBean.numInstalment = contractListBean.numInstalment;
                                        contentBean.repayDate = contractListBean.repayDate;
                                        currentMonthList.add(contentBean);//添加当期标题下的bean,零花钱没有下期还的概念
                                    }
                                }

                                //红包列表
                                if (response.data.product != null && CommonUtils.isNotNullOrEmpty(response.data.redPacketList)) {
                                    RepaymentBean titileBean = new RepaymentBean();
                                    titileBean.isTitle = true;
                                    titileBean.isWithholding = response.data.product.cycleMoney.isWithholding;
                                    currentMonthList.add(titileBean);//添加标题bean
                                    for (RepaymentResponse.RedPacketListBean redPacketListBean : response.data.redPacketList) {
                                        RepaymentBean redPacketBean = new RepaymentBean();
                                        redPacketBean.type = redPacketListBean.type;
                                        redPacketBean.id = redPacketListBean.id;
                                        redPacketBean.price = redPacketListBean.price;
                                    }
                                }
                                getView().showRepayment(response.data.important, currentMonthList, nextMonthList);
                            }
                        }

                        @Override
                        public void onError(BaseBean errorBean) {
                            if (getView() != null) {
                                getView().showEmpty();
                                CommonLoadingView.showErrorToast(errorBean);
                            }
                        }
                    }
            );
        }

    }
}
