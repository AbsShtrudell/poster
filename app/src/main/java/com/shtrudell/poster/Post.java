package com.shtrudell.poster;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import org.json.simple.JSONObject;

import java.io.Serializable;
import java.net.URI;

public class Post implements Serializable, Parcelable{
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

    protected Post(Parcel in) {
        header = in.readString();
        text = in.readString();
        image = in.readParcelable(Uri.class.getClassLoader());
        song = in.readParcelable(Uri.class.getClassLoader());
    }

    public Post() {
        header = null;
        text = null;
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(header);
        parcel.writeString(text);
        parcel.writeParcelable(image, i);
        parcel.writeParcelable(song, i);
    }

    public JSONObject parseToJSON() {
        JSONObject post = new JSONObject();
        post.put("header", header == null? "" : header.toString());
        post.put("text", text == null? "" : text.toString());
        post.put("image_source", image == null? "" : image.toString());
        post.put("song_source", song == null? "" : song.toString());

        return post;
    }

    public static Post parseFromJSON(JSONObject object) {
        Post post = new Post();

        String header = (String) object.get("header");
        if(!header.isEmpty())
            post.setHeader(header);

        String text = (String)object.get("text");
        if(!text.isEmpty())
            post.setText(text);

        String imageSource = (String)object.get("image_source");
        if(!imageSource.isEmpty())
            post.setImage(Uri.parse(imageSource));

        String songSource = (String)object.get("song_source");
        if(!songSource.isEmpty())
            post.setSong(Uri.parse(songSource));

        return post;
    }
}
