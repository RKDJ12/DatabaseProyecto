package com.kmo.databaseproyecto;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {

    private DatabaseReference databaseReference;

    public FirebaseHelper() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("songs");
    }

    public void addSongToFirebase(Song song) {
        databaseReference.push().setValue(song)
                .addOnSuccessListener(aVoid -> Log.d("FirebaseHelper", "Canción guardada exitosamente"))
                .addOnFailureListener(e -> Log.e("FirebaseHelper", "Error al guardar canción", e));
    }
}
