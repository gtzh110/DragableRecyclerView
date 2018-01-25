package FscBoardView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

import com.woxthebox.draglistview.sample.R;

import java.util.Timer;
import java.util.TimerTask;

public class DragHelper {
    private static final int HORIZONTAL_SCROLL_PERIOD = 40;
    private static final int HORIZONTAL_STEP = 30;
    private static final int VERTICAL_SCROLL_PERIOD = 20;
    private static final int VERTICAL_STEP = 10;
    private boolean confirmOffset = false;
    private int downScrollBounce;
    private boolean isDragging = false;
    private int leftScrollBounce;
    private float mBornLocationX;
    private float mBornLocationY;
    private RecyclerView mCurrentVerticalView;
    private ImageView mDragImageView;
    private PagerRecyclerView mHorizontalRecyclerView;
    private Timer mHorizontalScrollTimer = new Timer();
    private TimerTask mHorizontalScrollTimerTask;
    private int mPagerPosition = -1;
    private int mPosition = -1;
    private Timer mVerticalScrollTimer = new Timer();
    private TimerTask mVerticalScrollTimerTask;
    private WindowManager mWindowManager;
    private LayoutParams mWindowParams;
    private int offsetX;
    private int offsetY;
    private int rightScrollBounce;
    private Object tag;
    private int upScrollBounce;
    private Activity activity;

