package com.tehmou.mapsclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tehmou.mapsclient.network.MapNetworkAdapter;
import com.tehmou.mapsclient.network.MapNetworkAdapterSimple;
import com.tehmou.mapsclient.network.NetworkClient;
import com.tehmou.mapsclient.network.NetworkClientOkHttp;
import com.tehmou.mapsclient.network.TileBitmapLoader;
import com.tehmou.mapsclient.utils.CoordinateProjection;
import com.tehmou.mapsclient.utils.LatLng;
import com.tehmou.mapsclient.utils.MapTileUtils;
import com.tehmou.mapsclient.utils.PointD;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NetworkClient networkClient = new NetworkClientOkHttp();
        MapNetworkAdapter mapNetworkAdapter = new MapNetworkAdapterSimple(networkClient,
                "https://b.tile.openstreetmap.org/%d/%d/%d.png");
        TileBitmapLoader tileBitmapLoader = new TileBitmapLoader(mapNetworkAdapter);

        final double TILE_SIZE = 256;

        TilesView tilesView = (TilesView) findViewById(R.id.tiles_view);
        tilesView.setTileBitmapLoader(tileBitmapLoader);

        Observable<PointD> tilesViewSize = tilesView.getViewSize();
        Observable<LatLng> mapCenter = Observable.just(new LatLng(51.509865, -0.118092));

        Observable.combineLatest(
                tilesViewSize,
                mapCenter,
                (tilesViewSizeValue, mapCenterValue) -> {
                    int zoom = 10;
                    CoordinateProjection coordinateProjection =
                            new CoordinateProjection((int) TILE_SIZE);
                    PointD offset = MapTileUtils.calculateOffset(coordinateProjection,
                            zoom, tilesViewSizeValue, mapCenterValue);
                    return MapTileUtils.calculateMapTiles(
                            TILE_SIZE, zoom, tilesViewSizeValue, offset
                    );
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tilesView::setTiles);
    }
}
