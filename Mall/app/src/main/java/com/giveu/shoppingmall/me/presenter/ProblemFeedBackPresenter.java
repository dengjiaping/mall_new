package com.giveu.shoppingmall.me.presenter;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.me.view.agent.IProblemFeedBackView;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.utils.FileUtils;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 513419 on 2017/7/17.
 */

public class ProblemFeedBackPresenter extends BasePresenter<IProblemFeedBackView> {

    public ProblemFeedBackPresenter(IProblemFeedBackView view) {
        super(view);
    }

    public void addQuestionMessage(String files, ArrayList<String> photoList, int type, String content, String ident, String name,
                                   String nickname, String phone, String userId) {
        ApiImpl.addQuestionMessage(files, photoList, type, content, ident, name, nickname, phone, userId, new BaseRequestAgent.ResponseListener<BaseBean>() {
            @Override
            public void onSuccess(BaseBean response) {
            }

            @Override
            public void onError(BaseBean errorBean) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FileUtils.deleteAllFile(FileUtils.getDirFile(FileUtils.TEMP_IMAGE));
                    }
                }).start();
                String originalStr = errorBean.originResultString;
                try {
                    JSONObject jsonObject = new JSONObject(originalStr);
                    if ("success".equals(jsonObject.getString("status"))) {
                        if (getView() != null) {
                            getView().uploadSuccess();
                        }
                    } else {
                        CommonLoadingView.showErrorToast(errorBean);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (getView() != null) {
                        getView().hideLoding();
                    }
                }

            }
        });
    }
}
