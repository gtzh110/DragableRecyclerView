package board;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.woxthebox.draglistview.sample.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import util.UiUtil;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class RecyclerViewListsAdapter extends Adapter<RecyclerViewListsAdapter.ViewHolder> implements RecyclerViewItemMoveListener {
    public static final int ON_COPY_LIST_SUCCESS = 2;
    public static final int ON_FAILED = 0;
    public static final int ON_SUBSCRIBE_LIST_SUCCESS = 3;
    public static final int ON_SUCCESS = 1;
    public static final int TYPE_DATA = 0;
    public static final int TYPE_FOOTER = 1;
    private ProjectTaskBoardActivity mContext;
    private List<String> mData;
    //    private Map<String, List<String>> mDataMap;
    private View mFooterView;
    private LayoutInflater mInflater;
    private PopupWindow popupWindow;
    public int itemWidth;


    public static class ViewHolder extends RecyclerView.ViewHolder implements RecyclerViewHolderMoveListener {
        ImageView btn_more;
        RelativeLayout layout_title;
        RecyclerView rv_tasks;
        TextView tv_title;

        public ViewHolder(View convertView, int itemType) {
            super(convertView);
            if (itemType == RecyclerViewListsAdapter.TYPE_FOOTER) {

                return;
            }
            this.tv_title = (TextView) convertView.findViewById(R.id.tv_stage_name);
            this.layout_title = (RelativeLayout) convertView.findViewById(R.id.layout_list_name);
            this.rv_tasks = (RecyclerView) convertView.findViewById(R.id.task_list_rv);
            this.btn_more = (ImageView) convertView.findViewById(R.id.btn_more);
        }

        public void onItemSelected() {
            this.itemView.setAlpha(0.8f);
            this.itemView.setRotation(0.8f);
        }

        public void onItemClear() {
            this.itemView.setAlpha(ProjectTaskBoardActivity.FULL_SCALE);
            this.itemView.setRotation(0.0f);
        }
    }

    public RecyclerViewListsAdapter(ProjectTaskBoardActivity context, List<String> data) {
        this.mContext = context;
        this.mData = data;
//        this.mDataMap = dataMap;
        this.mInflater = LayoutInflater.from(context);
        itemWidth = UiUtil.getScreenWidth(context) - UiUtil.dp2px(context, 50);
    }

    public View getFooterView() {
        return this.mFooterView;
    }

    public void setFooterView(View view) {
        this.mFooterView = view;
        notifyItemInserted(getItemCount() - 1);
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (this.mFooterView == null || viewType != TYPE_FOOTER) {
            View itemView = this.mInflater.inflate(R.layout.recyclerview_item_taskboard_list, parent, false);
            ViewGroup.LayoutParams lp = itemView.getLayoutParams();
            lp.width = itemWidth;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            itemView.setLayoutParams(lp);
            return new ViewHolder(itemView, TYPE_DATA);
        }

        if (mFooterView.getLayoutParams() == null) {
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(itemWidth, -2);
            mFooterView.setLayoutParams(lp);
        }
        return new ViewHolder(this.mFooterView, TYPE_FOOTER);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_DATA:
//                List list = this.mData.get(position);


                holder.tv_title.setText(mData.get(position));
                List<String> taskList = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    taskList.add(mData.get(position) + i);
                }
                holder.rv_tasks.setLayoutManager(new LinearLayoutManager(this.mContext));
//                holder.rv_tasks.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 900));
//                holder.rv_tasks.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                RecyclerViewTasksAdapter tasksAdapter = new RecyclerViewTasksAdapter(mContext, taskList);
                holder.rv_tasks.setAdapter(tasksAdapter);
                setOnTitleDrag(holder);
//                setOnItemClick(taskList, list.getProjectId(), tasksAdapter);
//                setOnClickAddTask(holder);
//                setOnAddTaskKeyboardListener(holder, list);
                return;
            case TYPE_FOOTER:
//                setOnClickAddList(holder);
//                setOnAddListOperations(holder);
//                setOnAddListEditTextFocusChanged(holder);
                return;
            default:
                return;
        }
    }


//    private void lambda$setButtonMore$0(com.worktilecore.core.task.List l, View v) {
//        showPopupWindow(v, l);
//    }

//    private void setOnClickAddList(ViewHolder holder) {
//        this.mFooterView.setOnClickListener(RecyclerViewListsAdapter$$Lambda$2.lambdaFactory$(this, holder));
//    }

