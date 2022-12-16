package com.dennis_brink.android.ferdithefly;

public interface IConstants {
    enum characterType{
        ENEMY, REWARD
    }
    enum characterKey{
        MINE, MINE2, GERM, WASP, FERDI, BAT, COIN, COIN2
    }

    String TAG = "DENNIS_B";
    String SPDB_NAME = "ferdiTheFly";
    String SPDB_FIELD = "highScore";
    int COIN_VALUE = 10;
    int SPEED_INCREASE_A = 30;
    int SPEED_INCREASE_B = 25;

}
