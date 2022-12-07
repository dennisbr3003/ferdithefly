package com.dennis_brink.android.ferdithefly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ImageView imageViewFerdi, imageViewGerm, imageViewMine, imageViewBat, imageViewCoin, imageViewVolume;
    AppCompatButton btnStart;

    private Animation animation;
    private MediaPlayer mediaPlayer;
    private boolean media_playing = true;
    HashMap<String, ImageView> characters = new HashMap<>();

    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = findViewById(R.id.btnStart);
        imageViewFerdi = findViewById(R.id.imageViewFerdi);
        imageViewGerm = findViewById(R.id.imageViewGerm);
        imageViewMine = findViewById(R.id.imageViewMine);
        imageViewBat = findViewById(R.id.imageViewBat);
        imageViewCoin = findViewById(R.id.imageViewCoin);
        imageViewVolume = findViewById(R.id.imageViewVolume);

        loadCharacters();
        setCharacterAnimation();

    }

    private void loadCharacters(){
        characters.put("ferdi", imageViewFerdi);
        characters.put("germ", imageViewGerm);
        characters.put("mine", imageViewMine);
        characters.put("Bat", imageViewBat);
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

        if(mediaPlayer==null) {
            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.getready3);
        }
        mediaPlayer.setLooping(true); // play the intro in a loop (hopefully)
        mediaPlayer.start();

        handler = new Handler(); // bat animation
        runnable = () -> {
            if (imageViewBat.getTag().equals("bat_1")){
                imageViewBat.setImageResource(R.drawable.bat_2);
                imageViewBat.setTag("bat_2");
                imageViewFerdi.setImageResource(R.drawable.ferdi_1a);
                imageViewFerdi.setTag("bat_2");
            } else {
                imageViewBat.setImageResource(R.drawable.bat_1);
                imageViewBat.setTag("bat_1");
                imageViewFerdi.setImageResource(R.drawable.ferdi_2a);
                imageViewFerdi.setTag("bat_1");
            }
            handler.postDelayed(runnable, 80);
        };

        handler.post(runnable);

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
            handler.removeCallbacks(runnable);
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