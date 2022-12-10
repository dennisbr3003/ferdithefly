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
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    TextView textViewResultInfo, textViewResultScore, textViewResultHighScore;
    AppCompatButton btnAgain;

    int score;
    int highscore;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        textViewResultInfo = findViewById(R.id.textViewResultInfo);
        textViewResultScore = findViewById(R.id.textViewResultScore);
        textViewResultHighScore = findViewById(R.id.textViewResultHighScore);

        btnAgain = findViewById(R.id.btnResultPlayAgain);

        score = getIntent().getIntExtra("score", 0);
        textViewResultScore.setText("Your Score: " + score);

        sharedPreferences = this.getSharedPreferences("ferdi", MODE_PRIVATE); // this is the name of the collection of preferences
        highscore = sharedPreferences.getInt("highscore", 0);

        if(score >= 500){ // 200+ is win
            if(score >= highscore) {
                textViewResultInfo.setText("Ferdi lives! And you win with a new HighScore!");
                textViewResultHighScore.setText("High Score: " + score);
                sharedPreferences.edit().putInt("highscore", score).apply();
            }
        } else {
            if (score >= highscore){ // < 200 you died
                textViewResultInfo.setText("Ferdi died; but with a new HighScore! Time to celebrate!");
                textViewResultHighScore.setText("High Score: " + score);
                sharedPreferences.edit().putInt("highscore", score).apply();
            } else {
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
            AudioLibrary.setupMediaPlayerTrack1(ResultActivity.this, R.raw.sinister);
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