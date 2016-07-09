package com.makbeard.logoped;

import android.app.Activity;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Стартовая Activity приложения
 * Created 09.07.2016.
 */
public class MainActivity extends AppCompatActivity implements Const {

    @BindView(R.id.enter_button)
    Button enter_button;
    private String[] chooseChild;
    private String[] chooseDoctor;
    private AudioManager am;
    private Button play;
    private int chooseSomething;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

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

//выпадающие списки детей и врачей

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

//переключатель между списками
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radiobutton1:
                        spinner.setAdapter(adapterDoctors);
                        chooseSomething=1;
                        break;
                    case R.id.radiobutton2:
                        spinner.setAdapter(adapterChild);
                        chooseSomething=2;
                }
            }
        });

    }

    @OnClick(R.id.enter_button)
    protected void onClickEnter() {
        switch (chooseSomething){
            case 1:
                Intent intent = new Intent(this, ListChildActivity.class);
                intent.putExtra(EXTRA_CHOOSE, chooseChild);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case 2:
                Intent intent1 = new Intent(this, TaleChooserActivity.class);
                intent1.putExtra(EXTRA_CHOOSE, chooseChild);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                break;
        }

    }


}




/*    @OnClick(R.id.play_button)
    protected void play(){
        Log.d("happy", "Player enable");
        TaleAudioPlayer taleAudioPlayer = new TaleAudioPlayer();
        taleAudioPlayer.playExm(this);
    }*/


