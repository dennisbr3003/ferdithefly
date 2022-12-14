package com.dennis_brink.android.ferdithefly;

import android.content.Context;
import android.media.MediaPlayer;

public class AudioLibrary extends android.app.Application {

    private static Context mContext;
    private static MediaPlayer mediaPlayerTrack1;
    private static MediaPlayer mediaPlayerTrack3;
    private static boolean track1MediaPlaying = false;
    private static MediaPlayer mediaPlayerMainActivity;
    private static MediaPlayer mediaPlayerResultActivity;
    private static boolean mediaPlayerMainActivityPlaying = false;
    private static boolean mediaPlayerMainActivityMuted = false;

    private static MediaPlayer mediaPlayerGameActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }

    public static void setupMediaPlayerTrack1(Context activity_context, int resource) {
        if(mediaPlayerTrack1!=null) {
            if (mediaPlayerTrack1.isPlaying() || mediaPlayerTrack1.isLooping()) {
                mediaPlayerTrack1.reset(); // if sound already playing then set back to 0 (cut off playing sound ad start new one)
            }
        }
        mediaPlayerTrack1 = MediaPlayer.create(activity_context, resource);
        mediaPlayerTrack1.setLooping(true); // play the intro in
        mediaPlayerTrack1.start();
    }

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
        mediaPlayerMainActivityPlaying = true;
    }

    public static void mediaPlayerMainActivityMute(){
        mediaPlayerMainActivity.setVolume(0,0); // mute
        mediaPlayerMainActivityMuted = true;
        mediaPlayerMainActivityPlaying = false;
    }

    public static void mediaPlayerMainActivityUnMute(){
        mediaPlayerMainActivity.setVolume(1,1); // unmute
        mediaPlayerMainActivityMuted = false;
        mediaPlayerMainActivityPlaying = true;
    }

    public static void mediaPlayerMainActivityStop(){
        mediaPlayerMainActivity.reset(); // mute
        mediaPlayerMainActivityPlaying = false;
    }

    //-- background music score start screan -------------------------------------------------------

    //-- background music score in game ------------------------------------------------------------

    public static void mediaPlayerGameActivityBackground(Context activity_context, int resource) {
        if(mediaPlayerGameActivity!=null) {
            if (mediaPlayerGameActivity.isPlaying() || mediaPlayerGameActivity.isLooping()) {
                mediaPlayerGameActivity.reset(); // if sound already playing then set back to 0 (cut off playing sound ad start new one)
            }
        }
        mediaPlayerGameActivity = MediaPlayer.create(activity_context, resource);
        mediaPlayerGameActivity.setLooping(true); // play the intro in
        mediaPlayerGameActivity.start();
    }

    public static void mediaPlayerGameActivityBackgroundStop(){
        mediaPlayerGameActivity.reset(); // mute
        track1MediaPlaying = false;
    }

    //-- background music score in game ------------------------------------------------------------

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

    public static void setupMediaPlayerTrack3(Context activity_context, int resource) {

        if(mediaPlayerTrack3!=null) {
            if (mediaPlayerTrack3.isPlaying() || mediaPlayerTrack3.isLooping()) {
                mediaPlayerTrack3.reset(); // if sound already playing then set back to 0 (cut off playing sound ad start new one)
            }
        }
        mediaPlayerTrack3 = MediaPlayer.create(activity_context, resource);
        mediaPlayerTrack3.setLooping(true); // play the intro in
        mediaPlayerTrack3.start();
    }

    public static void mediaPlayerTrack1Mute(){
        mediaPlayerTrack1.setVolume(0,0); // mute
        track1MediaPlaying = false;
    }

    public static void mediaPlayerTrack1UnMute(){
        mediaPlayerTrack1.setVolume(1,1); // mute
        track1MediaPlaying = true;
    }

    public static void mediaPlayerTrack1Stop(){
        mediaPlayerTrack1.reset(); // mute
        track1MediaPlaying = false;
    }

    public static void mediaPlayerTrack3Stop(){
        mediaPlayerTrack3.reset(); // mute
        track1MediaPlaying = false;
    }


    public static boolean mediaPlayerTrack1IsPlaying(){
        return track1MediaPlaying;
    }
    public static boolean mediaPlayerMainActivityIsPlaying(){
        return mediaPlayerMainActivityPlaying;
    }

    public static boolean mediaPlayerMainActivityIsMuted(){
        return mediaPlayerMainActivityMuted;
    }

    // in-game sound-effects that do not need to loop
    public static void setupMediaPlayerTrack2(Context activity_context, int resource){
        mediaPlayerTrack1 = MediaPlayer.create(activity_context, resource);
        mediaPlayerTrack1.setOnCompletionListener(mediaPlayer -> mediaPlayer.reset());
        if(mediaPlayerTrack1.isPlaying()){
            mediaPlayerTrack1.seekTo(0); // if sound already playing then set back to 0 (cut off playing sound ad start new one)
        }
        mediaPlayerTrack1.start();
    }

}
