package com.giveu.shoppingmall.utils.listener;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

/**
 * simple and powerful Keyboard show/hidden listener,view {@android.R.id.content} and {@ViewTreeObserver.OnGlobalLayoutListener}
 * Created by yes.cpu@gmail.com 2016/7/13.
 */
public class KeyboardChangeListener implements ViewTreeObserver.OnGlobalLayoutListener {
    private static final String TAG = "ListenerHandler";
    private View mContentView;
    private int mOriginHeight;
    private int mPreHeight;
    private KeyBoardListener mKeyBoardListen;
    Context context;
    private boolean lastStatus = false;

    public interface KeyBoardListener {
        /**
         * call back
         * @param isShow true is show else hidden
         * @param keyboardHeight keyboard height
         */
        void onKeyboardChange(boolean isShow, int keyboardHeight);
    }

    public void setKeyBoardListener(KeyBoardListener keyBoardListen) {
        this.mKeyBoardListen = keyBoardListen;
    }

    public KeyboardChangeListener(Activity contextObj) {
        if (contextObj == null) {
            Log.i(TAG, "contextObj is null");
            return;
        }
        context = contextObj;
        mContentView = findContentView(contextObj);
        if (mContentView != null) {
            addContentTreeObserver();
        }
    }

    private View findContentView(Activity contextObj) {
        return contextObj.findViewById(android.R.id.content);
    }

    private void addContentTreeObserver() {
        mContentView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    private int screenheight = 0;

    public int getHeight() {
        if (screenheight > 0)
            return screenheight;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        // int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        screenheight = height;
        return screenheight;

    }

    public void onGlobalLayout() {
        // TODO Auto-generated method stub
        Rect r = new Rect();
        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(r);

        int screenHeight = getHeight();
        int heightDiff = screenHeight - (r.bottom - r.top);
        Log.d("Keyboard Size", "Size: " + heightDiff);
        boolean visible = Math.abs(heightDiff) > screenHeight / 3;

        if (lastStatus != visible) {
            lastStatus = visible;
            Log.d("Keyboard", "Keyboard " + (visible ? "opened" : "closed"));
            if (mKeyBoardListen != null) {
                mKeyBoardListen.onKeyboardChange(visible, heightDiff);
            }
        }
    }

    public void destroy() {
        if (mContentView != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mContentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        }
    }
}
