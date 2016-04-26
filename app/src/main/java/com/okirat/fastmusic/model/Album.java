package com.okirat.fastmusic.model;

/**
 * Created by Oguz Kirat on 2/19/2015.
 */
public class Album {
    public String albumkey;
    public String albumtitle;
    public String albumartist;
    public String albumarturi;

    public Album(String albumkey, String albumtitle, String albumartist, String albumarturi) {

        this.albumkey=albumkey;
        this.albumtitle=albumtitle;
        this.albumartist=albumartist;
        this.albumarturi=albumarturi;

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
