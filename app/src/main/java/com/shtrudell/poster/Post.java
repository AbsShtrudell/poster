package com.shtrudell.poster;

import android.net.Uri;

import java.io.Serializable;
import java.net.URI;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class Post implements Serializable {
    private String header;
    private String text;
    private Uri image;
    private Uri song;

    public Post(String header, String text, Uri image, Uri song) {
        this.header = header;
        this.text = text;
        this.image = image;
        this.song = song;
    }

    public Post(String header, String text, Uri image) {
        this.header = header;
        this.text = text;
        this.image = image;
    }

    public Post(String header, String text) {
        this.header = header;
        this.text = text;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public Uri getSong() {
        return song;
    }

    public void setSong(Uri song) {
        this.song = song;
    }
}
