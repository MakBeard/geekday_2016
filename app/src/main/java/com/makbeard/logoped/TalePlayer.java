package com.makbeard.logoped;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.makbeard.logoped.db.DbOpenHelper;
import com.makbeard.logoped.db.tables.TalesTable;
import com.makbeard.logoped.model.TaleModel;
import com.makbeard.logoped.model.TaleModelSQLiteTypeMapping;
import com.makbeard.logoped.model.TalePart;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.lang.reflect.Type;
import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TalePlayer extends AppCompatActivity {

    private static final String EXTRA_KEY = "TALE_DATA";

    private TaleModel tale;
    @BindView(R.id.taleplayer_slide)
    View slideView;

    @BindView(R.id.taleplayer_image)
    ImageView imageView;

    @BindView(R.id.taleplayer_text)
    TextView textView;
    private int animationDuration;
    private LinkedList<TalePart> taleParts;
    private int partIndex = 0;
    private DefaultStorIOSQLite mDefaultStorIOSQLite;
    private AudioManager am;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tale_player);

        context=this;

        ButterKnife.bind(this);

        am = (AudioManager) getSystemService(AUDIO_SERVICE);

        String taleName = getIntent().getStringExtra(Const.EXTRA_NAME);

        setTitle(taleName);

        SQLiteOpenHelper sqLiteOpenHelper = new DbOpenHelper(this);
        mDefaultStorIOSQLite = DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(sqLiteOpenHelper)
                .addTypeMapping(TaleModel.class, new TaleModelSQLiteTypeMapping())
                .build();
        /*
        Gson gson = new Gson();
        Type type = new TypeToken<TaleModel>() {
        }.getType();
        tale = gson.fromJson(
                getIntent().getExtras().getString(EXTRA_KEY),
                type
        );
        */
        tale = loadTaleFromDb(taleName);
        taleParts = tale.getTaleParts();

        animationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        play();
    }

    private void play() {

        final TalePart part = taleParts.get(partIndex);

        if (part != null) {


            textView.setText(part.getText());
            imageView.setImageURI(Uri.parse(part.getImageLink()));

            // fade in
            slideView.animate()
                    .alpha(1f)
                    .setDuration(animationDuration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);

                            // playing audio

                            MediaPlayer taleAudioPlayer = new MediaPlayer();
                            taleAudioPlayer = MediaPlayer.create(getApplicationContext(),Uri.parse(part.getAudioLink()));
                            taleAudioPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            taleAudioPlayer.start();
                            taleAudioPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    Log.d("happy", "onCompletion");
                                    slideView.animate()
                                            .alpha(0f)
                                            .setDuration(animationDuration)
                                            .setListener(new AnimatorListenerAdapter() {
                                                @Override
                                                public void onAnimationEnd(Animator animation) {
                                                    super.onAnimationEnd(animation);
                                                    // Next slide
                                                    if (++partIndex < taleParts.size()) {
                                                        play();
                                                    } else {
                                                        // go to another activity
                                                        startActivity(new Intent(TalePlayer.this, TaleChooserActivity.class));
                                                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                                                    }
                                                }
                                            });
                                }
                            });

                            // fade out

                        }
                    });
        }

    }

    public static Intent makeIntent(Context context, TaleModel taleModel) {
        Intent intent = new Intent(context, TalePlayer.class);
        Gson gson = new Gson();
        String taleString = gson.toJson(taleModel);
        intent.putExtra(EXTRA_KEY, taleString);
        return intent;
    }


    /**
     * Метод возвращает по имени Tale из БД
     * @return объекты Tale
     */
    private TaleModel loadTaleFromDb(String taleName) {
        return mDefaultStorIOSQLite
                .get()
                .object(TaleModel.class)
                .withQuery(
                        Query.builder()
                                .table(TalesTable.TABLE_TALE)
                                .where("name = ?")
                                .whereArgs(taleName)
                                .build())
                .prepare()
                .executeAsBlocking();
    }
}
