package org.chengpx.test180501.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * create at 2018/5/2 11:05 by chengpx
 */
public class NoScrollViewPager extends ViewPager {

    /**
     * 是否禁止左右滑动，true 为禁止，false 为不禁止
     */
    private boolean noScroll = true;

    public NoScrollViewPager(@NonNull Context context) {
        super(context);
    }

    public NoScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return !noScroll && super.onTouchEvent(motionEvent);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return !noScroll && super.onInterceptTouchEvent(motionEvent);
    }

}
