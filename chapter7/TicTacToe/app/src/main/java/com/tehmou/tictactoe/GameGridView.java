package com.tehmou.tictactoe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class GameGridView extends View {
    private static final String TAG = GameGridView.class.getSimpleName();
    private GameGrid gameState;
    private int width;
    private int height;
    private final Paint linePaint;
    private final Paint circlePaint;
    private final Paint crossPaint;

    public GameGridView(Context context) {
        this(context, null);
    }

    public GameGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GameGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        linePaint = new Paint();
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(8f);

        circlePaint = new Paint();
        circlePaint.setColor(Color.RED);

        crossPaint = new Paint();
        crossPaint.setColor(Color.BLUE);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        width = right - left;
        height = bottom - top;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.WHITE);

        Log.d(TAG, "width: " + width + ", height: " + height);

        if (gameState == null) {
            return;
        }

        final float tileWidth = width / gameState.getWidth();
        for (int i = 0; i <= gameState.getWidth(); i++) {
            canvas.drawLine(i * tileWidth, 0, i * tileWidth, height, linePaint);
        }

        final float tileHeight = height / gameState.getHeight();
        for (int n = 0; n <= gameState.getHeight(); n++) {
            canvas.drawLine(0, n * tileHeight, width, n * tileHeight, linePaint);
        }

        for (int i = 0; i < gameState.getWidth(); i++) {
            for (int n = 0; n < gameState.getHeight(); n++) {
                GameSymbol symbol = gameState.getSymbolAt(i, n);
                if (symbol == GameSymbol.CIRCLE) {
                    canvas.drawRect(
                            i * tileWidth, n * tileHeight,
                            (i+1) * tileWidth, (n+1) * tileHeight,
                            circlePaint);
                } else if (symbol == GameSymbol.CROSS) {
                    canvas.drawRect(
                            i * tileWidth, n * tileHeight,
                            (i+1) * tileWidth, (n+1) * tileHeight,
                            crossPaint);
                }
            }
        }
    }

    public void setData(GameGrid gameState) {
        this.gameState = gameState;
        invalidate();
    }
}
