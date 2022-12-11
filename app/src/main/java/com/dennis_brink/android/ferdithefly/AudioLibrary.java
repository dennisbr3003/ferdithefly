package com.dennis_brink.android.ferdithefly;

import android.content.Context;
import android.media.MediaPlayer;

public class AudioLibrary extends android.app.Application {

    private static Context mContext;
    private static MediaPlayer mediaPlayerTrack1;
    private static MediaPlayer mediaPlayerTrack3;
    private static boolean track1MediaPlaying = false;

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
