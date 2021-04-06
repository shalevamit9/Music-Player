package com.example.mymusicplayer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class SongFragment extends Fragment {

    private static final String ARG_SONG = "song";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_song, container, false);

        Song song = (Song) getArguments().getSerializable(ARG_SONG);

        ImageView imageView = rootView.findViewById(R.id.song_iv);
        Glide.with(container.getContext()).load(song.getSongImagePath()).into(imageView);

        TextView songNameTv = rootView.findViewById(R.id.song_name_tv);
        songNameTv.setText(song.getSongName());

        TextView artistNameTv = rootView.findViewById(R.id.artist_name_tv);
        artistNameTv.setText(song.getArtistName());

        TextView mainSongNameTv = getActivity().findViewById(R.id.main_song_name_tv);
        ImageView mainSongIv = getActivity().findViewById(R.id.main_song_iv);

        mainSongNameTv.setText(song.getSongName());

        if (song.getSongImagePath() != null) {
            Glide.with(getContext()).load(song.getSongImagePath()).into(mainSongIv);
        } else {
            Glide.with(getContext()).load(R.drawable.ic_baseline_no_photography_24).into(mainSongIv);
        }

        return rootView;
    }
}