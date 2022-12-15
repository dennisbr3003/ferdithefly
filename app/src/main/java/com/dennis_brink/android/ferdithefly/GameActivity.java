package com.dennis_brink.android.ferdithefly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.dennis_brink.android.ferdithefly.models.CharacterConfig;

import java.util.HashMap;

public class GameActivity extends AppCompatActivity implements IConstants {

    ImageView imageViewGameFerdi, imageViewGameGerm, imageViewGameMine, imageViewGameBat,
              imageViewGameMine2, imageViewGameWasp,
              imageViewLive1, imageViewLive2, imageViewLive3,
              imageViewGameCoin, imageViewGameCoin2, imageViewGameCoinScore, imageViewGamePause;
    TextView textViewTabToPlay, textViewScore, textViewCountdown;
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

    private Runnable runnableWaspAnimation;
    private Handler handlerWaspAnimation;

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

    boolean[] increase_level = {false,false,false};

    boolean movementUp = false;
    boolean movementExit = false;

    int gameSpeed = 40;
    int ferdiSensitivity = 75;
    int batAnimationSpeed = 80;
    int waspAnimationSpeed = 80;
    int ferdiAnimationSpeed = 40;
    int ferdiExitSpeed = 20;
    int ferdiExitInterval = 200;

    HashMap<characterKey, CharacterConfig> characters = new HashMap<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        imageViewGameFerdi = findViewById(R.id.imageViewGameFerdi);
        imageViewGameGerm = findViewById(R.id.imageViewGameGerm);
        imageViewGameMine = findViewById(R.id.imageViewGameMine);
        imageViewGameMine2 = findViewById(R.id.imageViewGameMine2);
        imageViewGameBat = findViewById(R.id.imageViewGameBat);
        imageViewGameWasp = findViewById(R.id.imageViewGameWasp);
        imageViewLive1 = findViewById(R.id.imageViewLive1);
        imageViewLive2 = findViewById(R.id.imageViewLive2);
        imageViewLive3 = findViewById(R.id.imageViewLive3);
        imageViewGameCoin = findViewById(R.id.imageViewGameCoin);
        imageViewGameCoin2 = findViewById(R.id.imageViewGameCoin2);
        imageViewGameCoinScore = findViewById(R.id.imageViewGameCoinScore);
        imageViewGamePause = findViewById(R.id.imageViewGamePause);

        textViewTabToPlay = findViewById(R.id.textViewStart);
        textViewScore = findViewById(R.id.textViewScore);
        textViewCountdown = findViewById(R.id.textViewCountdown);

        imageViewGamePause.setOnClickListener(view -> {
            if(GameConfig.isGamePaused()){
                restartGame();
            } else {
                pauseGame();
            }
        });

        setRotation(imageViewGameMine); // the mine should rotate
        setRotation(imageViewGameMine2); // this mine is not visible yet (will be at 300 pts)
        setScale(imageViewGameGerm); // the germ should pulse

        constraintLayout = findViewById(R.id.constraintLayout);

