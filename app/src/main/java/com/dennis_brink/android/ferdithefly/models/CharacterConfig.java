package com.dennis_brink.android.ferdithefly.models;

import android.util.Log;
import android.widget.ImageView;

public class CharacterConfig {

    private int current_speed = 0;
    private int initial_speed = 0;
    private ImageView imageView;
    private String type = "";

    public CharacterConfig(int current_speed, ImageView imageView, String type) {
        this.current_speed = current_speed;
        this.imageView = imageView;
        this.type = type;
        if(this.initial_speed == 0){
            this.initial_speed = current_speed;
        }
    }

    public int getCurrent_speed() {
        return current_speed;
    }

    public void setCurrent_speed(int current_speed) {
        this.current_speed = current_speed;
    }

    public void increase_speed(int speed) {
        Log.d("DENNIS_B", "current speed " + this.current_speed);
        this.current_speed -= speed;
        Log.d("DENNIS_B", "new current speed " + this.current_speed);
    }

    public void decrease_speed(int speed) {
        this.current_speed += speed;
    }

    public void fullstop_speed() {
        this.current_speed = 0;
    }

    public void reset_speed() {
        this.current_speed = this.initial_speed;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
