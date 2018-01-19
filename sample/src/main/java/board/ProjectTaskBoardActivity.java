package board;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.View.OnLayoutChangeListener;

import com.woxthebox.draglistview.sample.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import drag.DragHelper;
import drag.DragLayout;

public class ProjectTaskBoardActivity extends Activity implements OnListDragAndDropListener {
    public static final float DRAGGING_SCALE = 0.5f;
    public static final float FULL_SCALE = 1.0f;
    public static final int TYPE_FROM_ADD = 5;
    public static final int TYPE_FROM_DETAIL = 3;
    public static final int TYPE_FROM_MSG = 2;
    public static final int TYPE_FROM_NOTIFY = 3;
    public static final int TYPE_FROM_PROJECT_LIST = 1;
    public static final int TYPE_FROM_TEAM = 4;
    public static boolean ifRefresh = false;
    private RecyclerViewListsAdapter mAdapter;
    private List mData = new ArrayList();
    private Map<String, String> mDataMap = new LinkedHashMap();
    private DragHelper mDragHelper;
    private int mFromPosition = -1;
    private ItemTouchHelper mItemTouchHelper;
    private DragLayout mLayoutMain;

    private OnLayoutChangeListener mOnLayoutChangedListener = new OnLayoutChangeListener() {
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        }
    };

    private PagerRecyclerView.OnPageChangedListener mOnPagerChangedListener = new PagerRecyclerView.OnPageChangedListener() {
        public void OnPageChanged(int oldPosition, int newPosition) {
        }
    };

    private OnScrollListener mOnScrollListener = new OnScrollListener() {
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            int childCount = ProjectTaskBoardActivity.this.mRecyclerView.getChildCount();
            int padding = (ProjectTaskBoardActivity.this.mRecyclerView.getWidth() - ProjectTaskBoardActivity.this.mRecyclerView.getChildAt(0).getWidth()) / ProjectTaskBoardActivity.TYPE_FROM_MSG;
            for (int j = 0; j < childCount; j += ProjectTaskBoardActivity.TYPE_FROM_PROJECT_LIST) {
                View v = recyclerView.getChildAt(j);
                float rate = 0.0f;
                if (v.getLeft() <= padding) {
                    if (v.getLeft() >= padding - v.getWidth()) {
                        rate = (((float) (padding - v.getLeft())) * ProjectTaskBoardActivity.FULL_SCALE) / ((float) v.getWidth());
                    } else {
                        rate = ProjectTaskBoardActivity.FULL_SCALE;
                    }
                    v.setScaleX(ProjectTaskBoardActivity.FULL_SCALE - (rate * 0.1f));
                } else {
                    if (v.getLeft() <= recyclerView.getWidth() - padding) {
                        rate = (((float) ((recyclerView.getWidth() - padding) - v.getLeft())) * ProjectTaskBoardActivity.FULL_SCALE) / ((float) v.getWidth());
                    }
                    v.setScaleX(0.9f + (rate * 0.1f));
                }
            }
        }
    };

    private PagerRecyclerView mRecyclerView;
    private ScaleHelper mScaleHelper;
    private String movingListId;
    public String projectId;
    public String projectName;
    public String teamId;
    public int type_from;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskboard);
        this.mLayoutMain = (DragLayout) findViewById(R.id.layout_main);
        this.mRecyclerView = (PagerRecyclerView) findViewById(R.id.rv_lists);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setFlingFactor(0.1f);
        for (int i = 0; i < 10; i++) {
            mData.add("List ======= " + i);
        }
        this.mAdapter = new RecyclerViewListsAdapter(this, this.mData);
        this.mAdapter.setFooterView(getLayoutInflater().inflate(R.layout.recyclerview_footer_addlist, null, false));
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mItemTouchHelper = new ItemTouchHelper(new ListItemTouchHelperCallback(this.mAdapter));
        this.mItemTouchHelper.attachToRecyclerView(this.mRecyclerView);
        this.mRecyclerView.addOnPageChangedListener(this.mOnPagerChangedListener);
        this.mRecyclerView.addOnLayoutChangeListener(this.mOnLayoutChangedListener);
        this.mRecyclerView.addOnScrollListener(this.mOnScrollListener);
        this.mDragHelper = new DragHelper(this);
        this.mDragHelper.bindHorizontalRecyclerView(this.mRecyclerView);
        this.mLayoutMain.setDragHelper(this.mDragHelper);
        this.mScaleHelper = new ScaleHelper(this);
        this.mScaleHelper.setContentView(this.mLayoutMain);
        this.mScaleHelper.setHorizontalView(this.mRecyclerView);
        this.projectId = "120";
        this.projectName = "test";
    }

    public DragHelper getDragHelper() {
        return this.mDragHelper;
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    protected void onStart() {
        super.onStart();

    }


    public void scrollRecyclerView(int position) {
        if (this.mRecyclerView != null) {
            this.mRecyclerView.smoothScrollToPosition(position);
        }
    }


    private void showAddDialog() {

    }


    public void onClick(View v) {
    }


//    public void httpMoveTask(int toListPosition, int position, Task task) {
//
//    }

    private void httpGetLists(String pid) {

    }

    private void httpGetProjectInfo(String pid) {

    }

    private void httpMoveListByRelativeListId(int toPosition) {

    }

    public void onStartDrag(ViewHolder viewHolder, int fromPosition) {
        this.mScaleHelper.startScaleModel();
        this.mItemTouchHelper.startDrag(viewHolder);
//        List movingList = mData.get(fromPosition);
//        if (movingList != null) {
//            this.movingListId = movingList.getListId();
        this.mFromPosition = fromPosition;
//        }
    }

    public void onEndDrag(int toPosition) {
        this.mScaleHelper.stopScaleModel(mAdapter.itemWidth);
        this.mRecyclerView.scrollToPosition(toPosition);
    }
}
