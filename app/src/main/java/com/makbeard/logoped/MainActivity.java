package com.makbeard.logoped;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.makbeard.logoped.model.TaleModel;
import com.makbeard.logoped.model.TalePart;

import java.util.LinkedList;

import butterknife.ButterKnife;
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

        ButterKnife.bind(this);

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

    @OnClick(R.id.testPlayer)
    public void startPlayerActivity() {

        // Make some dummy data for test
        LinkedList<TalePart> taleParts = new LinkedList<>();
        taleParts.add(new TalePart(
                ResourcesCompat.getDrawable(getResources(), R.drawable.nahod_mishonok_1, null).toString(),
                "Жили-были...",
                ""
        ));
        taleParts.add(new TalePart(
                ResourcesCompat.getDrawable(getResources(), R.drawable.nahod_mishonok_2, null).toString(),
                "...водку пили...",
                ""
        ));
        taleParts.add(new TalePart(
                ResourcesCompat.getDrawable(getResources(), R.drawable.nahod_mishonok_3, null).toString(),
                "...морду били...",
                ""
        ));
        taleParts.add(new TalePart(
                ResourcesCompat.getDrawable(getResources(), R.drawable.nahod_mishonok_4, null).toString(),
                "...так и прожили.",
                ""
        ));

        TaleModel tale = new TaleModel(
                "Сказка про алкашей",
                ResourcesCompat.getDrawable(getResources(), R.drawable.udach_rybalka_1, null).toString(),
                taleParts);

        startActivity(TalePlayer.makeIntent(this, tale));
    }
}
