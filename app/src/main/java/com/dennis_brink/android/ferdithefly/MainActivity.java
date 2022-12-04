package com.dennis_brink.android.ferdithefly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ImageView imageViewFerdi, imageViewGerm, imageViewMine, imageViewWhirlwind, imageViewCoin, imageViewVolume;
    AppCompatButton btnStart;

    private Animation animation;

    HashMap<String, ImageView> characters = new HashMap<>();

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

        loadCharacters();
        setCharacterAnimation();

    }

    private void loadCharacters(){
        characters.put("ferdi", imageViewFerdi);
        characters.put("germ", imageViewGerm);
        characters.put("mine", imageViewMine);
        characters.put("wirlwind", imageViewWhirlwind);
        characters.put("coin", imageViewCoin);
    }

    private void setCharacterAnimation(){

        animation = AnimationUtils.loadAnimation(MainActivity.this,R.anim.scale_animation);

        for (String key : characters.keySet()) {
            characters.get(key).setAnimation(animation);
        }

    }

}