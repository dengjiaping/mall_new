package com.giveu.shoppingmall.event;

/**
 * Created by 524202 on 2017/9/8.
 */

public class SearchEvent {
    private String keyword;
    public SearchEvent(String keyword){
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }
}
