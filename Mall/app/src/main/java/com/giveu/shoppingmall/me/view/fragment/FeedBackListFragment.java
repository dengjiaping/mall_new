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
import com.giveu.shoppingmall.model.bean.response.FeedBackListResponse;
import com.giveu.shoppingmall.utils.LoginHelper;
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
    public static final String isSecondFragmentKey = "isSecondFragmentKey";
    boolean isSecondFrg = false;//是不是已处理
    int pageNum = 1;//分页页码
    View fragmentView;
    @BindView(R.id.ptrlv)
    PullToRefreshListView ptrlv;
    LvCommonAdapter<FeedBackListResponse> feedbackListAdapter = null;
    LvCommonAdapter<String> gridViewAdapter = null;
    @BindView(R.id.ll_feedbacklist)
    LinearLayout llFeedbacklist;
    private FeedBackPresenter presenter;


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            isSecondFrg = arguments.getBoolean(isSecondFragmentKey);
        }

        baseLayout.setTitleBarAndStatusBar(false, false);
        fragmentView = View.inflate(mBaseContext, R.layout.fragment_feedback_list, null);
        ButterKnife.bind(this, fragmentView);

        ptrlv.setPullRefreshEnable(false);
        ptrlv.setPullLoadEnable(false);
        presenter = new FeedBackPresenter(this);
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
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                initDataDelay();
            }
        });
    }


    @Override
    public void initDataDelay() {
//        String state = "0";//state	记录：处理中/已处理	string	0处理中 1已处理
//        if (isSecondFrg) {
//            state = "1";
//        }
        presenter.getFeedBackRecord(LoginHelper.getInstance().getIdent(), LoginHelper.getInstance().getName(),
                "1", 1, LoginHelper.getInstance().getUserId());
        FeedBackListResponse response1 = new FeedBackListResponse();
        FeedBackListResponse response2 = new FeedBackListResponse();
        FeedBackListResponse response3 = new FeedBackListResponse();
        FeedBackListResponse response4 = new FeedBackListResponse();
        FeedBackListResponse response5 = new FeedBackListResponse();
        List<FeedBackListResponse> data = new ArrayList();
        data.add(response1);
        data.add(response2);
        data.add(response3);
        data.add(response4);
        data.add(response5);
        List<String> imgList = new ArrayList();
        imgList.add("");
        imgList.add("");
        imgList.add("");
        gridViewAdapter = new LvCommonAdapter<String>(mBaseContext, R.layout.img_feedback_item, imgList) {
            @Override
            protected void convert(ViewHolder viewHolder, String item, int position) {
                //ImageUtils.loadImage(item.bigImage,R.drawable.defalut_img_88_88,iv_img);
            }
        };

        feedbackListAdapter = new LvCommonAdapter<FeedBackListResponse>(mBaseContext, R.layout.activity_feedback_list_item, data) {
            @Override
            protected void convert(ViewHolder viewHolder, FeedBackListResponse item, int position) {
                GridView gv_item = viewHolder.getView(R.id.gv_img);
                TextView tv_content_yes = viewHolder.getView(R.id.tv_content_yes);
                TextView tv_time_yes = viewHolder.getView(R.id.tv_time_yes);
                ImageView iv_apply_right = viewHolder.getView(R.id.iv_apply_right);
                ImageView iv_apply_down = viewHolder.getView(R.id.iv_apply_down);
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
                gv_item.setAdapter(gridViewAdapter);
            }

        };

//        PersonInfoResponse personInfo = PersonInfoResponse.getInstance();
//        if (personInfo == null || personInfo.userInfo == null) {
//            return;
//        }
//        ApiImpl.sendFeedListRequest(null, personInfo.userInfo.ident, personInfo.userInfo.name, pageNum + "", personInfo.userInfo.mobile, state, "", new ResponseListener<FeedBackListResponse>() {
//            @Override
//            public void onSuccess(FeedBackListResponse response) {
//                if (response.data.size() == 0) {
//                    ptrlv.setVisibility(View.GONE);
//                    View view = LinearLayout.inflate(mBaseContext, R.layout.activity_nothing, null);
//                    TextView text = (TextView) view.findViewById(R.id.tv_nothing);
//                    ImageView img = (ImageView) view.findViewById(R.id.iv_nothing);
//                    text.setText("您还没有任何申请记录");
//                    img.setBackgroundResource(R.drawable.bg_apply_noapply);
//                    llFeedbacklist.addView(view);
//                } else {
//                    feedbackListAdapter.addItemList(response.data);
        ptrlv.setAdapter(feedbackListAdapter);
//                    if (response.data != null && response.data.size() == 20) {
//                        ptrlv.setPullLoadEnable(true);
//                    } else {
//                        ptrlv.setPullLoadEnable(false);
//                    }
//
//                    pageNum++;
//                }
//
//            }
//
//            @Override
//            public void onError(BaseBean errorBean) {
//                CommonLoadingView.showErrorToast(errorBean);
//            }
//        });
    }


}
