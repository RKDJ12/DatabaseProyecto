package com.kmo.databaseproyecto;

public class Song {
    private String title;
    private String artist;
    private String audioFileName;  // Ruta del archivo de audio
    private String imageUri;  // URI de la imagen (como String)

    // Constructor
    public Song(String title, String artist, String audioFileName, String imageUri) {
        this.title = title;
        this.artist = artist;
        this.audioFileName = audioFileName;
        this.imageUri = imageUri;
    }

    // Métodos getter y setter
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

    public String getAudioFileName() {
        return audioFileName;
    }

    public void setAudioFileName(String audioFileName) {
        this.audioFileName = audioFileName;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
