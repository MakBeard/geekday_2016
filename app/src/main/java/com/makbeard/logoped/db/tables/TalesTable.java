package com.makbeard.logoped.db.tables;

/**
 * Класс описывает таблицу для хранения классов Tale в SQL
 *
 * @author D.Makarov
 * Created 09.07.2016.
 */
public class TalesTable {

    public static final String TABLE_TALE = "tales";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TALE_NAME = "name";
    public static final String COLUMN_TALE_IMAGE_LINK = "image";
    public static final String COLUMN_TALE_PARTS = "tale_parts";

    private TalesTable() {
        throw new IllegalStateException("No instances please");
    }

    public static String getCreateTableQuery() {
        return  "CREATE TABLE " + TABLE_TALE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TALE_NAME + " TEXT, "
                + COLUMN_TALE_IMAGE_LINK + " TEXT, "
                + COLUMN_TALE_PARTS + " TEXT"
                + ");";
    }
}
