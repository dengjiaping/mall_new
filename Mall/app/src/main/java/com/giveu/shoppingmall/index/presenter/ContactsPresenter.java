package com.giveu.shoppingmall.index.presenter;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.index.view.agent.IContactsView;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.ContactsResponse;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

/**
 * Created by 101900 on 2017/8/10.
 */

public class ContactsPresenter extends BasePresenter<IContactsView> {
    public ContactsPresenter(IContactsView view) {
        super(view);
    }

    public void commit(String name, String contactsCode,String idPerson,String phone) {
        ApiImpl.addOtherContact(getView().getAct(), name, contactsCode, idPerson, phone, new BaseRequestAgent.ResponseListener<BaseBean>() {
            @Override
            public void onSuccess(BaseBean response) {// 0 未添加 1添加
                if (getView() != null) {
                    getView().commitContactsSuccess();
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }

    public void showContactsList() {
        ApiImpl.getContactTypeInfo(getView().getAct(), new BaseRequestAgent.ResponseListener<ContactsResponse>() {
            @Override
            public void onSuccess(ContactsResponse response) {
                if (getView() != null) {
                    getView().showContactsList(response.data);
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }
}
