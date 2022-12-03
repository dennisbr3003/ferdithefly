package com.dennis_brink.android.ferdithefly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageView imageViewFerdi, imageViewGerm, imageViewMine, imageViewWhirlwind, imageViewCoin, imageViewVolume;
    AppCompatButton btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = findViewById(R.id.btnStart);
        imageViewFerdi = findViewById(R.id.imageViewFerdi);
        imageViewGerm = findViewById(R.id.imageViewGerm);
        imageViewMine = findViewById(R.id.imageViewMine);
        imageViewWhirlwind = findViewById(R.id.imageViewWhirlwind);
        imageViewCoin = findViewById(R.id.imageViewCoin);
        imageViewVolume = findViewById(R.id.imageViewVolume);

    }
}