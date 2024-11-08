package com.kmo.databaseproyecto;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
    private OnSongClickListener onSongClickListener;

    public SongAdapter(ArrayList<Song> songList, Context context) {
        this.songList = songList;
        this.context = context;
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.song_item, parent, false);
        return new SongViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        Song song = songList.get(position);
        holder.txtNombre.setText(song.getNombre());
        holder.txtArtista.setText(song.getArtista());
        if (song.getImage() != null) {
            holder.imagen.setImageDrawable(song.getImage());
        }
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public void setOnSongClickListener(OnSongClickListener listener) {
        this.onSongClickListener = listener;
    }

    public interface OnSongClickListener {
        void onSongClick(int position);
    }

    public class SongViewHolder extends RecyclerView.ViewHolder {

        public TextView txtNombre, txtArtista;
        public ImageView imagen;

        public SongViewHolder(View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombreSong);
            txtArtista = itemView.findViewById(R.id.txtArtistaSong);
            imagen = itemView.findViewById(R.id.imagenSong);

            itemView.setOnClickListener(v -> {
                if (onSongClickListener != null) {
                    onSongClickListener.onSongClick(getAdapterPosition());
                }
            });
        }
    }
}
