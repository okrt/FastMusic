package com.teknobolge.android.fastmusic;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {

	private final Handler handler = new Handler();
    private Resources res;
	private PagerSlidingTabStrip tabs;
    private String palbumkey="";
	private ViewPager pager;
	private MyPagerAdapter adapter;
    private ImageView imgvStop;
	private Drawable oldBackground = null;
	private int currentColor = 0xFF666666;
    public ArrayList<String> songList;
    private ListView songView;
    String[] sarkilarlistesi;
    //service
    public MusicService musicSrv;
    private Intent playIntent;
    //binding
    private boolean musicBound=false;

    //controller


    //activity and playback pause flags
    private int playing=2;
    Handler updateViewHandler = new Handler();
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
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
    public void setBarColor(int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);


            getWindow().setStatusBarColor(color);
        }

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
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
    @Override
    public void onStart() {
        super.onStart();
        res=getResources();
        if(playIntent==null){
            playIntent = new Intent(getApplicationContext(), MusicService.class);
            getApplicationContext().bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            getApplicationContext().startService(playIntent);
           // setBarColor(0xFF666666);
        }

    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);
		adapter = new MyPagerAdapter(getSupportFragmentManager());

		pager.setAdapter(adapter);

		final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
				.getDisplayMetrics());
		pager.setPageMargin(pageMargin);

		tabs.setViewPager(pager);
        imgvStop=(ImageView)findViewById(R.id.stop);
		changeColor(currentColor);
        Intent servicestart = new Intent(this, MusicService.class);
        startService(servicestart);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.action_contact:
			QuickContactFragment dialog = new QuickContactFragment();
			dialog.show(getSupportFragmentManager(), "QuickContactFragment");
			return true;

		}

		return super.onOptionsItemSelected(item);
	}
    Runnable run = new Runnable() {

        @Override
        public void run() {
            updateViewFromService();
        }
    };
    public void updateViewFromService(){
        String title=musicSrv.getTitle();

        String info, album,artist;
        if(title.isEmpty()||title.equals(""))
        {
            title=res.getString(R.string.app_name);
            album="";
            artist="";
            info="";
        }
        else
        {
            album=musicSrv.getAlbum();
            artist=musicSrv.getArtist();
            if(title.length()>40)
                title=title.substring(0,40)+"...";
            if(album.length()>20)
                album=album.substring(0,20)+"...";
            if(artist.length()>20)
                artist=artist.substring(0,20)+"...";
            info=album+" "+res.getString(R.string.by)+" "+artist;
        }
        ((TextView)findViewById(R.id.infoSongTitle)).setText(title);
        ((TextView)findViewById(R.id.infoAlbumTitle)).setText(info);
        if(musicSrv.getAlbumKey()!=null&&!palbumkey.equals(musicSrv.getAlbumKey())) {
            try {
                MusicLibrary ml = new MusicLibrary(getApplicationContext());
                String art=ml.getAlbumArt(musicSrv.getAlbumKey());
                if(art!=null&&!art.equals("")) {
                    Bitmap albumart = BitmapFactory.decodeFile(art);
                    ImageView albumimage = (ImageView) findViewById(R.id.albumArt);
                    albumimage.setImageBitmap(albumart);
                    Palette.generateAsync(albumart, new Palette.PaletteAsyncListener() {
                        public void onGenerated(Palette palette) {
                            int renk;
                            renk=palette.getDarkVibrantColor(0xFF666666);

                            if(renk==0xFF666666){
                                renk=palette.getDarkMutedColor(0xFF666666);
                            }
                            if(renk==0xFF666666){
                                renk=palette.getLightMutedColor(0xFF666666);
                            }
                            setBarColor(renk);
                            changeColor(renk);
                        }
                    });

                }
                else{ImageView albumimage = (ImageView) findViewById(R.id.albumArt);
                    albumimage.setImageResource(R.drawable.ic_launcher);}

            } catch (Exception ex) {
                ImageView albumimage = (ImageView) findViewById(R.id.albumArt);
                albumimage.setImageResource(R.drawable.ic_launcher);
            }
            palbumkey=musicSrv.getAlbumKey();
        }
        if(musicSrv.isPlaying()) {
            if(playing!=1)
                imgvStop.setImageResource(R.drawable.stop);
        }
        else{
            if(playing!=0)
                imgvStop.setImageResource(R.drawable.play);
        }
        updateViewHandler.postDelayed(run, 1000);
    }
	public void changeColor(int newColor) {

		tabs.setIndicatorColor(newColor);

		// change ActionBar color just if an ActionBar is available
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

			Drawable colorDrawable = new ColorDrawable(newColor);
			Drawable bottomDrawable = getResources().getDrawable(R.drawable.actionbar_bottom);
			LayerDrawable ld = new LayerDrawable(new Drawable[] { colorDrawable, bottomDrawable });

			if (oldBackground == null) {

				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
					ld.setCallback(drawableCallback);
				} else {
					getActionBar().setBackgroundDrawable(ld);
				}

			} else {

				TransitionDrawable td = new TransitionDrawable(new Drawable[] { oldBackground, ld });

				// workaround for broken ActionBarContainer drawable handling on
				// pre-API 17 builds
				// https://github.com/android/platform_frameworks_base/commit/a7cc06d82e45918c37429a59b14545c6a57db4e4
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
					td.setCallback(drawableCallback);
				} else {
					getActionBar().setBackgroundDrawable(td);
				}

				td.startTransition(200);

			}

			oldBackground = ld;

			// http://stackoverflow.com/questions/11002691/actionbar-setbackgrounddrawable-nulling-background-from-thread-handler
			getActionBar().setDisplayShowTitleEnabled(false);
			getActionBar().setDisplayShowTitleEnabled(true);

		}

		currentColor = newColor;

	}

	public void onColorClicked(View v) {

		int color = Color.parseColor(v.getTag().toString());
		changeColor(color);

	}
    public void onControlClicked(View v) {

        String command = v.getTag().toString();
        if(command.equals("stop")){
            if(musicSrv.isPlaying()) {
                musicSrv.pause();
                imgvStop.setImageResource(R.drawable.play);
                playing=0;
            }
            else{
                    musicSrv.start();
                    if(musicSrv.isPlaying()) {
                        imgvStop.setImageResource(R.drawable.stop);
                        playing = 1;
                    }
            }
        }
        else if(command.equals("rewind")){
            if(musicSrv.isPrepared()) {
                musicSrv.rewind();
            }

        }
        else if(command.equals("forward")){
            if(musicSrv.isPrepared()) {
                musicSrv.forward();
            }
        }
        else if(command.equals("next")){
            if(musicSrv.isPrepared()) {
                musicSrv.playNext();
            }
        }
        else if(command.equals("prev")){
            if(musicSrv.isPrepared()) {
                musicSrv.playPrev();
            }
        }


    }
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("currentColor", currentColor);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		currentColor = savedInstanceState.getInt("currentColor");
		changeColor(currentColor);
	}

	private Drawable.Callback drawableCallback = new Drawable.Callback() {
		@Override
		public void invalidateDrawable(Drawable who) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                getActionBar().setBackgroundDrawable(who);
            }
		}

		@Override
		public void scheduleDrawable(Drawable who, Runnable what, long when) {
			handler.postAtTime(what, when);
		}

		@Override
		public void unscheduleDrawable(Drawable who, Runnable what) {
			handler.removeCallbacks(what);
		}
	};

	public class MyPagerAdapter extends FragmentPagerAdapter {
        Resources res = getResources();
		private final String[] TITLES = { res.getString(R.string.artists), res.getString(R.string.albums), res.getString(R.string.songs) };

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public Fragment getItem(int position) {
			return LibraryFragment.newInstance(position);
		}

	}

}