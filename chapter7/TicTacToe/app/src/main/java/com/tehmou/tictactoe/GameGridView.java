package com.tehmou.tictactoe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class GameGridView extends View {
    private static final String TAG = GameGridView.class.getSimpleName();
    private GameGrid gameState;
    private int width;
    private int height;
    private final Paint linePaint;
    private final Paint bitmapPaint;
    private final Bitmap circleBitmap;
    private final Bitmap crossBitmap;
    private final Rect bitmapSrcRect;

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

        bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        circleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.symbol_circle);
        crossBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.symbol_cross);
        bitmapSrcRect = new Rect(0, 0, circleBitmap.getWidth(), circleBitmap.getHeight());
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
        final float tileHeight = height / gameState.getHeight();

        for (int i = 0; i < gameState.getWidth(); i++) {
            for (int n = 0; n < gameState.getHeight(); n++) {
                GameSymbol symbol = gameState.getSymbolAt(i, n);
                RectF dst = new RectF(i * tileWidth, n * tileHeight,
                        (i+1) * tileWidth, (n+1) * tileHeight);
                if (symbol == GameSymbol.CIRCLE) {
                    canvas.drawBitmap(
                            circleBitmap,
                            bitmapSrcRect, dst,
                            bitmapPaint);
                } else if (symbol == GameSymbol.CROSS) {
                    canvas.drawBitmap(
                            crossBitmap,
                            bitmapSrcRect, dst,
                            bitmapPaint);
                }
            }
        }

        for (int i = 0; i <= gameState.getWidth(); i++) {
            canvas.drawLine(i * tileWidth, 0, i * tileWidth, height, linePaint);
        }

        for (int n = 0; n <= gameState.getHeight(); n++) {
            canvas.drawLine(0, n * tileHeight, width, n * tileHeight, linePaint);
        }
    }

    public void setData(GameGrid gameState) {
        this.gameState = gameState;
        invalidate();
    }
}
