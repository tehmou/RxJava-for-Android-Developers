package com.tehmou.mapsclient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.tehmou.mapsclient.network.TileBitmapLoader;
import com.tehmou.mapsclient.utils.PointD;

import java.util.Collection;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.BehaviorSubject;

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

        bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    private final BehaviorSubject<PointD> viewSize = BehaviorSubject.create();
    private Collection<DrawableTile> tiles;
    private Paint tilePaint;
    private Paint bitmapPaint;
    private TileBitmapLoader tileBitmapLoader;

    public void setTileBitmapLoader(TileBitmapLoader tileBitmapLoader) {
        this.tileBitmapLoader = tileBitmapLoader;
        this.tileBitmapLoader.bitmapsLoadedEvent()
                .onBackpressureDrop()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ignore -> invalidate());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int width = right - left;
        int height = bottom - top;
        viewSize.onNext(new PointD(width, height));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.GRAY);
        if (tiles != null) {
            for (DrawableTile tile : tiles) {
                Bitmap bitmap = null;
                if (tileBitmapLoader != null) {
                    bitmap = tileBitmapLoader.getBitmap(tile);
                }
                RectF dst = new RectF(
                        (float) tile.getScreenX(), (float) tile.getScreenY(),
                        (float) (tile.getScreenX() + tile.getWidth()),
                        (float) (tile.getScreenY() + tile.getHeight()));
                if (bitmap != null) {
                    canvas.drawBitmap(bitmap, null, dst, bitmapPaint);
                }
                canvas.drawRect(dst, tilePaint);
            }
        }
    }

    public void setTiles(Collection<DrawableTile> tiles) {
        this.tiles = tiles;
        if (tileBitmapLoader != null) {
            tileBitmapLoader.load(tiles);
        }
        invalidate();
    }

    public Observable<PointD> getViewSize() {
        return viewSize.asObservable();
    }
}
