package com.giveu.shoppingmall.index.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RadioButton;

/**
 * Created by 524202 on 2017/9/7.
 * 图片和文字居中的RadioButton,可充当TextView使用
 * 用于价格排序切换按钮
 */
public class MiddleRadioButton extends RadioButton {

    public MiddleRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MiddleRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MiddleRadioButton(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //获取设置的图片  
        Drawable[] drawables = getCompoundDrawables();
        if (drawables != null) {
            //第一个是left  
            Drawable drawableLeft = drawables[0];
            if (drawableLeft != null) {
                //获取文字的宽度  
                float textWidth = getPaint().measureText(getText().toString());
                int drawablePadding = getCompoundDrawablePadding();
                int drawableWidth = 0;
                drawableWidth = drawableLeft.getIntrinsicWidth();
                float bodyWidth = textWidth + drawableWidth + drawablePadding;
                int y = getWidth();
                canvas.translate((getWidth() - bodyWidth) / 2, 0);
            }
        }
        super.onDraw(canvas);
    }
}