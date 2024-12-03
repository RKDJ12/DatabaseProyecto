package com.kmo.databaseproyecto;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private List<Song> songList;
    private OnItemClickListener onItemClickListener;

    // Interfaz para el manejo de clics
    public interface OnItemClickListener {
        void onItemClick(Song song);
    }

    // Constructor
    public SongAdapter(List<Song> songList, OnItemClickListener onItemClickListener) {
        this.songList = songList;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflar el layout del ítem
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_item, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        // Obtener la canción actual
        Song song = songList.get(position);

        // Asignar los datos al ViewHolder
        holder.titleTextView.setText(song.getTitle());
        holder.artistTextView.setText(song.getArtist());
        holder.albumCoverImageView.setImageResource(song.getAlbumCoverResId());

        // Establecer el comportamiento del clic en la canción
        holder.itemView.setOnClickListener(v -> {
            // Llamar al listener cuando se hace clic en un ítem
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(song);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    // ViewHolder que contiene los elementos de cada canción
    public static class SongViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView artistTextView;
        ImageView albumCoverImageView;

        public SongViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.songTitle);
            artistTextView = itemView.findViewById(R.id.txtArtist);
            albumCoverImageView = itemView.findViewById(R.id.imgSongCover);
        }
    }
}
