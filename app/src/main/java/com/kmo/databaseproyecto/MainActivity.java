package com.kmo.databaseproyecto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText txtNombre, txtArtista;
    private Button btnAgregarC, btEliminarC, btImagen;
    private ImageView imagenCancion;
    private RecyclerView recyclerViewSongs;
    private SongAdapter songAdapter;
    private ArrayList<Song> songList;
    private FirebaseFirestore db;
    private CollectionReference songsCollection;

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
        songsCollection = db.collection("songs");

        // Inicializamos la lista de canciones y el adaptador
        songList = new ArrayList<>();
        songAdapter = new SongAdapter(songList, this);
        recyclerViewSongs.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSongs.setAdapter(songAdapter);

        // Cargar canciones desde Firestore
        loadSongs();

        // Acción del botón para agregar canción
        btnAgregarC.setOnClickListener(v -> {
            String nombre = txtNombre.getText().toString().trim();
            String artista = txtArtista.getText().toString().trim();

            if (nombre.isEmpty() || artista.isEmpty()) {
                Toast.makeText(MainActivity.this, "Por favor ingrese nombre y artista", Toast.LENGTH_SHORT).show();
            } else {
                String imageUrl = ""; // Aquí deberías poner la URL de la imagen si la tienes

                // Agregar la canción a Firestore
                Song newSong = new Song(nombre, artista, imageUrl);
                songsCollection.add(newSong)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(MainActivity.this, "Canción agregada", Toast.LENGTH_SHORT).show();
                            loadSongs(); // Recargar las canciones
                        });
            }
        });

        // Acción del botón para eliminar canción
        btEliminarC.setOnClickListener(v -> {
            // Implementar eliminación de canción (si tienes un identificador único para cada canción)
        });

        // Acción del botón para seleccionar imagen
        btImagen.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });
    }

    // Método para cargar las canciones desde Firestore
    private void loadSongs() {
        songsCollection.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    songList.clear();
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                            Song song = snapshot.toObject(Song.class);
                            songList.add(song);
                        }
                        songAdapter.notifyDataSetChanged();
                    }
                });
    }

    // Método para manejar la imagen seleccionada desde la galería
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imagenCancion.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al seleccionar imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
