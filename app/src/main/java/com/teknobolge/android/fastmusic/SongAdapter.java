package com.teknobolge.android.fastmusic;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
/**
 * Created by Oguz Kirat on 2/17/2015.
 */
public class SongAdapter extends ArrayAdapter<Song> {
    public SongAdapter(Context context, ArrayList<Song> songs) {
        super(context, 0, songs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Song song = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_song1, parent, false);
        }
        // Lookup view for data population
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvSongTitle);
        TextView tvArtist = (TextView) convertView.findViewById(R.id.tvSongArtist);
        TextView tvId= (TextView) convertView.findViewById(R.id.tvSongId);
        // Populate the data into the template view using the data object
        tvTitle.setText(song.title);
        tvArtist.setText(song.artist);
        tvId.setText(""+song.songid);
        // Return the completed view to render on screen
        return convertView;
    }
}