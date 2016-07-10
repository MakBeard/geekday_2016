package com.makbeard.logoped;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.content.res.Resources;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.crash.FirebaseCrash;
import com.makbeard.logoped.model.TaleModel;
import com.makbeard.logoped.model.TalePart;

import java.io.File;
import java.io.FileOutputStream;
import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
//sss

/**
 * Стартовая Activity приложения
 * Created 09.07.2016.
 */
public class MainActivity extends AppCompatActivity implements Const {
    public static final String P = Manifest.permission.WRITE_EXTERNAL_STORAGE;



    @BindView(R.id.enter_button)
    Button enter_button;
    @BindView(R.id.main_activity)
    RelativeLayout relativeLayout;
    private String[] chooseChild;
    private String[] chooseDoctor;
    private AudioManager am;
    private Button play;
    private int chooseSomething;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(15000);  //Delay of 3 seconds
                } catch (Exception e) {
                }
                handler.sendEmptyMessage(0);
            }
        };
        t.start();


        doFileOperationsWrapper();

        ButterKnife.bind(this);


        am = (AudioManager) getSystemService(AUDIO_SERVICE);

        //test
        //  play = (Button) findViewById(R.id.play_button);
      /*  assert play != null;
        play.setOnClickListener(v -> {
            Log.d("happy", "Player enable");
            TaleAudioPlayer taleAudioPlayer = new TaleAudioPlayer();
            taleAudioPlayer.playExm(v.getContext());
        });*/

//выпадающие списки детей и врачей

 /*       final Spinner spinner = (Spinner) findViewById(R.id.name_spinner);

        final ArrayAdapter<?> adapterChild =
                ArrayAdapter.createFromResource(this, R.array.child, android.R.layout.simple_spinner_item);
        adapterChild.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final ArrayAdapter<?> adapterDoctors =
                ArrayAdapter.createFromResource(this, R.array.doctors, android.R.layout.simple_spinner_item);
        adapterDoctors.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        assert spinner != null;
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
       *//* radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {*//*
        assert radioGroup != null;
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radiobutton1:
                    spinner.setAdapter(adapterDoctors);
                    relativeLayout.setBackgroundResource(R.drawable.activity_main_background1);
                    chooseSomething = 1;
                    break;
                case R.id.radiobutton2:
                    spinner.setAdapter(adapterChild);
                    relativeLayout.setBackgroundResource(R.drawable.activity_main_background2);
                    chooseSomething = 2;
            }
        });*/

    }


    private Handler handler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            relativeLayout.setBackgroundResource(R.drawable.activity_main_background2);
        }
    };


    private void doFileOperationsWrapper() {

        // если прав нет

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, P)) {
            new AlertDialog.Builder(this)
                    .setMessage("Необходимо предоставить доступ в память для добавления сказок")
                    /*.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // здесь мы запросим права*/
                    .setPositiveButton("Ок", (dialog, which) -> {
                        // здесь мы запросим права
                        makeRequest();
                    })
                    /*.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {*/
                    .setNegativeButton("Нет", (dialog, which) -> {
                        // нам здесь делать нечего
                        new AlertDialog.Builder(MainActivity.this)
                                .setMessage("Добавление сказок не будет работать корректно")
                                .create()
                                .show();
                    })
                    .create()
                    .show();

            return; // сказали пользователю все, пусть решает

        }

        // просто запрашиваем права
        makeRequest();
    }

    private void makeRequest() {
        ActivityCompat.requestPermissions(this, new String[]{P}, 22);
    }

    @OnClick(R.id.enter_button)
    protected void onClickEnter() {
        startActivity(new Intent(MainActivity.this, TaleChooserActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


/*    @OnClick(R.id.enter_button)
    protected void onClickEnter() {
        switch (chooseSomething) {
            case 1:
                Intent intent = new Intent(this, ListChildActivity.class);
                intent.putExtra(EXTRA_CHOOSE, chooseDoctor);
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




*//*    @OnClick(R.id.play_button)
    protected void play(){
        Log.d("happy", "Player enable");
        TaleAudioPlayer taleAudioPlayer = new TaleAudioPlayer();
        taleAudioPlayer.playExm(this);
    }*//*


    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        getSupportActionBar().setHomeButtonEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_doctor:
                Intent intent = new Intent(this, ListChildActivity.class);
                intent.putExtra(EXTRA_CHOOSE, chooseDoctor);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            /*case R.id.action_child:
                Intent intent1 = new Intent(this, TaleChooserActivity.class);
                intent1.putExtra(EXTRA_CHOOSE, chooseChild);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;*/


        }

        return super.onOptionsItemSelected(item);
    }
}


