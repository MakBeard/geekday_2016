package com.makbeard.logoped;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import butterknife.OnClick;

/**
 * Стартовая Activity приложения
 * Created 09.07.2016.
 */
public class MainActivity extends AppCompatActivity implements Const{

    private String[] choose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Spinner spinner = (Spinner)findViewById(R.id.name_spinner);

        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(this, R.array.child, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choose = getResources().getStringArray(R.array.child);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @OnClick(R.id.enter_button)
    protected void onClickEnter() {
        Intent intent = new Intent(this, TaleChooserActivity.class);
        intent.putExtra(EXTRA_CHOOSE,choose);
        startActivity(intent);
    }
}
