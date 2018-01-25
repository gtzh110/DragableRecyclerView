package FscBoardView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.os.Build.VERSION;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import com.woxthebox.draglistview.sample.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import util.UiUtil;

public class PagerRecyclerView extends RecyclerView {
    View mCurView;
    int mFirstTopWhenDragging;
    int mFisrtLeftWhenDragging;
    private float mFlingFactor = 0.15f;
    private boolean mHasCalledOnPageChanged = true;
    int mLastX;
    int mLastY;
    int mMaxLeftWhenDragging = Integer.MIN_VALUE;
    int mMaxTopWhenDragging = Integer.MIN_VALUE;
    int mMinLeftWhenDragging = Integer.MAX_VALUE;
    int mMinTopWhenDragging = Integer.MAX_VALUE;
    boolean mNeedAdjust;
    private List<OnPageChangedListener> mOnPageChangedListeners;
    private int mPositionBeforeScroll = -1;
    private int mPositionOnTouchDown = -1;
    private boolean mSinglePageFling;
    private int mSmoothScrollTargetPosition = -1;
    private float mTouchSpan;
    private float mTriggerOffset = 0.25f;
    private RecyclerViewPagerAdapter<?> mViewPagerAdapter;
    private boolean reverseLayout = false;

    public interface OnPageChangedListener {
        void OnPageChanged(int i, int i2);
    }

    public PagerRecyclerView(Context context) {
        super(context, null);
    }

