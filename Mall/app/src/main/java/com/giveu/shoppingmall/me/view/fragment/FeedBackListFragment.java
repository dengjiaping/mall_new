package com.giveu.shoppingmall.me.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseFragment;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.me.presenter.FeedBackPresenter;
import com.giveu.shoppingmall.me.view.agent.IFeedBackView;
import com.giveu.shoppingmall.model.bean.response.FeedBackResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.ImageUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshBase;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by 508632 on 2016/12/29.
 */

public class FeedBackListFragment extends BaseFragment implements IFeedBackView {
    private int status = 0;//0为未处理，1为已处理
    int pageNum = 1;//分页页码
    private final int pageSize = 10;
    View fragmentView;
    @BindView(R.id.ptrlv)
    PullToRefreshListView ptrlv;
    LvCommonAdapter<FeedBackResponse> feedbackListAdapter = null;
    @BindView(R.id.ll_feedbacklist)
    LinearLayout llFeedbacklist;
    private FeedBackPresenter presenter;


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            status = arguments.getInt("status");
        }

        baseLayout.setTitleBarAndStatusBar(false, false);
        fragmentView = View.inflate(mBaseContext, R.layout.fragment_feedback_list, null);
        ButterKnife.bind(this, fragmentView);
        presenter = new FeedBackPresenter(this);
        feedbackListAdapter = new LvCommonAdapter<FeedBackResponse>(mBaseContext, R.layout.activity_feedback_list_item, new ArrayList<FeedBackResponse>()) {
            @Override
            protected void convert(ViewHolder holder, FeedBackResponse item, int position) {
                GridView gv_item = holder.getView(R.id.gv_img);
                TextView tv_content_yes = holder.getView(R.id.tv_content_yes);
                TextView tv_time_yes = holder.getView(R.id.tv_time_yes);
                ImageView iv_apply_right = holder.getView(R.id.iv_apply_right);
                ImageView iv_apply_down = holder.getView(R.id.iv_apply_down);
                holder.setText(R.id.tv_content_no, item.content);
                holder.setText(R.id.tv_date_no, StringUtils.formatDate(item.createdate));
                if (!item.fold) {
                    tv_content_yes.setVisibility(View.GONE);
                    tv_time_yes.setVisibility(View.GONE);
                    iv_apply_right.setVisibility(View.VISIBLE);
                    iv_apply_down.setVisibility(View.GONE);
                } else {
                    tv_content_yes.setVisibility(View.VISIBLE);
                    tv_time_yes.setVisibility(View.VISIBLE);
                    iv_apply_right.setVisibility(View.GONE);
                    iv_apply_down.setVisibility(View.VISIBLE);
                }
                //没有回复内容则隐藏处理结果
                if (StringUtils.isNull(item.replyContent)) {
                    holder.setVisible(R.id.iv_line, false);
                    holder.setVisible(R.id.ll_yes, false);
                } else {
                    holder.setVisible(R.id.iv_line, true);
                    holder.setVisible(R.id.ll_yes, true);
                    tv_content_yes.setText(item.replyContent);
                    tv_time_yes.setText(StringUtils.formatDate(item.replayDate));
                }

                if (CommonUtils.isNotNullOrEmpty(item.questionImageList)) {
                    LvCommonAdapter<FeedBackResponse.QuestionImageListBean> gridViewAdapter = new LvCommonAdapter<FeedBackResponse.QuestionImageListBean>(mBaseContext, R.layout.img_feedback_item, item.questionImageList) {
                        @Override
                        protected void convert(ViewHolder holder, FeedBackResponse.QuestionImageListBean item, int position) {
                            ImageView ivPic = holder.getView(R.id.iv_img);
                            ImageUtils.loadImage(item.smallImage, R.drawable.defalut_img_88_88, ivPic);
                        }
                    };
                    gv_item.setAdapter(gridViewAdapter);
                    gv_item.setVisibility(View.VISIBLE);
                } else {
                    gv_item.setVisibility(View.GONE);
                }
            }
        };
        ptrlv.setAdapter(feedbackListAdapter);
        ptrlv.setMode(PullToRefreshBase.Mode.BOTH);
        ptrlv.setPullRefreshEnable(true);
        ptrlv.setPullLoadEnable(false);
        return fragmentView;
    }

    @Override
    protected BasePresenter[] initPresenters() {
        return new BasePresenter[]{presenter};
    }

    @Override
    protected void setListener() {
        ptrlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!feedbackListAdapter.getItem(i - 1).fold) {
                    feedbackListAdapter.getItem(i - 1).fold = true;
                } else {
                    feedbackListAdapter.getItem(i - 1).fold = false;
                }
                feedbackListAdapter.notifyDataSetChanged();
            }
        });
        ptrlv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageNum = 1;
                initDataDelay();
                ptrlv.setPullLoadEnable(false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                initDataDelay();
                ptrlv.setPullRefreshEnable(false);
            }
        });
    }


    @Override
    public void initDataDelay() {
        presenter.getFeedBackRecord("w", status + "", pageNum, LoginHelper.getInstance().getUserId());
    }


    @Override
    public void feedBackListSuccess(List<FeedBackResponse> response) {

        //显示查询结果
        if (pageNum == 1) {
            ptrlv.onRefreshComplete();
        }
        ptrlv.setPullRefreshEnable(true);
        if (CommonUtils.isNotNullOrEmpty(response)) {
            if (pageNum == 1) {
                feedbackListAdapter.getData().clear();
                if (response.size() >= pageSize) {
                    ptrlv.setPullLoadEnable(true);
                } else {
                    ptrlv.setPullLoadEnable(false);
                    ptrlv.showEnd("没有更多数据");
                }
                baseLayout.showEmpty("暂无反馈记录");
            }
            baseLayout.hideEmpty();
            feedbackListAdapter.getData().addAll(response);
            feedbackListAdapter.notifyDataSetChanged();
            pageNum++;
        } else {
            if (pageNum == 1) {
                feedbackListAdapter.getData().clear();
                feedbackListAdapter.notifyDataSetChanged();
                baseLayout.showEmpty("暂无反馈记录");
                ptrlv.setPullLoadEnable(false);
            } else {
                ptrlv.setPullLoadEnable(false);
                ptrlv.showEnd("没有更多数据");
            }
        }
    }
}
