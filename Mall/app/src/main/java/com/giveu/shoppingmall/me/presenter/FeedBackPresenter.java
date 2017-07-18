package com.giveu.shoppingmall.me.presenter;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.me.view.agent.IFeedBackView;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.FeedBackResponse;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 513419 on 2017/7/17.
 */

public class FeedBackPresenter extends BasePresenter<IFeedBackView> {

    public FeedBackPresenter(IFeedBackView view) {
        super(view);
    }

    public void getFeedBackRecord(String source, final String status, int pageNum, String userId) {
        ApiImpl.queryQuestionInfo(getView().getAct(), source, status, pageNum, userId, new BaseRequestAgent.ResponseListener<FeedBackResponse>() {
            @Override
            public void onSuccess(FeedBackResponse response) {

            }

            @Override
            public void onError(BaseBean errorBean) {
                String originalStr = errorBean.originResultString;
                try {
                    JSONObject jsonObject = new JSONObject(originalStr);
                    if ("success".equals(jsonObject.getString("status"))) {
                        FeedBackResponse response = new Gson().fromJson(originalStr, FeedBackResponse.class);
                        if (getView() != null) {
                            getView().feedBackListSuccess(response.data);
                        }
                    } else {
                        CommonLoadingView.showErrorToast(errorBean);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
