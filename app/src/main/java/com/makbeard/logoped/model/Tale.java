package com.makbeard.logoped.model;

import java.util.LinkedList;

/**
 * Класс представляет абстрактный тип данных - детский рассказ
 *
 * @author D.Makarov
 * Created 09.07.2016.
 */
public class Tale {

    //Название сказки
    private String name = "";

    //Изображение для списка выбора
    private String imageLink = "";

    //Массив частей рассказа
    private LinkedList<TalePart> taleParts = new LinkedList<>();

    public Tale(String name, String imageLink, LinkedList<TalePart> taleParts) {
        this.name = name;
        this.imageLink = imageLink;
        this.taleParts = taleParts;
    }

    public LinkedList<TalePart> getTaleParts() {
        return taleParts;
    }

    public void setTaleParts(LinkedList<TalePart> taleParts) {
        this.taleParts = taleParts;
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

}
