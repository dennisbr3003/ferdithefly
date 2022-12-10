package com.dennis_brink.android.ferdithefly;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

public class Application extends android.app.Application {

    private static Context mContext;
    private static MediaPlayer mediaPlayerTrack1;
    private static boolean track1MediaPlaying = false;

    private static MediaPlayer mediaPlayerTrack2;
    private static MediaPlayer mediaPlayerTrack3;
    private static MediaPlayer mediaPlayerTrack4;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }

    public static void setupMediaPlayerTrack1(Context activity_context){
        if(mediaPlayerTrack1==null) {
            mediaPlayerTrack1 = MediaPlayer.create(activity_context, R.raw.getready3);
        } else {
            if(!mediaPlayerTrack1.isPlaying()) {
                mediaPlayerTrack1 = MediaPlayer.create(activity_context, R.raw.getready3);
            }
        }
        mediaPlayerTrack1.setLooping(true); // play the intro in
        mediaPlayerTrack1.start();
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

    public static boolean mediaPlayerTrack1IsPlaying(){
        return track1MediaPlaying;
    }

    // in-game sound-effects that do not need to loop
    public static void setupMediaPlayerTrack2(Context activity_context, int resource){
        mediaPlayerTrack2 = MediaPlayer.create(activity_context, resource);
        mediaPlayerTrack2.setOnCompletionListener(mediaPlayer -> mediaPlayer.reset());
        if(mediaPlayerTrack2.isPlaying()){
            mediaPlayerTrack2.seekTo(0); // if sound already playing then set back to 0 (cut off playing sound ad start new one)
        }
        mediaPlayerTrack2.start();
    }
    public static void setupMediaPlayerTrack3(Context activity_context, int resource){
        mediaPlayerTrack3 = MediaPlayer.create(activity_context, resource);
        mediaPlayerTrack3.setOnCompletionListener(mediaPlayer -> mediaPlayer.reset());
        if(mediaPlayerTrack3.isPlaying()){
            mediaPlayerTrack3.seekTo(0); // if sound already playing then set back to 0 (cut off playing sound ad start new one)
        }
        mediaPlayerTrack3.start();
    }
    public static void setupMediaPlayerTrack4(Context activity_context, int resource){
        mediaPlayerTrack4 = MediaPlayer.create(activity_context, resource);
        mediaPlayerTrack4.setOnCompletionListener(mediaPlayer -> mediaPlayer.reset());
        if(mediaPlayerTrack4.isPlaying()){
            mediaPlayerTrack4.seekTo(0); // if sound already playing then set back to 0 (cut off playing sound ad start new one)
        }
        mediaPlayerTrack4.start();
    }
}
