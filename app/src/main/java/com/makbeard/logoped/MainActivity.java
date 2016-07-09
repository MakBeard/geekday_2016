package com.makbeard.logoped;

import android.content.Intent;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import butterknife.OnClick;

/**
 * Стартовая Activity приложения
 * Created 09.07.2016.
 */
public class MainActivity extends AppCompatActivity {

    private AudioManager am;
    private Button play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        am = (AudioManager) getSystemService(AUDIO_SERVICE);

        //test
        play = (Button) findViewById(R.id.play_button);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("happy", "Player enable");
                TaleAudioPlayer taleAudioPlayer = new TaleAudioPlayer();
                taleAudioPlayer.playExm(v.getContext());
            }
        });


    }

    @OnClick(R.id.enter_button)
    protected void onClickEnter() {
        Intent intent = new Intent(this, TaleChooserActivity.class);
        startActivity(intent);
    }

/*    @OnClick(R.id.play_button)
    protected void play(){
        Log.d("happy", "Player enable");
        TaleAudioPlayer taleAudioPlayer = new TaleAudioPlayer();
        taleAudioPlayer.playExm(this);
    }*/
}
