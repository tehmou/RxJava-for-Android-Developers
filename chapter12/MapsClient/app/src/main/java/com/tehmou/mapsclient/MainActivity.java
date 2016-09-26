package com.tehmou.mapsclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jakewharton.rxbinding.view.RxView;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;

import rx.Observable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TilesView tilesView = (TilesView) findViewById(R.id.tiles_view);
        final Collection<Tile> tiles =
                Arrays.asList(
                        new Tile(0, 0),
                        new Tile(1, 0),
                        new Tile(0, 1),
                        new Tile(1, 1)
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
