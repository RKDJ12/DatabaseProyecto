package com.kmo.databaseproyecto;

public class Song {
    private String id;
    private String nombre;
    private String artista;
    private String imageUrl;  // Usaremos la URL de la imagen en lugar del Drawable

    public Song(String nombre, String artista, String imageUrl) {
        this.nombre = nombre;
        this.artista = artista;
        this.imageUrl = imageUrl;
    }

    // Getters y setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
