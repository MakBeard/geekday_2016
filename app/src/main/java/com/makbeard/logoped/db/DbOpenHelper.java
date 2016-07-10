package com.makbeard.logoped.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.makbeard.logoped.Const;
import com.makbeard.logoped.db.tables.StatisticTable;
import com.makbeard.logoped.db.tables.TalesTable;

/**
 * SQLiteHelper для работы со StorIO
 *
 * @author D.Makarov
 * Created 09.07.2016.
 */
public class DbOpenHelper extends SQLiteOpenHelper {
    public DbOpenHelper(Context context) {
        super(context, Const.DB_NAME, null, Const.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TalesTable.getCreateTableQuery());
        db.execSQL(StatisticTable.getCreateTableQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
