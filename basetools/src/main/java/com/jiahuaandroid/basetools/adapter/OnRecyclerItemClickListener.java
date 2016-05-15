package com.jiahuaandroid.basetools.adapter;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by jhhuang on 2016/5/13.
 * QQ:781913268
 * 作用：recyclerview触摸监听器
 */
public abstract class OnRecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    /**
     * 手势探测器
     */
    private GestureDetectorCompat mGestureDetector;
    private RecyclerView recyclerView;

    public OnRecyclerItemClickListener(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        mGestureDetector = new GestureDetectorCompat(recyclerView.getContext(),new ItemTouchHelperGestureLintener());
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public abstract void onItemClick(RecyclerView.ViewHolder vh);
    public abstract void onItemLongClick(RecyclerView.ViewHolder vh);

    private class ItemTouchHelperGestureLintener extends GestureDetector.SimpleOnGestureListener {
        /**
         * 一次单独的轻击抬起操作，也就是轻击了一下屏幕，就是普通的点击事件
         * @param e
         * @return
         */
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if(child!=null) {
                RecyclerView.ViewHolder vh = recyclerView.getChildViewHolder(child);
                onItemClick(vh);
            }
            return true;
        }

        /**
         * 长按触摸屏，超过一定时长触发，长按监听
         * @param e
         */
        @Override
        public void onLongPress(MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if(child!=null) {
                RecyclerView.ViewHolder vh = recyclerView.getChildViewHolder(child);
                onItemLongClick(vh);
            }
        }
        /**
         * 用户按下屏幕会触发
         * @param e
         * @return
         */
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        /**
         * 如果是按下的时间超过瞬间，而且在按下的时候没有松开或者拖动
         * @param e
         */
        @Override
        public void onShowPress(MotionEvent e) {

        }

        /**
         * 在屏幕上拖动事件
         * @param e1
         * @param e2
         * @param distanceX
         * @param distanceY
         * @return
         */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        /**
         * 滑屏，用户按下触摸屏快速移动后松开
         * @param e1
         * @param e2
         * @param velocityX
         * @param velocityY
         * @return
         */
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }
}
