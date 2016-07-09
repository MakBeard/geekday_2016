package com.makbeard.logoped.model;

/**
 * Модель части рассказа (текст, картинка, аудио)
 *
 * @author D.Makarov
 * Created 09.07.2016.
 */
public class TalePart {
    private String imageLink;
    private String text;
    private String audioLink;

    public TalePart(String imageLink, String text, String audioLink) {
        this.imageLink = imageLink;
        this.text = text;
        this.audioLink = audioLink;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAudioLink() {
        return audioLink;
    }

    public void setAudioLink(String audioLink) {
        this.audioLink = audioLink;
    }
}
