package com.tehmou.fanexample;

import android.content.Context;
import android.graphics.Matrix;
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
        final float index = indexOfChild(child);
        final float height = child.getHeight();

        Log.d(TAG, "params: " + index + ", " + height);

        Matrix matrix = t.getMatrix();
        matrix.setRotate(index * 30, height/2, height/2);
        return true;
    }
}
