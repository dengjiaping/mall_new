package com.giveu.shoppingmall.index.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;

/**
 * Created by zlt on 2017/9/2.
 * 左侧固定内容的EditText
 * 用于回复/评论
 */

public class StableEditText extends android.support.v7.widget.AppCompatEditText {

    public final static int DEFAULT_STABLE_TEXT_COLOR = Color.parseColor("#333333");

    private CharSequence stableText = "";
    private ForegroundColorSpan colorSpan = null;
    private int stableTextColor;
    private boolean initStableText = true;

    private boolean isFirstIn = true;

    private int start = 0;
    private int end = 0;

    public StableEditText(Context context) {
        super(context);
        init();
    }

    public StableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        stableTextColor = DEFAULT_STABLE_TEXT_COLOR;
        addTextChangedListener(new TextWatcher() {
            String textBefore;

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.toString().startsWith(stableText.toString())) {
                    textBefore = s.subSequence(stableText.length(), s.length()).toString();
                }
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < stableText.length()) {
                    initStableText = false;
                    setText(stableText);
                    setSelection(stableText.length());
                } else {
                    String text = s.toString();
                    String tempStableText = text.substring(0, stableText.length());
                    if (!tempStableText.equals(stableText)) {
                        setText(textBefore);
                    }
                }
            }
        });
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (isFirstIn) {
            super.setText(text, type);
            return;
        }

        String newText;
        if (text == null) {
            text = "";
        }

        if (stableText == null) {
            stableText = "";
        }

        if (initStableText) {
            newText = stableText.toString() + text;
        } else {
            newText = text.toString();
            initStableText = true;
        }

        if (end > start) {
            colorSpan = new ForegroundColorSpan(stableTextColor);
            SpannableString spannableString = new SpannableString(newText);
            spannableString.setSpan(colorSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            super.setText(spannableString, type);
        } else {
            super.setText(newText, type);
        }

        setSelection(newText.length());
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        int length = stableText.length();
        if (selStart < length) {
            setSelection(length);
        }
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused && isFirstIn) {
            setText("");
//            setTextColor(Color.parseColor("#4a4a4a"));
            isFirstIn = false;
            if (firstFocusedListener != null){
                firstFocusedListener.firstFocused();
            }
        }
    }

    /**
     * 设置左侧固定字体
     *
     * @param s 左侧固定字体
     */
    public void setStableText(CharSequence s) {
        stableText = s;
        initStableText = false;
        setText(stableText);
    }

    public void setSpannerStableText(CharSequence s, int start, int end, int color) {
        stableText = s;
        initStableText = false;
        this.start = start;
        this.end = end;
        stableTextColor = color;
        setText(stableText);
    }

    /**
     * 返回除固定字体外的内容
     *
     * @return
     */
    public String getFinalText() {
        if (isFirstIn){
            return null;
        }
        String reslut = getText().toString();
        if (stableText == null) {
            return reslut;
        }
        return reslut.substring(stableText.length());
    }

    public OnFirstFocusedListener firstFocusedListener;

    public void setOnFirstFocusedListener(OnFirstFocusedListener listener){
        firstFocusedListener = listener;
    }

    public interface OnFirstFocusedListener {
        void firstFocused();
    }
}