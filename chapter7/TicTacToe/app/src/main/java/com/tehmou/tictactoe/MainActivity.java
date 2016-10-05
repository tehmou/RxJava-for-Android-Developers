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
        GameStateView gameStateView = (GameStateView) findViewById(R.id.game_state_view);

        final TTTGameGrid emptyGame = new TTTGameGrid.TTTGameGridBuilder()
                .setPlayerInTurn(TTTSymbol.CIRCLE)
                .build();

        // Create the placeholder for the gameGrid and gameState,
        // we need these because of the cyclic chain
        final BehaviorSubject<TTTGameGrid> gameGridSubject = BehaviorSubject.create(emptyGame);
        final BehaviorSubject<TTTGameState> gameStateSubject = BehaviorSubject.create();

        // Get state changes from the reset button that starts a new game
        final Observable<TTTGameGrid> gameStateUpdatesFromReset =
                RxView.clicks(findViewById(R.id.reset_button)).map(event -> emptyGame);

        // Get state changes based on the user playing the game
        final Observable<TTTGameGrid.GridPosition> touchesOnGrid =
                getTouchesOnGrid(gridView, emptyGame.getWidth(), emptyGame.getHeight());

        final Observable<TTTGameGrid> gameStateUpdatesFromTouch =
                getGameStateUpdatesFromTouch(touchesOnGrid, gameGridSubject);

        // Aggregate updates from all sources that can change the gameState
        Observable.merge(
                gameStateUpdatesFromReset,
                gameStateUpdatesFromTouch
        ).subscribe(gameGridSubject::onNext);

        gameGridSubject
                .map(MainActivity::calculateGameState)
                .subscribe(gameStateSubject::onNext);

        gameStateSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(gameStateView::setData);

        gameGridSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(gridView::setData);
    }

    private static TTTGameState calculateGameState(TTTGameGrid gameGrid) {
        return new TTTGameState.TTTGameStateBuilder()
                .setIsEnded(false)
                .setWinner(null)
                .build();
    }

    private static Observable<TTTGameGrid.GridPosition> getTouchesOnGrid(View gridView,
                                                                         int gridWidth, int gridHeight) {
        return RxView.touches(gridView, motionEvent -> true)
                .doOnNext(ev -> Log.d(TAG, "touch: " + ev))
                .filter(ev -> ev.getAction() == MotionEvent.ACTION_UP)
                .map(ev -> {
                    float rx = ev.getX() / (float)(gridView.getWidth()+1);
                    int x = (int)(rx * gridWidth);

                    float ry = ev.getY() / (float)(gridView.getHeight()+1);
                    int y = (int)(ry * gridHeight);

                    return new TTTGameGrid.GridPosition(x, y);
                });
    }

    private static Observable<TTTGameGrid.GridPosition> getAllowedTouchesOnGrid(Observable<TTTGameGrid.GridPosition> touchesOnGrid,
                                                                                Observable<TTTGameGrid> gameState) {
        return touchesOnGrid
                        .doOnNext(position -> Log.d(TAG, "Touching in position: " + position))
                        .withLatestFrom(gameState, Pair::new)
                        .filter(pair -> pair.second.isInsideGrid(pair.first))
                        .filter(pair -> pair.second.getSymbolAt(pair.first) == TTTSymbol.EMPTY)
                        .map(pair -> pair.first);

    }

    private static Observable<TTTGameGrid> getGameStateUpdatesFromTouch(Observable<TTTGameGrid.GridPosition> touchesOnGrid,
                                                                        Observable<TTTGameGrid> gameState) {
        return getAllowedTouchesOnGrid(touchesOnGrid, gameState)
                .doOnNext(position -> Log.d(TAG, "Playing in position: " + position))
                .withLatestFrom(
                        gameState,
                        (playerMove, gameStateValue) ->
                                new TTTGameGrid.TTTGameGridBuilder(gameStateValue)
                                        .setSymbol(playerMove, gameStateValue.getPlayerInTurn())
                                        .build())
                .map(gameStateValue ->
                        new TTTGameGrid.TTTGameGridBuilder(gameStateValue)
                                .setPlayerInTurn(
                                        gameStateValue.getPlayerInTurn() == TTTSymbol.CIRCLE ? TTTSymbol.CROSS : TTTSymbol.CIRCLE)
                                .build());
    }
}
