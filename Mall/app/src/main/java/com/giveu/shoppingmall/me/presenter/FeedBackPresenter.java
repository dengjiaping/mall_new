package com.giveu.shoppingmall.me.presenter;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.me.view.agent.IFeedBackView;
import com.giveu.shoppingmall.model.ApiImpl;

/**
 * Created by 513419 on 2017/7/17.
 */

public class FeedBackPresenter extends BasePresenter<IFeedBackView> {

    public FeedBackPresenter(IFeedBackView view) {
        super(view);
    }

    public void getFeedBackRecord(String ident, String name, String status, int pageNum,String userId){
        ApiImpl.queryQuestionInfo(getView().getAct(), ident, name, status, pageNum, userId, new BaseRequestAgent.ResponseListener<BaseBean>() {
            @Override
            public void onSuccess(BaseBean response) {

            }

            @Override
            public void onError(BaseBean errorBean) {

            }
        });
    }
}
