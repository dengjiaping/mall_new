package com.giveu.shoppingmall.index.view.agent;

import com.giveu.shoppingmall.base.IView;
import com.giveu.shoppingmall.model.bean.response.GoodsSearchResponse;
import com.giveu.shoppingmall.model.bean.response.IndexResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 513419 on 2017/9/5.
 */

public interface IShoppingView extends IView{
    void getIndexContent(List<GoodsSearchResponse.GoodsBean> contentList, String srcIp);
    void getDataFail();
    void getHeadContent(ArrayList<IndexResponse> data);
}
