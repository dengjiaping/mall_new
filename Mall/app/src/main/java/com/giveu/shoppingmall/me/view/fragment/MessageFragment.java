package com.giveu.shoppingmall.me.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseFragment;
import com.giveu.shoppingmall.me.adapter.MessageAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 101912 on 2017/10/13.
 */

public class MessageFragment extends BaseFragment {

    @BindView(R.id.lv_message)
    ListView listView;

    private MessageAdapter adapter;
    private ArrayList<String> messageList;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        baseLayout.setTitleBarAndStatusBar(false, false);
        View view = inflater.inflate(R.layout.fragment_message, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void initDataDelay() {
        messageList = new ArrayList<>();
        messageList.add("lin");
        messageList.add("lin");
        messageList.add("lin");
        messageList.add("lin");
        messageList.add("lin");
        messageList.add("lin");
        adapter = new MessageAdapter(mBaseContext, messageList);
        listView.setAdapter(adapter);
    }
}
