package com.dennis_brink.android.ferdithefly;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
                textViewResultInfo.setText("You Win with a new HighScore!");
                textViewResultHighScore.setText("High Score: " + score);
                sharedPreferences.edit().putInt("highscore", score).apply();
            }
        } else {
            if (score >= highscore){ // < 200 you died
                textViewResultInfo.setText("You died; but with a new HighScore!");
                textViewResultHighScore.setText("High Score: " + score);
                sharedPreferences.edit().putInt("highscore", score).apply();
            } else {
                textViewResultInfo.setText("You are not very good a this. Maybe you should consider a game change");
                textViewResultHighScore.setText("High Score: " + highscore);
            }
        }

        btnAgain.setOnClickListener(view -> {
            Intent i = new Intent(ResultActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        });

    }
}