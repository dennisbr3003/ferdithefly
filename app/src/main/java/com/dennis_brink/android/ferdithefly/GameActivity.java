package com.dennis_brink.android.ferdithefly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.dennis_brink.android.ferdithefly.models.CharacterConfig;

import java.util.HashMap;

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

    private Runnable runnableFerdiAnimation;
    private Handler handlerFerdiAnimation;

    private Runnable runnableBatAnimation;
    private Handler handlerBatAnimation;

    private Handler handlerFerdiExit;
    private Runnable runnableFerdiExit;

    private Handler handlerExplosion;
    private Runnable runnableExplosion;

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

    boolean[] increase_level = {false,false,false};

    boolean movementUp = false;
    boolean movementExit = false;

    int gamespeed = 40;
    int ferdisensitivity = 75;
    int batanimationspeed = 80;
    int ferdianimationspeed = 40;
    int ferdiexitspeed = 20;
    int ferdiexitinterval = 200;

    HashMap<String, CharacterConfig> characters = new HashMap<>();

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

                loadCharacters();

                handler = new Handler();
                runnable = () -> {

                    /*
                    enemyControl(imageViewGameGerm, 160);
                    enemyControl(imageViewGameMine, 180);
                    enemyControl(imageViewGameCoin, 140);
                    enemyControl(imageViewGameCoin2, 110);
                    enemyControl(imageViewGameBat, 120);
                    */
                    characterControl();
                    /*
                    collisionControl(imageViewGameGerm, "enemy");
                    collisionControl(imageViewGameMine, "enemy");
                    collisionControl(imageViewGameBat, "enemy");
                    collisionControl(imageViewGameCoin, "reward");
                    collisionControl(imageViewGameCoin2, "reward");
                    */

                    characterCollisionHandler();

                    evaluateGameStats();
                };
                handler.post(runnable);

                startBatAnimation();
                startFerdiAnimation();

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

    private void characterCollisionHandler() {

        ImageView character = null;

        for (String key : characters.keySet()) {

            character = characters.get(key).getImageView();
            int centreViewX = (int) (character.getX() + (character.getWidth() / 2));
            int centreViewY = (int) (character.getY() + (character.getHeight() / 2));

            if(centreViewX >= imageViewGameFerdi.getX() &&
                    centreViewX <= (imageViewGameFerdi.getX() + imageViewGameFerdi.getWidth()) &&
                    centreViewY >= imageViewGameFerdi.getY() &&
                    centreViewY <= (imageViewGameFerdi.getY() + imageViewGameFerdi.getHeight())){

                // collision ferdi and enemy or coin is a fact, the center of the enemy or coin is in the hit box of Ferdi
                // first move the enemy or coin of the screen
                character.setX(screenWidth + 200);
                // take a live
                if (characters.get(key).getType().equals("enemy")) {
                    if(key.equals("mine")){
                        Application.setupMediaPlayerTrack3(GameActivity.this, R.raw.explosion);
                    } else {
                        Application.setupMediaPlayerTrack3(GameActivity.this, R.raw.collision4);
                    }
                    lives--;
                }
                if (characters.get(key).getType().equals("reward")){
                    Application.setupMediaPlayerTrack4(GameActivity.this, R.raw.reward);
                    score += 50;
                    textViewScore.setText(""+score);
                }
            }
        }

    }

    private void characterControl() {

        ImageView character = null;

        for (String key : characters.keySet()) {

            character = characters.get(key).getImageView();
            character.setVisibility(View.VISIBLE);

            characterX = (int)character.getX();
            characterY = (int)character.getY();

            characterX = characterX - (screenWidth / characters.get(key).getCurrent_speed());
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
        }

    }

    private void loadCharacters(){
        characters.put("germ", new CharacterConfig(160, imageViewGameGerm, "enemy"));
        characters.put("mine", new CharacterConfig(180, imageViewGameMine, "enemy"));
        characters.put("coin_1", new CharacterConfig(140, imageViewGameCoin, "reward"));
        characters.put("coin_2", new CharacterConfig(110, imageViewGameCoin2, "reward"));
        characters.put("bat", new CharacterConfig(120, imageViewGameBat, "enemy"));
    }

    private void startBatAnimation(){

        handlerBatAnimation = new Handler();
        runnableBatAnimation = () -> {
            if (imageViewGameBat.getTag().equals("bat_1")){
                imageViewGameBat.setImageResource(R.drawable.bat_2);
                imageViewGameBat.setTag("bat_2");
            } else {
                imageViewGameBat.setImageResource(R.drawable.bat_1);
                imageViewGameBat.setTag("bat_1");
            }
            handlerBatAnimation.postDelayed(runnableBatAnimation, batanimationspeed);
        };

        handlerBatAnimation.post(runnableBatAnimation);
    }

    private void startFerdiAnimation(){

        handlerFerdiAnimation = new Handler();
        runnableFerdiAnimation = () -> {
            if (movementUp || movementExit) {
                if (imageViewGameFerdi.getTag().equals("bat_1")) {
                    imageViewGameFerdi.setImageResource(R.drawable.ferdi_1a);
                    imageViewGameFerdi.setTag("bat_2");
                } else {
                    imageViewGameFerdi.setImageResource(R.drawable.ferdi_2a);
                    imageViewGameFerdi.setTag("bat_1");
                }
            }
            handlerFerdiAnimation.postDelayed(runnableFerdiAnimation, ferdianimationspeed);
        };

        handlerFerdiAnimation.post(runnableFerdiAnimation);
    }

    public void moveFerdi(){
        // if the screen is touched the bird is going up
        // if the screen is released this bird is going down
        // 0,0 is top left so to let Ferdi go up we must deduct a
        // value from the current Y value
        if(touchControl){
            // up
            ferdiY = ferdiY - (screenWidth / 50);
            movementUp = true;
        } else {
            // down
            ferdiY = ferdiY + (screenWidth / 50);
            movementUp = false;
        }

        if(ferdiY <= 0){
            ferdiY = 0;
        }
        if (ferdiY > (screenHeight - imageViewGameFerdi.getHeight())){ // deduct Ferdi's height or it will still go off
            ferdiY = (screenHeight - imageViewGameFerdi.getHeight());
        }

        imageViewGameFerdi.setY((float) ferdiY);
    }
