package com.giveu.shoppingmall.view.imageselect;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.DensityUtils;


public class FloderView extends LinearLayout implements OnItemClickListener {
    private ListView listView;
    private View view_bg;
    private View floder_con;
    private Activity mContext;
    private FloderImageAdapter mAdapter;
    private ImageInfo imageInfo;

    private int animationDuration = 250;


    public FloderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = (Activity) context;
        View.inflate(getContext(), R.layout.list_dir, this);
        listView = (ListView) findViewById(R.id.list_dir);
        view_bg = findViewById(R.id.view_bg);
        floder_con = findViewById(R.id.floder_con);
        listView.setOnItemClickListener(this);
        mAdapter = new FloderImageAdapter(mContext);
        listView.setAdapter(mAdapter);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) listView.getLayoutParams();
        params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        params.height = (int) (DensityUtils.getHeight() * 0.5);
        listView.setLayoutParams(params);

        floder_con.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimation(false);
            }
        });
    }

    /**
     * 设置数据
     *
     * @param imageInfo
     */
    public void setData(ImageInfo imageInfo) {
        this.imageInfo = imageInfo;
        mAdapter.setItemList(imageInfo.imageFloders);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mAdapter.setSelectIndex(position);
        mAdapter.notifyDataSetChanged();
        setVisibility(View.GONE);
        if (onClickFloderListener != null) {
            if (imageInfo != null && !CommonUtils.isNullOrEmpty(imageInfo.imageFloders)) {
                onClickFloderListener.onClickFloder(imageInfo.imageFloders.get(position));
            }
        }
    }

    public interface OnClickFloderListener {
        public void onClickFloder(ImageFloder imageFloder);
    }

    private OnClickFloderListener onClickFloderListener;

    public void setOnClickFloderListener(OnClickFloderListener onClickFloderListener) {
        this.onClickFloderListener = onClickFloderListener;
    }

    private ValueAnimator animator1;

    /**
     * 开启显示动画
     */
    public void startAnimation(final boolean isShow) {
        animator1 = new ValueAnimator();
        animator1.setDuration(animationDuration);
        float startY = 0;
        float endY = 0;
        if (isShow) {
            startY = -listView.getHeight();
            endY = 0;
        } else {
            startY = 0;
            endY = -listView.getHeight();
        }
        animator1.setFloatValues(startY, endY);
        animator1.setInterpolator(new AccelerateDecelerateInterpolator());
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentY = (float) animation.getAnimatedValue();
                listView.setY(currentY);
            }
        });
        animator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (isShow) {
                    listView.setVisibility(View.VISIBLE);
                    view_bg.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isShow) {
                    listView.setVisibility(View.INVISIBLE);
                    view_bg.setVisibility(View.INVISIBLE);
                    setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator1.start();
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == View.VISIBLE) {
            startAnimation(true);
        }
    }
}