//    private /* synthetic */ void lambda$setOnClickAddList$1(ViewHolder holder, View v) {
//        holder.cv_add.setVisibility(8);
//        holder.cv_edit.setVisibility(TYPE_DATA);
//        holder.et_title.requestFocus();
//        showSoftInput();
//    }

//    private void setOnAddListOperations(ViewHolder holder) {
//        holder.tv_save.setOnClickListener(RecyclerViewListsAdapter$$Lambda$3.lambdaFactory$(this, holder));
//        holder.tv_cancel.setOnClickListener(RecyclerViewListsAdapter$$Lambda$4.lambdaFactory$(this, holder));
//    }

//    private void lambda$setOnAddListOperations$2(ViewHolder holder, View v) {
//        String name = holder.et_title.getText().toString().trim();
//        if (!TextUtils.isEmpty(name)) {
//            holder.et_title.setText(BuildConfig.VERSION_NAME);
//            httpAddList(this.mContext.projectId, name);
//        }
//    }

//    private void lambda$setOnAddListOperations$3(ViewHolder holder, View v) {
//        holder.cv_edit.setVisibility(8);
//        holder.cv_add.setVisibility(TYPE_DATA);
//        holder.et_title.setText(BuildConfig.VERSION_NAME);
//        hideSoftInput(this.mContext, holder.et_title);
//    }

//    private void setOnAddListEditTextFocusChanged(ViewHolder holder) {
//        holder.et_title.setOnFocusChangeListener(RecyclerViewListsAdapter$$Lambda$5.lambdaFactory$(this, holder));
//    }

//    private  void lambda$setOnAddListEditTextFocusChanged$4(ViewHolder holder, View v, boolean hasFocus) {
//        if (!hasFocus) {
//            holder.cv_edit.setVisibility(8);
//            holder.cv_add.setVisibility(TYPE_DATA);
//            holder.et_title.setText(BuildConfig.VERSION_NAME);
//            hideSoftInput(this.mContext, holder.et_title);
//        }
//    }

//    private void lambda$setOnClickAddTask$5(ViewHolder holder, View v) {
//        enterEditTaskAdding(holder);
//    }

//    private void setOnClickAddTask(ViewHolder holder) {
//        holder.layout_addTask.setOnClickListener(RecyclerViewListsAdapter$$Lambda$6.lambdaFactory$(this, holder));
//    }

//    private void setOnAddTaskKeyboardListener(ViewHolder holder, com.worktilecore.core.task.List list) {
//        holder.et_title.setImeOptions(Utils.SELECTED_ALPHA_THEME_DARK);
//        holder.et_title.setOnEditorActionListener(RecyclerViewListsAdapter$$Lambda$7.lambdaFactory$(this, holder, list));
//    }

//    private /* synthetic */ boolean lambda$setOnAddTaskKeyboardListener$6(ViewHolder holder, com.worktilecore.core.task.List list, TextView v, int actionId, KeyEvent event) {
//        if (actionId != Utils.SELECTED_ALPHA_THEME_DARK) {
//            return false;
//        }
//        String name = holder.et_title.getText().toString().trim();
//        if (!TextUtils.isEmpty(name)) {
//            holder.et_title.setText(BuildConfig.VERSION_NAME);
//            httpAddTask(holder, list.getListId(), list.getProjectId(), name);
//        }
//        return true;
//    }

//    private void setOnAddTaskOperations(ViewHolder holder, com.worktilecore.core.task.List list) {
//        holder.tv_save.setOnClickListener(RecyclerViewListsAdapter$$Lambda$8.lambdaFactory$(this, holder, list));
//        holder.tv_cancel.setOnClickListener(RecyclerViewListsAdapter$$Lambda$9.lambdaFactory$(this, holder));
//    }

//    private void lambda$setOnAddTaskOperations$7(ViewHolder holder, com.worktilecore.core.task.List list, View v) {
//        String name = holder.et_title.getText().toString().trim();
//        if (!TextUtils.isEmpty(name)) {
//            holder.et_title.setText(BuildConfig.VERSION_NAME);
//            httpAddTask(holder, list.getListId(), list.getProjectId(), name);
//        }
//    }

//    private void lambda$setOnAddTaskOperations$8(ViewHolder holder, View v) {
//        exitEditTaskAdding(holder);
//    }

//    private void setOnAddTaskEditTextFocusChanged(ViewHolder holder) {
//        holder.et_title.setOnFocusChangeListener(RecyclerViewListsAdapter$$Lambda$10.lambdaFactory$(this, holder));
//    }

