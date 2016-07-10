package com.makbeard.logoped;

import android.app.ActionBar;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makbeard.logoped.db.DbOpenHelper;
import com.makbeard.logoped.db.tables.TalesTable;
import com.makbeard.logoped.model.TaleModel;
import com.makbeard.logoped.model.TaleModelSQLiteTypeMapping;
import com.makbeard.logoped.model.TalePart;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TaleTextActivity extends AppCompatActivity {

    private static final String TAG = TaleTextActivity.class.getSimpleName();
    private DefaultStorIOSQLite mDefaultStorIOSQLite;
    private TaleModel mTale;

    @BindView(R.id.tale_textview)
    TextView mTaleTextView;
    @BindView(R.id.relativelayouttaletext)
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tale_text);


        ButterKnife.bind(this);

        relativeLayout.setBackgroundColor(getResources().getColor(R.color.back));

        String taleName = getIntent().getStringExtra(Const.EXTRA_NAME);

        setTitle(taleName);

        SQLiteOpenHelper sqLiteOpenHelper = new DbOpenHelper(this);
        mDefaultStorIOSQLite = DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(sqLiteOpenHelper)
                .addTypeMapping(TaleModel.class, new TaleModelSQLiteTypeMapping())
                .build();

        mTale = loadTaleFromDb(taleName);

        StringBuilder stringBuilder = new StringBuilder();
        for (TalePart talePart : mTale.getTaleParts()) {
            stringBuilder.append(talePart.getText()).append(" ");
        }

        mTaleTextView.setText(stringBuilder.toString());
    }

    @OnClick(R.id.text_read_button)
    protected void onReadClick() {
        Intent intent = new Intent(this, TaleReadPickActivity.class);
        intent.putExtra(Const.EXTRA_NAME, mTale.getName());
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
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
