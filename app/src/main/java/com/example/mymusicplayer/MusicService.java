package com.example.mymusicplayer;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;

import java.io.IOException;

public class MusicService extends Service
        implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener {

    public static final String EXTRA_COMMAND = "command";
    public static final String COMMAND_PLAY_PAUSE = "play_pause";
    public static final String COMMAND_PREV = "prev";
    public static final String COMMAND_NEXT = "next";
    private static final String COMMAND_CLOSE = "close";

    private static final int NOTIFICATION_ID = 1;

    private NotificationManager manager;
    private RemoteViews remoteViews;

    public static final MediaPlayer mediaPlayer = new MediaPlayer();
    private NotificationCompat.Builder builder;

    private BroadcastReceiver receiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.reset();

        String channelId = "channel_id";
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "My Music Player", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }

        builder = new NotificationCompat.Builder(this, channelId);

        remoteViews = new RemoteViews(getPackageName(), R.layout.music_notification_layout);

        Intent playPauseIntent = new Intent(this, MusicService.class);
        playPauseIntent.putExtra(EXTRA_COMMAND, COMMAND_PLAY_PAUSE);
        PendingIntent playPausePendingIntent = PendingIntent.getService(this, 0, playPauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notification_play_pause_btn, playPausePendingIntent);
        remoteViews.setImageViewResource(R.id.notification_play_pause_btn, R.drawable.ic_baseline_pause_black_24);

        Intent prevIntent = new Intent(this, MusicService.class);
        prevIntent.putExtra(EXTRA_COMMAND, COMMAND_PREV);
        PendingIntent prevPendingIntent = PendingIntent.getService(this, 1, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notification_prev_btn, prevPendingIntent);

        Intent nextIntent = new Intent(this, MusicService.class);
        nextIntent.putExtra(EXTRA_COMMAND, COMMAND_NEXT);
        PendingIntent nextPendingIntent = PendingIntent.getService(this, 2, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notification_next_btn, nextPendingIntent);

        Intent closeIntent = new Intent(this, MusicService.class);
        closeIntent.putExtra(EXTRA_COMMAND, COMMAND_CLOSE);
        PendingIntent closePendingIntent = PendingIntent.getService(this, 3, closeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.close_btn, closePendingIntent);

        builder.setCustomContentView(remoteViews)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setOnlyAlertOnce(true);

        startForeground(NOTIFICATION_ID, builder.build());

        IntentFilter filter = new IntentFilter(MainPlayerFragment.SONG_BROADCAST_SERVICE_FILTER);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Song song = (Song) intent.getSerializableExtra(MainPlayerFragment.EXTRA_SONG);

                remoteViews.setTextViewText(R.id.notification_song_name_tv, song.getSongName());
                remoteViews.setImageViewResource(R.id.notification_play_pause_btn, R.drawable.ic_baseline_pause_black_24);

                manager.notify(NOTIFICATION_ID, builder.build());
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String command = intent.getStringExtra(EXTRA_COMMAND);

        if (command == null) {
            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }

                remoteViews.setImageViewResource(R.id.notification_play_pause_btn, R.drawable.ic_baseline_pause_black_24);

                mediaPlayer.reset();
                mediaPlayer.setDataSource(MainPlayerFragment.lastPlayedSong.getSongLink());
            } catch (IOException e) {
                e.printStackTrace();
            }

            remoteViews.setTextViewText(R.id.notification_song_name_tv, MainPlayerFragment.lastPlayedSong.getSongName());

            manager.notify(NOTIFICATION_ID, builder.build());

            mediaPlayer.prepareAsync();
        } else if (command.equals(COMMAND_PLAY_PAUSE)) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                remoteViews.setImageViewResource(R.id.notification_play_pause_btn, R.drawable.ic_baseline_play_arrow_black_24);
            } else {
                mediaPlayer.start();
                remoteViews.setImageViewResource(R.id.notification_play_pause_btn, R.drawable.ic_baseline_pause_black_24);
            }

            manager.notify(NOTIFICATION_ID, builder.build());
        } else if (command.equals(COMMAND_PREV)) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }

            playSong(false);
        } else if (command.equals(COMMAND_NEXT)) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }

            playSong(true);
        } else if (command.equals(COMMAND_CLOSE)) {
            stopSelf();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void playSong(boolean isNext) {
        if (isNext) {
            MainPlayerFragment.lastPlayedSongIndex++;
            if (MainPlayerFragment.lastPlayedSongIndex == MainPlayerFragment.songs.size()) {
                MainPlayerFragment.lastPlayedSongIndex = 0;
            }
        } else {
            MainPlayerFragment.lastPlayedSongIndex--;
            if (MainPlayerFragment.lastPlayedSongIndex == -1) {
                MainPlayerFragment.lastPlayedSongIndex = MainPlayerFragment.songs.size() - 1;
            }
        }

        Song song = MainPlayerFragment.songs.get(MainPlayerFragment.lastPlayedSongIndex);
        sendFragmentBroadcast(song);
        sendServiceBroadcast(song);

        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(song.getSongLink());
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.prepareAsync();
    }

    private void sendServiceBroadcast(Song song) {
        Intent broadcastService = new Intent(MainPlayerFragment.SONG_BROADCAST_SERVICE_FILTER);
        broadcastService.putExtra(MainPlayerFragment.EXTRA_SONG, song);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastService);
    }

    private void sendFragmentBroadcast(Song song) {
        Intent broadcastFragment = new Intent(MainPlayerFragment.SONG_BROADCAST_FRAGMENT_FILTER);
        broadcastFragment.putExtra(MainPlayerFragment.EXTRA_SONG, song);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastFragment);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }

        mediaPlayer.release();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        playSong(true);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }
}