//    private /* synthetic */ void lambda$setOnAddTaskEditTextFocusChanged$9(ViewHolder holder, View v, boolean hasFocus) {
//        if (!hasFocus) {
//            exitEditTaskAdding(holder);
//        }
//    }

//    private void enterEditTaskAdding(ViewHolder holder) {
//        holder.tv_add_task.setVisibility(8);
//        holder.btn_add.setVisibility(TYPE_DATA);
//        holder.et_title.requestFocus();
//        showSoftInput();
//    }

//    private void goOnEditTaskAdding(ViewHolder holder) {
//        holder.et_title.setText(BuildConfig.VERSION_NAME);
//        new Handler().postDelayed(RecyclerViewListsAdapter$$Lambda$11.lambdaFactory$(holder), 200);
//    }

//    private static /* synthetic */ void lambda$goOnEditTaskAdding$10(ViewHolder holder) {
//        int taskDataCount = holder.rv_tasks.getAdapter().getItemCount();
//        if (taskDataCount > 0) {
//            holder.rv_tasks.smoothScrollToPosition(taskDataCount - 1);
//        }
//    }

//    private void exitEditTaskAdding(ViewHolder holder) {
//        holder.tv_add_task.setVisibility(TYPE_DATA);
//        holder.btn_add.setVisibility(8);
//        holder.et_title.setText(BuildConfig.VERSION_NAME);
//        hideSoftInput(this.mContext, holder.et_title);
//    }

    private void showSoftInput() {
        ((InputMethodManager) this.mContext.getSystemService(INPUT_METHOD_SERVICE)).toggleSoftInput(TYPE_DATA, ON_COPY_LIST_SUCCESS);
    }

    private void hideSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, ON_COPY_LIST_SUCCESS);
        imm.hideSoftInputFromWindow(view.getWindowToken(), TYPE_DATA);
    }

    private void setOnTitleDrag(final ViewHolder holder) {
        holder.layout_title.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mContext.onStartDrag(holder, holder.getAdapterPosition());
                return true;
            }
        });
    }

//    private boolean lambda$setOnTitleDrag$11(ViewHolder holder, View v) {
//        if (Director.getInstance().hasPermission(Project.ACCESS_APPLICATIONS, this.mContext.projectId)) {
//            this.mContext.onStartDrag(holder, holder.getAdapterPosition());
//        }
//        return true;
//    }

//    private void setOnItemClick(List<Task> tasks, String projectId, RecyclerViewTasksAdapter adapter) {
//        adapter.setOnItemClickListener(RecyclerViewListsAdapter$$Lambda$13.lambdaFactory$(this, tasks, projectId));
//    }

//    private  void lambda$setOnItemClick$12(List tasks, String projectId, int position) {
//        Intent intent = new Intent();
//        intent.setClass(this.mContext, TaskDetailsActivity.class);
//        intent.putExtra(HbCodecBase.type, TYPE_FOOTER);
//        intent.putExtra("taskId", ((Task) tasks.get(position)).getTaskId());
//        intent.putExtra("projectId", projectId);
//        this.mContext.startActivityAnim(intent);
//    }

//    private void fetchTasksFromCacheAndNotify(ViewHolder holder) {
//        RecyclerViewTasksAdapter tasksAdapter = (RecyclerViewTasksAdapter) holder.rv_tasks.getAdapter();
//        String listId = ((com.worktilecore.core.task.List) this.mData.get(holder.getAdapterPosition())).getListId();
//        List<Task> tasks = (List) this.mDataMap.get(listId);
//        tasks.clear();
//        tasks.addAll(Arrays.asList(TaskManager.getInstance().fetchTasksFromCacheByListId(listId)));
//        tasksAdapter.notifyDataSetChanged();
//    }

//    private int createPositionForList() {
//        if (this.mData.size() > 0) {
//            return (((com.worktilecore.core.task.List) this.mData.get(this.mData.size() - 1)).getPosition() + Constants.posAdd) + TYPE_FOOTER;
//        }
//        return Constants.posAdd + TYPE_FOOTER;
//    }

    private void dismissPopupWindow(View button) {
        button.setSelected(false);
        if (this.popupWindow != null && this.popupWindow.isShowing()) {
            this.popupWindow.dismiss();
        }
    }

