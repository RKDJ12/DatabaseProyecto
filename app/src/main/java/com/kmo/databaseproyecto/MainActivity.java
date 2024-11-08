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
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText txtNombre, txtArtista;
    private Button btnAgregarC, btEliminarC, btImagen;
    private ImageView imagenCancion;
    private RecyclerView recyclerViewSongs;
    private SongAdapter songAdapter;
    private ArrayList<Song> songList;
    private int selectedSongIndex = -1; // Variable to keep track of the selected song for deletion

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

        // Inicializamos la lista de canciones
        songList = new ArrayList<>();
        songAdapter = new SongAdapter(songList, this);
        recyclerViewSongs.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSongs.setAdapter(songAdapter);

        // Cargar las canciones al inicio (si es necesario)
        loadSongs();

        // Acción del botón para agregar canción
        btnAgregarC.setOnClickListener(v -> {
            String nombre = txtNombre.getText().toString().trim();
            String artista = txtArtista.getText().toString().trim();

            if (nombre.isEmpty() || artista.isEmpty()) {
                Toast.makeText(MainActivity.this, "Por favor ingrese nombre y artista", Toast.LENGTH_SHORT).show();
            } else {
                Song newSong = new Song(nombre, artista, imagenCancion.getDrawable());
                songList.add(newSong);
                songAdapter.notifyDataSetChanged();
                txtNombre.setText("");
                txtArtista.setText("");
                imagenCancion.setImageResource(0); // Limpiar la imagen seleccionada
            }
        });

        // Acción del botón para eliminar canción
        btEliminarC.setOnClickListener(v -> {
            if (selectedSongIndex != -1) {
                songList.remove(selectedSongIndex);
                songAdapter.notifyItemRemoved(selectedSongIndex);
                selectedSongIndex = -1; // Reset the selected song index
            } else {
                Toast.makeText(MainActivity.this, "No hay canción seleccionada para eliminar", Toast.LENGTH_SHORT).show();
            }
        });

        // Acción del botón para seleccionar imagen
        btImagen.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        // Configuración para la selección de imagen
        songAdapter.setOnSongClickListener((position) -> {
            selectedSongIndex = position;
            Song selectedSong = songList.get(position);
            txtNombre.setText(selectedSong.getNombre());
            txtArtista.setText(selectedSong.getArtista());
            if (selectedSong.getImage() != null) {
                imagenCancion.setImageDrawable(selectedSong.getImage());
            } else {
                imagenCancion.setImageResource(0); // Reset the image if none selected
            }
        });
    }

    // Método para cargar las canciones (puede ser desde una base de datos o almacenamiento local)
    private void loadSongs() {
        // Esto es solo un ejemplo. Puedes cargar canciones desde una base de datos o archivo si lo deseas.
        songList.add(new Song("Canción 1", "Artista 1", null));
        songList.add(new Song("Canción 2", "Artista 2", null));
        songList.add(new Song("Canción 3", "Artista 3", null));

        songAdapter.notifyDataSetChanged();
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
