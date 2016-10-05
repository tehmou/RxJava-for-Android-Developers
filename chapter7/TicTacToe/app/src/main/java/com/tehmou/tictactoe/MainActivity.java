package com.tehmou.tictactoe;

import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.jakewharton.rxbinding.view.RxView;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.BehaviorSubject;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_main);

        TTTGridView gridView = (TTTGridView) findViewById(R.id.grid_view);

        final TTTGameState emptyGame = new TTTGameState.TTTGameStateBuilder()
                .setPlayerInTurn(TTTSymbol.CIRCLE)
                .build();

        final Observable<TTTGameState.GridPosition> touchesOnGrid =
                getTouchesOnGrid(gridView, emptyGame.getWidth(), emptyGame.getHeight());

        final BehaviorSubject<TTTGameState> gameStateSubject = BehaviorSubject.create(emptyGame);

        final Observable<TTTGameState> gameStateUpdatesFromReset =
                RxView.clicks(findViewById(R.id.reset_button)).map(event -> emptyGame);

        final Observable<TTTGameState.GridPosition> allowedTouchesOnGrid =
                touchesOnGrid
                        .doOnNext(position -> Log.d(TAG, "Touching in position: " + position))
                        .withLatestFrom(gameStateSubject, Pair::new)
                        .filter(pair -> pair.second.isInsideGrid(pair.first))
                        .filter(pair -> pair.second.getSymbolAt(pair.first) == TTTSymbol.EMPTY)
                        .map(pair -> pair.first);

        final Observable<TTTGameState> gameStateUpdatesFromTouch =
                allowedTouchesOnGrid
                        .doOnNext(position -> Log.d(TAG, "Playing in position: " + position))
                        .withLatestFrom(
                                gameStateSubject,
                                (playerMove, gameStateValue) ->
                                new TTTGameState.TTTGameStateBuilder(gameStateValue)
                                        .setSymbol(playerMove, gameStateValue.getPlayerInTurn())
                                        .build())
                        .map(gameStateValue ->
                                new TTTGameState.TTTGameStateBuilder(gameStateValue)
                                        .setPlayerInTurn(
                                                gameStateValue.getPlayerInTurn() == TTTSymbol.CIRCLE ? TTTSymbol.CROSS : TTTSymbol.CIRCLE)
                                        .build());

        Observable.merge(
                gameStateUpdatesFromReset,
                gameStateUpdatesFromTouch
        ).subscribe(gameStateSubject::onNext);

        gameStateSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(gridView::setData);
    }

    private static Observable<TTTGameState.GridPosition> getTouchesOnGrid(View gridView,
                                                                          int gridWidth, int gridHeight) {
        return RxView.touches(gridView, motionEvent -> true)
                .doOnNext(ev -> Log.d(TAG, "touch: " + ev))
                .filter(ev -> ev.getAction() == MotionEvent.ACTION_UP)
                .map(ev -> {
                    float rx = ev.getX() / (float)(gridView.getWidth()+1);
                    int x = (int)(rx * gridWidth);

                    float ry = ev.getY() / (float)(gridView.getHeight()+1);
                    int y = (int)(ry * gridHeight);

                    return new TTTGameState.GridPosition(x, y);
                });
    }
}
