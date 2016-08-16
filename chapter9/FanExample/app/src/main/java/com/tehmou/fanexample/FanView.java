package com.tehmou.fanexample;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ttuo on 28/07/16.
 */
public class FanView extends FrameLayout {
    private static final String TAG = FanView.class.getSimpleName();

    private List<FanItem> fanItems = new ArrayList<>();
    private float openRatio = 0f;

    public FanView(Context context) {
        this(context, null, 0);
    }

    public FanView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setStaticTransformationsEnabled(true);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.d(TAG, "onLayout(" + changed + ", " + left + ", "  + top + ", " + right + ", " + bottom + ")");
        super.onLayout(changed, left, top, right, bottom);
        Log.d(TAG, "End onLayout");
    }

    @Override
    protected boolean getChildStaticTransformation(View child, Transformation t) {
        Log.d(TAG, "getChildStaticTransformation(" + child + ", " + t + ")");
        final float index = getChildCount() - indexOfChild(child) - 1;
        final float height = child.getHeight();

        Log.d(TAG, "params: " + index + ", " + height);

        Matrix matrix = t.getMatrix();
        matrix.setRotate(index * 20 * openRatio, height/2, height/2);
        return true;
    }

    public void setOpenRatio(float r) {
        this.openRatio = r;
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            view.invalidate();
        }
        invalidate();
    }

    public void setFanItems(List<FanItem> fanItems) {
        this.fanItems = fanItems;
        removeAllViewsInLayout();
        for (int i = 0; i < fanItems.size(); i++) {
            int fanItemIndex = fanItems.size() - i - 1;
            FanItem fanItem = fanItems.get(fanItemIndex);
            View fanView = inflate(getContext(),
                    fanItemIndex == 0 ? R.layout.fan_item_header : R.layout.fan_item, null);
            TextView textView = (TextView) fanView.findViewById(R.id.fan_view_item_title);
            textView.setText(fanItem.getTitle());
            addViewInLayout(fanView, i,
                    new FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        invalidate();
    }
}
