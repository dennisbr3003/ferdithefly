package com.dennis_brink.android.ferdithefly.models;

import android.widget.ImageView;
import com.dennis_brink.android.ferdithefly.IConstants;

public class CharacterConfig implements IConstants {

    private int current_speed;
    private int initial_speed = 0;
    private int restore_speed = 0;
    private ImageView imageView;
    private characterType type;

    public CharacterConfig(int current_speed, ImageView imageView, characterType type) {
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

    public void increase_speed(int speed) {
        this.restore_speed = this.current_speed;
        this.current_speed -= speed;
    }

    public void full_stop(int speed) {
        this.restore_speed = this.current_speed;
        this.current_speed = speed;
    }

    public void restart() {
        this.current_speed = this.restore_speed;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public characterType getType() {
        return type;
    }

}
