package dont_need_copy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.woxthebox.draglistview.sample.R;


/**
 * Created with IntelliJ IDEA.
 * User: Yi
 * Date: 13-12-4
 * Time: 上午10:00
 */
public class BottomDialogActivityTypes extends RelativeLayout {
    protected View buttonPart;
    private Animation upAnimation;
    private Animation downAnimation;

    public BottomDialogActivityTypes(Context context) {
        super(context);
    }

    public BottomDialogActivityTypes(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomDialogActivityTypes(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

//    public void init(JsonActivityTypes types, final long objectId, final int systemId, final long groupId, final String objectName) {
//        BaseActivity activity = (BaseActivity) getContext();
//        activity.setBackPressed(new BaseActivity.BackPressed() {
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
//        initView(types, objectId, systemId, groupId, objectName);
//
//    }

//    public void initDialog(JsonActivityTypes types, final long objectId, final int systemId, final long groupId, final String objectName) {
//        initView(types, objectId, systemId, groupId, objectName);
//    }

//    private void initView(JsonActivityTypes types, final long objectId, final int systemId, final long groupId, final String objectName) {
//        buttonPart = findViewById(R.id.button_part);
//        if (types != null) {
//            LinearLayout layout = (LinearLayout) findViewById(R.id.action_buttons);
//            layout.removeAllViews();
//            for (int i = 0; i < types.typeKeys.length; i++) {
//                //4.4开始取消过滤掉普通记录类型
//                if ("-11".equals(types.typeCategories[i])) {
//                    continue;
//                }
//                View view = View.inflate(getContext(), R.layout.object_feed_type, null);
//                TextView typeText = (TextView) view.findViewById(R.id.type_text);
//                typeText.setText(types.typeStrings[i]);
//                ImageView imageView = (ImageView) view.findViewById(R.id.type_image);
//                final String category = types.typeCategories[i];
//                final String key = types.typeKeys[i];
//                final String name = types.typeStrings[i];
//                imageView.setImageResource(JsonRecordNew.getCategorySendImage(TextUtils.isEmpty(category) ? 0 : Integer.valueOf(category)));
//                view.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        setVisibility(View.GONE);
//                        Activity activity = (Activity) getContext();
//                        //if is sign in, get the location info first
//                        if (RecordEditorActivity.isSignin(category)) {
//                            MapSwitch.gotoMapList(getContext(), null, null);
////                            MapList.gotoMapList(activity, null, null, null);
//                        } else {
//                            Intent editor = new Intent(activity, RecordEditorActivity.class);
//                            editor.putExtra(ActivityConsts.TITLE, IngageStringUtil.getString(activity, R.string.add) + name);
//                            if (objectId > 0)
//                                editor.putExtra(Apis.ACTIVITY_RECORD_SYSTEM_ITEM_ID, objectId + "");
//                            if (systemId > 0)
//                                editor.putExtra(Apis.ACTIVITY_RECORD_SYSTEM_ID, systemId + "");
//                            editor.putExtra(Apis.ACTIVITY_TYPE_ID, key + "");
//                            editor.putExtra(Apis.CATEGORY_ID, category + "");
//                            if (groupId > 0)
//                                editor.putExtra(Apis.GROUP_ID, groupId);
//                            if (TextUtils.isEmpty(objectName)) {
//                                editor.putExtra(ActivityConsts.NAME, name);
//                            }
//                            editor.putExtra(ActivityConsts.BELONG_ID, Long.parseLong(SystemIds.APPS_ACTIVITY_RECORD + ""));
//                            activity.startActivityForResult(editor, ActivityConsts.REQ_DETAIL_REFRESH);
//                        }
//                    }
//                });
//                layout.addView(view);
//            }
//        }
//
//
//        findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setVisibility(View.INVISIBLE);
//            }
//        });
//        setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setVisibility(View.INVISIBLE);
//            }
//        });
//    }

    /**
     * 报销单多业务类型
     * @param
     */
//    public void initView(JsonExpenseAccountEntityType entityTypes, final ExpenseAccountList.TypeCallback callback){
//        buttonPart = findViewById(R.id.button_part);
//        if (entityTypes != null && entityTypes.mEntityTypeNames != null) {
//            LinearLayout layout = (LinearLayout) findViewById(R.id.action_buttons);
//            layout.removeAllViews();
//            for (int i = 0; i < entityTypes.mEntityTypeNames.size(); i++) {
//                final long id = entityTypes.mEntityTypeIds.get(i);
//                final String name = entityTypes.mEntityTypeNames.get(i);
//                View view = View.inflate(getContext(), R.layout.object_feed_type, null);
//                TextView typeText = (TextView) view.findViewById(R.id.type_text);
//                ImageView imageView = (ImageView) view.findViewById(R.id.type_image);
//                typeText.setText(name);
//                imageView.setImageResource(0);
//                view.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        setVisibility(View.GONE);
//                        if(callback != null){
//                            callback.callback(id,name);
//                        }
//                    }
//                });
//                layout.addView(view);
//            }
//        }
//
//        findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setVisibility(View.INVISIBLE);
//            }
//        });
//        setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setVisibility(View.INVISIBLE);
//            }
//        });
//    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int screenHeight = ((Activity) getContext()).getWindowManager().getDefaultDisplay().getHeight();
        if (buttonPart == null) {
            buttonPart = findViewById(R.id.button_part);
        }
        int height = buttonPart.getHeight();
        if (height > screenHeight * 3 / 4) {
            ViewGroup.LayoutParams layoutParams = buttonPart.getLayoutParams();
            layoutParams.height = screenHeight * 3 / 4;
            buttonPart.setLayoutParams(layoutParams);
        }
    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility == View.VISIBLE) {
            super.setVisibility(visibility);
            animationUp();
        } else {
            animationDown();
        }
    }


    public void animationDown() {
        if (downAnimation == null) {
            int height = buttonPart.getHeight();
            downAnimation = new TranslateAnimation(0, 0, 0, height);
            downAnimation.setDuration(300);
            downAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    BottomDialogActivityTypes.super.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        if (downAnimation.hasStarted() && !downAnimation.hasEnded()) {
            return;
        }
        buttonPart.startAnimation(downAnimation);
    }

    public void animationUp() {
        if (upAnimation == null) {
            int height = buttonPart.getHeight();
            upAnimation = new TranslateAnimation(0, 0, height, 0);
            upAnimation.setDuration(300);
        }
        buttonPart.startAnimation(upAnimation);
        try {
            Activity activity = (Activity) getContext();
//            activity.closeKeyboard(activity);
        } catch (ClassCastException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
