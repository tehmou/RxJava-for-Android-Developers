package com.tehmou.tictactoe;

public class TTTGameState {
    private final TTTSymbol[][] grid;
    private final TTTSymbol playerInTurn;

    private TTTGameState(TTTGameStateBuilder builder) {
        this.grid = builder.grid;
        this.playerInTurn = builder.playerInTurn;
    }

    public TTTSymbol getSymbolAt(GridPosition position) {
        return getSymbolAt(position.x, position.y);
    }

    public TTTSymbol getSymbolAt(int x, int y) {
        return grid[x][y];
    }

    public int getWidth() {
        return grid.length;
    }

    public int getHeight() {
        return grid[0].length;
    }

    public TTTSymbol getPlayerInTurn() {
        return playerInTurn;
    }

    public Boolean isInsideGrid(GridPosition position) {
        return position.getX() >= 0 && position.getX() < getWidth() &&
                position.getY() >= 0 && position.getY() < getHeight();
    }

    public static class TTTGameStateBuilder {
        private TTTSymbol[][] grid;
        private TTTSymbol playerInTurn;

        public TTTGameStateBuilder() {
            grid = new TTTSymbol[3][3];
            for (int i = 0; i < 3; i++) {
                for (int n = 0; n < 3; n++) {
                    grid[i][n] = TTTSymbol.EMPTY;
                }
            }
        }

        public TTTGameStateBuilder(TTTGameState gameState) {
            // Make a copy
            grid = new TTTSymbol[3][3];
            for (int i = 0; i < 3; i ++) {
                System.arraycopy(gameState.grid[i], 0, grid[i], 0, 3);
            }
            playerInTurn = gameState.playerInTurn;
        }

        public TTTGameStateBuilder setSymbol(GridPosition position, TTTSymbol symbol) {
            return setSymbol(position.getX(), position.getY(), symbol);
        }

        public TTTGameStateBuilder setSymbol(int x, int y, TTTSymbol symbol) {
            grid[x][y] = symbol;
            return this;
        }

        public TTTGameStateBuilder setPlayerInTurn(TTTSymbol symbol) {
            this.playerInTurn = symbol;
            return this;
        }

        public TTTGameState build() {
            return new TTTGameState(this);
        }
    }


    public static class GridPosition {
        private final int x;
        private final int y;

        public GridPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public String toString() {
            return "GridPosition(" + x + ", " + y + ")";
        }
    }
}
