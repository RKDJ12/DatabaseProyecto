package com.kmo.databaseproyecto;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FirebaseMusicHelper {

    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private Context context;

    // Constructor
    public FirebaseMusicHelper(Context context) {
        this.db = FirebaseFirestore.getInstance();
        this.storage = FirebaseStorage.getInstance();
        this.context = context;
    }

    // Método para agregar o actualizar una canción

    public void addOrUpdateSong(String id, String nombreCancion, String artista, String imageUrl, String audioUrl) {
        // Convertir las URLs de String a Uri
        Uri imageUri = Uri.parse(imageUrl);
        Uri audioUri = Uri.parse(audioUrl);

        Song song = new Song(id, nombreCancion, artista, imageUri.toString(), audioUri.toString());

        if (id == null || id.isEmpty()) {
            db.collection("songs")
                    .add(song)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(context, "Canción agregada correctamente", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Error al agregar la canción: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            db.collection("songs")
                    .document(id)
                    .set(song, SetOptions.merge())
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "Canción actualizada correctamente", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Error al actualizar la canción: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }



    // Método para subir el archivo de audio a Firebase Storage
    private void uploadAudio(Uri audioUri, final UploadAudioCallback callback) {
        StorageReference audioRef = storage.getReference().child("audio/" + audioUri.getLastPathSegment());

        audioRef.putFile(audioUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Una vez subido, obtener la URL de descarga del archivo de audio
                    audioRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String audioUrl = uri.toString();
                        callback.onSuccess(audioUrl);
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error al subir el audio: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    callback.onFailure(e);
                });
    }

    // Método para subir la imagen
    private void uploadImage(String imageUrl) {
        // Implementa aquí el código para subir la imagen al storage de Firebase si es necesario.
        // Si ya tienes la URL de la imagen, no es necesario subirla.
    }

    // Método para eliminar una canción
    public void deleteSong(String id) {
        db.collection("songs")
                .document(id)
                .delete()
                .addOnSuccessListener(aVoid -> Toast.makeText(context, "Canción eliminada correctamente", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Error al eliminar la canción: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // Interfaz para el callback del archivo de audio
    public interface UploadAudioCallback {
        void onSuccess(String audioUrl);
        void onFailure(Exception e);
    }
}
