package com.dennis_brink.android.ferdithefly;

public class GameState extends android.app.Application {

    private static boolean gamePaused = false;

    public static boolean isGamePaused(){
        return gamePaused;
    }

    public static void setGamePaused(boolean b){
        gamePaused = b;
    }
}
