package com.giveu.shoppingmall.me.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import com.giveu.shoppingmall.R;



/**
 * 输错位数红字显示，输正确位数黑字的EditText
 * Created by 101900 on 2017/2/24.
 */

public class EditView extends EditText {


    public EditText etEditView;


    public EditView(Context context) {
        super(context);
        init(null);
    }

    public EditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }


    private void init(AttributeSet attrs) {
        TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.EditView);
        etEditView = this;
//        tvEditView.setTextColor(attributes.getInt(R.styleable.EditView_tv_text_color_left, getContext().getResources().getColor(R.color.color_4a4a4a)));
//        tvEditView.setText(attributes.getString(R.styleable.EditView_tv_text_left));
//        etEditView.setHint(attributes.getString(R.styleable.EditView_et_hint));
//        etEditView.setText(attributes.getString(R.styleable.EditView_et_text_right));
//        etEditView.setTextColor(attributes.getInt(R.styleable.EditView_et_text_color_right, getContext().getResources().getColor(R.color.color_4a4a4a)));
//        etEditView.setInputType(attributes.getInt(R.styleable.EditView_android_inputType, InputType.TYPE_CLASS_TEXT));
//        etEditView.setFocusableInTouchMode(attributes.getBoolean(R.styleable.EditView_et_editable, true));
        etEditView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(attributes.getInt(R.styleable.EditView_et_maxLength, 40))});
        attributes.recycle();
    }

    public interface Style{
        String NAME = "name";//姓名
        String IDENT = "ident";//身份证
    }

    /**
     * 根据传入的位数限制检验字体显示红色还是黑色
     * @param flag
     */
    public void checkFormat(final int flag) {
        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == flag) {
                    etEditView.setTextColor(getResources().getColor(R.color.black));
                } else {
                    etEditView.setTextColor(getResources().getColor(R.color.red));
                }
            }
        });
    }

    /**
     * 根据传入的类型检验字体显示红色还是黑色
     * @param style 传入的类型
     */
    public void checkFormat(final String style) {
        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                switch (style) {
                    case Style.NAME://姓名2-18，不含特殊字符
                        if (checkUserNameAndTipError(s.toString())) {
                            etEditView.setTextColor(getResources().getColor(R.color.black));
                        } else {
                            etEditView.setTextColor(getResources().getColor(R.color.red));
                        }
                        break;
                    case Style.IDENT://身份证15或18位
                        if (s.length() == 15 || s.length() == 18) {
                            etEditView.setTextColor(getResources().getColor(R.color.black));
                        } else {
                            etEditView.setTextColor(getResources().getColor(R.color.red));
                        }
                        break;
                }
            }
        });
    }

    /**
     * 姓名格式检测
     * @param nickname
     * @return
     */
    public boolean checkUserNameAndTipError(String nickname) {
        if (TextUtils.isEmpty(nickname)) {
            return false;
        }
        if (nickname.length() < 2 || nickname.length() > 18) {
            return false;
        }
        if(nickname.matches("^[A-Z|a-z]*$")){
            return false;
        }else if (!nickname.matches("[\\u4e00-\\u9fa5]{1,14}[\\?•·・∙]{0,1}[\\u4e00-\\u9fa5]{1,13}+$")) {
            return false;
        }
        return true;
    }
}
