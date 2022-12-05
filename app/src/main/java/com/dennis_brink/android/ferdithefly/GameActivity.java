package com.dennis_brink.android.ferdithefly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    ImageView imageViewGameFerdi, imageViewGameGerm, imageViewGameMine, imageViewGameWhirlwind,
              imageViewLive1, imageViewLive2, imageViewLive3,
              imageViewGameCoin, imageViewGameCoin2, imageViewGameCoinScore;
    TextView textViewTabToPlay, textViewScore;
    ConstraintLayout constraintLayout;
    private boolean touchControl = false;
    private boolean beginControl = false;

    private Runnable runnable;
    private Handler handler;

    private Runnable runnableFerdi;
    private Handler handlerFerdi;


    //private Animation animation;
    float start = 0.8f;
    float end = 1f;
    // XY coordinates Ferdi, and all the other characters
    int ferdiX, characterX;
    int ferdiY, characterY;

    // screen dimensions
    int screenHeight;
    int screenWidth;

    int gamespeed = 40;
    int ferdisensitivity = 60;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        imageViewGameFerdi = findViewById(R.id.imageViewGameFerdi);
        imageViewGameGerm = findViewById(R.id.imageViewGameGerm);
        imageViewGameMine = findViewById(R.id.imageViewGameMine);
        imageViewGameWhirlwind = findViewById(R.id.imageViewGameWhirlwind);
        imageViewLive1 = findViewById(R.id.imageViewLive1);
        imageViewLive2 = findViewById(R.id.imageViewLive2);
        imageViewLive3 = findViewById(R.id.imageViewLive3);
        imageViewGameCoin = findViewById(R.id.imageViewGameCoin);
        imageViewGameCoin2 = findViewById(R.id.imageViewGameCoin2);
        imageViewGameCoinScore = findViewById(R.id.imageViewGameCoinScore);

        textViewTabToPlay = findViewById(R.id.textViewStart);
        textViewScore = findViewById(R.id.textViewScore);

        setRotation(imageViewGameMine); // the mine should rotate
        setScale(imageViewGameGerm); // the germ should pulse

        constraintLayout = findViewById(R.id.constraintLayout);

        constraintLayout.setOnTouchListener((view, motionEvent) -> {

            // hide 'tap to play'
            textViewTabToPlay.setVisibility(View.INVISIBLE);

            if(!beginControl){  // screen = touched for the first time
                beginControl = true;

                screenWidth = (int)constraintLayout.getWidth();
                screenHeight = (int) constraintLayout.getHeight();

                ferdiX = (int)imageViewGameFerdi.getX();
                ferdiY = (int)imageViewGameFerdi.getY();

                handlerFerdi = new Handler();
                runnableFerdi= () -> {
                    moveFerdi();
                    handlerFerdi.postDelayed(runnableFerdi, ferdisensitivity);
                };
                handlerFerdi.post(runnableFerdi);

                handler = new Handler();
                runnable = () -> {

                    enemyControl(imageViewGameGerm, 160);
                    enemyControl(imageViewGameMine, 180);
                    enemyControl(imageViewGameCoin, 140);
                    enemyControl(imageViewGameCoin2, 110);
                    enemyControl(imageViewGameWhirlwind, 120);

                    handler.postDelayed(runnable, gamespeed);
                };
                handler.post(runnable);
            } else { // screen is touched one or more times
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){ // touchscreen event is now active
                    touchControl = true;
                }
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){ // touchscreen event has just ended
                    touchControl = false;
                }
            }
            return true;
        });

    }

    public void moveFerdi(){
        // if the screen is touched the bird is going up
        // if the screen is released this bird is going down
        // 0,0 is top left so to let Ferdi go up we must deduct a
        // value from the current Y value
        if(touchControl){
            // up
            ferdiY = ferdiY - (screenWidth / 50);
        } else {
            // down
            ferdiY = ferdiY + (screenWidth / 50);
        }

        if(ferdiY <= 0){
            ferdiY = 0;
        }
        if (ferdiY > (screenHeight - imageViewGameFerdi.getHeight())){ // deduct Ferdi's height or it will still go off
            ferdiY = (screenHeight - imageViewGameFerdi.getHeight());
        }

        imageViewGameFerdi.setY((float) ferdiY);
    }

    public void enemyControl(ImageView character, int speed){

        character.setVisibility(View.VISIBLE);

        characterX = (int)character.getX();
        characterY = (int)character.getY();

        characterX = characterX - (screenWidth / speed);
        if(characterX < -200){ // make sur the just don't disappear on the edge
            characterX = screenWidth + 200; // set it back completely to the left plus an extra 200 to get completely off the screen
            characterY = (int) Math.floor(Math.random() * screenHeight); // reset Y-axis position to a random coordinate (so it starts at a different height)
            if(characterY <= 0){ // prevent an enemy of going off the screen via the Y-axis
                characterY = 0;
            }
            if (characterY > (screenHeight - character.getHeight())){ // deduct the character's height or it will still go off the screen
                characterY = (screenHeight - character.getHeight());
            }
        }
        character.setX((float)characterX);
        character.setY((float)characterY);

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

    public void setScale(View view){
        view.animate().scaleX(start).scaleY(start).withEndAction(new Runnable() {
            @Override
            public void run() {
                // end of shrinking so enlarge again
                view.animate().scaleX(end).scaleY(end).setDuration(1000).withEndAction(this).setInterpolator(new LinearInterpolator()).start();
                if(end == 1){
                    end = start;
                } else {
                    end = 1;
                }
            }
        }).setDuration(1000).setInterpolator(new LinearInterpolator()).start();
    }


}