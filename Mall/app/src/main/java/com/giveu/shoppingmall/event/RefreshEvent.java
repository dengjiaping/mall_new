package com.giveu.shoppingmall.event;

/**
 * Created by 513419 on 2017/9/18.
 */

public class RefreshEvent {
    public int orderState;

    public RefreshEvent(int orderState) {
        this.orderState = orderState;
    }
}
