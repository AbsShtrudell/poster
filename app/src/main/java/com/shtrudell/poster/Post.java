package com.shtrudell.poster;

import java.io.Serializable;
import java.net.URI;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class Post implements Serializable {
    private String header;
    private String text;
    private URI image;
    private URI song;

    public Post(String header, String text, URI image, URI song) {
        this.header = header;
        this.text = text;
        this.image = image;
        this.song = song;
    }

    public Post(String header, String text, URI image) {
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

    public URI getImage() {
        return image;
    }

    public void setImage(URI image) {
        this.image = image;
    }

    public URI getSong() {
        return song;
    }

    public void setSong(URI song) {
        this.song = song;
    }
}
