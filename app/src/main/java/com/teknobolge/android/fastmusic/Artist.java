package com.teknobolge.android.fastmusic;

/**
 * Created by Oguz Kirat on 2/20/2015.
 */
public class Artist {
    public String artistkey;
    public String artisttitle;
    public String albumartist;
    public String numberofalbums;
    public String numberoftracks;

    public Artist(String artistkey, String artisttitle,String numberofalbums, String numberoftracks) {

        this.artistkey=artistkey;
        this.artisttitle=artisttitle;

        this.numberofalbums=numberofalbums;
        this.numberoftracks=numberoftracks;

    }
    @Override
    public boolean equals (Object object) {
        boolean result = false;
        if (object == null) {
            result = false;
        } else {
            Artist a = (Artist) object;
            if (this.artistkey.equals(a.artistkey)) {
                result = true;
            }
        }
        return result;
    }
}