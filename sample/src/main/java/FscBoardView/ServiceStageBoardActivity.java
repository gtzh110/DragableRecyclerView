package FscBoardView;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.woxthebox.draglistview.sample.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ServiceStageBoardActivity extends Activity implements OnListDragAndDropListener {
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
            int childCount = mRecyclerView.getChildCount();
            int padding = (mRecyclerView.getWidth() - mRecyclerView.getChildAt(0).getWidth()) / ServiceStageBoardActivity.TYPE_FROM_MSG;
            for (int j = 0; j < childCount; j += ServiceStageBoardActivity.TYPE_FROM_PROJECT_LIST) {
                View v = recyclerView.getChildAt(j);
                float rate = 0.0f;
                if (v.getLeft() <= padding) {
                    if (v.getLeft() >= padding - v.getWidth()) {
                        rate = (((float) (padding - v.getLeft())) * ServiceStageBoardActivity.FULL_SCALE) / ((float) v.getWidth());
                    } else {
                        rate = ServiceStageBoardActivity.FULL_SCALE;
                    }
                    v.setScaleX(ServiceStageBoardActivity.FULL_SCALE - (rate * 0.1f));
                } else {
                    if (v.getLeft() <= recyclerView.getWidth() - padding) {
                        rate = (((float) ((recyclerView.getWidth() - padding) - v.getLeft())) * ServiceStageBoardActivity.FULL_SCALE) / ((float) v.getWidth());
                    }
                    v.setScaleX(0.9f + (rate * 0.1f));
                }
            }
        }
    };

    private PagerRecyclerView mRecyclerView;
    private ScaleHelper mScaleHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskboard);
        initView();
    }

    private void initView() {
        mLayoutMain = (DragLayout) findViewById(R.id.layout_main);
        mRecyclerView = (PagerRecyclerView) findViewById(R.id.rv_lists);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setFlingFactor(0.1f);
        ((TextView) findViewById(R.id.title)).setText("服务阶段名称");
        findViewById(R.id.seek).setVisibility(View.VISIBLE);
        findViewById(R.id.button).setVisibility(View.VISIBLE);
        ((ImageView) findViewById(R.id.button)).setImageResource(R.drawable.team_member);
        initAllAdapter();
    }

    /**
     * 初始化缩放、拖拽、adapter
     */
    private void initAllAdapter() {
        for (int i = 0; i < 10; i++) {
            mData.add("List ======= " + i);
        }
        mAdapter = new RecyclerViewListsAdapter(this, mData);
        mAdapter.setFooterView(getLayoutInflater().inflate(R.layout.recyclerview_footer_addlist, null, false));
        mRecyclerView.setAdapter(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(new ListItemTouchHelperCallback(mAdapter));
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.addOnPageChangedListener(mOnPagerChangedListener);
        mRecyclerView.addOnLayoutChangeListener(mOnLayoutChangedListener);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        mDragHelper = new DragHelper(this);
        mDragHelper.bindHorizontalRecyclerView(mRecyclerView);
        mLayoutMain.setDragHelper(mDragHelper);
        mScaleHelper = new ScaleHelper(this);
        mScaleHelper.setContentView(mLayoutMain);
        mScaleHelper.setHorizontalView(mRecyclerView);
    }

    public DragHelper getDragHelper() {
        return mDragHelper;
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    protected void onStart() {
        super.onStart();

    }


    public void scrollRecyclerView(int position) {
        if (mRecyclerView != null) {
            mRecyclerView.smoothScrollToPosition(position);
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
        mScaleHelper.startScaleModel();
        mItemTouchHelper.startDrag(viewHolder);
//        List movingList = mData.get(fromPosition);
//        if (movingList != null) {
//            movingListId = movingList.getListId();
        mFromPosition = fromPosition;
//        }
    }

    public void onEndDrag(int toPosition) {
        mScaleHelper.stopScaleModel(mAdapter.itemWidth);
        mRecyclerView.scrollToPosition(toPosition);
    }
}
