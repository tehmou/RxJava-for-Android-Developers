package com.tehmou.connectfour;

public class GameGrid {
    private final GameSymbol[][] grid;
    private final GameSymbol playerInTurn;

    private GameGrid(GameGridBuilder builder) {
        this.grid = builder.grid;
        this.playerInTurn = builder.playerInTurn;
    }

    public GameSymbol getSymbolAt(GridPosition position) {
        return getSymbolAt(position.x, position.y);
    }

    public GameSymbol getSymbolAt(int x, int y) {
        return grid[x][y];
    }

    public int getWidth() {
        return grid.length;
    }

    public int getHeight() {
        return grid[0].length;
    }

    public GameSymbol getPlayerInTurn() {
        return playerInTurn;
    }

    public Boolean isInsideGrid(GridPosition position) {
        return position.getX() >= 0 && position.getX() < getWidth() &&
                position.getY() >= 0 && position.getY() < getHeight();
    }

    public Boolean isFull() {
        for (int i = 0; i < getWidth(); i++) {
            for (int n = 0; n < getHeight(); n++) {
                if (getSymbolAt(i, n) == GameSymbol.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    public static class GameGridBuilder {
        private static final int GRID_SIZE = 7;

        private GameSymbol[][] grid;
        private GameSymbol playerInTurn;

        public GameGridBuilder() {
            grid = new GameSymbol[GRID_SIZE][GRID_SIZE];
            for (int i = 0; i < GRID_SIZE; i++) {
                for (int n = 0; n < GRID_SIZE; n++) {
                    grid[i][n] = GameSymbol.EMPTY;
                }
            }
        }

        public GameGridBuilder(GameGrid gameState) {
            // Make a copy
            grid = new GameSymbol[GRID_SIZE][GRID_SIZE];
            for (int i = 0; i < GRID_SIZE; i ++) {
                System.arraycopy(gameState.grid[i], 0, grid[i], 0, GRID_SIZE);
            }
            playerInTurn = gameState.playerInTurn;
        }

        public GameGridBuilder setSymbol(GridPosition position, GameSymbol symbol) {
            return setSymbol(position.getX(), position.getY(), symbol);
        }

        public GameGridBuilder setSymbol(int x, int y, GameSymbol symbol) {
            grid[x][y] = symbol;
            return this;
        }

        public GameGridBuilder setPlayerInTurn(GameSymbol symbol) {
            this.playerInTurn = symbol;
            return this;
        }

        public GameGrid build() {
            return new GameGrid(this);
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
