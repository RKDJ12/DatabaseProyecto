package com.kmo.databaseproyecto;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SongListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SongAdapter songAdapter;
    private ArrayList<Song> songList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list_view);

        // Inicializar el RecyclerView
        recyclerView = findViewById(R.id.recyclerViewSongs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar el adaptador y vincularlo
        songAdapter = new SongAdapter(songList, this);
        recyclerView.setAdapter(songAdapter);

        // Cargar canciones desde Firebase
        loadSongsFromFirebase();

        // Configurar el listener para los clics en los botones Play
        songAdapter.setOnItemClickListener(position -> {
            // Obtener la canción seleccionada
            Song selectedSong = songList.get(position);

            // Crear un Intent para pasar la canción al PlayerActivity
            Intent intent = new Intent(SongListActivity.this, PlayerActivity.class);
            intent.putExtra("song", selectedSong);  // Pasar la canción seleccionada
            startActivity(intent);  // Iniciar el reproductor
        });
    }

    private void loadSongsFromFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("songs");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                songList.clear(); // Limpia la lista antes de agregar los datos
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Song song = snapshot.getValue(Song.class);
                    if (song != null) {
                        songList.add(song);
                    }
                }
                songAdapter.notifyDataSetChanged(); // Notifica cambios al adaptador
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("SongListActivity", "Error al cargar canciones: " + databaseError.getMessage());
            }
        });
    }
}