    public DragHelper(Activity activity) {
        this.activity = activity;
        this.mWindowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        this.mWindowParams = new LayoutParams();
        this.mWindowParams.type = 2;
        this.mWindowParams.flags = 262944;
        this.mWindowParams.alpha = ServiceStageBoardActivity.FULL_SCALE;
        this.mWindowParams.format = -3;
        this.mWindowParams.width = -2;
        this.mWindowParams.height = -2;
        this.mWindowParams.gravity = 51;
        this.mWindowParams.x = 0;
        this.mWindowParams.y = 0;
        this.mDragImageView = new ImageView(activity);
        this.mDragImageView.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        this.mDragImageView.setPadding(VERTICAL_STEP, VERTICAL_STEP, VERTICAL_STEP, VERTICAL_STEP);
        this.mDragImageView.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 4 || event.getAction() == 0) {
                    DragHelper.this.drop();
                }
                return false;
            }
        });
    }

    public void bindHorizontalRecyclerView(@NonNull PagerRecyclerView view) {
        this.mHorizontalRecyclerView = view;
        if (!(view.getLayoutManager() instanceof LinearLayoutManager)) {
            throw new RuntimeException("LayoutManager must be LinearLayoutManager");
        }
    }

    public boolean isDragging() {
        return this.isDragging;
    }

    public void drag(View dragger, int position) {
        dragger.destroyDrawingCache();
        dragger.setDrawingCacheEnabled(true);
        Bitmap bitmap = dragger.getDrawingCache();
        if (bitmap != null && !bitmap.isRecycled()) {
            this.mDragImageView.setImageBitmap(bitmap);
            this.mDragImageView.setRotation(1.5f);
            this.mDragImageView.setAlpha(0.9f);
//            this.mDragImageView.setBackgroundColor(Color.parseColor("#F2F2F2"));
            this.isDragging = true;
            this.tag = dragger.getTag();
            int dragPage = this.mHorizontalRecyclerView.getCurrentPosition();
            RecyclerViewListsAdapter.ViewHolder holder = (RecyclerViewListsAdapter.ViewHolder) this.mHorizontalRecyclerView.findViewHolderForAdapterPosition(dragPage);
            if (!(holder == null || holder.itemView == null || holder.getItemViewType() == 1)) {
                this.mCurrentVerticalView = (RecyclerView) holder.itemView.findViewById(R.id.task_list_rv);
                this.mPagerPosition = dragPage;
            }
            getTargetHorizontalRecyclerViewScrollBoundaries();
            getTargetVerticalRecyclerViewScrollBoundaries();
            int[] location = new int[2];
            dragger.getLocationOnScreen(location);
            this.mWindowParams.x = location[0];
            this.mWindowParams.y = location[1];
            this.mBornLocationX = (float) location[0];
            this.mBornLocationY = (float) location[1];
            this.confirmOffset = false;
            this.mPosition = position;
            this.mWindowManager.addView(this.mDragImageView, this.mWindowParams);
            getCurrentAdapter().onDrag(position);
        }
    }

    public void drop() {
        if (this.isDragging) {
            this.mWindowManager.removeView(this.mDragImageView);
            this.isDragging = false;
            if (this.mVerticalScrollTimerTask != null) {
                this.mVerticalScrollTimerTask.cancel();
            }
            if (this.mHorizontalScrollTimerTask != null) {
                this.mHorizontalScrollTimerTask.cancel();
            }
            if (this.mHorizontalRecyclerView != null) {
                this.mHorizontalRecyclerView.backToCurrentPage();
            }
            getCurrentAdapter().onDrop(this.mPagerPosition, this.mPosition, this.tag);
        }
    }

    public void updateDraggingPosition(float rowX, float rowY) {
        if (this.mWindowManager != null && this.mWindowParams != null) {
            if (!this.confirmOffset) {
                calculateOffset(rowX, rowY);
            }
            if (this.isDragging) {
                this.mWindowParams.x = (int) (rowX - ((float) this.offsetX));
                this.mWindowParams.y = (int) (rowY - ((float) this.offsetY));
                this.mWindowManager.updateViewLayout(this.mDragImageView, this.mWindowParams);//这里应该是控制被拖拽itemView的移动

                updateSlidingVerticalRecyclerView(rowX, rowY);
                findViewPosition(rowX, rowY);
                recyclerViewScrollHorizontal((int) rowX, (int) rowY);
                recyclerViewScrollVertical((int) rowX, (int) rowY);
            }
        }
    }

    private void calculateOffset(float x, float y) {
        this.offsetX = (int) Math.abs(x - this.mBornLocationX);
        this.offsetY = (int) Math.abs(y - this.mBornLocationY);
        this.confirmOffset = true;
    }

    private void getTargetVerticalRecyclerViewScrollBoundaries() {
        int[] location = new int[2];
        this.mCurrentVerticalView.getLocationOnScreen(location);
        this.upScrollBounce = location[1] + 150;
        this.downScrollBounce = (location[1] + this.mCurrentVerticalView.getHeight()) - 150;
    }

    private void getTargetHorizontalRecyclerViewScrollBoundaries() {
        this.leftScrollBounce = 200;
        this.rightScrollBounce = activity.getWindowManager().getDefaultDisplay().getWidth() - 200;
    }

    private void recyclerViewScrollHorizontal(final int x, final int y) {
        if (this.mHorizontalScrollTimerTask != null) {
            this.mHorizontalScrollTimerTask.cancel();
        }
        if (x > this.rightScrollBounce) {
            this.mHorizontalScrollTimerTask = new TimerTask() {
                public void run() {
                    DragHelper.this.mHorizontalRecyclerView.post(new Runnable() {
                        public void run() {
                            DragHelper.this.mHorizontalRecyclerView.scrollBy(DragHelper.HORIZONTAL_STEP, 0);
                            DragHelper.this.findViewPosition((float) x, (float) y);
                        }
                    });
                }
            };
            this.mHorizontalScrollTimer.schedule(this.mHorizontalScrollTimerTask, 0, 40);
        } else if (x < this.leftScrollBounce) {
            this.mHorizontalScrollTimerTask = new TimerTask() {
                public void run() {
                    DragHelper.this.mHorizontalRecyclerView.post(new Runnable() {
                        public void run() {
                            DragHelper.this.mHorizontalRecyclerView.scrollBy(-30, 0);
                            DragHelper.this.findViewPosition((float) x, (float) y);
                        }
                    });
                }
            };
            this.mHorizontalScrollTimer.schedule(this.mHorizontalScrollTimerTask, 0, 40);
        }
    }

    private void recyclerViewScrollVertical(final int x, final int y) {
        if (this.mVerticalScrollTimerTask != null) {
            this.mVerticalScrollTimerTask.cancel();
        }
        if (y > this.downScrollBounce) {
            this.mVerticalScrollTimerTask = new TimerTask() {
                public void run() {
                    DragHelper.this.mCurrentVerticalView.post(new Runnable() {
                        public void run() {
                            DragHelper.this.mCurrentVerticalView.scrollBy(0, DragHelper.VERTICAL_STEP);
                            DragHelper.this.findViewPosition((float) x, (float) y);
                        }
                    });
                }
            };
            this.mVerticalScrollTimer.schedule(this.mVerticalScrollTimerTask, 0, 20);
        } else if (y < this.upScrollBounce) {
            this.mVerticalScrollTimerTask = new TimerTask() {
                public void run() {
                    DragHelper.this.mCurrentVerticalView.post(new Runnable() {
                        public void run() {
                            DragHelper.this.mCurrentVerticalView.scrollBy(0, -10);
                            DragHelper.this.findViewPosition((float) x, (float) y);
                        }
                    });
                }
            };
            this.mVerticalScrollTimer.schedule(this.mVerticalScrollTimerTask, 0, 20);
        }
    }

    private void updateSlidingVerticalRecyclerView(float x, float y) {
        int newPage = getHorizontalCurrentPosition(x, y);
        if (this.mPagerPosition != newPage) {
            RecyclerView.ViewHolder holder =  this.mHorizontalRecyclerView.findViewHolderForAdapterPosition(newPage);
            if (holder != null && holder.itemView != null && holder.getItemViewType() != 1) {
                getCurrentAdapter().onDragOut();
                this.mCurrentVerticalView = (RecyclerView) holder.itemView.findViewById(R.id.task_list_rv);
                this.mPagerPosition = newPage;
                getCurrentAdapter().onDragIn(this.mPosition, this.tag);
            }
        }
    }

    private void findViewPosition(float rowX, float rowY) {
        int[] location = new int[2];
        this.mCurrentVerticalView.getLocationOnScreen(location);
        int newPosition = this.mCurrentVerticalView.getChildAdapterPosition(this.mCurrentVerticalView.findChildViewUnder(rowX - ((float) location[0]), rowY - ((float) location[1])));
        if (newPosition != -1) {
            getCurrentAdapter().updateDragItemVisibility(this.mPosition);
            if (this.mPosition != newPosition) {
                this.mPosition = newPosition;
            }
        }
    }

    private RecyclerViewTasksAdapter getCurrentAdapter() {
        return (RecyclerViewTasksAdapter) this.mCurrentVerticalView.getAdapter();
    }

    private int getHorizontalCurrentPosition(float rowX, float rowY) {
        int[] location = new int[2];
        this.mHorizontalRecyclerView.getLocationOnScreen(location);
        View child = this.mHorizontalRecyclerView.findChildViewUnder(rowX - ((float) location[0]), rowY - ((float) location[1]));
        if (child != null) {
            return this.mHorizontalRecyclerView.getChildAdapterPosition(child);
        }
        return this.mPagerPosition;
    }
}
