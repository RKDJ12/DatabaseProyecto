package com.kmo.databaseproyecto;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private List<Song> songList;
    private Context context;
    private OnItemClickListener onItemClickListener;  // Definir el listener

    public SongAdapter(List<Song> songList, Context context) {
        this.songList = songList;
        this.context = context;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout del item
        View view = LayoutInflater.from(context).inflate(R.layout.song_item, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        Song song = songList.get(position);

        // Set song title
        holder.songTitle.setText(song.getTitle());

        // Set artist name
        holder.songArtist.setText(song.getArtist());

        // Verifica si la ruta de la imagen no es nula ni vacía
        String imagePath = song.getImagePath();
        if (imagePath != null && !imagePath.isEmpty()) {
            // Si la ruta de la imagen es válida, establece la imagen
            holder.songImage.setImageURI(Uri.parse(imagePath));
        } else {
            // Si la ruta de la imagen es inválida, carga una imagen por defecto
            holder.songImage.setImageResource(R.drawable.defaultmusic);
        }

        // Configurar el botón de Play
        holder.playButton.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position);  // Notificar al listener
            }
        });
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    // Método para configurar el listener de clics en los elementos de la lista
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    // Interfaz para manejar el clic de un item
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {

        TextView songTitle, songArtist;
        ImageView songImage;
        ImageButton playButton; // Botón de play

        public SongViewHolder(View itemView) {
            super(itemView);
            // Inicializamos los componentes de la vista
            songTitle = itemView.findViewById(R.id.songTitle);
            songArtist = itemView.findViewById(R.id.songArtist);
            songImage = itemView.findViewById(R.id.songImage);
            playButton = itemView.findViewById(R.id.playButton);
        }
    }
}
