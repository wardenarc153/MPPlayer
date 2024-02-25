package com.example.mpplayer.MainPages.SongBase;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class SongDataClass implements Parcelable {
    private String title; // Название песни
    private Uri uri; // URI медиафайла
    private String imagePath; // Путь к изображению
    private String audioPath; // Путь к песне

    public SongDataClass(){

    }

    public SongDataClass( String title, Uri uri, String imagePath, String audioPath) {
        this.title = title;
        this.uri = uri;
        this.imagePath = imagePath;
        this.audioPath =audioPath;
    }

    protected SongDataClass(Parcel in) {
        title = in.readString();
        audioPath = in.readString();
        uri = in.readParcelable(Uri.class.getClassLoader());
        imagePath = in.readString();
    }

    public static final Creator<SongDataClass> CREATOR = new Creator<SongDataClass>() {
        @Override
        public SongDataClass createFromParcel(Parcel in) {
            return new SongDataClass(in);
        }

        @Override
        public SongDataClass[] newArray(int size) {
            return new SongDataClass[size];
        }
    };
    public String getTitle() {
        return title;
    }

    public Uri getUri() {
        return uri;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getAudioPath() {
        return audioPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeParcelable(uri, i);
        parcel.writeString(imagePath);
        parcel.writeString(audioPath);
    }
}
