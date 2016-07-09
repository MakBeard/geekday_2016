package com.makbeard.logoped;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tale_player);

        ButterKnife.bind(this);

        String taleName = getIntent().getStringExtra(Const.EXTRA_NAME);

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
//            imageView.setImageURI(Uri.parse(part.getImageLink()));

            // fade in
            slideView.animate()
                    .alpha(1f)
                    .setDuration(animationDuration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);

                            // playing audio

                            // fade out
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
                                            }
                                        }
                                    });
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
