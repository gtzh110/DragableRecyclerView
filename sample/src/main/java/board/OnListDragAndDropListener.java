package board;

import android.support.v7.widget.RecyclerView.ViewHolder;

public interface OnListDragAndDropListener {
    void onEndDrag(int i);

    void onStartDrag(ViewHolder viewHolder, int i);
}
