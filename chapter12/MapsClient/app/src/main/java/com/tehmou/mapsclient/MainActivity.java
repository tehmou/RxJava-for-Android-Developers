package com.tehmou.mapsclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tehmou.mapsclient.network.MapNetworkAdapter;
import com.tehmou.mapsclient.network.MapNetworkAdapterSimple;
import com.tehmou.mapsclient.network.NetworkClient;
import com.tehmou.mapsclient.network.NetworkClientOkHttp;
import com.tehmou.mapsclient.network.TileBitmapLoader;

import java.util.Collection;

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
        final Collection<DrawableTile> tiles =
                MapTileUtils.calculateMapTiles(
                        TILE_SIZE, 2, new PointD(512, 512), new PointD(10, 10)
                );

        Observable.just(tiles)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tilesView::setTiles);
    }
}
