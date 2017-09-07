package com.giveu.shoppingmall.utils;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created by 524202 on 2017/9/4.
 */

public class RecyclerViewScrollHelper {
    //将当前选中的item居中
    public static void moveToCenter(RecyclerView rv, int position) {
        RecyclerView.LayoutManager manager = rv.getLayoutManager();
        int firstPosition = 0;
        if (manager instanceof GridLayoutManager) {
            firstPosition = ((GridLayoutManager) manager).findFirstVisibleItemPosition();
        } else if (manager instanceof LinearLayoutManager) {
            firstPosition = ((LinearLayoutManager) manager).findFirstVisibleItemPosition();
        }

        View childAt = rv.getChildAt(position - firstPosition);
        if (childAt != null) {
            int y = (childAt.getTop() - rv.getHeight() / 2);
            rv.smoothScrollBy(0, y);
        }
    }

    public static void smoothScrollToPosition(RecyclerView rv, int pos) {
        if (rv.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
            rv.stopScroll();
        }
        RecyclerView.LayoutManager mManager = rv.getLayoutManager();

        int firstPosition = 0;
        int lastPosition = 0;

        if (mManager instanceof GridLayoutManager) {
            firstPosition = ((GridLayoutManager) mManager).findFirstVisibleItemPosition();
            lastPosition = ((GridLayoutManager) mManager).findLastVisibleItemPosition();
        } else if (mManager instanceof LinearLayoutManager) {
            firstPosition = ((LinearLayoutManager) mManager).findFirstVisibleItemPosition();
            lastPosition = ((LinearLayoutManager) mManager).findLastVisibleItemPosition();
        }

        if (pos <= firstPosition || pos > lastPosition) {
            rv.scrollToPosition(pos);
        } else {
            int top = rv.getChildAt(pos - firstPosition).getTop();
            rv.scrollBy(0, top);
        }
    }

    public static class RecyclerViewListener extends RecyclerView.OnScrollListener {

        private boolean isMove;
        private int mIndex;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (isMove && newState == RecyclerView.SCROLL_STATE_IDLE) {
                isMove = false;
                RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                int firstPosition = 0;
                if (manager instanceof GridLayoutManager) {
                    firstPosition = ((GridLayoutManager) manager).findFirstVisibleItemPosition();
                } else if (manager instanceof LinearLayoutManager) {
                    firstPosition = ((LinearLayoutManager) manager).findFirstVisibleItemPosition();
                }
                int n = mIndex - firstPosition;
                if (0 <= n && n < recyclerView.getChildCount()) {
                    int top = recyclerView.getChildAt(n).getTop();
                    recyclerView.smoothScrollBy(0, top);
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (isMove) {
                isMove = false;
                RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                int firstPosition = 0;
                if (manager instanceof GridLayoutManager) {
                    firstPosition = ((GridLayoutManager) manager).findFirstVisibleItemPosition();
                } else if (manager instanceof LinearLayoutManager) {
                    firstPosition = ((LinearLayoutManager) manager).findFirstVisibleItemPosition();
                }
                int n = mIndex - firstPosition;
                if (0 <= n && n < recyclerView.getChildCount()) {
                    int top = recyclerView.getChildAt(n).getTop();
                    recyclerView.scrollBy(0, top);
                }
            }
        }

        public boolean isMove() {
            return isMove;
        }

        public void setMove(boolean isMove) {
            this.isMove = isMove;
        }

        public int getIndex() {
            return mIndex;
        }

        public void setIndex(int index) {
            mIndex = index;
        }
    }


}
