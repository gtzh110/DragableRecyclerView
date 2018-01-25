package FscBoardView;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.AdapterDataObserver;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;

public class RecyclerViewPagerAdapter<VH extends ViewHolder> extends Adapter<VH> {
    Adapter<VH> mAdapter;
    private final PagerRecyclerView mViewPager;

    public RecyclerViewPagerAdapter(PagerRecyclerView viewPager, Adapter<VH> adapter) {
        this.mAdapter = adapter;
        this.mViewPager = viewPager;
        setHasStableIds(this.mAdapter.hasStableIds());
    }

    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return this.mAdapter.onCreateViewHolder(parent, viewType);
    }

    public void registerAdapterDataObserver(AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
        this.mAdapter.registerAdapterDataObserver(observer);
    }

    public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
        super.unregisterAdapterDataObserver(observer);
        this.mAdapter.unregisterAdapterDataObserver(observer);
    }

    public void onViewRecycled(VH holder) {
        super.onViewRecycled(holder);
        this.mAdapter.onViewRecycled(holder);
    }

    public boolean onFailedToRecycleView(VH holder) {
        return this.mAdapter.onFailedToRecycleView(holder);
    }

    public void onViewAttachedToWindow(VH holder) {
        super.onViewAttachedToWindow(holder);
        this.mAdapter.onViewAttachedToWindow(holder);
    }

    public void onViewDetachedFromWindow(VH holder) {
        super.onViewDetachedFromWindow(holder);
        this.mAdapter.onViewDetachedFromWindow(holder);
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mAdapter.onAttachedToRecyclerView(recyclerView);
    }

    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.mAdapter.onDetachedFromRecyclerView(recyclerView);
    }

    public void onBindViewHolder(VH holder, int position) {
//        LayoutParams lp;
        this.mAdapter.onBindViewHolder(holder, position);
//        View itemView = holder.itemView;

    }

    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
        this.mAdapter.setHasStableIds(hasStableIds);
    }

    public int getItemCount() {
        return this.mAdapter.getItemCount();
    }

    public int getItemViewType(int position) {
        return this.mAdapter.getItemViewType(position);
    }

    public long getItemId(int position) {
        return this.mAdapter.getItemId(position);
    }
}
