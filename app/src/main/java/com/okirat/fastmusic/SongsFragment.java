package com.okirat.fastmusic;
/**
 * Created by Oguz Kirat on 9/25/2015.
 */

import android.animation.AnimatorSet;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.okirat.fastmusic.adapter.SongRVAdapter;
import com.okirat.fastmusic.model.Song;
import com.okirat.fastmusic.util.PostRVTouchListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SongsFragment extends Fragment {
    View v;
    private static final String TAG ="SongsFragment";
    private int pageno=1;
    public static String URL;
    private ProgressDialog pDialog;
    private Resources res;
    private List<Song> songList = new ArrayList<Song>();
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    private SongRVAdapter songRVAdapter;
    private RelativeLayout yukleniyor;
    Parcelable state;
    AnimatorSet set;
    public MusicService musicSrv;
    private Intent playIntent;
    //binding
    private boolean musicBound=false;
    public SongsFragment() {


    }
    private boolean paused=false, playbackPaused=false;
    Handler updateViewHandler = new Handler();
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicSrv = binder.getService();

            musicBound = true;
            //updateViewFromService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };
    @Override
    public void onStart() {
        super.onStart();
        res=getActivity().getResources();
        if(playIntent==null){
            playIntent = new Intent(getActivity().getApplicationContext(), MusicService.class);
            getActivity().getApplicationContext().bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            getActivity().getApplicationContext().startService(playIntent);
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_library1, container, false);
        yukleniyor=(RelativeLayout)v.findViewById(R.id.loadingPanel);
        yukleniyor.setVisibility(View.INVISIBLE);

        recyclerView = (RecyclerView)v.findViewById(R.id.recycler_view);
        mLayoutManager = new GridLayoutManager(v.getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
       // recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        try {
            Bundle bundle = getArguments();
            String artist = bundle.getString("artist");
            String album = bundle.getString("album");
            Log.i("FastMusic",artist+" :"+album);
            if((artist!=null&&artist!="")&&(album!=null&&album!=""))
            {
                songList=MusicLibrary.getSongList(v.getContext(),artist,album);
                Collections.sort(songList, new Comparator<Song>() {
                    public int compare(Song s1, Song s2) {
                        return s1.trackid.compareToIgnoreCase(s2.trackid);
                    }
                });
                songRVAdapter = new SongRVAdapter(v.getContext(), songList);
            }
            else if(artist!=null&&artist!=""){
                songList= MusicLibrary.getSongList(v.getContext(), artist);
                Collections.sort(songList, new Comparator<Song>() {
                    public int compare(Song s1, Song s2) {
                        return s1.trackid.compareToIgnoreCase(s2.trackid);
                    }
                });
                Collections.sort(songList, new Comparator<Song>() {
                    public int compare(Song s1, Song s2) {
                        return s1.album.compareToIgnoreCase(s2.album);
                    }
                });
                songRVAdapter = new SongRVAdapter(v.getContext(),songList);
            }
            else{

                songList= MusicLibrary.getSongList(v.getContext());
                Collections.sort(songList, new Comparator<Song>() {
                    public int compare(Song s1, Song s2) {
                        return s1.title.compareToIgnoreCase(s2.title);
                    }
                });
                songRVAdapter = new SongRVAdapter(v.getContext(),songList);
            }
        }
        catch (Exception e) {

            songList=MusicLibrary.getSongList(v.getContext());
            Collections.sort(songList, new Comparator<Song>() {
                public int compare(Song s1, Song s2) {
                    return s1.title.compareToIgnoreCase(s2.title);
                }
            });
            songRVAdapter = new SongRVAdapter(v.getContext(), songList);
        }

        recyclerView.setAdapter(songRVAdapter);

       recyclerView.addOnItemTouchListener(new PostRVTouchListener(v.getContext(), recyclerView, new PostRVTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                if (musicBound) {
                    musicSrv.getSongListAndSong(songList, position);
                    Intent intent=new Intent(getActivity().getApplicationContext(), NowPlayingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);

                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        //float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        int columnNumber=(int)Math.floor(dpWidth/200);

            mLayoutManager = new GridLayoutManager(v.getContext(), columnNumber);

        recyclerView.setLayoutManager(mLayoutManager);
/*
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore(int current_page) {

                pageno++;
                getJSONContent(pageno);
            }
        });






        getJSONContent(pageno);
*/
        return v;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
            float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
            //float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
            int columnNumber=(int)Math.floor(dpWidth / 200);
            GridLayoutManager mGridLayoutManager=(GridLayoutManager)recyclerView.getLayoutManager();

            mGridLayoutManager.setSpanCount(columnNumber);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
            float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
            //float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
            int columnNumber=(int)Math.floor(dpWidth / 200);
            GridLayoutManager mGridLayoutManager=(GridLayoutManager)recyclerView.getLayoutManager();

            mGridLayoutManager.setSpanCount(columnNumber);

        }
    }

}