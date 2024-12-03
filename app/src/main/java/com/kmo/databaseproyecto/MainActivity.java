package com.kmo.databaseproyecto;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
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

    private static final int PICK_AUDIO_REQUEST = 1;
    private static final int PICK_IMAGE_REQUEST = 2;

    private RecyclerView recyclerView;
    private SongAdapter songAdapter;
    private ArrayList<Song> songList;
    private Button btnAgregarC, btnEliminarC, btnSeleccionarAudio, btnSeleccionarImagen;
    private EditText txtNombre, txtArtista;
    private ImageView imagenCancion;

    private Uri audioUri = null;
    private Uri imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicialización de elementos UI
        recyclerView = findViewById(R.id.recyclerViewSongs);
        txtNombre = findViewById(R.id.txtNombre);
        txtArtista = findViewById(R.id.txtArtista);
        imagenCancion = findViewById(R.id.imagenCancion);
        btnAgregarC = findViewById(R.id.btnAgregarC);
        btnEliminarC = findViewById(R.id.btEliminarC);
        btnSeleccionarAudio = findViewById(R.id.btSeleccionarAudio);
        btnSeleccionarImagen = findViewById(R.id.btImagen);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Lista de canciones (inicialmente vacía)
        songList = new ArrayList<>();
        songAdapter = new SongAdapter(songList, new SongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Song song) {
                // Aquí manejas el clic en una canción
                // Por ejemplo, puedes mostrar un Toast con el nombre de la canción
                Toast.makeText(MainActivity.this, "Seleccionaste la canción: " + song.getTitle(), Toast.LENGTH_SHORT).show();

                // O puedes hacer algo más, como iniciar una nueva actividad para reproducir la canción
                // Por ejemplo, si tienes una actividad para reproducir la canción, la iniciarías así:
                // Intent intent = new Intent(MainActivity.this, PlaySongActivity.class);
                // intent.putExtra("audioUri", song.getAudioUri().toString()); // Pasa el URI del audio
                // startActivity(intent);
            }
        });
        recyclerView.setAdapter(songAdapter);

        // Función para agregar una canción
        btnAgregarC.setOnClickListener(v -> {
            String nombre = txtNombre.getText().toString();
            String artista = txtArtista.getText().toString();

            if (!nombre.isEmpty() && !artista.isEmpty() && audioUri != null && imageUri != null) {
                // Crear la canción con la URI del audio y la imagen
                songList.add(new Song(null ,nombre, artista, audioUri.toString(), imageUri.toString()));
                songAdapter.notifyItemInserted(songList.size() - 1);
                txtNombre.setText("");
                txtArtista.setText("");
                Toast.makeText(MainActivity.this, "Canción agregada", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Por favor ingrese todos los datos y seleccione audio e imagen", Toast.LENGTH_SHORT).show();
            }

        });

        // Función para eliminar la canción seleccionada
        btnEliminarC.setOnClickListener(v -> {
            if (!songList.isEmpty()) {
                // Eliminar la última canción de la lista (puedes modificar esto para eliminar la canción seleccionada)
                songList.remove(songList.size() - 1);
                songAdapter.notifyItemRemoved(songList.size());
                Toast.makeText(MainActivity.this, "Canción eliminada", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "No hay canciones para eliminar", Toast.LENGTH_SHORT).show();
            }
        });

        // Función para seleccionar el archivo de audio
        btnSeleccionarAudio.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
            intent.setType("audio/*");
            startActivityForResult(intent, PICK_AUDIO_REQUEST);
        });

        // Función para seleccionar una imagen
        btnSeleccionarImagen.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });
    }

    // Método para manejar los resultados de los selectores de audio e imagen
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == PICK_AUDIO_REQUEST) {
                audioUri = data.getData();
                Toast.makeText(this, "Audio seleccionado", Toast.LENGTH_SHORT).show();
            } else if (requestCode == PICK_IMAGE_REQUEST) {
                imageUri = data.getData();
                imagenCancion.setImageURI(imageUri); // Mostrar la imagen seleccionada en la vista de la portada
                Toast.makeText(this, "Imagen seleccionada", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
