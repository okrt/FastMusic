package com.okirat.fastmusic;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.okirat.fastmusic.util.LruBitmapCache;

public class NowPlayingActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    private ImageView imgvStop;
    GestureDetector gdt;
    CardView cvsongi;
    CardView cvart;
    CardView cvcontrols;
    private Intent intent;
    int playing = 2;
    TextView tvSongTitle;
    TextView tvSongInfo;
    TextView tvSeekerInfo;
    private Resources res;
    public MusicService musicSrv;
    private Intent playIntent;
    private Bitmap albumart;
    SquareImageView albumimage;
    SeekBar seeksong;
    SeekBar seekvolume;

    private String palbumkey = "";
    boolean donotrefresh = false;
    LruBitmapCache imagecache;
    RelativeLayout rl;

    //binding
    private boolean musicBound = false;

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {

        if (fromUser) {
            if (seekBar.equals(seeksong)) {
                musicSrv.seek(progress);
                seeksong.setProgress(progress);
            }
            if (seekBar.equals(seekvolume)) {
                musicSrv.setCurrentVolume(progress);
            }
        }
    }

    Handler updateViewHandler = new Handler();
    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            //get service
            musicSrv = binder.getService();

            musicBound = true;
            updateViewFromService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };


    @Override
    public void onStart() {
        super.onStart();
        res = getResources();
        if (playIntent == null) {
            playIntent = new Intent(getApplicationContext(), MusicService.class);
            getApplicationContext().bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            getApplicationContext().startService(playIntent);

        }

    }

    public void recycleAlbumArt() {
        try {
            ((BitmapDrawable) albumimage.getDrawable()).getBitmap().recycle();
            albumimage.setImageDrawable(null);
            albumart.recycle();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        donotrefresh = true;


    }

    @Override
    public void onResume() {
        super.onResume();
        palbumkey = "";
        donotrefresh = false;
        if (musicBound) {
            updateViewFromService();
        }
        // recycleAlbumArt();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        donotrefresh = true;


    }

    @Override
    public void onStop() {
        super.onStop();
        donotrefresh = true;


    }

    protected void initlayout() {
        setContentView(R.layout.activity_now_playing);
        try {

            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        } catch (Exception ex) {
            Log.i("FastMusic", ex.toString());
        }
        rl = (RelativeLayout) findViewById(R.id.rl);
        albumimage = (SquareImageView) findViewById(R.id.imvAlbumArt);
        imgvStop = (ImageView) findViewById(R.id.stop);
        seeksong = (SeekBar) findViewById(R.id.seeksong);
        seeksong.setOnSeekBarChangeListener(this);
        seekvolume = (SeekBar) findViewById(R.id.seekvolume);
        seekvolume.setOnSeekBarChangeListener(this);
        cvsongi = (CardView) findViewById(R.id.cvsongi);
        cvart = (CardView) findViewById(R.id.cvart);
        cvcontrols = (CardView) findViewById(R.id.cvcontrols);
        tvSongTitle = (TextView) findViewById(R.id.infoSongTitle);
        tvSongInfo = (TextView) findViewById(R.id.infoAlbumTitle);
        tvSeekerInfo = (TextView) findViewById(R.id.tvseekerinfo);
        imagecache = new LruBitmapCache();



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        try {
            getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } catch (Exception ex) {
            Log.i("FastMusic", ex.toString());
        }
        initlayout();

    }

    Runnable run = new Runnable() {

        @Override
        public void run() {
            updateViewFromService();
        }
    };

    public void updateViewFromService() {
        if (musicSrv.isPlaying()) {
            seekvolume.setMax(musicSrv.getMaxVolume());
            seekvolume.setProgress(musicSrv.getCurrentVolume());
            seeksong.setMax(musicSrv.getDuration());
            seeksong.setProgress(musicSrv.currentPosition());
            tvSeekerInfo.setText(musicSrv.getHumanReadableDuration(musicSrv.currentPosition()) + " / " + musicSrv.getHumanReadableDuration(musicSrv.getDuration()));
            String title = musicSrv.getTitle();

            String info, album, artist;
            if (title.isEmpty() || title.equals("")) {
                title = res.getString(R.string.app_name);
                album = "";
                artist = "";
                info = "";
            } else {
                album = musicSrv.getAlbum();
                artist = musicSrv.getArtist();
                info = album + " " + res.getString(R.string.by) + " " + artist;
            }

            tvSongTitle.setText(title);
            tvSongInfo.setText(album + "\n" + artist);
        }

/*
Album Art İşlemleri
 */
        try {
            if (musicSrv.getAlbumKey() != null && !palbumkey.equals(musicSrv.getAlbumKey())) {

                try {

                    String art = MusicLibrary.getAlbumArt(getApplicationContext(), musicSrv.getAlbumKey());
                    if (art != null && !art.equals("")) {
                        albumart = imagecache.getBitmap("file://" + art);


                        albumimage.setImageBitmap(albumart);
                        Palette.generateAsync(albumart, new Palette.PaletteAsyncListener() {
                            public void onGenerated(Palette palette) {
                                int koyurenk;
                                koyurenk = palette.getDarkVibrantColor(0x000000);

                                if (koyurenk == 0x000000) {
                                    koyurenk = palette.getDarkMutedColor(0x000000);
                                }

                                if (koyurenk == 0x000000) {
                                    koyurenk = palette.getLightMutedColor(0xFF666666);
                                }
                                int acikrenk;
                                acikrenk = palette.getLightMutedColor(0xFFFFFF);
                                if (acikrenk == koyurenk) {
                                    acikrenk = palette.getLightVibrantColor(0XFFFFFF);
                                }



                                cvsongi.setCardBackgroundColor(koyurenk);
                                cvcontrols.setCardBackgroundColor(koyurenk);
                                cvart.setCardBackgroundColor(koyurenk);
                                String tcolor = "#80" + String.format("%06X", 0xFFFFFF & koyurenk);
                                //setBarColor(0xFFFFFF00);
                                //rl.setBackgroundColor(acikrenk);

                            }
                        });


                    } else {
                        final BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.cover_logo, options);
                        options.inSampleSize = MusicLibrary.calculateInSampleSize(options, 150, 150);
                        options.inJustDecodeBounds = false;
                        albumart = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.cover_logo, options);


                    }

                } catch (Exception ex) {
                    Log.i("FastMusic", ex.toString());
                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.cover_logo, options);
                    options.inSampleSize = MusicLibrary.calculateInSampleSize(options, 150, 150);
                    options.inJustDecodeBounds = false;
                    albumart = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.cover_logo, options);
                    albumimage.setImageBitmap(albumart);
                }
                palbumkey = musicSrv.getAlbumKey();
            }

            if (musicSrv.isPlaying()) {
                if (playing != 1)
                    imgvStop.setImageResource(R.drawable.stop);
            } else {
                if (playing != 0)
                    imgvStop.setImageResource(R.drawable.play);
            }
            if (!donotrefresh) {
                updateViewHandler.postDelayed(run, 1000);
            }
        } catch (Exception ex) {
        }
    }


    public void onControlClicked(View v) {

        String command = v.getTag().toString();
        if (command.equals("stop")) {
            if (musicSrv.isPlaying()) {
                musicSrv.pause();
                imgvStop.setImageResource(R.drawable.play);
                playing = 0;
            } else {
                musicSrv.start();
                if (musicSrv.isPlaying()) {
                    imgvStop.setImageResource(R.drawable.stop);
                    playing = 1;
                }
            }
        } else if (command.equals("rewind")) {
            if (musicSrv.isPrepared()) {
                musicSrv.rewind();
            }

        } else if (command.equals("forward")) {
            if (musicSrv.isPrepared()) {
                musicSrv.forward();
            }
        } else if (command.equals("next")) {
            if (musicSrv.isPrepared()) {
                musicSrv.playNext();
            }
        } else if (command.equals("prev")) {
            if (musicSrv.isPrepared()) {
                musicSrv.playPrev();
            }
        }


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //albumimage.getLayoutParams().width=albumimage.getWidth()/2;
            palbumkey = "reinit";
            initlayout();
        } else {
            //albumimage.getLayoutParams().width=albumimage.getWidth()*2;
            palbumkey = "reinit";
            initlayout();
        }
    }

    public void setBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);


            getWindow().setStatusBarColor(color);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setTintColor(color);
            tintManager.setStatusBarTintEnabled(true);

            tintManager.setNavigationBarTintEnabled(true);

        }

    }





}
