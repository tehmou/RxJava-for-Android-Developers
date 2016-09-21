package com.tehmou.mapsclient;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ttuo on 21/09/16.
 */
public class TilesView extends View {
    public TilesView(Context context) {
        this(context, null, 0);
    }

    public TilesView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TilesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.RED);
    }
}
