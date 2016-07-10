
package com.makbeard.logoped.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.makbeard.logoped.db.tables.TalesTable;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

import java.lang.reflect.Type;
import java.util.LinkedList;

/**
 * Класс представляет абстрактный тип данных - детский рассказ
 *
 * @author D.Makarov
 *         Created 09.07.2016.
 */

@StorIOSQLiteType(table = TalesTable.TABLE_TALE)
public class TaleModel {

    //Название сказки
    @StorIOSQLiteColumn(name = TalesTable.COLUMN_TALE_NAME, key = true)
    public String name = "";

    //Изображение для списка выбора
    @StorIOSQLiteColumn(name = TalesTable.COLUMN_TALE_IMAGE_LINK)
    public String imageLink = "";

    @StorIOSQLiteColumn(name = TalesTable.COLUMN_TALE_PARTS)
    public String talePartsString;

    //Оставляем конструктор по-умолчанию для StorIO
    public TaleModel() {

    }

    public TaleModel(String name, String imageLink, LinkedList<TalePart> taleParts) {
        this.name = name;
        this.imageLink = imageLink;

        //Сериализуем список частей рассказа
        Gson gson = new Gson();
        talePartsString = gson.toJson(taleParts);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public void setTaleParts(LinkedList<TalePart> taleParts) {
        Gson gson = new Gson();
        talePartsString = gson.toJson(taleParts);
    }

    public LinkedList<TalePart> getTaleParts() {
        //Десиарелизуем список частей рассказа
        Gson gson = new Gson();
        Type type = new TypeToken<LinkedList<TalePart>>() {
        }.getType();
        return gson.fromJson(talePartsString, type);
    }


    @Override
    public String toString() {
        return "TaleModel{" +
                "name='" + name + '\'' +
                ", imageLink='" + imageLink + '\'' +
                ", talePartsString='" + talePartsString + '\'' +
                '}';
    }
}

