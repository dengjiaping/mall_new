package com.giveu.shoppingmall.me.presenter;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.me.view.agent.IProblemFeedBackView;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

import java.util.ArrayList;

/**
 * Created by 513419 on 2017/7/17.
 */

public class ProblemFeedBackPresenter extends BasePresenter<IProblemFeedBackView> {

    public ProblemFeedBackPresenter(IProblemFeedBackView view) {
        super(view);
    }

    public void addQuestionMessage(String files, ArrayList<String> photoList, String type, String content, String ident, String name,
                                   String nickname, String phone, String userId) {
        ApiImpl.addQuestionMessage(files, photoList, type, content, ident, name, nickname, phone, userId, new BaseRequestAgent.ResponseListener<BaseBean>() {
            @Override
            public void onSuccess(BaseBean response) {
                if (getView() != null) {
                    getView().uploadSuccess();
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }
}
