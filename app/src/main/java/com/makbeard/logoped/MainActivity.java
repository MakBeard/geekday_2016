package com.makbeard.logoped;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Стартовая Activity приложения
 * Created 09.07.2016.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.enter_button)
    protected void onClickEnter() {
        Intent intent = new Intent(this, TaleCreatingActivity.class);
        startActivity(intent);
    }
}
