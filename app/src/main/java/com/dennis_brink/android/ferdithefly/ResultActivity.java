package com.dennis_brink.android.ferdithefly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity implements IConstants {

    TextView textViewResultInfo, textViewResultScore, textViewResultHighScore;
    ImageView imageViewEnd, imageViewWin, imageViewWinLoss;
    AppCompatButton btnAgain;

    int score;
    int high_score;
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

        textViewResultScore.setText(String.format(
                getResources().getString(R.string._yourscore), ""+score));

        sharedPreferences = this.getSharedPreferences(SPDB_NAME, MODE_PRIVATE); // this is the name of the collection of preferences
        high_score = sharedPreferences.getInt(SPDB_FIELD, 0); // get saved high score

        if(score >= 500){ // 500+ is win
            if(score >= high_score) {
                imageViewWin.setVisibility(View.VISIBLE);
                textViewResultInfo.setText(R.string._win);
                textViewResultHighScore.setText(String.format(
                        getResources().getString(R.string._highscore), ""+score));
                sharedPreferences.edit().putInt(SPDB_FIELD, score).apply();
            }
        } else {
            if (score >= high_score){ // < 500 you died but with a high score
                imageViewWinLoss.setVisibility(View.VISIBLE);
                textViewResultInfo.setText(R.string._losswithhighscore);
                type="win";
                textViewResultHighScore.setText(String.format(
                        getResources().getString(R.string._highscore), ""+score));
                sharedPreferences.edit().putInt(SPDB_FIELD, score).apply(); // save high score
            } else {
                imageViewEnd.setVisibility(View.VISIBLE); // you died without an high score
                textViewResultInfo.setText(R.string._totalloss);
                textViewResultHighScore.setText(String.format(
                        getResources().getString(R.string._highscore), ""+high_score));
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
            if(type.equals("loss")){
                AudioLibrary.mediaPlayerResultActivityBackground(ResultActivity.this, R.raw.sinister);
            } else {
                AudioLibrary.mediaPlayerResultActivityBackground(ResultActivity.this, R.raw.win);
            }
        } catch (Exception e){
            Log.d(TAG, "Start media player error " + e.getLocalizedMessage());
        }
    }

    @Override
    protected void onPause() {
        AudioLibrary.mediaPlayerResultActivityBackgroundStop();
        super.onPause();
    }

}