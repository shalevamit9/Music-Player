package com.example.mymusicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    public static final String SP_NAME = "sp_name";

    private BroadcastReceiver receiver;

    private ImageView imageView;
    private TextView textView;
    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.main_song_iv);
        textView = findViewById(R.id.main_song_name_tv);
        imageButton = findViewById(R.id.main_play_pause_ib);

        IntentFilter filter = new IntentFilter(MainPlayerFragment.SONG_BROADCAST_SERVICE_FILTER);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Song song = (Song) intent.getSerializableExtra(MainPlayerFragment.EXTRA_SONG);

                Glide.with(MainActivity.this).load(song.getSongImagePath()).into(imageView);
                textView.setText(song.getSongName());
                imageButton.setImageResource(R.drawable.ic_baseline_pause_24);
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }
}