package com.basti.viewdraghelper;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by SHIBW-PC on 2016/2/24.
 */
public class VDHLayout extends LinearLayout {

    private ViewDragHelper mDragger;

    private View view1, view2, view3;

    private Point mAutoVIewPos = new Point();

    public VDHLayout(Context context) {
        this(context, null);
    }

    public VDHLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VDHLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initViewDragHelper();

    }

    private void initViewDragHelper() {
        mDragger = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == view1 || child == view2;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                //水平方向限制在vg内
                if (left < 0) return 0;
                if (left > getWidth() - child.getWidth()) return getWidth() - child.getWidth();

                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                //竖直方向限制在vg内
                if (top < 0) return 0;
                if (top > getHeight() - child.getHeight()) return getHeight() - child.getHeight();

                return top;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if (releasedChild == view2) {
                    mDragger.settleCapturedViewAt(mAutoVIewPos.x, mAutoVIewPos.y);
                    invalidate();
                }
            }

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                mDragger.captureChildView(view3, pointerId);
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return getMeasuredWidth() - child.getMeasuredWidth();
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return getMeasuredHeight() - child.getMeasuredHeight();
            }
        });
        mDragger.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    @Override
    public void computeScroll() {
        if (mDragger.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //决定是否应当拦截当前的事件
        return mDragger.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //onTouchEvent中通过mDragger.processTouchEvent(event)处理事件
        mDragger.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        view1 = getChildAt(0);
        view2 = getChildAt(1);
        view3 = getChildAt(2);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        mAutoVIewPos.x = view2.getLeft();
        mAutoVIewPos.y = view2.getTop();
    }
}
