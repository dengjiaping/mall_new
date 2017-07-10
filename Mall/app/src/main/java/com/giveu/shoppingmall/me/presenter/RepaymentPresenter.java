package com.giveu.shoppingmall.me.presenter;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.me.view.agent.IRepaymentView;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.RepaymentBean;
import com.giveu.shoppingmall.model.bean.response.BillListResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
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

    public void getBillList(String idPerson) {
        ApiImpl.getBillList(getView().getAct(), idPerson, new BaseRequestAgent.ResponseListener<BillListResponse>() {
                    @Override
                    public void onSuccess(BillListResponse response) {
                        if (getView() != null) {
                            ArrayList<RepaymentBean> currentMonthList = new ArrayList<>();//当期账单列表
                            ArrayList<RepaymentBean> nextMonthList = new ArrayList<>();//下期账单列表
                            //先处理分期产品再处理零花钱最后是红包
//                            wechatresponse.data.product.billList.addAll(wechatresponse.data.product.billList);
//                            wechatresponse.data.product.billList.addAll(wechatresponse.data.product.billList);
//                            wechatresponse.data.product.billList.addAll(wechatresponse.data.product.billList);
                            if (response.data.product != null && CommonUtils.isNotNullOrEmpty(response.data.product.billList)) {
                                for (BillListResponse.BillListBean billListBean : response.data.product.billList) {
                                    //listview的头部bean
                                    RepaymentBean titileBean = new RepaymentBean();
                                    titileBean.isTitle = true;
                                    titileBean.productType = TypeUtlis.CERDIT_PRODUCT;//产品类型为分期产品
                                    titileBean.isOverdue = billListBean.isOverduce;
                                    titileBean.repayAmount = billListBean.repayAmount;
                                    titileBean.isWithholding = billListBean.isWithholding;
                                    if(billListBean.isCurrent) {
                                        currentMonthList.add(titileBean);//当期应还添加标题bean
                                    }else {
                                        nextMonthList.add(titileBean);//下期应还添加标题bean
                                    }
                                    for (BillListResponse.ContractListBean contractListBean : billListBean.contractList) {
                                        RepaymentBean contentBean = new RepaymentBean();
                                        contentBean.isTitle = false;
                                        contentBean.contractId = contractListBean.contractId;
                                        contentBean.contractNo = contractListBean.contractNo;
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

/*                            //// TODO: 2017/7/6虚假数据
                            RepaymentBean titile = new RepaymentBean();
                            titile.isTitle = true;
                            titile.productType = TypeUtlis.CASH;//产品类型为取现随借随还
                            titile.isOverdue = wechatresponse.data.product.cycleMoney.isOverduce;
                            titile.repayAmount = wechatresponse.data.product.cycleMoney.repayAmount;
                            titile.creditType = wechatresponse.data.product.cycleMoney.productType;
                            titile.isWithholding = wechatresponse.data.product.cycleMoney.isWithholding;
                            currentMonthList.add(titile);//添加标题bean

                            BillListResponse.ContractListBean contract1ListBean = new BillListResponse.ContractListBean();
                            contract1ListBean.paymentNum = "20";


                            RepaymentBean content1Bean = new RepaymentBean();
                            content1Bean.isTitle = false;
                            content1Bean.contractId = contract1ListBean.contractId;
                            content1Bean.contractNo = contract1ListBean.contractNo;
                            content1Bean.creditType = contract1ListBean.creditType;
                            content1Bean.isWithholding = contract1ListBean.isWithholding;
                            content1Bean.isOverdue = contract1ListBean.isOverduce;
                            content1Bean.productType = contract1ListBean.productType;
                            content1Bean.repayAmount = contract1ListBean.repayAmount;
                            content1Bean.numInstalment = contract1ListBean.numInstalment;
                            content1Bean.repayDate = contract1ListBean.repayDate;
                            currentMonthList.add(content1Bean);

                            RepaymentBean content2Bean = new RepaymentBean();
                            content2Bean.isTitle = false;
                            content2Bean.contractId = contract1ListBean.contractId;
                            content2Bean.contractNo = contract1ListBean.contractNo;
                            content2Bean.creditType = contract1ListBean.creditType;
                            content2Bean.isWithholding = contract1ListBean.isWithholding;
                            content2Bean.isOverdue = contract1ListBean.isOverduce;
                            content2Bean.productType = contract1ListBean.productType;
                            content2Bean.repayAmount = contract1ListBean.repayAmount;
                            content2Bean.numInstalment = contract1ListBean.numInstalment;
                            content2Bean.repayDate = contract1ListBean.repayDate;
                            currentMonthList.add(content2Bean);*/

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
                                for (BillListResponse.ContractListBean contractListBean : response.data.product.cycleMoney.contractList) {
                                    RepaymentBean contentBean = new RepaymentBean();
                                    contentBean.isTitle = false;
                                    contentBean.paymentNum = contractListBean.paymentNum;
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
                                for (BillListResponse.RedPacketListBean redPacketListBean : response.data.redPacketList) {
                                    RepaymentBean redPacketBean = new RepaymentBean();
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