//    @TargetApi(21)
//    private void showPopupWindow(View button, com.worktilecore.core.task.List list) {
//        button.setSelected(true);
//        View layout_popupWindow = View.inflate(this.mContext, R.layout.layout_list_operations, null);
//        if (this.popupWindow == null) {
//            this.popupWindow = new PopupWindow(layout_popupWindow, -2, -2);
//            this.popupWindow.setFocusable(true);
//            this.popupWindow.setOutsideTouchable(true);
//            this.popupWindow.setAnimationStyle(R.style.popAnim);
//            this.popupWindow.setElevation(this.mContext.getResources().getDimension(R.dimen.popwindow_elevation));
//            this.popupWindow.setBackgroundDrawable(this.mContext.getResources().getDrawable(R.drawable.bg_popwindow));
//        }
//        this.popupWindow.getContentView().findViewById(R.id.tv1).setOnClickListener(onClick(button, list));
//        this.popupWindow.getContentView().findViewById(R.id.tv2).setOnClickListener(onClick(button, list));
//        TextView tv_subscribe = (TextView) this.popupWindow.getContentView().findViewById(R.id.tv3);
//        if (list.isSubscribed()) {
//            tv_subscribe.setText(R.string.unsubscribe);
//        } else {
//            tv_subscribe.setText(R.string.subscribe);
//        }
//        tv_subscribe.setOnClickListener(onClick(button, list));
//        this.popupWindow.getContentView().findViewById(R.id.tv4).setOnClickListener(onClick(button, list));
//        this.popupWindow.getContentView().findViewById(R.id.tv5).setOnClickListener(onClick(button, list));
//        this.popupWindow.getContentView().findViewById(R.id.tv6).setOnClickListener(onClick(button, list));
//        this.popupWindow.setOnDismissListener(RecyclerViewListsAdapter$$Lambda$14.lambdaFactory$(button));
//        this.popupWindow.showAsDropDown(button, -(UiUtil.getViewMeasuredWidthAndHeight(layout_popupWindow)[TYPE_DATA] - button.getWidth()), TYPE_DATA);
//    }

//    private OnClickListener onClick(final View operatButton, final com.worktilecore.core.task.List l) {
//        return new OnClickListener() {
//            public void onClick(View v) {
//                final NetHandler handler = new NetHandler(RecyclerViewListsAdapter.this);
//                RecyclerViewListsAdapter.this.dismissPopupWindow(operatButton);
//                String pid = l.getProjectId();
//                String listId = l.getListId();
//                switch (v.getId()) {
//                    case R.id.tv1 /*2131558785*/:
//                        RecyclerViewListsAdapter.this.mContext.showProgress(true);
//                        ListManager.getInstance().archiveCompletedTask(pid, listId, new WebApiResponse() {
//                            public void onSuccess() {
//                                handler.sendEmptyMessage(RecyclerViewListsAdapter.TYPE_FOOTER);
//                            }
//
//                            public boolean onFailure(String error) {
//                                handler.sendEmptyMessage(RecyclerViewListsAdapter.TYPE_DATA);
//                                return super.onFailure(error);
//                            }
//                        });
//                        return;
//                    case R.id.tv2 /*2131558786*/:
//                        RecyclerViewListsAdapter.this.showRenameListDialog(l);
//                        return;
//                    case R.id.tv3 /*2131558787*/:
//                        RecyclerViewListsAdapter.this.mContext.showProgress(true);
//                        if (l.isSubscribed()) {
//                            ListManager.getInstance().unSubscribeList(pid, listId, new WebApiResponse() {
//                                public void onSuccess() {
//                                    Message message = Message.obtain();
//                                    message.what = RecyclerViewListsAdapter.ON_SUBSCRIBE_LIST_SUCCESS;
//                                    message.arg1 = R.string.unsubscribe_success;
//                                    handler.sendMessage(message);
//                                }
//
//                                public boolean onFailure(String error) {
//                                    handler.sendEmptyMessage(RecyclerViewListsAdapter.TYPE_DATA);
//                                    return super.onFailure(error);
//                                }
//                            });
//                            return;
//                        } else {
//                            ListManager.getInstance().subscribeList(pid, listId, new WebApiResponse() {
//                                public void onSuccess() {
//                                    Message message = Message.obtain();
//                                    message.what = RecyclerViewListsAdapter.ON_SUBSCRIBE_LIST_SUCCESS;
//                                    message.arg1 = R.string.subscribe_success;
//                                    handler.sendMessage(message);
//                                }
//
//                                public boolean onFailure(String error) {
//                                    handler.sendEmptyMessage(RecyclerViewListsAdapter.TYPE_DATA);
//                                    return super.onFailure(error);
//                                }
//                            });
//                            return;
//                        }
//                    case R.id.tv4 /*2131558788*/:
//                        RecyclerViewListsAdapter.this.showCopyListDialog(pid, l);
//                        return;
//                    case R.id.tv5 /*2131558789*/:
//                        RecyclerViewListsAdapter.this.mContext.showProgress(true);
//                        ListManager.getInstance().archiveList(listId, pid, new WebApiResponse() {
//                            public void onSuccess() {
//                                handler.sendEmptyMessage(RecyclerViewListsAdapter.TYPE_FOOTER);
//                            }
//
//                            public boolean onFailure(String error) {
//                                handler.sendEmptyMessage(RecyclerViewListsAdapter.TYPE_DATA);
//                                return super.onFailure(error);
//                            }
//                        });
//                        return;
//                    case R.id.tv6 /*2131558790*/:
//                        RecyclerViewListsAdapter.this.mContext.showProgress(true);
//                        ListManager.getInstance().removeList(pid, listId, new WebApiResponse() {
//                            public void onSuccess() {
//                                handler.sendEmptyMessage(RecyclerViewListsAdapter.TYPE_FOOTER);
//                            }
//
//                            public boolean onFailure(String error) {
//                                handler.sendEmptyMessage(RecyclerViewListsAdapter.TYPE_DATA);
//                                return super.onFailure(error);
//                            }
//                        });
//                        return;
//                    default:
//                        return;
//                }
//            }
//        };
//    }

