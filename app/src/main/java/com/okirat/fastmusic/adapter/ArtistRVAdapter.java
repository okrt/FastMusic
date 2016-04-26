package com.okirat.fastmusic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.volley.toolbox.ImageLoader;
import com.okirat.fastmusic.AppController;
import com.okirat.fastmusic.R;
import com.okirat.fastmusic.model.Artist;

import java.util.List;


/**
 * Created by Oguz Kirat on 9/9/2015.
 */
public class ArtistRVAdapter extends RecyclerView.Adapter<ArtistListRowHolder> {
    private int lastPosition = -1;
    private List<Artist> artistsList;
    private Context context;
    String aaimgurl;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    public ArtistRVAdapter(Context context, List<Artist> artistList) {
        this.context = context;
        this.artistsList = artistList;
    }

    @Override
    public ArtistListRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_artist, parent,false);
        ArtistListRowHolder artistListRowHolder = new ArtistListRowHolder(view);
        return artistListRowHolder;
    }

    @Override
    public void onBindViewHolder(ArtistListRowHolder holder, int position) {

        Artist artist = artistsList.get(position);

        holder.title.setText(Html.fromHtml(artist.artisttitle));
        holder.excerpt.setText(Html.fromHtml(artist.numberoftracks+" şarkı, "+artist.numberofalbums+" albüm"));
        if (position>lastPosition && position>3) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.down_from_top);
            holder.cv.startAnimation(animation);
            lastPosition = position;
        }
        /*Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.up_from_bottom
                        : R.anim.down_from_top);
                        */



    }

    @Override
    public int getItemCount() {
        return (null != artistsList ? artistsList.size() : 0);
    }


}