        constraintLayout.setOnTouchListener((view, motionEvent) -> {

            // hide 'tap to play'
            textViewTabToPlay.setVisibility(View.INVISIBLE);

            if(!beginControl){  // screen = touched for the first time
                beginControl = true;
                AudioLibrary.mediaPlayerGameActivityBackground(GameActivity.this, R.raw.ingame);
                screenWidth = (int)constraintLayout.getWidth();
                screenHeight = (int) constraintLayout.getHeight();

                loadCharacters();
                startFerdi();

                handler = new Handler();
                runnable = () -> {

                    characterControl();
                    characterCollisionHandler();
                    evaluateGameStats();

                };
                handler.post(runnable);

                startBatAnimation();
                startFerdiAnimation();
                startWaspAnimation();

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

    public void startFerdi(){

        ferdiX = (int)imageViewGameFerdi.getX();
        ferdiY = (int)imageViewGameFerdi.getY();

        handlerFerdi = new Handler();
        runnableFerdi= () -> {
            moveFerdi();
            handlerFerdi.postDelayed(runnableFerdi, ferdiSensitivity);
        };
        handlerFerdi.post(runnableFerdi);

    }

    @Override
    protected void onResume() {
        restartGame();
        super.onResume();
    }

    private void pauseGame(){
        Log.d(TAG, "pauseGame - Game paused");
        for (characterKey key : characters.keySet()) {
           characters.get(key).full_stop(screenWidth * 2);
        }
        handlerFerdi.removeCallbacks(runnableFerdi);
        AudioLibrary.mediaPlayerGameActivityBackgroundStop();
        GameConfig.setGamePaused(true);
        imageViewGamePause.setImageResource(android.R.drawable.ic_media_play);
    }

    private void restartGame(){
        Log.d(TAG, "restartGame - Game paused " + GameConfig.isGamePaused());
        try {
            if(GameConfig.isGamePaused()) {
                textViewCountdown.setVisibility(View.VISIBLE);
                textViewCountdown.bringToFront();
                new CountDownTimer(5000, 1000){

                    @Override
                    public void onTick(long l) {
                        textViewCountdown.setText(String.valueOf(l/1000));
                        if(textViewCountdown.getText().equals("3")){
                            AudioLibrary.mediaPlayerGameActivitySoundFx(GameActivity.this, R.raw.prepare_yourself);
                        }
                    }

                    @Override
                    public void onFinish() {
                        imageViewGamePause.setImageResource(android.R.drawable.ic_media_pause);
                        textViewCountdown.setVisibility(View.INVISIBLE);
                        for (characterKey key : characters.keySet()) {
                            characters.get(key).restart();
                        }
                        AudioLibrary.mediaPlayerGameActivityBackground(GameActivity.this, R.raw.ingame);
                        startFerdi();
                        GameConfig.setGamePaused(false);
                    }
                }.start();

            }
        } catch (Exception e){
            Log.d(TAG, "Error occurred restarting the game: " + e.getLocalizedMessage());
        }
    }

    private void characterCollisionHandler() {

        ImageView character;

        for (characterKey key : characters.keySet()) {

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
                if (characters.get(key).getType().equals(characterType.ENEMY)) {
                    if(key.equals(characterKey.MINE) || key.equals(characterKey.MINE2)){
                        AudioLibrary.mediaPlayerGameActivitySoundFx(GameActivity.this, R.raw.explosion);
                    } else {
                        AudioLibrary.mediaPlayerGameActivitySoundFx(GameActivity.this, R.raw.hit7);
                    }
                    lives--;
                }
                if (characters.get(key).getType().equals(characterType.REWARD)){
                    AudioLibrary.mediaPlayerGameActivitySoundFx(GameActivity.this, R.raw.reward);
                    score += COIN_VALUE;
                    textViewScore.setText(String.valueOf(score));
                }
            }
        }

    }

    private void characterControl() {

        ImageView character;

        for (characterKey key : characters.keySet()) {

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
        characters.put(characterKey.GERM, new CharacterConfig(150, imageViewGameGerm, characterType.ENEMY));
        characters.put(characterKey.MINE, new CharacterConfig(130, imageViewGameMine, characterType.ENEMY));
        characters.put(characterKey.COIN, new CharacterConfig(140, imageViewGameCoin, characterType.REWARD));
        characters.put(characterKey.COIN2, new CharacterConfig(110, imageViewGameCoin2, characterType.REWARD));
        characters.put(characterKey.BAT, new CharacterConfig(120, imageViewGameBat, characterType.ENEMY));
        characters.put(characterKey.WASP, new CharacterConfig(125, imageViewGameWasp, characterType.ENEMY));
    }

    private void startWaspAnimation(){

        handlerWaspAnimation = new Handler();
        runnableWaspAnimation = () -> { // you start with b1
            if (imageViewGameWasp.getTag().equals("b_1")){
                imageViewGameWasp.setImageResource(R.drawable.b2);
                imageViewGameWasp.setTag("b_2");
            } else if(imageViewGameWasp.getTag().equals("b_2")) {
                imageViewGameWasp.setImageResource(R.drawable.b3);
                imageViewGameWasp.setTag("b_3");
            } else if(imageViewGameWasp.getTag().equals("b_3")) {
                imageViewGameWasp.setImageResource(R.drawable.b4);
                imageViewGameWasp.setTag("b_4");
            } else if(imageViewGameWasp.getTag().equals("b_4")) {
                imageViewGameWasp.setImageResource(R.drawable.b5);
                imageViewGameWasp.setTag("b_5");
            } else if(imageViewGameWasp.getTag().equals("b_5")) {
                imageViewGameWasp.setImageResource(R.drawable.b6);
                imageViewGameWasp.setTag("b_6");
            }else if(imageViewGameWasp.getTag().equals("b_6")) {
                imageViewGameWasp.setImageResource(R.drawable.b1);
                imageViewGameWasp.setTag("b_1");
            }
            handlerWaspAnimation.postDelayed(runnableWaspAnimation, waspAnimationSpeed);
        };

        handlerWaspAnimation.post(runnableWaspAnimation);
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
            handlerBatAnimation.postDelayed(runnableBatAnimation, batAnimationSpeed);
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
            handlerFerdiAnimation.postDelayed(runnableFerdiAnimation, ferdiAnimationSpeed);
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

        AudioLibrary.mediaPlayerGameActivitySoundFx(GameActivity.this, R.raw.success);

        runnableFerdiExit = () -> {
            characterX = (int)imageViewGameFerdi.getX();
            // Ferdi moves from left to right so +
            movementExit = true; // start animation of wings here
            characterX = characterX + (screenWidth / ferdiExitInterval);
            imageViewGameFerdi.setX((float) characterX);
            imageViewGameFerdi.setY((float) screenHeight / 2);
            if(imageViewGameFerdi.getX() <= screenWidth) {
                handlerFerdiExit.postDelayed(runnableFerdiExit, ferdiExitSpeed); // Ferdi is not there yet
            } else { // Ferdi is off the screen so stop and show result
                handlerFerdiExit.removeCallbacks(runnableFerdiExit);
                handlerFerdiAnimation.removeCallbacks(runnableFerdiAnimation);
                startResult("win");
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
            handler.postDelayed(runnable, gameSpeed);
        } else if(score >= 500) {

            new CountDownTimer(1000, 500) {
                @Override
                public void onTick(long l) {
                    // wait for the final sound effect to finish
                }

                @Override
                public void onFinish() {

                    AudioLibrary.mediaPlayerGameActivitySoundFxStop(); // stop background music

                    // win: stop game
                    handlerFerdi.removeCallbacks(runnableFerdi);
                    handler.removeCallbacks(runnable);
                    // set screen touch passive (Ferdi won't react anymore)
                    constraintLayout.setEnabled(false);

                    // hide everybody but Ferdi
                    hideAllCharactersButFerdi();

                    //Set a text indicating the game is won
                    textViewTabToPlay.setVisibility(View.VISIBLE);
                    textViewTabToPlay.setText(R.string._youwin);

                    startFerdiExitAnimation();
                }
            }.start();


        } else if(lives==0){
            // loose: stop game

            new CountDownTimer(1000, 500) {
                @Override
                public void onTick(long l) {
                    // wait for the final sound effect to finish
                }

                @Override
                public void onFinish() {

                    AudioLibrary.mediaPlayerGameActivitySoundFxStop(); // stop background music
                    handler.removeCallbacks(runnableFerdi);
                    handler.removeCallbacks(runnable);
                    imageViewLive3.setImageResource(R.drawable.ic_baseline_favorite_24_grey);
                    startResult("loss");
                }
            }.start();

        }
    }

    private void adjustSpeed() {
        if(score >= 200 ){
            if(!increase_level[0]) {
                AudioLibrary.mediaPlayerGameActivitySoundFx(GameActivity.this, R.raw.speed_up);
                increaseCharacterSpeed(SPEED_INCREASE);
                increase_level[0]=true;
            }
        }
        if(score >= 300 ){
            if(!increase_level[1]) {
                AudioLibrary.mediaPlayerGameActivitySoundFx(GameActivity.this, R.raw.speed_up);
                // add the new extra mine to the game
                // next it will add  30 speed immediately so 115 will be good to start with
                characters.put(characterKey.MINE2,
                        new CharacterConfig(115, imageViewGameMine2, characterType.ENEMY));
                increaseCharacterSpeed(SPEED_INCREASE);
                increase_level[1]=true;
            }
        }
        if(score >= 400 ){
            if(!increase_level[2]) {
                AudioLibrary.mediaPlayerGameActivitySoundFx(GameActivity.this, R.raw.speed_up);
                increaseCharacterSpeed(SPEED_INCREASE);
                increase_level[2]=true;
            }
        }
    }

    public void startResult(String type){

        AudioLibrary.mediaPlayerGameActivityBackgroundStop(); // stop background music

        Intent i = new Intent(GameActivity.this, ResultActivity.class);
        i.putExtra("score", score);
        i.putExtra("type", type);
        startActivity(i);
        finish();
    }

    private void hideAllCharactersButFerdi(){

        for (characterKey key : characters.keySet()) {
            characters.get(key).getImageView().setVisibility(View.INVISIBLE);
        }

    }

    private void increaseCharacterSpeed(int speed){

        for (characterKey key : characters.keySet()) {
            if(!characters.get(key).getType().equals(characterType.REWARD)) {
                characters.get(key).increase_speed(speed);
            }
        }

    }

    @Override
    protected void onPause() {
        pauseGame();
        super.onPause();
    }

    @Override
    public void onBackPressed() {

    }
}