package com.kmo.databaseproyecto;

public class Song {
    private String title;
    private String artist;
    private String audioFileName;
    private String imageFileName;

    public Song(String title, String artist, String audioFileName, String imageFileName) {
        this.title = title;
        this.artist = artist;
        this.audioFileName = audioFileName;
        this.imageFileName = imageFileName;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAudioFileName() {
        return audioFileName;
    }

    public String getImageFileName() {
        return imageFileName;
    }
}
