package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

import java.util.List;

/**
 * Created by 513419 on 2017/7/3.
 */

public class BillListResponse extends BaseBean<BillListResponse> {

    public HeaderBean important;//头部数据
    public ProductBean product;
    public List<RedPacketListBean> redPacketList;//红包列表

    public static class HeaderBean {
        public double cycleTotalAmount;//零花钱总欠款
        public String endDate;
        public boolean isOverduce;
        public String maxRepayAmount;//还款金额上限
        public String othersTotalAmount;//分期产品总欠款(非当期与下期和)
        public String repayAmount;//该期应还
        public String repayDate;//账单还款日
    }


    public static class ProductBean {
        public CycleMoneyBean cycleMoney;//取现随借随还
        public List<BillListBean> billList;//分期产品列表
    }

    public static class BillListBean {
        public String repayBillDate;//合同还款日
        public boolean isCurrent;//	是否当期
        public boolean isOverduce;//是否逾期
        public boolean isWithholding;//是否代扣中
        public String repayAmount;//当前应还金额
        public List<ContractListBean> contractList;

    }


    public static class CycleMoneyBean {
        public boolean isOverduce;//是否逾期
        public boolean isWithholding;//是否代扣中
        public String maxRepayAmount;//还款金额上限
        public String productType;//产品类别
        public String repayAmount;//所有当期应还的零花钱的总金额
        public List<ContractListBean> contractList;//合同列表
    }

    public static class RedPacketListBean {
        public String id;//红包ID
        public String price;//红包金额
        public String type;//	红包类别
    }

    public static class ContractListBean {
        public String appDate;//合同申请时间
        public String creditType;//合同类型
        public boolean isOverduce;//是否逾期
        public String loanDate;//分期日期
        public String productType;//产品类别
        public String repayDate;//还款日期
        public String contractId;//合同id
        public String contractNo;//合同号
        public boolean isCurrent;//	是否当期
        public boolean isWithholding;//是否代扣中
        public String numInstalment;//期次
        public String paymentNum;//期数
        public String repayAmount;//当前应还金额
    }
}
