package com.teknobolge.android.fastmusic;

/**
 * Created by Oguz Kirat on 2/19/2015.
 */
public class Album {
    public String albumkey;
    public String albumtitle;
    public String albumartist;

    public Album(String albumkey, String albumtitle, String albumartist) {

        this.albumkey=albumkey;
        this.albumtitle=albumtitle;
        this.albumartist=albumartist;

    }
    @Override
    public boolean equals (Object object) {
        boolean result = false;
        if (object == null) {
            result = false;
        } else {
            Album a = (Album) object;
            if (this.albumkey.equals(a.albumkey)) {
                result = true;
            }
        }
        return result;
    }
}
