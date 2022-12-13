package com.dennis_brink.android.ferdithefly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements IConstants {

    ImageView imageViewFerdi, imageViewGerm, imageViewMine, imageViewBat,
              imageViewCoin, imageViewVolume, imageViewWasp;
    AppCompatButton btnStart;

    private Animation animation;
    HashMap<characterKey, ImageView> characters = new HashMap<>();

    private Handler handler;
    private Runnable runnable;

    private Runnable runnableWaspAnimation;
    private Handler handlerWaspAnimation;

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
        imageViewWasp = findViewById(R.id.imageViewWasp);

        loadCharacters();
        setCharacterAnimation();

    }

    private void loadCharacters(){
        characters.put(characterKey.FERDI, imageViewFerdi);
        characters.put(characterKey.GERM, imageViewGerm);
        characters.put(characterKey.MINE, imageViewMine);
        characters.put(characterKey.BAT, imageViewBat);
        characters.put(characterKey.COIN, imageViewCoin);
        characters.put(characterKey.WASP, imageViewWasp);
    }

    private void setCharacterAnimation(){

        animation = AnimationUtils.loadAnimation(MainActivity.this,R.anim.scale_animation);

        for (characterKey key : characters.keySet()) {
            if(key != characterKey.MINE) {
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

        try {
            //AudioLibrary.mediaPlayerTrack1Stop();
            //AudioLibrary.setupMediaPlayerTrack1(MainActivity.this, R.raw.getready3);
            AudioLibrary.mediaPlayerMainActivity(MainActivity.this, R.raw.getready3);
        } catch (Exception e){
            Log.d(TAG, "Start mediaplayer error " + e.getLocalizedMessage());
        }

        handler = new Handler(); // bat/ferdi animation
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

        startWaspAnimation();

        // volume button (mute or on)
        imageViewVolume.setOnClickListener(view -> {
            //if(media_playing){
            //if(AudioLibrary.mediaPlayerTrack1IsPlaying()){
            //if(AudioLibrary.mediaPlayerMainActivityIsPlaying()){
            if(!AudioLibrary.mediaPlayerMainActivityIsMuted()){
                //mediaPlayer.setVolume(0,0); // mute
                //AudioLibrary.mediaPlayerTrack1Mute();
                AudioLibrary.mediaPlayerMainActivityMute();
                imageViewVolume.setImageResource(R.drawable.ic_baseline_volume_off_24);
                //media_playing = false;
            } else {
                //mediaPlayer.setVolume(1,1); // un mute
                //AudioLibrary.mediaPlayerTrack1UnMute();
                AudioLibrary.mediaPlayerMainActivityUnMute();
                imageViewVolume.setImageResource(R.drawable.ic_baseline_volume_up_24);
                //media_playing = true;
            }
        });

        btnStart.setOnClickListener(view -> {

            // close audio
            //mediaPlayer.reset();
            //AudioLibrary.mediaPlayerTrack1Stop();
            AudioLibrary.mediaPlayerMainActivityStop();
            imageViewVolume.setImageResource(R.drawable.ic_baseline_volume_up_24);
            // stop all threads
            handler.removeCallbacks(runnable);
            handlerWaspAnimation.removeCallbacks(runnableWaspAnimation);
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

    @Override
    protected void onPause() {
        AudioLibrary.mediaPlayerMainActivityStop();
        handler.removeCallbacks(runnable);
        handlerWaspAnimation.removeCallbacks(runnableWaspAnimation);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setMessage(R.string._quit);
        builder.setCancelable(false);
        builder.setNegativeButton(R.string._btnquit, (dialogInterface, i) -> {
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);  //0 = close normally, any other value is close abnormally
        });
        builder.setPositiveButton(R.string._btncancel, (dialogInterface, i) -> {
           dialogInterface.cancel();
           return;
        });
        builder.create().show();
    }

    private void startWaspAnimation(){

        handlerWaspAnimation = new Handler();
        runnableWaspAnimation = () -> { // you start with b1
            if (imageViewWasp.getTag().equals("b_1")){
                imageViewWasp.setImageResource(R.drawable.b2);
                imageViewWasp.setTag("b_2");
            } else if(imageViewWasp.getTag().equals("b_2")) {
                imageViewWasp.setImageResource(R.drawable.b3);
                imageViewWasp.setTag("b_3");
            } else if(imageViewWasp.getTag().equals("b_3")) {
                imageViewWasp.setImageResource(R.drawable.b4);
                imageViewWasp.setTag("b_4");
            } else if(imageViewWasp.getTag().equals("b_4")) {
                imageViewWasp.setImageResource(R.drawable.b5);
                imageViewWasp.setTag("b_5");
            } else if(imageViewWasp.getTag().equals("b_5")) {
                imageViewWasp.setImageResource(R.drawable.b6);
                imageViewWasp.setTag("b_6");
            }else if(imageViewWasp.getTag().equals("b_6")) {
                imageViewWasp.setImageResource(R.drawable.b1);
                imageViewWasp.setTag("b_1");
            }
            handlerWaspAnimation.postDelayed(runnableWaspAnimation, 100);
        };

        handlerWaspAnimation.post(runnableWaspAnimation);
    }
}