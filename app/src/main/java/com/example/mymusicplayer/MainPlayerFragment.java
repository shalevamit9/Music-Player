package com.example.mymusicplayer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
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
    private static final String SONG_INDEX_FILE_KEY = "song_index_file_key";

    private static final String TAG = "MainPlayerFragment";

    private SharedPreferences sp;

    private ItemTouchHelper itemTouchHelper;

    private SongRecyclerViewAdapter songRecyclerViewAdapter;
    public static ArrayList<Song> songs;
    public static Song lastPlayedSong;
    public static Integer lastPlayedSongIndex;

    // current song components
    private ImageView mainSongIv;
    private TextView mainSongTv;
    private ImageButton mainPrevIb;
    private ImageButton mainPlayPauseIb;
    private ImageButton mainNextIb;

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

        initializeMainSongWidgets();
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
        MyFileUtils.saveObjectToFile(getContext(), SONG_FILE_KEY, lastPlayedSong);
        MyFileUtils.saveObjectToFile(getContext(), SONG_INDEX_FILE_KEY, lastPlayedSongIndex);
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

    private void initializeMainSongWidgets() {
        mainSongIv = getActivity().findViewById(R.id.main_song_iv);
        mainSongTv = getActivity().findViewById(R.id.main_song_name_tv);
        mainPrevIb = getActivity().findViewById(R.id.main_prev_ib);
        mainNextIb = getActivity().findViewById(R.id.main_next_ib);
        mainPlayPauseIb = getActivity().findViewById(R.id.main_play_pause_ib);

        if (MusicService.mediaPlayer.isPlaying()) {
            mainPlayPauseIb.setImageResource(R.drawable.ic_baseline_pause_24);
        }

        mainPrevIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MusicService.class);
                intent.putExtra(MusicService.EXTRA_COMMAND, MusicService.COMMAND_PREV);

                Song song;
                if (lastPlayedSongIndex == 0) {
                    song = songs.get(songs.size() - 1);
                } else {
                    song = songs.get(lastPlayedSongIndex - 1);
                }

                Glide.with(getContext()).load(song.getSongImagePath()).into(mainSongIv);
                mainSongTv.setText(song.getSongName());

                getActivity().startService(intent);
            }
        });

        mainNextIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MusicService.class);
                intent.putExtra(MusicService.EXTRA_COMMAND, MusicService.COMMAND_NEXT);

                Song song;
                if (lastPlayedSongIndex == songs.size() - 1) {
                    song = songs.get(0);
                } else {
                    song = songs.get(lastPlayedSongIndex + 1);
                }

                Glide.with(getContext()).load(song.getSongImagePath()).into(mainSongIv);
                mainSongTv.setText(song.getSongName());

                getActivity().startService(intent);
            }
        });

        mainPlayPauseIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MusicService.mediaPlayer.isPlaying()) {
                    mainPlayPauseIb.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                } else {
                    mainPlayPauseIb.setImageResource(R.drawable.ic_baseline_pause_24);
                }

                playPauseSong(false);
            }
        });
    }

    private void initializeCurrentPlayingSong() {
//        Song song;
        boolean hasSongBeenClicked = sp.getBoolean(HAS_SONG_BEEN_CLICKED_KEY, false);
        if (!hasSongBeenClicked) {
//            song = songs.get(0);
            lastPlayedSong = songs.get(0);
            lastPlayedSongIndex = 0;
        } else {
            lastPlayedSong = (Song) MyFileUtils.loadObjectFromFile(getContext(), SONG_FILE_KEY);
            lastPlayedSongIndex = (Integer) MyFileUtils.loadObjectFromFile(getContext(), SONG_INDEX_FILE_KEY);
        }

        mainSongTv.setText(lastPlayedSong.getSongName());

        if (lastPlayedSong.getSongImagePath() != null) {
            Glide.with(getContext()).load(lastPlayedSong.getSongImagePath()).into(mainSongIv);
        } else {
            Glide.with(getContext()).load(R.drawable.ic_baseline_no_photography_24).into(mainSongIv);
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

    private void playPauseSong(boolean isSongClicked) {
        Intent intent = new Intent(getContext(), MusicService.class);

        if (!isSongClicked) {
            intent.putExtra(MusicService.EXTRA_COMMAND, MusicService.COMMAND_PLAY_PAUSE);
        }

        getActivity().startService(intent);
    }

    @Override
    public void onSongClicked(View view, Song song, int position) {
        MyFileUtils.saveObjectToFile(getContext(), SONG_FILE_KEY, song);
        MyFileUtils.saveObjectToFile(getContext(), SONG_INDEX_FILE_KEY, Integer.valueOf(position));

        mainPlayPauseIb.setImageResource(R.drawable.ic_baseline_pause_24);

        lastPlayedSong = song;
        lastPlayedSongIndex = position;

        boolean hasSongBeenClicked = sp.getBoolean(HAS_SONG_BEEN_CLICKED_KEY, false);
        if (!hasSongBeenClicked) {
            sp.edit().putBoolean(HAS_SONG_BEEN_CLICKED_KEY, true).apply();
        }

        playPauseSong(true);

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