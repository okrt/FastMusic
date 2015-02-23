package com.teknobolge.android.fastmusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Oguz Kirat on 2/20/2015.
 */
public class ArtistAdapter extends ArrayAdapter<Artist> {
    public ArtistAdapter(Context context, ArrayList<Artist> artists) {
        super(context, 0, artists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Artist artist= getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_song1, parent, false);
        }
        // Lookup view for data population
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvSongTitle);
        TextView tvArtist = (TextView) convertView.findViewById(R.id.tvSongArtist);
        TextView tvId= (TextView) convertView.findViewById(R.id.tvSongId);
        // Populate the data into the template view using the data object
        tvTitle.setText(artist.artisttitle);
        String psalbum;
        String pstrack;
        if(artist.numberofalbums.equals("1"))
        {
            psalbum="album";
        }
        else{
            psalbum="albums";
        }
        if(artist.numberoftracks.equals("1"))
        {
            pstrack="track";
        }
        else{
            pstrack="tracks";
        }
        tvArtist.setText(artist.numberofalbums+" "+psalbum+", "+artist.numberoftracks+" "+pstrack);
        tvId.setText(artist.artistkey);
        // Return the completed view to render on screen
        return convertView;
    }
}