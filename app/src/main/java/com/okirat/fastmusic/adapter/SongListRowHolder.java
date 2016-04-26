package com.okirat.fastmusic.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.okirat.fastmusic.R;

/**
 * Created by Oguz Kirat on 9/9/2015.
 */
public class SongListRowHolder extends RecyclerView.ViewHolder {

    protected NetworkImageView thumbnail;
    protected CardView cv;
    protected TextView title, album, artist, author, postid;

    public SongListRowHolder(View itemView) {
        super(itemView);
        this.cv = (CardView)itemView.findViewById(R.id.cv);
        this.thumbnail = (NetworkImageView)itemView.findViewById(R.id.thumbnail);
        this.thumbnail.setDefaultImageResId(R.drawable.cover_logo);
        this.thumbnail.setErrorImageResId(R.drawable.cover_logo);
        this.title = (TextView) itemView.findViewById(R.id.title);

       // this.author= (TextView) itemView.findViewById(R.id.author);
        this.artist = (TextView) itemView.findViewById(R.id.artist);
        this.album = (TextView) itemView.findViewById(R.id.album);
        this.postid= (TextView) itemView.findViewById(R.id.postid);
    }
}
