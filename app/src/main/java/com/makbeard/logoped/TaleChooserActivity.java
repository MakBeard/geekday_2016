package com.makbeard.logoped;


import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.RelativeLayout;

import com.makbeard.logoped.db.DbOpenHelper;
import com.makbeard.logoped.db.tables.TalesTable;
import com.makbeard.logoped.model.TaleModel;
import com.makbeard.logoped.model.TaleModelSQLiteTypeMapping;
import com.makbeard.logoped.model.TalePart;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Activity со списком рассказов
 */
public class TaleChooserActivity extends AppCompatActivity {

    private static final String TAG = TaleChooserActivity.class.getSimpleName();
    @BindView(R.id.tales_recyclerview)
    RecyclerView mTaleRecyclerView;
    private DefaultStorIOSQLite mDefaultStorIOSQLite;

    @BindView(R.id.relativelayoutchooser)
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tale_chooser);
        ButterKnife.bind(this);

        relativeLayout.setBackgroundColor(getResources().getColor(R.color.back));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.getBackground().setAlpha(0);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        gridLayoutManager.supportsPredictiveItemAnimations();

        mTaleRecyclerView.setLayoutManager(gridLayoutManager);

        TalesRecyclerViewAdapter adapter =
                new TalesRecyclerViewAdapter(this, new ArrayList<TaleModel>());

        mTaleRecyclerView.setAdapter(adapter);

        SQLiteOpenHelper sqLiteOpenHelper = new DbOpenHelper(this);
        mDefaultStorIOSQLite = DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(sqLiteOpenHelper)
                .addTypeMapping(TaleModel.class, new TaleModelSQLiteTypeMapping())
                .build();

        //-----------------------Тестирование-------------------------------------------------------
/*        for (TaleModel tale : getMockTales()) {
            putTaleToDb(tale);
        }*/
        //------------------------------------------------------------------------------------------
        adapter.swap(loadTalesFromDb());
    }

    /**
     * Метод сохраняет объект Tale в БД
     * @param tale сохраняемый объект
     */
    private void putTaleToDb(TaleModel tale) {
        mDefaultStorIOSQLite
                .put()
                .object(tale)
                .prepare()
                .executeAsBlocking();

    }

    /**
     * Метод возвращает все объекты Tale из БД
     * @return объекты Tale
     */
    private List<TaleModel> loadTalesFromDb() {
         return mDefaultStorIOSQLite
                .get()
                .listOfObjects(TaleModel.class)
                .withQuery(
                        Query.builder()
                        .table(TalesTable.TABLE_TALE)
                        .build())
                .prepare()
                .executeAsBlocking();
    }

    /**
     * Метод для тестирования сохранения
     * @return список объектов TaleModel
     */
    private List<TaleModel> getMockTales() {
        List<TaleModel> mockList = new ArrayList<>();
        for (int i = 0; i <= 5; i++) {
            LinkedList<TalePart> mockLinkedList = new LinkedList<>();
            for (int j = 0; j <= 5; j++) {
                mockLinkedList.add(new TalePart(
                        "Ссылка на картинку" + i + j,
                        "Весёлый рассказ " + i + j,
                        "Ссылка на аудио " + i + j)
                );
            }
            mockList.add(new TaleModel("Name " + i, "", mockLinkedList));
        }
        return mockList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tale_chooser_activity,menu);
        getSupportActionBar().setHomeButtonEnabled(true);
        return true;
    }
}

