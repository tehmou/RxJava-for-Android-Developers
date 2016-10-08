package com.tehmou.mapsclient;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;

import com.tehmou.mapsclient.network.MapNetworkAdapter;
import com.tehmou.mapsclient.network.MapNetworkAdapterSimple;
import com.tehmou.mapsclient.network.NetworkClient;
import com.tehmou.mapsclient.network.NetworkClientOkHttp;
import com.tehmou.mapsclient.network.TileBitmapLoader;
import com.tehmou.mapsclient.utils.CoordinateProjection;
import com.tehmou.mapsclient.utils.LatLng;
import com.tehmou.mapsclient.utils.MapTileUtils;
import com.tehmou.mapsclient.utils.PointD;
import com.tehmou.mapsclient.utils.ViewUtils;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.BehaviorSubject;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NetworkClient networkClient = new NetworkClientOkHttp();
        MapNetworkAdapter mapNetworkAdapter = new MapNetworkAdapterSimple(networkClient,
                "https://b.tile.openstreetmap.org/%d/%d/%d.png");
        TileBitmapLoader tileBitmapLoader = new TileBitmapLoader(mapNetworkAdapter);

        final double TILE_SIZE = 256;

        ViewUtils.TouchDelta touchDelta = new ViewUtils.TouchDelta();
        TilesView tilesView = (TilesView) findViewById(R.id.tiles_view);
        tilesView.setOnTouchListener(touchDelta);
        tilesView.setTileBitmapLoader(tileBitmapLoader);

        final int zoom = 10;

        Observable<PointD> tilesViewSize = tilesView.getViewSize();
        BehaviorSubject<LatLng> mapCenter =
                BehaviorSubject.create(new LatLng(51.509865, -0.118092));
        CoordinateProjection coordinateProjection =
                new CoordinateProjection((int) TILE_SIZE);
        Observable<PointD> offset =
                Observable.combineLatest(
                        tilesViewSize, mapCenter,
                        (tilesViewSizeValue, mapCenterValue) ->
                                MapTileUtils.calculateOffset(coordinateProjection,
                                        zoom, tilesViewSizeValue, mapCenterValue));

        touchDelta.getObservable()
                .withLatestFrom(
                        Observable.combineLatest(tilesViewSize, offset, Pair::new),
                        (pixelDelta, pair) -> {
                            Log.v(TAG, "pixelDelta(" + pixelDelta + ")");
                            final double cx = pair.first.x / 2.0 - pair.second.x;
                            final double cy = pair.first.y / 2.0 - pair.second.y;
                            final PointD newPoint = new PointD(cx - pixelDelta.x, cy - pixelDelta.y);
                            return coordinateProjection.fromPointToLatLng(newPoint, zoom);
                        })
                .subscribe(mapCenter::onNext);


        Observable.combineLatest(
                tilesViewSize,
                offset,
                (tilesViewSizeValue, offsetValue) -> MapTileUtils.calculateMapTiles(
                        TILE_SIZE, zoom, tilesViewSizeValue, offsetValue
                ))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tilesView::setTiles);
    }
}
