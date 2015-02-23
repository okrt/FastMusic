package com.teknobolge.android.fastmusic;

/**
 * Created by Oguz Kirat on 2/19/2015.
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import android.content.Context;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.content.ContentResolver;
import android.database.Cursor;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import android.net.Uri;
import android.database.Cursor;
public class MusicLibrary {
    final Context context;
    public MusicLibrary(final Context context1) {
        context=context1;
    }
    public ArrayList<Song> getSongList(){
        //şarkıları sorgula
        ArrayList<Song> liste =  new ArrayList<Song>();
        ContentResolver musicResolver;

        musicResolver =  context.getContentResolver();

        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if(musicCursor!=null && musicCursor.moveToFirst()){
            do {


                long thisId = musicCursor.getLong(musicCursor.getColumnIndex("_ID"));
                String thisTitle =  musicCursor.getString(musicCursor.getColumnIndex("TITLE"));
                String thisArtist = musicCursor.getString(musicCursor.getColumnIndex("ARTIST"));
                String thisAlbum = musicCursor.getString(musicCursor.getColumnIndex("ALBUM"));
                String thisArtistId = musicCursor.getString(musicCursor.getColumnIndex("ARTIST_KEY"));
                String thisAlbumId = musicCursor.getString(musicCursor.getColumnIndex("ALBUM_KEY"));
                String thisTrackId = musicCursor.getString(musicCursor.getColumnIndex("TRACK"));
                Song thisSong=new Song(thisTitle,thisArtist, thisAlbum,thisId, thisArtistId, thisAlbumId,thisTrackId);

                liste.add(thisSong);


            }
            while (musicCursor.moveToNext());
        }
        return(liste);
    }
    public ArrayList<Album> getAlbumList(){
        //şarkıları sorgula
        ArrayList<Album> liste =  new ArrayList<Album>();
        ContentResolver musicResolver;

        musicResolver =  context.getContentResolver();

        Uri musicUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;

        Cursor musicCursor = musicResolver.query(musicUri,null,null,null, null);


        //iterate over results if valid
        if(musicCursor!=null && musicCursor.moveToFirst()){

            do {


                    String thisId = musicCursor.getString(musicCursor.getColumnIndex("ALBUM_KEY"));
                    String thisTitle = musicCursor.getString(musicCursor.getColumnIndex("ALBUM"));
                    String thisArtist = musicCursor.getString(musicCursor.getColumnIndex("ARTIST"));


                Album thisAlbum=new Album(thisId,thisTitle,thisArtist);

                liste.add(thisAlbum);


            }
            while (musicCursor.moveToNext());
        }
        return(liste);
    }
    public String getAlbumArt(String albumkey){
        //şarkıları sorgula
        String albumart=null;
        ContentResolver musicResolver;

        musicResolver =  context.getContentResolver();

        Uri musicUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;

        Cursor musicCursor = musicResolver.query(musicUri,null,null,null, null);


        //iterate over results if valid
        if(musicCursor!=null && musicCursor.moveToFirst()){

            do {


                String thisId = musicCursor.getString(musicCursor.getColumnIndex("ALBUM_KEY"));
                if(thisId.equals(albumkey)){
                    albumart=musicCursor.getString(musicCursor.getColumnIndex("ALBUM_ART"));
                   break;
                }



            }
            while (musicCursor.moveToNext());
        }
        return albumart;

    }
    public ArrayList<Artist> getArtistList(){
        //şarkıları sorgula
        ArrayList<Artist> liste =  new ArrayList<Artist>();
        ContentResolver musicResolver;

        musicResolver =  context.getContentResolver();

        Uri musicUri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;

        Cursor musicCursor = musicResolver.query(musicUri,null,null,null, null);


        //iterate over results if valid
        if(musicCursor!=null && musicCursor.moveToFirst()){

            do {


                String thisId = musicCursor.getString(musicCursor.getColumnIndex("ARTIST_KEY"));
                String thisTitle = musicCursor.getString(musicCursor.getColumnIndex("ARTIST"));
                String thisNOA = musicCursor.getString(musicCursor.getColumnIndex("NUMBER_OF_ALBUMS"));
                String thisNOT = musicCursor.getString(musicCursor.getColumnIndex("NUMBER_OF_TRACKS"));


                Artist thisArtist=new Artist(thisId,thisTitle,thisNOA,thisNOT);

                liste.add(thisArtist);


            }
            while (musicCursor.moveToNext());
        }
        return(liste);
    }
}