package com.kmo.databaseproyecto;

import android.graphics.drawable.Drawable;

public class Song {
    private String nombre;
    private String artista;
    private Drawable image;

    public Song(String nombre, String artista, Drawable image) {
        this.nombre = nombre;
        this.artista = artista;
        this.image = image;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }
}
