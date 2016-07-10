package com.makbeard.logoped;

import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.makbeard.logoped.db.DbOpenHelper;
import com.makbeard.logoped.db.tables.StatisticTable;
import com.makbeard.logoped.model.ReadStatisticModel;
import com.makbeard.logoped.model.ReadStatisticModelSQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailStatisticActivity extends AppCompatActivity {

    private static final String TAG = DetailStatisticActivity.class.getSimpleName();
    private DefaultStorIOSQLite mDefaultStorIOSQLite;

    @BindView(R.id.statistics_recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.tales_passed_count)
    TextView talesPassedCountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_statistic);

        ButterKnife.bind(this);

        recyclerView.setBackgroundColor(getResources().getColor(R.color.back));

        String childName = getIntent().getStringExtra(Const.EXTRA_CHILD_NAME);

        setTitle(childName);

        SQLiteOpenHelper sqLiteOpenHelper = new DbOpenHelper(this);

        mDefaultStorIOSQLite = DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(sqLiteOpenHelper)
                .addTypeMapping(ReadStatisticModel.class, new ReadStatisticModelSQLiteTypeMapping())
                .build();

        StatisticRecyclerViewAdapter statisticRecyclerViewAdapter =
                new StatisticRecyclerViewAdapter(new LinkedList<>());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.supportsPredictiveItemAnimations();

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(statisticRecyclerViewAdapter);


        LinkedList<ReadStatisticModel> statisticList = new LinkedList<>(loadStatisticForChildFromDb(childName));

        statisticRecyclerViewAdapter.swap(statisticList);

        talesPassedCountTextView.setText(String.valueOf(statisticList.size()));

    }

    /**
     * Метод возвращает статистику по имени ребёнка из БД
     * @return объекты ReadStatisticModel
     */
    private List<ReadStatisticModel> loadStatisticForChildFromDb(String childName) {
        return mDefaultStorIOSQLite
                .get()
                .listOfObjects(ReadStatisticModel.class)
                .withQuery(
                        Query.builder()
                                .table(StatisticTable.TABLE_STATISTIC)
                                .where("child_name = ?")
                                .whereArgs(childName)
                                .build())
                .prepare()
                .executeAsBlocking();
    }
}
