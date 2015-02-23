package com.teknobolge.android.fastmusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Oguz Kirat on 2/19/2015.
 */
public class AlbumAdapter extends ArrayAdapter<Album> {
    public AlbumAdapter(Context context, ArrayList<Album> albums) {
        super(context, 0, albums);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Album album = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_song1, parent, false);
        }
        // Lookup view for data population
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvSongTitle);
        TextView tvArtist = (TextView) convertView.findViewById(R.id.tvSongArtist);
        TextView tvId= (TextView) convertView.findViewById(R.id.tvSongId);
        // Populate the data into the template view using the data object
        tvTitle.setText(album.albumtitle);
        tvArtist.setText(album.albumartist);
        tvId.setText(""+album.albumkey);
        // Return the completed view to render on screen
        return convertView;
    }
}
