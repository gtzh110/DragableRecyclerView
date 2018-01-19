package board;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.woxthebox.draglistview.sample.R;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerViewTasksAdapter extends Adapter<RecyclerViewTasksAdapter.ViewHolder> {
    private ProjectTaskBoardActivity mActivity;
    private List<String> mData;
    private int mDragPosition;
    private boolean mHideDragItem;
    private LayoutInflater mInflater;
    private boolean mOnBind = false;
//    private OnItemClickListener mOnItemClickListener;

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        TextView tv_title;

        public ViewHolder(View itemView, Activity activity) {
            super(itemView);
            this.tv_title = (TextView) itemView.findViewById(R.id.tv_title);

        }
    }

    public RecyclerViewTasksAdapter(ProjectTaskBoardActivity activity, List data) {
        this.mActivity = activity;
        this.mData = data;
        this.mInflater = LayoutInflater.from(activity);
    }

//    public void setOnItemClickListener(OnItemClickListener onitemClickListener) {
//        this.mOnItemClickListener = onitemClickListener;
//    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(this.mInflater.inflate(R.layout.recyclerview_item_taskboard_task, parent, false), this.mActivity);
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (position == this.mDragPosition && this.mHideDragItem) {
            holder.itemView.setVisibility(View.INVISIBLE);
        } else {
            holder.itemView.setVisibility(View.VISIBLE);
        }
//        if (this.mOnItemClickListener != null) {
//            holder.itemView.setOnClickListener(new OnClickListener() {
//                public void onClick(View v) {
////                    RecyclerViewTasksAdapter.this.mOnItemClickListener.onItemClick(position);
//                }
//            });
//        }
        holder.tv_title.setText(mData.get(position));
        holder.itemView.setOnLongClickListener(new OnLongClickListener() {
            public boolean onLongClick(View v) {
//                if (!NetUtils.isNetworkAvailable()) {
//                    ProjectUtil.showToast(RecyclerViewTasksAdapter.this.mActivity, (int) R.string.net_notconnected_cannot_move_task, 0);
//                } else if (Director.getInstance().hasPermission(Project.ACCESS_APPLICATIONS, RecyclerViewTasksAdapter.this.mActivity.projectId)) {
                v.setTag(mData.get(position));
                RecyclerViewTasksAdapter.this.mActivity.getDragHelper().drag(v, position);
//                }
                return true;
            }
        });

    }

    public int getItemCount() {
        return this.mData.size();
    }

    public void onDrag(int position) {
        this.mDragPosition = position;
        this.mHideDragItem = true;
        notifyItemChanged(position);
    }

    public void onDrop(int page, int position, Object tag) {
        this.mHideDragItem = false;
        notifyItemChanged(position);
        if (tag != null) {
//            this.mActivity.httpMoveTask(page, position, (Task) tag);
        }
    }

    public void onDragOut() {
        if (this.mDragPosition >= 0 && this.mDragPosition < this.mData.size()) {
            this.mData.remove(this.mDragPosition);
            notifyDataSetChanged();
            this.mDragPosition = -1;
        }
    }

    public void onDragIn(int position, Object tag) {
        String str = (String) tag;
        if (position > this.mData.size()) {
            position = this.mData.size();
        }
        this.mData.add(position, str);
        notifyItemInserted(position);
        this.mDragPosition = position;
        this.mHideDragItem = true;
    }

    public void updateDragItemVisibility(int position) {
        if (this.mDragPosition >= 0 && this.mDragPosition < this.mData.size() && position < this.mData.size() && this.mDragPosition != position) {
            if (Math.abs(this.mDragPosition - position) == 1) {
                notifyItemChanged(this.mDragPosition);
                Collections.swap(this.mData, this.mDragPosition, position);
                this.mDragPosition = position;
                notifyItemChanged(position);
                return;
            }
            notifyItemChanged(this.mDragPosition);
            int i;
            if (this.mDragPosition > position) {
                for (i = this.mDragPosition; i > position; i--) {
                    Collections.swap(this.mData, i, i - 1);
                    notifyItemChanged(i);
                }
            } else {
                for (i = this.mDragPosition; i < position; i++) {
                    Collections.swap(this.mData, i, i + 1);
                    notifyItemChanged(i);
                }
            }
            this.mDragPosition = position;
            notifyItemChanged(position);
        }
    }

//    private OnCheckedChangeListener onCheckedChange(final int position) {
//        return new OnCheckedChangeListener() {
//            public void onCheckedChanged(boolean isChecked) {
//                if (!RecyclerViewTasksAdapter.this.mOnBind) {
//                    Task task = (Task) RecyclerViewTasksAdapter.this.mData.get(position);
//                    task.setCompleted(isChecked);
//                    TaskManager.getInstance().updateCompleteInCache(task.getTaskId(), isChecked);
//                    RecyclerViewTasksAdapter.this.notifyItemChanged(position);
//                    DashBoardFragment.ifRefresh = true;
//                    TaskManager.getInstance().markTaskAsCompleted(isChecked, task.getTaskId(), task.getProjectId(), new WebApiResponse() {
//                        public void onSuccess() {
//                        }
//                    });
//                }
//            }
//        };
//    }

//    private void setLabelsLayout(ViewHolder holder, Task task) {
//        for (String labelId : task.getLabels()) {
//            Label label = LabelManager.getInstance().fetchLabelFromCacheByLabelId(labelId);
//            TextView textView = (TextView) holder.labelViews.get(labelId);
//            if (!(textView == null || label == null)) {
//                LabelsManager.setLabel(this.mActivity, textView, label.getLabelName(), new Elabel(label).getLabelColorResId());
//                textView.setVisibility(0);
//                holder.flowLayout.addView(textView);
//            }
//        }
//    }

//    private void setSubscribersLayout(ViewHolder holder, Task task) {
//        LayoutParams params = (LayoutParams) holder.members.getLayoutParams();
//        params.setReverse(true);
//        holder.members.setLayoutParams(params);
//        holder.members.removeAllViews();
//        String[] memberIds = task.getSubscribers();
//        for (String memberId : memberIds) {
//            User user = UserManager.getInstance().fetchUserFromCacheByUid(memberId);
//            if (user != null) {
//                ImageView view = (ImageView) holder.avatarViews.get(user.getUid());
//                if (view != null) {
//                    BitmapUtils.showAvatar(this.mActivity, view, user.getDisplayName(), user.getAvatarUrl(), CustomViewFactory.avatarSmall);
//                    view.setVisibility(0);
//                    holder.members.addView(view);
//                }
//            }
//        }
//        if (memberIds.length == 0) {
//            holder.members.setVisibility(8);
//            return;
//        }
//        holder.members.setVisibility(0);
//        holder.flowLayout.addView(holder.members);
//    }
}
