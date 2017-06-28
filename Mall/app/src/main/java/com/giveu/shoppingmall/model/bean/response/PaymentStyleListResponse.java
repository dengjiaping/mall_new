package com.giveu.shoppingmall.model.bean.response;

import java.util.List;

/**
 * Created by 101900 on 2017/6/28.
 */

public class PaymentStyleListResponse {
   public List<ListBean> list;

    public static class ListBean {
        public String styleName;//支付类型
        public int Icon;
        public boolean isChecked = false;//是否选中

        public ListBean(String styleName, int icon, boolean isChecked) {
            this.styleName = styleName;
            Icon = icon;
            this.isChecked = isChecked;
        }
    }
}
