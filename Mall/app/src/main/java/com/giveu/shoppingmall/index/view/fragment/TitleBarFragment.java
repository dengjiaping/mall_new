package com.giveu.shoppingmall.index.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.index.view.activity.ShoppingSearchActivity;
import com.giveu.shoppingmall.index.widget.ClearEditText;
import com.giveu.shoppingmall.index.widget.MiddleRadioButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 524202 on 2017/9/8.
 */

public class TitleBarFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.fragment_left_image)
    ImageView leftImage;
    @BindView(R.id.fragment_center_text)
    MiddleRadioButton centerText;
    @BindView(R.id.fragment_center_edit)
    ClearEditText centerEdit;
    @BindView(R.id.fragment_right_text)
    TextView rightText;
    @BindView(R.id.fragment_right_image)
    ImageView rightImage;

    /**
     * 是否显示centerEdit,默认隐藏。与centerText互斥,即必须只显示其中一个
     */
    private boolean showCenterEdit = false;
    /**
     * 是否显示rightImage,默认隐藏
     */
    private boolean showRightImage = false;
    /**
     * 是否显示rightText,默认隐藏
     */
    private boolean showRightText = false;

    private View.OnClickListener searchListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (centerEdit.getText() != null) {
                v.setTag(centerEdit.getText().toString());
                searchListener.onClick(v);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_title_bar, container, false);
        Bundle args = getArguments();

        showCenterEdit = args.getBoolean("showCenterEdit", false);
        showRightText = args.getBoolean("showRightText", false);
        showRightImage = args.getBoolean("showRightImage", false);

        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    private void initViews() {
        if (showCenterEdit) {
            centerEdit.setVisibility(View.VISIBLE);
            centerText.setVisibility(View.GONE);
        }

        if (showRightImage) {
            rightImage.setVisibility(View.VISIBLE);
        }

        if (showRightText) {
            rightText.setVisibility(View.VISIBLE);
        }

        rightText.setOnClickListener(searchListener);
    }

    public void setSearchText(String text) {
        centerEdit.setText(text);
    }

    public void setOnSearchListener(View.OnClickListener listener) {
        searchListener = listener;
    }

    @OnClick({R.id.fragment_left_image, R.id.fragment_center_text,
            R.id.fragment_right_image})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_left_image:
                getActivity().finish();
                break;
            case R.id.fragment_center_text:
                ShoppingSearchActivity.startIt(getActivity());
                break;
            case R.id.fragment_right_image:
                break;
            default:
                break;
        }
    }

    public static TitleBarFragment newInstance(Bundle bundle) {

        Bundle args = new Bundle();
        if (bundle != null) {
            args.putAll(bundle);
        }
        TitleBarFragment fragment = new TitleBarFragment();
        fragment.setArguments(args);
        return fragment;
    }
}

