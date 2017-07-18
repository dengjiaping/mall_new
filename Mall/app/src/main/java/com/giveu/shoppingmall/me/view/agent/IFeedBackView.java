package com.giveu.shoppingmall.me.view.agent;

import com.giveu.shoppingmall.base.IView;
import com.giveu.shoppingmall.model.bean.response.FeedBackResponse;

import java.util.List;

/**
 * Created by 513419 on 2017/7/17.
 */

public interface IFeedBackView extends IView {
    void feedBackListSuccess(List<FeedBackResponse> response);
}
