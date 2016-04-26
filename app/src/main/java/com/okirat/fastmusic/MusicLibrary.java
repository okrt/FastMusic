package com.okirat.fastmusic;

/**
 * Created by Oguz Kirat on 2/19/2015.
 */
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.okirat.fastmusic.model.*;

public class MusicLibrary {
    public static String aaimgurl;
    public static ArrayList<Song> getSongList(Context context){
        //Get song list from device
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
                long duration = musicCursor.getLong(musicCursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                if(duration>3000) {
                    Song thisSong = new Song(thisTitle, thisArtist, thisAlbum, thisId, thisArtistId, thisAlbumId, thisTrackId);

                    liste.add(thisSong);
                }

            }
            while (musicCursor.moveToNext());
        }
        return(liste);
    }
    public static ArrayList<Song> getSongList(Context context,String artist){
        //Get song list from device
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
                if(artist.equals(thisArtist)) {
                    Song thisSong = new Song(thisTitle, thisArtist, thisAlbum, thisId, thisArtistId, thisAlbumId, thisTrackId);

                    liste.add(thisSong);
                }

            }
            while (musicCursor.moveToNext());
        }
        return(liste);
    }
    public static ArrayList<Song> getSongList(Context context,String artist, String album){
        //Get song list from device
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
                long duration = musicCursor.getLong(musicCursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                if(artist.equals(thisArtist)&&album.equals(thisAlbum)) {
                    Song thisSong = new Song(thisTitle, thisArtist, thisAlbum, thisId, thisArtistId, thisAlbumId, thisTrackId);

                    liste.add(thisSong);
                }

            }
            while (musicCursor.moveToNext());
        }
        return(liste);
    }
    public static List<Album> getAlbumList(Context context){
        // Get album list from device
        List<Album> liste =  new ArrayList<Album>();
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


                Album thisAlbum=new Album(thisId,thisTitle,thisArtist,getAlbumArt(context,thisId));

                liste.add(thisAlbum);


            }
            while (musicCursor.moveToNext());
        }
        return(liste);
    }
    public static List<Album> getAlbumList(Context context, String artist){
        // Get album list from device
        List<Album> liste =  new ArrayList<Album>();
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

                if(thisArtist.equals(artist)) {
                    Album thisAlbum = new Album(thisId, thisTitle, thisArtist, getAlbumArt(context, thisId));

                    liste.add(thisAlbum);

                }
            }
            while (musicCursor.moveToNext());
        }
        return(liste);
    }
    public static String getAlbumArt(Context context,String albumkey){
        // Get cached album art based on album key
        String albumart=null;
        String albumname=null;
        String albumartist=null;
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
                    albumartist=musicCursor.getString(musicCursor.getColumnIndex("ARTIST"));
                    albumname=musicCursor.getString(musicCursor.getColumnIndex("ALBUM"));
                    break;
                }



            }
            while (musicCursor.moveToNext());
        }
        if(albumart==null)
        {
            if(albumartist!=null&&albumname!=null){

                //GET FROM LAST FM
            }

        }
        return albumart;

    }


    public static ArrayList<Artist> getArtistList(Context context){
        //Get artist list from device
        ArrayList<Artist> liste =  new ArrayList<Artist>();
        ContentResolver musicResolver;

        musicResolver =  context.getContentResolver();

        Uri musicUri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;

        Cursor musicCursor = musicResolver.query(musicUri, null,null,null, null);


        //iterate over results if valid
        if(musicCursor!=null && musicCursor.moveToFirst()){

            do {


                String thisId = musicCursor.getString(musicCursor.getColumnIndex("ARTIST_KEY"));
                String thisTitle = musicCursor.getString(musicCursor.getColumnIndex("ARTIST"));
                String thisNOA = musicCursor.getString(musicCursor.getColumnIndex("NUMBER_OF_ALBUMS"));
                String thisNOT = musicCursor.getString(musicCursor.getColumnIndex("NUMBER_OF_TRACKS"));


                Artist thisArtist=new Artist(thisId,thisTitle,thisNOA,thisNOT,"");

                liste.add(thisArtist);


            }
            while (musicCursor.moveToNext());
        }
        return(liste);
    }

    public static ArrayList<Artist> getArtistListFromSongs(Context context){
        ArrayList<Artist> liste = new ArrayList<Artist>();
        ArrayList<Song> allsongs = getSongList(context);
        for (Song item : allsongs) {
            Artist thisartist=new Artist(item.artistkey,item.artist,"-","-","");
            if(!liste.contains(thisartist))
            {
                liste.add(thisartist);
            }
        }

        return liste;
    }
    public static ArrayList<Album> getAlbumListFromSongs(Context context){
        ArrayList<Album> liste =  new ArrayList<Album>();
        ArrayList<Song> allsongs=getSongList(context);
        for (Song item : allsongs) {
            Album thisalbum=new Album(item.albumkey,item.album, item.artist, getAlbumArt(context,item.albumkey));
            if(!liste.contains(thisalbum))
            {
                liste.add(thisalbum);
            }
        }

        return liste;
    }
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
    public static int getMax(int a, int b) {
        return (a>=b?a:b);
    }
}
