package com.giveu.shoppingmall.me.view.activity;

import android.os.Bundle;
import android.view.View;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.me.adapter.CollectionAdapter;
import com.giveu.shoppingmall.model.bean.response.CollectionResponse;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 我的收藏
 * Created by 101900 on 2017/9/4.
 */

public class CollectionActivity extends BaseActivity {
    @BindView(R.id.ptrlv)
    PullToRefreshListView ptrlv;
    CollectionAdapter collectionAdapter;
    List<CollectionResponse> list;
    boolean rightTextClick = true;//true 编辑 false 取消

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_collection);
        baseLayout.setTitle("我的收藏");
        baseLayout.setRightText("编辑");
        baseLayout.setRightTextListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightTextClick) {
                    baseLayout.setRightText("取消");
                } else {
                    baseLayout.setRightText("编辑");
                }

                for (CollectionResponse collectionResponse : collectionAdapter.getData()) {
                    collectionResponse.isShowCb = rightTextClick;
                }
                collectionAdapter.notifyDataSetChanged();
                rightTextClick = rightTextClick == true ? false : true;
            }
        });
        baseLayout.setRightTextColor(R.color.title_color);
    }


    @Override
    public void setData() {
        list = new ArrayList<>();
        CollectionResponse collectionResponse = new CollectionResponse(false);
        list.add(collectionResponse);
        list.add(collectionResponse);

        collectionAdapter = new CollectionAdapter(mBaseContext, list);
        ptrlv.setAdapter(collectionAdapter);
        ptrlv.setPullLoadEnable(false);
    }

}
