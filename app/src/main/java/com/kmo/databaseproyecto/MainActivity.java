package com.kmo.databaseproyecto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText txtNombre, txtArtista;
    private Button btnAgregarC, btEliminarC, btImagen;
    private ImageView imagenCancion;
    private RecyclerView recyclerViewSongs;
    private SongAdapter songAdapter;
    private ArrayList<Song> songList;
    private int selectedSongIndex = -1; // Variable para seleccionar la canción a eliminar
    private FirebaseFirestore db;
    private String selectedSongId = ""; // ID de la canción seleccionada para editar o eliminar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicialización de los componentes
        txtNombre = findViewById(R.id.txtNombre);
        txtArtista = findViewById(R.id.txtArtista);
        btnAgregarC = findViewById(R.id.btnAgregarC);
        btEliminarC = findViewById(R.id.btEliminarC);
        btImagen = findViewById(R.id.btImagen);
        imagenCancion = findViewById(R.id.imagenCancion);
        recyclerViewSongs = findViewById(R.id.recyclerViewSongs);

        // Inicializamos Firestore
        db = FirebaseFirestore.getInstance();

        // Inicializamos la lista de canciones y el adaptador
        songList = new ArrayList<>();
        songAdapter = new SongAdapter(songList, this);
        recyclerViewSongs.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSongs.setAdapter(songAdapter);

        // Cargar las canciones desde Firestore
        loadSongs();

        // Acción del botón para agregar una canción
        // Acción del botón para agregar una canción
        btnAgregarC.setOnClickListener(v -> {
            String nombre = txtNombre.getText().toString().trim();
            String artista = txtArtista.getText().toString().trim();

            if (nombre.isEmpty() || artista.isEmpty()) {
                Toast.makeText(MainActivity.this, "Por favor ingrese nombre y artista", Toast.LENGTH_SHORT).show();
            } else {
                String imageUrl = "https://example.com/imagen.jpg";  // Aquí deberías poner la URL de la imagen seleccionada
                if (selectedSongId.isEmpty()) {  // Si no hay canción seleccionada, agregar una nueva
                    FirebaseMusicHelper helper = new FirebaseMusicHelper(MainActivity.this);
                    helper.addOrUpdateSong(null, nombre, artista, imageUrl); // Pasamos null para generar un nuevo ID automáticamente
                } else {  // Si hay una canción seleccionada, la actualizamos
                    FirebaseMusicHelper helper = new FirebaseMusicHelper(MainActivity.this);
                    helper.addOrUpdateSong(selectedSongId, nombre, artista, imageUrl); // Usamos el ID de la canción seleccionada
                }
            }
        });



        // Acción del botón para eliminar una canción
        btEliminarC.setOnClickListener(v -> {
            if (!selectedSongId.isEmpty()) {
                deleteSong(selectedSongId);
            } else {
                Toast.makeText(MainActivity.this, "No hay canción seleccionada para eliminar", Toast.LENGTH_SHORT).show();
            }
        });

        // Acción del botón para seleccionar una imagen
        btImagen.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        // Configuración para la selección de una canción
        songAdapter.setOnSongClickListener(position -> {
            selectedSongIndex = position;
            Song selectedSong = songList.get(position);
            selectedSongId = selectedSong.getId();
            txtNombre.setText(selectedSong.getNombre());
            txtArtista.setText(selectedSong.getArtista());
            if (selectedSong.getImageUrl() != null) {
                Picasso.get().load(selectedSong.getImageUrl()).into(imagenCancion);
            }
        });
    }

    // Método para cargar las canciones desde Firestore
    private void loadSongs() {
        db.collection("songs")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        songList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Song song = document.toObject(Song.class);
                            song.setId(document.getId()); // Asignamos el ID del documento
                            songList.add(song);
                        }
                        songAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(MainActivity.this, "Error al cargar las canciones", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Método para agregar una nueva canción
    private void addNewSong(String nombre, String artista, String imageUrl) {
        Song song = new Song(nombre, artista, imageUrl);
        db.collection("songs")
                .add(song)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(MainActivity.this, "Canción agregada correctamente", Toast.LENGTH_SHORT).show();
                    loadSongs(); // Volver a cargar las canciones
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MainActivity.this, "Error al agregar la canción", Toast.LENGTH_SHORT).show();
                });
    }

    // Método para actualizar una canción existente
    private void updateSong(String songId, String nombre, String artista, String imageUrl) {
        Song updatedSong = new Song(nombre, artista, imageUrl);
        db.collection("songs")
                .document(songId)
                .set(updatedSong)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(MainActivity.this, "Canción actualizada correctamente", Toast.LENGTH_SHORT).show();
                    loadSongs(); // Volver a cargar las canciones
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MainActivity.this, "Error al actualizar la canción", Toast.LENGTH_SHORT).show();
                });
    }

    // Método para eliminar una canción
    private void deleteSong(String songId) {
        db.collection("songs")
                .document(songId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(MainActivity.this, "Canción eliminada correctamente", Toast.LENGTH_SHORT).show();
                    loadSongs(); // Volver a cargar las canciones
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MainActivity.this, "Error al eliminar la canción", Toast.LENGTH_SHORT).show();
                });
    }

    // Método para manejar la imagen seleccionada desde la galería
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            Picasso.get().load(imageUri).into(imagenCancion);  // Usamos Picasso para cargar la imagen
        }
    }
}
