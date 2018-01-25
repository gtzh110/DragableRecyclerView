package dont_need_copy;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.woxthebox.draglistview.sample.R;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Descriptions: 派工单中 退回派工池 弹出的dialog
 * Created by zhanghao on 2017/7/20.
 */

public class BottomDialogInFieldJobMain extends BottomDialogActivityTypes {
    private OnItemClickListener mOnItemClickListener;

    public BottomDialogInFieldJobMain(Context context) {
        super(context);
    }

    public BottomDialogInFieldJobMain(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomDialogInFieldJobMain(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public interface OnItemClickListener {
        void onItemClick(String id);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void initDialog(ArrayList<JSONObject> list) {
        Activity activity = (Activity) getContext();
//        activity.setBackPressed(new Activity.BackPressed() {
//            @Override
//            public boolean backPressed() {
//                if (getVisibility() == View.VISIBLE) {
//                    setVisibility(View.GONE);
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        });
        initDialogView(list);
    }

    private void initDialogView(ArrayList<JSONObject> list) {
        buttonPart = findViewById(R.id.button_part);
        if (list != null && !list.isEmpty()) {
            LinearLayout layout = (LinearLayout) findViewById(R.id.action_buttons);
            layout.removeAllViews();
            String reason = null, id = null;
            for (JSONObject jsonObject : list) {
                reason = jsonObject.optString("reason");
                id = jsonObject.optString("id");
                View view = View.inflate(getContext(), R.layout.field_job_bottom_dialog_item, null);
                TextView typeText = (TextView) view.findViewById(R.id.type_text);
                if (!TextUtils.isEmpty(reason)) {
                    typeText.setText(reason);
                }
                final String finalId = id;
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setVisibility(View.GONE);
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemClick(finalId);
                        }
                    }
                });
                layout.addView(view);
            }
        }

        findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibility(View.GONE);
            }
        });

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibility(View.GONE);
            }
        });
    }


}
