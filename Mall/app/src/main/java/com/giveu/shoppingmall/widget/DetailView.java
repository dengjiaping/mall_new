package com.giveu.shoppingmall.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.utils.StringUtils;


/**
 * 新建订单选择控件
 * Created by 101900 on 2017/2/24.
 */

public class DetailView extends LinearLayout {

    TextView tvLeft;
    TextView tvMiddle;
    TextView tvRight;
    ImageView ivRight;
    LinearLayout llDetail;

    public DetailView(Context context) {
        super(context);
        init(null);
    }

    public DetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    private void init(AttributeSet attrs) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_detail, this, true);
        //初始化控件
        tvLeft = (TextView) view.findViewById(R.id.tv_left);
        tvMiddle = (TextView) view.findViewById(R.id.tv_middle);
        tvRight = (TextView) view.findViewById(R.id.tv_right);
        ivRight = (ImageView) view.findViewById(R.id.iv_right);
        llDetail = (LinearLayout) view.findViewById(R.id.ll_detail_view);
        TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.DetailView);
        tvLeft.setText(attributes.getString(R.styleable.DetailView_text_left));
        tvLeft.setTextColor(attributes.getInt(R.styleable.DetailView_text_left_color, getContext().getResources().getColor(R.color.color_4a4a4a)));
        tvMiddle.setText(attributes.getString(R.styleable.DetailView_text_middle));
        tvMiddle.setTextColor(attributes.getInt(R.styleable.DetailView_text_middle_color, getContext().getResources().getColor(R.color.color_364343)));
        tvRight.setText(attributes.getString(R.styleable.DetailView_text_right));
        tvRight.setTextColor(attributes.getInt(R.styleable.DetailView_text_right_color, getContext().getResources().getColor(R.color.title_color)));
        tvRight.setFilters(new InputFilter[]{new InputFilter.LengthFilter(attributes.getInt(R.styleable.DetailView_text_right_maxLength, 40))});
        ivRight.setImageResource(attributes.getResourceId(R.styleable.DetailView_iv_right, R.drawable.ic_right));
        int left_visible = attributes.getInteger(R.styleable.DetailView_tv_left_visible, VISIBLE);
        if (left_visible == 0) {
            tvLeft.setVisibility(VISIBLE);
        } else if (left_visible == 1) {
            tvLeft.setVisibility(INVISIBLE);
        } else {
            tvLeft.setVisibility(INVISIBLE);
        }
        int iv_visible = attributes.getInteger(R.styleable.DetailView_iv_right_visible, VISIBLE);
        if (iv_visible == 0) {
            ivRight.setVisibility(VISIBLE);
        } else {
            ivRight.setVisibility(GONE);
        }

        int text_left_width = attributes.getInt(R.styleable.DetailView_cv_text_left_width, -1);
        if (text_left_width != -1) {
            tvLeft.setWidth(text_left_width);
        }
        int text_right_gravity = attributes.getInt(R.styleable.DetailView_cv_text_right_gravity, -1);
        if (text_right_gravity != -1) {
            tvRight.setGravity(text_right_gravity);
        }

        attributes.recycle();
    }

    //改变左边显示值
    public void setLeftText(String leftText) {
        setLeftText(leftText, 0);
    }

    //改变左边显示值
    public void setLeftText(String leftText, int color) {
        tvLeft.setText(leftText);
        if (color != 0) {
            tvLeft.setTextColor(getContext().getResources().getColor(color));
        }
    }

    //改变中间显示值
    public void setMiddleText(String leftText) {
        setMiddleText(leftText, 0);
    }

    public void setMiddleText(String leftText, int color) {
        tvMiddle.setText(leftText);
        if (color != 0) {
            tvMiddle.setTextColor(getContext().getResources().getColor(color));
        }
    }

    //选择后改变右边显示值
    public void setRightText(String leftText) {
        setRightText(leftText, 0);
    }

    public void setRightText(String leftText, int color) {
        tvRight.setText(leftText);
        if (color != 0) {
            tvRight.setTextColor(getContext().getResources().getColor(color));
        }
        ivRight.setVisibility(GONE);
    }

    public void clearTextLines() {
        tvRight.setMaxLines(2);
    }

    //改变右边字的显示位置
    public void changeRightTextGravity() {
        if (tvRight.getLineCount() == 1) {
            tvRight.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        }
        if (tvRight.getLineCount() > 1) {
            tvRight.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        }
    }

    //选择后改变右边显示值并改变箭头显示和修改右边字体颜色
    public void setRightText(String rightText, int color, boolean flag) {
        if (StringUtils.isNotNull(rightText)) {
            tvRight.setText(rightText.trim());
        }
        tvRight.setTextColor(getContext().getResources().getColor(color));
        if (flag) {
            ivRight.setVisibility(VISIBLE);
        } else {
            ivRight.setVisibility(GONE);
        }
    }

    //清空选择项右边的值（改成请选择...,并字体颜色蓝色）
    public void recoverRightText() {
        tvRight.setText("请选择...");
        tvRight.setTextColor(getContext().getResources().getColor(R.color.title_color));
        ivRight.setVisibility(VISIBLE);
    }


    public boolean isEmpty() {
        if ("请选择...".equals(tvRight.getText())) {
            //为空
            return true;
        }
        return false;
    }


    public boolean showIsNullNoHint() {
        if ("请选择...".equals(tvRight.getText())) {
            //为空
            return true;
        }
        return false;
    }


}