    public PagerRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public PagerRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(context, attrs, defStyle);
        setNestedScrollingEnabled(false);
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RecyclerViewPager, defStyle, 0);
        this.mFlingFactor = a.getFloat(R.styleable.RecyclerViewPager_flingFactor, 0.15f);
        this.mTriggerOffset = a.getFloat(R.styleable.RecyclerViewPager_triggerOffset, 0.25f);
        this.mSinglePageFling = a.getBoolean(R.styleable.RecyclerViewPager_singlePageFling, this.mSinglePageFling);
        a.recycle();
    }

    public void setFlingFactor(float flingFactor) {
        this.mFlingFactor = flingFactor;
    }

    public float getFlingFactor() {
        return this.mFlingFactor;
    }

    public void setTriggerOffset(float triggerOffset) {
        this.mTriggerOffset = triggerOffset;
    }

    public float getTriggerOffset() {
        return this.mTriggerOffset;
    }

    public void setSinglePageFling(boolean singlePageFling) {
        this.mSinglePageFling = singlePageFling;
    }

    public boolean isSinglePageFling() {
        return this.mSinglePageFling;
    }

    protected void onRestoreInstanceState(Parcelable state) {
        try {
            Field fLayoutState = state.getClass().getDeclaredField("mLayoutState");
            fLayoutState.setAccessible(true);
            Object layoutState = fLayoutState.get(state);
            Field fAnchorOffset = layoutState.getClass().getDeclaredField("mAnchorOffset");
            Field fAnchorPosition = layoutState.getClass().getDeclaredField("mAnchorPosition");
            fAnchorPosition.setAccessible(true);
            fAnchorOffset.setAccessible(true);
            if (fAnchorOffset.getInt(layoutState) > 0) {
                fAnchorPosition.set(layoutState, Integer.valueOf(fAnchorPosition.getInt(layoutState) - 1));
            } else if (fAnchorOffset.getInt(layoutState) < 0) {
                fAnchorPosition.set(layoutState, Integer.valueOf(fAnchorPosition.getInt(layoutState) + 1));
            }
            fAnchorOffset.setInt(layoutState, 0);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        super.onRestoreInstanceState(state);
    }

    public void setAdapter(Adapter adapter) {
        this.mViewPagerAdapter = ensureRecyclerViewPagerAdapter(adapter);
        super.setAdapter(this.mViewPagerAdapter);
    }

    public void swapAdapter(Adapter adapter, boolean removeAndRecycleExistingViews) {
        this.mViewPagerAdapter = ensureRecyclerViewPagerAdapter(adapter);
        super.swapAdapter(adapter, removeAndRecycleExistingViews);
    }

    public Adapter getAdapter() {
        if (this.mViewPagerAdapter != null) {
            return this.mViewPagerAdapter.mAdapter;
        }
        return null;
    }

    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        if (layout instanceof LinearLayoutManager) {
            this.reverseLayout = ((LinearLayoutManager) layout).getReverseLayout();
        }
    }

    public boolean fling(int velocityX, int velocityY) {
        boolean fling = super.fling((int) (((float) velocityX) * this.mFlingFactor), (int) (((float) velocityY) * this.mFlingFactor));
        if (fling) {
            if (getLayoutManager().canScrollHorizontally()) {
                adjustPositionX(velocityX);
            } else {
                adjustPositionY(velocityY);
            }
        }
        return fling;
    }

    public void smoothScrollToPosition(int position) {
        this.mSmoothScrollTargetPosition = position;
        if (getLayoutManager() == null || !(getLayoutManager() instanceof LinearLayoutManager)) {
            super.smoothScrollToPosition(position);
            return;
        }
        LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(getContext()) {
            public PointF computeScrollVectorForPosition(int targetPosition) {
                if (getLayoutManager() == null) {
                    return null;
                }
                return ((LinearLayoutManager) getLayoutManager()).computeScrollVectorForPosition(targetPosition);
            }

            protected void onTargetFound(View targetView, State state, Action action) {
                if (getLayoutManager() != null) {
                    int dx = calculateDxToMakeVisible(targetView, getHorizontalSnapPreference());
                    int dy = calculateDyToMakeVisible(targetView, getVerticalSnapPreference());
                    if (dx > 0) {
                        dx -= getLayoutManager().getLeftDecorationWidth(targetView);
                    } else {
                        dx += getLayoutManager().getRightDecorationWidth(targetView);
                    }
                    if (dy > 0) {
                        dy -= getLayoutManager().getTopDecorationHeight(targetView);
                    } else {
                        dy += getLayoutManager().getBottomDecorationHeight(targetView);
                    }
                    int time = calculateTimeForDeceleration((int) Math.sqrt((double) ((dx * dx) + (dy * dy))));
                    if (time > 0) {
                        action.update(-dx, -dy, time, this.mDecelerateInterpolator);
                    }
                }
            }
        };
        linearSmoothScroller.setTargetPosition(position);
        if (position != -1) {
            getLayoutManager().startSmoothScroll(linearSmoothScroller);
        }
    }

    public void scrollToPosition(int position) {
        this.mPositionBeforeScroll = getCurrentPosition();
        this.mSmoothScrollTargetPosition = position;
        super.scrollToPosition(position);
        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if (VERSION.SDK_INT < 16) {
                    PagerRecyclerView.this.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    PagerRecyclerView.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                if (PagerRecyclerView.this.mSmoothScrollTargetPosition >= 0 && PagerRecyclerView.this.mSmoothScrollTargetPosition < PagerRecyclerView.this.mViewPagerAdapter.getItemCount() && PagerRecyclerView.this.mOnPageChangedListeners != null) {
                    for (OnPageChangedListener onPageChangedListener : PagerRecyclerView.this.mOnPageChangedListeners) {
                        if (onPageChangedListener != null) {
                            onPageChangedListener.OnPageChanged(PagerRecyclerView.this.mPositionBeforeScroll, PagerRecyclerView.this.getCurrentPosition());
                        }
                    }
                }
            }
        });
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            int centerXChildPosition;
            if (getLayoutManager().canScrollHorizontally()) {
                centerXChildPosition = RecyclerviewUtils.getCenterXChildPosition(this);
            } else {
                centerXChildPosition = RecyclerviewUtils.getCenterYChildPosition(this);
            }
            this.mPositionOnTouchDown = centerXChildPosition;
        }
        return super.dispatchTouchEvent(ev);
    }

    public boolean onInterceptTouchEvent(MotionEvent e) {
        int x = (int) e.getRawX();
        int y = (int) e.getRawY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN :
                this.mLastX = x;
                this.mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                return Math.abs(x - this.mLastX) > UiUtil.dp2px(getContext(), 16.0f);
        }
        return super.onInterceptTouchEvent(e);
    }

    public boolean onTouchEvent(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_MOVE && this.mCurView != null) {
            this.mMaxLeftWhenDragging = Math.max(this.mCurView.getLeft(), this.mMaxLeftWhenDragging);
            this.mMaxTopWhenDragging = Math.max(this.mCurView.getTop(), this.mMaxTopWhenDragging);
            this.mMinLeftWhenDragging = Math.min(this.mCurView.getLeft(), this.mMinLeftWhenDragging);
            this.mMinTopWhenDragging = Math.min(this.mCurView.getTop(), this.mMinTopWhenDragging);
        }
        return super.onTouchEvent(e);
    }

    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == SCROLL_STATE_DRAGGING) {//被拖拽滚动
            View centerXChild;
            this.mNeedAdjust = true;
            if (getLayoutManager().canScrollHorizontally()) {
                centerXChild = RecyclerviewUtils.getCenterXChild(this);
            } else {
                centerXChild = RecyclerviewUtils.getCenterYChild(this);
            }
            this.mCurView = centerXChild;
            if (this.mCurView != null) {
                if (this.mHasCalledOnPageChanged) {
                    this.mPositionBeforeScroll = getChildLayoutPosition(this.mCurView);
                    this.mHasCalledOnPageChanged = false;
                }
                this.mFisrtLeftWhenDragging = this.mCurView.getLeft();
                this.mFirstTopWhenDragging = this.mCurView.getTop();
            } else {
                this.mPositionBeforeScroll = -1;
            }
            this.mTouchSpan = 0.0f;
        } else if (state == SCROLL_STATE_SETTLING) {//自动滚动
            this.mNeedAdjust = false;
            if (this.mCurView == null) {
                this.mTouchSpan = 0.0f;
            } else if (getLayoutManager().canScrollHorizontally()) {
                this.mTouchSpan = (float) (this.mCurView.getLeft() - this.mFisrtLeftWhenDragging);
            } else {
                this.mTouchSpan = (float) (this.mCurView.getTop() - this.mFirstTopWhenDragging);
            }
            this.mCurView = null;
        } else if (state == SCROLL_STATE_IDLE) {
            if (this.mNeedAdjust) {
                int targetPosition;
                if (getLayoutManager().canScrollHorizontally()) {
                    targetPosition = RecyclerviewUtils.getCenterXChildPosition(this);
                } else {
                    targetPosition = RecyclerviewUtils.getCenterYChildPosition(this);
                }
                if (this.mCurView != null) {
                    targetPosition = getChildAdapterPosition(this.mCurView);
                    if (getLayoutManager().canScrollHorizontally()) {
                        int spanX = this.mCurView.getLeft() - this.mFisrtLeftWhenDragging;
                        if (((float) spanX) > ((float) this.mCurView.getWidth()) * this.mTriggerOffset && this.mCurView.getLeft() >= this.mMaxLeftWhenDragging) {
                            targetPosition = !this.reverseLayout ? targetPosition - 1 : targetPosition + 1;
                        } else if (((float) spanX) < ((float) this.mCurView.getWidth()) * (-this.mTriggerOffset) && this.mCurView.getLeft() <= this.mMinLeftWhenDragging) {
                            targetPosition = !this.reverseLayout ? targetPosition + 1 : targetPosition - 1;
                        }
                    } else {
                        int spanY = this.mCurView.getTop() - this.mFirstTopWhenDragging;
                        if (((float) spanY) > ((float) this.mCurView.getHeight()) * this.mTriggerOffset && this.mCurView.getTop() >= this.mMaxTopWhenDragging) {
                            targetPosition = !this.reverseLayout ? targetPosition - 1 : targetPosition + 1;
                        } else if (((float) spanY) < ((float) this.mCurView.getHeight()) * (-this.mTriggerOffset) && this.mCurView.getTop() <= this.mMinTopWhenDragging) {
                            targetPosition = !this.reverseLayout ? targetPosition + 1 : targetPosition - 1;
                        }
                    }
                }
                smoothScrollToPosition(safeTargetPosition(targetPosition, this.mViewPagerAdapter.getItemCount()));
                this.mCurView = null;
            } else if (this.mSmoothScrollTargetPosition != this.mPositionBeforeScroll) {
                if (this.mOnPageChangedListeners != null) {
                    for (OnPageChangedListener onPageChangedListener : this.mOnPageChangedListeners) {
                        if (onPageChangedListener != null) {
                            onPageChangedListener.OnPageChanged(this.mPositionBeforeScroll, this.mSmoothScrollTargetPosition);
                        }
                    }
                }
                this.mHasCalledOnPageChanged = true;
                this.mPositionBeforeScroll = this.mSmoothScrollTargetPosition;
            }
            this.mMaxLeftWhenDragging = Integer.MIN_VALUE;
            this.mMinLeftWhenDragging = Integer.MAX_VALUE;
            this.mMaxTopWhenDragging = Integer.MIN_VALUE;
            this.mMinTopWhenDragging = Integer.MAX_VALUE;
        }
    }

    @NonNull
    protected RecyclerViewPagerAdapter ensureRecyclerViewPagerAdapter(Adapter adapter) {
        return adapter instanceof RecyclerViewPagerAdapter ? (RecyclerViewPagerAdapter) adapter : new RecyclerViewPagerAdapter(this, adapter);
    }

    public void addOnPageChangedListener(OnPageChangedListener listener) {
        if (this.mOnPageChangedListeners == null) {
            this.mOnPageChangedListeners = new ArrayList();
        }
        this.mOnPageChangedListeners.add(listener);
    }

    public void removeOnPageChangedListener(OnPageChangedListener listener) {
        if (this.mOnPageChangedListeners != null) {
            this.mOnPageChangedListeners.remove(listener);
        }
    }

    public void clearOnPageChangedListeners() {
        if (this.mOnPageChangedListeners != null) {
            this.mOnPageChangedListeners.clear();
        }
    }

    public void nextPage() {
        smoothScrollToPosition(getCurrentPosition() + 1);
    }

    public void prePage() {
        smoothScrollToPosition(getCurrentPosition() - 1);
    }

    public void backToCurrentPage() {
        smoothScrollToPosition(getCurrentPosition());
    }

    public int getCurrentPosition() {
        int curPosition;
        if (getLayoutManager().canScrollHorizontally()) {
            curPosition = RecyclerviewUtils.getCenterXChildPosition(this);
        } else {
            curPosition = RecyclerviewUtils.getCenterYChildPosition(this);
        }
        if (curPosition < 0) {
            return this.mSmoothScrollTargetPosition;
        }
        return curPosition;
    }

    private void adjustPositionX(int velocityX) {
        if (this.reverseLayout) {
            velocityX *= -1;
        }
        if (getChildCount() > 0) {
            int curPosition = RecyclerviewUtils.getCenterXChildPosition(this);
            int flingCount = getFlingCount(velocityX, (getWidth() - getPaddingLeft()) - getPaddingRight());
            int targetPosition = curPosition + flingCount;
            if (this.mSinglePageFling) {
                flingCount = Math.max(-1, Math.min(1, flingCount));
                if (flingCount == 0) {
                    targetPosition = curPosition;
                } else {
                    targetPosition = this.mPositionOnTouchDown + flingCount;
                }
            }
            targetPosition = Math.min(Math.max(targetPosition, 0), this.mViewPagerAdapter.getItemCount() - 1);
            if (targetPosition == curPosition && ((this.mSinglePageFling && this.mPositionOnTouchDown == curPosition) || !this.mSinglePageFling)) {
                View centerXChild = RecyclerviewUtils.getCenterXChild(this);
                if (centerXChild != null) {
                    if (this.mTouchSpan > (((float) centerXChild.getWidth()) * this.mTriggerOffset) * this.mTriggerOffset && targetPosition != 0) {
                        targetPosition = !this.reverseLayout ? targetPosition - 1 : targetPosition + 1;
                    } else if (this.mTouchSpan < ((float) centerXChild.getWidth()) * (-this.mTriggerOffset) && targetPosition != this.mViewPagerAdapter.getItemCount() - 1) {
                        targetPosition = !this.reverseLayout ? targetPosition + 1 : targetPosition - 1;
                    }
                }
            }
            smoothScrollToPosition(safeTargetPosition(targetPosition, this.mViewPagerAdapter.getItemCount()));
        }
    }

    protected void adjustPositionY(int velocityY) {
        if (this.reverseLayout) {
            velocityY *= -1;
        }
        if (getChildCount() > 0) {
            int curPosition = RecyclerviewUtils.getCenterYChildPosition(this);
            int flingCount = getFlingCount(velocityY, (getHeight() - getPaddingTop()) - getPaddingBottom());
            int targetPosition = curPosition + flingCount;
            if (this.mSinglePageFling) {
                flingCount = Math.max(-1, Math.min(1, flingCount));
                if (flingCount == 0) {
                    targetPosition = curPosition;
                } else {
                    targetPosition = this.mPositionOnTouchDown + flingCount;
                }
            }
            targetPosition = Math.min(Math.max(targetPosition, 0), this.mViewPagerAdapter.getItemCount() - 1);
            if (targetPosition == curPosition && ((this.mSinglePageFling && this.mPositionOnTouchDown == curPosition) || !this.mSinglePageFling)) {
                View centerYChild = RecyclerviewUtils.getCenterYChild(this);
                if (centerYChild != null) {
                    if (this.mTouchSpan > ((float) centerYChild.getHeight()) * this.mTriggerOffset && targetPosition != 0) {
                        targetPosition = !this.reverseLayout ? targetPosition - 1 : targetPosition + 1;
                    } else if (this.mTouchSpan < ((float) centerYChild.getHeight()) * (-this.mTriggerOffset) && targetPosition != this.mViewPagerAdapter.getItemCount() - 1) {
                        targetPosition = !this.reverseLayout ? targetPosition + 1 : targetPosition - 1;
                    }
                }
            }
            smoothScrollToPosition(safeTargetPosition(targetPosition, this.mViewPagerAdapter.getItemCount()));
        }
    }

    private int getFlingCount(int velocity, int cellSize) {
        if (velocity == 0) {
            return 0;
        }
        int sign = velocity > 0 ? 1 : -1;
        return (int) (((double) sign) * Math.ceil((double) (((((float) (velocity * sign)) * this.mFlingFactor) / ((float) cellSize)) - this.mTriggerOffset)));
    }

    private int safeTargetPosition(int position, int count) {
        if (position < 0) {
            return 0;
        }
        if (position >= count) {
            return count - 1;
        }
        return position;
    }
}
