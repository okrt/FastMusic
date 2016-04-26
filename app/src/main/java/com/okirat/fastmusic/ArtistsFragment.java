package com.okirat.fastmusic;
/**
 * Created by Oguz Kirat on 9/25/2015.
 */

import android.animation.AnimatorSet;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.okirat.fastmusic.adapter.ArtistRVAdapter;
import com.okirat.fastmusic.model.Artist;
import com.okirat.fastmusic.util.PostRVTouchListener;

import java.util.ArrayList;
import java.util.List;

public class ArtistsFragment extends Fragment {
    View v;
    private static final String TAG ="ArtistsFragment";
    private int pageno=1;
    public static String URL;
    private ProgressDialog pDialog;

    private List<Artist> artistList = new ArrayList<Artist>();
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    private ArtistRVAdapter artistRVAdapter;
    private RelativeLayout yukleniyor;
    Parcelable state;
    AnimatorSet set;

    public ArtistsFragment() {


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
        artistList=MusicLibrary.getArtistList(v.getContext());
        artistRVAdapter = new ArtistRVAdapter(v.getContext(),artistList);

        recyclerView.setAdapter(artistRVAdapter);

       recyclerView.addOnItemTouchListener(new PostRVTouchListener(v.getContext(), recyclerView, new PostRVTouchListener.ClickListener() {
           @Override
           public void onClick(View view, int position) {
               //String selectedpostid = postList.get(position).getId();
               String selectedartist=artistList.get(position).artisttitle;
               Intent i = new Intent(getContext(), NoTabsLibrary.class);
               i.putExtra("artist", selectedartist);
               startActivity(i);

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