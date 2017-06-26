package com.lichfaker.scaleview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by 101900 on 2017/6/16.
 */

public class PointerView extends View {
    Context mContext;
    public PointerView(Context context) {
        super(context);
        mContext = context;
    }

    public PointerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public PointerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#00B8BC"));
        paint.setAntiAlias(true);
        int width = getScreenWidth(mContext)/2;
        canvas.drawLine(width, DisplayUtils.dip2px(mContext, 100),
                width, DisplayUtils.dip2px(mContext, 100) + 160, paint);//标尺高度  mRectHeight = mScaleHeight(20) * 8;
        Path path = new Path();
        path.moveTo(width - 15, DisplayUtils.dip2px(mContext, 100));//330 - 100dp=横线上方
        path.lineTo(width + 15, DisplayUtils.dip2px(mContext, 100));
        path.lineTo(width, DisplayUtils.dip2px(mContext, 110));
        path.close();
        canvas.drawPath(path, paint);
        super.onDraw(canvas);
    }
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }
}
