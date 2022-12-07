package com.dennis_brink.android.ferdithefly;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

public class Application extends android.app.Application {

    private static Context mContext;
    private static MediaPlayer mediaPlayerTrack1;
    private static boolean track1MediaPlaying = false;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }

    public static void setupMediaPlayerTrack1(Context activity_context){
        Log.d("DENNIS_B", "mediaplayer start mediaplayer alive: " + String.valueOf(mediaPlayerTrack1!=null));
        if(mediaPlayerTrack1==null) {
            mediaPlayerTrack1 = MediaPlayer.create(activity_context, R.raw.getready3);
        } else {
            Log.d("DENNIS_B", "media playing " + mediaPlayerTrack1.isPlaying());
            Log.d("DENNIS_B", "media is looping " + mediaPlayerTrack1.isLooping());
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

}
