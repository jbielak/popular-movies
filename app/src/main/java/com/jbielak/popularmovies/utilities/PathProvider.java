package com.jbielak.popularmovies.utilities;

import android.net.Uri;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Justyna on 2018-02-26.
 */

public abstract class PathProvider {

    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String POSTER_SIZE = "w185";

    private static final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie";
    private static final String API_KEY_PARAM = "api_key";
    private static final String API_KEY = ""; // put your API key here

    public static URL getMoviesUrl(SortType sortType) {
        Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(sortType.getValue())
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch(MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL getPosterUrl(String posterPath) {
        Uri builtUri = Uri.parse(POSTER_BASE_URL).buildUpon()
                .appendPath(POSTER_SIZE)
                .appendPath(posterPath)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch(MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
}
