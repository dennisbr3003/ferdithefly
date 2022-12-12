package com.dennis_brink.android.ferdithefly;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    TextView textViewResultInfo, textViewResultScore, textViewResultHighScore;
    ImageView imageViewEnd, imageViewWin, imageViewWinLoss;
    AppCompatButton btnAgain;

    int score;
    int highscore;
    String type; // type of ending (win or loss)
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        textViewResultInfo = findViewById(R.id.textViewResultInfo);
        textViewResultScore = findViewById(R.id.textViewResultScore);
        textViewResultHighScore = findViewById(R.id.textViewResultHighScore);

        imageViewEnd = findViewById(R.id.imageViewTombStone);
        imageViewWin = findViewById(R.id.imageViewWin);
        imageViewWinLoss = findViewById(R.id.imageViewTombStoneWin);

        btnAgain = findViewById(R.id.btnResultPlayAgain);

        score = getIntent().getIntExtra("score", 0);
        type = getIntent().getStringExtra("type");
        if(type.equals("")){
            type = "loss";
        }

        textViewResultScore.setText("Your Score: " + score);

        sharedPreferences = this.getSharedPreferences("ferdi", MODE_PRIVATE); // this is the name of the collection of preferences
        highscore = sharedPreferences.getInt("highscore", 0);

        if(score >= 500){ // 500+ is win
            if(score >= highscore) {
                imageViewWin.setVisibility(View.VISIBLE);
                textViewResultInfo.setText("Ferdi lives!\nHe's havin' a beer with his friends! Get one yourself, you earned it...");
                textViewResultHighScore.setText("High Score: " + score);
                sharedPreferences.edit().putInt("highscore", score).apply();
            }
        } else {
            if (score >= highscore){ // < 500 you died
                imageViewWinLoss.setVisibility(View.VISIBLE);
                textViewResultInfo.setText("Ferdi died; but that's ok because you have a new HighScore!\n LET'S CELEBRATE!");
                type="win";
                textViewResultHighScore.setText("High Score: " + score);
                sharedPreferences.edit().putInt("highscore", score).apply();
            } else {
                imageViewEnd.setVisibility(View.VISIBLE); // you died without an highscore
                textViewResultInfo.setText("Ferdi died because of you! Maybe you should just..go away!");
                textViewResultHighScore.setText("High Score: " + highscore);
            }
        }

        btnAgain.setOnClickListener(view -> {
            Intent i = new Intent(ResultActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            AudioLibrary.mediaPlayerTrack1Stop();
            if(type.equals("loss")){
                AudioLibrary.setupMediaPlayerTrack1(ResultActivity.this, R.raw.sinister);
            } else {
                AudioLibrary.setupMediaPlayerTrack1(ResultActivity.this, R.raw.win);
            }
        } catch (Exception e){
            Log.d("DENNIS_B", "Start mediaplayer error " + e.getLocalizedMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //AudioLibrary.mediaPlayerTrack1Stop();
    }
}