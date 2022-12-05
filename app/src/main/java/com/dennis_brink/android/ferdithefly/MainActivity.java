package com.dennis_brink.android.ferdithefly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ImageView imageViewFerdi, imageViewGerm, imageViewMine, imageViewWhirlwind, imageViewCoin, imageViewVolume;
    AppCompatButton btnStart;

    private Animation animation;
    private MediaPlayer mediaPlayer;
    private boolean media_playing = true;
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
            if(key != "mine") {
                characters.get(key).setAnimation(animation);
            } else {
                characters.get(key).setAnimation(animation);
                setRotation(imageViewMine);
            }
        }

    }

    @Override
    protected void onResume() {

        super.onResume();

        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.getready3);
        mediaPlayer.setLooping(true); // play the intro in a loop (hopefully)
        mediaPlayer.start();

        // volume button (mute or on)
        imageViewVolume.setOnClickListener(view -> {
            if(media_playing){
                mediaPlayer.setVolume(0,0); // mute
                imageViewVolume.setImageResource(R.drawable.ic_baseline_volume_off_24);
                media_playing = false;
            } else {
                mediaPlayer.setVolume(1,1); // un mute
                imageViewVolume.setImageResource(R.drawable.ic_baseline_volume_up_24);
                media_playing = true;
            }
        });

        btnStart.setOnClickListener(view -> {

            // close audio
            mediaPlayer.reset();
            imageViewVolume.setImageResource(R.drawable.ic_baseline_volume_up_24);
            // start game activity
            Intent i = new Intent(MainActivity.this, GameActivity.class);
            startActivity(i);

        });

    }

    public void setRotation(View view){
        view.animate().rotationBy(-360).withEndAction(new Runnable() {
            @Override
            public void run() {
                // animation ends, so start again
                view.animate().rotationBy(-360).withEndAction(this).setDuration(3000).setInterpolator(new LinearInterpolator()).start();
            }
        }).setDuration(3000).setInterpolator(new LinearInterpolator()).start();
    }



}