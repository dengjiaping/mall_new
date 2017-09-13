package com.giveu.shoppingmall.index.presenter;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.index.view.agent.IShoppingView;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.GoodsSearchResponse;
import com.giveu.shoppingmall.model.bean.response.IndexResponse;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

/**
 * Created by 513419 on 2017/9/5.
 */

public class ShoppingPresenter extends BasePresenter<IShoppingView> {
    public ShoppingPresenter(IShoppingView view) {
        super(view);
    }

    public void getHeadContent() {
        ApiImpl.getShoppingIndex(null, new BaseRequestAgent.ResponseListener<IndexResponse>() {
            @Override
            public void onSuccess(IndexResponse response) {
                if (getView() != null) {
                    getView().getHeadContent(response.data);
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
                if (getView() != null) {
                    CommonLoadingView.showErrorToast(errorBean);
                    getView().getDataFail();
                }
            }
        });
        /*Gson gson = new Gson();
        //解析json
        final ShoppingResponse shoppingResponse = gson.fromJson(commoidty, ShoppingResponse.class);
        getView().getDataFail(shoppingResponse.data);*/
    }

    public void getIndexContent(String channel, String idPerson, int pageNumber, int pageSize, String code ) {
        ApiImpl.getGoodsSearch(null, channel, "13042734", "小米", "salesVolume", pageNumber, pageSize, 0, "", new BaseRequestAgent.ResponseListener<GoodsSearchResponse>() {
            @Override
            public void onSuccess(GoodsSearchResponse response) {
                if (getView() != null) {
                    getView().getIndexContent(response.data.resultList,response.srcIp);
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
                if (getView() != null) {
                    CommonLoadingView.showErrorToast(errorBean);
                    getView().getDataFail();
                }
            }
        });
        /*Gson gson = new Gson();
        //解析json
        final ArrayList<IndexResponse> contentList = gson.fromJson(content,
                new TypeToken<List<IndexResponse>>() {
                }.getType());
        getView().getIndexContent(contentList);
        final ShoppingResponse shoppingResponse = gson.fromJson(commoidty, ShoppingResponse.class);
        getView().getDataFail(shoppingResponse.data);*/
    }
}
