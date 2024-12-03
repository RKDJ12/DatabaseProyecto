package com.kmo.databaseproyecto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.activity.result.ActivityResultCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText txtNombre, txtArtista;
    private ImageView imagenCancion;
    private Button btnAgregarC, btImagen, btSeleccionarAudio;
    private RecyclerView recyclerViewSongs;
    private ImageButton btnReproductor;

    private ArrayList<Song> songList = new ArrayList<>();
    private SongAdapter songAdapter;

    private Uri imageUri;
    private Uri audioUri;

    // Declara los lanzadores de actividad
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private ActivityResultLauncher<Intent> audioPickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar vistas
        btnReproductor = findViewById(R.id.btnGoToPlayer);
        txtNombre = findViewById(R.id.txtNombre);
        txtArtista = findViewById(R.id.txtArtista);
        imagenCancion = findViewById(R.id.imagenCancion);
        btnAgregarC = findViewById(R.id.btnAgregarC);
        btImagen = findViewById(R.id.btImagen);
        btSeleccionarAudio = findViewById(R.id.btSeleccionarAudio);
        recyclerViewSongs = findViewById(R.id.recyclerViewSongs);


        // Configurar RecyclerView
        songAdapter = new SongAdapter(songList);
        recyclerViewSongs.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSongs.setAdapter(songAdapter);

        // Registrar lanzadores para los pickers de imagen y audio
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData();
                        imagenCancion.setImageURI(imageUri);
                    }
                });

        audioPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        audioUri = result.getData().getData();
                    }
                });

        btnReproductor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,PlayerActivity.class);
                startActivity(intent);
            }
        });
        // Funcionalidad para seleccionar imagen
        btImagen.setOnClickListener(v -> openImagePicker());

        // Funcionalidad para seleccionar audio
        btSeleccionarAudio.setOnClickListener(v -> openAudioPicker());

        // Funcionalidad para agregar canción
        btnAgregarC.setOnClickListener(v -> addSong());
    }
    // Método para seleccionar imagen
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    // Método para seleccionar audio
    private void openAudioPicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        audioPickerLauncher.launch(intent);
    }

    // Método para agregar una canción
    private void addSong() {
        String nombre = txtNombre.getText().toString();
        String artista = txtArtista.getText().toString();

        if (nombre.isEmpty() || artista.isEmpty() || imageUri == null || audioUri == null) {
            Log.d("MainActivity", "No se han completado todos los campos");
            return; // Validación básica
        }

        // Guardar la canción
        String audioFileName = saveAudioLocally(audioUri);
        String imageFileName = saveImageLocally(imageUri);

        Song newSong = new Song(nombre, artista, audioFileName, imageFileName);
        songList.add(newSong);
        songAdapter.notifyItemInserted(songList.size() - 1);

        // Limpiar campos
        txtNombre.setText("");
        txtArtista.setText("");
        imagenCancion.setImageResource(R.drawable.logo);
    }

    // Guardar el audio localmente y devolver el nombre del archivo
    private String saveAudioLocally(Uri audioUri) {
        File audioFile = new File(getExternalFilesDir(null), "audio_" + System.currentTimeMillis() + ".mp3");
        try (FileOutputStream fos = new FileOutputStream(audioFile)) {
            fos.write(audioUri.toString().getBytes());  // Simulando la copia del archivo
            return audioFile.getName();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Guardar la imagen localmente y devolver el nombre del archivo
    private String saveImageLocally(Uri imageUri) {
        File imageFile = new File(getExternalFilesDir(null), "image_" + System.currentTimeMillis() + ".jpg");
        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            return imageFile.getName();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}