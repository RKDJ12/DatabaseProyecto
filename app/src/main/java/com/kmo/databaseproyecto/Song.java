package com.kmo.databaseproyecto;

import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable {
    private String title;
    private String artist;
    private String audioPath;
    private String imagePath;

    // Constructor vacío (necesario para la deserialización)
    public Song() {
    }

    // Constructor con parámetros
    public Song(String title, String artist, String audioPath, String imagePath) {
        this.title = title;
        this.artist = artist;
        this.audioPath = audioPath;
        this.imagePath = imagePath;
    }

    // Métodos Getter y Setter
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    // Métodos Parcelable
    protected Song(Parcel in) {
        title = in.readString();
        artist = in.readString();
        audioPath = in.readString();
        imagePath = in.readString();
    }

    // CREATOR para crear el objeto Song a partir del Parcel
    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    // Describe los contenidos (en este caso, no hay nada especial que describir)
    @Override
    public int describeContents() {
        return 0;
    }

    // Escribe el objeto en el Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(artist);
        dest.writeString(audioPath);
        dest.writeString(imagePath);
    }
}
