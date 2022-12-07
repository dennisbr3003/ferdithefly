package com.dennis_brink.android.ferdithefly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    TextView textViewResultInfo, textViewResultScore, textViewResultHighScore;
    AppCompatButton btnAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        textViewResultInfo = findViewById(R.id.textViewResultInfo);
        textViewResultScore = findViewById(R.id.textViewResultScore);
        textViewResultHighScore = findViewById(R.id.textViewResultHighScore);

        btnAgain = findViewById(R.id.btnResultPlayAgain);

        btnAgain.setOnClickListener(view -> {

        });

    }
}