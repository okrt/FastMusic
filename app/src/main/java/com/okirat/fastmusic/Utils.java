package com.okirat.fastmusic;

/**
 * Created by Tatar on 1.05.2016.
 */

        import android.content.Context;
        import android.content.pm.PackageManager;
        import android.content.res.AssetManager;
        import android.content.res.XmlResourceParser;
        import android.graphics.Bitmap;
        import android.net.Uri;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.text.Editable;
        import android.text.Html;
        import android.text.TextUtils;
        import android.util.DisplayMetrics;
        import android.util.Log;



        import org.xml.sax.XMLReader;
        import org.xmlpull.v1.XmlPullParser;
        import org.xmlpull.v1.XmlPullParserException;

        import java.io.BufferedInputStream;
        import java.io.Closeable;
        import java.io.File;
        import java.io.FileInputStream;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.OutputStream;
        import java.math.BigInteger;
        import java.security.MessageDigest;
        import java.security.NoSuchAlgorithmException;
        import java.security.cert.Certificate;
        import java.security.cert.CertificateEncodingException;
        import java.text.DateFormat;
        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.Date;
        import java.util.Formatter;
        import java.util.Iterator;
        import java.util.List;
        import java.util.Locale;
        import java.util.zip.Adler32;

public final class Utils {

    @SuppressWarnings("UnusedDeclaration")
    private static final String TAG = "Utils";

    private static final int BUFFER_SIZE = 4096;

    // The date format used for storing dates (e.g. lastupdated, added) in the
    // database.
    private static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    private static final SimpleDateFormat TIME_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.ENGLISH);


    private static final String[] ANDROID_VERSION_NAMES = {
            "?",     // 0, undefined
            "1.0",   // 1
            "1.1",   // 2
            "1.5",   // 3
            "1.6",   // 4
            "2.0",   // 5
            "2.0.1", // 6
            "2.1",   // 7
            "2.2",   // 8
            "2.3",   // 9
            "2.3.3", // 10
            "3.0",   // 11
            "3.1",   // 12
            "3.2",   // 13
            "4.0",   // 14
            "4.0.3", // 15
            "4.1",   // 16
            "4.2",   // 17
            "4.3",   // 18
            "4.4",   // 19
            "4.4W",  // 20
            "5.0",   // 21
            "5.1",   // 22
            "6.0",   // 23
    };

    public static String getAndroidVersionName(int sdkLevel) {
        if (sdkLevel < 0) {
            return ANDROID_VERSION_NAMES[0];
        }
        if (sdkLevel >= ANDROID_VERSION_NAMES.length) {
            return String.format(Locale.ENGLISH, "v%d", sdkLevel);
        }
        return ANDROID_VERSION_NAMES[sdkLevel];
    }

    /* PackageManager doesn't give us the min and max sdk versions, so we have
     * to parse it */


    public static Locale getLocaleFromAndroidLangTag(String languageTag) {
        if (TextUtils.isEmpty(languageTag)) {
            return null;
        }

        final String[] parts = languageTag.split("-");
        if (parts.length == 1) {
            return new Locale(parts[0]);
        }
        if (parts.length == 2) {
            String country = parts[1];
            // Some languages have an "r" before the country as per the values folders, such
            // as "zh-rCN". As far as the Locale class is concerned, the "r" is
            // not helpful, and this should be "zh-CN". Thus, we will
            // strip the "r" when found.
            if (country.charAt(0) == 'r' && country.length() == 3) {
                country = country.substring(1);
            }
            return new Locale(parts[0], country);
        }
        Log.e(TAG, "Locale could not be parsed from language tag: " + languageTag);
        return new Locale(languageTag);
    }
    public static String getVersionName(Context context) {
        String versionName = null;
        try {
            versionName = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Could not get client version name", e);
        }
        return versionName;
    }
}

