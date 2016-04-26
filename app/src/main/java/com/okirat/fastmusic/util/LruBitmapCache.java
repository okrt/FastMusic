package com.okirat.fastmusic.util;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.okirat.fastmusic.MusicLibrary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.util.Log;

public class LruBitmapCache extends LruCache<String, Bitmap> implements
        ImageCache {
    public static int getDefaultLruCacheSize() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        return cacheSize;
    }

    public LruBitmapCache() {
        this(getDefaultLruCacheSize());
    }

    public LruBitmapCache(int sizeInKiloBytes) {
        super(sizeInKiloBytes);
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight() / 1024;
    }

    @Override
    public Bitmap getBitmap(String url) {
        Log.i("FastMusic", "Requested: "+url);
        if (url.contains("file://")) {
            if(get(url)==null) {
                //Network Image'lar otomatik olarak cache'leniyor. Ancak local gelen resimler cache'e alınmıyor. Bu durumda her seferinde resmin çözülmesi OutOfMemoryException oluşturuyor.
                //Bunu çözmek için local resimleri bir kez dekode ettikten sonra direkt memory cache'e atıyorum. Ardından aynı local resme istek geldiğinde bellekteki sürümü gönderiyorum.
                //Aksi halde her resim farklıymış gibi tekrar dekode edileceğinden bellek dolacak.
                Log.i("FastMusic", "Detected local content: " + url.substring(url.indexOf("file://") + 7));
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(url.substring(url.indexOf("file://") + 7), options);

                // Calculate inSampleSize
                options.inSampleSize = MusicLibrary.calculateInSampleSize(options, 200, 200);

                // Decode bitmap with inSampleSize set
                options.inJustDecodeBounds = false;
                Bitmap image = BitmapFactory.decodeFile(url.substring(url.indexOf("file://") + 7), options);
                putBitmap(url, image);
            }
            return get(url);

        }
        else {
            return get(url);
        }
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }
}