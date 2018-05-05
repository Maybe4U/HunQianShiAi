package com.zykj.hunqianshiai.utils;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ScrollView;

/**
 * 监听ScrollView滑动距离
 * Created by xu on 2017/12/20.
 */

public class ObservableScrollView extends ScrollView {
    private Context mContext;

    private OnScrollChangedListener onScrollChangedListener = null;

    public ObservableScrollView(Context context, float width) {
        super(context);
        mContext = context;
        screenWidth = width;
    }

    public ObservableScrollView(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener) {
        this.onScrollChangedListener = onScrollChangedListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (onScrollChangedListener != null) {
            onScrollChangedListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

    public interface OnScrollChangedListener {

        void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);

    }



    /*===================右滑返回上一层====================*/
    /**
     * 手指按下时的坐标
     */
    float downX, downY, moveX, moveY, xDistance, yDistance;

    /**
     * 手机屏幕的宽度和高度
     */
    float screenWidth, screenHeight;


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 获得按下时的X坐标
                downX = ev.getX();
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                // 获得按下时的X坐标
                moveX = ev.getX();
                moveY = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                // 获得按下时的X坐标
                moveX = ev.getX();
                moveY = ev.getY();
                break;
        }
        xDistance = moveX - downX;
        yDistance = Math.abs(moveY - downY);
        if (xDistance > yDistance && moveX != 0) {
            return true;
        }


        return super.onInterceptTouchEvent(ev);
    }

    //    @Override
    //    public boolean onTouchEvent(MotionEvent event) {
    //
    //        if(event.getAction() == MotionEvent.ACTION_DOWN){// 当按下时
    //            // 获得按下时的X坐标
    //            downX = event.getX();
    //            downY = event.getY();
    //            //return false;
    //        }else if(event.getAction() == MotionEvent.ACTION_MOVE){// 当手指滑动时
    //            // 获得滑过的距离
    //            float moveDistanceX = event.getX() - downX;
    //            if(moveDistanceX > 0){// 如果是向右滑动
    //                return false;
    //            }
    //
    //        }else if(event.getAction() == MotionEvent.ACTION_UP){// 当抬起手指时
    //            // 获得滑过的距离
    //            float moveDistanceX = event.getX() - downX;
    //            float moveDistanceY = event.getY() - downY;
    //            if(moveDistanceX > screenWidth / 2){
    //                // 如果滑动的距离超过了手机屏幕的一半, 结束当前Activity
    //                return false;
    //            }else{ // 如果滑动距离没有超过一半
    //                return  false;// 恢复初始状态
    //            }
    //        }
    //        return super.onTouchEvent(event);
    //    }
}
