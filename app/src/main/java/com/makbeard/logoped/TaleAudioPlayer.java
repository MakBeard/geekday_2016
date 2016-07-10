package com.makbeard.logoped;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import com.makbeard.logoped.model.TalePart;

import java.io.IOException;

/**
 * Created by Ivan on 09.07.2016.
 */
public class TaleAudioPlayer extends MediaPlayer{

    private MediaPlayer mediaPlayer;
    private Context mContext;

    public TaleAudioPlayer(Context context) {

        this.mediaPlayer = MediaPlayer.create(context,R.raw.explosion);

    }

    public void playTale (TalePart talePart) {
        try {

            mediaPlayer = MediaPlayer.create(mContext,Uri.parse(talePart.getAudioLink()));
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void playExm (Context context) {

      //  mediaPlayer = MediaPlayer.create(context,R.raw.udach_rybalka_1);
        mediaPlayer.start();
    }
}
