package com.makbeard.logoped.model;

import com.makbeard.logoped.db.tables.StatisticTable;
import com.makbeard.logoped.db.tables.TalesTable;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

/**
 * Класс-модель статистики чтения
 * Created by Hp on 10.07.2016.
 */
@StorIOSQLiteType(table = StatisticTable.TABLE_STATISTIC)
public class ReadStatisticModel {

    @StorIOSQLiteColumn(name = StatisticTable.COLUMN_STATISTIC_CHILD_NAME, key = true)
    public String childName;

    @StorIOSQLiteColumn(name = StatisticTable.COLUMN_STATISTIC_TALE_NAME)
    public String taleName;

    @StorIOSQLiteColumn(name = StatisticTable.COLUMN_STATISTIC_TIME_SPENT)
    public long timeSpent;

    @StorIOSQLiteColumn(name = StatisticTable.COLUMN_STATISTIC_WRONG_ATTEMPS)
    public int wrongAttemps;

    //Оставляем конструктор по-умолчанию для StorIO
    public ReadStatisticModel() {

    }

    public ReadStatisticModel(String childName, String taleName, long timeSpent, int wrongAttemps) {
        this.childName = childName;
        this.taleName = taleName;
        this.timeSpent = timeSpent;
        this.wrongAttemps = wrongAttemps;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getTaleName() {
        return taleName;
    }

    public void setTaleName(String taleName) {
        this.taleName = taleName;
    }

    public long getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(long timeSpent) {
        this.timeSpent = timeSpent;
    }

    public int getWrongAttemps() {
        return wrongAttemps;
    }

    public void setWrongAttemps(int wrongAttemps) {
        this.wrongAttemps = wrongAttemps;
    }

    @Override
    public String toString() {
        return "ReadStatisticModel{" +
                "childName='" + childName + '\'' +
                ", taleName='" + taleName + '\'' +
                ", timeSpent=" + timeSpent +
                ", wrongAttemps=" + wrongAttemps +
                '}';
    }
}
