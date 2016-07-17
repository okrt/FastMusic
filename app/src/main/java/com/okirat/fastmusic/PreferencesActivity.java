package com.okirat.fastmusic;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewFragment;
import java.util.List;

/**
 * Created by Tatar on 15.05.2016.
 */
public class PreferencesActivity extends PreferenceActivity {
    public void onBuildHeaders(List<Header> target)
    {
        loadHeadersFromResource(R.xml.preference_headers, target);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
    public static class AboutFragment extends WebViewFragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            WebView view = (WebView)super.onCreateView(inflater, container, savedInstanceState);
            view.getSettings().setJavaScriptEnabled(true);
            TypedValue value = new TypedValue();
            getActivity().getTheme().resolveAttribute(R.attr.overlay_foreground_color, value, true);
            String fontColor = TypedValue.coerceToString(value.type, value.data);
            view.loadUrl("file:///android_asset/about.html?"+ Uri.encode(fontColor));
            view.setBackgroundColor(Color.TRANSPARENT);
            return view;
        }
    }
}
