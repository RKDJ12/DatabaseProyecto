package com.kmo.databaseproyecto;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.io.ByteArrayOutputStream;

public class FirebaseMusicHelper {

    private FirebaseFirestore db;
    private Context context;

    // Constructor
    public FirebaseMusicHelper(Context context) {
        this.db = FirebaseFirestore.getInstance();
        this.context = context;
    }

    // Método para agregar o actualizar una canción
    public void addOrUpdateSong(String id, String nombreCancion, String artista, String imageUrl) {
        // Creamos un objeto Song
        Song song = new Song(nombreCancion, artista, imageUrl);

        // Si el id es nulo o vacío, es una nueva canción, así que creamos un nuevo documento
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
            // Si el id existe, actualizamos la canción
            db.collection("songs")
                    .document(id)
                    .set(song, SetOptions.merge()) // Usamos merge para actualizar solo los campos necesarios
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "Canción actualizada correctamente", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Error al actualizar la canción: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    // Método para cargar imágenes de Firebase y convertirlas a base64 (si es necesario para Firestore)
    private void loadImageToBase64(String imageUrl, final ImageUploadCallback callback) {
        Picasso.get().load(imageUrl).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);
                callback.onSuccess(base64Image);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                callback.onError(e);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                // Aquí puedes manejar un estado antes de cargar la imagen si es necesario
            }
        });
    }

    // Interfaz de callback para manejar la respuesta de la imagen cargada
    public interface ImageUploadCallback {
        void onSuccess(String base64Image);
        void onError(Exception e);
    }
}
