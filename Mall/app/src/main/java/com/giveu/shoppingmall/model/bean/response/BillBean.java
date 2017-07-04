package com.giveu.shoppingmall.model.bean.response;

/**
 * Created by 513419 on 2017/6/22.
 */

public class BillBean {
    public boolean isTitle;
    public boolean isChoose;
    public int billType;//0是分期产品，1是零花钱随借随还，2是红包
    public String repayDate;//还款日期
    public String contractId;//合同id
    public String contractNo;//合同号
    public boolean isOverdue;//是否逾期
    public boolean isWithholding;//是否代扣中
    public String creditType;//合同类型
    public String productType;//产品类型
    public String repayAmount;//当前应还金额
    public String id;//红包ID
    public String price;//红包金额
    public String type;//红包类别
    public String numInstalment;//期次

    @Override
    public String toString() {
        return "BillBean{" +
                "isTitle=" + isTitle +
                ", isChoose=" + isChoose +
                ", billType=" + billType +
                ", repayDate='" + repayDate + '\'' +
                ", contractId='" + contractId + '\'' +
                ", contractNo='" + contractNo + '\'' +
                ", isWithholding=" + isWithholding +
                ", creditType='" + creditType + '\'' +
                ", repayAmount=" + repayAmount +
                ", id='" + id + '\'' +
                ", price='" + price + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
