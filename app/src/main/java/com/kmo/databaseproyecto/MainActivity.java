package com.kmo.databaseproyecto;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText txtNombre, txtArtista;
    private ImageView imagenCancion;
    private Button btnAgregarC, btImagen, btSeleccionarAudio, btEliminarC;
    private RecyclerView recyclerViewSongs;
    private ImageButton btnReproductor;

    private ArrayList<Song> songList = new ArrayList<>();
    private SongAdapter songAdapter;

    private Uri imageUri;
    private Uri audioUri;

    private Song selectedSongToDelete;  // Para almacenar la canción seleccionada para eliminar

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
        btEliminarC = findViewById(R.id.btEliminarC);
        recyclerViewSongs = findViewById(R.id.recyclerViewSongs);

        // Configurar RecyclerView
        songAdapter = new SongAdapter(songList, this);
        recyclerViewSongs.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSongs.setAdapter(songAdapter);

        // Funcionalidad para agregar canción
        btnAgregarC.setOnClickListener(v -> addSong());
        btImagen.setOnClickListener(v -> openImagePicker());
        btSeleccionarAudio.setOnClickListener(v -> openAudioPicker());

        // Funcionalidad para eliminar canción seleccionada
        btEliminarC.setOnClickListener(v -> deleteSelectedSong());

        // Botón para abrir el reproductor
        btnReproductor.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SongListActivity.class);
            startActivity(intent);  // Esto inicia la actividad
        });

        // Funcionalidad de selección de canción para eliminar
        songAdapter.setOnItemClickListener(position -> {
            selectedSongToDelete = songList.get(position);
            Toast.makeText(MainActivity.this, "Canción seleccionada para eliminar", Toast.LENGTH_SHORT).show();
        });

        // Obtener canciones desde Firebase
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("songs");

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                songList.clear();  // Limpiar la lista antes de agregar los nuevos datos
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Song song = snapshot.getValue(Song.class);
                    songList.add(song);
                }
                songAdapter.notifyDataSetChanged();  // Notificar al adaptador que los datos cambiaron
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar el error si ocurre
                Toast.makeText(MainActivity.this, "Error al cargar las canciones", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 100);
    }

    private void openAudioPicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 100) {  // Imagen seleccionada
                imageUri = data.getData();

                // Obtener la ruta completa de la imagen seleccionada
                String imagePath = getRealPathFromURI(imageUri);

                // Mostrar la imagen en el ImageView
                imagenCancion.setImageURI(imageUri);
            } else if (requestCode == 200) {  // Audio seleccionado
                audioUri = data.getData();
            }
        }
    }

    private String getRealPathFromURI(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String imagePath = cursor.getString(columnIndex);
            cursor.close();
            return imagePath;
        }
        return null;
    }

    private void addSong() {
        String nombre = txtNombre.getText().toString();
        String artista = txtArtista.getText().toString();

        if (nombre.isEmpty() || artista.isEmpty() || imageUri == null || audioUri == null) {
            Log.d("MainActivity", "No se han completado todos los campos");
            return; // Validación básica
        }

        String audioFilePath = saveAudioLocally(audioUri);  // Guardamos la ruta completa
        String imageFilePath = saveImageLocally(imageUri);  // Guardamos la ruta completa

        if (audioFilePath != null && imageFilePath != null) {
            Song newSong = new Song(nombre, artista, audioFilePath, imageFilePath);
            songList.add(newSong);
            songAdapter.notifyItemInserted(songList.size() - 1);

            // Limpiar campos
            txtNombre.setText("");
            txtArtista.setText("");
            imagenCancion.setImageResource(R.drawable.logo);
        }
    }

    private void deleteSelectedSong() {
        if (selectedSongToDelete != null) {
            // Eliminar la canción seleccionada de la lista
            int position = songList.indexOf(selectedSongToDelete);
            if (position != -1) {
                songList.remove(position);
                songAdapter.notifyItemRemoved(position);
                selectedSongToDelete = null;  // Limpiar la selección
                Toast.makeText(MainActivity.this, "Canción eliminada", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "Por favor, selecciona una canción para eliminar", Toast.LENGTH_SHORT).show();
        }
    }

    private String saveAudioLocally(Uri audioUri) {
        File audioFile = new File(getExternalFilesDir(null), "audio_" + System.currentTimeMillis() + ".mp3");
        try (InputStream inputStream = getContentResolver().openInputStream(audioUri);
             OutputStream outputStream = new FileOutputStream(audioFile)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }

            // Devuelve la ruta completa del archivo de audio
            return audioFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String saveImageLocally(Uri imageUri) {
        File imageFile = new File(getExternalFilesDir(null), "image_" + System.currentTimeMillis() + ".jpg");
        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            // Devuelve la ruta completa del archivo de imagen
            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
