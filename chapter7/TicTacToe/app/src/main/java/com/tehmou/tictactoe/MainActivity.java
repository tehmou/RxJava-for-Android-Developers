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

        GameGridView gameGridView = (GameGridView) findViewById(R.id.grid_view);
        GameStateView gameStateView = (GameStateView) findViewById(R.id.game_state_view);

        final GameGrid emptyGame = new GameGrid.GameGridBuilder()
                .setPlayerInTurn(GameSymbol.CIRCLE)
                .build();

        // Create the placeholder for the gameGrid and gameState,
        // we need these because of the cyclic chain
        final BehaviorSubject<GameGrid> gameGridSubject = BehaviorSubject.create(emptyGame);
        final BehaviorSubject<GameState> gameStateSubject = BehaviorSubject.create();

        // Get state changes from the reset button that starts a new game
        final Observable<GameGrid> gameStateUpdatesFromReset =
                RxView.clicks(findViewById(R.id.reset_button)).map(event -> emptyGame);

        // Get state changes based on the user playing the game
        final Observable<GameGrid.GridPosition> touchesOnGrid =
                getTouchesOnGrid(gameGridView, emptyGame.getWidth(), emptyGame.getHeight());

        final Observable<GameGrid> gameStateUpdatesFromTouch =
                getGameStateUpdatesFromTouch(
                        touchesOnGrid, gameGridSubject, gameStateSubject);

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
                .subscribe(gameGridView::setData);
    }

    private static GameState calculateGameState(GameGrid gameGrid) {
        final GameSymbol winner = calculateWinnerForGrid(gameGrid);
        if (winner != null) {
            return new GameState.GameStateBuilder()
                    .setIsEnded(true)
                    .setWinner(winner)
                    .build();
        } else if (gameGrid.isFull()) {
            return new GameState.GameStateBuilder()
                    .setIsEnded(true)
                    .build();
        } else {
            return new GameState.GameStateBuilder()
                    .setIsEnded(false)
                    .build();
        }
    }

    private static Observable<GameGrid.GridPosition> getTouchesOnGrid(View gridView,
                                                                      int gridWidth, int gridHeight) {
        return RxView.touches(gridView, motionEvent -> true)
                .doOnNext(ev -> Log.d(TAG, "touch: " + ev))
                .filter(ev -> ev.getAction() == MotionEvent.ACTION_UP)
                .map(ev -> {
                    float rx = ev.getX() / (float)(gridView.getWidth()+1);
                    int x = (int)(rx * gridWidth);

                    float ry = ev.getY() / (float)(gridView.getHeight()+1);
                    int y = (int)(ry * gridHeight);

                    return new GameGrid.GridPosition(x, y);
                });
    }

    private static Observable<GameGrid.GridPosition> getAllowedTouchesOnGrid(Observable<GameGrid.GridPosition> touchesOnGrid,
                                                                             Observable<GameGrid> gameState) {
        return touchesOnGrid
                        .doOnNext(position -> Log.d(TAG, "Touching in position: " + position))
                        .withLatestFrom(gameState, Pair::new)
                        .filter(pair -> pair.second.isInsideGrid(pair.first))
                        .filter(pair -> pair.second.getSymbolAt(pair.first) == GameSymbol.EMPTY)
                        .map(pair -> pair.first);

    }

    private static Observable<GameGrid> getGameStateUpdatesFromTouch(Observable<GameGrid.GridPosition> touchesOnGrid,
                                                                     Observable<GameGrid> gameGrid,
                                                                     Observable<GameState> gameState) {
        return getAllowedTouchesOnGrid(touchesOnGrid, gameGrid)
                .doOnNext(position -> Log.d(TAG, "Playing in position: " + position))
                .flatMap(playerMove ->
                        Observable.just(playerMove)
                                .withLatestFrom(gameState, Pair::new)
                                .filter(pair -> !pair.second.isEnded())
                                .map(pair -> pair.first))
                .withLatestFrom(
                        gameGrid,
                        (playerMove, gameStateValue) ->
                                new GameGrid.GameGridBuilder(gameStateValue)
                                        .setSymbol(playerMove, gameStateValue.getPlayerInTurn())
                                        .build())
                .map(gameStateValue ->
                        new GameGrid.GameGridBuilder(gameStateValue)
                                .setPlayerInTurn(
                                        gameStateValue.getPlayerInTurn() == GameSymbol.CIRCLE ? GameSymbol.CROSS : GameSymbol.CIRCLE)
                                .build());
    }

    private static GameSymbol calculateWinnerForGrid(GameGrid gameGrid) {
        GameSymbol[] row;
        GameSymbol winner;

        // Horizontals
        for (int n = 0; n < gameGrid.getHeight(); n++) {
            row = new GameSymbol[3];
            for (int i = 0; i < gameGrid.getWidth(); i++) {
                row[i] = gameGrid.getSymbolAt(i, n);
            }
            winner = calculateWinnerForRow(row);
            if (winner != null) {
                return winner;
            }
        }

        // Verticals
        for (int i = 0; i < gameGrid.getWidth(); i++) {
            row = new GameSymbol[3];
            for (int n = 0; n < gameGrid.getHeight(); n++) {
                row[i] = gameGrid.getSymbolAt(i, n);
            }
            winner = calculateWinnerForRow(row);
            if (winner != null) {
                return winner;
            }
        }

        // Diagonals
        row = new GameSymbol[3];
        for (int i = 0; i < gameGrid.getWidth(); i++) {
            for (int n = 0; n < gameGrid.getHeight(); n++) {
                row[i] = gameGrid.getSymbolAt(i, n);
            }
        }
        winner = calculateWinnerForRow(row);
        if (winner != null) {
            return winner;
        }

        row = new GameSymbol[3];
        for (int i = gameGrid.getWidth() - 1; i >= 0; i--) {
            for (int n = 0; n < gameGrid.getHeight(); n++) {
                row[i] = gameGrid.getSymbolAt(i, n);
            }
        }
        winner = calculateWinnerForRow(row);
        if (winner != null) {
            return winner;
        }

        return GameSymbol.EMPTY;
    }

    private static GameSymbol calculateWinnerForRow(GameSymbol[] row) {
        GameSymbol lastGameSymbol = null;
        for (GameSymbol gameSymbol : row) {
            if (gameSymbol == GameSymbol.EMPTY) {
                return null;
            } else if (lastGameSymbol == null) {
                lastGameSymbol = gameSymbol;
            } else if (lastGameSymbol != gameSymbol) {
                return null;
            }
        }
        return lastGameSymbol;
    }
}
