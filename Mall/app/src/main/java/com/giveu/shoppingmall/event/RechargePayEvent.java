package com.giveu.shoppingmall.event;

/**
 * Created by 513419 on 2017/7/14.
 */

public class RechargePayEvent {
   public boolean paySuccess;

    public RechargePayEvent(boolean paySuccess) {
        this.paySuccess = paySuccess;
    }
}
