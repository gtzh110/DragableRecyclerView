package util;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.view.View;
import android.view.View.MeasureSpec;

import com.woxthebox.draglistview.sample.R;

import FscBoardView.ServiceStageBoardActivity;

public class UiUtil {
    public static int getToolbarHeight(Context context) {
        TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0.0f);
        styledAttributes.recycle();
        return toolbarHeight;
    }

    public static int sp2px(Context context, float spValue) {
        return (int) ((spValue * context.getResources().getDisplayMetrics().scaledDensity) + ServiceStageBoardActivity.DRAGGING_SCALE);
    }

    public static int dp2px(Context context, float dpValue) {
        return (int) ((dpValue * context.getResources().getDisplayMetrics().density) + ServiceStageBoardActivity.DRAGGING_SCALE);
    }

    public static int px2dp(Context context, float pxValue) {
        return (int) ((pxValue / context.getResources().getDisplayMetrics().density) + ServiceStageBoardActivity.DRAGGING_SCALE);
    }

    public static int[] getViewMeasuredWidthAndHeight(View view) {
        view.measure(MeasureSpec.makeMeasureSpec(0, 0), MeasureSpec.makeMeasureSpec(0, 0));
        return new int[]{view.getMeasuredWidth(), view.getMeasuredHeight()};
    }

    public static int getContentHeight(Activity context) {
        Rect outRect = new Rect();
        context.getWindow().findViewById(android.R.id.content).getDrawingRect(outRect);
        return outRect.height();
    }



    public static int getScreenWidth(Activity context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Activity context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getStatusBarHeight(Context context){
        int statusBarHeight = -1;
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取尺寸值
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);

        }
        return statusBarHeight;
    }

//    public static int getScreenWidth(){
//
//    }
//
//    public static int getScreenHeight(){
//
//    }

//    public static int getClassifyItemHeight() {
//        int height = 0;
//        View classify = LayoutInflater.from(HbApplication.getContext()).inflate(R.layout.listview_item_classify, null);
//        if (classify != null) {
//            height = 0;
//            try {
//                classify.measure(MeasureSpec.makeMeasureSpec(0, 0), MeasureSpec.makeMeasureSpec(0, 0));
//                height = classify.getMeasuredHeight();
//            } catch (NullPointerException e) {
//            }
//        }
//        return height;
//    }
//
//    public static void hideSoftInput(Activity activity, View view) {
//        InputMethodManager imm = (InputMethodManager) activity.getSystemService("input_method");
//        imm.showSoftInput(view, 2);
//        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//    }
//
//    public static void showSoftInput(Activity activity) {
//        ((InputMethodManager) activity.getSystemService("input_method")).toggleSoftInput(0, 2);
//    }
//
//    public static void showDialogSoftInput(Activity activity, View view) {
//        ((InputMethodManager) activity.getSystemService("input_method")).showSoftInput(view, 1);
//    }
//
//    public static void showRuningNumber(@NonNull final TextView view, int from, int to, long duration) {
//        ValueAnimator valueAnimator = ValueAnimator.ofInt(new int[]{from, to});
//        valueAnimator.setDuration(duration);
//        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                view.setText(String.valueOf(((Integer) valueAnimator.getAnimatedValue()).intValue()));
//            }
//        });
//        valueAnimator.start();
//    }
//
//    public static void expand(final View view, int height) {
//        android.animation.ValueAnimator valueAnimator = android.animation.ValueAnimator.ofInt(new int[]{0, height});
//        valueAnimator.setDuration(400);
//        valueAnimator.addUpdateListener(new android.animation.ValueAnimator.AnimatorUpdateListener() {
//            public void onAnimationUpdate(android.animation.ValueAnimator animation) {
//                view.getLayoutParams().height = ((Integer) animation.getAnimatedValue()).intValue();
//                view.requestLayout();
//            }
//        });
//        valueAnimator.setTarget(view);
//        valueAnimator.start();
//    }
//
//    public static void collapse(final View view, int height) {
//        android.animation.ValueAnimator valueAnimator = android.animation.ValueAnimator.ofInt(new int[]{height, 0});
//        valueAnimator.setDuration(400);
//        valueAnimator.addUpdateListener(new android.animation.ValueAnimator.AnimatorUpdateListener() {
//            public void onAnimationUpdate(android.animation.ValueAnimator animation) {
//                view.getLayoutParams().height = ((Integer) animation.getAnimatedValue()).intValue();
//                view.requestLayout();
//            }
//        });
//        valueAnimator.setTarget(view);
//        valueAnimator.start();
//    }
}
