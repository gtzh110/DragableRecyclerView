package FscBoardView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class DragLayout extends RelativeLayout {
    private DragHelper mDragHelper;

    public DragLayout(Context context) {
        super(context);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setDragHelper(DragHelper dragHelper) {
        this.mDragHelper = dragHelper;
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (this.mDragHelper == null || !this.mDragHelper.isDragging()) {
            return super.onInterceptTouchEvent(ev);
        }
        return true;
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP /*1*/:
            case MotionEvent.ACTION_CANCEL /*3*/:
                if (this.mDragHelper != null) {
                    this.mDragHelper.drop();
                    break;
                }
                break;
            case MotionEvent.ACTION_MOVE /*2*/:
                if (this.mDragHelper != null) {
                    this.mDragHelper.updateDraggingPosition(event.getRawX(), event.getRawY());
                    break;
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}
