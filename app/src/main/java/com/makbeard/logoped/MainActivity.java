package com.makbeard.logoped;

import android.content.Intent;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Button;

import butterknife.OnClick;

/**
 * Стартовая Activity приложения
 * Created 09.07.2016.
 */
public class MainActivity extends AppCompatActivity implements Const {

    private String[] chooseChild;
    private String[] chooseDoctor;
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


        final Spinner spinner = (Spinner) findViewById(R.id.name_spinner);

        final ArrayAdapter<?> adapterChild =
                ArrayAdapter.createFromResource(this, R.array.child, android.R.layout.simple_spinner_item);
        adapterChild.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final ArrayAdapter<?> adapterDoctors =
                ArrayAdapter.createFromResource(this, R.array.doctors, android.R.layout.simple_spinner_item);
        adapterDoctors.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chooseChild = getResources().getStringArray(R.array.child);
                chooseDoctor = getResources().getStringArray(R.array.doctors);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radiobutton1:
                        spinner.setAdapter(adapterDoctors);
                        break;
                    case R.id.radiobutton2:
                        spinner.setAdapter(adapterChild);
                }
            }
        });

    }

    @OnClick(R.id.enter_button)
    protected void onClickEnter() {
        Intent intent = new Intent(this, TaleChooserActivity.class);
        intent.putExtra(EXTRA_CHOOSE, chooseChild);
        startActivity(intent);
    }


}




/*    @OnClick(R.id.play_button)
    protected void play(){
        Log.d("happy", "Player enable");
        TaleAudioPlayer taleAudioPlayer = new TaleAudioPlayer();
        taleAudioPlayer.playExm(this);
    }*/


