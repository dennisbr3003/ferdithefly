package com.dennis_brink.android.ferdithefly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    ImageView imageViewGameFerdi, imageViewGameGerm, imageViewGameMine, imageViewGameBat,
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

    private Handler handlerFerdiExit;
    private Runnable runnableFerdiExit;

    //private Animation animation;
    float start = 0.8f;
    float end = 1f;
    // XY coordinates Ferdi, and all the other characters
    int ferdiX, characterX;
    int ferdiY, characterY;

    // screen dimensions
    int screenHeight;
    int screenWidth;

    int lives = 3;
    int score = 0;
    int interval = 0;

    int gamespeed = 40;
    int ferdisensitivity = 75;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        imageViewGameFerdi = findViewById(R.id.imageViewGameFerdi);
        imageViewGameGerm = findViewById(R.id.imageViewGameGerm);
        imageViewGameMine = findViewById(R.id.imageViewGameMine);
        imageViewGameBat = findViewById(R.id.imageViewGameBat);
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

                    enemyControl(imageViewGameGerm, 160, "");
                    enemyControl(imageViewGameMine, 180, "");
                    enemyControl(imageViewGameCoin, 140, "");
                    enemyControl(imageViewGameCoin2, 110, "");
                    enemyControl(imageViewGameBat, 120, "animation");

                    collisionControl(imageViewGameGerm, "enemy");
                    collisionControl(imageViewGameMine, "enemy");
                    collisionControl(imageViewGameBat, "enemy");
                    collisionControl(imageViewGameCoin, "reward");
                    collisionControl(imageViewGameCoin2, "reward");

                    evaluateGameStats();
                };
                handler.post(runnable);
            } else { // screen is touched two or more times
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

    public void enemyControl(ImageView character, int speed, String type){

        character.setVisibility(View.VISIBLE);

        characterX = (int)character.getX();
        characterY = (int)character.getY();

        characterX = characterX - (screenWidth / speed);
        if(characterX <= 0){
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

        // bat animation: use 2 times game speed for wings
        if (type.equals("animation")){
            interval += gamespeed;
            if (interval == (2 * gamespeed)){
                if (character.getTag().equals("bat_1")){
                    character.setImageResource(R.drawable.bat_2);
                    character.setTag("bat_2");
                } else {
                    character.setImageResource(R.drawable.bat_1);
                    character.setTag("bat_1");
                }
                interval = 0;
            }
        }

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

    public void evaluateGameStats(){
        if(lives > 0 && score < 200){
            if(lives == 2){
                imageViewLive1.setImageResource(R.drawable.ic_baseline_favorite_24_grey);
            }
            if(lives == 1){
                imageViewLive2.setImageResource(R.drawable.ic_baseline_favorite_24_grey);
            }
            handler.postDelayed(runnable, gamespeed);
        } else if(score >= 200) {

            // win: stop game
            handlerFerdi.removeCallbacks(runnableFerdi);
            handler.removeCallbacks(runnable);

            // set screen touch passive (Ferdi won't react anymore)
            constraintLayout.setEnabled(false);

            // hide everybody but Ferdi
            hideAllCharactersButFerdi();

            //Set a text indicating the game is won
            textViewTabToPlay.setVisibility(View.VISIBLE);
            textViewTabToPlay.setText("Congratulations! You won and Ferdi is safe...");

            handlerFerdiExit = new Handler();
            runnableFerdiExit = new Runnable() {
                @Override
                public void run() {
                    characterX = (int)imageViewGameFerdi.getX();
                    // Ferdi moves from left to right so +
                    characterX = characterX + (screenWidth / 300);
                    imageViewGameFerdi.setX((float) characterX);
                    imageViewGameFerdi.setY((float) screenHeight/2);
                    if(imageViewGameFerdi.getX() <= screenWidth) {
                        handlerFerdiExit.postDelayed(runnableFerdiExit, 20); // Ferdi is not there yet
                    } else { // Ferdi is off the screen so stop and show result
                        handlerFerdiExit.removeCallbacks(runnableFerdiExit);
                        startResult();
                    }

                }
            };
            handlerFerdiExit.post(runnableFerdiExit);

        } else if(lives==0){
            // loose: stop game
            handler.removeCallbacks(runnableFerdi);
            handler.removeCallbacks(runnable);
            imageViewLive3.setImageResource(R.drawable.ic_baseline_favorite_24_grey);
            startResult();
        }
    }

    public void startResult(){
        Intent i = new Intent(GameActivity.this, ResultActivity.class);
        i.putExtra("score", score);
        startActivity(i);
        finish();
    }

    private void hideAllCharactersButFerdi(){
        imageViewGameGerm.setVisibility(View.INVISIBLE);
        imageViewGameMine.setVisibility(View.INVISIBLE);
        imageViewGameCoin.setVisibility(View.INVISIBLE);
        imageViewGameCoin2.setVisibility(View.INVISIBLE);
        imageViewGameBat.setVisibility(View.INVISIBLE);
    }

    public void collisionControl(View view, String type){

        int centreViewX = (int) (view.getX() + (view.getWidth() / 2));
        int centreViewY = (int) (view.getY() + (view.getHeight() / 2));

        if(centreViewX >= imageViewGameFerdi.getX() &&
                centreViewX <= (imageViewGameFerdi.getX() + imageViewGameFerdi.getWidth()) &&
                centreViewY >= imageViewGameFerdi.getY() &&
                centreViewY <= (imageViewGameFerdi.getY() + imageViewGameFerdi.getHeight())){
            // collision ferdi and enemy or coin is a fact, the center of the enemy or coin is in the hit box of Ferdi
            // first move the enemy or coin of the screen
            view.setX(screenWidth + 200);
            // take a live
            if (type.equals("enemy")) {
                lives--;
                // set one heart to grey
            }
            if (type.equals("reward")){
                score += 50;
                textViewScore.setText(""+score);
            }

        }

    }

}