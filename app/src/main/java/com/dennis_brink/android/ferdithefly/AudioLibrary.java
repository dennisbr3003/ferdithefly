package com.dennis_brink.android.ferdithefly;

import android.content.Context;
import android.media.MediaPlayer;

public class AudioLibrary extends android.app.Application {
    
    private static MediaPlayer mediaPlayerMainActivity;
    private static MediaPlayer mediaPlayerResultActivity;
    private static MediaPlayer mediaPlayerGameActivitySoundFx;
    private static boolean mediaPlayerMainActivityMuted = false;

    private static MediaPlayer mediaPlayerGameActivityMusic;

    //-- background music score start screen -------------------------------------------------------

    public static void mediaPlayerMainActivity(Context activity_context, int resource) {
        if(mediaPlayerMainActivity!=null) {
            if (mediaPlayerMainActivity.isPlaying() || mediaPlayerMainActivity.isLooping()) {
                mediaPlayerMainActivity.reset(); // if sound already playing then set back to 0 (cut off playing sound ad start new one)
            }
        }
        mediaPlayerMainActivity = MediaPlayer.create(activity_context, resource);
        mediaPlayerMainActivity.setLooping(true); // play the intro in
        if(mediaPlayerMainActivityMuted) {
            mediaPlayerMainActivityMute();
        }
        mediaPlayerMainActivity.start();
    }

    public static void mediaPlayerMainActivityMute(){
        mediaPlayerMainActivity.setVolume(0,0); // mute
        mediaPlayerMainActivityMuted = true;
    }

    public static void mediaPlayerMainActivityUnMute(){
        mediaPlayerMainActivity.setVolume(1,1); // un mute
        mediaPlayerMainActivityMuted = false;
    }

    public static void mediaPlayerMainActivityStop(){
        mediaPlayerMainActivity.reset(); // mute
    }

    public static boolean mediaPlayerMainActivityIsMuted(){
        return mediaPlayerMainActivityMuted;
    }

    //-- background music score start screen -------------------------------------------------------

    //-- background music score in game ------------------------------------------------------------

    public static void mediaPlayerGameActivityBackground(Context activity_context, int resource) {
        if(mediaPlayerGameActivityMusic !=null) {
            if (mediaPlayerGameActivityMusic.isPlaying() || mediaPlayerGameActivityMusic.isLooping()) {
                mediaPlayerGameActivityMusic.reset(); // if sound already playing then set back to 0 (cut off playing sound ad start new one)
            }
        }
        mediaPlayerGameActivityMusic = MediaPlayer.create(activity_context, resource);
        mediaPlayerGameActivityMusic.setLooping(true); // play the intro in
        mediaPlayerGameActivityMusic.start();
    }

    public static void mediaPlayerGameActivityBackgroundStop(){
        mediaPlayerGameActivityMusic.reset(); // mute
    }

    //-- background music score in game ------------------------------------------------------------

    //-- Sound FX in game==-------------------------------------------------------------------------

    public static void mediaPlayerGameActivitySoundFxStop(){
        mediaPlayerGameActivitySoundFx.reset(); // mute
    }

    // in-game sound-effects that do not need to loop
    public static void mediaPlayerGameActivitySoundFx(Context activity_context, int resource){
        mediaPlayerGameActivitySoundFx = MediaPlayer.create(activity_context, resource);
        mediaPlayerGameActivitySoundFx.setOnCompletionListener(MediaPlayer::reset);
        if(mediaPlayerGameActivitySoundFx.isPlaying()){
            mediaPlayerGameActivitySoundFx.seekTo(0); // if sound already playing then set back to 0 (cut off playing sound ad start new one)
        }
        mediaPlayerGameActivitySoundFx.start();
    }

    //-- Sound FX in game==-------------------------------------------------------------------------

    //-- background music score in result-----------------------------------------------------------

    public static void mediaPlayerResultActivityBackground(Context activity_context, int resource) {
        if(mediaPlayerResultActivity!=null) {
            if (mediaPlayerResultActivity.isPlaying() || mediaPlayerResultActivity.isLooping()) {
                mediaPlayerResultActivity.reset(); // if sound already playing then set back to 0 (cut off playing sound ad start new one)
            }
        }
        mediaPlayerResultActivity = MediaPlayer.create(activity_context, resource);
        mediaPlayerResultActivity.setLooping(true); // play the intro in
        mediaPlayerResultActivity.start();
    }

    public static void mediaPlayerResultActivityBackgroundStop(){
        mediaPlayerResultActivity.reset(); // mute
    }

    //-- background music score in result-----------------------------------------------------------

}
