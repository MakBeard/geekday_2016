package com.makbeard.logoped;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.makbeard.logoped.model.TalePart;

import java.io.IOException;

/**
 * Created by Ivan on 09.07.2016.
 */
public class TaleAudioPlayer extends MediaPlayer{

    private MediaPlayer mediaPlayer;

    public void playTale (TalePart talePart) {
        try {
            mediaPlayer.setDataSource(talePart.getAudioLink());
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playExm (Context context) {

        mediaPlayer = MediaPlayer.create(context,R.raw.explosion);
        mediaPlayer.start();
    }
}
