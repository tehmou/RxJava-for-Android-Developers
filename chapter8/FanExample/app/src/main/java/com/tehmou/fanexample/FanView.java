package com.tehmou.fanexample;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.FrameLayout;

/**
 * Created by ttuo on 28/07/16.
 */
public class FanView extends FrameLayout {
    private static final String TAG = FanView.class.getSimpleName();

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
}
