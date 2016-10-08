package com.tehmou.mapsclient;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.jakewharton.rxbinding.view.RxView;
import com.tehmou.mapsclient.network.MapNetworkAdapter;
import com.tehmou.mapsclient.network.MapNetworkAdapterSimple;
import com.tehmou.mapsclient.network.NetworkClient;
import com.tehmou.mapsclient.network.NetworkClientOkHttp;
import com.tehmou.mapsclient.network.TileBitmapLoader;
import com.tehmou.mapsclient.utils.CoordinateProjection;
import com.tehmou.mapsclient.utils.LatLng;
import com.tehmou.mapsclient.utils.MapTileUtils;
import com.tehmou.mapsclient.utils.PointD;
import com.tehmou.mapsclient.utils.Triple;
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

        BehaviorSubject<Integer> zoom = BehaviorSubject.create(10);
        RxView.clicks(findViewById(R.id.zoom_in_button))
                .subscribe(ignore -> zoom.onNext(zoom.getValue() + 1));
        RxView.clicks(findViewById(R.id.zoom_out_button))
                .subscribe(ignore -> zoom.onNext(zoom.getValue() - 1));

        Observable<PointD> tilesViewSize = tilesView.getViewSize();
        BehaviorSubject<LatLng> mapCenter =
                BehaviorSubject.create(new LatLng(51.509865, -0.118092));
        CoordinateProjection coordinateProjection =
                new CoordinateProjection((int) TILE_SIZE);
        Observable<PointD> offset =
                Observable.combineLatest(
                        tilesViewSize, mapCenter, zoom,
                        (tilesViewSizeValue, mapCenterValue, zoomValue) ->
                                MapTileUtils.calculateOffset(coordinateProjection,
                                        zoomValue, tilesViewSizeValue, mapCenterValue));

        Observable<Triple<PointD, PointD, Integer>> mapState =
                Observable.combineLatest(
                        tilesViewSize, offset, zoom, Triple::new);
        touchDelta.getObservable()
                .withLatestFrom(mapState,
                        (pixelDelta, mapStateValue) -> {
                            Log.v(TAG, "pixelDelta(" + pixelDelta + ")");
                            final double cx = mapStateValue.first.x / 2.0 - mapStateValue.second.x;
                            final double cy = mapStateValue.first.y / 2.0 - mapStateValue.second.y;
                            final PointD newPoint = new PointD(cx - pixelDelta.x, cy - pixelDelta.y);
                            return coordinateProjection.fromPointToLatLng(newPoint, mapStateValue.third);
                        })
                .subscribe(mapCenter::onNext);


        Observable.combineLatest(
                tilesViewSize, offset, zoom,
                (tilesViewSizeValue, offsetValue, zoomValue) ->
                        MapTileUtils.calculateMapTiles(
                                TILE_SIZE, zoomValue, tilesViewSizeValue, offsetValue
                        ))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tilesView::setTiles);
    }
}
