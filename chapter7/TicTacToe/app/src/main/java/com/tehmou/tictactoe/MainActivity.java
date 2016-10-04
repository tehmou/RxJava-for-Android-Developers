package com.tehmou.tictactoe;

import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;

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

        final TTTGameState gameState = new TTTGameState.TTTGameStateBuilder()
                .setPlayerInTurn(TTTSymbol.CIRCLE)
                .build();

        Observable<TTTGameState.GridPosition> touchesOnGrid =
            RxView.touches(gridView, motionEvent -> true)
                    .doOnNext(ev -> Log.d(TAG, "touch: " + ev))
                    .filter(ev -> ev.getAction() == MotionEvent.ACTION_UP)
                    .map(ev -> {
                        float rx = ev.getX() / (float)(gridView.getWidth()+1);
                        int x = (int)(rx * gameState.getWidth());

                        float ry = ev.getY() / (float)(gridView.getHeight()+1);
                        int y = (int)(ry * gameState.getHeight());

                        return new TTTGameState.GridPosition(x, y);
                    });

        BehaviorSubject<TTTGameState> gameStateSubject = BehaviorSubject.create(gameState);

        findViewById(R.id.reset_button)
                .setOnClickListener(
                        ev -> gameStateSubject.onNext(gameState));

        Observable<TTTGameState.GridPosition> allowedTouchesOnGrid =
                touchesOnGrid
                        .doOnNext(position -> Log.d(TAG, "Touching in position: " + position))
                        .withLatestFrom(gameStateSubject, Pair::new)
                        .filter(pair -> pair.second.isInsideGrid(pair.first))
                        .filter(pair -> pair.second.getSymbolAt(pair.first) == TTTSymbol.EMPTY)
                        .map(pair -> pair.first);

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
                                .build())
                .subscribe(gameStateSubject::onNext,
                        e -> Log.d(TAG, "Error processing gameState", e));

        gameStateSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(gridView::setData);
    }

    private static class PlayerMove {
        private final TTTGameState.GridPosition position;
        private final TTTSymbol symbol;


        public PlayerMove(TTTGameState.GridPosition position, TTTSymbol symbol) {
            this.position = position;
            this.symbol = symbol;
        }

        public TTTGameState.GridPosition getPosition() {
            return position;
        }

        public TTTSymbol getSymbol() {
            return symbol;
        }
    }
}
