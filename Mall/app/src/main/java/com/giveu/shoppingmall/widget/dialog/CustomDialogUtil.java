package com.giveu.shoppingmall.widget.dialog;

import android.app.Activity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.base.ParentAdapter;

import java.util.ArrayList;
import java.util.List;

public class CustomDialogUtil {
	Activity mActivity;
    public CustomDialog customDialog;
    private TextView tv_right;
    private TextView tv_title;
	private TextView tv_content;
    private TextView tv_left;
	
	
	public CustomDialogUtil(Activity context) {
		this.mActivity = context;
	}
	
	/**
	 * 类似登录被挤下来的dialog样式,详情请看custom_dialog_view_mode1.xml
	 * @param title
	 * @param content
	 * @param rightText
	 * @param leftText
	 * @return
	 */
	public CustomDialog getDialogMode1(CharSequence title, CharSequence content, CharSequence leftText, CharSequence rightText, final OnClickListener leftListener, final OnClickListener rightListener) {
		View view = View.inflate(mActivity, R.layout.custom_dialog_view_mode1, null);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_content = (TextView) view.findViewById(R.id.tv_content);
		tv_right = (TextView) view.findViewById(R.id.tv_right);
		tv_left = (TextView) view.findViewById(R.id.tv_left);

        tv_left.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dismissDialog();

                if (leftListener != null) {
                    leftListener.onClick(v);
                }
            }
        });

        tv_right.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dismissDialog();

                if (rightListener != null) {
                    rightListener.onClick(v);
                }
            }
        });

		customDialog = new CustomDialog(mActivity, view, R.style.date_dialog_style, Gravity.CENTER, false);
		if (TextUtils.isEmpty(content)) {
			tv_content.setVisibility(View.GONE);
		}else {
			tv_content.setVisibility(View.VISIBLE);
			tv_content.setText(content);
		}
		
		if (TextUtils.isEmpty(title)) {
			tv_title.setVisibility(View.GONE);
		}else {
			tv_title.setVisibility(View.VISIBLE);
			tv_title.setText(title);
		}
	
		if (TextUtils.isEmpty(rightText)) {
			tv_right.setVisibility(View.GONE);
		}else {
			tv_right.setVisibility(View.VISIBLE);
			tv_right.setText(rightText);
		}
		
		if (TextUtils.isEmpty(leftText)) {
			tv_left.setVisibility(View.GONE);
		}else {
			tv_left.setVisibility(View.VISIBLE);
			tv_left.setText(leftText);
		}
		
		customDialog.setCancelable(true);
		customDialog.setCanceledOnTouchOutside(true);
		return customDialog;
	}

	public void setDialogCancelable(boolean cancelable){
		customDialog.setCancelable(cancelable);
	}

	public void setDialogCanceledOnTouchOutside(boolean canceledOnTouchOutside){
		customDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
	}

    /**
     * 垂直列表样式的dialog，样子参考custom_dialog_view_mode2.xml
     * @param leftText
     * @param rightText
     * @param leftListener
     * @param rightListener
     * @return
     */
    public CustomDialog getDialogMode2(final CharSequence leftText, final CharSequence rightText, final OnClickListener leftListener, final OnClickListener rightListener) {
        CustomListDialog listDialog = new CustomListDialog(mActivity, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) ((ParentAdapter) parent.getAdapter()).getItem(position);
                if ( !TextUtils.isEmpty(item) && item.equals(leftText)){
                    if (leftListener != null){
                        leftListener.onClick(view);
                    }
                }else if (!TextUtils.isEmpty(item) && item.equals(rightText)){
                    if (rightListener != null){
                        rightListener.onClick(view);
                    }
                }
            }
        });

        List<CharSequence> data = new ArrayList<>();
        if ( !TextUtils.isEmpty(leftText)){
            data.add(leftText);
        }
        if ( !TextUtils.isEmpty(rightText)){
            data.add(rightText);
        }
        listDialog.setData(data);

        return customDialog = listDialog;
    }
	public CustomDialog getDialogMode3(final CharSequence topText, final CharSequence midText, final CharSequence bottomText, final OnClickListener topListener,final OnClickListener midListener, final OnClickListener bottomListener) {
		CustomListDialog listDialog = new CustomListDialog(mActivity, new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String item = (String) ((ParentAdapter) parent.getAdapter()).getItem(position);
				if ( !TextUtils.isEmpty(item) && item.equals(topText)){
					if (topListener != null){
						topListener.onClick(view);
					}
				}else if (!TextUtils.isEmpty(item) && item.equals(midText)){
					if (midListener != null){
						midListener.onClick(view);
					}
				}else if(!TextUtils.isEmpty(item) && item.equals(bottomText)){
					if (bottomListener != null){
						bottomListener.onClick(view);
					}
				}
			}
		});

		List<CharSequence> data = new ArrayList<>();
		if ( !TextUtils.isEmpty(topText)){
			data.add(topText);
		}
		if ( !TextUtils.isEmpty(midText)){
			data.add(midText);
		}
		if ( !TextUtils.isEmpty(bottomText)){
			data.add(bottomText);
		}
		listDialog.setData(data);

		return customDialog = listDialog;
	}
	public void dismissDialog() {
		if (customDialog != null && customDialog.isShowing()) {
			customDialog.dismiss();
		}
	}


}
