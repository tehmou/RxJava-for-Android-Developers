package com.tehmou.connectfour;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class GameStateView extends TextView {
    public GameStateView(Context context) {
        super(context);
    }

    public GameStateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GameStateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setData(GameState gameState) {
        this.setText("Ended: " + gameState.isEnded() + ", winner: " + gameState.getWinner());
    }
}
