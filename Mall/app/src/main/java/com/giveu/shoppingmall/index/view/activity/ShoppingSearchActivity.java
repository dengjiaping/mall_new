package com.giveu.shoppingmall.index.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.rvadapter.RvCommonAdapter;
import com.giveu.shoppingmall.base.rvadapter.ViewHolder;
import com.giveu.shoppingmall.event.ClearEvent;
import com.giveu.shoppingmall.event.SearchEvent;
import com.giveu.shoppingmall.index.view.fragment.ShoppingListFragment;
import com.giveu.shoppingmall.index.view.fragment.TitleBarFragment;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.sharePref.SharePrefUtil;
import com.giveu.shoppingmall.widget.NoScrollListView;
import com.giveu.shoppingmall.widget.flowlayout.FlowLayout;
import com.giveu.shoppingmall.widget.flowlayout.TagAdapter;
import com.giveu.shoppingmall.widget.flowlayout.TagFlowLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 524202 on 2017/9/4.
 */

public class ShoppingSearchActivity extends BaseActivity {

    private List<String> labels;
    @BindView(R.id.shopping_search_flowlayout)
    TagFlowLayout mFlowLayout;
    @BindView(R.id.shopping_search_refresh)
    TextView mRefreshView;
    @BindView(R.id.shopping_search_content)
    FrameLayout contentLayout;
    @BindView(R.id.shopping_search_history_list)
    NoScrollListView historyListView;
    @BindView(R.id.search_history_ll)
    LinearLayout historyLayout;

    private BaseAdapter historyAdapter;
    private TagAdapter<String> mTagAdapter;
    private TitleBarFragment titleFragment;
    private ShoppingListFragment contentFragment;
    private FragmentTransaction fragmentTransaction;

    private Queue<String> historyQueue = new ArrayDeque<>(4);
    private String[] historyArray;
    private String searchHistory;
    private List<String> historyList = new ArrayList<>();

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_shopping_search_layout);

        baseLayout.setTitleBarAndStatusBar(false, true);
        baseLayout.setTopBarBackgroundColor(R.color.white);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putBoolean("showCenterEdit", true);
        bundle.putBoolean("showRightText", true);
        titleFragment = TitleBarFragment.newInstance(bundle);
        fragmentTransaction.replace(R.id.shopping_search_title, titleFragment).commit();

        contentFragment = ShoppingListFragment.newInstance();
        fragmentTransaction.add(R.id.shopping_search_content, contentFragment);

        labels = new ArrayList<>();
        mFlowLayout.setAdapter(mTagAdapter = new TagAdapter<String>(labels) {
            @Override
            public View getView(FlowLayout parent, int position, final String s) {
                TextView tvTag = (TextView) LayoutInflater.from(mBaseContext).inflate(R.layout.search_label_tv, parent, false);
                tvTag.setText(s);
                tvTag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        titleFragment.setSearchText(s);
                    }
                });
                return tvTag;
            }
        });

//        initSearchHistory();

        refreshHotWorlds();
        registerEventBus();
    }

    private void initSearchHistory() {
        searchHistory = SharePrefUtil.getSearchHistory();
        if (StringUtils.isNotNull(searchHistory)) {
            historyArray = searchHistory.split("#\\*#");
            for (String s : historyArray) {
                historyQueue.add(s);
            }
            historyList.addAll(historyQueue);
            historyListView.setAdapter(historyAdapter = new LvCommonAdapter<String>(this, R.layout.adapter_search_history_item, historyList) {
                @Override
                protected void convert(com.giveu.shoppingmall.base.lvadapter.ViewHolder viewHolder, String item, int position) {
                    viewHolder.setText(R.id.search_item_text, item);
                }
            });

            historyLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setData() {

    }

    public static void startIt(Context context) {
        Intent intent = new Intent(context, ShoppingSearchActivity.class);
        context.startActivity(intent);
    }

    @OnClick({R.id.shopping_search_refresh})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.shopping_search_refresh:
                refreshHotWorlds();
                break;
            default:
                break;
        }
    }

    private void refreshHotWorlds() {
        ApiImpl.refreshHotWords(mBaseContext, new BaseRequestAgent.ResponseListener<BaseBean>() {
            @Override
            public void onSuccess(BaseBean response) {
                try {
                    JSONObject json = new JSONObject(response.originResultString);
                    List<String> list = new Gson().fromJson(json.getString("data"), new TypeToken<List<String>>() {
                    }.getType());
                    labels.clear();
                    labels.addAll(list);
                    mTagAdapter.notifyDataChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(BaseBean errorBean) {
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void startSearch(SearchEvent event) {
        fragmentTransaction.show(contentFragment);
        contentFragment.initDataForFragment();
        contentLayout.setVisibility(View.VISIBLE);

//        historyQueue.add(event.getKeyword());
//        historyList.clear();
//        historyList.addAll(historyQueue);
//        historyAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClear(ClearEvent event) {
        fragmentTransaction.hide(contentFragment);
        contentLayout.setVisibility(View.GONE);
    }

}
