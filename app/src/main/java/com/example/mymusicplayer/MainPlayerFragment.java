package com.example.mymusicplayer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class MainPlayerFragment extends Fragment
        implements SongRecyclerViewAdapter.SongListener,
        View.OnClickListener {

    public static final String ARG_SONGS = "arg_songs";

    private static final String IS_FIRST_TIME_KEY = "is_first_time_key";
    private static final String HAS_SONG_BEEN_CLICKED_KEY = "has_song_been_clicked_key";

    public static final String SONGS_FILE_KEY = "songs_file_key";
    private static final String SONG_FILE_KEY = "song_file_key";

    private static final String TAG = "MainPlayerFragment";

    private SharedPreferences sp;

    private ItemTouchHelper itemTouchHelper;

    private SongRecyclerViewAdapter songRecyclerViewAdapter;
    public static ArrayList<Song> songs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp = getContext().getSharedPreferences(MainActivity.SP_NAME, Context.MODE_PRIVATE);

        initializeItemTouchHelper();

        boolean isFirstTime = sp.getBoolean(IS_FIRST_TIME_KEY, true);
        if (isFirstTime) {
            initializeFirstSongs();
            sp.edit().putBoolean(IS_FIRST_TIME_KEY, false).apply();
        } else {
            songs = (ArrayList<Song>) MyFileUtils.loadObjectFromFile(getContext(), SONGS_FILE_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//         Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main_player, container, false);

        FloatingActionButton fab = rootView.findViewById(R.id.add_song_fab);
        fab.setOnClickListener(this);

        initializeCurrentPlayingSong();

        RecyclerView songsRv = rootView.findViewById(R.id.songs_rv);
        songsRv.setHasFixedSize(true);
        songsRv.setLayoutManager(new LinearLayoutManager(container.getContext()));

        songRecyclerViewAdapter = new SongRecyclerViewAdapter(getContext(), songs);
        songRecyclerViewAdapter.setSongListener(this);

        itemTouchHelper.attachToRecyclerView(songsRv);

        songsRv.setAdapter(songRecyclerViewAdapter);

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();

        MyFileUtils.saveObjectToFile(getContext(), SONGS_FILE_KEY, songs);
    }

    private void initializeFirstSongs() {
        Bitmap oneMoreCupOfCoffeeBm = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.bob_dylan_one_more_cup_of_coffee);
        Bitmap saraBm = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.bob_dylan_sara);
        Bitmap theManInMeBm = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.bob_dylan_the_man_in_me);

        String dirPath = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();

        MyFileUtils.saveBitmapToFile(new File(dirPath), "pic1.jpg", oneMoreCupOfCoffeeBm);
        MyFileUtils.saveBitmapToFile(new File(dirPath), "pic2.jpg", saraBm);
        MyFileUtils.saveBitmapToFile(new File(dirPath), "pic3.jpg", theManInMeBm);

        songs = new ArrayList<>();
        songs.add(new Song("https://www.syntax.org.il/xtra/bob.m4a", dirPath + "/pic1.jpg", "One More Cup of Coffee", "Bob Dylan"));
        songs.add(new Song("https://www.syntax.org.il/xtra/bob1.m4a", dirPath + "/pic2.jpg", "Sara", "Bob Dylan"));
        songs.add(new Song("https://www.syntax.org.il/xtra/bob2.mp3", dirPath + "/pic3.jpg", "The Man in Me", "Bob Dylan"));
        songs.add(new Song("https://www.syntax.org.il/xtra/bob.m4a", dirPath + "/pic1.jpg", "One More Cup of Coffee", "Bob Dylan"));
        songs.add(new Song("https://www.syntax.org.il/xtra/bob1.m4a", dirPath + "/pic2.jpg", "Sara", "Bob Dylan"));
        songs.add(new Song("https://www.syntax.org.il/xtra/bob2.mp3", dirPath + "/pic3.jpg", "The Man in Me", "Bob Dylan"));
        songs.add(new Song("https://www.syntax.org.il/xtra/bob.m4a", dirPath + "/pic1.jpg", "One More Cup of Coffee", "Bob Dylan"));
        songs.add(new Song("https://www.syntax.org.il/xtra/bob1.m4a", dirPath + "/pic2.jpg", "Sara", "Bob Dylan"));
        songs.add(new Song("https://www.syntax.org.il/xtra/bob2.mp3", dirPath + "/pic3.jpg", "The Man in Me", "Bob Dylan"));
        songs.add(new Song("https://www.syntax.org.il/xtra/bob.m4a", dirPath + "/pic1.jpg", "One More Cup of Coffee", "Bob Dylan"));
        songs.add(new Song("https://www.syntax.org.il/xtra/bob1.m4a", dirPath + "/pic2.jpg", "Sara", "Bob Dylan"));
        songs.add(new Song("https://www.syntax.org.il/xtra/bob2.mp3", dirPath + "/pic3.jpg", "The Man in Me", "Bob Dylan"));
        songs.add(new Song("https://www.syntax.org.il/xtra/bob.m4a", dirPath + "/pic1.jpg", "One More Cup of Coffee", "Bob Dylan"));
        songs.add(new Song("https://www.syntax.org.il/xtra/bob1.m4a", dirPath + "/pic2.jpg", "Sara", "Bob Dylan"));
        songs.add(new Song("https://www.syntax.org.il/xtra/bob2.mp3", dirPath + "/pic3.jpg", "The Man in Me", "Bob Dylan"));
        songs.add(new Song("https://www.syntax.org.il/xtra/bob.m4a", dirPath + "/pic1.jpg", "One More Cup of Coffee", "Bob Dylan"));
        songs.add(new Song("https://www.syntax.org.il/xtra/bob1.m4a", dirPath + "/pic2.jpg", "Sara", "Bob Dylan"));
        songs.add(new Song("https://www.syntax.org.il/xtra/bob2.mp3", dirPath + "/pic3.jpg", "The Man in Me", "Bob Dylan"));
        songs.add(new Song("https://www.syntax.org.il/xtra/bob.m4a", dirPath + "/pic1.jpg", "One More Cup of Coffee", "Bob Dylan"));
        songs.add(new Song("https://www.syntax.org.il/xtra/bob1.m4a", dirPath + "/pic2.jpg", "Sara", "Bob Dylan"));
        songs.add(new Song("https://www.syntax.org.il/xtra/bob2.mp3", dirPath + "/pic3.jpg", "The Man in Me", "Bob Dylan"));
        songs.add(new Song("https://www.syntax.org.il/xtra/bob.m4a", dirPath + "/pic1.jpg", "One More Cup of Coffee", "Bob Dylan"));
        songs.add(new Song("https://www.syntax.org.il/xtra/bob1.m4a", dirPath + "/pic2.jpg", "Sara", "Bob Dylan"));
        songs.add(new Song("https://www.syntax.org.il/xtra/bob2.mp3", dirPath + "/pic3.jpg", "The Man in Me", "Bob Dylan"));
        songs.add(new Song("https://www.syntax.org.il/xtra/bob.m4a", dirPath + "/pic1.jpg", "One More Cup of Coffee", "Bob Dylan"));
        songs.add(new Song("https://www.syntax.org.il/xtra/bob1.m4a", dirPath + "/pic2.jpg", "Sara", "Bob Dylan"));
        songs.add(new Song("https://www.syntax.org.il/xtra/bob2.mp3", dirPath + "/pic3.jpg", "The Man in Me", "Bob Dylan"));
        songs.add(new Song("https://www.syntax.org.il/xtra/bob.m4a", dirPath + "/pic1.jpg", "One More Cup of Coffee", "Bob Dylan"));
        songs.add(new Song("https://www.syntax.org.il/xtra/bob1.m4a", dirPath + "/pic2.jpg", "Sara", "Bob Dylan"));
        songs.add(new Song("https://www.syntax.org.il/xtra/bob2.mp3", dirPath + "/pic3.jpg", "The Man in Me", "Bob Dylan"));
    }

    private void initializeCurrentPlayingSong() {
        ImageView imageView = getActivity().findViewById(R.id.main_song_iv);
        TextView songNameTv = getActivity().findViewById(R.id.main_song_name_tv);

        Song song;
        boolean hasSongBeenClicked = sp.getBoolean(HAS_SONG_BEEN_CLICKED_KEY, false);
        if (!hasSongBeenClicked) {
            song = songs.get(0);
        } else {
            song = (Song) MyFileUtils.loadObjectFromFile(getContext(), SONG_FILE_KEY);
        }

        songNameTv.setText(song.getSongName());

        if (song.getSongImagePath() != null) {
            Glide.with(getContext()).load(song.getSongImagePath()).into(imageView);
        } else {
            Glide.with(getContext()).load(R.drawable.ic_baseline_no_photography_24).into(imageView);
        }
    }

    private void initializeItemTouchHelper() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
        ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();

                Collections.swap(songs, fromPosition, toPosition);
                recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);

                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                AlertDialog.Builder builder = new AlertDialog.Builder(viewHolder.itemView.getContext(), AlertDialog.THEME_HOLO_DARK);

                builder.setCancelable(false).setTitle("Remove song").setMessage("Do you wish to remove the song?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int position = viewHolder.getAdapterPosition();

                                songs.remove(position);
                                songRecyclerViewAdapter.notifyItemRemoved(position);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                songRecyclerViewAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                            }
                        }).show();
            }
        };

        itemTouchHelper = new ItemTouchHelper(simpleCallback);
    }

    @Override
    public void onSongClicked(View view, Song song) {
        MyFileUtils.saveObjectToFile(getContext(), SONG_FILE_KEY, song);

        boolean hasSongBeenClicked = sp.getBoolean(HAS_SONG_BEEN_CLICKED_KEY, false);
        if (!hasSongBeenClicked) {
            sp.edit().putBoolean(HAS_SONG_BEEN_CLICKED_KEY, true).apply();
        }

        NavDirections action = MainPlayerFragmentDirections.navigateToSongFragment(song);
        Navigation.findNavController(view).navigate(action);
    }

    @Override
    public void onClick(View v) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_SONGS, songs);
        Navigation.findNavController(v).navigate(R.id.navigateToAddSongFragment, args);
    }
}