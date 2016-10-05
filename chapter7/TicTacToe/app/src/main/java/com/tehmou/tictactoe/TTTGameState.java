package com.tehmou.tictactoe;

import rx.Observable;

public class TTTGameState {
    private final boolean isEnded;
    private final TTTSymbol winner;

    private TTTGameState(TTTGameStateBuilder builder) {
        this.isEnded = builder.isEnded;
        this.winner = builder.winner;
    }

    public boolean isEnded() {
        return isEnded;
    }

    public TTTSymbol getWinner() {
        return winner;
    }

    public static class TTTGameStateBuilder {
        private boolean isEnded;
        private TTTSymbol winner;

        public TTTGameStateBuilder() {

        }

        public TTTGameStateBuilder(TTTGameState gameState) {
            this.isEnded = gameState.isEnded;
            this.winner = gameState.winner;
        }

        public TTTGameStateBuilder setIsEnded(boolean isEnded) {
            this.isEnded = isEnded;
            return this;
        }

        public TTTGameStateBuilder setWinner(TTTSymbol winner) {
            this.winner = winner;
            return this;
        }

        public TTTGameState build() {
            return new TTTGameState(this);
        }
    }
}
