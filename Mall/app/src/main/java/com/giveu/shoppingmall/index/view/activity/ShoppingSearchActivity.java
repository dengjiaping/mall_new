package com.giveu.shoppingmall.index.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.event.ClearEvent;
import com.giveu.shoppingmall.event.SearchEvent;
import com.giveu.shoppingmall.index.view.fragment.ShoppingListFragment;
import com.giveu.shoppingmall.index.view.fragment.TitleBarFragment;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.utils.LimitQueue;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.sharePref.SharePrefUtil;
import com.giveu.shoppingmall.widget.NoScrollListView;
import com.giveu.shoppingmall.widget.flowlayout.FlowLayout;
import com.giveu.shoppingmall.widget.flowlayout.TagAdapter;
import com.giveu.shoppingmall.widget.flowlayout.TagFlowLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 524202 on 2017/9/4.
 */

public class ShoppingSearchActivity extends BaseActivity {

    //默认最多保存5条搜索记录
    public final static int MAX_SEARCH_SIZE = 5;
    //搜索历史记录合并到一条String中,已HISTORY_PATTERN分隔
    public final static String HISTORY_PATTERN = "___";

    private List<String> labels;
    @BindView(R.id.shopping_search_flowlayout)
    TagFlowLayout mFlowLayout;
    @BindView(R.id.shopping_search_refresh)
    TextView mRefreshView;
    @BindView(R.id.shopping_search_content)
    FrameLayout contentLayout;
    @BindView(R.id.shopping_search_history_list)
    NoScrollListView historyView;
    @BindView(R.id.shopping_search_history_clear)
    TextView clearView;
    @BindView(R.id.search_history_ll)
    LinearLayout historyLayout;

    private LvCommonAdapter historyAdapter;
    private TagAdapter<String> mTagAdapter;
    private TitleBarFragment titleFragment;
    private ShoppingListFragment contentFragment;
    private FragmentTransaction fragmentTransaction;

    private LimitQueue<String> historyQueue = new LimitQueue<>(MAX_SEARCH_SIZE);
    private String[] historyArray;
    private String searchHistory;

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
                        doSearch(s);
                    }
                });
                return tvTag;
            }
        });

        initSearchHistory();

        refreshHotWorlds();
        registerEventBus();
    }

    private void initSearchHistory() {
        searchHistory = SharePrefUtil.getSearchHistory();
        historyAdapter = new LvCommonAdapter<String>(this, R.layout.adapter_search_history_item, historyQueue.values()) {
            @Override
            protected void convert(com.giveu.shoppingmall.base.lvadapter.ViewHolder viewHolder, String item, int position) {
                final String text = historyQueue.get(historyQueue.getSize() - 1 - position);
                viewHolder.setText(R.id.search_item_text, text);
                viewHolder.setOnClickListener(R.id.search_item_layout, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doSearch(text);
                    }
                });
            }
        };
        historyView.setAdapter(historyAdapter);

        if (StringUtils.isNotNull(searchHistory)) {
            historyArray = searchHistory.split(HISTORY_PATTERN);
            for (String s : historyArray) {
                if (StringUtils.isNotNull(s)) {
                    historyQueue.offer(s);
                }
            }
            historyAdapter.notifyDataSetChanged();
            //暂时屏蔽搜索记录功能
//            historyLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setData() {

    }

    public static void startIt(Context context) {
        Intent intent = new Intent(context, ShoppingSearchActivity.class);
        context.startActivity(intent);
    }

    @OnClick({R.id.shopping_search_refresh, R.id.shopping_search_history_clear})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.shopping_search_refresh:
                refreshHotWorlds();
                break;
            case R.id.shopping_search_history_clear:
                historyQueue.empty();
                historyAdapter.notifyDataSetChanged();
                saveSearchHistory();
                historyLayout.setVisibility(View.GONE);
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

    public void doSearch(String keyword) {
        //隐藏软键盘
        hideSoftKeyboard();
        //显示搜索结果列表
        fragmentTransaction.show(contentFragment);
        contentFragment.setKeyword(keyword);
        contentFragment.initDataForFragment();
        contentLayout.setVisibility(View.VISIBLE);
        //刷新搜索历史列表
        historyQueue.offer(keyword);
        historyAdapter.notifyDataSetChanged();
        if (historyLayout.getVisibility() != View.VISIBLE) {
            //暂时屏蔽搜索记录功能
            //historyLayout.setVisibility(View.VISIBLE);
        }
        //保存搜索记录到本地
        saveSearchHistory();
    }

    /**
     * 点击搜索
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void startSearch(SearchEvent event) {
        doSearch(event.getKeyword());
    }

    /**
     * 搜索栏清除搜索内容
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClear(ClearEvent event) {
        fragmentTransaction.hide(contentFragment);
        contentLayout.setVisibility(View.GONE);
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 保存搜索记录
     */
    private void saveSearchHistory() {
        StringBuilder sb = new StringBuilder();
        for (String s : historyQueue.values()) {
            sb.append(s).append(HISTORY_PATTERN);
        }
        SharePrefUtil.saveSearchHistory(sb.toString());
    }

}
