package com.giveu.shoppingmall.index.view.agent;

import com.giveu.shoppingmall.base.IView;
import com.giveu.shoppingmall.model.bean.response.ContactsResponse;

import java.util.List;

/**
 * Created by 101900 on 2017/8/10.
 */

public interface IContactsView extends IView {
    void commitContactsSuccess();
    void commitContactsFail();
    void showContactsList(List<ContactsResponse> data);
}
