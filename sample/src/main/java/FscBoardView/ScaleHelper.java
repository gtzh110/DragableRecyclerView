package FscBoardView;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnChildAttachStateChangeListener;
import android.view.View;


import java.util.ArrayList;
import java.util.List;

import util.UiUtil;

public class ScaleHelper {
    private RecyclerView horizontalView;
    private boolean isInScaleMode;
    private Activity mActivity;
    private View mContentView;
    private float normalFlingFactor = 0.135f;
    private int scale = 2;
    private List<View> verticalViewList = new ArrayList();
    private int verticalWidth;

    public ScaleHelper(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public synchronized void startScaleModel() {
        if (!this.isInScaleMode) {
            this.isInScaleMode = true;
            int width = mActivity.getResources().getDisplayMetrics().widthPixels;
            int height = UiUtil.getContentHeight(mActivity);
            this.mContentView.getLayoutParams().width = scale * width;
            this.mContentView.getLayoutParams().height = scale * height;
            this.horizontalView.getLayoutParams().height = scale * height;
            this.horizontalView.getLayoutParams().width = scale * width;
            this.horizontalView.setScaleX(ServiceStageBoardActivity.FULL_SCALE / ((float) this.scale));
            this.horizontalView.setScaleY(ServiceStageBoardActivity.FULL_SCALE / ((float) this.scale));
            this.horizontalView.setPivotX(0.0f);
            this.horizontalView.setPivotY(0.0f);
            for (View view : this.verticalViewList) {
                this.verticalWidth = view.getWidth();
                view.getLayoutParams().width = view.getWidth();
                view.requestLayout();
            }
            if (this.horizontalView instanceof PagerRecyclerView) {
                this.normalFlingFactor = ((PagerRecyclerView) this.horizontalView).getFlingFactor();
                ((PagerRecyclerView) this.horizontalView).setFlingFactor(this.normalFlingFactor * 3.0f);
                ((PagerRecyclerView) this.horizontalView).setSinglePageFling(false);
            }
        }
    }

    public synchronized void stopScaleModel(int itemWidth) {
        if (this.isInScaleMode) {
            this.isInScaleMode = false;
            this.mContentView.getLayoutParams().width = RecyclerView.LayoutParams.MATCH_PARENT;
            this.mContentView.getLayoutParams().height = RecyclerView.LayoutParams.MATCH_PARENT;
            this.horizontalView.getLayoutParams().height = RecyclerView.LayoutParams.MATCH_PARENT;
            this.horizontalView.getLayoutParams().width = RecyclerView.LayoutParams.MATCH_PARENT;
            for (View view : this.verticalViewList) {
                view.getLayoutParams().width = itemWidth;
                view.requestLayout();
            }
            this.horizontalView.setScaleX(ServiceStageBoardActivity.FULL_SCALE);
            this.horizontalView.setScaleY(ServiceStageBoardActivity.FULL_SCALE);
            if (this.horizontalView instanceof PagerRecyclerView) {
                ((PagerRecyclerView) this.horizontalView).setFlingFactor(this.normalFlingFactor);
                ((PagerRecyclerView) this.horizontalView).setSinglePageFling(true);
                this.horizontalView.scrollToPosition(((PagerRecyclerView) this.horizontalView).getCurrentPosition());
            }
        }
    }

    private void addVerticalView(View view) {
//        if (!verticalViewList.contains(view)){
            this.verticalViewList.add(view);
//        }
        if (this.isInScaleMode) {
            view.getLayoutParams().width = this.verticalWidth;
        }
    }

    public void setContentView(View contentView) {
        this.mContentView = contentView;
    }

    public void setHorizontalView(RecyclerView horizontalView) {
        this.horizontalView = horizontalView;
        horizontalView.addOnChildAttachStateChangeListener(new OnChildAttachStateChangeListener() {
            public void onChildViewAttachedToWindow(View view) {
                ScaleHelper.this.addVerticalView(view);
            }

            public void onChildViewDetachedFromWindow(View view) {
            }
        });
    }
}
