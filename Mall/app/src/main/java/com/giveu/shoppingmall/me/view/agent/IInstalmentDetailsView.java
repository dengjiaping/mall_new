package com.giveu.shoppingmall.me.view.agent;

import com.giveu.shoppingmall.base.IView;
import com.giveu.shoppingmall.model.bean.response.InstalmentDetailResponse;

/**
 * Created by 513419 on 2017/7/3.
 */

public interface IInstalmentDetailsView extends IView {

    void showInstalmentDetails(InstalmentDetailResponse data, String creditType);
}
