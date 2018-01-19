package board;

import android.graphics.Canvas;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;

public class ListItemTouchHelperCallback extends Callback {
    private final RecyclerViewItemMoveListener mListener;

    public ListItemTouchHelperCallback(RecyclerViewItemMoveListener listener) {
        this.mListener = listener;
    }

    public boolean isLongPressDragEnabled() {
        return false;
    }

    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    public void onSwiped(ViewHolder viewHolder, int direction) {
    }

    public int getMovementFlags(RecyclerView recyclerView, ViewHolder viewHolder) {
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            return makeMovementFlags(15, 0);
        }
        return 0;
    }

    public boolean onMove(RecyclerView recyclerView, ViewHolder source, ViewHolder target) {
        if (source.getItemViewType() != target.getItemViewType()) {
            return false;
        }
        this.mListener.onItemMove(source.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    public int interpolateOutOfBoundsScroll(RecyclerView recyclerView, int viewSize, int viewSizeOutOfBounds, int totalSize, long msSinceStartScroll) {
        return super.interpolateOutOfBoundsScroll(recyclerView, (int) (((float) viewSize) / 0.2f), viewSizeOutOfBounds, totalSize, msSinceStartScroll * 4);
    }

    public void onSelectedChanged(ViewHolder viewHolder, int actionState) {
        if (actionState != 0 && (viewHolder instanceof RecyclerViewHolderMoveListener)) {
            RecyclerViewHolderMoveListener itemViewHolder = (RecyclerViewHolderMoveListener) viewHolder;
            int position = viewHolder.getAdapterPosition();
            if ((this.mListener instanceof RecyclerViewListsAdapter) && ((RecyclerViewListsAdapter) this.mListener).getItemViewType(position) == 0) {
                itemViewHolder.onItemSelected();
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    public void clearView(RecyclerView recyclerView, ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (viewHolder instanceof RecyclerViewHolderMoveListener) {
            ((RecyclerViewHolderMoveListener) viewHolder).onItemClear();
            this.mListener.onItemDrop(viewHolder.getAdapterPosition());
        }
    }

    public void onChildDraw(Canvas c, RecyclerView recyclerView, ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == 2) {
            viewHolder.itemView.setTranslationX(dX);
            viewHolder.itemView.setTranslationY(dY);
            return;
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
