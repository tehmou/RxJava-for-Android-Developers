package com.tehmou.mapsclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TilesView tilesView = (TilesView) findViewById(R.id.tiles_view);
        final Collection<DrawableTile> drawableTiles =
                Arrays.asList(
                        new DrawableTile(0, 0, 0, 0, 256, 256),
                        new DrawableTile(1, 0, 256, 0, 256, 256),
                        new DrawableTile(0, 1, 0, 256, 256, 256),
                        new DrawableTile(1, 1, 256, 256, 256, 256)
                );
        tilesView.setTiles(drawableTiles);
    }
}
