package com.okirat.fastmusic;

/**
 * Created by Oguz Kirat on 2/17/2015.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;

import android.app.PendingIntent;

import java.util.List;
import java.util.concurrent.TimeUnit;

import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.okirat.fastmusic.model.*;

public class MusicService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {
    private static final String TAG = null;
    private static final int NOTIFY_ID = 1337;
    private final IBinder musicBind = new MusicBinder();
    MediaPlayer player;
    AudioManager am;
    boolean userstopped;
    private Resources res;
    private long currentSongID;
    private int currentPos;
    private AudioManager.OnAudioFocusChangeListener amOnAudioFocusChange;
    private List<Song> songList;
    private int seekDuration;
    private boolean shuffle;
    private boolean repeat;
    private boolean prepared = false;
    private NotificationManager notMgr = null;
    /*Headphone unplugged broadcast receiver */
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("android.media.AUDIO_BECOMING_NOISY")) {
                if (isPlaying()) {
                    userstopped = true;
                    stop();
                }
            }

        }
    };

    /*
    Service intilaziton and MediaPlayer preperation
     */
    @Override
    public void onCreate() {
        super.onCreate();
        res = getResources();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.media.AUDIO_BECOMING_NOISY");
        registerReceiver(receiver, filter);
        notMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        seekDuration = 10000;
        player = new MediaPlayer();
        prepareplayer();
        userstopped = true;
        amOnAudioFocusChange = new AudioManager.OnAudioFocusChangeListener() {

            @Override
            public void onAudioFocusChange(int focusChange) {
                switch (focusChange) {
                    case AudioManager.AUDIOFOCUS_GAIN:
                        Log.i(TAG, "AUDIOFOCUS_GAIN");
                        // Set volume level to desired levels
                        if (userstopped == false) {
                            player.start();
                        }
                        break;
                    case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
                        Log.i(TAG, "AUDIOFOCUS_GAIN_TRANSIENT");
                        // You have audio focus for a short time
                        if (!userstopped) {
                            player.start();

                        }
                        break;
                    case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:
                        Log.i(TAG, "AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK");
                        // Play over existing audio
                        if (!userstopped) {
                            player.start();

                        }
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS:
                        Log.e(TAG, "AUDIOFOCUS_LOSS");

                        player.pause();
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        Log.e(TAG, "AUDIOFOCUS_LOSS_TRANSIENT");
                        // Temporary loss of audio focus - expect to get it back - you can keep your resources around

                        player.pause();
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                        Log.e(TAG, "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");

                        player.pause();
                        break;
                }
            }
        };
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);


    }

    public void prepareplayer() {
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnCompletionListener(this);
        player.setOnPreparedListener(this);
    }

    public void onStart(Intent intent, int startId) {
        // TO DO
    }

    //activity will bind to service
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    //release resources when unbind
    @Override
    public boolean onUnbind(Intent intent) {
        player.stop();
        player.release();
        return false;
    }

    public void onStop() {

    }

    public void onPause() {

    }

    @Override
    public void onDestroy() {
        player.stop();
        player.release();
        unregisterReceiver(receiver);
    }

    public void onPrepared(MediaPlayer mp) {
        prepared = true;

        int result = am.requestAudioFocus(amOnAudioFocusChange, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            Log.e("FastMusic", "Cant get audio focus");
        }
        //notification
        Intent notIntent = new Intent(getApplicationContext(), MainActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //stop intent will send extra named action which equals stop to MainActivity, then activity will connect to MusicService and call stop(). This method should stop and release MediaPlayer and also clear notifications.
        Intent stopIntent = new Intent(this, NowPlayingActivity.class);
        stopIntent.putExtra("action", "stop");
        stopIntent.setData(ContentUris.withAppendedId(Uri.EMPTY, 1));
        stopIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
        );

        PendingIntent stopPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        stopIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setContentIntent(pendInt)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(getTitle())


                        .setContentText(res.getString(R.string.by) + " " + getArtist())

                        .setOngoing(true)
                        .setTicker(getTitle());
        Notification not= mBuilder.build();
        not.contentView=new RemoteViews(this.getPackageName(),
                R.layout.notification_songplaying);
        not.contentView.setTextViewText(R.id.infoSongTitle,getTitle());
        not.contentView.setTextViewText(R.id.infoAlbumTitle,getArtist());
        not.contentView.setOnClickPendingIntent(R.id.songinfo,pendInt);

        not.contentView.setOnClickPendingIntent(R.id.songcontrols,stopPendingIntent);
        notMgr.notify(NOTIFY_ID, not);

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.i("MUSIC SERVICE", "Completed!");
        if (player.getCurrentPosition() > 0) {
            if (player.getCurrentPosition() > 0) {
                mp.reset();
                playNext();
            }
        }
    }

    /*
    Player operation
     */
    public void changesong(long id) {
        currentSongID = id;
        startsong();
    }

    public void startsong() {
        try {
            player.reset();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currentSongID);
        //set the data source
        try {
            player.setDataSource(getApplicationContext(), trackUri);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), res.getString(R.string.error_playing_song), Toast.LENGTH_SHORT).show();
        }
        try {
            player.prepare();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), res.getString(R.string.error_initializing), Toast.LENGTH_SHORT).show();
        }
        am.requestAudioFocus(amOnAudioFocusChange, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        player.start();
        userstopped = false;

    }

    public void getSongListAndSong(List<Song> songArrayList, int pos) {
        songList = songArrayList;
        currentPos = pos;
        long id = songArrayList.get(pos).songid;

        changesong(id);
    }

    public void stop() {
        player.stop();

        release();

    }

    public void release() {
        player.release();

        notMgr.cancelAll();
    }

    public void pause() {
        player.pause();
        userstopped = true;
    }

    public void start() {
        if (prepared) {
            player.start();
            userstopped = false;

        } else {
            if (currentSongID != 0) {
                player = new MediaPlayer();
                prepareplayer();
                startsong();
            } else {
                Toast.makeText(getApplicationContext(), res.getString(R.string.no_song_playing), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void forward() {


        if (player.getCurrentPosition() + seekDuration < player.getDuration()) {
            player.seekTo(player.getCurrentPosition() + seekDuration);
        } else {
            playNext();
        }

    }

    public void playNext() {
        if (songList.size() == currentPos + 1) {
            currentPos = 0;

        } else {
            currentPos = currentPos + 1;
        }
        changesong(songList.get(currentPos).songid);

    }

    public void playPrev() {
        if (currentPos - 1 < 0) {
            currentPos = songList.size() - 1;
        } else {
            currentPos = currentPos - 1;
        }
        changesong(songList.get(currentPos).songid);

    }

    public void rewind() {

        if (player.getCurrentPosition() - seekDuration < 0) {
            player.seekTo(0);
        } else {

            player.seekTo(player.getCurrentPosition() - seekDuration);
        }
    }

    public void seek(int to) {
        player.seekTo(to);
    }

    /*
    Get information from player
 */
    public boolean isPlaying() {
        try {
            return player.isPlaying();
        } catch (IllegalStateException e) {
            prepared = false;
            return false;
        }
    }

    public int getCurrentVolume() {
        int currentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        return currentVolume;
    }

    public void setCurrentVolume(int volume) {
        am.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_PLAY_SOUND);
    }

    public int getMaxVolume() {
        return am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    public int currentPosition() {
        return player.getCurrentPosition();
    }

    public int getDuration() {
        return player.getDuration();
    }

    public String getTitle() {
        if (songList.isEmpty()) {
            return "";
        } else {
            return songList.get(currentPos).title;
        }
    }

    public String getHumanReadableDuration(int ms) {
        String n = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(ms),
                TimeUnit.MILLISECONDS.toSeconds(ms) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(ms))
        );
        return n;
    }

    public String getAlbumKey() {
        if (songList.isEmpty()) {
            return "";
        } else {
            return songList.get(currentPos).albumkey;
        }
    }

    public String getAlbum() {
        if (songList.isEmpty()) {
            return "";
        } else {
            return songList.get(currentPos).album;
        }
    }

    public String getArtist() {
        if (songList.isEmpty()) {
            return "";
        } else {
            return songList.get(currentPos).artist;
        }
    }

    public boolean isPrepared() {
        return prepared;
    }

    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }
}
