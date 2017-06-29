package com.giveu.shoppingmall.model.bean.response;

import java.util.List;

/**
 * 支付类型
 * Created by 101900 on 2017/6/28.
 */

public class PaymentTypeListResponse {
   public List<ListBean> list;

    public static class ListBean {
        public String typeName;//支付类型
        public int Icon;
        public boolean isChecked = false;//是否选中

        public ListBean(String typeName, int icon, boolean isChecked) {
            this.typeName = typeName;
            Icon = icon;
            this.isChecked = isChecked;
        }
    }
}
