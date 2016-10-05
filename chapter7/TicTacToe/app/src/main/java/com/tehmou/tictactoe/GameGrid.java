package com.tehmou.tictactoe;

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

    public static class GameGridBuilder {
        private GameSymbol[][] grid;
        private GameSymbol playerInTurn;

        public GameGridBuilder() {
            grid = new GameSymbol[3][3];
            for (int i = 0; i < 3; i++) {
                for (int n = 0; n < 3; n++) {
                    grid[i][n] = GameSymbol.EMPTY;
                }
            }
        }

        public GameGridBuilder(GameGrid gameState) {
            // Make a copy
            grid = new GameSymbol[3][3];
            for (int i = 0; i < 3; i ++) {
                System.arraycopy(gameState.grid[i], 0, grid[i], 0, 3);
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
