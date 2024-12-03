package com.kmo.databaseproyecto;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private ArrayList<Song> songList;
    private Context context;

    // Constructor
    public SongAdapter(ArrayList<Song> songList) {
        this.songList = songList;
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflar la vista para cada item del RecyclerView
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_item, parent, false);
        context = parent.getContext();
        return new SongViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        Song song = songList.get(position);

        // Establecer los valores en el ViewHolder
        holder.titleTextView.setText(song.getTitle());
        holder.artistTextView.setText(song.getArtist());

        // Si tienes la imagen de la canción (URI), la puedes cargar usando un ImageView.
        if (song.getImageUri() != null) {
            holder.songImageView.setImageURI(Uri.parse(song.getImageUri()));
        }
    }


    @Override
    public int getItemCount() {
        return songList.size();
    }

    // ViewHolder para la canción
    public static class SongViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView artistTextView;
        public ImageView songImageView;

        public SongViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.songTitle);
            artistTextView = itemView.findViewById(R.id.songArtist);
            songImageView = itemView.findViewById(R.id.songImage);
        }
    }
}
