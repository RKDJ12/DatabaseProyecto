package com.kmo.databaseproyecto;

public class Song {
    private String id;
    private String title;
    private String artist;
    private String imageUrl;  // Ahora es String
    private String audioUrl;  // Ahora es String

    // Constructor modificado
    public Song(String id, String title, String artist, String imageUrl, String audioUrl) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.imageUrl = imageUrl;
        this.audioUrl = audioUrl;
    }

    // Métodos getter y setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public int getAlbumCoverResId() {
        return 0;
    }
}
