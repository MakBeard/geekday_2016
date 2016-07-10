package com.makbeard.logoped.db.tables;

/**
 * Класс описывает таблицу для хранения классов ReadStatistic в SQL
 *
 * @author D.Makarov
 * Created 09.07.2016.
 */
public class StatisticTable {

    public static final String TABLE_STATISTIC = "statistics";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_STATISTIC_CHILD_NAME = "child_name";
    public static final String COLUMN_STATISTIC_TALE_NAME = "tale_name";
    public static final String COLUMN_STATISTIC_TIME_SPENT = "time_spent";
    public static final String COLUMN_STATISTIC_WRONG_ATTEMPS = "wrong_attemps";


    private StatisticTable() {
        throw new IllegalStateException("No instances please");
    }

    public static String getCreateTableQuery() {
        return  "CREATE TABLE " + TABLE_STATISTIC + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_STATISTIC_CHILD_NAME + " TEXT, "
                + COLUMN_STATISTIC_TALE_NAME + " TEXT, "
                + COLUMN_STATISTIC_TIME_SPENT + " INTEGER, "
                + COLUMN_STATISTIC_WRONG_ATTEMPS + " INTEGER "
                + ");";
    }
}
