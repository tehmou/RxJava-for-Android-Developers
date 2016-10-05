package com.tehmou.tictactoe;

public class GameState {
    private final boolean isEnded;
    private final GameSymbol winner;

    private GameState(GameStateBuilder builder) {
        this.isEnded = builder.isEnded;
        this.winner = builder.winner;
    }

    public boolean isEnded() {
        return isEnded;
    }

    public GameSymbol getWinner() {
        return winner;
    }

    public static class GameStateBuilder {
        private boolean isEnded;
        private GameSymbol winner;

        public GameStateBuilder() {

        }

        public GameStateBuilder(GameState gameState) {
            this.isEnded = gameState.isEnded;
            this.winner = gameState.winner;
        }

        public GameStateBuilder setIsEnded(boolean isEnded) {
            this.isEnded = isEnded;
            return this;
        }

        public GameStateBuilder setWinner(GameSymbol winner) {
            this.winner = winner;
            return this;
        }

        public GameState build() {
            return new GameState(this);
        }
    }
}
