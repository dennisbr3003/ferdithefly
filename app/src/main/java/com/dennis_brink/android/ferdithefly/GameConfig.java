package com.dennis_brink.android.ferdithefly;

public class GameConfig extends android.app.Application {

    private boolean gameStarted = false;

    public boolean isGameStarted(){
        return gameStarted;
    }

    public void setGameStarted(boolean b){
        this.gameStarted = b;
    }
}