//    private void showCopyListDialog(String pid, com.worktilecore.core.task.List list) {
//        final NetHandler handler = new NetHandler(this);
//        View dialogView = LayoutInflater.from(this.mContext).inflate(R.layout.dialog_edittext, null, false);
//        final EditText editName = (EditText) dialogView.findViewById(R.id.et_title);
//        final String str = pid;
//        final com.worktilecore.core.task.List list2 = list;
//        AlertDialog dialog = new CustomAlertDialogBuilder(this.mContext, R.style.theDialog).setTitle((int) R.string.edit_name).setView(dialogView).setNegativeButton(R.string.cancle, RecyclerViewListsAdapter$$Lambda$15.lambdaFactory$()).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                RecyclerViewListsAdapter.this.mContext.showProgress(true);
//                ListManager.getInstance().copyList(str, list2.getListId(), editName.getText().toString(), new WebApiWithObjectResponse() {
//                    public void onSuccess(Object object) {
//                        com.worktilecore.core.task.List copyOne = (com.worktilecore.core.task.List) object;
//                        final String copyListId = copyOne.getListId();
//                        ListManager.getInstance().getTasksInList(copyOne.getProjectId(), copyListId, new WebApiGetTasksInListResponse() {
//                            public void onSuccess(Object list, Object[] tasks) {
//                                Message message = Message.obtain();
//                                message.what = RecyclerViewListsAdapter.ON_COPY_LIST_SUCCESS;
//                                message.obj = copyListId;
//                                handler.sendMessage(message);
//                            }
//
//                            public boolean onFailure(String error) {
//                                handler.sendEmptyMessage(RecyclerViewListsAdapter.TYPE_DATA);
//                                return super.onFailure(error);
//                            }
//                        });
//                    }
//
//                    public boolean onFailure(String error) {
//                        handler.sendEmptyMessage(RecyclerViewListsAdapter.TYPE_DATA);
//                        return super.onFailure(error);
//                    }
//                });
//            }
//        }).create();
//        dialog.setCanceledOnTouchOutside(true);
//        dialog.setOnShowListener(RecyclerViewListsAdapter$$Lambda$16.lambdaFactory$(this, editName, list));
//        dialog.show();
//    }
//
//    private static /* synthetic */ void lambda$showCopyListDialog$14(DialogInterface dialog1, int which) {
//    }
//
//    private /* synthetic */ void lambda$showCopyListDialog$15(EditText editName, com.worktilecore.core.task.List list, DialogInterface dialog12) {
//        UiUtil.showDialogSoftInput(this.mContext, editName);
//        editName.setText(list.getListName());
//        editName.setSelection(editName.length());
//    }
//
//    private void showRenameListDialog(final com.worktilecore.core.task.List targetList) {
//        final NetHandler handler = new NetHandler(this);
//        View view = LayoutInflater.from(this.mContext).inflate(R.layout.dialog_edittext, null, false);
//        final EditText et_rename = (EditText) view.findViewById(R.id.et_title);
//        et_rename.setText(targetList.getListName());
//        AlertDialog dialog = new CustomAlertDialogBuilder(this.mContext, R.style.theDialog).setTitle((int) R.string.rename).setView(view).setNegativeButton(R.string.cancle, RecyclerViewListsAdapter$$Lambda$17.lambdaFactory$()).setPositiveButton(R.string.finish, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialogInterface, int i) {
//                String rename = et_rename.getText().toString().trim();
//                if (rename.equals(targetList.getListName())) {
//                    RecyclerViewListsAdapter.this.showRenameListDialog(targetList);
//                    return;
//                }
//                String listId = targetList.getListId();
//                RecyclerViewListsAdapter.this.mContext.showProgress(true);
//                ListManager.getInstance().renameList(listId, targetList.getProjectId(), rename, new WebApiResponse() {
//                    public void onSuccess() {
//                        handler.sendEmptyMessage(RecyclerViewListsAdapter.TYPE_FOOTER);
//                    }
//
//                    public boolean onFailure(String error, int code) {
//                        handler.sendEmptyMessage(RecyclerViewListsAdapter.TYPE_DATA);
//                        return super.onFailure(error, code);
//                    }
//                });
//            }
//        }).create();
//        dialog.setCanceledOnTouchOutside(true);
//        dialog.setOnShowListener(RecyclerViewListsAdapter$$Lambda$18.lambdaFactory$(this, et_rename));
//        dialog.show();
//    }
//
//    private static /* synthetic */ void lambda$showRenameListDialog$16(DialogInterface dialogInterface, int i) {
//    }
//
//    private /* synthetic */ void lambda$showRenameListDialog$17(EditText et_rename, DialogInterface dialog1) {
//        UiUtil.showDialogSoftInput(this.mContext, et_rename);
//        et_rename.setSelection(et_rename.length());
//    }
//
//    private void httpAddList(String pid, String listName) {
//        ListManager.getInstance().createList(pid, listName, createPositionForList(), new WebApiWithObjectResponse() {
//            public void onSuccess(Object object) {
//                RecyclerViewListsAdapter.this.mContext.runOnUiThread(RecyclerViewListsAdapter$4$$Lambda$1.lambdaFactory$(this));
//            }
//
//            private /* synthetic */ void lambda$onSuccess$0() {
//                RecyclerViewListsAdapter.this.mContext.fetchListDataFromCacheAndNotify();
//                if (RecyclerViewListsAdapter.this.getItemCount() > RecyclerViewListsAdapter.ON_COPY_LIST_SUCCESS) {
//                    RecyclerViewListsAdapter.this.mContext.scrollRecyclerView(RecyclerViewListsAdapter.this.getItemCount() - 2);
//                }
//            }
//
//            public boolean onFailure(String error, int code) {
//                return super.onFailure(error, code);
//            }
//        });
//    }
//
//    private void httpAddTask(final ViewHolder holder, String listId, String projectId, String taskName) {
//        TaskManager.getInstance().createTask(listId, projectId, new String[TYPE_DATA], taskName, 0, true, new WebApiWithObjectResponse() {
//            public void onSuccess(Object object) {
//                RecyclerViewListsAdapter.this.mContext.runOnUiThread(RecyclerViewListsAdapter$5$$Lambda$1.lambdaFactory$(this, holder));
//            }
//
//            private /* synthetic */ void lambda$onSuccess$0(ViewHolder holder) {
//                RecyclerViewListsAdapter.this.fetchTasksFromCacheAndNotify(holder);
//                RecyclerViewListsAdapter.this.goOnEditTaskAdding(holder);
//            }
//
//            public boolean onFailure(String error) {
//                RecyclerViewListsAdapter.this.mContext.runOnUiThread(RecyclerViewListsAdapter$5$$Lambda$2.lambdaFactory$());
//                return super.onFailure(error);
//            }
//
//            private static /* synthetic */ void lambda$onFailure$1() {
//            }
//        });
//    }

    public int getItemCount() {
        if (this.mFooterView == null) {
            return this.mData.size();
        }
        return this.mData.size() + TYPE_FOOTER;
    }

    public int getItemViewType(int position) {
        if (this.mFooterView != null && position == getItemCount() - 1) {
            return TYPE_FOOTER;
        }
        return TYPE_DATA;
    }

    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(this.mData, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    public void onItemDrop(int position) {
        this.mContext.onEndDrag(position);
    }
}