/*
    public void enemyControl(ImageView character, int speed){

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
    }
*/
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

    private void startFerdiExitAnimation(){

        handlerFerdiExit = new Handler();

        Application.setupMediaPlayerTrack2(GameActivity.this, R.raw.success);

        runnableFerdiExit = () -> {
            characterX = (int)imageViewGameFerdi.getX();
            // Ferdi moves from left to right so +
            movementExit = true; // start animation of wings here
            characterX = characterX + (screenWidth / ferdiexitinterval);
            imageViewGameFerdi.setX((float) characterX);
            imageViewGameFerdi.setY((float) screenHeight / 2);
            if(imageViewGameFerdi.getX() <= screenWidth) {
                handlerFerdiExit.postDelayed(runnableFerdiExit, ferdiexitspeed); // Ferdi is not there yet
            } else { // Ferdi is off the screen so stop and show result
                handlerFerdiExit.removeCallbacks(runnableFerdiExit);
                handlerFerdiAnimation.removeCallbacks(runnableFerdiAnimation);
                startResult();
            }
        };

        handlerFerdiExit.post(runnableFerdiExit);
    }

    public void evaluateGameStats(){

        adjustSpeed();

        if(lives > 0 && score < 500){
            if(lives == 2){
                imageViewLive1.setImageResource(R.drawable.ic_baseline_favorite_24_grey);
            }
            if(lives == 1){
                imageViewLive2.setImageResource(R.drawable.ic_baseline_favorite_24_grey);
            }
            handler.postDelayed(runnable, gamespeed);
        } else if(score >= 500) {

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

            startFerdiExitAnimation();

        } else if(lives==0){
            // loose: stop game
            handler.removeCallbacks(runnableFerdi);
            handler.removeCallbacks(runnable);
            imageViewLive3.setImageResource(R.drawable.ic_baseline_favorite_24_grey);
            startResult();
        }
    }

    private void adjustSpeed() {
        if(score >= 100 ){
            if(!increase_level[0]==true) {
                Application.setupMediaPlayerTrack2(GameActivity.this, R.raw.speed_up);
                increaseCharacterSpeed(20);
                increase_level[0]=true;
            }
        }
        if(score >= 200 ){
            if(!increase_level[1]==true) {
                Application.setupMediaPlayerTrack2(GameActivity.this, R.raw.speed_up);
                increaseCharacterSpeed(20);
                increase_level[1]=true;
            }
        }
        if(score >= 300 ){
            if(!increase_level[2]==true) {
                Application.setupMediaPlayerTrack2(GameActivity.this, R.raw.speed_up);
                increaseCharacterSpeed(15);
                increase_level[2]=true;
            }
        }
    }

    public void startResult(){
        Intent i = new Intent(GameActivity.this, ResultActivity.class);
        i.putExtra("score", score);
        startActivity(i);
        finish();
    }

    private void hideAllCharactersButFerdi(){

        for (String key : characters.keySet()) {
            characters.get(key).getImageView().setVisibility(View.INVISIBLE);
        }
        /*
        imageViewGameGerm.setVisibility(View.INVISIBLE);
        imageViewGameMine.setVisibility(View.INVISIBLE);
        imageViewGameCoin.setVisibility(View.INVISIBLE);
        imageViewGameCoin2.setVisibility(View.INVISIBLE);
        imageViewGameBat.setVisibility(View.INVISIBLE);
        */
    }

    private void increaseCharacterSpeed(int speed){

        for (String key : characters.keySet()) {
            characters.get(key).increase_speed(speed);
        }
        /*
        imageViewGameGerm.setVisibility(View.INVISIBLE);
        imageViewGameMine.setVisibility(View.INVISIBLE);
        imageViewGameCoin.setVisibility(View.INVISIBLE);
        imageViewGameCoin2.setVisibility(View.INVISIBLE);
        imageViewGameBat.setVisibility(View.INVISIBLE);
        */
    }

/*
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
*/
    @Override
    public void onBackPressed() {

        return;

    }
}