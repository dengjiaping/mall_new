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

public class ChooseView extends LinearLayout {

    TextView tvChooseLeft;
    TextView tvChooseRight;
    ImageView ivRight;
    LinearLayout llChooseView;

    public ChooseView(Context context) {
        super(context);
        init(null);
    }

    public ChooseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    private void init(AttributeSet attrs) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_choose, this, true);
        //初始化控件
        tvChooseLeft = (TextView) view.findViewById(R.id.tv_choose_left);
        tvChooseRight = (TextView) view.findViewById(R.id.tv_choose_right);
        ivRight = (ImageView) view.findViewById(R.id.iv_right);
        llChooseView = (LinearLayout) view.findViewById(R.id.ll_choose_view);
        TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.ChooseView);
        tvChooseLeft.setText(attributes.getString(R.styleable.ChooseView_text_left));
        tvChooseLeft.setTextColor(attributes.getInt(R.styleable.ChooseView_text_left_color, getContext().getResources().getColor(R.color.color_4a4a4a)));
        tvChooseRight.setText(attributes.getString(R.styleable.ChooseView_text_right));
        tvChooseRight.setTextColor(attributes.getInt(R.styleable.ChooseView_text_right_color, getContext().getResources().getColor(R.color.title_color)));
        tvChooseRight.setFilters(new InputFilter[]{new InputFilter.LengthFilter(attributes.getInt(R.styleable.ChooseView_text_right_maxLength, 40))});
        ivRight.setImageResource(attributes.getResourceId(R.styleable.ChooseView_iv_right, R.drawable.ic_right));
        int text_left_width = attributes.getInt(R.styleable.ChooseView_cv_text_left_width, -1);
        if(text_left_width != -1){
            tvChooseLeft.setWidth(text_left_width);
        }
        int text_right_gravity = attributes.getInt(R.styleable.ChooseView_cv_text_right_gravity, -1);
        if(text_right_gravity != -1){
            tvChooseRight.setGravity(text_right_gravity);
        }

        attributes.recycle();
    }

    //改变左边显示值
    public void setLeftText(String leftText, int color) {
        tvChooseLeft.setText(leftText);
        if (color != 0) {
            tvChooseLeft.setTextColor(getContext().getResources().getColor(color));
        }
    }

    //选择后改变右边显示值
    public void setRightText(String rightText) {
        tvChooseRight.setText(rightText.trim());
        tvChooseRight.setTextColor(getContext().getResources().getColor(R.color.color_4a4a4a));
        ivRight.setVisibility(GONE);
    }

    public void clearTextLines() {
        tvChooseRight.setMaxLines(2);
    }

    //改变右边字的显示位置
    public void changeRightTextGravity() {
        if(tvChooseRight.getLineCount() == 1){
        tvChooseRight.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        }
        if(tvChooseRight.getLineCount() > 1){
        tvChooseRight.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        }
    }

    //选择后改变右边显示值并改变箭头显示和修改右边字体颜色
    public void setRightText(String rightText, int color, boolean flag) {
        if (StringUtils.isNotNull(rightText)) {
            tvChooseRight.setText(rightText.trim());
        }
        tvChooseRight.setTextColor(getContext().getResources().getColor(color));
        if (flag) {
            ivRight.setVisibility(VISIBLE);
        } else {
            ivRight.setVisibility(GONE);
        }
    }

    //清空选择项右边的值（改成请选择...,并字体颜色蓝色）
    public void recoverRightText() {
        tvChooseRight.setText("请选择...");
        tvChooseRight.setTextColor(getContext().getResources().getColor(R.color.title_color));
        ivRight.setVisibility(VISIBLE);
    }


    public boolean isEmpty() {
        if ("请选择...".equals(tvChooseRight.getText())) {
            //为空
            return true;
        }
        return false;
    }


    public boolean showIsNullNoHint() {
        if ("请选择...".equals(tvChooseRight.getText())) {
            //为空
            return true;
        }
        return false;
    }


}

