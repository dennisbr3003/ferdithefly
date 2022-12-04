package com.dennis_brink.android.ferdithefly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    ImageView imageViewGameFerdi, imageViewGameGerm, imageViewGameMine, imageViewGameWhirlwind,
              imageViewLive1, imageViewLive2, imageViewLive3,
              imageViewGameCoin, imageViewGameCoin2, imageViewGameCoinScore;
    TextView textViewTabToPlay, textViewScore;
    ConstraintLayout constraintLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        imageViewGameFerdi = findViewById(R.id.imageViewGameFerdi);
        imageViewGameGerm = findViewById(R.id.imageViewGameGerm);
        imageViewGameMine = findViewById(R.id.imageViewGameMine);
        imageViewGameWhirlwind = findViewById(R.id.imageViewGameWhirlwind);
        imageViewLive1 = findViewById(R.id.imageViewLive1);
        imageViewLive2 = findViewById(R.id.imageViewLive2);
        imageViewLive3 = findViewById(R.id.imageViewLive3);
        imageViewGameCoin = findViewById(R.id.imageViewGameCoin);
        imageViewGameCoin2 = findViewById(R.id.imageViewGameCoin2);
        imageViewGameCoinScore = findViewById(R.id.imageViewGameCoinScore);

        textViewTabToPlay = findViewById(R.id.textViewStart);
        textViewScore = findViewById(R.id.textViewScore);

        constraintLayout = findViewById(R.id.constraintLayout);

    }
}