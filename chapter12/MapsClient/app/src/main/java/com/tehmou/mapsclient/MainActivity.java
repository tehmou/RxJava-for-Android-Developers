package com.tehmou.mapsclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tehmou.mapsclient.network.MapNetworkAdapter;
import com.tehmou.mapsclient.network.MapNetworkAdapterSimple;
import com.tehmou.mapsclient.network.NetworkClient;
import com.tehmou.mapsclient.network.NetworkClientOkHttp;
import com.tehmou.mapsclient.network.TileBitmapLoader;

import java.util.Arrays;
import java.util.Collection;

import rx.Observable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NetworkClient networkClient = new NetworkClientOkHttp();
        MapNetworkAdapter mapNetworkAdapter = new MapNetworkAdapterSimple(networkClient,
                "https://b.tile.openstreetmap.org/%d/%d/%d.png");
        TileBitmapLoader tileBitmapLoader = new TileBitmapLoader(mapNetworkAdapter);

        TilesView tilesView = (TilesView) findViewById(R.id.tiles_view);
        tilesView.setTileBitmapLoader(tileBitmapLoader);
        final Collection<Tile> tiles =
                Arrays.asList(
                        new Tile(0, 0, 1),
                        new Tile(1, 0, 1),
                        new Tile(0, 1, 1),
                        new Tile(1, 1, 1)
                );

        final int TILE_SIZE = 256;
        Observable.from(tiles)
                .map(tile ->
                        new DrawableTile(tile,
                                tile.getX() * TILE_SIZE,
                                tile.getY() * TILE_SIZE,
                                TILE_SIZE, TILE_SIZE))
                .toList()
                .subscribe(tilesView::setTiles);
    }
}
