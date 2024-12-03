package com.kmo.databaseproyecto;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PlayerActivity extends AppCompatActivity {

    private TextView songTitleTextView;
    private TextView artistNameTextView;
    private ImageView albumCoverImageView;
    private SeekBar progressBar;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        songTitleTextView = findViewById(R.id.songTitle);
        artistNameTextView = findViewById(R.id.artistName);
        albumCoverImageView = findViewById(R.id.albumCover);
        progressBar = findViewById(R.id.progressBar);

        // Obtener los datos del Intent
        String songTitle = getIntent().getStringExtra("song_title");
        String artistName = getIntent().getStringExtra("song_artist");
        String audioUrl = getIntent().getStringExtra("song_audio_url");  // URL del audio
        String imageUrl = getIntent().getStringExtra("song_image_url");  // URL de la imagen

        // Setear los datos en la interfaz
        songTitleTextView.setText(songTitle);
        artistNameTextView.setText(artistName);

        // Convertir la URL de la imagen de String a Uri y establecerla en el ImageView
        Uri imageUri = Uri.parse(imageUrl);
        albumCoverImageView.setImageURI(imageUri);

        // Convertir la URL del audio de String a Uri y configurar el MediaPlayer
        Uri audioUri = Uri.parse(audioUrl);
        mediaPlayer = MediaPlayer.create(this, audioUri);

        if (mediaPlayer != null) {
            mediaPlayer.start();
        } else {
            Toast.makeText(this, "No se pudo cargar el audio", Toast.LENGTH_SHORT).show();
        }

        // Configuración de la barra de progreso (Opcional)
        progressBar.setMax(mediaPlayer.getDuration());
        mediaPlayer.setOnPreparedListener(mp -> {
            progressBar.setMax(mediaPlayer.getDuration());
            mediaPlayer.start();
        });

        // Opcional: Actualización de la barra de progreso
        new Thread(() -> {
            while (mediaPlayer != null && mediaPlayer.isPlaying()) {
                try {
                    progressBar.setProgress(mediaPlayer.getCurrentPosition());
                    Thread.sleep(1000);  // Actualizar cada segundo
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.pause();  // Pausar la canción cuando se pause la actividad
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            mediaPlayer.release();  // Liberar recursos cuando la actividad se detenga
        }
    }
}
