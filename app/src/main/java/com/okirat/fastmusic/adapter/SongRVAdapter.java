package com.okirat.fastmusic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.okirat.fastmusic.AppController;
import com.okirat.fastmusic.MusicLibrary;
import com.okirat.fastmusic.R;
import com.okirat.fastmusic.model.Song;

import java.util.List;


/**
 * Created by Oguz Kirat on 9/9/2015.
 */
public class SongRVAdapter extends RecyclerView.Adapter<SongListRowHolder> {
    private int lastPosition = -1;
    private List<Song> songList;
    private Context context;
    String aaimgurl;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    SongListRowHolder holdert;

    public SongRVAdapter(Context context, List<Song> songList) {
        this.context = context;
        this.songList = songList;
    }

    @Override
    public SongListRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_song, parent,false);
        SongListRowHolder songListRowHolder = new SongListRowHolder(view);
        return songListRowHolder;
    }

    @Override
    public void onBindViewHolder(SongListRowHolder holder, int position) {
        Song song = songList.get(position);
        String albumarturi="";
       if(imageLoader == null) {
            imageLoader = AppController.getInstance().getImageLoader();
        }
        String albumart;
        albumart=MusicLibrary.getAlbumArt(context,song.albumkey);
        if(albumart!=null){
        if(!(albumart.startsWith("http://"))) {
            albumarturi="file://"+albumart;


        }else{albumarturi=albumart;}
            Log.i("FastMusic", "Album art for "+song.title+": " + albumarturi);
            holder.thumbnail.setImageUrl(albumarturi, imageLoader);
        }
        else{




        }

        holder.title.setText(Html.fromHtml(song.title));
        holder.artist.setText(Html.fromHtml(song.artist));
        holder.album.setText(Html.fromHtml(song.album));





    }

    @Override
    public int getItemCount() {
        return (null != songList ? songList.size() : 0);
    }


}

