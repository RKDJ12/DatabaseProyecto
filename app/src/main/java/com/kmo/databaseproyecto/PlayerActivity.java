package com.kmo.databaseproyecto;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class PlayerActivity extends AppCompatActivity {

    private TextView songTitle, artistName, songTime;
    private SeekBar progressBar;
    private ImageButton btnPlayPause;
    private ImageView albumCover;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        // Obtener la canción pasada desde la actividad anterior
        Song currentSong = getIntent().getParcelableExtra("song");

        songTitle = findViewById(R.id.songTitle);
        artistName = findViewById(R.id.artistName);
        songTime = findViewById(R.id.songTime);
        progressBar = findViewById(R.id.progressBar);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        albumCover = findViewById(R.id.albumCover);

        // Establecer los valores de la canción
        if (currentSong != null) {
            songTitle.setText(currentSong.getTitle());
            artistName.setText(currentSong.getArtist());
            // Establecer imagen de la carátula
            albumCover.setImageURI(Uri.parse(currentSong.getImagePath()));
            // Reproducir la canción
            playSong(currentSong.getAudioPath());
        }

        // Botón Play/Pause
        btnPlayPause.setOnClickListener(view -> {
            if (isPlaying) {
                pauseSong();
            } else {
                playSong(currentSong.getAudioPath());
            }
        });

        // Actualizar SeekBar y temporizador cada segundo
        progressBar.setMax(100);
        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress * mediaPlayer.getDuration() / 100);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void playSong(String audioPath) {
        if (mediaPlayer == null) {
            // Si no hay un MediaPlayer, crear uno nuevo
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(audioPath);
                mediaPlayer.prepare();
                mediaPlayer.start();

                // Iniciar la actualización del temporizador y la barra de progreso
                updateProgressBar();

                isPlaying = true;
                btnPlayPause.setImageResource(R.drawable.pause); // Cambiar icono a "Pause"
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (!isPlaying) {
            // Si el MediaPlayer ya existe, solo iniciar la canción
            mediaPlayer.start();
            isPlaying = true;
            btnPlayPause.setImageResource(R.drawable.pause); // Cambiar icono a "Pause"
        }
    }

    private void pauseSong() {
        if (mediaPlayer != null && isPlaying) {
            mediaPlayer.pause();
            isPlaying = false;
            btnPlayPause.setImageResource(R.drawable.play); // Cambiar icono a "Play"
        }
    }

    private void updateProgressBar() {
        if (mediaPlayer != null) {
            int currentPosition = mediaPlayer.getCurrentPosition();
            int duration = mediaPlayer.getDuration();

            // Actualizar la barra de progreso y el tiempo de la canción
            progressBar.setProgress(currentPosition * 100 / duration);

            // Actualizar el tiempo de la canción
            songTime.setText(formatTime(currentPosition));

            // Continuar actualizando cada segundo
            handler.postDelayed(this::updateProgressBar, 1000);
        }
    }

    private String formatTime(int milliseconds) {
        int seconds = (milliseconds / 1000) % 60;
        int minutes = (milliseconds / (1000 * 60)) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}
