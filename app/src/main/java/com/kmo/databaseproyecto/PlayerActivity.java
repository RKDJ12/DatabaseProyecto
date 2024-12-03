package com.kmo.databaseproyecto;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PlayerActivity extends AppCompatActivity {

    private ImageButton btnPlayPause;
    private MediaPlayer mediaPlayer;
    private Uri songUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        // Inicializar los elementos
        btnPlayPause = findViewById(R.id.btnPlayPause);
        songUri = getIntent().getData();  // Suponiendo que la URI de la canción es pasada

        if (songUri != null) {
            mediaPlayer = MediaPlayer.create(this, songUri);
        } else {
            Toast.makeText(this, "No se seleccionó canción", Toast.LENGTH_SHORT).show();
        }

        btnPlayPause.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                btnPlayPause.setImageResource(R.drawable.play);
            } else {
                mediaPlayer.start();
                btnPlayPause.setImageResource(R.drawable.pause);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}
