package com.example.mymusicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class SongRecyclerViewAdapter extends RecyclerView.Adapter<SongRecyclerViewAdapter.SongViewHolder> {

    private final Context context;
    private final List<Song> songs;

    public SongRecyclerViewAdapter(Context context, List<Song> songs) {
        this.context = context;
        this.songs = songs;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_cell, parent, false);

        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songs.get(position);

        holder.artistNameTv.setText(song.getArtistName());
        holder.songNameTv.setText(song.getSongName());

        if (song.getSongImagePath() != null) {
            Glide.with(context).load(song.getSongImagePath()).into(holder.songIv);
        } else {
            Glide.with(context).load(R.drawable.ic_baseline_no_photography_24).into(holder.songIv);
        }
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    interface SongListener {
        /**
         * @param view The view that was clicked
         * @param song Is the song containing info on the clicked song
         * @param position the index for the song in the songs list
         */
        void onSongClicked(View view, Song song, int position);
    }

    private SongListener listener;

    public void setSongListener(SongListener listener) {
        this.listener = listener;
    }

    public class SongViewHolder extends RecyclerView.ViewHolder {

        ImageView songIv;
        TextView songNameTv;
        TextView artistNameTv;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);

            songIv = itemView.findViewById(R.id.song_iv);
            songNameTv = itemView.findViewById(R.id.song_name_tv);
            artistNameTv = itemView.findViewById(R.id.artist_name_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onSongClicked(v, songs.get(getAdapterPosition()), getAdapterPosition());
                    }
                }
            });
        }
    }
}
