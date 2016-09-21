package com.tehmou.mapsclient;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Collection;

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
        tilePaint = new Paint();
        tilePaint.setColor(Color.BLACK);
        tilePaint.setStyle(Paint.Style.STROKE);
        tilePaint.setStrokeWidth(2);
    }

    private Collection<DrawableTile> tiles;
    private Paint tilePaint;

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.GRAY);
        if (tiles != null) {
            for (DrawableTile tile : tiles) {
                canvas.drawRect(
                        tile.getScreenX(), tile.getScreenY(),
                        tile.getScreenX() + tile.getWidth(),
                        tile.getScreenY() + tile.getHeight(),
                        tilePaint);
            }
        }
    }

    public void setTiles(Collection<DrawableTile> tiles) {
        this.tiles = tiles;
        invalidate();
    }
}
