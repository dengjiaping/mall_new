package com.giveu.shoppingmall.me.presenter;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.me.view.agent.IBillIistView;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.BillBean;
import com.giveu.shoppingmall.model.bean.response.BillListResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

import java.util.ArrayList;

/**
 * Created by 513419 on 2017/7/3.
 */

public class BillListPresenter extends BasePresenter<IBillIistView> {
    public BillListPresenter(IBillIistView view) {
        super(view);
    }

    public void getBillList(String idPerson) {
        ApiImpl.getBillList(getView().getAct(), idPerson, new BaseRequestAgent.ResponseListener<BillListResponse>() {
                    @Override
                    public void onSuccess(BillListResponse response) {
                        if (getView() != null) {
                            ArrayList<BillBean> currentMonthList = new ArrayList<>();//当期账单列表
                            ArrayList<BillBean> nextMonthList = new ArrayList<>();//下期账单列表
                            //先处理分期产品再处理零花钱最后是红包
                            if (response.data.product != null && CommonUtils.isNotNullOrEmpty(response.data.product.billList)) {
                                for (BillListResponse.BillListBean billListBean : response.data.product.billList) {
                                    //listview的头部bean
                                    BillBean titileBean = new BillBean();
                                    titileBean.isTitle = true;
                                    titileBean.productType = "o";//产品类型为分期产品
                                    titileBean.isOverdue = billListBean.isOverduce;
                                    titileBean.repayAmount = billListBean.repayAmount;
                                    titileBean.isWithholding = billListBean.isWithholding;
                                    if(billListBean.isCurrent) {
                                        currentMonthList.add(titileBean);//当期应还添加标题bean
                                    }else {
                                        nextMonthList.add(titileBean);//下期应还添加标题bean
                                    }
                                    for (BillListResponse.ContractListBean contractListBean : billListBean.contractList) {
                                        BillBean contentBean = new BillBean();
                                        contentBean.isTitle = false;
                                        contentBean.contractId = contractListBean.contractId;
                                        contentBean.contractNo = contractListBean.contractNo;
                                        contentBean.creditType = contractListBean.creditType;
                                        contentBean.isWithholding = contractListBean.isWithholding;
                                        contentBean.isOverdue = contractListBean.isOverduce;
                                        contentBean.numInstalment = contractListBean.numInstalment;
                                        contentBean.repayAmount = contractListBean.repayAmount;
                                        contentBean.productType = contractListBean.productType;
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
                                BillBean titileBean = new BillBean();
                                titileBean.isTitle = true;
                                titileBean.productType = "c";//产品类型为取现随借随还
                                titileBean.isOverdue = response.data.product.cycleMoney.isOverduce;
                                titileBean.repayAmount = response.data.product.cycleMoney.repayAmount;
                                titileBean.creditType = response.data.product.cycleMoney.productType;
                                titileBean.isWithholding = response.data.product.cycleMoney.isWithholding;
                                currentMonthList.add(titileBean);//添加标题bean
                                for (BillListResponse.ContractListBean contractListBean : response.data.product.cycleMoney.contractList) {
                                    BillBean contentBean = new BillBean();
                                    contentBean.isTitle = false;
                                    contentBean.contractId = contractListBean.contractId;
                                    contentBean.contractNo = contractListBean.contractNo;
                                    contentBean.creditType = contractListBean.creditType;
                                    contentBean.isWithholding = contractListBean.isWithholding;
                                    contentBean.isOverdue = contractListBean.isOverduce;
                                    contentBean.productType = contractListBean.productType;
                                    contentBean.repayAmount = contractListBean.repayAmount;
                                    contentBean.numInstalment = contractListBean.numInstalment;
                                    contentBean.repayDate = contractListBean.repayDate;
                                    currentMonthList.add(contentBean);//添加当期标题下的bean,零花钱没有下期还的概念
                                }
                            }

                            //红包列表
                            if (response.data.product != null && CommonUtils.isNotNullOrEmpty(response.data.redPacketList)) {
                                BillBean titileBean = new BillBean();
                                titileBean.isTitle = true;
                                titileBean.isWithholding = response.data.product.cycleMoney.isWithholding;
                                currentMonthList.add(titileBean);//添加标题bean
                                for (BillListResponse.RedPacketListBean redPacketListBean : response.data.redPacketList) {
                                    BillBean redPacketBean = new BillBean();
                                    redPacketBean.type = redPacketListBean.type;
                                    redPacketBean.id = redPacketListBean.id;
                                    redPacketBean.price = redPacketListBean.price;
                                }
                            }
                            getView().showBillList(response.data.important, currentMonthList, nextMonthList);
                        }
                    }

                    @Override
                    public void onError(BaseBean errorBean) {
                        CommonLoadingView.showErrorToast(errorBean);
                    }
                }

        );
    }
}
