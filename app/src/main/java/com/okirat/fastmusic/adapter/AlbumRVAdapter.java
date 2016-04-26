package com.okirat.fastmusic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.volley.toolbox.ImageLoader;
import com.okirat.fastmusic.AppController;
import com.okirat.fastmusic.R;
import com.okirat.fastmusic.model.Album;

import java.util.List;



/**
 * Created by Oguz Kirat on 9/9/2015.
 */
public class AlbumRVAdapter extends RecyclerView.Adapter<AlbumListRowHolder> {
    private int lastPosition = -1;
    private List<Album> albumList;
    private Context context;
    String aaimgurl;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    AlbumListRowHolder holdert;

    public AlbumRVAdapter(Context context, List<Album> albumList) {
        this.context = context;
        this.albumList = albumList;
    }

    @Override
    public AlbumListRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_album, parent,false);
        AlbumListRowHolder albumListRowHolder = new AlbumListRowHolder(view);
        return albumListRowHolder;
    }

    @Override
    public void onBindViewHolder(AlbumListRowHolder holder, int position) {
        Album album = albumList.get(position);
        String albumarturi="";
        if(imageLoader == null) {
            imageLoader = AppController.getInstance().getImageLoader();
        }
        if(album.albumarturi!=null){
        if(!(album.albumarturi.startsWith("http://"))) {
            albumarturi="file://"+album.albumarturi;


        }else{albumarturi=album.albumarturi;}
            Log.i("FastMusic", "Album art for "+album.albumtitle+": " + albumarturi);
            holder.thumbnail.setImageUrl(albumarturi, imageLoader);
        }
        else{



        }
        holder.title.setText(Html.fromHtml(album.albumtitle));
        holder.excerpt.setText(Html.fromHtml(album.albumartist));
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
        return (null != albumList ? albumList.size() : 0);
    }


}

